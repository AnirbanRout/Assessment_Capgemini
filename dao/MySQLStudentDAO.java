
package dao;

import model.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import exception.InvalidStudentDataException;

public class MySQLStudentDAO implements StudentDAO {

    private Connection getConnection() throws SQLException {

        Connection con = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/jdbc_demo",
                "postgres",
                "lime12");

        createTableIfNotExists(con);

        return con;
    }

    private void createTableIfNotExists(Connection con) throws SQLException {

        String sql = """
                CREATE TABLE IF NOT EXISTS student (
                    id INT PRIMARY KEY,
                    name VARCHAR(50) NOT NULL,
                    email VARCHAR(100) NOT NULL,
                    age INT CHECK (age > 0),
                    mobile VARCHAR(10) UNIQUE
                )
                """;

        try (Statement st = con.createStatement()) {
            st.execute(sql);
        }
    }

    @Override
    public void addStudent(Student s) throws SQLException {

        String sql = "INSERT INTO student VALUES (?, ?, ?, ?, ?)";

        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, s.getId());
            ps.setString(2, s.getName());
            ps.setString(3, s.getEmail());
            ps.setInt(4, s.getAge());
            ps.setString(5, s.getMobile());

            ps.executeUpdate();
        }
    }

    @Override
    public List<Student> viewAllStudents() throws SQLException, InvalidStudentDataException {

        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM student";

        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Student s = new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getInt("age"),
                        rs.getString("mobile"));
                list.add(s);
            }
        }
        return list;
    }

    @Override
    public void updateStudent(long mobile, String email) throws SQLException {

        String sql = "UPDATE student SET email=? WHERE mobile=?";

        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setLong(2, mobile);
            ps.executeUpdate();
        }
    }

    @Override
    public void deleteStudent(int id) throws SQLException {

        String sql = "DELETE FROM student WHERE id=?";

        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}

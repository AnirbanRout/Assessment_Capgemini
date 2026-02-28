
package dao;

import model.Student;
import java.sql.SQLException;
import java.util.List;

import exception.InvalidStudentDataException;

public interface StudentDAO {

    void addStudent(Student student) throws SQLException, InvalidStudentDataException;

    List<Student> viewAllStudents() throws SQLException, InvalidStudentDataException;

    void updateStudent(long num, String email) throws SQLException, InvalidStudentDataException;

    void deleteStudent(int id) throws SQLException, InvalidStudentDataException;
}


package Project;

import dao.*;
import exception.InvalidStudentDataException;
import model.Student;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class StudentManagementSystem {

    public static void main(String[] args) throws InvalidStudentDataException {

        Scanner sc = new Scanner(System.in);

        StudentDAO dao = new MySQLStudentDAO();

        while (true) {
            System.out.println("\n1.Add 2.View 3.Update Email 4.Delete 5.Exit");
            int choice = -1;

            try {
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number (1-5)!");
                sc.nextLine();
                continue;
            }

            sc.nextLine();

            switch (choice) {

                case 1:
                    try {
                        Student s = new Student();

                        System.out.print("ID: ");
                        s.setId(sc.nextInt());
                        sc.nextLine();

                        System.out.print("Name: ");
                        s.setName(sc.nextLine());

                        System.out.print("Email: ");
                        s.setEmail(sc.nextLine());

                        System.out.print("Age: ");
                        s.setAge(sc.nextInt());
                        sc.nextLine();

                        System.out.print("Mobile: ");
                        s.setMobile(sc.nextLine());

                        dao.addStudent(s);
                        System.out.println("Student added successfully.");

                    } catch (InvalidStudentDataException e) {
                        System.out.println("Validation Error: " + e.getMessage());
                        sc.nextLine();
                    } catch (SQLException e) {
                        System.out.println("Database Error: " + e.getMessage());
                    } catch (InputMismatchException e) {
                        System.out.println("Please enter numbers where required.");
                        sc.nextLine();
                    }
                    break;

                case 2:
                    try {
                        List<Student> list = dao.viewAllStudents();
                        if (list.isEmpty()) {
                            System.out.println("No students found.");
                        } else {
                            list.forEach(st -> System.out.println(
                                    st.getId() + " | " + st.getName() + " | " +
                                            st.getEmail() + " | " + st.getAge() + " | " +
                                            st.getMobile()));
                        }
                    } catch (SQLException e) {
                        System.out.println("Database Error: " + e.getMessage());
                    }
                    break;

                case 3:
                    try {
                        System.out.print("Enter mobile number of student to update email: ");
                        String mobileStr = sc.nextLine();

                        if (!mobileStr.matches("\\d{10}")) {
                            throw new InvalidStudentDataException("Mobile must be exactly 10 digits.");
                        }
                        long mobile = Long.parseLong(mobileStr);

                        System.out.print("Enter new email: ");
                        String newEmail = sc.nextLine();

                        if (!newEmail.contains("@")) {
                            throw new InvalidStudentDataException("Invalid email. Must contain '@'.");
                        }

                        dao.updateStudent(mobile, newEmail);
                        System.out.println("Email updated successfully.");

                    } catch (InvalidStudentDataException e) {
                        System.out.println("Validation Error: " + e.getMessage());
                    } catch (SQLException e) {
                        System.out.println("Database Error: " + e.getMessage());
                    } catch (NumberFormatException e) {
                        System.out.println("Mobile number must be numeric.");
                    }
                    break;

                case 4:
                    try {
                        System.out.print("Enter ID to delete: ");
                        int delId = sc.nextInt();
                        sc.nextLine();

                        if (delId <= 0) {
                            throw new InvalidStudentDataException("ID must be positive.");
                        }

                        dao.deleteStudent(delId);
                        System.out.println("Student deleted successfully.");

                    } catch (InvalidStudentDataException e) {
                        System.out.println("Validation Error: " + e.getMessage());
                        sc.nextLine();
                    } catch (SQLException e) {
                        System.out.println("Database Error: " + e.getMessage());
                    } catch (InputMismatchException e) {
                        System.out.println("Please enter a valid number.");
                        sc.nextLine();
                    }
                    break;

                case 5:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Enter 1-5.");
            }

            sc.close();
        }
    }
}

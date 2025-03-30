package com.example;

import java.util.List;

public interface StudentDAO {
    void addStudent(Student student);
    Student getStudentByStdNo(String stdNo);
    List<Student> getAllStudents();
    void updateStudent(Student student);
    void deleteStudent(String stdNo);
    boolean authenticateStudent(String stdNo, String password);
    String insertCourseRegistration(String stdNo, String courseID, int semesterID);
} 
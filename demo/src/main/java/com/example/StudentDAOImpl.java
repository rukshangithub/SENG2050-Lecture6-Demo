package com.example;

import java.sql.PreparedStatement;
import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.w3c.dom.Document;


public class StudentDAOImpl implements StudentDAO {

    private static DataSource datasource;

    static {

        try {
            // Reading database configuration parameters    
            File file = new File("databaseConfig.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            String jdbc = document.getElementsByTagName("jdbcDriver").item(0).getTextContent();
            String databaseURL = document.getElementsByTagName("databaseURL").item(0).getTextContent();
            String usr = document.getElementsByTagName("user").item(0).getTextContent();
            String pwd = document.getElementsByTagName("password").item(0).getTextContent();
     
            // Setting the connection pool properties
            PoolProperties p = new PoolProperties();
            p.setUrl(databaseURL);
            p.setDriverClassName(jdbc);
            p.setUsername(usr);
            p.setPassword(pwd);
            // You can set additional pool properties
            p.setMaxActive(100); // Maximum number of connections in the pool
    
            // Setting the data source with the pool properties defined above
            datasource = new DataSource();
            datasource.setPoolProperties(p);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
      

    }

    @Override
    public void addStudent(Student student) {
        String sql = "INSERT INTO student (stdNO, givenNames, lastName, passwordHash, passwordSalt) VALUES (?, ?, ?, ?, ?)";
      
        try (Connection conn = datasource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, student.getStdNo());
            stmt.setString(2, student.getGivenNames());
            stmt.setString(3, student.getLastName());
            stmt.setString(4, student.getPasswordHash());
            stmt.setDouble(5, student.getSalt());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Student getStudentByStdNo(String stdNo) {
        String sql = "SELECT * FROM student WHERE stdNo = ?";
        try (Connection conn = datasource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, stdNo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Student(rs.getString("stdNo"), rs.getString("givenNames"), 
                    rs.getString("lastName"), rs.getString("passwordHash"), rs.getDouble("passwordSalt"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM student";
        try (Connection conn = datasource.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
             
            while (rs.next()) {
                students.add(new Student(rs.getString("stdNo"), rs.getString("givenNames"), 
                        rs.getString("lastName"), rs.getString("passwordHash"), rs.getDouble("passwordSalt")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }


    @Override
    public void updateStudent(Student student) {
        String sql = "UPDATE student SET givenNames = ?, lastName = ? WHERE stdNo = ?";
        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, student.getGivenNames());
            stmt.setString(2, student.getLastName());
            stmt.setString(3, student.getStdNo());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteStudent(String stdNo) {
        String sql = "DELETE FROM student WHERE stdNo = ?";
        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, stdNo);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean authenticateStudent(String stdNo, String password)
    {
        boolean result = false;
        String sql = "{? = call authenticateStudent(?, ?)}";
 
        try (Connection conn = datasource.getConnection();
            CallableStatement stmt = conn.prepareCall(sql)) {
    
            /* Register the output parameter */
            stmt.registerOutParameter(1, Types.BOOLEAN);

            /* Register the input parameters */
            stmt.setString(2, stdNo);
            stmt.setString(3, password);

            /* Execute the function */
            stmt.execute();

            /* Get the output parameter */
            result = stmt.getBoolean(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String insertCourseRegistration(String stdNo, String courseID, int semesterID)
    {
        String sql = "INSERT INTO studentCourseRegistration(stdNo, courseID, semesterID) VALUES (?, ?, ?);";
        try (Connection conn = datasource.getConnection();
            CallableStatement stmt = conn.prepareCall(sql)) {

            /* Register the parameters */
            stmt.setString(1, stdNo);
            stmt.setString(2, courseID);
            stmt.setInt(3, semesterID);

            /* Execute the statement */
            stmt.execute();
            
        } catch (SQLException e) {
                return e.getMessage();
        }
        return "Success";
    }
}




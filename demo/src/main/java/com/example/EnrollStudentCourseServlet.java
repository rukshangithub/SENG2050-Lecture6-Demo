package com.example;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class EnrollStudentCourseServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        response.setContentType("text/html");

        // Retrieve the 'name' parameter from the GET request
        String stdNo = request.getParameter("studentNo");
        String courseID = request.getParameter("courseID");
        String semesterID = request.getParameter("semesterID");

               try {       
            // Enrolling a student
            StudentService stdServervice = new StudentService();
            String returnMessage = stdServervice.enrollStudentCourse(stdNo, courseID, Integer.parseInt(semesterID));
            if (returnMessage.equals("Success"))
            {

                String message = new String("Student " + stdNo + " was enrolled in " + courseID + "in semester id " + semesterID);
                request.setAttribute("Message", message);
                request.setAttribute("returnURL", "enrollStudentCourse.html");

                RequestDispatcher dispatcher = request.getRequestDispatcher("success.jsp");
                dispatcher.forward(request, response);  

            } else
            {
                request.setAttribute("errorMessage", returnMessage);
                request.setAttribute("returnURL", "enrollStudentCourse.html");

                // Forward to error.jsp
                RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
                dispatcher.forward(request, response);  
            }


        } catch (Exception e)
        {
            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("returnURL", "enrollStudentCourse.html");

            // Forward to error.jsp
            RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
            dispatcher.forward(request, response);          
        }

    }
    
    
}

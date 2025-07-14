package com.idiot.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/editScreen")
public class EditScreenServlet extends HttpServlet {
    private static final String query = "SELECT BOOKNAME, BOOKEDITION, BOOKPRICE FROM BOOKDATA WHERE ID=?";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter pw = res.getWriter();
        res.setContentType("text/html");

        // Show the form to enter the Book ID
        pw.println("<h2>Please Enter Book ID to Edit</h2>");
        pw.println("<form action='editScreen' method='get'>");
        pw.println("<table align='center'>");
        pw.println("<tr>");
        pw.println("<td>Book ID:</td>");
        pw.println("<td><input type='text' name='id' required></td>");
        pw.println("</tr>");
        pw.println("<tr>");
        pw.println("<td><input type='submit' value='Find Book'></td>");
        pw.println("</tr>");
        pw.println("</table>");
        pw.println("</form>");

        // If an ID was provided, fetch the book details and show the edit form
        String idStr = req.getParameter("id");
        if (idStr != null && !idStr.trim().isEmpty()) {
            int id = 0;
            try {
                id = Integer.parseInt(idStr);
            } catch (NumberFormatException e) {
                pw.println("<h2>Invalid ID format!</h2>");
                return;
            }

            // Load JDBC driver
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException cnf) {
                cnf.printStackTrace();
                pw.println("<h2>Error loading JDBC driver!</h2>");
                return;
            }

            // Generate the connection
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/book", "root", "");
                 PreparedStatement ps = con.prepareStatement(query)) {

                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();

                // Check if record exists
                if (rs.next()) {
                    pw.println("<h2>Edit Book Details</h2>");
                    pw.println("<form action='edit?id=" + id + "' method='post'>");
                    pw.println("<table align='center'>");
                    pw.println("<tr>");
                    pw.println("<td>Book Edition</td>");
                    pw.println("<td><input type='text' name='bookEdition' value='" + rs.getString(2) + "' required></td>");
                    pw.println("</tr>");
                    pw.println("<tr>");
                    pw.println("<td>Book Price</td>");
                    pw.println("<td><input type='text' name='bookPrice' value='" + rs.getFloat(3) + "' required></td>");
                    pw.println("</tr>");
                    pw.println("<tr>");
                    pw.println("<td><input type='submit' value='Edit'></td>");
                    pw.println("<td><a href='bookList'>Cancel</a></td>");  // Cancel button
                    pw.println("</tr>");
                    pw.println("</table>");
                    pw.println("</form>");
                } else {
                    pw.println("<h2>No record found with the provided ID!</h2>");
                }

            } catch (SQLException se) {
                se.printStackTrace();
                pw.println("<h1>Error: " + se.getMessage() + "</h1>");
            } catch (Exception e) {
                e.printStackTrace();
                pw.println("<h1>Error: " + e.getMessage() + "</h1>");
            }
        }
        // Links to other pages
        pw.println("<br><a href='home.html'>Home</a>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doGet(req, res);
    }
}

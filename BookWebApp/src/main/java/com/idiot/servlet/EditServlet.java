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

@WebServlet("/edit")
public class EditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String query = "UPDATE BOOKDATA SET BOOKNAME=?, BOOKEDITION=?, BOOKPRICE=? WHERE ID=?";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter pw = res.getWriter();
        res.setContentType("text/html");

        // Get the ID parameter from the request
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            pw.println("<h2>Invalid ID provided!</h2>");
            return;
        }

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
             PreparedStatement ps = con.prepareStatement("SELECT BOOKNAME, BOOKEDITION, BOOKPRICE FROM BOOKDATA WHERE ID=?")) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            // Check if record exists
            if (rs.next()) {
                pw.println("<h2>Edit Book Details</h2>");
                pw.println("<form action='edit' method='post'>");
                pw.println("<input type='hidden' name='id' value='" + id + "'/>");
                pw.println("<table align='center'>");
                
                // Book Name is now editable
                pw.println("<tr>");
                pw.println("<td>Book Name:</td>");
                pw.println("<td><input type='text' name='bookName' value='" + rs.getString(1) + "' required></td>");
                pw.println("</tr>");

                pw.println("<tr>");
                pw.println("<td>Book Edition:</td>");
                pw.println("<td><input type='text' name='bookEdition' value='" + rs.getString(2) + "' required></td>");
                pw.println("</tr>");

                pw.println("<tr>");
                pw.println("<td>Book Price:</td>");
                pw.println("<td><input type='text' name='bookPrice' value='" + rs.getFloat(3) + "' required></td>");
                pw.println("</tr>");

                pw.println("<tr>");
                pw.println("<td><input type='submit' value='Edit'></td>");
                pw.println("<td><a href='bookList'>Cancel</a></td>");
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

        // Links to other pages
        pw.println("<br><a href='home.html'>Home</a>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter pw = res.getWriter();
        res.setContentType("text/html");

        // Retrieve values from the form
        int id = Integer.parseInt(req.getParameter("id"));
        String bookName = req.getParameter("bookName");
        String bookEdition = req.getParameter("bookEdition");
        String bookPriceStr = req.getParameter("bookPrice");

        float bookPrice = 0;
        if (bookPriceStr != null && !bookPriceStr.trim().isEmpty()) {
            try {
                bookPrice = Float.parseFloat(bookPriceStr.trim());
            } catch (NumberFormatException e) {
                pw.println("<h2>Invalid price format!</h2>");
                return; // Stop further processing
            }
        }

        // Load JDBC driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException cnf) {
            cnf.printStackTrace();
            pw.println("<h2>Error loading JDBC driver!</h2>");
            return;
        }

        // Update the book details
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/book", "root", "");
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, bookName);
            ps.setString(2, bookEdition);
            ps.setFloat(3, bookPrice);
            ps.setInt(4, id);

            int count = ps.executeUpdate();
            if (count == 1) {
                pw.println("<h2>Record Updated Successfully!</h2>");
            } else {
                pw.println("<h2>Record Not Updated Successfully!</h2>");
            }

        } catch (SQLException se) {
            se.printStackTrace();
            pw.println("<h1>Error: " + se.getMessage() + "</h1>");
        } catch (Exception e) {
            e.printStackTrace();
            pw.println("<h1>Error: " + e.getMessage() + "</h1>");
        }

        // Links to other pages
        pw.println("<br><a href='home.html'>Home</a>");
    }
}

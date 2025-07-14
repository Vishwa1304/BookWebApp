package com.idiot.servlet;

import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class BookListServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set response content type
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();

        // DB connection details
        String url = "jdbc:mysql://localhost:3306/book";
        String user = "root";
        String password = "";

        // Correct SQL query (using correct column names)
        String query = "SELECT * FROM BOOKDATA";

        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to DB and execute query
            try (Connection con = DriverManager.getConnection(url, user, password);
                 PreparedStatement ps = con.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {

                pw.println("<html><body>");
                pw.println("<h2 align='center'>Book List</h2>");
                pw.println("<table border='1' align='center' cellpadding='10'>");
                pw.println("<tr>");
                pw.println("<th>Book ID</th>");
                pw.println("<th>Name</th>");
                pw.println("<th>Edition</th>");
                pw.println("<th>Price</th>");
                pw.println("<th>Edit</th>");
                pw.println("<th>Delete</th>");
                pw.println("</tr>");

                while (rs.next()) {
                    int id = rs.getInt("ID");  // Correct column name from your table
                    String name = rs.getString("BOOKNAME");  // Correct column name from your table
                    String edition = rs.getString("BOOKEDITION");  // Correct column name from your table
                    double price = rs.getDouble("BOOKPRICE");  // Correct column name from your table

                    pw.println("<tr>");
                    pw.println("<td>" + id + "</td>");
                    pw.println("<td>" + name + "</td>");
                    pw.println("<td>" + edition + "</td>");
                    pw.println("<td>" + price + "</td>");
                    pw.println("<td><a href='edit?id=" + id + "'>Edit</a></td>");
                    pw.println("<td><a href='delete?id=" + id + "'>Delete</a></td>");
                    pw.println("</tr>");
                }

                pw.println("</table>");
                pw.println("</body></html>");

            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace(pw);
        } finally {
            pw.close();
        }
    }
}

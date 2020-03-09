package servlets;

import classes.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Attempts to create a new bank account, and serves a page informing the user whether or not it was successful.
 */
@WebServlet({"/BankAccountDeleted"})
public class BankAccountDeleted extends HttpServlet {

  /**
   * Respond to the user request.
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    // Set response parameters
    response.setContentType("text/HTML");
    PrintWriter out = response.getWriter();

    // Get request data from the HttpServletRequest
    String accName = request.getParameter("accountName");
    HttpSession session = request.getSession();
    User CurrentUser = (User)session.getAttribute("UserData");

    // Attempt to remove user's bank account.
    boolean AccountRemoved = CurrentUser.RemoveAccount(accName);

    // Point the "UserData" attribute for this session to that of the CurrentUser object.
    session.setAttribute("UserData", CurrentUser);

    /**
     * Write the pertinent HTML response to the PrintWriter
     */
    out.println("<html>");
    out.println("<head>");
    out.println("<meta http-equiv='pragma'  content='no-cache'>");
    out.println("<meta http-equiv='cache-control'  content='no-cache'>");
    out.println("<meta http-equiv='expires'  content='0'>");
    out.println("<title>Delete Account</title>");
    out.println("</head>");
    out.println("<body>");
    out.println("<br>");

     // Display the corresponding message depending on whether or not the account has been deleted.
    if (AccountRemoved) {
      out.println("<h1>Account " + accName + " has been deleted</h1>");
    } else {
      out.println("<h1>Account " + accName + " could not be deleted as it has a non-zero balance</h1>");
    }

    out.println("<br><br>");
    out.println("<a href='HomePage'>Return to Homepage</a>");
    out.println("</body>");
    out.println("</html>");
    out.flush();

  }

   /**
   * Performs the same function as doGet().
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request, response);
  }

}

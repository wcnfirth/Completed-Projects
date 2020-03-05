package servlets;

import classes.BankAccountType;
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
@WebServlet({"/BankAccountCreated"})
public class BankAccountCreated extends HttpServlet {

  /**
   * Respond to the user request.
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    // Set response parameters
    response.setContentType("text/HTML");
    PrintWriter out = response.getWriter();

    // Get request data from the HttpServletRequest
    String type = request.getParameter("accountType");
    String name = request.getParameter("name");
    float balance = Float.parseFloat(request.getParameter("balance"));
    HttpSession session = request.getSession();

    // Get prevalent data for the current user.
    User CurrentUser = (User)session.getAttribute("UserData");
    BankAccountType accType = BankAccountType.StringToAccountType(type);
    boolean duplicateAccName = CurrentUser.AccountNameExists(name);

    // Check if an account already exists under the requested account name, and if not then create a new account under that name.
    if (!duplicateAccName)
      CurrentUser.AddAccount(accType, name, balance);

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
    out.println("<title>Create Account</title>");
    out.println("</head>");
    out.println("<body>");
    out.println("<br>");

    // Display the corresponding message depending on whether or not the account has been created.
    if (duplicateAccName) {
      out.println("<h1>" + type + "Account " + name + " could not be created as you already have an account by that name. Please choose a unique account name.</h1>");
    } else {
      out.println("<h1>" + type + "Account " + name + " has been successfully created.</h1>");
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

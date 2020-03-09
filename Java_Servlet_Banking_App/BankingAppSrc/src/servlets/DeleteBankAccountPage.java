package servlets;

import classes.BankAccount;
import classes.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Serve page that allows user to delete an existing bank account, if the current balance is $0.00.
 */
@WebServlet({"/DeleteBankAccountPage"})
public class DeleteBankAccountPage extends HttpServlet {

  /**
   * Respond to the user request.
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    // Set response parameters
    response.setContentType("text/HTML");
    PrintWriter out = response.getWriter();

    // Get request data from the HttpServletRequest
    HttpSession session = request.getSession();
    User CurrentUser = (User)session.getAttribute("UserData");

    /**
     * Write the pertinent HTML response to the PrintWriter
     */
    out.println("<html>");
    out.println("<head>");
    out.println("<meta http-equiv='pragma'  content='no-cache'>");
    out.println("<meta http-equiv='cache-control'  content='no-cache'>");
    out.println("<meta http-equiv='expires'  content='0'>");
    out.println("<title>Create An Account</title>");
    out.println("</head>");
    out.println("<body>");
    out.println("<br>");
    out.println("<h1>Choose an account to delete.</h1><br>");
    out.println("<h2>Note: Only accounts with a balance of $0.00 can be deleted.</h2><br><br>");
    out.println("<form method='post' action='BankAccountDeleted'>");
    out.println("<table>");
    out.println("<tr>");
    out.println("<td><b>Account</b></td>");
    out.println("</tr>");
    out.println("<tr>");
    out.println("<td><select name='accountName' required>");

    // Retrieve all CheckingAccount data for the current user (if there is any), and only give the user the option
    // to delete the existing account(s) if there is no current balance.
    ArrayList<BankAccount> CheckingAccounts = CurrentUser.getCheckingAccounts();
    for (BankAccount acc : CheckingAccounts) {
      if (acc.getBalance() <= 0.0F)
        out.println("<option value='" + acc.getName() + "'>(Checking) - " + acc.getName() + "</option>");
    }
    // Retrieve all SavingsAccount data for the current user (if there is any), and only give the user the option
    // to delete the existing account(s) if there is no current balance.
    ArrayList<BankAccount> SavingsAccounts = CurrentUser.getSavingsAccounts();
    for (BankAccount acc : SavingsAccounts) {
      if (acc.getBalance() <= 0.0F)
        out.println("<option value='" + acc.getName() + "'>(Savings) - " + acc.getName() + "</option>");
    }
    // Retrieve all BrokerageAccount data for the current user (if there is any), and only give the user the option
    // to delete the existing account(s) if there is no current balance.
    ArrayList<BankAccount> BrokerageAccounts = CurrentUser.getBrokerageAccounts();
    for (BankAccount acc : BrokerageAccounts) {
      if (acc.getBalance() <= 0.0F)
        out.println("<option value='" + acc.getName() + "'>(Brokerage) - " + acc.getName() + "</option>");
    }

    out.println("</select></td>");
    out.println("<td><input type='submit' value='Delete'></td>");
    out.println("</tr>");
    out.println("</table>");
    out.println("</form>");
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

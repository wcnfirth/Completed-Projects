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
 * Serves the balances page based on information from the classes User and BankAccount.
 */
@WebServlet({"/BalancesPage"})
public class BalancesPage extends HttpServlet {

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
    out.println("<title>Balances</title>");
    out.println("</head>");
    out.println("<body>");
    out.println("<br>");
    out.println("<h1>Account Balances</h1><br>");
    out.println("<h3>Total Balance: $" + CurrentUser.getTotalBalance() + "</h3><br>");
    out.println("<table>");
    out.println("<tr>");
    out.println("<td><b>Account Type</b></td>");
    out.println("<td><b>Name</b></td>");
    out.println("<td><b>Balance</b></td>");
    out.println("</tr><br>");

    // Retrieve the CheckingAccount data for the current user (if there is any).
    ArrayList<BankAccount> CheckingAccounts = CurrentUser.getCheckingAccounts();
    for (BankAccount acc : CheckingAccounts) {
      out.println("<tr>");
      out.println("<td>CHECKING</td>");
      out.println("<td>" + acc.getName() + "</td>");
      out.println("<td>" + acc.getBalance() + "</td>");
      out.println("</tr><br>");
    }

    // Retrieve the SavingsAccount data for the current user (if there is any).
    ArrayList<BankAccount> SavingsAccounts = CurrentUser.getSavingsAccounts();
    for (BankAccount acc : SavingsAccounts) {
      out.println("<tr>");
      out.println("<td>SAVINGS</td>");
      out.println("<td>" + acc.getName() + "</td>");
      out.println("<td>" + acc.getBalance() + "</td>");
      out.println("</tr><br>");
    }

    // Retrieve the BrokerageAccount data for the current user (if there is any).
    ArrayList<BankAccount> BrokerageAccounts = CurrentUser.getBrokerageAccounts();
    for (BankAccount acc : BrokerageAccounts) {
      out.println("<tr>");
      out.println("<td>BROKERAGE</td>");
      out.println("<td>" + acc.getName() + "</td>");
      out.println("<td>" + acc.getBalance() + "</td>");
      out.println("</tr><br>");
    }

    out.println("</table>");
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

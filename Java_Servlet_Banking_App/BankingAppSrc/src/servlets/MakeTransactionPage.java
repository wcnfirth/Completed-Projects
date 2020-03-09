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
 * Serves the transaction page based on information from the classes User and BankAccount.
 */
@WebServlet({"/MakeTransactionPage"})
public class MakeTransactionPage extends HttpServlet {

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
    out.println("<title>Make a Deposit or Withdrawal</title>");
    out.println("</head>");
    out.println("<body>");
    out.println("<br>");
    out.println("<h1>Choose a transaction type, and input the amount of money you would like to deposit/withdrawal.</h1><br><br>");
    out.println("<form method='post' action='TransactionCompleted'>");
    out.println("<table>");
    out.println("<tr>");
    out.println("<td><b>Transaction Type</b></td>");
    out.println("<td><b>Account</b></td>");
    out.println("<td><b>Amount</b></td>");
    out.println("</tr>");
    out.println("<tr>");
    out.println("<td><select name='transactionType'>");
    out.println("<option value='Deposit'>Deposit</option>");
    out.println("<option value='Withdrawal'>Withdrawal</option>");
    out.println("</select></td>");
    out.println("<td><select name = 'accountName' required>");

    // Retrieve the CheckingAccount data for the current user (if there is any).
    ArrayList<BankAccount> CheckingAccounts = CurrentUser.getCheckingAccounts();
    for (BankAccount acc : CheckingAccounts)
      out.println("<option value='" + acc.getName() + "'>(C - $" + acc.getBalance() + ") " + acc.getName() + "</option>");
    // Retrieve the SavingsAccount data for the current user (if there is any).
    ArrayList<BankAccount> SavingsAccounts = CurrentUser.getSavingsAccounts();
    for (BankAccount acc : SavingsAccounts)
      out.println("<option value='" + acc.getName() + "'>(S - $" + acc.getBalance() + ") " + acc.getName() + "</option>");
    // Retrieve the BrokerageAccount data for the current user (if there is any).
    ArrayList<BankAccount> BrokerageAccounts = CurrentUser.getBrokerageAccounts();
    for (BankAccount acc : BrokerageAccounts)
      out.println("<option value='" + acc.getName() + "'>(B - $" + acc.getBalance() + ") " + acc.getName() + "</option>");

    out.println("</select></td>");
    out.println("<td><input type='number' name='transactionAmount' min='0.01' step='0.01' max='9999' required /></td>");
    out.println("<td><input type='submit' value='Submit'></td>");
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

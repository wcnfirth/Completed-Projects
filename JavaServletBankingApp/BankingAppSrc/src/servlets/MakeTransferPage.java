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

@WebServlet({"/MakeTransferPage"})
public class MakeTransferPage extends HttpServlet {
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/HTML");
    PrintWriter out = response.getWriter();
    HttpSession session = request.getSession();
    User CurrentUser = (User)session.getAttribute("UserData");
    out.println("<html>");
    out.println("<head>");
    out.println("<meta http-equiv='pragma'  content='no-cache'>");
    out.println("<meta http-equiv='cache-control'  content='no-cache'>");
    out.println("<meta http-equiv='expires'  content='0'>");
    out.println("<title>Make a Transfer</title>");
    out.println("</head>");
    out.println("<body>");
    out.println("<br>");
    out.println("<h1>Choose the accounts you would like to make a transfer between, and the amount to transfer.</h1><br><br>");
    out.println("<form method='post' action='TransferCompleted'>");
    out.println("<table>");
    out.println("<tr>");
    out.println("<td><b>From</b></td>");
    out.println("<td><b>To</b></td>");
    out.println("<td><b>Transfer Amount</b></td>");
    out.println("</tr>");
    out.println("<tr>");
    out.println("<td><select name='transferFrom'>");
    ArrayList<BankAccount> CheckingAccounts = CurrentUser.getCheckingAccounts();
    for (BankAccount acc : CheckingAccounts) {
      if (acc.getBalance() > 0.0F)
        out.println("<option value='" + acc.getName() + "'>(C - $" + acc.getBalance() + ") " + acc.getName() + "</option>"); 
    } 
    ArrayList<BankAccount> SavingsAccounts = CurrentUser.getSavingsAccounts();
    for (BankAccount acc : SavingsAccounts) {
      if (acc.getBalance() > 0.0F)
        out.println("<option value='" + acc.getName() + "'>(S - $" + acc.getBalance() + ") " + acc.getName() + "</option>"); 
    } 
    ArrayList<BankAccount> BrokerageAccounts = CurrentUser.getBrokerageAccounts();
    for (BankAccount acc : BrokerageAccounts) {
      if (acc.getBalance() > 0.0F)
        out.println("<option value='" + acc.getName() + "'>(B - $" + acc.getBalance() + ") " + acc.getName() + "</option>"); 
    } 
    out.println("</select></td>");
    out.println("<td><select name = 'transferTo'>");
    for (BankAccount acc : CheckingAccounts)
      out.println("<option value='" + acc.getName() + "'>(C - $" + acc.getBalance() + ") " + acc.getName() + "</option>"); 
    for (BankAccount acc : SavingsAccounts)
      out.println("<option value='" + acc.getName() + "'>(S - $" + acc.getBalance() + ") " + acc.getName() + "</option>"); 
    for (BankAccount acc : BrokerageAccounts)
      out.println("<option value='" + acc.getName() + "'>B - $" + acc.getBalance() + ") " + acc.getName() + "</option>"); 
    out.println("</select></td>");
    out.println("<td><input type='number' name='transferAmount' min='0.01' step='0.01' max='9999' required /></td>");
    out.println("<td><input type='submit' value='Submit'></td>");
    out.println("</tr>");
    out.println("</table>");
    out.println("</form>");
    out.println("</body>");
    out.println("</html>");
    out.flush();
  }
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request, response);
  }
}

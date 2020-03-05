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

@WebServlet({"/DeleteBankAccountPage"})
public class DeleteBankAccountPage extends HttpServlet {
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
    ArrayList<BankAccount> CheckingAccounts = CurrentUser.getCheckingAccounts();
    for (BankAccount acc : CheckingAccounts) {
      if (acc.getBalance() <= 0.0F)
        out.println("<option value='" + acc.getName() + "'>(Checking) - " + acc.getName() + "</option>"); 
    } 
    ArrayList<BankAccount> SavingsAccounts = CurrentUser.getSavingsAccounts();
    for (BankAccount acc : SavingsAccounts) {
      if (acc.getBalance() <= 0.0F)
        out.println("<option value='" + acc.getName() + "'>(Savings) - " + acc.getName() + "</option>"); 
    } 
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
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request, response);
  }
}

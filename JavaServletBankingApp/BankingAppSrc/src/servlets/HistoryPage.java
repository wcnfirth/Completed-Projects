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

@WebServlet({"/HistoryPage"})
public class HistoryPage extends HttpServlet {
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
    out.println("<title>Select Account</title>");
    out.println("</head>");
    out.println("<body>");
    out.println("<br>");
    out.println("<h1>Choose an account and submit to view its history.</h1><br><br>");
    out.println("<form method='post' action='ViewTransactionHistory'>");
    out.println("<table>");
    out.println("<tr>");
    out.println("<td>Account: <select name = 'accountName' required>");
    ArrayList<BankAccount> CheckingAccounts = CurrentUser.getCheckingAccounts();
    for (BankAccount acc : CheckingAccounts)
      out.println("<option value='" + acc.getName() + "'>(C - $" + acc.getBalance() + ") " + acc.getName() + "</option>"); 
    ArrayList<BankAccount> SavingsAccounts = CurrentUser.getSavingsAccounts();
    for (BankAccount acc : SavingsAccounts)
      out.println("<option value='" + acc.getName() + "'>(S - $" + acc.getBalance() + ") " + acc.getName() + "</option>"); 
    ArrayList<BankAccount> BrokerageAccounts = CurrentUser.getBrokerageAccounts();
    for (BankAccount acc : BrokerageAccounts)
      out.println("<option value='" + acc.getName() + "'>(B - $" + acc.getBalance() + ") " + acc.getName() + "</option>"); 
    out.println("</select></td>");
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

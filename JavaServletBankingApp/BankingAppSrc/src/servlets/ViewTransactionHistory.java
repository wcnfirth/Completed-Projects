package servlets;

import classes.BankAccount;
import classes.Transaction;
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

@WebServlet({"/ViewTransactionHistory"})
public class ViewTransactionHistory extends HttpServlet {
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/HTML");
    PrintWriter out = response.getWriter();
    HttpSession session = request.getSession();
    String AccName = request.getParameter("accountName");
    BankAccount ThisBankAccount = ((User)session.getAttribute("UserData")).getAccount(AccName);
    ArrayList<Transaction> Transactions = ThisBankAccount.getAllTransactions();
    out.println("<html>");
    out.println("<head>");
    out.println("<meta http-equiv='pragma'  content='no-cache'>");
    out.println("<meta http-equiv='cache-control'  content='no-cache'>");
    out.println("<meta http-equiv='expires'  content='0'>");
    out.println("<title>View Transaction History</title>");
    out.println("</head>");
    out.println("<body>");
    out.println("<br>");
    out.println("<h1>Transaction History for Bank Account " + AccName + ".</h1><br><br>");
    out.println("<a href='HomePage'>Return to HomePage</a><br>");
    out.println("<table>");
    out.println("<tr>");
    out.println("<td><b>Transaction Type</b></td>");
    out.println("<td><b>Transaction Amount</b></td>");
    out.println("<td><b>Resulting Balance</b></td>");
    out.println("</tr><br>");
    for (Transaction t : Transactions) {
      out.println("<tr>");
      out.println("<td>" + t.getType().toString().toUpperCase() + "</td>");
      out.println("<td>" + t.getAmount() + "</td>");
      out.println("<td>" + t.getResultingBalance() + "</td>");
      out.println("</tr><br>");
    } 
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

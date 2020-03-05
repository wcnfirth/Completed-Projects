package servlets;

import classes.TransactionType;
import classes.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet({"/TransactionCompleted"})
public class TransactionCompleted extends HttpServlet {
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/HTML");
    PrintWriter out = response.getWriter();
    TransactionType transactionType = TransactionType.StringToTransactionType(request.getParameter("transactionType"));
    String accountName = request.getParameter("accountName");
    String strTransactionAmount = request.getParameter("transactionAmount");
    float transactionAmount = Float.parseFloat(strTransactionAmount);
    System.out.println("Float TransactionAmount: " + transactionAmount);
    System.out.println("");
    HttpSession session = request.getSession();
    User CurrentUser = (User)session.getAttribute("UserData");
    boolean TransactionSuccessful = CurrentUser.getAccount(accountName).MakeTransaction(transactionAmount, transactionType);
    session.setAttribute("UserData", CurrentUser);
    out.println("<html>");
    out.println("<head>");
    out.println("<meta http-equiv='pragma'  content='no-cache'>");
    out.println("<meta http-equiv='cache-control'  content='no-cache'>");
    out.println("<meta http-equiv='expires'  content='0'>");
    out.println("<title>Transaction</title>");
    out.println("</head>");
    out.println("<body>");
    out.println("<br>");
    if (TransactionSuccessful) {
      String strTransactionType = (transactionType == TransactionType.DEPOSIT) ? "deposited into" : "withdrawn from";
      out.println("<h1>$" + strTransactionAmount + " has been successfully " + strTransactionType + " account " + accountName + ".</h1>");
    } else {
      out.println("<h1>Transaction not completed - requested withdraw amount $" + strTransactionAmount + " is larger than the funds available in account " + accountName + ".</h1>");
    } 
    out.println("<br><br>");
    out.println("<a href='HomePage'>Return to Homepage</a>");
    out.println("</body>");
    out.println("</html>");
    out.flush();
  }
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request, response);
  }
}

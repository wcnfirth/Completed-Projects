package servlets;

import classes.User;
import classes.UserDB;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet({"/HomePage"})
public class HomePage extends HttpServlet {
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/HTML");
    PrintWriter out = response.getWriter();
    String name = request.getParameter("UserName");
    HttpSession session = request.getSession();
    UserDB Users = (UserDB)session.getAttribute("UserDB");
    ServletContext servletContext = getServletContext();
    String contextPath = servletContext.getRealPath(File.separator);
    if (session.getAttribute("UserData") == null) {
      User CurrentUser = Users.getUser(name);
      session.setAttribute("UserData", CurrentUser);
    } 
    if (name == null)
      name = ((User)session.getAttribute("UserData")).getUserName(); 
    Users.BackupUserDatabase(contextPath);
    out.println("<html>");
    out.println("<head>");
    out.println("<meta http-equiv='pragma'  content='no-cache'>");
    out.println("<meta http-equiv='cache-control'  content='no-cache'>");
    out.println("<meta http-equiv='expires'  content='0'>");
    out.println("<title>Home</title>");
    out.println("</head>");
    out.println("<body>");
    out.println("<br>");
    out.println("<h1>Welcome, " + name + "!</h1><br><br>");
    out.println("<a href='HistoryPage'>View Bank Account Histories</a><br>");
    out.println("<a href='BalancesPage'>View Bank Account Balances</a><br>");
    out.println("<a href='MakeTransferPage'>Make a Transfer</a><br>");
    out.println("<a href='MakeTransactionPage'>Make a Deposit/Withdrawal</a><br>");
    out.println("<a href='CreateBankAccountPage'>Create a Bank Account</a><br>");
    out.println("<a href='DeleteBankAccountPage'>Delete a Bank Account</a><br><br>");
    out.println("<a href='index.html'>Log Out</a><br>");
    out.println("</body>");
    out.println("</html>");
    out.flush();
  }
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request, response);
  }
}

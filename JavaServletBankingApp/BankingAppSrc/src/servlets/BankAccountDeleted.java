package servlets;

import classes.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet({"/BankAccountDeleted"})
public class BankAccountDeleted extends HttpServlet {
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/HTML");
    PrintWriter out = response.getWriter();
    String accName = request.getParameter("accountName");
    HttpSession session = request.getSession();
    User CurrentUser = (User)session.getAttribute("UserData");
    boolean AccountRemoved = CurrentUser.RemoveAccount(accName);
    session.setAttribute("UserData", CurrentUser);
    out.println("<html>");
    out.println("<head>");
    out.println("<meta http-equiv='pragma'  content='no-cache'>");
    out.println("<meta http-equiv='cache-control'  content='no-cache'>");
    out.println("<meta http-equiv='expires'  content='0'>");
    out.println("<title>Delete Account</title>");
    out.println("</head>");
    out.println("<body>");
    out.println("<br>");
    if (AccountRemoved) {
      out.println("<h1>Account " + accName + " has been deleted</h1>");
    } else {
      out.println("<h1>Account " + accName + " could not be deleted as it has a non-zero balance</h1>");
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

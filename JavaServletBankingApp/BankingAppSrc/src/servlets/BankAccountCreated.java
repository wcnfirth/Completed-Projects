package servlets;

import classes.BankAccountType;
import classes.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet({"/BankAccountCreated"})
public class BankAccountCreated extends HttpServlet {
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/HTML");
    PrintWriter out = response.getWriter();
    String type = request.getParameter("accountType");
    String name = request.getParameter("name");
    float balance = Float.parseFloat(request.getParameter("balance"));
    HttpSession session = request.getSession();
    User CurrentUser = (User)session.getAttribute("UserData");
    BankAccountType accType = BankAccountType.StringToAccountType(type);
    boolean duplicateAccName = CurrentUser.AccountNameExists(name);
    if (!duplicateAccName)
      CurrentUser.AddAccount(accType, name, balance); 
    session.setAttribute("UserData", CurrentUser);
    out.println("<html>");
    out.println("<head>");
    out.println("<meta http-equiv='pragma'  content='no-cache'>");
    out.println("<meta http-equiv='cache-control'  content='no-cache'>");
    out.println("<meta http-equiv='expires'  content='0'>");
    out.println("<title>Create Account</title>");
    out.println("</head>");
    out.println("<body>");
    out.println("<br>");
    if (duplicateAccName) {
      out.println("<h1>" + type + "Account " + name + " could not be created as you already have an account by that name. Please choose a unique account name.</h1>");
    } else {
      out.println("<h1>" + type + "Account " + name + " has been successfully created.</h1>");
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

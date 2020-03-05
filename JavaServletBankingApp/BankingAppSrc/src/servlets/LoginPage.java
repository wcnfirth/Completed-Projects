package servlets;

import classes.UserDB;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet({"/LoginPage"})
public class LoginPage extends HttpServlet {
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/HTML");
    PrintWriter out = response.getWriter();
    HttpSession session = request.getSession();
    session.removeAttribute("UserData");
    ServletContext servletContext = getServletContext();
    String contextPath = servletContext.getRealPath(File.separator);
    if (session.getAttribute("UserDB") == null) {
      UserDB userDB = UserDB.RestoreUserDatabase(contextPath);
      session.setAttribute("UserDB", userDB);
    } 
    UserDB Users = (UserDB)session.getAttribute("UserDB");
    List<String> UserNames = Users.getAllUserNames();
    out.println("<html>");
    out.println("<head>");
    out.println("<meta http-equiv='pragma'  content='no-cache'>");
    out.println("<meta http-equiv='cache-control'  content='no-cache'>");
    out.println("<meta http-equiv='expires'  content='0'>");
    out.println("<title>Login</title>");
    out.println("</head>");
    out.println("<body>");
    out.println("<br>");
    out.println("<h2>Choose an account from the list below to login as</h2><br><br>");
    out.println("<form method='post' action='HomePage'>");
    out.println("<table>");
    out.println("<tr>");
    out.println("<td><select name = 'UserName' required>");
    for (String name : UserNames)
      out.println("<option value='" + name + "'>" + name + "</option>"); 
    out.println("</select></td>");
    out.println("<td><input type='submit' value='Log In'></td>");
    out.println("</tr>");
    out.println("</table>");
    out.println("</form>");
    out.println("<br><br><br>");
    out.println("<a href='NewUserPage'>New user? Create new account</a>");
    out.println("</body>");
    out.println("</html>");
    out.flush();
  }
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request, response);
  }
}

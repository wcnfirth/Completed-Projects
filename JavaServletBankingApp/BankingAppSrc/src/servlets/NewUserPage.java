package servlets;

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

@WebServlet({"/NewUserPage"})
public class NewUserPage extends HttpServlet {
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    HttpSession session = request.getSession();
    ServletContext servletContext = getServletContext();
    String contextPath = servletContext.getRealPath(File.separator);
    if (session.getAttribute("UserDB") == null) {
      UserDB Users = UserDB.RestoreUserDatabase(contextPath);
      session.setAttribute("UserDB", Users);
    } 
    out.println("<html>");
    out.println("<head>");
    out.println("<meta http-equiv='pragma'  content='no-cache'>");
    out.println("<meta http-equiv='cache-control'  content='no-cache'>");
    out.println("<meta http-equiv='expires'  content='0'>");
    out.println("<title>New User</title>");
    out.println("</head>");
    out.println("<body>");
    out.println("<br>");
    out.println("<h2>Create a unique account name</h2><br><br>");
    out.println("<form method='post' action='UserAccountCreated'>");
    out.println("<table>");
    out.println("<tr>");
    out.println("<td><input type='text' name='UserName' maxlength='24' placeholder='Account Name' required /></td>");
    out.println("<td><input type='submit' value='Create'></td>");
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

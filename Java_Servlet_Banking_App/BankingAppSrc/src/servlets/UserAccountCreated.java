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

@WebServlet({"/UserAccountCreated"})
public class UserAccountCreated extends HttpServlet {
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/HTML");
    PrintWriter out = response.getWriter();
    HttpSession session = request.getSession();
    session.removeAttribute("UserData");
    ServletContext servletContext = getServletContext();
    String contextPath = servletContext.getRealPath(File.separator);
    if (session.getAttribute("UserDB") == null) {
      UserDB userDB = new UserDB();
      session.setAttribute("UserDB", userDB);
    }
    UserDB Users = (UserDB)session.getAttribute("UserDB");
    String name = request.getParameter("UserName");
    out.println("<html>");
    out.println("<head>");
    out.println("<meta http-equiv='pragma'  content='no-cache'>");
    out.println("<meta http-equiv='cache-control'  content='no-cache'>");
    out.println("<meta http-equiv='expires'  content='0'>");
    out.println("<title>Create Account</title>");
    out.println("</head>");
    out.println("<body>");
    out.println("<br>");
    if (Users.UserNameExists(name)) {
      out.println("<h1>User " + name + " could not be created as there already exists a user by that name. Please choose a unique username.</h1>");
      out.println("<br><br>");
      out.println("<a href='index.html'>Back to Index Page</a>");
    } else {
      Users.addNewUser(new User(name, contextPath));
      session.setAttribute("UserDB", Users);
      out.println("<h1>User " + name + " has been successfully created.</h1>");
      out.println("<br><br>");
      out.println("<form action='HomePage' method='post'>");
      out.println("<input type='text' style='display:none' name='UserName' value='" + name + "'>");
      out.println("<input type='submit' value='Go to HomePage'>");
      out.println("</form>");
    }
    out.println("</body>");
    out.println("</html>");
    out.flush();
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request, response);
  }
}

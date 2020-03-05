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

@WebServlet({"/TransferCompleted"})
public class TransferCompleted extends HttpServlet {
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/HTML");
    PrintWriter out = response.getWriter();
    String transferFrom = request.getParameter("transferFrom");
    String transferTo = request.getParameter("transferTo");
    String strTransferAmount = request.getParameter("transferAmount");
    float transferAmount = Float.parseFloat(strTransferAmount);
    HttpSession session = request.getSession();
    User CurrentUser = (User)session.getAttribute("UserData");
    int TransferFailed = CurrentUser.MakeTransfer(transferFrom, transferTo, transferAmount);
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
    if (TransferFailed == 0) {
      out.println("<h1>$" + strTransferAmount + " has been successfully transferred from account " + transferFrom + " to account " + transferTo + ".</h1>");
    } else if (TransferFailed == 1) {
      out.println("<h1>Transfer not completed - requested transfer amount $" + strTransferAmount + " is larger than the funds available in account " + transferFrom + ".</h1>");
    } else {
      out.println("<h1>Transfer not completed - funds cannot be transferred between the same account</h1>");
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

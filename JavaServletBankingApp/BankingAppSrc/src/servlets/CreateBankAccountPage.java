package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Serve page that allows user to create a new checking, savings, or brokerage account with an optional initial balance.
 */
@WebServlet({"/CreateBankAccountPage"})
public class CreateBankAccountPage extends HttpServlet {

  /**
   * Respond to the user request.
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    // Set response parameters
    response.setContentType("text/HTML");
    PrintWriter out = response.getWriter();

    /**
     * Write the pertinent HTML response to the PrintWriter
     */
    out.println("<html>");
    out.println("<head>");
    out.println("<meta http-equiv='pragma'  content='no-cache'>");
    out.println("<meta http-equiv='cache-control'  content='no-cache'>");
    out.println("<meta http-equiv='expires'  content='0'>");
    out.println("<title>Create An Account</title>");
    out.println("</head>");
    out.println("<body>");
    out.println("<br>");
    out.println("<h1>Choose an account type, and give it a unique name and a starting balance.</h1><br><br>");
    out.println("<form method='post' action='BankAccountCreated'>");
    out.println("<table>");
    out.println("<tr>");
    out.println("<td><b>Type</b></td>");
    out.println("<td><b>Name</b></td>");
    out.println("<td><b>Balance</b></td>");
    out.println("</tr>");
    out.println("<tr>");
    out.println("<td><select name='accountType'>");
    out.println("<option value='Checking'>Checking</option>");
    out.println("<option value='Savings'>Savings</option>");
    out.println("<option value='Brokerage'>Brokerage</option>");
    out.println("</select></td>");
    out.println("<td><input type='text' name='name' maxlength='24' placeholder='Account Name' required /></td>");
    out.println("<td><input type='number' name='balance' min='0.00' step='0.01' max='999999' value='0.0' /></td>");
    out.println("<td><input type='submit' value='Create'></td>");
    out.println("</tr>");
    out.println("</table>");
    out.println("</form>");
    out.println("</body>");
    out.println("</html>");
    out.flush();

  }

  /**
   * Performs the same function as doGet().
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request, response);
  }

}

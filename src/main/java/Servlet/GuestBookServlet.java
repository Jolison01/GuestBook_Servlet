package Servlet;

import Dao.MessageDao;
import Model.Message;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDateTime;

@WebServlet("/")
public class GuestBookServlet extends HttpServlet {

    private MessageDao messageDao;

    @Override
    public void init() throws ServletException {


        try {
            Class.forName("org.mariadb.jdbc.Driver");
            messageDao = new MessageDao(getInitParameter("jdbc:mariadb://noelvaes.eu/StudentDB")
            , getInitParameter("student")
            , getInitParameter("student123"));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       resp.setContentType("text/html");
       resp.setCharacterEncoding("UTF-8");

       try  (PrintWriter writer = resp.getWriter()) {

           writer.println("<!DOCTYPE html>");
           writer.println("<html>");
           writer.println("<head>");
           writer.println("<title>HelloWorldServlet</title>");
           writer.println("</head>");
           writer.println("<body>");
           writer.println("<h1>Comments</h1>");
           writer.println("<img src=\"https://www.motherjones.com/wp-content/uploads/2019/05/20190529_Comments_2002.png?resize=630,354\">");
           writer.println("<br>");




           writer.println("<div>");
           writer.println("<form method='post' action=''>");
           writer.println("<p> please leave us your comments...</p><br>"+
           "name: <input type=\"text\" name=\"name\" required><br>"+
           "e-mail: <input type=\"email\" name=\"e-mail\" required><br><br>"+
           "message: <textarea name='message'  cols='30' rows='10'></textarea>"+
                   "<button type='submit'>Send!</button>");
           writer.println("</form>");
           writer.println("</div>");
           writer.println("</body>");
           writer.println("</html>");
            writer.close();

           for (Message message : messageDao.getAllMessages()) {
               writer.println("<p>"+message+"</p>");
           }

       }catch (IOException ex){
           System.out.printf("exception during writing procedure");
           throw  ex;
       }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    Message message = new Message(LocalDateTime.now()
            , req.getParameter("name")
            ,req.getParameter("message"));
            messageDao.createMessage(message);
            resp.sendRedirect(req.getContextPath());

    }


    @Override
    public void destroy(){
        try {
            messageDao.getConnection().close();
        }catch (SQLException e){
            e.printStackTrace();
        }


    }

}

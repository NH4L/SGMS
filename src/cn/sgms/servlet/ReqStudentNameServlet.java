package cn.sgms.servlet;

import cn.util.DbUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 通过专业，年级，学号信息从数据库加载学生姓名
 * @author lcy
 * @version 12-03
 */
@WebServlet(name="ReqStudentNameServlet", urlPatterns = {"/sgms/ReqStudentNameServlet"})
public class ReqStudentNameServlet extends HttpServlet {
    @Override
    public  void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/xml;charset=utf-8");
        PrintWriter out = response.getWriter();

        String specialty = request.getParameter("specialty");
        String grade = request.getParameter("grade");
        String studentNo = request.getParameter("studentNo");

        String studentName = null;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DbUtils.getConnection();
            stmt = conn.createStatement();

            String querySql = "SELECT studentName\n" +
                    "FROM StudentInfo\n" +
                    "WHERE specialty='" + specialty + "' AND grade='" + grade + "' AND studentNo='" + studentNo + "'";
            System.out.println(querySql);
            rs = stmt.executeQuery(querySql);
            while (rs.next()) {
                studentName = rs.getString("studentName");
            }

            System.out.println(studentName);
            if (studentName == null) {
                out.write("fail");
            } else {
                out.write(studentName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.close(rs, stmt, conn);
        }

        out.flush();
        out.close();
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public String getServletInfo() {
        return "ReqStudentNameServlet";
    }
}
package cn.sgms.servlet;

import cn.util.DbUtils;
import cn.util.JsonUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 通过前端传回的学号和科目删除成绩信息
 * @author lcy
 * @version 12-04
 */
@WebServlet(name="DeleteScoreServlet", urlPatterns = {"/sgms/DeleteScoreServlet"})
public class DeleteScoreServlet extends HttpServlet {
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

        String studentNo = request.getParameter("studentNo");
        String subjectName = request.getParameter("subjectName");

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DbUtils.getConnection();
            stmt = conn.createStatement();

            String querySql = "DELETE\n" +
                    "FROM ScoreInfo\n" +
                    "WHERE subjectId = (SELECT subjectId FROM SubjectInfo WHERE subjectName='" + subjectName + "')\n" +
                    "\t  AND studentId = (SELECT studentId FROM StudentInfo WHERE studentNo='" + studentNo + "')";
            System.out.println(querySql);
            int isSuccess = stmt.executeUpdate(querySql);
            if (isSuccess > 0) {
                System.out.println("删除成绩成功");
                out.write("success");
            } else {
                out.write("fail");
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
        return "DeleteScoreServlet";
    }
}
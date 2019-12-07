package cn.sgms.servlet;

import cn.sgms.entity.Statistics;
import cn.util.DbUtils;
import cn.util.JsonUtils;
import com.google.gson.JsonObject;

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
import java.util.ArrayList;
import java.util.List;


/**
 * 从数据库加载首页的学生成绩信息
 * @author lcy
 * @version 12-02
 */
@WebServlet(name="StudentStatisticsServlet", urlPatterns = {"/sgms/StudentStatisticsServlet"})
public class StudentStatisticsServlet extends HttpServlet {
    @Override
    public  void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected  void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/xml;charset=utf-8");
        PrintWriter out = response.getWriter();
        List<JsonObject> studentSum = new ArrayList<JsonObject>();
        List<JsonObject> subjectAvg = new ArrayList<JsonObject>();
        Statistics statistics = new Statistics();

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DbUtils.getConnection();
            stmt = conn.createStatement();

            String sumSql = "SELECT studentNo, studentName, SUM(studentScore) AS sumScore\n" +
                    "FROM StudentInfo, ScoreInfo, SubjectInfo\n" +
                    "WHERE ScoreInfo.studentId=StudentInfo.studentId \n" +
                    "\t  AND ScoreInfo.subjectId=SubjectInfo.subjectId\n" +
                    "GROUP BY studentNo, studentName";
            rs = stmt.executeQuery(sumSql);
            while (rs.next()) {
                JsonObject obj = new JsonObject();
                obj.addProperty("studentNo", rs.getString("studentNo"));
                obj.addProperty("studentName", rs.getString("studentName"));
                obj.addProperty("sumScore", rs.getFloat("sumScore"));
                studentSum.add(obj);
            }

            String avgSql = "SELECT subjectName, AVG(studentScore) AS avgScore\n" +
                    "FROM StudentInfo, ScoreInfo, SubjectInfo\n" +
                    "WHERE ScoreInfo.studentId=StudentInfo.studentId \n" +
                    "\t  AND ScoreInfo.subjectId=SubjectInfo.subjectId\n" +
                    "GROUP BY subjectName";
            rs = stmt.executeQuery(avgSql);
            while (rs.next()) {
                JsonObject obj = new JsonObject();
                obj.addProperty("subjectName", rs.getString("subjectName"));
                obj.addProperty("avgScore", rs.getFloat("avgScore"));
                subjectAvg.add(obj);
            }
            statistics.setStudentSum(studentSum);
            statistics.setSubjectAvg(subjectAvg);

            String studentStatistics = JsonUtils.objectToJson(statistics);
            System.out.println(studentStatistics);
            out.write(studentStatistics);

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
        return "StudentStatisticsServlet";
    }
}

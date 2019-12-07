package cn.sgms.servlet;

import cn.sgms.entity.StudentInfo;
import cn.util.DbUtils;
import cn.util.JsonUtils;

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
@WebServlet(name="LoadStudentServlet", urlPatterns = {"/sgms/LoadStudentServlet"})
public class LoadStudentServlet extends HttpServlet {
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
        List<StudentInfo> students = new ArrayList<StudentInfo>();

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DbUtils.getConnection();
            stmt = conn.createStatement();

            String querySql = "SELECT specialty, grade, studentNo, studentSex, " +
                    "studentName, subjectName, studentScore\n" +
                    "FROM StudentInfo, ScoreInfo, SubjectInfo\n" +
                    "WHERE ScoreInfo.studentId=StudentInfo.studentId \n" +
                    "\t  AND ScoreInfo.subjectId=SubjectInfo.subjectId";
            rs = stmt.executeQuery(querySql);
            while (rs.next()) {
                StudentInfo student = new StudentInfo();

                student.setSpecialty(rs.getString("specialty"));
                student.setGrade(rs.getString("grade"));
                student.setStudentNo(rs.getString("studentNo"));
                student.setStudentSex(rs.getString("studentSex"));
                student.setStudentName(rs.getString("studentName"));
                student.setSubjectName(rs.getString("subjectName"));
                student.setStudentScore(rs.getFloat("studentScore"));

                students.add(student);
            }
            String studentJson = JsonUtils.objectToJson(students);
            System.out.println(studentJson);
            out.write(studentJson);

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
        return "LoadStudentServlet";
    }
}

package cn.sgms.servlet;

import cn.sgms.entity.StudentInfo;
import cn.util.DbUtils;
import cn.util.JsonUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 添加学生信息到数据库
 * @author lcy
 * @version 12-02
 */
@WebServlet(name="AddStudentServlet", urlPatterns = {"/sgms/AddStudentServlet"})
public class AddStudentServlet extends HttpServlet {
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


        // 读取请求内容
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"utf-8"));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        //将json字符串转换为json对象
        System.out.println(sb.toString());

        //将前端传入的数据加载入student对象
        StudentInfo student = (StudentInfo) JsonUtils.jsonToObject(sb.toString(), StudentInfo.class);
        System.out.println(student);

        PrintWriter out = response.getWriter();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DbUtils.getConnection();
            stmt = conn.createStatement();
            String checkSql = "SELECT *\n" +
                    "FROM ScoreInfo\n" +
                    "WHERE subjectId = (SELECT subjectId FROM SubjectInfo WHERE subjectName='" + student.getSubjectName() + "') \n" +
                    "\t  AND studentId = (SELECT studentId FROM StudentInfo WHERE studentNo='" + student.getStudentNo() + "')";
            rs = stmt.executeQuery(checkSql);

            if (!rs.next()) {
                String insertSql = "INSERT INTO ScoreInfo(subjectId, studentId, studentScore, modifyTime)\n" +
                        "VALUES ((SELECT subjectId FROM SubjectInfo WHERE subjectName='" + student.getSubjectName() + "'),\n" +
                        "\t\t(SELECT studentId FROM StudentInfo WHERE studentNo='" + student.getStudentNo() + "'), " + student.getStudentScore() + ", GETDATE())";
                int isSuccess = stmt.executeUpdate(insertSql);
                System.out.println(insertSql + '\n' + isSuccess);

                if (isSuccess > 0) {
                    System.out.println("插入成绩成功");
                    out.write("success");
                } else {
                    out.write("fail");
                }
            } else {
                System.out.println("插入成绩重复，插入失败");
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
        return "AddStudentServlet";
    }
}
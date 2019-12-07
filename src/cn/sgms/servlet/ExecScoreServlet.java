package cn.sgms.servlet;

import cn.sgms.entity.StudentInfo;
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
 * 通过前端传回的信息从数据库更新学生成绩
 * @author lcy
 * @version 12-03
 */
@WebServlet(name="ExecScoreServlet", urlPatterns = {"/sgms/ExecScoreServlet"})
public class ExecScoreServlet extends HttpServlet {
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

        // 读取ajax请求内容
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

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DbUtils.getConnection();
            stmt = conn.createStatement();

            String querySql = "UPDATE ScoreInfo\n" +
                    "SET studentScore=" + student.getStudentScore() + "\n" +
                    "WHERE subjectId = (SELECT subjectId FROM SubjectInfo WHERE subjectName='" + student.getSubjectName() + "') \n" +
                    "\t  AND studentId = (SELECT studentId FROM StudentInfo WHERE studentNo='" + student.getStudentNo() + "')";
            System.out.println(querySql);
            int isSuccess = stmt.executeUpdate(querySql);
            if (isSuccess > 0) {
                System.out.println("修改成绩成功");
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
        return "ExecScoreServlet";
    }
}
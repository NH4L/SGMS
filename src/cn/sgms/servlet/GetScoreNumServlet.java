package cn.sgms.servlet;

import cn.util.DbUtils;
import cn.util.JsonUtils;
import com.google.gson.JsonObject;

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
 * 查询数据库中分数记录条数
 * @author lcy
 * @version 12-05
 */
@WebServlet(name="GetScoreNumServlet", urlPatterns = {"/sgms/GetScoreNumServlet"})
public class GetScoreNumServlet extends HttpServlet {
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

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DbUtils.getConnection();
            stmt = conn.createStatement();

            JsonObject obj = new JsonObject();

            String querySql = "SELECT COUNT(*) AS scoreCount\n" +
                              "FROM ScoreInfo";
            System.out.println(querySql);
            rs = stmt.executeQuery(querySql);
            while (rs.next()) {
                obj.addProperty("scoreCount", rs.getInt("scoreCount"));
            }
            out.write(obj.toString());
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
        return "GetScoreNumServlet";
    }
}
package com.example.dmo2;

import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// 统计数据的独立 Servlet
@WebServlet("/get-stats")
public class StatsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        // 创建一个 Map 存储统计数据
        Map<String, Object> stats = new HashMap<>();
        try {
            stats.put("totalTransactions", HouseServile.getTotalTransactions()); // 成交数量
            stats.put("totalVisits", HouseServile.getTotalVisits()); // 用户访问量
            stats.put("totalHouses", HouseServile.getHouseCount()); // 房屋总数

            // 示例新增用户数量
            // 如果实现了用户管理功能，可以将其替换为动态计算值
            stats.put("newUserCount", 2);

        } catch (Exception e) {
            // 如果出现任何异常，返回错误信息
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            stats.put("error", "获取统计数据失败：" + e.getMessage());
        }

        // 返回 JSON 响应
        response.getWriter().write(new Gson().toJson(stats));
    }
}

package com.example.dmo2;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

@WebServlet("/login") // 登录验证接口
//后续可扩展数据库判定验证
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 获取前端传递的用户名和密码
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // 模拟随机登录结果
        String[] loginMessages = {
                "登录成功！",
                "密码错误，请重试。",
                "用户名不存在，请注册。",
                "服务器忙，请稍后重试。",
                "账户已锁定，请联系管理员。",
                "登录失败，网络问题。"
        };
        Random random = new Random();
        int randomIndex = random.nextInt(loginMessages.length);
        String randomMessage = loginMessages[randomIndex];

        // 判断随机结果是否为 "登录成功！"
        if ("登录成功！".equals(randomMessage)) {
            // 后续可以改为数据库验证
            if ("zcj".equals(username) && "123456".equals(password)) {
                response.getWriter().write("{\"success\": true, \"message\": \"" + randomMessage + "\"}");
            } else {
                response.getWriter().write("{\"success\": false, \"message\": \"Invalid credentials\"}");
            }
        } else {
            // 返回随机登录失败的结果
            response.getWriter().write("{\"success\": false, \"message\": \"" + randomMessage + "\"}");
        }
    }
}

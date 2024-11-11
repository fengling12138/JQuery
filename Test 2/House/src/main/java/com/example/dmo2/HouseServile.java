package com.example.dmo2;

import com.google.gson.Gson;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//数据存储
@WebServlet("/get-houses")
public class HouseServile extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<House> houses = new ArrayList<>();
        houses.add(new House(101, "123 Main St", 1500.0, "123-456-7890"));
        houses.add(new House(102, "456 Elm St", 2000.0, "098-765-4321"));
        houses.add(new House(103, "789 Oak St", 1800.0, "555-555-5555"));
        houses.add(new House(104, "123 Main St", 1500.0, "123-456-7890"));
        houses.add(new House(105, "456 Elm St", 2000.0, "098-765-4321"));
        houses.add(new House(106, "789 Oak St", 1800.0, "555-555-5555"));
        houses.add(new House(107, "123 Main St", 1500.0, "123-456-7890"));
        houses.add(new House(108, "456 Elm St", 2000.0, "098-765-4321"));
        houses.add(new House(109, "789 Oak St", 1800.0, "555-555-5555"));


        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();
        String json = gson.toJson(houses);
        response.getWriter().write(json);
    }
}

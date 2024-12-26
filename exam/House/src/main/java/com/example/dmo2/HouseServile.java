package com.example.dmo2;

import com.google.gson.Gson;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@WebServlet("/get-houses")
public class HouseServile extends HttpServlet {

    // 房屋列表
    private static final List<House> houses = Collections.synchronizedList(new ArrayList<>());
    // 成交数量
    private static int totalTransactions = 0;
    // 用户访问量
    private static int totalVisits = 0;
    // 锁对象（确保线程安全）
    private static final ReentrantLock lock = new ReentrantLock();

    // 静态初始化房屋数据
    static {
        houses.add(new House(201, "北京市海淀区中关村大街", 3500.0, "010-88888888", Arrays.asList("/dmo2_war_exploded/1.jpg", "/dmo2_war_exploded/2.jpg")));
        houses.add(new House(202, "上海市浦东新区陆家嘴环路", 4500.0, "021-66666666", Arrays.asList("/dmo2_war_exploded/3.jpg")));
        houses.add(new House(203, "广州市天河区珠江新城", 2800.0, "020-77777777", Arrays.asList("/dmo2_war_exploded/4.jpg", "/dmo2_war_exploded/1.jpg")));
        houses.add(new House(204, "深圳市南山区科技园", 3000.0, "0755-99999999", Arrays.asList("/dmo2_war_exploded/5.jpg")));
        houses.add(new House(205, "重庆市南岸区梨花大道", 2600.0, "023-44444444", Arrays.asList("/dmo2_war_exploded/3.jpg", "/dmo2_war_exploded/2.jpg")));
        houses.add(new House(206, "杭州市西湖区文三路", 3200.0, "0571-22222222", Arrays.asList("/dmo2_war_exploded/6.jpg", "/dmo2_war_exploded/7.jpg")));
        houses.add(new House(207, "成都市高新区天府大道", 3400.0, "028-55555555", Arrays.asList("/dmo2_war_exploded/8.jpg")));
        houses.add(new House(208, "武汉市武昌区中南路", 3100.0, "027-33333333", Arrays.asList("/dmo2_war_exploded/5.jpg", "/dmo2_war_exploded/6.jpg")));
        houses.add(new House(209, "西安市雁塔区长安中路", 2900.0, "029-88888888", Arrays.asList("/dmo2_war_exploded/8.jpg")));
        houses.add(new House(210, "南京市玄武区中山路", 3000.0, "025-66666666", Arrays.asList("/dmo2_war_exploded/1.jpg", "/dmo2_war_exploded/8.jpg")));

    }

    // 获取当前房屋总数
    public static int getHouseCount() {
        return houses.size();
    }

    // 获取成交总量
    public static int getTotalTransactions() {
        return totalTransactions;
    }

    // 获取访问总量
    public static int getTotalVisits() {
        return totalVisits;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        lock.lock(); // 加锁保护 totalVisits 的更新
        try {
            totalVisits++; // 每次调用接口时增加访问量
        } finally {
            lock.unlock();
        }

        String address = request.getParameter("address");
        String id = request.getParameter("id");
        String minPrice = request.getParameter("minPrice");
        String maxPrice = request.getParameter("maxPrice");
        String priceOrder = request.getParameter("priceOrder");

        List<House> filteredHouses = new ArrayList<>(houses);

        if (address != null && !address.isEmpty()) {
            filteredHouses = filteredHouses.stream()
                    .filter(house -> house.getAddress().contains(address))
                    .collect(Collectors.toList());
        }

        if (id != null && !id.isEmpty()) {
            try {
                int houseId = Integer.parseInt(id);
                filteredHouses = filteredHouses.stream()
                        .filter(house -> house.getId() == houseId)
                        .collect(Collectors.toList());
            } catch (NumberFormatException ignored) {
                response.getWriter().write("{\"status\":\"failure\",\"message\":\"ID 格式不正确\"}");
                return;
            }
        }

        if (minPrice != null && !minPrice.isEmpty()) {
            try {
                double min = Double.parseDouble(minPrice);
                filteredHouses = filteredHouses.stream()
                        .filter(house -> house.getPrice() >= min)
                        .collect(Collectors.toList());
            } catch (NumberFormatException ignored) {
                response.getWriter().write("{\"status\":\"failure\",\"message\":\"最低价格格式不正确\"}");
                return;
            }
        }

        if (maxPrice != null && !maxPrice.isEmpty()) {
            try {
                double max = Double.parseDouble(maxPrice);
                filteredHouses = filteredHouses.stream()
                        .filter(house -> house.getPrice() <= max)
                        .collect(Collectors.toList());
            } catch (NumberFormatException ignored) {
                response.getWriter().write("{\"status\":\"failure\",\"message\":\"最高价格格式不正确\"}");
                return;
            }
        }

        if ("asc".equals(priceOrder)) {
            filteredHouses.sort(Comparator.comparingDouble(House::getPrice));
        } else if ("desc".equals(priceOrder)) {
            filteredHouses.sort((a, b) -> Double.compare(b.getPrice(), a.getPrice()));
        }

        response.getWriter().write(new Gson().toJson(filteredHouses));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        BufferedReader reader = request.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        House newHouse = new Gson().fromJson(sb.toString(), House.class);

        lock.lock();
        try {

            boolean idExists = houses.stream().anyMatch(house -> house.getId() == newHouse.getId());
            if (idExists) {
                response.getWriter().write("{\"status\":\"failure\",\"message\":\"房屋 ID 已存在\"}");
                return;
            }

            houses.add(newHouse);
        } finally {
            lock.unlock();
        }

        response.getWriter().write("{\"status\":\"success\"}");
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idParam = request.getParameter("id");
        if (idParam != null) {
            int id;
            try {
                id = Integer.parseInt(idParam);
            } catch (NumberFormatException e) {
                response.getWriter().write("{\"status\":\"failure\",\"message\":\"ID 格式不正确\"}");
                return;
            }

            lock.lock();
            try {
                boolean removed = houses.removeIf(house -> house.getId() == id);

                if (removed) {
                    totalTransactions++; // 增加成交数量
                    response.getWriter().write("{\"status\":\"success\"}");
                } else {
                    response.getWriter().write("{\"status\":\"failure\",\"message\":\"房屋不存在\"}");
                }
            } finally {
                lock.unlock();
            }
        } else {
            response.getWriter().write("{\"status\":\"failure\",\"message\":\"缺少房屋编号\"}");
        }
    }
}

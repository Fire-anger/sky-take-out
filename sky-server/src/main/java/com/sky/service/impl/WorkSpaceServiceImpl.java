package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.entity.Orders;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkSpaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class WorkSpaceServiceImpl implements WorkSpaceService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 根据时间段统计营业数据
     * @return
     */
    @Override
    public BusinessDataVO getBusinessData(LocalDateTime begin,LocalDateTime end) {
        // LocalDateTime begin = LocalDateTime.now().with(LocalTime.MIN);
        // LocalDateTime end = LocalDateTime.now().with(LocalTime.MAX);

        Map map = new HashMap();
        map.put("begin",begin);
        map.put("end",end);

        // 今日订单总数
        Integer totalOrderCount = orderMapper.countByMap(map);

        map.put("status", Orders.COMPLETED);
        // 今日营业额
        Double turnover = orderMapper.sumByMap(map);  // 答案的营业额的map是没有订单状态为已完成的，但是我感觉要加上
        turnover = turnover == null ? 0.0 : turnover;
        // 今日有效订单数
        Integer validOrderCount = orderMapper.countByMap(map);
        // 订单完成率
        Double orderCompletionRate = 0.0;
        // 平均客单价
        Double unitPrice = 0.0;
        if(totalOrderCount != null && totalOrderCount != 0 && validOrderCount != null && validOrderCount != 0){
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
            unitPrice = turnover / validOrderCount;
        }

        // 新增用户数
        Integer newUsers = userMapper.countByMap(map);
        newUsers = newUsers == null ? 0 : newUsers;

        return new BusinessDataVO().builder()
                .turnover(turnover)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .newUsers(newUsers)
                .unitPrice(unitPrice)
                .build();
    }

    /**
     * 查询订单管理数据
     * @return
     */
    @Override
    public OrderOverViewVO getOverviewOrders() {
        Map map = new HashMap();
        // 待接单数量
        map.put("status",Orders.TO_BE_CONFIRMED);
        Integer waitingOrders = orderMapper.countByMap(map);
        // 待派送数量
        map.put("status",Orders.CONFIRMED);
        Integer deliveredOrders = orderMapper.countByMap(map);
        // 已完成数量
        map.put("status",Orders.COMPLETED);
        Integer completedOrders = orderMapper.countByMap(map);
        // 已取消数量
        map.put("status",Orders.CANCELLED);
        Integer cancelledOrders = orderMapper.countByMap(map);
        // 全部订单
        map.put("status",null);
        Integer allOrders = orderMapper.countByMap(map);

        return new OrderOverViewVO().builder()
                .waitingOrders(waitingOrders)
                .deliveredOrders(deliveredOrders)
                .completedOrders(completedOrders)
                .cancelledOrders(cancelledOrders)
                .allOrders(allOrders)
                .build();
    }

    /**
     * 查询菜品总览
     * @return
     */
    @Override
    public DishOverViewVO getOverviewDishes() {
        Map map = new HashMap();
        // 已停售菜品数量
        map.put("status", StatusConstant.DISABLE);
        Integer discontinued = dishMapper.countByMap(map);

        // 已起售菜品数量
        map.put("status", StatusConstant.ENABLE);
        Integer sole = dishMapper.countByMap(map);
        return new DishOverViewVO().builder()
                .discontinued(discontinued)
                .sold(sole)
                .build();
    }

    /**
     * 查询套餐总览
     * @return
     */
    @Override
    public SetmealOverViewVO getOverviewSetmeals() {
        Map map = new HashMap();

        // 已起售套餐数量
        map.put("status",StatusConstant.ENABLE);
        Integer sold = setmealMapper.countByMap(map);

        // 已停售套餐数量
        map.put("status",StatusConstant.DISABLE);
        Integer discontinued = setmealMapper.countByMap(map);
        return new SetmealOverViewVO().builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }
}

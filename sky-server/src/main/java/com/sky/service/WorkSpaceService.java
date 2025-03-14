package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;

public interface WorkSpaceService {

    /**
     * 根据时间段统计营业数据
     * @return
     */
    BusinessDataVO getBusinessData();

    /**
     * 查询订单管理数据
     * @return
     */
    OrderOverViewVO getOverviewOrders();

    /**
     * 查询菜品总览
     * @return
     */
    DishOverViewVO getOverviewDishes();

    /**
     * 查询套餐总览
     *
     * @return
     */
    SetmealOverViewVO getOverviewSetmeals();

}

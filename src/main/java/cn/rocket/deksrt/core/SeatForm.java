/*
 * Copyright (c) 2022 Rocket, Project DeskSorting
 */

package cn.rocket.deksrt.core;

import cn.rocket.deksrt.core.student.Student;

/**
 * 学生表格接口
 *
 * @author Rocket
 * @version 1.0.8
 * @since 1.0.8
 */
public interface SeatForm {
    /**
     * 横向最多的学生数量
     */
    int MAX_X = 8;

    /**
     * 纵向最多的学生数量
     */
    int MAY_Y = 7;

    /**
     * 根据位置设置网格中学生
     *
     * @param x   x坐标
     * @param y   y坐标
     * @param stu 要设置的学生对象
     */
    void set(int x, int y, Student stu);

    /**
     * 获取对应位置的学生对象
     *
     * @param x x坐标
     * @param y y坐标
     * @return 相应的学生对象
     */
    Student get(int x, int y);

    /**
     * 快速交换两个学生的位置
     *
     * @param x1 第1个学生的x坐标
     * @param y1 第1个学生的y坐标
     * @param x2 第2个学生的x坐标
     * @param y2 第2个学生的y坐标
     */
    void fastSwap(int x1, int y1, int x2, int y2);

    /**
     * 对所有学生执行快速排序
     */
    void fastSort();

    /**
     * 对所有学生执行随机排序
     */
    void randomSort();
}

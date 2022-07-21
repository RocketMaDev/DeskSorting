/*
 * Copyright (c) 2022 Rocket, Project DeskSorting
 */

package cn.rocket.deksrt.core.iterator;

import static cn.rocket.deksrt.core.Util.readX;
import static cn.rocket.deksrt.core.Util.readY;

/**
 * 封装学生所在位置
 *
 * @author Rocket
 * @version 1.0.8
 * @since 1.0.8
 */
public class Pair {

    private final int x;
    private final int y;

    /**
     * 返回座位的X坐标
     *
     * @return {@code int} - x坐标
     */
    public int x() {
        return x;
    }

    /**
     * 返回座位的Y坐标
     *
     * @return {@code int} - y坐标
     */
    public int y() {
        return y;
    }

    /**
     * 封装两个坐标到对象
     *
     * @param xy 以预定义方式封装好的xy组合坐标
     */
    public Pair(int xy) {
        x = readX(xy);
        y = readY(xy);
    }
}

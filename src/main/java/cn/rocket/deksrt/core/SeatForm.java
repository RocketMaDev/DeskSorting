/*
 * Copyright (c) 2022 Rocket, Project DeskSorting
 */

package cn.rocket.deksrt.core;

public interface SeatForm {
    int MAX_X = 8;
    int MAY_Y = 7;

    void set(int x, int y, Student stu);

    Student get(int x, int y);

    void fastSwap(int x1, int y1, int x2, int y2);

    void fastSort();

    void randomSort();
}

/*
 * Copyright (c) 2022 Rocket, Project DeskSorting
 */

package cn.rocket.deksrt.gui;

import cn.rocket.deksrt.core.SeatForm;
import cn.rocket.deksrt.core.student.Student;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

/**
 * GUI座位表实现
 *
 * @author Rocket
 * @version 1.0.8
 * @since 1.0.8
 */
public class GUISeatForm implements SeatForm {

    private JFXButton[][] btns;
    private JFXTextField[][] textfields;
    private Student[][] students;

    @Override
    public void set(int x, int y, Student stu) {

    }

    @Override
    public Student get(int x, int y) {
        return null;
    }

    @Override
    public void fastSwap(int x1, int y1, int x2, int y2) {

    }

    @Override
    public void fastSort() {

    }

    @Override
    public void randomSort() {

    }
}

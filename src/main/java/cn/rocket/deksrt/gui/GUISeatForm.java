/*
 * Copyright (c) 2022 Rocket, Project DeskSorting
 */

package cn.rocket.deksrt.gui;

import cn.rocket.deksrt.core.SeatForm;
import cn.rocket.deksrt.core.student.Student;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

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

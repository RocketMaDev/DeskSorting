/*
 * Copyright (c) 2021 Rocket, Project DeskSorting
 */

package cn.rocket.deksrt.core;

import cn.rocket.deksrt.gui.interfaces.Controller;
import cn.rocket.deksrt.util.Student;
import cn.rocket.deksrt.util.StudentList;
import javafx.stage.Stage;

import java.util.HashMap;

/**
 * @author Rocket
 * @version 0.9-pre
 */
public class Vars {

    public static final HashMap<Class<?>, Object> objMap = new HashMap<>();
    public static final HashMap<Controller, Stage> stageMap = new HashMap<>();
    public static StudentList<Student> stuInfo;


    static final String GIVE_UP_LAYOUT_WARNING = "是否放弃当前布局？";


}

/*
 * Copyright (c) 2021 Rocket, Project DeskSorting
 */

package cn.rocket.deksrt.core;

import cn.rocket.deksrt.core.student.Student;
import cn.rocket.deksrt.core.student.StudentList;
import javafx.stage.Stage;

import java.util.HashMap;

/**
 * @author Rocket
 * @version 0.9-pre
 */
public class Vars {
    /**
     * 用来存放需要在全局访问的类对应的object
     */
    private static final HashMap<Class<?>, Object> objMap = new HashMap<>();
    /**
     * 用来存放GUI类对应的Stage
     */
    private static final HashMap<Class<?>, Stage> stageMap = new HashMap<>();
    /**
     * 系统当前加载的学生列表
     */
    public static StudentList<Student> stuInfo;


    public static final String GIVE_UP_LAYOUT_WARNING = "是否放弃当前布局？";

    /**
     * 向字典里放全局类实例
     *
     * @param obj 存放的对象
     */
    public static void putObj(Object obj) {
        objMap.put(obj.getClass(), obj);
    }

    /**
     * 从字典里取出所需的全局类实例
     *
     * @param type 用于泛型访问的空实例
     * @param <T>  对应{@code type}的类型
     * @return {@code type}类型的实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T getObj(T type) {
        return (T) objMap.get(type.getClass());
    }

    public static void putStage(Class<?> clz, Stage stage) {
        stageMap.put(clz, stage);
    }

    public static Stage getStage(Class<?> clz) {
        return stageMap.get(clz);
    }
}

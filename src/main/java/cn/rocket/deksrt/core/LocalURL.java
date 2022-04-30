/*
 * Copyright (c) 2022 Rocket, Project DeskSorting
 */

package cn.rocket.deksrt.core;

import cn.rocket.deksrt.cli.Main;

import java.io.File;
import java.net.URISyntaxException;

public final class LocalURL {
    private LocalURL() {
    }

    // User associated
    public static final String USER_PATH = System.getProperty("user.home") + "/"; // with /
    public static final String FOLDER_PATH = USER_PATH + ".rocketdev/";
    public static final String WORK_PATH = FOLDER_PATH + "AssignMark/";

    // Jar associated
    public static final String JAR_PATH; // with /
    public static final String JAR_PARENT_PATH; // with /

    public static final String RES_PATH = "/cn/rocket/deksrt/resource/";
    public static final String STU_INFO_TEMPLATE_P = RES_PATH + "templateOfStuInfo.xlsx";
    public static final String TABLE_TEMPLATE_P = RES_PATH + "templateOfTable.xlsx";
    public static final String MAIN_WINDOW_FXML = RES_PATH + "MainWindow.fxml";
    public static final String FOLDER_ICON = RES_PATH + "folder.png";
    public static final String INTRODUCTION = RES_PATH + "introduction.png";


    static {
        String jarPath;
        try {
            jarPath = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        } catch (URISyntaxException e) {
            System.err.println("无法解析jar路径！");
            throw new RuntimeException(e);
        }
        JAR_PATH = jarPath;
        JAR_PARENT_PATH = new File(JAR_PATH).getParent() + "/";
    }
}

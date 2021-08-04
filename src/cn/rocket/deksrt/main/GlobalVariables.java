/*
 * Copyright (c) 2021 Rocket, Project DeskSorting
 */

package cn.rocket.deksrt.main;

import cn.rocket.deksrt.util.Student;
import cn.rocket.deksrt.util.StudentList;
import javafx.stage.Stage;

/**
 * @author Rocket
 * @version 0.9-pre
 */
class GlobalVariables {
    static String jarPath; // With '/'
    static String jarParentPath; // With '/'

    static MainWindow mwObj;
    static Stage mainS;
    static Stage iodS;
    static StudentList<Student> stuInfo;

    static final String ENV = System.getenv("USERPROFILE") + "/.rocketdev/DeskSorting/";
    static final String STUDENT_INFO = ENV + "student.info";
    static final String RESOURCE_PATH = "/cn/rocket/deksrt/resource/";
    static final String STU_INFO_TEMPLATE_P = RESOURCE_PATH + "templateOfStuInfo.xlsx";
    static final String TABLE_TEMPLATE_P = RESOURCE_PATH + "templateOfTable.xlsx";
    static final String MAIN_WINDOW_FXML = RESOURCE_PATH + "MainWindow.fxml";
    static final String FOLDER_ICON = RESOURCE_PATH + "folder.png";
    static final String INTRODUCTION = RESOURCE_PATH + "introduction.png";

    static final String GIVE_UP_LAYOUT_WARNING = "是否放弃当前布局？";
}

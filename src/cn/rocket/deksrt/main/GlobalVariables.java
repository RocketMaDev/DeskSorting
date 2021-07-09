package cn.rocket.deksrt.main;

import cn.rocket.deksrt.util.Student;
import cn.rocket.deksrt.util.StudentList;

/**
 * @author Rocket
 * @version 1.0
 */
class GlobalVariables {
    static String jarPath; // With '/'
    static String jarParentPath; // With '/'


    static StudentList<Student> stuInfo;

    static final String ENV = System.getenv("USERPROFILE") + "/.rocketdev/DeskSorting/";
    static final String STUDENT_INFO = ENV + "student.info";
    static final String STU_INFO_TEMPLATE_P = "/cn/rocket/deksrt/resource/templateOfStuInfo.xlsx";
    static final String MAIN_WINDOW_FXML = "/cn/rocket/deksrt/resource/MainWindow.fxml";

    static boolean validatePinyin(String pinyin) {
        for (int i = 0; i < pinyin.length(); i++) {
            char c = pinyin.charAt(i);
            if (c < '0' || c > 'z' || (c > '9' && c < 'a')) // !'0'~'9'+'a'~'z'
                return false;
        }
        return true;
    }
}

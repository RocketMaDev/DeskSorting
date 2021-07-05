package cn.rocket.deksrt.main;

import cn.rocket.deksrt.util.StudentList;
import cn.rocket.deksrt.util.Student;

/**
 * @author Rocket
 * @version 1.0
 */
class GlobalVariables {
    static String jarPath; // With '/'
    static String jarParentPath; // With '/'


    static StudentList<Student> stuInfo;

    static final String env = System.getenv("USERPROFILE") + "/.rocketdev/DeskSorting/";
    static final String stuInfoTemplateP = "/cn/rocket/deksrt/resource/templateOfStuInfo.xlsx";

    static boolean validatePinyin(String pinyin) {
        for (int i = 0; i < pinyin.length(); i++) {
            char c = pinyin.charAt(i);
            if (c<'0'||c>'z'||(c>'9'&&c<'a')) // !'0'~'9'+'a'~'z'
                return false;
        }
        return true;
    }
}

package cn.rocket.deksrt.util;

/**
 * @author Rocket
 * @version 0.9-pre
 */
public class Student {
    private final String name;
    private final String pinyin;
    private final boolean boarding;
    private final boolean longName;

    @Override
    public boolean equals(Object obj) {
        Student stu;
        if (obj instanceof Student)
            stu = (Student) obj;
        else
            return false;
        return name.equals(stu.name);
    }

    @Override
    public String toString() {
        return "<" + name + "," + pinyin + "," + boarding + "," + (longName ? "long" : "short") + ">";
    }

    public Student(String name, String pinyin, boolean boarding) {
        this.name = name;
        this.pinyin = pinyin;
        this.boarding = boarding;
        this.longName = name.length() > 4;
    }

    public String getName() {
        return name;
    }

    public boolean isBoarding() {
        return boarding;
    }

    public String getPinyin() {
        return pinyin;
    }

    public boolean isLongName() {
        return longName;
    }
}

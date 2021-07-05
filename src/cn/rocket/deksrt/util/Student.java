package cn.rocket.deksrt.util;

public class Student {
    private final String name;
    private final String pinyin;
    private final boolean boarding;

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
        return "<" + name + "," + pinyin + "," + boarding + ">";
    }

    public Student(String name, String pinyin, boolean boarding) {
        this.name = name;
        this.pinyin = pinyin;
        this.boarding = boarding;
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
}

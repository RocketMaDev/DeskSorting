package cn.rocket.deksrt.main;

public class Student {
    private String name;
    private String pinyin;
    private boolean boarding;

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

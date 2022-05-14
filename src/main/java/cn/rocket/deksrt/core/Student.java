/*
 * Copyright (c) 2021 Rocket, Project DeskSorting
 */

package cn.rocket.deksrt.core;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * 不可修改的对象
 *
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
        StringJoiner joiner = new StringJoiner(", ", "<", ">");
        joiner.add(name).add(pinyin).add(Boolean.toString(boarding)).add(longName ? "long" : "short");
        return joiner.toString();
    }

    public Student(String name, String pinyin, boolean boarding) {
        this.name = name;
        this.pinyin = pinyin;
        this.boarding = boarding;
        this.longName = name.length() > 4;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
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

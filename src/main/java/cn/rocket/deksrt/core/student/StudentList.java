/*
 * Copyright (c) 2022 Rocket, Project DeskSorting
 */

package cn.rocket.deksrt.core.student;

import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.Objects;

/**
 * 存放学生对象的列表
 *
 * @param <E> see <code>E</code> in <code>ArrayList</code>
 * @author Rocket
 * @version 1.0.8
 * @see ArrayList
 * @since 0.9-pre
 */
public class StudentList<E extends Student> extends ArrayList<E> {
    private String className;
    private boolean searching = false;
    private boolean[] hasSearched = new boolean[size()];
    private int duplicated;

    @Override
    public boolean add(E e) {
        Objects.requireNonNull(e, "e can't be null");
        if (!super.contains(e))
            return super.add(e);
        return false;
    }

    public StudentList(int initialCapacity, String className) {
        super(initialCapacity);
        this.className = className;
    }

    /**
     * 开始搜索
     */
    public void startSearching() {
        if (searching)
            throw new IllegalStateException("Have started searching");
        searching = true;
        for (int i = 0; i < size(); i++)
            hasSearched[i] = false;
        duplicated = 0;
    }

    /**
     * 结束搜索过程
     *
     * @return 左值：统计到的有效学生数；右值：统计到的重复次数
     */
    public Pair<Integer, Integer> endSearching() {
        if (!searching)
            throw new IllegalStateException("Have ended searching");
        searching = false;
        int cnt = 0;
        for (int i = 0; i < size(); i++)
            if (hasSearched[i])
                cnt++;
        return Pair.create(cnt, duplicated);
    }

    public Student searchByName(String name) {
        if (!searching)
            throw new IllegalStateException("You need to invoke startSearching() first");
        for (int i = 0; i < size(); i++)
            if (get(i).getName().equals(name))
                if (hasSearched[i]) {
                    duplicated++;
                    return null;
                } else {
                    hasSearched[i] = true;
                    return get(i);
                }
        return null;
    }

    public String getClassName() {
        return className;
    }
}

/*
 * Copyright (c) 2021 Rocket, Project DeskSorting
 */

package cn.rocket.deksrt.core;

import java.util.ArrayList;

/**
 * @author Rocket
 * @version 0.9-pre
 * @see ArrayList
 */
public class StudentList {
    private final Student[] data;
    private int size = 0;
    private boolean searching = false;
    private boolean[] hasSearched;

    public StudentList(int size) {
        data = new Student[size];
    }

    public boolean add(Student e) {
        if (e == null)
            throw new NullPointerException("e can't be null");
        if (!contains(e)) {
            data[size++] = e;
            return true;
        }
        return false;
    }

    public boolean contains(Student e) {
        if (e == null)
            throw new NullPointerException("e can't be null");
        return indexOf(e) >= 0;
    }


    public int indexOf(Student e) {
        for (int i = 0; i < size; i++)
            if (e.equals(data[i])) {
                return i;
            }
        return -1;
    }

    public void startSearching() {
        if (searching)
            throw new IllegalStateException("Have started searching");
        searching = true;
        hasSearched = new boolean[data.length];
    }

    public void endSearching() {
        if (!searching)
            throw new IllegalStateException("Have ended searching");
        searching = false;
        hasSearched = null;
    }

    public Student searchByName(String name) {
        if (!searching)
            throw new IllegalStateException("You need to invoke startSearching() first");
        for (int i = 0; i < data.length; i++) {
            if (data[i].getName().equals(name))
                if (hasSearched[i])
                    return null;
                else {
                    hasSearched[i] = true;
                    return data[i];
                }
        }
        return null;
    }
}

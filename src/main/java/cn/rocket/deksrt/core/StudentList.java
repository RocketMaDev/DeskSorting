/*
 * Copyright (c) 2021 Rocket, Project DeskSorting
 */

package cn.rocket.deksrt.core;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @param <E> see <code>E</code> in <code>ArrayList</code>
 * @author Rocket
 * @version 0.9-pre
 * @see ArrayList
 */
public class StudentList<E> extends ArrayList<E> {
    private boolean searching;
    private boolean[] hasSearched;

    @Override
    public boolean add(E e) {
        if (!super.contains(e))
            return super.add(e);
        return false;
    }

    public StudentList(int initialCapacity) {
        super(initialCapacity);
        searching = false;
    }

    public StudentList() {
        super();
        searching = false;
    }

    public StudentList(Collection<? extends E> c) {
        super(c);
        searching = false;
    }

    public void startSearching() {
        if (searching)
            throw new IllegalStateException("Have started searching");
        searching = true;
        hasSearched = new boolean[toArray().length];
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
        Object[] elements = toArray();
        if (!(elements[0] instanceof Student))
            throw new IllegalStateException("Only used for Student class!");
        Student[] list = new Student[elements.length];
        for (int i = 0; i < elements.length; i++) {
            list[i] = (Student) elements[i];
        }
        for (int i = 0; i < list.length; i++) {
            if (list[i].getName().equals(name))
                if (hasSearched[i])
                    return null;
                else {
                    hasSearched[i] = true;
                    return list[i];
                }
        }
        return null;
    }
}

/*
 * Copyright (c) 2021 Rocket, Project DeskSorting
 */

package cn.rocket.deksrt.util;

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

    public void startSearching() throws IllegalAccessException {
        if (searching)
            throw new IllegalAccessException("Have started searching");
        searching = true;
        hasSearched = new boolean[toArray().length];
    }

    public void endSearching() throws IllegalAccessException {
        if (!searching)
            throw new IllegalAccessException("Have ended searching");
        searching = false;
        hasSearched = null;
    }

    @SuppressWarnings("unchecked")
    public E searchByName(String name) throws IllegalAccessException {
        if (!searching)
            throw new IllegalAccessException("You need to invoke startSearching() first");
        Object[] elements = toArray();
        if (!(elements[0] instanceof Student))
            throw new IllegalAccessException("Only used for Student class!");
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
                    return (E) list[i];
                }
        }
        return null;
    }
}

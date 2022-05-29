/*
 * Copyright (c) 2022 Rocket, Project DeskSorting
 */

package cn.rocket.deksrt.core.student;

import java.util.ArrayList;

/**
 * @param <E> see <code>E</code> in <code>ArrayList</code>
 * @author Rocket
 * @version 0.9-pre
 * @see ArrayList
 */
public class StudentList<E extends Student> extends ArrayList<E> {
    private boolean searching = false;
    private boolean[] hasSearched;

    @Override
    public boolean add(E e) {
        if (e == null)
            throw new NullPointerException("e can't be null!");
        if (!super.contains(e))
            return super.add(e);
        return false;
    }

    public StudentList(int initialCapacity) {
        super(initialCapacity);
    }

    public void startSearching() {
        if (searching)
            throw new IllegalStateException("Have started searching");
        searching = true;
        hasSearched = new boolean[size()];
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
        for (int i = 0; i < size(); i++) {
            if (get(i).getName().equals(name))
                if (hasSearched[i])
                    return null;
                else {
                    hasSearched[i] = true;
                    return get(i);
                }
        }
        return null;
    }
}

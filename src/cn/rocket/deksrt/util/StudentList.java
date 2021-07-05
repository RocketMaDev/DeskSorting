package cn.rocket.deksrt.util;

import java.util.ArrayList;
import java.util.Collection;

public class StudentList<E> extends ArrayList<E> {
    private boolean searching;
    private boolean[] hasSearched;

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
        Object[] element = toArray();
        if (!(element instanceof Student[]))
            throw new IllegalAccessException("Only used for Student class!");
        Student[] list = (Student[]) element;
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

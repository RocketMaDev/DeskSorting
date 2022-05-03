/*
 * Copyright (c) 2021 Rocket, Project DeskSorting
 */

package cn.rocket.deksrt.core;

import java.util.Iterator;

import static cn.rocket.deksrt.core.Util.*;

/**
 * @author Rocket
 * @version 0.9-pre
 */
public class GridIterator<T> implements Iterable<T> {
    public int x;
    public int y;
    private int pos;
    private final int[] content;

    public class Itr implements Iterator<T> {
        private int index = 0;

        @Override
        public boolean hasNext() {
            return index != content.length;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T next() {
            return (T) new Pair(content[index++]);
        }
    }

    public enum IterType {
        FULL_GRID,
        GRID0,
        GRID1
    }

    public GridIterator(IterType mode) {
        switch (mode) {
            case FULL_GRID:
                content = new int[52];
                pos = 0;
                for (int x = 0; x < 8; x++)
                    for (int y = 0; y < 6; y++) {
                        content[pos] = store(x, y);
                        pos++;
                    }
                content[content.length - 4] = 16;
                content[content.length - 3] = 26;
                content[content.length - 2] = 56;
                content[content.length - 1] = 66;
                break;
            case GRID0:
                content = new int[48];
                pos = 0;
                for (int x = 0; x < 8; x++)
                    for (int y = 0; y < 6; y++) {
                        content[pos] = store(x, y);
                        pos++;
                    }
                break;
            case GRID1:
                content = new int[]{0, 10, 30, 40};
                break;
            default:
                throw new IllegalArgumentException("mode must be one of keywords in IterType.");
        }
        pos = 0;
    }

    public static class Pair {
        private final int x;
        private final int y;

        public int x() {
            return x;
        }

        public int y() {
            return y;
        }

        public Pair(int xy) {
            x = readX(xy);
            y = readY(xy);
        }
    }

    public int getPos() {
        if (content.length != 4)
            throw new IllegalStateException("You can not access pos if you are not working with grid1");
        return pos;
    }

    public int[] toArray() {
        return content;
    }

    @Override
    public Iterator<T> iterator() {
        return new Itr();
    }

}

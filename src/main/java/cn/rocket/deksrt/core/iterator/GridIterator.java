/*
 * Copyright (c) 2022 Rocket, Project DeskSorting
 */

package cn.rocket.deksrt.core.iterator;

import java.util.Iterator;

import static cn.rocket.deksrt.core.Util.store;

/**
 * @author Rocket
 * @version 0.9-pre
 */
public class GridIterator<T extends Pair> implements Iterable<T> {
    private int pos;
    private final int[] content;
    public static final GridIterator<Pair> FULL_GRID = new GridIterator<>(IterType.FULL_GRID);
    public static final GridIterator<Pair> GRID1 = new GridIterator<>(IterType.GRID1);
    public static final GridIterator<Pair> GRID0 = new GridIterator<>(IterType.GRID0);

    private class Itr implements Iterator<T> {
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


    public int getPos() {
        if (content.length != 4)
            throw new IllegalStateException("You can not access pos if you are not working with grid1");
        return pos;
    }

    /**
     * NOTE: 如果{@code unsafe}为{@code true}，那么将返回对象中的引用，造成性能问题
     *
     * @return 返回网格int数组内容
     */
    public int[] toArray(boolean unsafe) {
        if (unsafe)
            return content;
        else {
            int[] res = new int[content.length];
            System.arraycopy(content, 0, res, 0, res.length);
            return res;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new Itr();
    }

}

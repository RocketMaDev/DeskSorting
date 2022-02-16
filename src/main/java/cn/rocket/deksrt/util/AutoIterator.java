/*
 * Copyright (c) 2021 Rocket, Project DeskSorting
 */

package cn.rocket.deksrt.util;

/**
 * @author Rocket
 * @version 0.9-pre
 */
public class AutoIterator {
    public int x;
    public int y;
    private int pos;
    private int[] content;

    public static final int SQUARE_ARRAY = 0;
    public static final int GRID0 = 1;
    public static final int GRID1 = 2;

    public AutoIterator(int mode) throws IllegalArgumentException {
        if (mode < 0 || mode > 2)
            throw new IllegalArgumentException("Unknown mode code");
        switch (mode) {
            case SQUARE_ARRAY:
                content = new int[52];
                pos = 0;
                for (int x = 0; x < 8; x++)
                    for (int y = 0; y < 6; y++) {
                        content[pos] = x * 10 + y;
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
                        content[pos] = x * 10 + y;
                        pos++;
                    }
                break;
            case GRID1:
                content = new int[]{0, 10, 30, 40};
                break;
        }
        pos = 0;
    }

    public void next() throws IllegalAccessException {
        if (!hasNext())
            throw new IllegalAccessException("No more element");
        pos++;
    }

    public boolean hasNextWithUpdate() {
        if (hasNext())
            update();
        return hasNext();
    }

    public void update() {
        x = content[pos] / 10;
        y = content[pos] % 10;
    }

    public boolean hasNext() {
        return pos != content.length;
    }

    public int getPos() throws IllegalAccessException {
        if (content.length != 4)
            throw new IllegalAccessException("You can not access pos if you are not working with grid1");
        return pos;
    }

    public int[] toArray() {
        return content;
    }
}

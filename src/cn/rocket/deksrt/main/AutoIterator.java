package cn.rocket.deksrt.main;

public class AutoIterator {
    int x;
    int y;
    private int pos;
    private int[] content;

    static final int SQUARE_ARRAY = 0;
    static final int GRID0 = 1;
    static final int GRID1 = 2;

    AutoIterator(int mode) throws IllegalArgumentException {
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

    void next() throws IllegalAccessException {
        if (!hasNext())
            throw new IllegalAccessException("No more element");
        pos++;
    }

    boolean hasNextWithUpdate() {
        if (hasNext())
            update();
        return hasNext();
    }

    void update() {
        x = content[pos] / 10;
        y = content[pos] % 10;
    }

    boolean hasNext() {
        return pos != content.length;
    }

    int getPos() throws IllegalAccessException {
        if (content.length!=4)
            throw new IllegalAccessException("You can not access pos if you are not working with grid1");
        return pos;
    }

    int[] toArray() {
        return content;
    }
}

class Test {
    public static void main(String[] args) throws IllegalAccessException {
        for (AutoIterator i = new AutoIterator(AutoIterator.SQUARE_ARRAY); i.hasNextWithUpdate(); i.next()) {
            System.out.println(i.x + "," + i.y);
        }
    }
}
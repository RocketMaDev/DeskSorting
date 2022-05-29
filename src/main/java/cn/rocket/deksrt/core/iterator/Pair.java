/*
 * Copyright (c) 2022 Rocket, Project DeskSorting
 */

package cn.rocket.deksrt.core.iterator;

import static cn.rocket.deksrt.core.Util.readX;
import static cn.rocket.deksrt.core.Util.readY;

public class Pair {

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

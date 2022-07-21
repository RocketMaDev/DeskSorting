/*
 * Copyright (c) 2022 Rocket, Project DeskSorting
 */

package cn.rocket.deksrt.gui.ctrler;

/**
 * 通过FXML创建的窗口的控制器的接口
 *
 * @author Rocket
 * @version 1.0.8
 * @since 1.0.8
 */
public interface Controller {
    /**
     * 锁定窗口
     */
    void lockWindow();

    /**
     * 解锁窗口
     */
    void unlockWindow();
}

/*
 * Copyright (c) 2022 Rocket, Project DeskSorting
 */

package cn.rocket.deksrt.core.iterator;

/**
 * 定义学生网格迭代方式
 *
 * @author Rocket
 * @version 1.0.8
 * @since 1.0.8
 */
public enum IterType {
    /**
     * 迭代所有网格
     */
    FULL_GRID,

    /**
     * 迭代讲台前方同学
     */
    GRID0,

    /**
     * 迭代讲台两侧同学
     */
    GRID1
}

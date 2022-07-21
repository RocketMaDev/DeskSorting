/*
 * Copyright (c) 2022 Rocket, Project DeskSorting
 */

package cn.rocket.deksrt.cli;

import cn.rocket.deksrt.core.LocalURL;
import cn.rocket.deksrt.gui.Launcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * 程序启动主类
 *
 * @author Rocket
 * @version 1.0.8
 * @since 0.9-pre
 */
public class Main {
    /**
     * The main entrance of the program.
     *
     * @param args [0]:--reset?delete student.info
     */
    public static void main(String[] args) {
        Logger logger = LogManager.getLogger(Main.class);

        String jreVer = System.getProperty("java.runtime.version");
        try {
            jreVer = jreVer.substring(0, jreVer.indexOf('.', 2));
        } catch (StringIndexOutOfBoundsException e) {
            logger.warn("Unknown JRE version! Try to run with JRE 1.11. Unexpected errors may be emitted.");
            jreVer = "1.11";
        }
        if (args == null || args.length == 0) {
            if (jreVer.equals("1.8")) {
                logger.info("Running in JRE 1.8. Suitable.");
            } else {
                logger.fatal("Running in JRE " + jreVer + "! Please run this program in 1.8.");
                logger.fatal("要使用图形化界面，只能使用java8！请使用java8启动此程序，或下载用于java17的版本（如果有）");
                System.exit(1);
            }
            Launcher.launchSelf();
        } else {
            //TODO More things todo

            // 重置 参数
            if (args[0].equals("--reset")) {
                File stuInfoFile = new File(LocalURL.STU_INFO);
                if (stuInfoFile.exists())
                    //noinspection ResultOfMethodCallIgnored
                    stuInfoFile.delete();
                System.exit(0);
            }

            logger.fatal("尚未完成CLI部分");
        }
    }
}

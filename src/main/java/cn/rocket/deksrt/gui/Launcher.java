/*
 * Copyright (c) 2022 Rocket, Project DeskSorting
 */

package cn.rocket.deksrt.gui;

import cn.rocket.deksrt.core.LocalURL;
import cn.rocket.deksrt.core.Vars;
import cn.rocket.deksrt.gui.ctrler.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

/**
 * @author Rocket
 * @version 1.0
 */
public class Launcher extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Vars.stageMap.put(MainController.class, primaryStage);
//        File propertiesFile = new File(GlobalVariables.env+"config.properties");
        if (!FileAccess.importStuInfo()) {
            String errors = FileAccess.scanStuInfo();
            if (errors == null) {
                FileAccess.showIntroduction(primaryStage); // TODO 这不应该放在FileAccess中
                return;
            } else
                System.out.println("errors:" + errors);
        }
        FileAccess.exportStuInfo();
        URL fxml = getClass().getResource(LocalURL.MAIN_WINDOW_FXML);
        assert fxml != null; //TODO 记得优化
        Parent root = FXMLLoader.load(fxml);
        primaryStage.setTitle("排座位");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }





    /**
     * The main entrance of the program.
     *
     * @param args [0]:--reset?delete student.info
     */
    public static void main(String[] args) {
        // 重置 参数
        if (args.length != 0 && args[0].equals("--reset")) {
            File stuInfoFile = new File(LocalURL.STU_INFO);
            if (stuInfoFile.exists())
                //noinspection ResultOfMethodCallIgnored
                stuInfoFile.delete();
            System.exit(0);
        }

        launch(args);
    }
}

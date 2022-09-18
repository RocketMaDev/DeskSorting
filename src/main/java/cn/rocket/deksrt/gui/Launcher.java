/*
 * Copyright (c) 2022 Rocket, Project DeskSorting
 */

package cn.rocket.deksrt.gui;

import cn.rocket.deksrt.core.LocalURL;
import cn.rocket.deksrt.core.Vars;
import cn.rocket.deksrt.gui.ctrler.MainController;
import com.jfoenix.controls.JFXButton;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;

/**
 * GUI启动类
 *
 * @author Rocket
 * @version 1.0.8
 * @since 0.9-pre
 */
public class Launcher extends Application {
    public static void launchSelf() {
        Application.launch(Launcher.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Vars.putStage(MainController.class, primaryStage);
//        File propertiesFile = new File(GlobalVariables.env+"config.properties");
        if (!FileAccess.importStuInfo()) {
            String errors = FileAccess.scanStuInfo();
            if (errors == null) {
                showIntroduction(primaryStage);
                return;
            } else
                System.out.println("errors:" + errors);
        }
        FileAccess.exportStuInfo();
        URL fxml = getClass().getResource(LocalURL.MAIN_FXML);
        assert fxml != null; //TODO 记得优化
        Parent root = FXMLLoader.load(fxml);
        primaryStage.setTitle("排座位");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void showIntroduction(Stage primaryStage) {
        JFXButton btn = new JFXButton("关闭");
        btn.setButtonType(JFXButton.ButtonType.RAISED);
        btn.setTextFill(Paint.valueOf("white"));
        btn.setFont(Font.font(25));
        btn.setOnAction(event -> primaryStage.close());
        btn.setBackground(new Background(new BackgroundFill(Paint.valueOf("dodgerblue"), null, null)));
        AnchorPane pane = new AnchorPane();
        AnchorPane.setTopAnchor(btn, 100.0);
        AnchorPane.setRightAnchor(btn, 100.0);
        pane.setPrefSize(970, 700);
        pane.getChildren().addAll(new ImageView(new Image(LocalURL.INTRO)), btn);
        primaryStage.setScene(new Scene(pane));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

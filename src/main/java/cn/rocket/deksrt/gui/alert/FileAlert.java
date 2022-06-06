/*
 * Copyright (c) 2022 Rocket, Project DeskSorting
 */

package cn.rocket.deksrt.gui.alert;

import cn.rocket.deksrt.core.LocalURL;
import cn.rocket.deksrt.core.Vars;
import cn.rocket.deksrt.gui.ctrler.Controller;
import cn.rocket.deksrt.gui.ctrler.MainController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

/**
 * @author Rocket
 * @version 0.9-pre
 */
public class FileAlert implements Alert, Controller {
    private static final double GAP = 25;
    private final Label label = new Label();
    private final AnchorPane pane = new AnchorPane();
    private Stage stage;
    private final FileChooser chooser = new FileChooser();
    private final JFXProgressBar bar = new JFXProgressBar(-1.0);
    private boolean export;

    public FileAlert(Controller ctrler, boolean export) {
        this.export = export;
        pane.setPrefSize(164, 480);

        AnchorPane.setTopAnchor(label, GAP);
        AnchorPane.setLeftAnchor(label, GAP);

        JFXTextField field = new JFXTextField();
        AnchorPane.setTopAnchor(field, GAP);
        field.setAlignment(Pos.CENTER_RIGHT);

        JFXButton ok = new JFXButton("确定");
        AnchorPane.setBottomAnchor(ok, GAP);
        AnchorPane.setRightAnchor(ok, GAP);
        ok.setDefaultButton(true);
        ok.setOnAction(event -> {
            if (!(ctrler instanceof MainController))
                throw new IllegalArgumentException("ctrler should be a child of MainController");
            String path = field.getText();
            if (this.export) {
                File toExport = new File(path);
                try {
                    if (!toExport.exists() && !toExport.getParentFile().exists() && !toExport.getParentFile().mkdirs())
                        throw new IOException("Failed to create file");
                    Vars.getObj(MainController.TYPE).exportTable(
                            new BufferedOutputStream(Files.newOutputStream(toExport.toPath())));
                } catch (IOException | InvalidPathException e) {
                    Alert alert = new SimpleAlert("无法保存文件！", false, this);
                    alert.show();
                }
            } else
                try (BufferedInputStream is = new BufferedInputStream(Files.newInputStream(Paths.get(path)))) {
                    Vars.getObj(MainController.TYPE).impl_importTable(is);
                } catch (IOException | InvalidPathException e) {
                    Alert alert = new SimpleAlert("无法打开文件！", false, this);
                    alert.show();
                }
            //TODO complete refactor
        });

        JFXButton cancel = new JFXButton("取消");
        AnchorPane.setBottomAnchor(cancel, GAP);
        cancel.setOnAction(event -> {
            ctrler.unlockWindow();
            stage.close();
        });

        JFXButton open = new JFXButton("");
        AnchorPane.setRightAnchor(open, GAP);
        AnchorPane.setTopAnchor(open, GAP);
        open.setPrefSize(32.0, 32.0);
        open.setGraphic(new ImageView(new Image(LocalURL.FOLDER_ICON)));
        open.setOnAction(event -> {
            File selected;
            if (this.export)
                selected = chooser.showSaveDialog(stage);
            else
                selected = chooser.showOpenDialog(stage);
            field.setText(selected != null ? selected.getAbsolutePath() : "");
            field.appendText("");
        });

        chooser.setTitle("选择您的座位表文件：");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("基于OOXML的新Excel文件", "*.xlsx")
        );

        AnchorPane.setLeftAnchor(bar, GAP);
        AnchorPane.setBottomAnchor(bar, GAP * 1.25);
        bar.setPrefSize(5 * GAP, GAP / 2);
        bar.setVisible(false);

        pane.setOnKeyTyped(event -> {
            if (event.getCode() == KeyCode.ENTER)
                ok.fire();
        });
    }

    public void reset(boolean export) {
        if (export) {
            label.setText("导出至...:");
            stage.setTitle("导出");
        } else {
            label.setText("从...导入:");
            stage.setTitle("导入");
        }
        this.export = export;
        bar.setVisible(false);
    }

    public void fileChooserLinker() {
        //TODO test it!
    }

    @Override
    public void lockWindow() {
        stage.setOnCloseRequest(Event::consume);
        pane.setDisable(true);
    }

    @Override
    public void unlockWindow() {
        stage.setOnCloseRequest(event -> {
        });
        pane.setDisable(false);
    }

    @Override
    public void show() {
        if (stage == null) {
            stage = new Stage();
//TODO
        }
        stage.show();
    }

    @Override
    public void close() {
        stage.close();
    }
}

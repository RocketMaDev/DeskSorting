/*
 * Copyright (c) 2022 Rocket, Project DeskSorting
 */

package cn.rocket.deksrt.gui;

import cn.rocket.deksrt.core.LocalURL;
import cn.rocket.deksrt.core.Vars;
import cn.rocket.deksrt.gui.alert.Alert;
import cn.rocket.deksrt.gui.ctrler.Controller;
import cn.rocket.deksrt.gui.ctrler.MainController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.*;

/**
 * @author Rocket
 * @version 0.9-pre
 */
public class FileAlert implements Alert, Controller {
    private static boolean isFileChooserCreated = false;
    @FXML
    JFXButton iBtnCancel;
    @FXML
    Label iLal;
    @FXML
    JFXTextField iodTxtF;
    @FXML
    JFXButton iconBtn;
    @FXML
    JFXButton iBtnOK;


    @FXML
    void initialize() {
        iodTxtF.setText("");
        iconBtn.setText("");
        iconBtn.setGraphic(new ImageView(new Image(LocalURL.FOLDER_ICON)));
    }

    @FXML
    void okM() throws IOException, IllegalAccessException, InvalidFormatException {
        String address = iodTxtF.getText();
        if (address.lastIndexOf(".xlsx") == address.length() - ".xlsx".length())
            switch (Vars.stageMap.get(FileAccess.class).getTitle()) {
                case "导入":
                    File importFile = new File(address);
                    if (importFile.exists())
                        ((MainController) Vars.objMap.get(MainController.class)).impl_importTable(new BufferedInputStream(new FileInputStream(address)));
                    else
                        throw new IOException("The file to import does NOT exist!");
                    break;
                case "导出":
                    File exportFile = new File(address);
                    if (!exportFile.exists()) {
                        //noinspection ResultOfMethodCallIgnored
                        exportFile.getParentFile().mkdirs();
                        //noinspection ResultOfMethodCallIgnored
                        exportFile.createNewFile();
                    }
                    ((MainController) Vars.objMap.get(MainController.class)).exportTable(new BufferedOutputStream(new FileOutputStream(address)));
            }
        Vars.stageMap.get(FileAccess.class).close();
    }

    @FXML
    void cancelM() {
        ((MainController) Vars.objMap.get(MainController.class)).unlockMainWindow();
        Vars.stageMap.get(FileAccess.class).close();
    }

    @FXML
    void fileChooserLinker() {
        if (isFileChooserCreated)
            return;
        isFileChooserCreated = true;
        FileChooser fc = new FileChooser();
        fc.setTitle("选择您的座位表文件：");
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("New Excel File", "*.xlsx")
        );
        File selected;
        if (Vars.stageMap.get(FileAccess.class).getTitle().equals("导入"))
            selected = fc.showOpenDialog(Vars.stageMap.get(FileAccess.class));
        else
            selected = fc.showSaveDialog(Vars.stageMap.get(FileAlert.class));
        isFileChooserCreated = false;
        iodTxtF.setText(selected != null ? selected.getAbsolutePath() : "");
        iodTxtF.appendText("");
    }

    @FXML
    void detectEnter(KeyEvent keyEvent) throws IOException, InvalidFormatException, IllegalAccessException {
        if (keyEvent.getCode().equals(KeyCode.ENTER))
            okM();
    }

    @Override
    public void lockWindow() {

    }

    @Override
    public void unlockWindow() {

    }
}

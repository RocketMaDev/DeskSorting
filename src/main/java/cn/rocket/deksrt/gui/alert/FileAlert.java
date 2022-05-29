/*
 * Copyright (c) 2022 Rocket, Project DeskSorting
 */

package cn.rocket.deksrt.gui.alert;

import cn.rocket.deksrt.core.LocalURL;
import cn.rocket.deksrt.core.Vars;
import cn.rocket.deksrt.gui.FileAccess;
import cn.rocket.deksrt.gui.ctrler.Controller;
import cn.rocket.deksrt.gui.ctrler.MainController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.*;

/**
 * @author Rocket
 * @version 0.9-pre
 */
public class FileAlert implements Alert, Controller {
    private static final double GAP = 25;
    private JFXButton cancel = new JFXButton("取消");
    private Label label = new Label();
    private JFXTextField field = new JFXTextField();
    private JFXButton open = new JFXButton();
    private JFXButton ok = new JFXButton("确定");
    private AnchorPane pane = new AnchorPane();
    private Stage stage;

    public FileAlert(Controller ctrler, EventHandler<Event> okHandler, EventHandler<Event> cancelHandler) {
        pane.setPrefSize(164, 480);

        AnchorPane.setTopAnchor(label, GAP);
        AnchorPane.setLeftAnchor(label, GAP);

        AnchorPane.setBottomAnchor(ok, GAP);
        AnchorPane.setRightAnchor(ok, GAP);
        ok.setDefaultButton(true);

        AnchorPane.setBottomAnchor(cancel, GAP);

        AnchorPane.setRightAnchor(open, GAP);
        AnchorPane.setTopAnchor(open, GAP);
        open.setPrefSize(32.0, 32.0);

        open.setOnKeyTyped(event -> {
            if (event.getCode() == KeyCode.ENTER)
                ok.fire();
        });
    }

    public void initialize() {
        iodTxtF.setText("");
        iconBtn.setText("");
        iconBtn.setGraphic(new ImageView(new Image(LocalURL.FOLDER_ICON)));
    }

    public void okM() throws IOException, IllegalAccessException, InvalidFormatException {
        String address = iodTxtF.getText();
        if (address.lastIndexOf(".xlsx") == address.length() - ".xlsx".length())
            switch (Vars.stageMap.get(FileAccess.class).getTitle()) {
                case "导入":
                    File importFile = new File(address);
                    if (importFile.exists())
                        Vars.getObj(MainController.TYPE).impl_importTable(new BufferedInputStream(new FileInputStream(address)));
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
                    Vars.getObj(MainController.TYPE).exportTable(new BufferedOutputStream(new FileOutputStream(address)));
            }
        Vars.stageMap.get(FileAccess.class).close();
    }

    public void cancelM() {
        ((MainController) Vars.objMap.get(MainController.class)).unlockMainWindow();
        Vars.stageMap.get(FileAccess.class).close();
    }

    public void fileChooserLinker() {
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

    public void detectEnter(KeyEvent keyEvent) throws IOException, InvalidFormatException, IllegalAccessException {
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

package cn.rocket.deksrt.main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;

public class IEportDialog {
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
        URL iconU = IEportDialog.class.getResource("/cn/rocket/deksrt/resource/folder.png");
        iconBtn.setText("");
        iconBtn.setGraphic(new ImageView(String.valueOf(iconU)));
    }

    @FXML
    void okM(ActionEvent actionEvent) {
        String address = iodTxtF.getText();
        if (address.lastIndexOf(".xlsx") == address.length() - ".xlsx".length() && !new File(address).exists())
            return;

    }

    @FXML
    void cancelM(ActionEvent actionEvent) {
        MainWindow.iodS.close();
    }

    @FXML
    void fileChooserLinker(ActionEvent actionEvent) {
        if (isFileChooserCreated)
            return;
        isFileChooserCreated = true;
        FileChooser fc = new FileChooser();
        fc.setTitle("选择您的座位表文件：");
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("New Excel File", "*.xlsx")
        );
        File selected;
        if (MainWindow.iodS.getTitle().equals("导入"))
            selected = fc.showOpenDialog(MainWindow.iodS);
        else
            selected = fc.showSaveDialog(MainWindow.iodS);
        isFileChooserCreated = false;
        iodTxtF.setText(selected != null ? selected.getAbsolutePath() : "");
    }
}

package cn.rocket.deksrt.main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

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
        URL iconU = IEportDialog.class.getResource("/cn/rocket/deksrt/resource/folder.png");
        iconBtn.setText("");
        iconBtn.setGraphic(new ImageView(String.valueOf(iconU)));
    }

    @FXML
    void okM(ActionEvent actionEvent) {

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
                new FileChooser.ExtensionFilter("New Excel File", "*.xlsx"),
                new FileChooser.ExtensionFilter("Legacy Excel File", "*.xls")
        );
    }
}

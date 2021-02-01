package cn.rocket.deksrt.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.net.URL;

public class IOportDialog {
	private static boolean isFileChooserCreated = false;
	@FXML Button iBtnCancel;
	@FXML Label iLal;
	@FXML TextField iodTxtF;
	@FXML Button iconBtn;
	@FXML Button iBtnOK;

	@FXML
	void initialize(){
		URL iconU = IOportDialog.class.getResource("/cn/rocket/deksrt/resource/folder.png");
		iconBtn.setGraphic(new ImageView(String.valueOf(iconU)));
	}

	@FXML void okM(ActionEvent actionEvent) {

	}

	@FXML void cancelM(ActionEvent actionEvent) {

	}

	@FXML void fileChooserLinker(ActionEvent actionEvent) {
		if(isFileChooserCreated)
			return;
		isFileChooserCreated = true;
		FileChooser fc = new FileChooser();
		fc.setTitle("选择您的座位表文件：");
		fc.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("New Excel File","*.xlsx"),
				new FileChooser.ExtensionFilter("Legacy Excel File","*.xls")
		);
	}
}

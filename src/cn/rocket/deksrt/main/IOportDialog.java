package cn.rocket.deksrt.main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.net.URL;

public class IOportDialog {
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
}

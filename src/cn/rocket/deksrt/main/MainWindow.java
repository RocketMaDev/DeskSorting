package cn.rocket.deksrt.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * @author Rocket
 * @version 1.0
 */
public class MainWindow {
	private Stage iodS;
	private Parent iod;

	@FXML void initialize(){
		iod = null;
		try {
			iod = FXMLLoader.load(MainWindow.class.getResource("/cn/rocket/deksrt/resource/IOportDialog.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		iodS = new Stage();
		iodS.setScene(new Scene(Objects.requireNonNull(iod)));
		iodS.setAlwaysOnTop(true);
	}

	@FXML
	Button importB;
	@FXML Button exportB;
	@FXML Button radSort;
	@FXML Button MSLSort;
	@FXML GridPane grid0;
	@FXML TextField headInfo;
	@FXML GridPane grid1;

	@FXML void impM(ActionEvent actionEvent) {
		if(iodS.isShowing())
			return;
		iodS.setTitle("导入");
		Label il = (Label) iod.lookup("#iLal");
		il.setText("从...导入:");
		iodS.show();
	}

	@FXML void expM(ActionEvent actionEvent) {
		if(iodS.isShowing())
			return;
		iodS.setTitle("导出");
		Label il = (Label) iod.lookup("#iLal");
		il.setText("导出至...:");
		iodS.show();
	}

	@FXML void radsM(ActionEvent actionEvent) {

	}

	@FXML void mslsM(ActionEvent actionEvent) {

	}
}

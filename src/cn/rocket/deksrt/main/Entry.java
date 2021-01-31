package cn.rocket.deksrt.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Rocket
 * @version 1.0
 */
public class Entry extends Application {
	public static Stage currentStage;
	 static Parent GRoot;

	@Override
	public void start(Stage primaryStage) throws Exception {
		currentStage = primaryStage;
		Parent root = FXMLLoader.load(getClass().getResource("/cn/rocket/deksrt/resource/MainWindow.fxml"));
		GRoot = root;
		primaryStage.setTitle("排座位");
		primaryStage.setScene(new Scene(root, 600,500));
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}
}

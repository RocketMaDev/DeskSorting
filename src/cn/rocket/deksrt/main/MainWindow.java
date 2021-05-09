package cn.rocket.deksrt.main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * @author Rocket
 * @version 1.0
 */
public class MainWindow {
    static Stage iodS;
    private Parent iod;
    private Label[][] names;

    @FXML
    JFXButton importB;
    @FXML
    JFXButton exportB;
    @FXML
    JFXButton radSort;
    @FXML
    JFXButton MSLSort;
    @FXML
    GridPane grid0;
    @FXML
    JFXTextField headInfo;
    @FXML
    GridPane grid1;

    @FXML
    void initialize() {
        iod = null;
        try {
            iod = FXMLLoader.load(MainWindow.class.getResource("/cn/rocket/deksrt/resource/IOportDialog.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        iodS = new Stage();
        iodS.setResizable(false);
        iodS.setScene(new Scene(Objects.requireNonNull(iod)));
        iodS.setAlwaysOnTop(true);

        grid0.getStylesheets().add(MainWindow.class.getResource("/cn/rocket/deksrt/resource/style.css").toExternalForm());
        grid1.getStylesheets().add(MainWindow.class.getResource("/cn/rocket/deksrt/resource/style.css").toExternalForm());
        names = new Label[8][7];
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 7; j++) {
                names[i][j] = new Label("");
//                names[i][j].setOnMousePressed(value -> {
//
//                });
                if ((i == 2 || i == 5) && j == 6)
                    grid1.add(names[i][j], i == 2 ? 0 : 2, 0);
                else if(j!=6)
                    grid0.add(names[i][j], i, j);

            }
    }

    @FXML
    void impM(ActionEvent actionEvent) {
        if (iodS.isShowing())
            return;
        iodS.setTitle("导入");
        Label il = (Label) iod.lookup("#iLal");
        il.setText("从...导入:");
        iodS.show();
    }

    @FXML
    void expM(ActionEvent actionEvent) {
        if (iodS.isShowing())
            return;
        iodS.setTitle("导出");
        Label il = (Label) iod.lookup("#iLal");
        il.setText("导出至...:");
        iodS.show();
    }

    @FXML
    void radsM(ActionEvent actionEvent) {

    }

    @FXML
    void mslsM(ActionEvent actionEvent) {
        Label[][] shadow = new Label[10][7];
        for (int i = 1; i < 3; i++)
            for (int j = 0; j < 8; j++)
                shadow[j+2][i-1] = names[j][i];
        for (int j = 0; j < 8; j++)
            shadow[j+2][2] = names[j][0];

        for (int i = 3; i < 6; i++)
            for (int j = 0; j < 8; j++)
                shadow[j+2][i-1] = names[j][i];
        for (int j = 0; j < 8; j++)
            shadow[j+2][5] = names[j][3];


        
    }
}

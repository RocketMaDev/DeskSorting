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
import javafx.scene.paint.Paint;
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
    private JFXButton[][] btns;
    private JFXTextField[][] textfields;
    private Student[][] students;

    @FXML
    JFXButton importB;
    @FXML
    JFXButton exportB;
    @FXML
    JFXButton randSort;
    @FXML
    JFXButton MSLSort;
    @FXML
    GridPane grid0;
    @FXML
    JFXTextField headInfo;
    @FXML
    GridPane grid1;

    @FXML
    void initialize() throws IllegalAccessException {
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

//        grid0.getStylesheets().add(MainWindow.class.getResource("/cn/rocket/deksrt/resource/style.css").toExternalForm());
//        grid1.getStylesheets().add(MainWindow.class.getResource("/cn/rocket/deksrt/resource/style.css").toExternalForm());
        students = new Student[7][8];
        btns = new JFXButton[7][8];
        textfields = new JFXTextField[7][8];

        for (AutoIterator i = new AutoIterator(AutoIterator.SQUARE_ARRAY); i.hasNextWithUpdate(); i.next()) {
            btns[i.y][i.x] = new JFXButton();
            btns[i.y][i.x].setPrefSize(80, 45);

            textfields[i.y][i.x] = new JFXTextField();
            textfields[i.y][i.x].setVisible(false);
            textfields[i.y][i.x].setDisable(true);
        }

//        for (int y = 0; y < 6; y++)
//            for (int x = 0; x < 8; x++) {
//                grid0.add(btns[y][x], x, y);
//                grid0.add(textfields[y][x], x, y);
//            }
        for (AutoIterator i = new AutoIterator(AutoIterator.GRID0); i.hasNextWithUpdate(); i.next()) {
            grid0.add(btns[i.y][i.x], i.x, i.y);
            grid0.add(textfields[i.y][i.x], i.x, i.y);
        }
//        for (int i : SEAT_MAP) {
//            grid1.add(btns[6][i], i, 0);
//            grid1.add(textfields[6][i], i, 0);
//        }
        for (AutoIterator i = new AutoIterator(AutoIterator.GRID1); i.hasNextWithUpdate(); i.next()) {
            int col = i.x < 3 ? i.x + 1 : i.x + 2;
            grid1.add(btns[6][col], i.x, i.y);
            grid1.add(textfields[6][col], i.x, i.y);
        }

    }

    @FXML
    void impM(ActionEvent actionEvent) {
        if (iodS.isShowing())
            return;
        iodS.setTitle("导入");
        Label il = (Label) iod.lookup("#iLal");
        il.setText("从...导入:");
        JFXTextField jtf = (JFXTextField) iod.lookup("#iodTxtF");
        jtf.setText("");
        iodS.show();
    }

    @FXML
    void expM(ActionEvent actionEvent) {
        if (iodS.isShowing())
            return;
        iodS.setTitle("导出");
        Label il = (Label) iod.lookup("#iLal");
        il.setText("导出至...:");
        JFXTextField jtf = (JFXTextField) iod.lookup("#iodTxtF");
        jtf.setText("");
        iodS.show();
    }

    @FXML
    void randM(ActionEvent actionEvent) {
    }

    @FXML
    void mslM(ActionEvent actionEvent) {
        Student[][] shadow = new Student[7][10];
        for (int i = 1; i < 3; i++)
            System.arraycopy(students[i], 0, shadow[i - 1], 2, 8);
        System.arraycopy(students[0], 0, shadow[2], 2, 8);

        for (int i = 3; i < 6; i++)
            System.arraycopy(students[i], 0, shadow[i - 1], 2, 8);
        System.arraycopy(students[3], 0, shadow[5], 2, 8);

        shadow[6][2] = students[6][6];
        shadow[6][1] = students[6][5];
        shadow[6][5] = students[6][1];
        shadow[6][6] = students[6][2];

        for (int y = 0; y < 6; y++) {
            System.arraycopy(shadow[y],2,students[y],2,6);
            System.arraycopy(shadow[y],8,students[y],0,2);
        }
        System.arraycopy(shadow[6],1,students[6],1,6);
        updateTable();
    }

    private void updateTable(int x, int y) {
        if (students[y][x] == null)
            return;
        JFXButton btn = btns[y][x];
        btn.setText(students[y][x].getName());
        btn.setTextFill(students[y][x].isBoarding()?Paint.valueOf("BLUE"):Paint.valueOf("GREEN"));
        if (!btn.isVisible())
            btn.setVisible(true);
        if (textfields[y][x].isVisible())
            textfields[y][x].setVisible(false);
    }

    private void updateTable() {
        try {
            for (AutoIterator i = new AutoIterator(AutoIterator.SQUARE_ARRAY); i.hasNextWithUpdate(); i.next())
                updateTable(i.x,i.y);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

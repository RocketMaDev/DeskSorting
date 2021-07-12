package cn.rocket.deksrt.main;

import cn.rocket.deksrt.util.AutoIterator;
import cn.rocket.deksrt.util.Student;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
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
    private int index;
    private static final Font size15 = new Font(15);
    private static final Font size12 = new Font(12);

    @FXML
    JFXCheckBox quickSwapCB;
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


    private class ButtonEventHandler implements EventHandler {

        private final int x;
        private final int y;

        public ButtonEventHandler(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void handle(Event event) {
            if (quickSwapCB.isSelected())
                if (index == -1) {
                    index = x * 10 + y;
                    btns[y][x].setDisable(true);
                } else {
                    int x0 = index / 10;
                    int y0 = index % 10;
                    index = -1;
                    Student t = students[y0][x0];
                    students[y0][x0] = students[y][x];
                    students[y][x] = t;
                    updateTable(x0, y0);
                    updateTable(x, y);
                }
            else
                return;
        }
    }

    @FXML
    void initialize() throws IllegalAccessException {
        iod = null;
        try {
            iod = FXMLLoader.load(Objects.requireNonNull(
                    MainWindow.class.getResource("/cn/rocket/deksrt/resource/IEportDialog.fxml")
            ));
        } catch (IOException e) {
            e.printStackTrace();
        }
        iodS = new Stage();
        iodS.setResizable(false);
        iodS.setScene(new Scene(Objects.requireNonNull(iod)));
        iodS.setAlwaysOnTop(true);

        students = new Student[7][8];
        btns = new JFXButton[7][8];
        textfields = new JFXTextField[7][8];

        for (AutoIterator i = new AutoIterator(AutoIterator.SQUARE_ARRAY); i.hasNextWithUpdate(); i.next()) {
            JFXButton btn = new JFXButton();
            btns[i.y][i.x] = btn;
            btn.setPrefSize(80, 45);
            btn.setOnAction(new ButtonEventHandler(i.x, i.y));
            btn.setFont(size15);

            JFXTextField textField = new JFXTextField();
            textfields[i.y][i.x] = textField;
            textField.setFont(size15);
            textField.setVisible(false);
            textField.setDisable(true);
        }
        for (AutoIterator i = new AutoIterator(AutoIterator.GRID0); i.hasNextWithUpdate(); i.next()) {
            grid0.add(btns[i.y][i.x], i.x, i.y);
            grid0.add(textfields[i.y][i.x], i.x, i.y);
        }
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
        LinkedList<Student> stus = new LinkedList<>(GlobalVariables.stuInfo);
        int[] t0 = new AutoIterator(AutoIterator.SQUARE_ARRAY).toArray();
        Integer[] t1 = new Integer[t0.length];
        for (int i = 0; i < t0.length; i++)
            t1[i] = t0[i];
        LinkedList<Integer> seats = new LinkedList<>(Arrays.asList(t1));
        students = new Student[7][8];
        int length = stus.size();
        for (int i = 0; i < length; i++) {
            int pstu = (int) (Math.random() * (length - i));
            int pseat = (int) (Math.random() * seats.size());
            int xy = seats.get(pseat);
            students[xy % 10][xy / 10] = stus.get(pstu);
            stus.remove(pstu);
            seats.remove(pseat);
        }
        updateTable();
    }

    @FXML
    void mslM(ActionEvent actionEvent) {
        Student[][] shadow = new Student[7][10];
        for (int i = 1; i < 3; i++)
            System.arraycopy(students[i], 0, shadow[i - 1], 2, 8);
        System.arraycopy(students[0], 0, shadow[2], 2, 8);

        for (int i = 4; i < 6; i++)
            System.arraycopy(students[i], 0, shadow[i - 1], 2, 8);
        System.arraycopy(students[3], 0, shadow[5], 2, 8);

        shadow[6][2] = students[6][6];
        shadow[6][1] = students[6][5];
        shadow[6][5] = students[6][1];
        shadow[6][6] = students[6][2];

        for (int y = 0; y < 6; y++) {
            System.arraycopy(shadow[y], 2, students[y], 2, 6);
            System.arraycopy(shadow[y], 8, students[y], 0, 2);
        }
        System.arraycopy(shadow[6], 1, students[6], 1, 6);
        updateTable();
    }

    void importTable(File tableFile) throws IllegalAccessException {
        GlobalVariables.stuInfo.startSearching();
        DataFormatter formatter = new DataFormatter();
        XSSFWorkbook ctWorkbook = null;
        try {
            OPCPackage opcPackage = OPCPackage.open(tableFile);
            ctWorkbook = new XSSFWorkbook(opcPackage);
        } catch (InvalidFormatException | IOException e) {
            e.printStackTrace();
        }
        XSSFSheet table = Objects.requireNonNull(ctWorkbook).getSheetAt(0);

    }

    void exportTable(String exportPath) {

    }

    private void updateTable(int x, int y) {
        JFXButton btn = btns[y][x];
        if (students[y][x] == null) {
            btn.setText("");
        } else {
            btn.setText(students[y][x].getName());
            if (btn.getText().length()>4)
                btn.setFont(size12);
            else
                btn.setFont(size15);
            btn.setTextFill(students[y][x].isBoarding() ? Paint.valueOf("BLUE") : Paint.valueOf("GREEN"));
        }
        if (btn.isDisable())
            btn.setDisable(false);
        if (!btn.isVisible())
            btn.setVisible(true);
        if (textfields[y][x].isVisible())
            textfields[y][x].setVisible(false);
    }

    private void updateTable() {
        try {
            for (AutoIterator i = new AutoIterator(AutoIterator.SQUARE_ARRAY); i.hasNextWithUpdate(); i.next())
                updateTable(i.x, i.y);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void quickSwapM(ActionEvent actionEvent) {
        index = -1;
        updateTable();
    }
}

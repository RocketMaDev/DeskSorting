/*
 * Copyright (c) 2022 Rocket, Project DeskSorting
 */

package cn.rocket.deksrt.main;

import cn.rocket.deksrt.util.AutoIterator;
import cn.rocket.deksrt.util.Student;
import cn.rocket.deksrt.util.StudentList;
import cn.rocket.deksrt.util.Util;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.*;

import java.awt.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * @author Rocket
 * @version 0.9-pre
 */
public class MainWindow {
    private Stage iodS;
    private Parent iod;
    private JFXButton[][] btns;
    private JFXTextField[][] textfields;
    private Student[][] students;
    private Student[][] saved;
    private int index;
    private static final Font SIZE18 = new Font(18);
    private static final Font SIZE15 = new Font(15);

    @FXML
    AnchorPane anchorPane;
    @FXML
    JFXButton copyright;
    @FXML
    JFXButton swapClass;
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

    /**
     * A private internal class to swap two seat in the seat table.
     *
     * @param <T> ???
     */
    private class ButtonEventHandler<T extends Event> implements EventHandler<T> {
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
            else {
                // TODO 处理普通点按
            }
        }


    }

    @FXML
    void initialize() throws IllegalAccessException {
        GlobalVariables.mwObj = this;
        iod = null;
        try {
            iod = FXMLLoader.load(Objects.requireNonNull(
                    MainWindow.class.getResource("/cn/rocket/deksrt/resource/IEportDialog.fxml")
            ));
        } catch (IOException e) {
            e.printStackTrace();
        }
        iodS = new Stage();
        GlobalVariables.iodS = iodS;
        iodS.setResizable(false);
        iodS.setScene(new Scene(Objects.requireNonNull(iod)));
        iodS.setAlwaysOnTop(true);
        iodS.setOnCloseRequest(event -> unlockMainWindow());

        students = new Student[7][8];
        btns = new JFXButton[7][8];
        textfields = new JFXTextField[7][8];

        for (AutoIterator i = new AutoIterator(AutoIterator.SQUARE_ARRAY); i.hasNextWithUpdate(); i.next()) {
            JFXButton btn = new JFXButton();
            btns[i.y][i.x] = btn;
            btn.setPrefSize(100, 60);
            btn.setOnAction(new ButtonEventHandler<>(i.x, i.y));
            btn.setFont(SIZE18);

            JFXTextField textField = new JFXTextField();
            textfields[i.y][i.x] = textField;
            textField.setFont(SIZE18);
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
    void swapClassM() {
        swapClass.setDisable(true);
    }

    @FXML
    void copyrightM() throws URISyntaxException, IOException {
        if (Desktop.isDesktopSupported())
            Desktop.getDesktop().browse(new URI("https://github.com/RocketMaDev/DeskSorting"));
    }

    @FXML
    void impM() {
        lockMainWindow();
        iodS.setTitle("导入");
        Label il = (Label) iod.lookup("#iLal");
        il.setText("从...导入:");
        iodS.show();
    }

    /**
     * @see MainWindow#importTable(BufferedInputStream)
     */
    void impl_importTable(BufferedInputStream in) throws IOException, IllegalAccessException {
        if (saved != null) {
            Alert alert = new Alert(GlobalVariables.GIVE_UP_LAYOUT_WARNING);
            alert.setEventHandler(event -> {
                try {
                    importTable(in);
                } catch (IllegalAccessException | IOException e) {
                    e.printStackTrace();
                }
                unlockMainWindow();
                alert.close();
            }, event -> {
                unlockMainWindow();
                alert.close();
            });
            alert.show();
        } else {
            importTable(in);
            unlockMainWindow();
        }
    }

    @FXML
    void expM() {
        lockMainWindow();
        iodS.setTitle("导出");
        Label il = (Label) iod.lookup("#iLal");
        il.setText("导出至...:");
        iodS.show();
    }

    private void randomSort() {
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
        syncSaved(true);
        updateTable();
    }

    @FXML
    void randM() {
        if (saved == null)
            randomSort();
        else {
            lockMainWindow();
            Alert alert = new Alert(GlobalVariables.GIVE_UP_LAYOUT_WARNING);
            alert.setEventHandler(event -> {
                randomSort();
                unlockMainWindow();
                alert.close();
            }, event -> {
                unlockMainWindow();
                alert.close();
            });
            alert.show();
        }
    }

    @FXML
    void mslM() {
        if (saved == null)
            return;
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

    /**
     * Import the seat table through the given file.
     *
     * @param in pointing at the file, which ends with ".xlsx" and contains a seat table
     * @throws IllegalAccessException if there is wrong with stuInfo
     * @throws IOException            if the file can't be read.
     */
    private void importTable(BufferedInputStream in) throws IllegalAccessException, IOException {
        students = new Student[7][8];
        StudentList<Student> stuInfo = GlobalVariables.stuInfo;
        stuInfo.startSearching();
        DataFormatter formatter = new DataFormatter();
        XSSFWorkbook ctWorkbook = null;
        OPCPackage opcPackage;
        try {
            opcPackage = OPCPackage.open(in);
            ctWorkbook = new XSSFWorkbook(opcPackage);
        } catch (InvalidFormatException | IOException e) {
            e.printStackTrace();
            stuInfo.endSearching();
        }
        XSSFSheet table = Objects.requireNonNull(ctWorkbook).getSheetAt(0);
        int[] chairColumns = {0, 2, 3, 5, 6, 8, 9, 11};
        for (int row = 0; row < 6; row++) {
            Row r = table.getRow(row);
            if (r == null)
                continue;
            for (int col = 0; col < chairColumns.length; col++) {
                Cell c = r.getCell(chairColumns[col], Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                if (c != null)
                    students[row][col] = stuInfo.searchByName(formatter.formatCellValue(c));
            }
        }
        int[] frontColumns = {2, 3, 8, 9};
        for (int col = 0; col < frontColumns.length; col++) {
            Row r = table.getRow(7);
            if (r == null)
                continue;
            Cell c = r.getCell(frontColumns[col], Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (c != null)
                students[6][col < 2 ? col + 1 : col + 3] = stuInfo.searchByName(formatter.formatCellValue(c)); // 把0,1映射到1,2;把2,3映射到5,6
        }
        headInfo.setText(formatter.formatCellValue(table.getRow(8).getCell(0)));
        stuInfo.endSearching();
        ctWorkbook.close();
        syncSaved(true);
        updateTable();
    }

    /**
     * Export the present table through an given BufferedOutputStream
     *
     * @param out the BufferedOutputStream to be written
     * @throws IOException            if <code>out</code> can not be written
     * @throws InvalidFormatException NEVER HAPPENS unless you delete <code>templateOfTable.xlsx</code>
     */
    void exportTable(BufferedOutputStream out) throws IOException, InvalidFormatException {
        if (saved == null)
            return;
        InputStream in = MainWindow.class.getResourceAsStream(GlobalVariables.TABLE_TEMPLATE_P);
        assert in != null;
        OPCPackage pkg = OPCPackage.open(in);
        XSSFWorkbook wb = new XSSFWorkbook(pkg);
        int[] chairColumns = {0, 2, 3, 5, 6, 8, 9, 11};
        XSSFSheet table = wb.getSheetAt(0);
        for (int row = 0; row < 6; row++) {
            XSSFRow r = table.getRow(row);
            for (int col = 0; col < chairColumns.length; col++) {
                writeCell(students[row][col],
                        r.getCell(chairColumns[col], Row.MissingCellPolicy.RETURN_NULL_AND_BLANK));
            }
        }
        int[] frontColumns = {2, 3, 8, 9};
        for (int col = 0; col < frontColumns.length; col++) {
            XSSFRow r = table.getRow(7);
            int x = col < 2 ? col + 1 : col + 3;
            writeCell(students[6][x],
                    r.getCell(frontColumns[col], Row.MissingCellPolicy.RETURN_NULL_AND_BLANK));
        }
        table.getRow(8).getCell(0).setCellValue(headInfo.getText());
        Calendar c = Calendar.getInstance();
        String date = c.get(Calendar.YEAR) + Util.MONTH_NAME[c.get(Calendar.MONTH)] + c.get(Calendar.DAY_OF_MONTH);
        wb.setSheetName(0, date);
        wb.write(out);
        out.close();
        pkg.close();
        syncSaved(false);
        unlockMainWindow();
    }

    /**
     * Thanks help from @Axel Richter from Stack Overflow.<p>
     * This method is written by him or her.<p>
     * Method for getting current font from cell.
     *
     * @param cell the cell to search for its font
     * @return the font of the <code>cell</code>
     */
    private static XSSFFont getFont(XSSFCell cell) {
        XSSFWorkbook workbook = cell.getSheet().getWorkbook();
        XSSFCellStyle style = cell.getCellStyle();
        return workbook.getFontAt(style.getFontIndex());
    }

    /**
     * Thanks help from @Axel Richter from Stack Overflow.<p>
     * This method is written by him or her.<p>
     * A method to write a cell.
     *
     * @param stu  the student to be stored
     * @param cell the cell to be written
     */
    private void writeCell(Student stu, XSSFCell cell) {
        XSSFWorkbook workbook = cell.getSheet().getWorkbook();
        Map<String, Object> styleproperties;
        Map<Util.FontProperty, Object> fontproperties;

        //get or create the needed font 20pt
        fontproperties = new HashMap<>();
        fontproperties.put(Util.FontProperty.FONTHEIGHT, (short) (20 * 20));
        XSSFFont font20 = Util.getFont(workbook, getFont(cell), fontproperties);
        //get or create the needed font 17pt
        fontproperties = new HashMap<>();
        fontproperties.put(Util.FontProperty.FONTHEIGHT, (short) (17 * 20));
        XSSFFont font17 = Util.getFont(workbook, getFont(cell), fontproperties);
        //create style properties for cell
        styleproperties = new HashMap<>();

        if (stu == null) {
            styleproperties.put(CellUtil.FILL_FOREGROUND_COLOR, IndexedColors.WHITE);
            styleproperties.put(CellUtil.FONT, font20.getIndex());
        } else {
            cell.setCellValue(stu.getName());
            styleproperties.put(CellUtil.FONT, stu.isLongName() ? font17.getIndex() : font20.getIndex());
            styleproperties.put(CellUtil.FILL_FOREGROUND_COLOR, stu.isBoarding() ?
                    IndexedColors.PALE_BLUE.getIndex() :
                    IndexedColors.LIGHT_GREEN.getIndex());
        }
        styleproperties.put(CellUtil.FILL_PATTERN, FillPatternType.SOLID_FOREGROUND);

        //set style properties to cell
        CellUtil.setCellStyleProperties(cell, styleproperties);
    }

    /**
     * Synchronize <code>saved</code>.
     *
     * @param withInit determines how to deal with a null <code>saved</code>.
     */
    private void syncSaved(boolean withInit) {
        if (withInit && saved == null)
            saved = new Student[7][8];
        try {
            for (AutoIterator i = new AutoIterator(AutoIterator.SQUARE_ARRAY); i.hasNextWithUpdate(); i.next())
                saved[i.y][i.x] = students[i.y][i.x];
        } catch (IllegalAccessException ignored) {
        }
    }

    private void lockMainWindow() {
        GlobalVariables.mainS.setOnCloseRequest(Event::consume);
        anchorPane.setDisable(true);
    }

    void unlockMainWindow() {
        GlobalVariables.mainS.setOnCloseRequest(event -> {
        });
        anchorPane.setDisable(false);
    }

    /**
     * Update a single cell of the table.
     *
     * @param x x position
     * @param y y position
     */
    private void updateTable(int x, int y) {
        JFXButton btn = btns[y][x];
        Student stu = students[y][x];
        if (stu == null) {
            btn.setTextFill(Paint.valueOf("black"));
            btn.setText("");
        } else {
            btn.setText(stu.getName());
            if (stu.isLongName())
                btn.setFont(SIZE15);
            else
                btn.setFont(SIZE18);
            btn.setTextFill(stu.isBoarding() ? Paint.valueOf("BLUE") : Paint.valueOf("GREEN"));
        }
        if (btn.isDisable())
            btn.setDisable(false);
        if (!btn.isVisible())
            btn.setVisible(true);
        if (textfields[y][x].isVisible())
            textfields[y][x].setVisible(false);
    }

    /**
     * Update the whole table.
     */
    private void updateTable() {
        try {
            for (AutoIterator i = new AutoIterator(AutoIterator.SQUARE_ARRAY); i.hasNextWithUpdate(); i.next())
                updateTable(i.x, i.y);
        } catch (IllegalAccessException ignored) {
        }
    }

    @FXML
    void quickSwapM(ActionEvent actionEvent) {
        index = -1;
        updateTable();
    }
}

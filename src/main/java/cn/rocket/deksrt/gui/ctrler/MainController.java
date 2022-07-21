/*
 * Copyright (c) 2022 Rocket, Project DeskSorting
 */

package cn.rocket.deksrt.gui.ctrler;

import cn.rocket.deksrt.core.LocalURL;
import cn.rocket.deksrt.core.Util;
import cn.rocket.deksrt.core.Vars;
import cn.rocket.deksrt.core.iterator.GridIterator;
import cn.rocket.deksrt.core.iterator.Pair;
import cn.rocket.deksrt.core.student.Student;
import cn.rocket.deksrt.core.student.StudentList;
import cn.rocket.deksrt.gui.alert.FileAlert;
import cn.rocket.deksrt.gui.alert.SimpleAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.poi.EmptyFileException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
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
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static cn.rocket.deksrt.core.Util.*;

/**
 * 主窗口控制器
 *
 * @author Rocket
 * @version 1.0.8
 * @since 0.9-pre
 */
public class MainController implements Controller {
    private MainController(boolean empty) {
        if (empty)
            iod = null;
    }

    public MainController() {
        this(false);
    }

    public static final MainController TYPE = new MainController(true);
    private Stage iodS;
    private Parent iod;

    private JFXButton[][] btns;
    private JFXTextField[][] textfields;
    private Student[][] students;
    private Student[][] saved;
    private int index;
    private final FileAlert fileAlert = new FileAlert(this, true);
    private static final Font SIZE18 = new Font(18);
    private static final Font SIZE15 = new Font(15);

    public AnchorPane anchorPane;
    public JFXButton copyright;
    public JFXButton swapClass;
    public JFXCheckBox quickSwapCB;
    public JFXButton importB;
    public JFXButton exportB;
    public JFXButton randSort;
    public JFXButton MSLSort;
    public GridPane grid0;
    public JFXTextField headInfo;
    public GridPane grid1;

    @Override
    public void lockWindow() {
        anchorPane.setDisable(true);
        Vars.getStage(MainController.class).setOnCloseRequest(Event::consume);
    }

    @Override
    public void unlockWindow() {
        anchorPane.setDisable(false);
        Vars.getStage(MainController.class).setOnCloseRequest(event -> {
        });
    }

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
                    index = store(x, y);
                    btns[y][x].setDisable(true);
                } else {
                    int x0 = readX(index);
                    int y0 = readY(index);
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

    public void initialize() {
        Vars.putObj(this);
        iod = null;
        try {
            iod = FXMLLoader.load(Objects.requireNonNull(
                    MainController.class.getResource("/cn/rocket/deksrt/resource/IEportDialog.fxml")
            ));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //TODO FileAlert处理
        iodS = new Stage();
        Vars.putStage(FileAlert.class, iodS);
        iodS.setResizable(false);
        iodS.setScene(new Scene(Objects.requireNonNull(iod)));
        iodS.setAlwaysOnTop(true);
        iodS.setOnCloseRequest(event -> unlockMainWindow());

        students = new Student[7][8];
        btns = new JFXButton[7][8];
        textfields = new JFXTextField[7][8];

        for (Pair p : GridIterator.FULL_GRID) {
            JFXButton btn = new JFXButton();
            btns[p.y()][p.x()] = btn;
            btn.setPrefSize(100, 60);
            btn.setOnAction(new ButtonEventHandler<>(p.x(), p.y()));
            btn.setFont(SIZE18);

            JFXTextField textField = new JFXTextField();
            textfields[p.y()][p.x()] = textField;
            textField.setFont(SIZE18);
            textField.setVisible(false);
            textField.setDisable(true);
        }
        for (Pair p : GridIterator.GRID0) {
            grid0.add(btns[p.y()][p.x()], p.x(), p.y());
            grid0.add(textfields[p.y()][p.x()], p.x(), p.y());
        }
        for (Pair p : GridIterator.GRID1) {
            int col = p.x() < 3 ? p.x() + 1 : p.x() + 2;
            grid1.add(btns[6][col], p.x(), p.y());
            grid1.add(textfields[6][col], p.x(), p.y());
        }

    }

    public void swapClassM() {
        swapClass.setDisable(true);
    }

    public void copyrightM() throws URISyntaxException, IOException {
        if (Desktop.isDesktopSupported())
            Desktop.getDesktop().browse(new URI("https://github.com/RocketMaDev/DeskSorting"));
    }

    public void impM() {
        lockMainWindow();
        iodS.setTitle("导入");
        Label il = (Label) iod.lookup("#iLal");
        il.setText("从...导入:");
        iodS.show();
    }

    /**
     * @see MainController#importTable(BufferedInputStream)
     */
    public void impl_importTable(BufferedInputStream in) throws IOException {
        if (saved != null) {
            SimpleAlert alert = new SimpleAlert(Vars.GIVE_UP_LAYOUT_WARNING, false, this);
            alert.setEventHandler(event -> {
                try {
                    importTable(in);
                } catch (IOException e) {
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

    public void expM() {
        lockMainWindow();
        iodS.setTitle("导出");
        Label il = (Label) iod.lookup("#iLal");
        il.setText("导出至...:");
        iodS.show();
    }

    private void randomSort() {
        LinkedList<Student> stus = new LinkedList<>(Vars.stuInfo);
        LinkedList<Integer> seats = Arrays.stream(GridIterator.FULL_GRID.toArray(true)).boxed()
                .collect(Collectors.toCollection(LinkedList::new));
        students = new Student[7][8];
        int length = stus.size();
        for (int i = 0; i < length; i++) {
            int pstu = (int) (Math.random() * (length - i));
            int pseat = (int) (Math.random() * seats.size());
            int xy = seats.get(pseat);
            students[readX(xy)][readY(xy)] = stus.get(pstu);
            stus.remove(pstu);
            seats.remove(pseat);
        }
        syncSaved(true);
        updateTable();
    }

    public void randM() {
        if (saved == null)
            randomSort();
        else {
            lockMainWindow();
            SimpleAlert alert = new SimpleAlert(Vars.GIVE_UP_LAYOUT_WARNING, true, this);
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

    public void fastSortM() {
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
     * @throws IOException if the file can't be read.
     */
    private void importTable(BufferedInputStream in) throws IOException {
        students = new Student[7][8];
        StudentList<Student> stuInfo = Vars.stuInfo;
        stuInfo.startSearching();
        DataFormatter formatter = new DataFormatter();
        XSSFWorkbook ctWorkbook = null;
        OPCPackage opcPackage;
        try {
            opcPackage = OPCPackage.open(in);
            ctWorkbook = new XSSFWorkbook(opcPackage);
        } catch (OLE2NotOfficeXmlFileException | EmptyFileException | InvalidFormatException e) {
            stuInfo.endSearching();
            throw new IOException(e);
        } catch (IOException e) {
            stuInfo.endSearching();
            throw e;
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
     * Export the present table through the given BufferedOutputStream
     *
     * @param out the BufferedOutputStream to be written
     * @throws IOException if <code>out</code> can not be written
     */
    public void exportTable(BufferedOutputStream out) throws IOException {
        if (saved == null)
            return;
        InputStream in = MainController.class.getResourceAsStream(LocalURL.TABLE_TEMPLATE_P);
        assert in != null;
        OPCPackage pkg;
        try {
            pkg = OPCPackage.open(in);
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        }
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
        String date = LocalDate.now().toString();
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
        fontproperties.put(Util.FontProperty.FONT_HEIGHT, (short) (20 * 20));
        XSSFFont font20 = Util.getFont(workbook, getFont(cell), fontproperties);
        //get or create the needed font 17pt
        fontproperties = new HashMap<>();
        fontproperties.put(Util.FontProperty.FONT_HEIGHT, (short) (17 * 20));
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
                    IndexedColors.PALE_BLUE.getIndex() ://TODO color to switch
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
        for (Pair p : GridIterator.FULL_GRID)
            saved[p.y()][p.x()] = students[p.y()][p.x()];
    }

    private void lockMainWindow() {
        Vars.getStage(MainController.class).setOnCloseRequest(Event::consume);
        anchorPane.setDisable(true);
    }

    public void unlockMainWindow() {
        Vars.getStage(MainController.class).setOnCloseRequest(event -> {
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
        for (Pair p : GridIterator.FULL_GRID)
            updateTable(p.x(), p.y());
    }

    public void quickSwapM() {
        index = -1;
        updateTable();
    }
}

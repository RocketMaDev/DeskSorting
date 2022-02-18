/*
 * Copyright (c) 2022 Rocket, Project DeskSorting
 */

package cn.rocket.deksrt.gui;

import cn.rocket.deksrt.core.GlobalVariables;
import cn.rocket.deksrt.util.Student;
import cn.rocket.deksrt.util.StudentList;
import cn.rocket.deksrt.util.Util;
import com.jfoenix.controls.JFXButton;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;

/**
 * @author Rocket
 * @version 1.0
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        GlobalVariables.mainS = primaryStage;
//        File propertiesFile = new File(GlobalVariables.env+"config.properties");
        if (!importStuInfo()) {
            String errors = scanStuInfo();
            if (errors == null) {
                showIntroduction(primaryStage);
                return;
            } else
                System.out.println("errors:" + errors);
        }
        exportStuInfo();

        Parent root = FXMLLoader.load(Objects.requireNonNull(
                getClass().getResource(GlobalVariables.MAIN_WINDOW_FXML)
        ));
        primaryStage.setTitle("排座位");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private void showIntroduction(Stage primaryStage) {
        JFXButton btn = new JFXButton("关闭");
        btn.setButtonType(JFXButton.ButtonType.RAISED);
        btn.setTextFill(Paint.valueOf("white"));
        btn.setFont(Font.font(25));
        btn.setOnAction(event -> primaryStage.close());
        btn.setBackground(new Background(new BackgroundFill(Paint.valueOf("dodgerblue"), null, null)));
        AnchorPane pane = new AnchorPane();
        AnchorPane.setTopAnchor(btn, 100.0);
        AnchorPane.setRightAnchor(btn, 100.0);
        pane.setPrefSize(970, 700);
        pane.getChildren().addAll(new ImageView(new Image(GlobalVariables.INTRODUCTION)), btn);
        primaryStage.setScene(new Scene(pane));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }

    /**
     * <b>EXIT CODE 99, 100 HERE</b>
     *
     * @return errors in xlsx file
     * @throws IOException if input a incorrect file
     */
    private String scanStuInfo() throws IOException, InvalidFormatException {
        StringBuilder errors = new StringBuilder();
        InputStream in = this.getClass().getResourceAsStream(GlobalVariables.STU_INFO_TEMPLATE_P);
        File infoXlsx = new File(GlobalVariables.jarParentPath + "StuInfo.xlsx");
        if (!infoXlsx.exists()) {
            assert in != null;
            Files.copy(in, infoXlsx.toPath());
            return null;
        } else {
            OPCPackage opcPackage = OPCPackage.open(infoXlsx);
            XSSFWorkbook wb = new XSSFWorkbook(opcPackage);
            GlobalVariables.stuInfo = new StudentList<>(52);
            StudentList<Student> stuIn = GlobalVariables.stuInfo;

            Sheet sheet = wb.getSheetAt(0);
            DataFormatter df = new DataFormatter();

            String name;
            String pinyin;
            boolean boarding;
            for (int row = 1; row <= 52; row++) {
                Row r = sheet.getRow(row);
                if (r == null)
                    continue;
                Cell c0 = r.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                Cell c1 = r.getCell(1, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                Cell c2 = r.getCell(2, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                if (c0 != null) {
                    if (c1 == null || !Util.validatePinyin((pinyin = df.formatCellValue(c1)))) {
                        errors.append("B").append(row + 1).append(",");
                        continue;
                    }
                    if (c2 != null) {
                        String b = df.formatCellValue(c2);
                        if (!(b.equals("1") || b.equals("0"))) {
                            errors.append(c2.getAddress()).append(",");
                            continue;
                        } else
                            boarding = b.equals("1");
                    } else
                        boarding = false;
                    name = df.formatCellValue(c0);
                } else if (c1 == null && c2 == null)
                    continue;
                else {
                    errors.append("A").append(row + 1).append(",");
                    continue;
                }
                if (!stuIn.add(new Student(name, pinyin.toLowerCase(), boarding)))
                    errors.append(c0.getAddress()).append(",");
            }
            if (stuIn.size() < 2) {
                System.err.println("Too less names in input file!");
                System.exit(100);
            }
            System.out.println("name\tpy\tboarding");
            for (Student student : stuIn)
                System.out.println(student);
            wb.close();
        }
        return errors.length() != 0 ? errors.deleteCharAt(errors.length() - 1).toString() : "";
    }

    private void exportStuInfo() {
        File parent = new File(GlobalVariables.ENV);
        if (!parent.exists())
            //noinspection ResultOfMethodCallIgnored
            parent.mkdirs();
        File infoFile = new File(GlobalVariables.STUDENT_INFO);
        if (!infoFile.exists())
            try {
                //noinspection ResultOfMethodCallIgnored
                infoFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        StringBuilder sb = new StringBuilder();

        sb.append("default:");
        for (Student student : GlobalVariables.stuInfo) {
            sb.append(student.getName()).append("$").append(student.getPinyin()).append("$")
                    .append(Boolean.valueOf(student.isBoarding())).append(",");
        }
        sb.deleteCharAt(sb.length() - 1).append(';');
        sb.deleteCharAt(sb.length() - 1);
        try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(infoFile), StandardCharsets.UTF_8)) {
            osw.write(sb.toString());
            osw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return <code>false</code> if no info in "student.info" or the file doesn't exist.
     */
    private boolean importStuInfo() {
        File infoFile = new File(GlobalVariables.STUDENT_INFO);
        String info = null;
        if (!infoFile.exists())
            return false;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(infoFile), StandardCharsets.UTF_8))) {
            info = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (info == null)
            return false;
//        HashMap<String, StudentList<Student>> classMap = new HashMap<>();
        String[] classes = new String[]{info}/*.split(";")*/;
        for (String cla : classes) {
            String[] detailedInfo = cla.split(":");
            String[] studentArray = detailedInfo[1].split(",");
            StudentList<Student> studentInfo = new StudentList<>(studentArray.length);
            for (String student : studentArray) {
                String[] basicInfo = student.split("\\$");
                studentInfo.add(new Student(basicInfo[0], basicInfo[1], Boolean.parseBoolean(basicInfo[2])));
            }
            GlobalVariables.stuInfo = studentInfo;
//            classMap.put(detailedInfo[0],studentInfo);
        }
        return true;
    }

    /**
     * The main entrance of the program.
     *
     * @param args [0]:--reset?delete student.info
     */
    public static void main(String[] args) {
        GlobalVariables.jarPath = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        GlobalVariables.jarParentPath = new File(GlobalVariables.jarPath).getParent() + "/";

        // 重置 参数
        if (args.length != 0 && args[0].equals("--reset")) {
            File stuInfoFile = new File(GlobalVariables.STUDENT_INFO);
            if (stuInfoFile.exists())
                //noinspection ResultOfMethodCallIgnored
                stuInfoFile.delete();
        }

        launch(args);
    }
}

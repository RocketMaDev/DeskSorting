package cn.rocket.deksrt.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;

/**
 * @author Rocket
 * @version 1.0
 */
public class Entry extends Application {
    private Student[] stuInfo;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(
                getClass().getResource("/cn/rocket/deksrt/resource/MainWindow.fxml")
        ));
        primaryStage.setTitle("排座位");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 720, 500));
        primaryStage.show();

    }

    private void detectUserFile() throws IOException {
        String _userProfile = System.getenv("USERPROFILE") + "/DeskSorting/env.properties";
        File userProfile = new File(_userProfile);
        if (!userProfile.exists()) {
            String jarPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
            String globalPath = new File(jarPath).getParentFile().getPath();  //jar file parent path
            InputStream in = this.getClass().getResourceAsStream("/cn/rocket/deksrt/resource/templateOfStuInfo.xlsx");
            File out = new File(globalPath + "StuInfo.xlsx");
            if (!out.exists()) {
                assert in != null;
                Files.copy(in, out.toPath());
            } else {
                XSSFWorkbook wb = (XSSFWorkbook) WorkbookFactory.create(out);
                ArrayList<String> names = new ArrayList<>();
                ArrayList<String> pinyinList = new ArrayList<>();
                ArrayList<Boolean> boarding = new ArrayList<>();
                Sheet sheet = wb.getSheetAt(0);
                DataFormatter df = new DataFormatter();
                for (int row = 1; row <= 50; row++) {
                    Row r = sheet.getRow(row);
                    if (r == null)
                        continue;

                    Cell c0 = r.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell c1 = r.getCell(1, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell c2 = r.getCell(2, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if (c0 != null) {
                        names.add(c0.getStringCellValue());
                        pinyinList.add(c1 != null ? c1.getStringCellValue() : "");
                        if (c2 == null || !df.formatCellValue(c2).equals("1"))
                            boarding.add(false);
                        else
                            boarding.add(true);
                    }
                }
                stuInfo = new Student[names.size()];
                for (int i = 0; i < stuInfo.length; i++)
                    stuInfo[i] = new Student(names.get(i), pinyinList.get(i), boarding.get(i));
                System.out.println("name\tpy\tboarding");
                for (int i = 0; i < names.size(); i++) {
                    System.out.print(names.get(i) + (names.get(i).length() == 2 ? "\t\t" : "\t"));
                    System.out.print(pinyinList.get(i) + "\t");
                    System.out.println(boarding.get(i).toString());
                }
            }

            return;
        }
        FileInputStream fis = new FileInputStream(userProfile);
        Properties p = new Properties();
        p.load(fis);
        String info = p.getProperty("stuinfo");
    }

    public static void main(String[] args) {
//        try {
//            new Entry().detectUserFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        launch(args);
    }
}

package cn.rocket.deksrt.main;

import cn.rocket.deksrt.util.Student;
import cn.rocket.deksrt.util.StudentList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;

/**
 * @author Rocket
 * @version 1.0
 */
public class Entry extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
//        File propertiesFile = new File(GlobalVariables.env+"config.properties");
        if (!importStuInfo())
            System.out.println("errors:" + scanStuInfo());
        exportStuInfo();

        Parent root = FXMLLoader.load(Objects.requireNonNull(
                getClass().getResource("/cn/rocket/deksrt/resource/MainWindow.fxml")
        ));
        primaryStage.setTitle("排座位");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 720, 500));
        primaryStage.show();

    }

    /**
     * @return errors in xlsx file
     * @throws IOException if input a incorrect file
     */
    private String scanStuInfo() throws IOException {
        StringBuilder errors = new StringBuilder();
        InputStream in = this.getClass().getResourceAsStream(GlobalVariables.stuInfoTemplateP);
        File infoXlsx = new File(GlobalVariables.jarParentPath + "StuInfo.xlsx");
        if (!infoXlsx.exists()) {
            assert in != null;
            Files.copy(in, infoXlsx.toPath());
            System.exit(99);
        } else {
            Workbook wb = WorkbookFactory.create(infoXlsx);
            GlobalVariables.stuInfo = new StudentList<>(52);
            StudentList<Student> stuIn = GlobalVariables.stuInfo;

            Sheet sheet = wb.getSheetAt(0);
            DataFormatter df = new DataFormatter();

            Student queue;
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
                    if (c1 == null || !GlobalVariables.validatePinyin((pinyin = df.formatCellValue(c1)))) {
                        errors.append("B").append(row + 1).append(",");
                        continue;
                    }
                    if (c2 != null) {
                        String b = df.formatCellValue(c2);
                        if (!(b.equals("1") || b.equals("0"))) {
                            errors.append("C").append(row + 1).append(",");
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
                queue = new Student(name, pinyin.toLowerCase(), boarding);
                if (!stuIn.contains(queue))
                    stuIn.add(new Student(name, pinyin.toLowerCase(), boarding));
            }
            System.out.println("name\tpy\tboarding");
            for (Student student : stuIn)
                System.out.println(student);
            wb.close();
        }

        return errors.length() != 0 ? errors.deleteCharAt(errors.length() - 1).toString() : "";
    }

    private void exportStuInfo() {
        File parent = new File(GlobalVariables.env);
        if (!parent.exists())
            //noinspection ResultOfMethodCallIgnored
            parent.mkdirs();
        File infoFile = new File(GlobalVariables.env + "student.info");
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
        sb.replace(sb.length() - 1, sb.length() - 1, ";");
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
        File infoFile = new File(GlobalVariables.env + "student.info");
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

    public static void main(String[] args) {
        GlobalVariables.jarPath = Entry.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        GlobalVariables.jarParentPath = new File(GlobalVariables.jarPath).getParent() + "/";

        launch(args);
    }
}

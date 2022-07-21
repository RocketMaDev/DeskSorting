/*
 * Copyright (c) 2022 Rocket, Project DeskSorting
 */

package cn.rocket.deksrt.gui;

import cn.rocket.deksrt.core.LocalURL;
import cn.rocket.deksrt.core.Util;
import cn.rocket.deksrt.core.Vars;
import cn.rocket.deksrt.core.student.Student;
import cn.rocket.deksrt.core.student.StudentList;
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
import java.util.StringJoiner;

/**
 * 访问配置文件、表格文件的类
 *
 * @author Rocket
 * @version 1.0.8
 * @since 1.0.8
 */
public class FileAccess {

    /**
     * <b>EXIT CODE 99, 100 HERE</b>
     *
     * @return errors in xlsx file
     * @throws IOException if input an incorrect file
     */
    public static String scanStuInfo() throws IOException, InvalidFormatException {
        StringBuilder errors = new StringBuilder();
        InputStream in = FileAccess.class.getResourceAsStream(LocalURL.STU_INFO_TEMPLATE_P);
        File infoXlsx = new File(LocalURL.JAR_PARENT_PATH + "StuInfo.xlsx");
        if (!infoXlsx.exists()) {
            assert in != null;
            Files.copy(in, infoXlsx.toPath());
            return null;
        } else {
            OPCPackage opcPackage = OPCPackage.open(infoXlsx);
            XSSFWorkbook wb = new XSSFWorkbook(opcPackage);
            Vars.stuInfo = new StudentList<>(52);
            StudentList<Student> stuIn = Vars.stuInfo;

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

    public static void exportStuInfo() {
        File parent = new File(LocalURL.WORK_PATH);
        if (!parent.exists())
            //noinspection ResultOfMethodCallIgnored
            parent.mkdirs();
        File infoFile = new File(LocalURL.STU_INFO);
        if (!infoFile.exists())
            try {
                //noinspection ResultOfMethodCallIgnored
                infoFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        StringJoiner line = new StringJoiner("\n");
        for (Student student : Vars.stuInfo) {
            StringJoiner unit = new StringJoiner("$");
            line.merge(unit.add(student.getName())
                    .add(student.getPinyin())
                    .add(student.isBoarding() ? "t" : "f"));
        }
        try (OutputStreamWriter osw = new OutputStreamWriter(Files.newOutputStream(infoFile.toPath()), StandardCharsets.UTF_8)) {//TODO infoFile
            osw.write(line.toString());
            osw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO StringJoiner(还有输入/出名单)

    /**
     * @return <code>false</code> if no info in "student.info" or the file doesn't exist.
     */
    public static boolean importStuInfo() {
        File infoFile = new File(LocalURL.STU_INFO);
        String info = null;
        if (!infoFile.exists())
            return false;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        Files.newInputStream(infoFile.toPath()), StandardCharsets.UTF_8))) {
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
            Vars.stuInfo = studentInfo;
//            classMap.put(detailedInfo[0],studentInfo);
        }
        return true;
    }
}

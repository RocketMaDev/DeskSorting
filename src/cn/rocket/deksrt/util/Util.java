package cn.rocket.deksrt.util;

import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Map;

/**
 * @author Rocket
 * @version 1.0
 */
public class Util {
    public enum FontProperty {
        BOLD, COLOR, FONTHEIGHT, FONTNAME, ITALIC, STRIKEOUT, TYPEOFFSET, UNDERLINE
    }

    public static String[] MONTH_NAME = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    /**
     * Thanks help from @Axel Richter from Stack Overflow.<p>
     * This method is written by him or her.<p>
     * Method for getting font having special settings additional to given source font
     *
     * @param workbook       the workbook to search font
     * @param fontSrc        source font
     * @param fontproperties font to search
     * @return existing match font in workbook, or create a new one
     */
    public static XSSFFont getFont(XSSFWorkbook workbook, XSSFFont fontSrc, Map<FontProperty, Object> fontproperties) {
        boolean isBold = fontSrc.getBold();
        short color = fontSrc.getColor();
        short fontHeight = fontSrc.getFontHeight();
        String fontName = fontSrc.getFontName();
        boolean isItalic = fontSrc.getItalic();
        boolean isStrikeout = fontSrc.getStrikeout();
        short typeOffset = fontSrc.getTypeOffset();
        byte underline = fontSrc.getUnderline();

        for (FontProperty property : fontproperties.keySet()) {
            switch (property) {
                case BOLD:
                    isBold = (boolean) fontproperties.get(property);
                    break;
                case COLOR:
                    color = (short) fontproperties.get(property);
                    break;
                case FONTHEIGHT:
                    fontHeight = (short) fontproperties.get(property);
                    break;
                case FONTNAME:
                    fontName = (String) fontproperties.get(property);
                    break;
                case ITALIC:
                    isItalic = (boolean) fontproperties.get(property);
                    break;
                case STRIKEOUT:
                    isStrikeout = (boolean) fontproperties.get(property);
                    break;
                case TYPEOFFSET:
                    typeOffset = (short) fontproperties.get(property);
                    break;
                case UNDERLINE:
                    underline = (byte) fontproperties.get(property);
                    break;
            }
        }

        XSSFFont font = workbook.findFont(isBold, color, fontHeight, fontName, isItalic, isStrikeout, typeOffset, underline);
        if (font == null) {
            font = workbook.createFont();
            font.setBold(isBold);
            font.setColor(color);
            font.setFontHeight(fontHeight);
            font.setFontName(fontName);
            font.setItalic(isItalic);
            font.setStrikeout(isStrikeout);
            font.setTypeOffset(typeOffset);
            font.setUnderline(underline);
        }

        return font;
    }

    /**
     * To validate a string if it consists of only pinyin.
     *
     * @param pinyin the string to be validate
     * @return <code>true</code> if the string consists of only pinyin
     */
    public static boolean validatePinyin(String pinyin) {
        for (int i = 0; i < pinyin.length(); i++) {
            char c = pinyin.charAt(i);
            if (c < '0' || c > 'z' || (c > '9' && c < 'a')) // !'0'~'9'+'a'~'z'
                return false;
        }
        return true;
    }
}

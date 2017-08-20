package org.font2png;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.*;

public class FontToPNG {

    private final static int MAX_THREADS = Runtime.getRuntime().availableProcessors();

    private final static String[][] specialChars = {
            {"\\", "backslash"},
            {"\"", "double_quote"},
            {" ", "space"},
            {";", "semicolon"},
            {"^", "caret"},
            {"!", "exclamation"},
            {"#", "pound"},
            {"$", "dollar"},
            {"%", "percecnt"},
            {"&", "ampersand"},
            {"'", "single_quote"},
            {"(", "open_parenthesis"},
            {")", "close_parenthesis"},
            {"*", "asterik"},
            {"+", "plus"},
            {",", "comma"},
            {"-", "hyphen"},
            {".", "period"},
            {"/", "forwardslash"},
            {":", "colon"},
            {"<", "open_angle"},
            {">", "close_angle"},
            {"?", "question_mark"},
            {"@", "ampersat"},
            {"[", "open_brace"},
            {"]", "close_brace"},
            {"_", "underscore"},
            {"`", "grave"},
            {"{", "open_bracket"},
            {"}", "close_bracket"},
            {"|", "pipe"},
            {"~", "tilde"}
    };

    private static File charsetDir, fontFile;
    private static String fontName, fontSize;
    private static Font font;

    public static void main(String[] args) throws IOException, InterruptedException {
        fontFile = new File(JOptionPane.showInputDialog("Font file name (ex. prstartk.ttf)"));
        if(!fontFile.exists()) {
            JOptionPane.showMessageDialog(null, "The font file does not exist", "Font Not Found", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        try {
            font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
        } catch(FontFormatException ff) {
            ff.printStackTrace();
            System.exit(2);
        }

        fontName = JOptionPane.showInputDialog("The name of the font (ex. press_start_medium)");
        fontSize = JOptionPane.showInputDialog("The size of the font (ex. 30)");
        GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        genv.registerFont(font);
        font = font.deriveFont((float) Integer.parseInt(fontSize));
        charsetDir = new File(System.getProperty("user.dir") + File.separator + "charsets" + File.separator + fontName);

        if(!charsetDir.exists()) {
            charsetDir.mkdirs();
        }

        ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);
        ArrayList<Future<?>> taskList = new ArrayList<Future<?>>();


        for(char c  = 'a'; c <= 'z'; c++) {
            FontSerializer fs = new FontSerializer(c, "lower_" + c, font, charsetDir.getAbsolutePath());
            Future<?> f = executor.submit(fs);
            taskList.add(f);
        }

        for(char c = 'A'; c <= 'Z'; c++) {
            FontSerializer fs = new FontSerializer(c, ("upper_" + c).toLowerCase(), font, charsetDir.getAbsolutePath());
            Future<?> f = executor.submit(fs);
            taskList.add(f);
        }

        for(char c = '0'; c <= '9'; c++) {
            FontSerializer fs = new FontSerializer(c, "" + c, font, charsetDir.getAbsolutePath());
            Future<?> f = executor.submit(fs);
            taskList.add(f);
        }

        for(String[] set : specialChars) {
            FontSerializer fs = new FontSerializer(set[0].charAt(0), set[1], font, charsetDir.getAbsolutePath());
            Future<?> f = executor.submit(fs);
            taskList.add(f);
        }

        for(Future<?> future : taskList) {
            try {
                future.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        System.exit(0);
    }
}

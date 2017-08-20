import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class GenerateCharset {

    private final static String[][] specialChars = {
            //{"\\", "backslash"},
            //{"\"", "double_quote"},
            {";", "semicolon"},
            //{"^", "caret"},
            {"!", "exclamation"},
            {"#", "pound"},
            {"$", "dollar"},
            //{"%", "percecnt"},
            //{"&", "ampersand"},
            //{"'", "single_quote"},
            {"(", "open_parenthesis"},
            {")", "close_parenthesis"},
            {"*", "asterik"},
            {"+", "plus"},
            //{",", "comma"},
            {"-", "hyphen"},
            {".", "period"},
            //{"/", "forwardslash"},
            {":", "colon"},
            //{"<", "open_angle"},
            //{">", "close_angle"},
            //{"?", "question_mark"},
            //{"@", "ampersat"},
            //{"[", "open_brace"},
            //{"]", "close_brace"},
            {"_", "underscore"}
            //{"`", "grave"},
            //{"{", "open_bracket"},
            //{"}", "close_bracket"},
            //{"|", "pipe"},
            //{"~", "tilde"}
    };

    private static File charsetDir, fontFile;
    private static String fontName, fontSize;

    public static void main(String[] args) throws IOException, InterruptedException {
        fontFile = new File(JOptionPane.showInputDialog("File file name? (Ex. prstartk.ttf)"));
        if(!fontFile.exists()) {
            JOptionPane.showMessageDialog(null, "The font file does not exist", "Font Not Found", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        fontName = JOptionPane.showInputDialog("The name of the font (Ex. press_start_medium)");
        fontSize = JOptionPane.showInputDialog("The size of the font (Ex. 25x25)");
        charsetDir = new File(System.getProperty("user.dir") + File.separator + "charsets"
                + File.separator + fontName);

        if(!charsetDir.exists())
            charsetDir.mkdirs();

        for(char letter = 'a'; letter <= 'z'; letter++) {
            String fileName = (fontName + "_lower_" + letter).toLowerCase();
            ArrayList<String> cmd = commandArray("" + letter, fileName);
            if(characterToPNG(cmd) != 0) {
                System.out.println("Error while creating character " + letter);
            }
        }
        for(char letter = 'A'; letter <= 'Z'; letter++) {
            String fileName = (fontName + "_upper_" + letter).toLowerCase();
            ArrayList<String> cmd = commandArray("" + letter, fileName);
            if(characterToPNG(cmd) != 0) {
                System.out.println("Error while creating character " + letter);
            }
        }
        for(String[] set : specialChars) {
            String fileName = fontName + "_" + set[1];
            ArrayList<String> cmd = commandArray(set[0], fileName);
            if(characterToPNG(cmd) != 0) {
                System.out.println("Error while creating character " + set[0] + " of name " + set[1]);
            }
        }
        
        for(int i = 0; i < 10; i++) {
        	String fileName = fontName + "_" + i;
        	ArrayList<String> cmd = commandArray("" + i, fileName);
        	if(characterToPNG(cmd) != 0) {
        		System.out.println("Error while creating character for digit " + i);
        	}
        }
        
        String sName = fontName + "_space";
        ArrayList<String> scmd = new ArrayList<>();
        scmd.add("convert.exe");
        scmd.add("-size");
        scmd.add(fontSize);
        scmd.add("xc:transparent");
        scmd.add(charsetDir.getAbsolutePath() + File.separator + sName + ".png");
        if(characterToPNG(scmd) != 0) {
        	System.out.println("Error while creating space character");	
        }

        System.out.println("Finished generating charset " + fontName);
    }

    public static int characterToPNG(ArrayList<String> commands) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(commands);
        pb.redirectErrorStream(true);
        Process p = pb.start();
        InputStreamReader isr = new InputStreamReader(p.getInputStream());
        BufferedReader br = new BufferedReader(isr);
        String line;
        while((line = br.readLine()) != null) {
            System.out.println(line);
        }

        return p.waitFor();
    }

    public static ArrayList<String> commandArray(String label, String fileName) {
        ArrayList<String> cmd = new ArrayList<>();
        cmd.add("convert.exe");
        cmd.add("-gravity");
        cmd.add("center");
        cmd.add("-background");
        cmd.add("none");
        cmd.add("-fill");
        cmd.add("black");
        cmd.add("-font");
        cmd.add(fontFile.getAbsolutePath());
        cmd.add("-size");
        cmd.add(fontSize);
        cmd.add("label:" + label);
        cmd.add(charsetDir.getAbsolutePath() + File.separator + fileName + ".png");

        return cmd;
    }
}

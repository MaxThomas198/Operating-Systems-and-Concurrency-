import java.util.Scanner;

public class Assign3 {

    public static void main(String[] args) throws Exception {
        Parser p = new Parser();
        String line;
        Scanner in = new Scanner(System.in);
        String display = ("[" + System.getProperty("user.dir") + "]:");
        System.out.print(display);
        while ((line = in.nextLine()) != null) {
            boolean ok = p.parse(line);
            if (!ok) {
                System.out.println(line + " is not recognized as an internal or external command,operable program or batch file.");
            }
            System.out.print(display);
        }
    }
}
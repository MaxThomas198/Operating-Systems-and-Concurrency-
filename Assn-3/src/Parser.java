import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Parser {
    public static void main(String[] args) throws Exception {
        Parser p = new Parser();
        String line;
        Scanner in = new Scanner(System.in);
        while ((line = in.nextLine()) != null) {
            if (!p.parse(line) && !line.isEmpty()) {
                System.out.println(line + " is not recognized as an internal or external command,operable program or batch file.");
            }
        }
    }

    private Shell shell = new Shell();

    private ArrayList<String> args;

    private String cmd;

    private final String[] commands = {"ptime", "history", "^", "ls", "cd", "mdir", "rdir", "|", "exit"};

    public void print(String output) {
        System.out.println(output);
    }

    public boolean parse(String input) throws Exception {
        input = input.replace("\\ ", "~");

        String[] List = input.split(" ");

        for (int i = 0; i < List.length; i++) {
            List[i] = List[i].replace("~", " ");
        }

        boolean cond = Arrays.asList(commands).contains(List[0]);
        if (!cond) return false;
        args = new ArrayList<>();
        cmd = List[0];
        boolean appendOperator = Arrays.asList(List).contains(">");
        boolean overWriteOperator = Arrays.asList(List).contains(">>");

        for (int i = 1; i < List.length; i++) {
            args.add(List[i]);
        }


        if (appendOperator) {
            args.remove(args.indexOf(">"));
            args.add(0, cmd);
            shell.redirectAppend(args);
            return true;
        } else if (overWriteOperator) {
            args.remove(args.indexOf(">>"));
            args.add(0, cmd);
            shell.overWrite(args);
            return true;
        }
        switch (cmd) {
            case "cd":
                shell.cd(args);
                break;
            case "ls":
                print(shell.ls(args));
                break;
            case "cp":
                shell.cp(args);
                break;
            case "mdir":
                shell.mdir(args);
                break;
            case "rdir":
                shell.rdir(args);
                break;
            default:
                System.exit(0);
        }

        return true;
    }

}
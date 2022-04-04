import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Shell {

    public File currentDirectory;

    Shell() {
        currentDirectory = new File(System.getProperty("user.dir"));
    }

    public File getAbsolute(String path) {
        File file = new File(path);
        if (!file.isAbsolute()) {
            file = new File(currentDirectory.getAbsolutePath(), path);
        }
        return file;
    }

    public void cp(ArrayList<String> args) {
        if (args.size() > 2) {
            System.out.println("Too many argument");
        } else if (args.size() < 2) {
            System.out.println("Few argument");
        } else {
            cp(args.get(0), args.get(1));
        }
    }

    public void cp(String source, String destination) {
        File sourceFile;
        File destinationFile;
        InputStream input;
        OutputStream output;
        try {
            sourceFile = getAbsolute(source);
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                destination += "\\";
            } else {
                destination += '/';
            }
            destination += sourceFile.getName();
            destinationFile = new File(destination);
            input = new FileInputStream(sourceFile);
            output = new FileOutputStream(destinationFile);
            byte[] buffer = new byte[1024];
            int numberOfBytes;
            while ((numberOfBytes = input.read(buffer)) > 0) {
                output.write(buffer, 0, numberOfBytes);
            }
            input.close();
            output.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static String[] splitCommand(String command) {
        java.util.List<String> matchList = new java.util.ArrayList<>();

        Pattern regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
        Matcher regexMatcher = regex.matcher(command);
        while (regexMatcher.find()) {
            if (regexMatcher.group(1) != null) {
                // Add double-quoted string without the quotes
                matchList.add(regexMatcher.group(1));
            } else if (regexMatcher.group(2) != null) {
                // Add single-quoted string without the quotes
                matchList.add(regexMatcher.group(2));
            } else {
                // Add unquoted word
                matchList.add(regexMatcher.group());
            }
        }
        return matchList.toArray(new String[matchList.size()]);
    }

    public void mdir(ArrayList<String> args) {
        if (args.size() > 1) {
            System.out.println("Too Many Arguments");
            return;
        } else if (args.size() < 1) {
            System.out.println("Too Few Arguments");
            return;
        }
        String path = args.get(0);
        try {
            if (Files.exists(Paths.get(path))) {
                System.out.println("Directory already exists");
            } else {
                Files.createDirectories(Paths.get(path));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String ls(ArrayList<String> args) {
        StringBuilder res = new StringBuilder();
        boolean cond = true;
        for (int i = 0; i < args.size(); i++) {
            File f = getAbsolute(args.get(i));
            if (!f.isDirectory()) {
                cond = false;
                System.out.println("ls: cannot access '" + args.get(i) + "': No such file or directory");
                args.remove(args.get(i));
                i--;
            }
        }
        if (args.size() == 0 && cond) {
            File f = currentDirectory;
            String[] files = f.list();
            assert files != null;
            for (String s : files)
                res.append(s).append('\n');
        } else if (args.size() == 1 && cond) {
            File f = getAbsolute(args.get(0));
            String[] files = f.list();
            assert files != null;
            for (String s : files)
                res.append(s).append('\n');
        } else {
            for (String arg : args) {
                res.append('\n').append(getAbsolute(arg).getAbsolutePath()).append(" : \n");
                File f = getAbsolute(arg);
                String[] files = f.list();
                assert files != null;
                for (String s : files)
                    res.append(s).append('\n');
                res.append('\n' + '\n');
            }
        }
        return res.toString();
    }

    public void cd(ArrayList<String> args) {
        if (args.size() > 1) {
            System.out.println("too many arguments!");
            return;
        } else if (args.size() == 0) {
            currentDirectory = getAbsolute(System.getProperty("user.home"));
            return;
        }
        String sourcePath = args.get(0);
        if (sourcePath.equals("..")) {
            String parent = currentDirectory.getParent();
            currentDirectory = getAbsolute(parent);
        } else {
            File f = getAbsolute(sourcePath);
            if (!f.exists())
                System.out.println("No such file exists");
            else
                currentDirectory = f;
        }
    }

    public void rdir(ArrayList<String> args) {
        if (args.size() > 1) {
            System.out.println("too many arguments!");
            return;
        } else if (args.size() == 0) {
            System.out.println("too few arguments!");
            return;
        }
        String sourcePath = args.get(0);
        File f = getAbsolute(sourcePath);
        if (!f.exists())
            System.out.println("No such directory exists");
        else if (f.isFile())
            System.out.println("Cannot delete file");
        else if (!f.delete())
            System.out.println("Cannot delete non-empty directory.");
    }

    public void redirectAppend(ArrayList<String> args) throws Exception {
        String command = args.get(0);
        String path = args.get(args.size() - 1);
        args.remove(args.size() - 1);
        args.remove(0);
        String content = "";
        if ("ls".equals(command)) {
            content = ls(args);
        } else {
            throw new IllegalArgumentException("Unexpected value: " + command);
        }
        File file = getAbsolute(path);
        if (!file.exists() || !file.isFile()) {
            System.out.println("Could not find file: " + file);
            return;
        }
        FileWriter writer = new FileWriter(file, true);
        writer.append(content);
        writer.append('\n');
        writer.close();
    }

    public void overWrite(ArrayList<String> args) throws IOException {
        String command = args.get(0);
        String path = args.get(args.size() - 1);
        args.remove(args.size() - 1);
        args.remove(0);
        String content = "";
        if ("ls".equals(command)) {
            content = ls(args);
        } else {
            throw new IllegalArgumentException("Unexpected value: " + command);
        }
        File file = getAbsolute(path);
        if (!file.exists() || !file.isFile()) {
            System.out.println("Could not find file: " + file);
            return;
        }
        FileWriter writer = new FileWriter(file, false);
        writer.write(content);
        writer.close();
    }

}
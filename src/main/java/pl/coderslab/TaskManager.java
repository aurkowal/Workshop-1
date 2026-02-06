package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serial;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TaskManager {
    private static final Scanner scanner = new Scanner(System.in);
    static String[][] tasks;
    public static final String pathTasks = "src/main/resources/tasks.csv";
    public static final Path tasksCSV = Paths.get(pathTasks);

    public static void main(String[] args) throws IOException {

        tasks = readFile();
        boolean isContinue = true;
        while (isContinue) {
            showOptions();
            isContinue = loadAction();
        }
    }

    public static void showOptions() {
        System.out.println("Please select an option: ");
        String[] options = {"add", "remove", "list", ConsoleColors.BLUE + "exit" + ConsoleColors.RESET};
        for (int option = 0; option < options.length; option++) {
            System.out.println(options[option]);
        }
    }

    public static String[][] readFile() throws IOException {

        StringBuilder reading = new StringBuilder();

        try (Scanner scan = new Scanner(tasksCSV)) {
            while (scan.hasNextLine()) {
                reading.append(scan.nextLine()).append("\n");
            }
        } catch (FileNotFoundException e) {
            System.out.println("brak pliku");
        }

        String contentTasksCSV = reading.toString().trim(); //sb >> string

        String[] linesTasksCSV = contentTasksCSV.split("\n");
        String[][] contentArray = new String[linesTasksCSV.length][3];

        for (int line = 0; line < linesTasksCSV.length; line++) {
            String parts[] = linesTasksCSV[line].split(",");
            for (int part = 0; part < parts.length; part++) {
                contentArray[line][part] = parts[part].trim();
            }
        }
        return contentArray;
    }

    public static boolean loadAction() throws FileNotFoundException {

        String input = scanner.nextLine().trim().toLowerCase();

        switch (input) {
            case "add":
                addTask();
                break;
            case "remove":
                removeTask();
                break;
            case "list":
                listTask();
                break;
            case "exit":
                try {
                    exitTask();
                    return false;
                } catch (IOException e) {
                    System.err.println("Can not write file");
                }

                break;
            default:
                System.out.println("I don't understand :( ");

        }
        return true;

    }

    private static String readDateDashed() {
        while (true) {
            System.out.println("Please add task due date in format " + ConsoleColors.PURPLE + "yyyy-MM-dd:" + ConsoleColors.RESET);
            String input = scanner.nextLine().trim();
            // Format: 4 cyfry, myślnik, 2 cyfry, myślnik, 2 cyfry
            if (!input.matches("\\d{4}-\\d{2}-\\d{2}")) {
                System.out.println("Wrong format! Use yyyy-MM-dd.");
                continue;
            }
            String[] parts = input.split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);

            if (month < 1 || month > 12) {
                System.out.println("Invalid month. Use 01-12.");
                continue;
            }
            int[] maxDays = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
            int maxDay = maxDays[month - 1];
            if (day < 1 || day > maxDay) {
                System.out.println("Invalid day for this month.");
                continue;
            }
            return input;
        }
    }


    public static void addTask() {
        System.out.println("Please add task description");
        String description = scanner.nextLine().trim().replace(",", " "); //błąd csv
        String date = readDateDashed();
        System.out.println("Is your task important: " + ConsoleColors.RED_BOLD + "true / false" + ConsoleColors.RESET);
        boolean isImportant;
        while (true) {
            String important = scanner.nextLine().trim().toLowerCase();
            if (important.equals("true")) {
                isImportant = true;
                break;
            } else if (important.equals("false")) {
                isImportant = false;
                break;
            } else {
                System.out.println("Error, only true / false value is correct, try again:");
            }
        }

        String[][] tasksAdded = Arrays.copyOf(tasks, tasks.length + 1);
        tasksAdded[tasksAdded.length - 1] = new String[]{description, date, String.valueOf(isImportant)};
        tasks = tasksAdded;
        System.out.println("Tasks added correctly");
    }

    public static void removeTask() {
        System.out.println("Please select number to remove");
        while (true) {
            while (!scanner.hasNextInt()) {
                System.out.println("Please select a number");
                scanner.next();
            }
            int number = scanner.nextInt();
            scanner.nextLine();
            if (number < 0 || number >= tasks.length) {
                System.out.println("Number out of range, min number is: 0, max number is: " + (tasks.length - 1));
                continue;
            }
            tasks = ArrayUtils.remove(tasks, number);
            System.out.println("Removed");
            break;
        }
    }

    public static void listTask() {
        for (int i = 0; i < tasks.length; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < tasks[i].length; j++) {
                sb.append(tasks[i][j]).append(" ");
            }
            System.out.println(i + " : " + sb);
        }
    }

    public static void exitTask() throws IOException {
        List<String> outList = new ArrayList<>();
        for (int i = 0; i < tasks.length; i++) {
            outList.add(tasks[i][0] + "," + tasks[i][1] + "," + tasks[i][2]);

        }
        Files.write(tasksCSV, outList);
        System.out.printf(ConsoleColors.RED + "Bye, bye!" + ConsoleColors.RESET);

    }
}

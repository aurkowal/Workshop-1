package pl.coderslab;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class TaskManager {
    private static final Scanner scanner = new Scanner(System.in);
    static String[][] tasks;
    public static void main(String[] args) throws FileNotFoundException {
        tasks = readFile();
        showOptions();
        loadAction();


    }

    public static void showOptions() {
        System.out.println("Please select an option: ");
        String[] options = {"add", "remove", "list", "exit"};
        for (int option = 0; option < options.length; option++) {
            System.out.println(options[option]);
        }
    }

    public static String[][] readFile() throws FileNotFoundException {
        File tasksCSV = new File("src/main/resources/tasks.csv");
        StringBuilder reading = new StringBuilder();
        try {
            Scanner scan = new Scanner(tasksCSV);
            while (scan.hasNextLine()) {
                reading.append(scan.nextLine()).append("\n");
            }
        } catch (FileNotFoundException e) {
            System.out.println("brak pliku");
        }

        String contentTasksCSV = reading.toString().trim(); //sb >> string

        int numOflinesInTasksCSV = contentTasksCSV.isEmpty() ? 0 : contentTasksCSV.split("\n").length;
        String[] linesTasksCSV = contentTasksCSV.split("\n");
        String[][] contentArray = new String[numOflinesInTasksCSV][3];

        for (int line = 0; line < numOflinesInTasksCSV; line++) {
            String parts[] = linesTasksCSV[line].split(",");
            for (int part = 0; part < parts.length; part++) {
                contentArray[line][part] = parts[part].trim();
            }
        }
        return contentArray;
    }

    public static void loadAction() throws FileNotFoundException {
        while (true) {
            String input = scanner.nextLine().trim().toLowerCase();

            switch (input) {
                case "add":
                    addTask();
                    return;
                case "remove":
                    removeTask();
                    return;
                case "list":
                    listTask();
                    return;
                case "exit":
                    exitTask();
                    return;
                default:
                    System.out.println("Please select a correct option.");
                    while (!scanner.hasNext()) {
                        scanner.next();
                    }

            }
        }

    }

    public static void addTask() {
        System.out.println("Dodam");
    }

    public static void removeTask() {
        System.out.println("usune");

    }

    public static void listTask() throws FileNotFoundException {
        System.out.println("listuje");
        for (String[] task : tasks) {
            System.out.println(Arrays.toString(task));
        }


    }

    public static void exitTask() {
        System.out.println("wychodze");

    }

}

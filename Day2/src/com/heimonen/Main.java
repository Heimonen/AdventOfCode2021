package com.heimonen;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException {
        task1(getBufferedReader());
        task2(getBufferedReader());
    }

    private static void task1(BufferedReader reader) throws IOException {
        int horizontal = 0;
        int depth = 0;
        String line;
        while((line = reader.readLine()) != null) {
            String[] operation = line.split("\\s");
            switch (operation[0]) {
                case "forward" -> horizontal += Integer.parseInt(operation[1]);
                case "down" -> depth += Integer.parseInt(operation[1]);
                case "up" -> depth -= Integer.parseInt(operation[1]);
            }
        }
        System.out.println("Task 1 result: " + horizontal * depth);
    }

    private static void task2(BufferedReader reader) throws IOException {
        int horizontal = 0;
        int depth = 0;
        int aim = 0;
        String line;
        while((line = reader.readLine()) != null) {
            String[] operation = line.split("\\s");
            String instruction = operation[0];
            int value = Integer.parseInt(operation[1]);
            switch (instruction) {
                case "forward" -> {
                    horizontal += value;
                    depth += aim * value;
                }
                case "down" -> aim += value;
                case "up" -> aim -= value;
            }
        }
        System.out.println("Task 2 result: " + horizontal * depth);
    }

    private static BufferedReader getBufferedReader() throws IOException {
        Path path = Paths.get("input.txt");
        return Files.newBufferedReader(path);
    }
}

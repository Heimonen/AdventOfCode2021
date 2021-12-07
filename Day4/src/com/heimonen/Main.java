package com.heimonen;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        List<Board> boards = sortBoardsByWinOrder(getBufferedReader());
        Board task1 = boards.get(0);
        Board task2 = boards.get(boards.size() - 1);
        System.out.println("Task 1 Score: " + task1.getScore());
        task1.print();
        // win 10374
        System.out.println("Task 2 Score: " + task2.getScore());
        task2.print();
        // win 24742
    }

    private static List<Board> sortBoardsByWinOrder(BufferedReader reader) throws IOException {
        String line;
        List<String> drawnNumbers = new ArrayList<>();
        List<Board> boards = new ArrayList<>();
        List<Board> orderedBoardsByWin = new ArrayList<>();
        while((line = reader.readLine()) != null) {
            if (line.length() > 15) {
                drawnNumbers.addAll(Arrays.asList(line.split(",")));
            } else {
                if (!line.isBlank()) {
                    if (boards.isEmpty() || boards.get(boards.size() - 1).hasAllEntries()) {
                        boards.add(new Board(line));
                    } else {
                        boards.get(boards.size() - 1).addLine(line);
                    }
                }
            }
        }
        for (String drawnNumber: drawnNumbers) {
            for (Board board: boards) {
                if (!board.hasWon()) {
                    boolean isAWin = board.mark(Integer.parseInt(drawnNumber));
                    if (isAWin) {
                        board.calculateScore(Integer.parseInt(drawnNumber));
                        orderedBoardsByWin.add(board);
                    }
                }
            }
        }
        return orderedBoardsByWin;
    }

    private static BufferedReader getBufferedReader() throws IOException {
        Path path = Paths.get("input.txt");
        return Files.newBufferedReader(path);
    }
}

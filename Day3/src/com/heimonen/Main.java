package com.heimonen;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        task1(getBufferedReader());
        task2(getBufferedReader());
    }

    private static void task1(BufferedReader reader) throws IOException {
        String line;
        StringBuilder gammaRate = new StringBuilder();
        StringBuilder epsilonRate = new StringBuilder();
        int[] parsedMatrix = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        while((line = reader.readLine()) != null) {
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == '1') {
                    parsedMatrix[i]++;
                } else if (line.charAt(i) == '0') {
                    parsedMatrix[i]--;
                } else {
                    throw new NumberFormatException("Invalid input");
                }
            }
        }
        for (int entry: parsedMatrix) {
            if (entry > 0) {
                gammaRate.append(1);
                epsilonRate.append(0);
            } else {
                gammaRate.append(0);
                epsilonRate.append(1);
            }
        }
        System.out.println("Power Consumption: " + Integer.parseInt(gammaRate.toString(), 2) * Integer.parseInt(epsilonRate.toString(), 2));
    }

    private static void task2(BufferedReader reader) throws IOException {
        List<String> lines = new ArrayList<>();
        String line;
        while((line = reader.readLine()) != null) {
            lines.add(line);
        }
        int oxygenGeneratorRating = getOxygenGeneratorRating(lines, 0);
        int co2ScrubberRating = getCo2ScrubberRating(lines, 0);
        System.out.println("Life support rating: " + oxygenGeneratorRating * co2ScrubberRating);
    }

    private static int getOxygenGeneratorRating(List<String> toParse, int currentBit) {
        if (toParse.size() == 1) {
            return Integer.parseInt(toParse.get(0), 2);
        }
        List<String> oneSignificant = new ArrayList<>();
        List<String> zeroSignificant = new ArrayList<>();
        parseData(oneSignificant, zeroSignificant, toParse, currentBit);
        if (oneSignificant.size() > zeroSignificant.size()) {
            return getOxygenGeneratorRating(oneSignificant, ++currentBit);
        } else if (oneSignificant.size() < zeroSignificant.size()) {
            return getOxygenGeneratorRating(zeroSignificant, ++currentBit);
        } else {
            return getOxygenGeneratorRating(oneSignificant, ++currentBit);
        }
    }

    private static int getCo2ScrubberRating(List<String> toParse, int currentBit) {
        if (toParse.size() == 1) {
            return Integer.parseInt(toParse.get(0), 2);
        }
        List<String> oneSignificant = new ArrayList<>();
        List<String> zeroSignificant = new ArrayList<>();
        parseData(oneSignificant, zeroSignificant, toParse, currentBit);
        if (oneSignificant.size() < zeroSignificant.size()) {
            return getCo2ScrubberRating(oneSignificant, ++currentBit);
        } else if (oneSignificant.size() > zeroSignificant.size()) {
            return getCo2ScrubberRating(zeroSignificant, ++currentBit);
        } else {
            return getCo2ScrubberRating(zeroSignificant, ++currentBit);
        }
    }

    private static void parseData(List<String> oneSignificant, List<String> zeroSignificant, List<String> toParse, int currentBit) {
        for (String line: toParse) {
            if (line.charAt(currentBit) == '1') {
                oneSignificant.add(line);
            } else if (line.charAt(currentBit) == '0') {
                zeroSignificant.add(line);
            } else {
                throw new NumberFormatException("Invalid input");
            }
        }
    }

    private static BufferedReader getBufferedReader() throws IOException {
        Path path = Paths.get("input.txt");
        return Files.newBufferedReader(path);
    }
}

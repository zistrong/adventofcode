package com.zistrong.adventofcode2023;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day3 {

    /**
     * 467..114..
     * ...*......
     * ..35..633.
     * ......#...
     * 617*......
     * .....+.58.
     * ..592.....
     * ......755.
     * ...$.*....
     * .664.598..
     */

    public static final String DOT = ".";

    @Test
    public void part1() throws IOException {
        List<String> contents = Files.readAllLines(Path.of("./src/test/resources/2023/", "day3.input"));


        int sum = 0;
        for (int i = 0; i < contents.size(); i++) {
            String content = contents.get(i);

            int startIndex = -1;
            int endIndex = -1;

            for (int j = 0; j < content.length(); j++) {

                if (startIndex == -1 && Character.isDigit(content.charAt(j))) {
                    startIndex = j;
                }
                if (startIndex != -1 && !Character.isDigit(content.charAt(j))) {
                    endIndex = j;
                }

                if (startIndex != -1 && endIndex != -1) {
                    if (adjacentSymbol(i, startIndex, endIndex, contents)) {
                        sum += Integer.parseInt(content.substring(startIndex, endIndex));
                    }
                    startIndex = -1;
                    endIndex = -1;
                }
            }
            if (startIndex != -1) {// this case .......755
                endIndex = content.length();
                if (adjacentSymbol(i, startIndex, endIndex, contents)) {
                    sum += Integer.parseInt(content.substring(startIndex, endIndex));
                }
            }
        }
        Assert.assertEquals(543867, sum);

    }

    private boolean adjacentSymbol(int line, int startIndex, int endIndex, List<String> contents) {

        if (isSymbol(line, startIndex - 1, contents)) {
            return true;
        }
        if (isSymbol(line, endIndex, contents)) {
            return true;
        }

        for (int i = startIndex - 1; i < endIndex + 1; i++) {
            if (isSymbol(line - 1, i, contents)) {
                return true;
            }
        }

        for (int i = startIndex - 1; i < endIndex + 1; i++) {
            if (isSymbol(line + 1, i, contents)) {
                return true;
            }
        }

        return false;
    }

    private boolean isSymbol(int i, int j, List<String> contents) {


        if (i < 0 || j < 0 || i > contents.size() - 1 || j > contents.get(i).length() - 1) {
            return false;
        }
        return !String.valueOf(contents.get(i).charAt(j)).equals(DOT);
    }
}

package com.zistrong.adventofcode2023;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day2 {

    private static final int RED = 12;

    private static final int GREEN = 13;

    private static final int BLUE = 14;

    @Test
    public void part1() throws IOException {

        List<String> contents = Files.readAllLines(Path.of("./src/test/resources/2023/", "day2.input"));
        String regEx = "Game (\\d+)";
        int sum = 0;
        for (String content : contents) {
            String[] ss = content.split(":");
            Pattern pat = Pattern.compile(regEx);
            Matcher mat = pat.matcher(ss[0]);
            int gameNumber = 0;
            if (mat.find()) {
                gameNumber = Integer.parseInt(mat.group(1));
            }
            String[] sss = ss[1].trim().split(";");
            boolean flag = true;
            for (String s : sss) {
                int blueNumber = getNumber(s, "blue");
                int redNumber = getNumber(s, "red");
                int greenNumber = getNumber(s, "green");
                if (blueNumber > BLUE || greenNumber > GREEN || redNumber > RED) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                sum += gameNumber;
            }
        }
        Assert.assertEquals(1867, sum);
    }

    @Test
    public void part2() throws IOException {

        List<String> contents = Files.readAllLines(Path.of("./src/test/resources/2023/", "day2.input"));
        int sum = 0;
        for (String content : contents) {
            String[] ss = content.split(":");

            String[] sss = ss[1].trim().split(";");
            int fewestBlueNumber = 0;
            int fewestRedNumber = 0;
            int fewestGreenNumber = 0;
            for (String s : sss) {
                int blueNumber = getNumber(s, "blue");
                int redNumber = getNumber(s, "red");
                int greenNumber = getNumber(s, "green");
                fewestGreenNumber = Math.max(greenNumber, fewestGreenNumber);
                fewestRedNumber = Math.max(redNumber, fewestRedNumber);
                fewestBlueNumber = Math.max(blueNumber, fewestBlueNumber);
            }
            sum += fewestGreenNumber * fewestBlueNumber * fewestRedNumber;

        }
        Assert.assertEquals(84538, sum);
    }

    private static int getNumber(String s, String color) {
        String colorReg = "(\\d+) ";
        Matcher mat1;
        Pattern pat1;
        pat1 = Pattern.compile(colorReg + color);
        mat1 = pat1.matcher(s.trim());
        if (mat1.find()) {
            return Integer.parseInt(mat1.group(1));
        }
        return 0;
    }
}

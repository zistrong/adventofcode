package com.zistrong.adventofcode2023;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day4 {


    /**
     * Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
     * Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
     * Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
     * Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
     * Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
     * Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
     * <p>
     * card1 83, 86, 17, 48   -> 1, 1, 2, 4
     * 61, 32 -> 1, 1
     * 21,1  ->1, 1
     * 84-> 1
     */
    //https://aoc23.netlify.app/day4
    @Test
    public void part1() throws IOException {

        List<String> contents = Files.readAllLines(Path.of("./src/test/resources/2023/", "day4.input"));

        int sum = 0;
        for (String content : contents) {
            String line = content.replaceAll("Card \\d+: ", "");
            List<Integer> winNumbers = getNumberList(line.split("\\|")[0]);
            List<Integer> allNumbers = getNumberList(line.split("\\|")[1]);
            int subSum = 0;

            int count = -1;
            //21485
            for (Integer allNumber : allNumbers) {
                if (winNumbers.contains(allNumber)) {
                    subSum += beishu(count);

                    // if (count == 2) {
                    //     count = 0;
                    // }
                    count++;
                }
            }
            sum += subSum;
        }


        System.out.println(sum);

    }

    private int beishu(int count) {

        if (count < 0) {
            count = 0;
        }
        return (int) Math.pow(2, count);
    }

    private List<Integer> getNumberList(String src) {
        List<Integer> list = new ArrayList<>();
        String regEx = "\\d+";
        Pattern pat = Pattern.compile(regEx);
        Matcher mat = pat.matcher(src);
        while (mat.find()) {
            list.add(Integer.parseInt(mat.group()));
        }
        return list;
    }

}

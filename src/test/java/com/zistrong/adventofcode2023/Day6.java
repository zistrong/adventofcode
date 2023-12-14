package com.zistrong.adventofcode2023;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day6 {


    @Test
    public void part1() throws IOException {
        List<String> contents = Files.readAllLines(Path.of("./src/test/resources/2023/", "day6.input"));

        List<List<Integer>> lists = new ArrayList<>();
        contents.forEach(item -> {
            List<Integer> list = new ArrayList<>();
            lists.add(list);
            String number = "(\\d+)";
            Pattern pat1 = Pattern.compile(number);
            Matcher mat1 = pat1.matcher(item);
            while (mat1.find()) {
                list.add(Integer.parseInt(mat1.group()));
            }
        });

        List<Integer> timeList = lists.get(0);
        List<Integer> distanceList = lists.get(1);

        long mutiply = 1;
        for (int k = 0; k < timeList.size(); k++) {
            long count = 0;
            count = getCount(timeList.get(k), distanceList.get(k), count);
            mutiply = mutiply * (timeList.get(k) % 2 == 0 ? count * 2 - 1 : count * 2);
        }
        Assert.assertEquals(1083852L, mutiply);

    }

    @Test
    public void part2() throws IOException {
        List<String> contents = Files.readAllLines(Path.of("./src/test/resources/2023/", "day6.input"));

        long time = Long.parseLong(contents.get(0).replace("Time:", "").replace(" ", ""));
        long distance = Long.parseLong(contents.get(1).replace("Distance:", "").replace(" ", ""));
        long count = 0;
        long mutiply = 1;
        count = getCount(time, distance, count);
        mutiply = mutiply * (time % 2 == 0 ? count * 2 - 1 : count * 2);
        Assert.assertEquals(23501589L, mutiply);

    }

    private static long getCount(long time, long distance, long count) {
        long mid = time / 2;
        while (mid > 0 && mid * (time - mid) > distance) {
            count++;
            mid--;
        }
        return count;
    }

}

package com.zistrong.adventofcode2023;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day1 {

    private final List<String> map = new ArrayList<>();


    @Test
    public void test() throws IOException {

        List<String> contents = Files.readAllLines(Path.of("./src/test/resources/2023/", "day1.input"));
        int sum = contents.stream().map(item -> {
            String s = item.replaceAll("[A-Za-z]", "");
            return Integer.parseInt(s.charAt(0) + s.substring(s.length() - 1));
        }).mapToInt(item -> item).sum();
        Assert.assertEquals(55108, sum);
    }

    private void initMap() {
        this.map.add("one");
        this.map.add("two");
        this.map.add("three");
        this.map.add("four");
        this.map.add("five");
        this.map.add("six");
        this.map.add("seven");
        this.map.add("eight");
        this.map.add("nine");
    }


    @Test
    public void test2() throws IOException {
        this.initMap();
        List<String> contents = Files.readAllLines(Path.of("./src/test/resources/2023/", "day1.input"));
        int sum = 0;
        for (String content : contents) {
            String firstNumber = null;
            String lastNumber = null;
            for (int i = 0; i < content.length(); i++) {
                char c = content.charAt(i);
                if (Character.isDigit(c)) {
                    if (firstNumber == null) {
                        firstNumber = String.valueOf(c);
                    }
                    lastNumber = String.valueOf(c);
                }
                for (int i1 = 0; i1 < this.map.size(); i1++) {
                    String n = this.map.get(i1);
                    int endIndex = Math.min(n.length() + i, content.length());
                    if (content.substring(i, endIndex).equals(n)) {
                        if (firstNumber == null) {
                            firstNumber = String.valueOf(i1 + 1);
                        }
                        lastNumber = String.valueOf(i1 + 1);
                    }
                }
            }

            if (lastNumber == null) {
                lastNumber = firstNumber;
            }
            sum += Integer.parseInt(firstNumber + lastNumber);

        }
        Assert.assertEquals(56324, sum);
    }
}

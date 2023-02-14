package com.zistrong.adventofcode;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Day6 {

    String content;

    @Test
    public void part1() {
        System.out.println(test(4));
    }
    @Test
    public void part2() {
        System.out.println(test(14));
    }

    private int test(int len) {
        int index = 0;
        for (int i = 0; i < this.content.length() - len; i++) {
            String sub = this.content.substring(i, i + len);
            boolean flag = true;
            for (int j = 0; j < sub.length() - 1; j++) {
                if (sub.substring(j + 1).indexOf(sub.charAt(j)) >= 0) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                index = i + len;
                break;
            }
        }
        return index;
    }

    @Before
    public void init() {
        try {
            this.content = Files.readString(Path.of("./src/test/resources/", "day6.input"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

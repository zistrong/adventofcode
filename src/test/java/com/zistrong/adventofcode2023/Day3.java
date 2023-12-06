package com.zistrong.adventofcode2023;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static final char DOT = '.';

    public static final char STAR = '*';

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
        return contents.get(i).charAt(j) != DOT;
    }

    @Test
    public void part2() throws IOException {
        List<String> contents = Files.readAllLines(Path.of("./src/test/resources/2023/", "day3.input"));

        int sum = 0;
        for (int i = 0; i < contents.size(); i++) {
            String content = contents.get(i);
            for (int j = 0; j < content.length(); j++) {

                if (content.charAt(j) == STAR) {
                    sum += getCurrentValue(i, j, contents);
                }

            }
        }
        Assert.assertEquals(79613331, sum);
    }

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
    private int getCurrentValue(int x, int y, List<String> contents) {


        int firstValue = -1;

        // left and right value
        if (y > 0 || y < contents.get(x).length() - 1) {
            StringBuilder sb = new StringBuilder();
            // get left value
            int i = y - 1;
            while (i >= 0 && Character.isDigit(contents.get(x).charAt(i))) {
                sb.append(contents.get(x).charAt(i));
                i--;
            }
            if (!sb.isEmpty()) {
                firstValue = Integer.parseInt(sb.reverse().toString());
                sb = new StringBuilder();
            }

            // get right value
            i = y + 1;
            while (i < contents.get(x).length() && Character.isDigit(contents.get(x).charAt(i))) {
                sb.append(contents.get(x).charAt(i));
                i++;
            }
            if (!sb.isEmpty()) {
                if (firstValue == -1) {// if firstValues is null, set
                    firstValue = Integer.parseInt(sb.toString());
                } else {
                    return firstValue * Integer.parseInt(sb.toString());// return value
                }
            }
        }
        // top
        if (x > 0) {
            int i = x - 1;
            String content = contents.get(i);
            int j = Math.max(0, y - 1);
            // 如果（i,j)是数字， 往左找， 否则退出
            while (j > -1 && Character.isDigit(content.charAt(j))) {
                j--;
            }
            int left = j;
            j = Math.min(content.length() - 1, y + 1);
            // 如果（i,j)是数字， 往右找， 否则退出
            while (j < content.length() && Character.isDigit(content.charAt(j))) {
                j++;
            }
            int right = j;
            // 确定了一个区间， 从这个区间中找数字， 找到两个， 直接乘积返回
            String sub = content.substring(left + 1, right);
            if (!sub.equals(String.valueOf(DOT))) {

                String regEx = "\\d+";
                Pattern pat = Pattern.compile(regEx);
                Matcher mat = pat.matcher(sub);
                if (mat.find()) {
                    String one = mat.group(0);
                    if (firstValue != -1) {
                        return firstValue * Integer.parseInt(one);
                    } else {
                        firstValue = Integer.parseInt(one);
                    }
                }
                if (mat.find()) {
                    String two = mat.group(0);
                    if (firstValue != -1) {
                        return firstValue * Integer.parseInt(two);
                    } else {
                        firstValue = Integer.parseInt(two);
                    }
                }
            }
        }
        // bottom
        if (x < contents.size() - 1) {

            int i = x + 1;
            String content = contents.get(i);
            int j = Math.max(0, y - 1);
            // 如果（i,j)是数字， 往左找， 否则退出
            while (j > -1 && Character.isDigit(content.charAt(j))) {
                j--;
            }
            int left = j;
            j = Math.min(content.length() - 1, y + 1);
            // 如果（i,j)是数字， 往右找， 否则退出
            while (j < content.length() && Character.isDigit(content.charAt(j))) {
                j++;
            }
            int right = j;
            // 确定了一个区间， 从这个区间中找数字， 找到两个， 直接乘积返回
            String sub = content.substring(left + 1, right);
            if (!sub.equals(String.valueOf(DOT))) {

                String regEx = "\\d+";
                Pattern pat = Pattern.compile(regEx);
                Matcher mat = pat.matcher(sub);
                if (mat.find()) {
                    String one = mat.group(0);
                    if (firstValue != -1) {
                        return firstValue * Integer.parseInt(one);
                    } else {
                        firstValue = Integer.parseInt(one);
                    }
                }
                if (mat.find()) {
                    String two = mat.group(0);
                    if (firstValue != -1) {
                        return firstValue * Integer.parseInt(two);
                    }
                }
            }
        }
        return 0;
    }
}

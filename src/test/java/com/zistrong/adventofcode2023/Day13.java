package com.zistrong.adventofcode2023;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Day13 {

    /**
     * With your help, the hot springs team locates an appropriate spring which launches you neatly and precisely up to
     * the edge of Lava Island.
     * <p>
     * There's just one problem: you don't see any lava.
     * <p>
     * You do see a lot of ash and igneous rock; there are even what look like gray mountains scattered around.
     * After a while, you make your way to a nearby cluster of mountains only to discover that the valley between
     * them is completely full of large mirrors. Most of the mirrors seem to be aligned in a consistent way;
     * perhaps you should head in that direction?
     * <p>
     * As you move through the valley of mirrors, you find that several of them have fallen from the large metal
     * frames keeping them in place. The mirrors are extremely flat and shiny, and many of the fallen mirrors have
     * lodged into the ash at strange angles. Because the terrain is all one color, it's hard to tell where it's safe
     * to walk or where you're about to run into a mirror.
     * <p>
     * You note down the patterns of ash (.) and rocks (#) that you see as you walk (your puzzle input); perhaps by
     * carefully analyzing these patterns, you can figure out where the mirrors are!
     * <p>
     * For example:
     * <p>
     * #.##..##.
     * ..#.##.#.
     * ##......#
     * ##......#
     * ..#.##.#.
     * ..##..##.
     * #.#.##.#.
     * <p>
     * #...##..#
     * #....#..#
     * ..##..###
     * #####.##.
     * #####.##.
     * ..##..###
     * #....#..#
     * To find the reflection in each pattern, you need to find a perfect reflection across either a horizontal
     * line between two rows or across a vertical line between two columns.
     * <p>
     * In the first pattern, the reflection is across a vertical line between two columns; arrows on each of the two
     * columns point at the line between the columns:
     * <p>
     * 123456789
     * ><
     * #.##..##.
     * ..#.##.#.
     * ##......#
     * ##......#
     * ..#.##.#.
     * ..##..##.
     * #.#.##.#.
     * ><
     * 123456789
     * In this pattern, the line of reflection is the vertical line between columns 5 and 6. Because the vertical line
     * is not perfectly in the middle of the pattern, part of the pattern (column 1) has nowhere to reflect onto and
     * can be ignored; every other column has a reflected column within the pattern and must match exactly: column
     * 2 matches column 9, column 3 matches 8, 4 matches 7, and 5 matches 6.
     * <p>
     * The second pattern reflects across a horizontal line instead:
     * <p>
     * 1 #...##..# 1
     * 2 #....#..# 2
     * 3 ..##..### 3
     * 4v#####.##.v4
     * 5^#####.##.^5
     * 6 ..##..### 6
     * 7 #....#..# 7
     * This pattern reflects across the horizontal line between rows 4 and 5. Row 1 would reflect with a hypothetical row 8,
     * but since that's not in the pattern, row 1 doesn't need to match anything. The remaining rows match: row 2 matches row 7,
     * row 3 matches row 6, and row 4 matches row 5.
     * <p>
     * To summarize your pattern notes, add up the number of columns to the left of each vertical line of reflection; to that,
     * also add 100 multiplied by the number of rows above each horizontal line of reflection. In the above example,
     * the first pattern's vertical line has 5 columns to its left and the second pattern's horizontal line has 4 rows above it, a total of 405.
     * <p>  10*4+5;
     * Find the line of reflection in each of the patterns in your notes. What number do you get after summarizing all of your notes?
     */
    @Test
    public void part1() throws IOException {


        int sum = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader("./src/test/resources/2023/day13.input"))) {
            String line = reader.readLine();
            List<String> currentMirror = new ArrayList<>();
            while (line != null) {
                if (line.isEmpty()) {
                    sum += getScore(currentMirror);
                    currentMirror.clear();
                    line = reader.readLine();
                    continue;
                }
                currentMirror.add(line);
                line = reader.readLine();
            }
            sum += getScore(currentMirror);
        }
        Assert.assertEquals(31739, sum);
    }


    private int getReflectIndex(List<String> currentMirror) {
        int index = 0;

        for (int i = 0; i < currentMirror.size() - 1; i++) {
            int t = 0;
            int b = currentMirror.size() - 2 - i;
            boolean flag = false;
            while (b - t >= 1) {
                if (!Objects.equals(currentMirror.get(t), currentMirror.get(b))) {
                    break;
                }
                if (b - t == 1) {
                    flag = true;
                    break;
                }
                t++;
                b--;
            }
            if (flag) {
                index = b;
                break;
            }
        }

        for (int i = currentMirror.size() - 1; i > 0; i--) {

            int b = currentMirror.size() - 1;
            int t = currentMirror.size() - i;
            boolean flag = false;
            while (b - t >= 1) {

                if (!Objects.equals(currentMirror.get(t), currentMirror.get(b))) {
                    break;
                }
                if (b - t == 1) {
                    flag = true;
                    break;
                }
                t++;
                b--;
            }
            if (flag) {
                index = Math.max(index, b);
                break;
            }
        }
        return index;
    }

    private int getScore(List<String> currentMirror) {
        int h = getReflectIndex(currentMirror);
        if (h > 0) {
            return h * 100;
        }

        List<String> rotate = new ArrayList<>();
        for (int i = 0; i < currentMirror.get(0).length(); i++) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : currentMirror) {
                stringBuilder.append(s.charAt(i));
            }
            rotate.add(stringBuilder.toString());
        }

        return getReflectIndex(rotate);

    }

    /**
     * You resume walking through the valley of mirrors and - SMACK! - run directly into one. Hopefully nobody was watching,
     * because that must have been pretty embarrassing.
     * <p>
     * Upon closer inspection, you discover that every mirror has exactly one smudge: exactly one . or # should be the opposite type.
     * <p>
     * In each pattern, you'll need to locate and fix the smudge that causes a different reflection line to be valid.
     * (The old reflection line won't necessarily continue being valid after the smudge is fixed.)
     * <p>
     * Here's the above example again:
     * <p>
     * #.##..##.
     * ..#.##.#.
     * ##......#
     * ##......#
     * ..#.##.#.
     * ..##..##.
     * #.#.##.#.
     * <p>
     * #...##..#
     * #....#..#
     * ..##..###
     * #####.##.
     * #####.##.
     * ..##..###
     * #....#..#
     * The first pattern's smudge is in the top-left corner. If the top-left # were instead .,
     * it would have a different, horizontal line of reflection:
     * <p>
     * 1 ..##..##. 1
     * 2 ..#.##.#. 2
     * 3v##......#v3
     * 4^##......#^4
     * 5 ..#.##.#. 5
     * 6 ..##..##. 6
     * 7 #.#.##.#. 7
     * With the smudge in the top-left corner repaired, a new horizontal line of reflection between rows 3 and 4 now exists.
     * Row 7 has no corresponding reflected row and can be ignored, but every other row matches exactly: row 1 matches row 6,
     * row 2 matches row 5, and row 3 matches row 4.
     * <p>
     * In the second pattern, the smudge can be fixed by changing the fifth symbol on row 2 from . to #:
     * <p>
     * 1v#...##..#v1
     * 2^#...##..#^2
     * 3 ..##..### 3
     * 4 #####.##. 4
     * 5 #####.##. 5
     * 6 ..##..### 6
     * 7 #....#..# 7
     * Now, the pattern has a different horizontal line of reflection between rows 1 and 2.
     * <p>
     * Summarize your notes as before, but instead use the new different reflection lines. In this example,
     * the first pattern's new horizontal line has 3 rows above it and the second pattern's new horizontal line has 1 row above it,
     * summarizing to the value 400.
     * <p>
     * In each pattern, fix the smudge and find the different line of reflection.
     * What number do you get after summarizing the new reflection line in each pattern in your notes?
     */
    @Test
    public void part2() {

    }

}

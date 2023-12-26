package com.zistrong.adventofcode2023;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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


        try (BufferedReader reader = new BufferedReader(new FileReader(new File("./src/test/resources/2023/day13.input")))
        ) {
            String line = reader.readLine();
            boolean groupDone = false;
            int sum = 0;
            int firstNumber = 0;
            int secondNumber = 0;

            List<String> currentMirror = new ArrayList<>();
            boolean flag = true;
            while (line != null) {
                if (line.isEmpty()) {
                    sum += getScore(currentMirror, flag);
                    flag = !flag;
                    line = reader.readLine();
                    continue;
                }
                currentMirror.add(line);
                line = reader.readLine();
            }
            sum += getScore(currentMirror, flag);
            System.out.println(sum);
        }
    }

    List<String> contents;

    private int getScore(List<String> currentMirror, boolean flag) {
        if (flag) {
            System.out.println("ve" + currentMirror);
        } else {
            System.out.println("ho" + currentMirror);

        }
        currentMirror.clear();


        return 0;
    }

}

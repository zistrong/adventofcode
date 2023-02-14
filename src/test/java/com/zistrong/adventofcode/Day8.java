package com.zistrong.adventofcode;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day8 {

    /**
     * The expedition comes across a peculiar patch of tall trees all planted
     * carefully in a grid.
     * The Elves explain that a previous expedition planted these trees as a
     * reforestation effort.
     * Now, they're curious if this would be a good location for a tree house.
     * <p>
     * First, determine whether there is enough tree cover here to keep a tree house
     * hidden.
     * To do this, you need to count the number of trees that are visible from
     * outside the grid when looking directly along a row or column.
     * <p>
     * The Elves have already launched a quadcopter to generate a map with the
     * height of each tree (your puzzle input). For example:
     * <p>
     * 30373
     * 25512
     * 65332
     * 33549
     * 35390
     * <p>
     * Each tree is represented as a single digit whose value is its height, where 0
     * is the shortest and 9 is the tallest.
     * <p>
     * A tree is visible if all of the other trees between it and an edge of the
     * grid are shorter than it.
     * Only consider trees in the same row or column; that is, only look up, down,
     * left, or right from any given tree.
     * <p>
     * All of the trees around the edge of the grid are visible - since they are
     * already on the edge,
     * there are no trees to block the view. In this example, that only leaves the
     * interior nine trees to consider:
     * <p>
     * The top-left 5 is visible from the left and top. (It isn't visible from the
     * right or bottom since other trees of height 5 are in the way.)
     * The top-middle 5 is visible from the top and right.
     * The top-right 1 is not visible from any direction; for it to be visible,
     * there would need to only be trees of height 0 between it and an edge.
     * The left-middle 5 is visible, but only from the right.
     * The center 3 is not visible from any direction; for it to be visible, there
     * would need to be only trees of at most height 2 between it and an edge.
     * The right-middle 3 is visible from the right.
     * In the bottom row, the middle 5 is visible, but the 3 and 4 are not.
     * <p>
     * With 16 trees visible on the edge and another 5 visible in the interior, a
     * total of 21 trees are visible in this arrangement.
     * <p>
     * Consider your map; how many trees are visible from outside the grid?
     */
    @Test
    public void part1() {

        int gird[][] = new int[this.content.size()][this.content.size()];
        for (int i = 0; i < this.content.size(); i++) {
            for (int j = 0; j < this.content.get(i).length(); j++) {
                gird[i][j] = Integer.parseInt(this.content.get(i).charAt(j) + "");
            }
        }

        Map<String, Boolean> map = new HashMap<>();

        for (int i = 1; i < gird.length - 1; i++) {
            int j = 0;
            while (j + 1 < gird[j].length && gird[i][j] < gird[i][j + 1]) {
                map.put(String.valueOf(i) +"$" +String.valueOf(j + 1), true);
                j++;
            }
            j = gird[i].length-1;
            while (j - 1 >= 0 && gird[i][j] < gird[i][j - 1]) {
                map.put(String.valueOf(String.valueOf(i) +"$" + String.valueOf(j - 1)), true);
                j--;
            }

            j = 0;
            while (j + 1 < gird[j].length && gird[j][i] < gird[j+1][i]) {
                map.put(String.valueOf(j+1) +"$" + String.valueOf(i), true);
                j++;
            }
            j = gird[i].length-1;
            while (j - 1 >= 0 && gird[j][i] < gird[j-1][i]) {
                map.put(String.valueOf(String.valueOf(j-1) +"$" + String.valueOf(i)), true);
                j--;
            }
        }
        System.out.println(map.size()+gird.length*gird.length);

    }

    List<String> content;

    @Before
    public void init() throws IOException {
        this.content = Files.readAllLines(Path.of("./src/test/resources/", "day8.input"));
    }
}

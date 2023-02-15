package com.zistrong.adventofcode;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

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
     * A tree is visible if all the other trees between it and an edge of the
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


        Set<String> set = new HashSet<>();
        for (int i = 0; i < grid.length; i++) {
            //row, left to right
            int j = 0;
            int currentTallest = -1;

            while (j < grid[j].length - 1) {
                if (currentTallest < grid[i][j]) {
                    set.add(i + "$" + j);
                    currentTallest = grid[i][j];
                    if (currentTallest == 9) {
                        break;
                    }
                }
                j++;
            }

            //row, right to left
            currentTallest = -1;
            j = grid[i].length - 1;
            while (j >= 0) {
                if (currentTallest < grid[i][j]) {
                    set.add(i + "$" + j);
                    currentTallest = grid[i][j];
                    if (currentTallest == 9) {
                        break;
                    }
                }
                j--;
            }

            //column, top to bottom
            currentTallest = -1;
            j = 0;
            while (j < grid[j].length - 1) {
                if (currentTallest < grid[j][i]) {
                    set.add(j + "$" + i);
                    currentTallest = grid[j][i];
                    if (currentTallest == 9) {
                        break;
                    }
                }
                j++;
            }
            //column, bottom to top
            currentTallest = -1;
            j = grid[i].length - 1;
            while (j >= 0) {
                if (currentTallest < grid[j][i]) {
                    set.add(j + "$" + i);
                    currentTallest = grid[j][i];
                    if (currentTallest == 9) {
                        break;
                    }
                }
                j--;
            }
        }
        Assert.assertEquals(1676, set.size());

    }

    List<String> content;

    /**
     * Content with the amount of tree cover available, the Elves just need to know the best spot
     * to build their tree house: they would like to be able to see a lot of trees.
     * <p>
     * To measure the viewing distance from a given tree, look up, down, left, and right from that tree;
     * stop if you reach an edge or at the first tree that is the same height or taller than the tree under consideration.
     * (If a tree is right on the edge, at least one of its viewing distances will be zero.)
     * <p>
     * The Elves don't care about distant trees taller than those found by the rules above; the proposed tree
     * house has large eaves to keep it dry, so they wouldn't be able to see higher than the tree house anyway.
     * <p>
     * In the example above, consider the middle 5 in the second row:
     * <p>
     * 30373
     * 25512
     * 65332
     * 33549
     * 35390
     * <p>
     * Looking up, its view is not blocked; it can see 1 tree (of height 3).
     * Looking left, its view is blocked immediately; it can see only 1 tree (of height 5, right next to it).
     * Looking right, its view is not blocked; it can see 2 trees.
     * Looking down, its view is blocked eventually; it can see 2 trees (one of height 3, then the tree of height 5 that blocks its view).
     * <p>
     * A tree's scenic score is found by multiplying together its viewing distance in each of the four directions.
     * For this tree, this is 4 (found by multiplying 1 * 1 * 2 * 2).
     * <p>
     * However, you can do even better: consider the tree of height 5 in the middle of the fourth row:
     * <p>
     * 30373
     * 25512
     * 65332
     * 33549
     * 35390
     * <p>
     * Looking up, its view is blocked at 2 trees (by another tree with a height of 5).
     * Looking left, its view is not blocked; it can see 2 trees.
     * Looking down, its view is also not blocked; it can see 1 tree.
     * Looking right, its view is blocked at 2 trees (by a massive tree of height 9).
     * <p>
     * This tree's scenic score is 8 (2 * 2 * 1 * 2); this is the ideal spot for the tree house.
     * <p>
     * Consider each tree on your map. What is the highest scenic score possible for any tree?
     */
    @Test
    public void part2() {

        int highestScore = 0;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                int top = 0, bottom = 0, left = 0, right = 0;
                int currentTree = grid[i][j];

                for (int k = j + 1; k < grid[i].length; k++) {// to right
                    right++;
                    if (currentTree <= grid[i][k]) {//same height or taller than the tree under consideration, break
                        break;
                    }
                }
                if (right == 0) {
                    continue;
                }
                for (int k = j - 1; k >= 0; k--) {// to left

                    left++;
                    if (currentTree <= grid[i][k]) {
                        break;
                    }
                }
                if (left == 0) {
                    continue;
                }
                for (int k = i + 1; k < grid[i].length; k++) {// to bottom

                    bottom++;
                    if (currentTree <= grid[k][j]) {
                        break;
                    }
                }
                if (bottom == 0) {
                    continue;
                }
                for (int k = i - 1; k >= 0; k--) {// to top
                    top++;
                    if (currentTree <= grid[k][j]) {
                        break;
                    }
                }
                if (top == 0) {
                    continue;
                }
                int score = top * bottom * left * right;

                if (score > highestScore) {
                    highestScore = score;
                }

            }
        }

        Assert.assertEquals(313200, highestScore);

    }

    int[][] grid = null;

    @Before
    public void init() throws IOException {
        this.content = Files.readAllLines(Path.of("./src/test/resources/", "day8.input"));
        this.grid = new int[this.content.size()][this.content.size()];
        for (int i = 0; i < this.content.size(); i++) {
            for (int j = 0; j < this.content.get(i).length(); j++) {
                grid[i][j] = Integer.parseInt(String.valueOf(this.content.get(i).charAt(j)));
            }
        }
    }
}

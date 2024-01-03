package com.zistrong.adventofcode2023;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class Day14 {

    /**
     * You reach the place where all of the mirrors were pointing: a massive parabolic
     * reflector dish attached to the side of another large mountain.
     * <p>
     * The dish is made up of many small mirrors, but while the mirrors themselves are
     * roughly in the shape of a parabolic reflector dish, each individual mirror seems to be pointing in slightly the wrong direction.
     * If the dish is meant to focus light, all it's doing right now is sending it in a vague direction.
     * <p>
     * This system must be what provides the energy for the lava! If you focus the reflector dish,
     * maybe you can go where it's pointing and use the light to fix the lava production.
     * <p>
     * Upon closer inspection, the individual mirrors each appear to be connected via
     * an elaborate system of ropes and pulleys to a large metal platform below the dish.
     * The platform is covered in large rocks of various shapes. Depending on their position,
     * the weight of the rocks deforms the platform, and the shape of the
     * platform controls which ropes move and ultimately the focus of the dish.
     * <p>
     * In short: if you move the rocks, you can focus the dish. The platform even has a
     * control panel on the side that lets you tilt it in one of four directions!
     * The rounded rocks (O) will roll when the platform is tilted, while the cube-shaped rocks (#)
     * will stay in place. You note the positions of all of the empty spaces (.) and rocks (your puzzle input). For example:
     * <p>
     * O....#....
     * O.OO#....#
     * .....##...
     * OO.#O....O
     * .O.....O#.
     * O.#..O.#.#
     * ..O..#O..O
     * .......O..
     * #....###..
     * #OO..#....
     * Start by tilting the lever so all of the rocks will slide north as far as they will go:
     * <p>
     * OOOO.#.O..
     * OO..#....#
     * OO..O##..O
     * O..#.OO...
     * ........#.
     * ..#....#.#
     * ..O..#.O.O
     * ..O.......
     * #....###..
     * #....#....
     * You notice that the support beams along the north side of the platform are damaged;
     * to ensure the platform doesn't collapse, you should calculate the total load on the north support beams.
     * <p>
     * The amount of load caused by a single rounded rock (O) is equal to the number of rows from the rock
     * to the south edge of the platform, including the row the rock is on. (Cube-shaped rocks (#) don't contribute to load.) So,
     * the amount of load caused by each rock in each row is as follows:
     * <p>
     * OOOO.#.O.. 10
     * OO..#....#  9
     * OO..O##..O  8
     * O..#.OO...  7
     * ........#.  6
     * ..#....#.#  5
     * ..O..#.O.O  4
     * ..O.......  3
     * #....###..  2
     * #....#....  1
     * The total load is the sum of the load caused by all of the rounded rocks. In this example, the total load is 136.
     * <p>
     * Tilt the platform so that the rounded rocks all roll north. Afterward, what is the total load on the north support beams?
     */

    private static final char O = 'O';
    private static final char DOT = '.';
    private static final char SHARP = '#';
    List<StringBuilder> contents;

    @Test
    public void part1() throws IOException {

        contents = Files.readAllLines(Path.of("./src/test/resources/2023/", "day14.input"))
                .stream().map(StringBuilder::new).toList();
        north();
        Assert.assertEquals(108889L, getSum());
    }

    private void round() {
        north();
        west();
        south();
        east();
    }

    private void north() {
        for (int i = 0; i < contents.size() - 1; i++) {
            StringBuilder line = contents.get(i);
            for (int j = 0; j < line.length(); j++) {
                char c = line.charAt(j);
                if (c == O || c == SHARP) {
                    continue;
                }
                for (int k = i + 1; k < contents.size(); k++) {
                    StringBuilder nextLine = contents.get(k);
                    char next = nextLine.charAt(j);
                    if (next == SHARP) {
                        break;
                    }
                    if (next == O) {
                        nextLine.replace(j, j + 1, String.valueOf(DOT));
                        line.replace(j, j + 1, String.valueOf(O));
                        break;
                    }
                }
            }
        }
    }

    private void south() {
        for (int i = contents.size() - 1; i >= 0; i--) {
            StringBuilder line = contents.get(i);
            for (int j = 0; j < line.length(); j++) {
                char c = line.charAt(j);
                if (c == O || c == SHARP) {
                    continue;
                }
                for (int k = i - 1; k >= 0; k--) {
                    StringBuilder nextLine = contents.get(k);
                    char next = nextLine.charAt(j);
                    if (next == SHARP) {
                        break;
                    }
                    if (next == O) {
                        nextLine.replace(j, j + 1, String.valueOf(DOT));
                        line.replace(j, j + 1, String.valueOf(O));
                        break;
                    }
                }
            }
        }
    }

    private void west() {
        for (StringBuilder line : contents) {
            for (int j = 0; j < line.length(); j++) {
                char c = line.charAt(j);
                if (c == O || c == SHARP) {
                    continue;
                }
                for (int k = j + 1; k < line.length(); k++) {
                    char next = line.charAt(k);
                    if (next == SHARP) {
                        break;
                    }
                    if (next == O) {
                        line.replace(j, j + 1, String.valueOf(O));
                        line.replace(k, k + 1, String.valueOf(DOT));
                        break;
                    }
                }

            }
        }
    }

    private void east() {
        for (StringBuilder line : contents) {
            for (int j = line.length() - 1; j >= 0; j--) {
                char c = line.charAt(j);
                if (c == O || c == SHARP) {
                    continue;
                }
                for (int k = j - 1; k >= 0; k--) {
                    char next = line.charAt(k);
                    if (next == SHARP) {
                        break;
                    }
                    if (next == O) {
                        line.replace(j, j + 1, String.valueOf(O));
                        line.replace(k, k + 1, String.valueOf(DOT));
                        break;
                    }
                }

            }
        }
    }

    /**
     * The parabolic reflector dish deforms, but not in a way that focuses the beam. To do that,
     * you'll need to move the rocks to the edges of the platform. Fortunately,
     * a button on the side of the control panel labeled "spin cycle" attempts to do just that!
     * <p>
     * Each cycle tilts the platform four times so that the rounded rocks roll north, then west,
     * then south, then east. After each tilt, the rounded rocks roll as far as they can before the platform tilts in the next direction.
     * After one cycle, the platform will have finished rolling the rounded rocks in those four directions in that order.
     * <p>
     * Here's what happens in the example above after each of the first few cycles:
     * <p>
     * After 1 cycle:
     * .....#....
     * ....#...O#
     * ...OO##...
     * .OO#......
     * .....OOO#.
     * .O#...O#.#
     * ....O#....
     * ......OOOO
     * #...O###..
     * #..OO#....
     * <p>
     * After 2 cycles:
     * .....#....
     * ....#...O#
     * .....##...
     * ..O#......
     * .....OOO#.
     * .O#...O#.#
     * ....O#...O
     * .......OOO
     * #..OO###..
     * #.OOO#...O
     * <p>
     * After 3 cycles:
     * .....#....
     * ....#...O#
     * .....##...
     * ..O#......
     * .....OOO#.
     * .O#...O#.#
     * ....O#...O
     * .......OOO
     * #...O###.O
     * #.OOO#...O
     * This process should work if you leave it running long enough, but you're still worried about the north support beams.
     * To make sure they'll survive for a while, you need to calculate the total load on the north support beams after 1000000000 cycles.
     * <p>
     * In the above example, after 1000000000 cycles, the total load on the north support beams is 64.
     * <p>
     * Run the spin cycle for 1000000000 cycles. Afterward, what is the total load on the north support beams?
     */
    @Test
    public void part2() throws IOException {
        contents = Files.readAllLines(Path.of("./src/test/resources/2023/", "day14.input"))
                .stream().map(StringBuilder::new).toList();
        List<String> list = new ArrayList<>();
        int k;
        int size;
        while (true) {
            round();
            StringBuilder stringBuilder = new StringBuilder();
            for (StringBuilder content : contents) {
                stringBuilder.append(content);
            }
            if (list.contains(getMD5String(stringBuilder.toString()))) {// if exist , it means the round reach a cycle, break loop
                size = list.size();
                k = list.indexOf(getMD5String(stringBuilder.toString()));
                break;
            }
            list.add(getMD5String(stringBuilder.toString()));
        }
        for (int i = 0; i < (1000000000 - k - 1) % (size - k); i++) {// get the correct loop
            round();
        }
        Assert.assertEquals(104671L, getSum());

    }

    public static String getMD5String(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            return null;
        }
    }

    public long getSum() {
        int length = contents.size();
        long sum = 0L;
        for (int m = 0; m < length; m++) {
            sum += contents.get(m).toString().chars().filter(item -> item == O).count() * (length - m);
        }
        return sum;
    }
}

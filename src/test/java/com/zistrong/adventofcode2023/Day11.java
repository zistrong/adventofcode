package com.zistrong.adventofcode2023;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day11 {

    /**
     * You continue following signs for "Hot Springs" and eventually come across an observatory.
     * The Elf within turns out to be a researcher studying cosmic expansion using the giant telescope here.
     * <p>
     * He doesn't know anything about the missing machine parts; he's only visiting for this research project.
     * However, he confirms that the hot springs are the next-closest area likely to have people;
     * he'll even take you straight there once he's done with today's observation analysis.
     * <p>
     * Maybe you can help him with the analysis to speed things up?
     * <p>
     * The researcher has collected a bunch of data and compiled the data into a single giant image (your puzzle input).
     * The image includes empty space (.) and galaxies (#). For example:
     * <p>
     * ...#......
     * .......#..
     * #.........
     * ..........
     * ......#...
     * .#........
     * .........#
     * ..........
     * .......#..
     * #...#.....
     * The researcher is trying to figure out the sum of the lengths of the shortest path between every pair of galaxies.
     * However, there's a catch: the universe expanded in the time it took the light from those galaxies to reach the observatory.
     * <p>
     * Due to something involving gravitational effects, only some space expands.
     * In fact, the result is that any rows or columns that contain no galaxies should all actually be twice as big.
     * <p>
     * In the above example, three columns and two rows contain no galaxies:
     * <p>
     * v  v  v
     * ...#......
     * .......#..
     * #.........
     * >..........<
     * ......#...
     * .#........
     * .........#
     * >..........<
     * .......#..
     * #...#.....
     * ^  ^  ^
     * These rows and columns need to be twice as big; the result of cosmic expansion therefore looks like this:
     * <p>
     * ....#........
     * .........#...
     * #............
     * .............
     * .............
     * ........#....
     * .#...........
     * ............#
     * .............
     * .............
     * .........#...
     * #....#.......
     * Equipped with this expanded universe, the shortest path between every pair of galaxies can be found.
     * It can help to assign every galaxy a unique number:
     * <p>
     * ....1........
     * .........2...
     * 3............
     * .............
     * .............
     * ........4....
     * .5...........
     * ............6
     * .............
     * .............
     * .........7...
     * 8....9.......
     * In these 9 galaxies, there are 36 pairs. Only count each pair once; order within the pair doesn't matter.
     * For each pair, find any shortest path between the two galaxies using only steps that move up, down, left, or right exactly one .
     * or # at a time. (The shortest path between two galaxies is allowed to pass through another galaxy.)
     * <p>
     * For example, here is one of the shortest paths between galaxies 5 and 9:
     * <p>
     * ....1........
     * .........2...
     * 3............
     * .............
     * .............
     * ........4....
     * .5...........
     * .##.........6
     * ..##.........
     * ...##........
     * ....##...7...
     * 8....9.......
     * This path has length 9 because it takes a minimum of nine steps to get from galaxy 5 to galaxy 9
     * (the eight locations marked # plus the step onto galaxy 9 itself). Here are some other example shortest path lengths:
     * <p>
     * Between galaxy 1 and galaxy 7: 15
     * Between galaxy 3 and galaxy 6: 17
     * Between galaxy 8 and galaxy 9: 5
     * In this example, after expanding the universe, the sum of the shortest path between all 36 pairs of galaxies is 374.
     * <p>
     * Expand the universe, then find the length of the shortest path between every pair of galaxies. What is the sum of these lengths?
     */
    @Test
    public void part1() {


        long step = getStep();
        Assert.assertEquals(step, 9639160L);
    }

    int p = 2;

    List<Galaxy> galaxies;
    List<Integer> rows;
    List<Integer> columns;

    private long getStep() {
        long step = 0L;
        for (int i = 0; i < galaxies.size(); i++) {
            for (int j = i + 1; j < galaxies.size(); j++) {
                Galaxy start = galaxies.get(i);
                Galaxy end = galaxies.get(j);
                step = step + Math.abs(start.x - end.x) + Math.abs(start.y - end.y);
                for (Integer row : rows) {
                    if (row > Math.min(start.x, end.x) && row < Math.max(start.x, end.x)) {
                        step = step + (p - 1);
                    }
                }
                for (Integer column : columns) {
                    if (column > Math.min(start.y, end.y) && column < Math.max(start.y, end.y)) {
                        step = step + (p - 1);
                    }
                }
            }
        }
        return step;
    }

    /**
     * The galaxies are much older (and thus much farther apart) than the researcher initially estimated.
     * <p>
     * Now, instead of the expansion you did before, make each empty row or column one million times larger.
     * That is, each empty row should be replaced with 1000000 empty rows, and each empty column should be replaced with 1000000 empty columns.
     * <p>
     * (In the example above, if each empty row or column were merely 10 times larger,
     * the sum of the shortest paths between every pair of galaxies would be 1030.
     * If each empty row or column were merely 100 times larger, the sum of the shortest paths between every pair of galaxies would be 8410.
     * However, your universe will need to expand far beyond these values.)
     * <p>
     * Starting with the same initial image, expand the universe according to these new rules,
     * then find the length of the shortest path between every pair of galaxies. What is the sum of these lengths?
     */
    @Test
    public void part2() {
        p = 1000000;
        long step = getStep();
        Assert.assertEquals(step, 752936133304L);
    }

    record Galaxy(int x, int y) {

    }


    @Before
    public void init() throws IOException {
        List<String> space = Files.readAllLines(Path.of("./src/test/resources/2023/", "day11.input"));
        rows = new ArrayList<>();
        for (int i = 0; i < space.size(); i++) {
            if (!space.get(i).contains("#")) {
                rows.add(i);
            }
        }

        galaxies = new ArrayList<>();
        columns = new ArrayList<>();
        for (int i = 0; i < space.get(0).length(); i++) {
            boolean flag = true;
            for (int j = 0; j < space.size(); j++) {
                if (space.get(j).charAt(i) == '#') {
                    flag = false;
                    galaxies.add(new Galaxy(j, i));
                }
            }
            if (flag) {
                columns.add(i);
            }
        }
    }
}

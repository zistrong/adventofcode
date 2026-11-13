package com.zistrong.adventofcode2023;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Enough sand has fallen; it can finally filter water for Snow Island. Well, almost.
 * <p>
 * The sand has been falling as large compacted bricks of sand, piling up to form an impressive stack here.
 * In order to make use of the sand to filter water, some of the bricks will need to be broken apart - nay,
 * disintegrated - back into freely flowing sand.
 * <p>
 * The Elves took a snapshot of the bricks while they were still falling (your puzzle input).
 * <p>
 * Each line of text in the snapshot represents the position of a single brick at the time the snapshot was taken.
 * The position is given as two {@code x,y,z} coordinates - one for each end of the brick - separated by a tilde (~).
 * Each brick is made up of a single straight line of cubes, and the snapshot is aligned to a three-dimensional cube grid.
 * <p>
 * A line like 2,2,2~2,2,2 means that both ends of the brick are at the same coordinate - a single cube.
 * A line like 0,0,1~0,0,10 represents a ten-cube brick which is oriented vertically.
 * <p>
 * The ground is at z=0 and is perfectly flat; the lowest z value a brick can have is therefore 1.
 * <p>
 * Because the snapshot was taken while the bricks were still falling, some bricks will still be in the air;
 * you'll need to start by figuring out where they will end up. Bricks are magically stabilized, so they never rotate,
 * even in weird situations like where a long horizontal brick is only supported on one end.
 * Two bricks cannot occupy the same position, so a falling brick will come to rest upon the first other brick it
 * encounters.
 * <p>
 * For the example snapshot:
 * <p>
 * 1,0,1~1,2,1<br>
 * 0,0,2~2,0,2<br>
 * 0,2,3~2,2,3<br>
 * 0,0,4~0,2,4<br>
 * 2,0,5~2,2,5<br>
 * 0,1,6~2,1,6<br>
 * 1,1,8~1,1,9
 * <p>
 * once all of the bricks have settled, brick A is the only brick supporting bricks B and C.
 * Brick B is one of two bricks supporting brick D and brick E; brick C is the other one.
 * Brick D supports brick F, brick E also supports brick F, and brick F supports brick G.
 * Brick G isn't supporting any bricks.
 * <p>
 * A brick can be safely disintegrated if, after removing it, no other bricks would fall further directly downward.
 * Don't actually disintegrate any bricks - just determine what would happen if, for each brick, only that brick
 * were disintegrated. Bricks can be disintegrated even if they're completely surrounded by other bricks.
 * <p>
 * In the example, 5 bricks can be safely disintegrated (B, C, D, E and G; A would drop B and C, F would drop G).
 * <p>
 * Part one: figure how the blocks will settle based on the snapshot. Once they've settled, consider disintegrating
 * a single brick; how many bricks could be safely chosen as the one to get disintegrated?
 * <p>
 * Part two: for each brick, count how many other bricks would fall if that brick were disintegrated,
 * then sum those counts up. How many bricks would fall in total?
 */
public class Day22 {

    private List<Brick> bricks;

    private int maxX;
    private int maxY;
    private int maxZ;

    @Test
    public void part1() {
        Assert.assertEquals(530, solvePart1());
    }

    @Test
    public void part2() {
        Assert.assertEquals(93292, solvePart2());
    }

    /**
     * The example from the puzzle statement: 5 bricks are safe to disintegrate, 7 fall in total.
     */
    @Test
    public void sample() {
        List<int[]> sample = List.of(
                new int[]{1, 0, 1, 1, 2, 1},
                new int[]{0, 0, 2, 2, 0, 2},
                new int[]{0, 2, 3, 2, 2, 3},
                new int[]{0, 0, 4, 0, 2, 4},
                new int[]{2, 0, 5, 2, 2, 5},
                new int[]{0, 1, 6, 2, 1, 6},
                new int[]{1, 1, 8, 1, 1, 9});
        this.bricks = new ArrayList<>();
        for (int[] item : sample) {
            this.bricks.add(new Brick(item));
        }
        this.maxX = 2;
        this.maxY = 2;
        this.maxZ = 9;
        settle();
        Assert.assertEquals(5, solvePart1());
        Assert.assertEquals(7, solvePart2());
    }

    /**
     * Drop every brick straight down (in ascending order of low z) using a height map per (x,y) column,
     * so each brick lands on the tallest column underneath its footprint.
     */
    private void settle() {
        int[][] heights = new int[maxX + 1][maxY + 1];
        bricks.sort(Comparator.comparingInt(brick -> brick.z1));
        for (Brick brick : bricks) {
            int floor = 0;
            for (int x = brick.x1; x <= brick.x2; x++) {
                for (int y = brick.y1; y <= brick.y2; y++) {
                    floor = Math.max(floor, heights[x][y]);
                }
            }
            int fall = brick.z1 - (floor + 1);
            brick.z1 -= fall;
            brick.z2 -= fall;
            for (int x = brick.x1; x <= brick.x2; x++) {
                for (int y = brick.y1; y <= brick.y2; y++) {
                    heights[x][y] = brick.z2;
                }
            }
        }
    }

    /**
     * For each brick, the set of bricks that directly hold it up (they occupy the cell right below its footprint).
     */
    private List<Set<Integer>> supporters() {
        Map<Long, Integer> occupied = new HashMap<>();
        for (int i = 0; i < bricks.size(); i++) {
            Brick brick = bricks.get(i);
            for (int x = brick.x1; x <= brick.x2; x++) {
                for (int y = brick.y1; y <= brick.y2; y++) {
                    for (int z = brick.z1; z <= brick.z2; z++) {
                        occupied.put(key(x, y, z), i);
                    }
                }
            }
        }
        List<Set<Integer>> supporters = new ArrayList<>();
        for (Brick brick : bricks) {
            Set<Integer> below = new HashSet<>();
            if (brick.z1 > 1) {
                int z = brick.z1 - 1;
                for (int x = brick.x1; x <= brick.x2; x++) {
                    for (int y = brick.y1; y <= brick.y2; y++) {
                        Integer index = occupied.get(key(x, y, z));
                        if (index != null) {
                            below.add(index);
                        }
                    }
                }
            }
            supporters.add(below);
        }
        return supporters;
    }

    private long key(int x, int y, int z) {
        return ((long) x << 20) | ((long) y << 10) | z;
    }

    private int solvePart1() {
        settle();
        List<Set<Integer>> supporters = supporters();
        Set<Integer> unsafe = new HashSet<>();
        for (Set<Integer> below : supporters) {
            if (below.size() == 1) {
                // this brick hangs on exactly one brick, that one may not be disintegrated
                unsafe.addAll(below);
            }
        }
        return bricks.size() - unsafe.size();
    }

    private long solvePart2() {
        settle();
        List<Set<Integer>> supporters = supporters();

        // reverse edges: brick j -> bricks resting on j
        List<Set<Integer>> holds = new ArrayList<>();
        for (int i = 0; i < bricks.size(); i++) {
            holds.add(new HashSet<>());
        }
        int[] supportCount = new int[bricks.size()];
        for (int i = 0; i < bricks.size(); i++) {
            for (int j : supporters.get(i)) {
                holds.get(j).add(i);
                supportCount[i]++;
            }
        }

        long total = 0;
        for (int removed = 0; removed < bricks.size(); removed++) {
            boolean[] fallen = new boolean[bricks.size()];
            int[] remain = supportCount.clone();
            Deque<Integer> queue = new ArrayDeque<>();
            fallen[removed] = true;
            queue.add(removed);
            long count = 0;
            while (!queue.isEmpty()) {
                int gone = queue.poll();
                for (int above : holds.get(gone)) {
                    if (fallen[above]) {
                        continue;
                    }
                    if (--remain[above] == 0) {
                        fallen[above] = true;
                        count++;
                        queue.add(above);
                    }
                }
            }
            total += count;
        }
        return total;
    }

    @Before
    public void init() throws IOException {
        List<String> contents = Files.readAllLines(Path.of("./src/test/resources/2023/", "day22.input"));
        this.bricks = new ArrayList<>();
        for (String content : contents) {
            if (content.isBlank()) {
                continue;
            }
            String[] ends = content.split("~");
            String[] first = ends[0].split(",");
            String[] second = ends[1].split(",");
            this.bricks.add(new Brick(new int[]{
                    Integer.parseInt(first[0]), Integer.parseInt(first[1]), Integer.parseInt(first[2]),
                    Integer.parseInt(second[0]), Integer.parseInt(second[1]), Integer.parseInt(second[2])}));
        }
        for (Brick brick : this.bricks) {
            this.maxX = Math.max(this.maxX, brick.x2);
            this.maxY = Math.max(this.maxY, brick.y2);
            this.maxZ = Math.max(this.maxZ, brick.z2);
        }
    }

    private static class Brick {
        int x1;
        int y1;
        int z1;
        int x2;
        int y2;
        int z2;

        Brick(int[] coordinates) {
            this.x1 = Math.min(coordinates[0], coordinates[3]);
            this.x2 = Math.max(coordinates[0], coordinates[3]);
            this.y1 = Math.min(coordinates[1], coordinates[4]);
            this.y2 = Math.max(coordinates[1], coordinates[4]);
            this.z1 = Math.min(coordinates[2], coordinates[5]);
            this.z2 = Math.max(coordinates[2], coordinates[5]);
        }
    }
}

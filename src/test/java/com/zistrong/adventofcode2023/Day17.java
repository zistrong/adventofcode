package com.zistrong.adventofcode2023;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Day17 {


    /**
     * The lava starts flowing rapidly once the Lava Production Facility is operational.
     * As you leave, the reindeer offers you a parachute, allowing you to quickly reach Gear Island.
     * <p>
     * As you descend, your bird's-eye view of Gear Island reveals why you had trouble finding anyone on your way up:
     * half of Gear Island is empty, but the half below you is a giant factory city!
     * <p>
     * You land near the gradually-filling pool of lava at the base of your new lavafall.
     * Lavaducts will eventually carry the lava throughout the city, but to make use of it immediately,
     * Elves are loading it into large crucibles on wheels.
     * <p>
     * The crucibles are top-heavy and pushed by hand. Unfortunately, the crucibles become very difficult to steer at high speeds,
     * and so it can be hard to go in a straight line for very long.
     * <p>
     * To get Desert Island the machine parts it needs as soon as possible,
     * you'll need to find the best way to get the crucible from the lava pool to the machine parts factory.
     * To do this, you need to minimize heat loss while choosing a route that doesn't require the crucible to go in a straight line for too long.
     * <p>
     * Fortunately, the Elves here have a map (your puzzle input) that uses traffic patterns, ambient temperature,
     * and hundreds of other parameters to calculate exactly how much heat loss can be expected for a crucible entering any particular city block.
     * <p>
     * For example:
     * <p>
     * 2413432311323
     * 3215453535623
     * 3255245654254
     * 3446585845452
     * 4546657867536
     * 1438598798454
     * 4457876987766
     * 3637877979653
     * 4654967986887
     * 4564679986453
     * 1224686865563
     * 2546548887735
     * 4322674655533
     * Each city block is marked by a single digit that represents the amount of heat loss if the crucible enters that block.
     * The starting point, the lava pool, is the top-left city block; the destination, the machine parts factory,
     * is the bottom-right city block. (Because you already start in the top-left block,
     * you don't incur that block's heat loss unless you leave that block and then return to it.)
     * <p>
     * Because it is difficult to keep the top-heavy crucible going in a straight line for very long,
     * it can move at most three blocks in a single direction before it must turn 90 degrees left or right.
     * The crucible also can't reverse direction; after entering each city block, it may only turn left, continue straight, or turn right.
     * <p>
     * One way to minimize heat loss is this path:
     * <p>
     * 2>>34^>>>1323
     * 32v>>>35v5623
     * 32552456v>>54
     * 3446585845v52
     * 4546657867v>6
     * 14385987984v4
     * 44578769877v6
     * 36378779796v>
     * 465496798688v
     * 456467998645v
     * 12246868655<v
     * 25465488877v5
     * 43226746555v>
     * <p>
     * This path never moves more than three consecutive blocks in the same direction and incurs a heat loss of only 102.
     * <p>
     * Directing the crucible from the lava pool to the machine parts factory, but not moving more than
     * three consecutive blocks in the same direction, what is the least heat loss it can incur?
     */
    @Test
    public void part1() {
        List<Block> s = new ArrayList<>();

        Block source = blockList.get(0).get(0);
        s.add(source);
        source.pathLen = source.heat;
        List<Block> s_v = new ArrayList<>();
        for (List<Block> list : blockList) {
            s_v.addAll(list);
        }
        s_v.removeIf(item -> source.x == item.x && source.y == item.y);

        while (!s_v.isEmpty()) {
            Block min = getMinBlock(s, s_v);
            s.add(min);
            min.visit = true;
            s_v.removeIf(block -> block.y == min.y && block.x == min.x);
            reCompute(s, s_v);
        }


    }

    private Block getMinBlock(List<Block> s, List<Block> s_v) {

        Block min = s_v.get(0);

        for (Block block : s_v) {

            int x = block.x;
            int y = block.y;

            // up
            if (x > 0) {
                Block temp = blockList.get(x - 1).get(y);
                if(s.contains(temp) && !(temp.listType == 'e' && temp.pathList.size() == 3)) {


                }
            }


            // bottom
            if (x < blockList.size()) {

            }


            // left
            if (y > 0) {

            }

            // right
            if (y < blockList.get(0).size()) {

            }

        }


        return min;
    }

    @Test
    public void part2() throws IOException {


    }

    private void reCompute(List<Block> s, List<Block> s_v) {


    }

    List<List<Block>> blockList = new ArrayList<>();


    @Before
    public void init() throws IOException {
        int i = 0;
        for (String item : Files.readAllLines(Path.of("./src/test/resources/2023/", "day17.input"))) {
            List<Block> blocks = new ArrayList<>();
            for (int j = 0; j < item.length(); j++) {
                Block block = new Block();
                block.x = i;
                block.y = j;
                block.heat = Integer.parseInt(String.valueOf(item.charAt(j)));
                blocks.add(block);
            }
            blockList.add(blocks);
            i++;
        }


    }

    private static class Block implements Comparator<Block> {
        int x;
        int y;
        boolean visit;
        int heat;
        int pathLen = Integer.MAX_VALUE;

        List<Block> pathList = new ArrayList<>();

        char listType = 'h';// v or h

        @Override
        public int compare(Block node1, Block node2) {
            return node1.pathLen - node2.pathLen;
        }

        @Override
        public boolean equals(Object obj) {
            Block block = (Block) obj;
            return this.x == block.x && this.y == block.y;
        }
    }

}

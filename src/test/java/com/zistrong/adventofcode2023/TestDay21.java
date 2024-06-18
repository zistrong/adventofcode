package com.zistrong.adventofcode2023;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestDay21 {


    /**
     * You manage to catch the airship right as it's dropping someone else off on their all-expenses-paid trip to Desert Island!
     * It even helpfully drops you off near the gardener and his massive farm.
     * <p>
     * "You got the sand flowing again! Great work! Now we just need to wait until we have enough sand to filter
     * the water for Snow Island and we'll have snow again in no time."
     * <p>
     * While you wait, one of the Elves that works with the gardener heard how good you are at solving problems and would like your help.
     * He needs to get his steps in for the day, and so he'd like to know which garden plots he can reach with exactly his remaining 64 steps.
     * <p>
     * He gives you an up-to-date map (your puzzle input) of his starting position (S), garden plots (.), and rocks (#). For example:
     * <p>
     * ...........
     * .....###.#.
     * .###.##..#.
     * ..#.#...#..
     * ....#.#....
     * .##..S####.
     * .##..#...#.
     * .......##..
     * .##.#.####.
     * .##..##.##.
     * ...........
     * The Elf starts at the starting position (S) which also counts as a garden plot. Then, he can take one step north, south, east, or west,
     * but only onto tiles that are garden plots. This would allow him to reach any of the tiles marked O:
     * <p>
     * ...........
     * .....###.#.
     * .###.##..#.
     * ..#.#...#..
     * ....#O#....
     * .##.OS####.
     * .##..#...#.
     * .......##..
     * .##.#.####.
     * .##..##.##.
     * ...........
     * Then, he takes a second step. Since at this point he could be at either tile marked O, his second step would allow him
     * to reach any garden plot that is one step north, south, east, or west of any tile that he could have reached after the first step:
     * <p>
     * ...........
     * .....###.#.
     * .###.##..#.
     * ..#.#O..#..
     * ....#.#....
     * .##O.O####.
     * .##.O#...#.
     * .......##..
     * .##.#.####.
     * .##..##.##.
     * ...........
     * After two steps, he could be at any of the tiles marked O above, including the starting position
     * (either by going north-then-south or by going west-then-east).
     * <p>
     * A single third step leads to even more possibilities:
     * <p>
     * ...........
     * .....###.#.
     * .###.##..#.
     * ..#.#.O.#..
     * ...O#O#....
     * .##.OS####.
     * .##O.#...#.
     * ....O..##..
     * .##.#.####.
     * .##..##.##.
     * ...........
     * He will continue like this until his steps for the day have been exhausted. After a total of 6 steps,
     * he could reach any of the garden plots marked O:
     * <p>
     * ...........
     * .....###.#.
     * .###.##.O#.
     * .O#O#O.O#..
     * O.O.#.#.O..
     * .##O.O####.
     * .##.O#O..#.
     * .O.O.O.##..
     * .##.#.####.
     * .##O.##.##.
     * ...........
     * In this example, if the Elf's goal was to get exactly 6 more steps today, he could use them to reach any of 16 garden plots.
     * <p>
     * However, the Elf actually needs to get 64 steps today, and the map he's handed you is much larger than the example map.
     * <p>
     * Starting from the garden plot marked S on your map, how many garden plots could the Elf reach in exactly 64 steps?
     */

    Node[][] nodes;
    Set<Node> first = new HashSet<>();


    private void process(Node node, Set<Node> second, int direct) {
        if (dotSet.contains((node.i + direct) + "-" + node.j)) {
            second.add(nodes[node.i + direct][node.j]);
        }
        if (dotSet.contains((node.i) + "-" + (node.j + direct))) {
            second.add(nodes[node.i][node.j + direct]);
        }
    }

    @Test
    public void part1() throws IOException {
        int step = 64;
        Set<Node> second = new HashSet<>();
        while (step-- > 0) {
            for (Node node : first) {
                process(node, second, 1);
                process(node, second, -1);
            }
            first = second;
            second = new HashSet<>();
        }
        Assert.assertEquals(3600, first.size());
    }

    private static final char DOT = '.';

    record Node(int i, int j) {
    }

    Set<String> dotSet = new HashSet<>();

    @Before
    public void init() throws IOException {
        List<String> list = Files.readAllLines(Path.of("./src/test/resources/2023/", "day21.input"));

        nodes = new Node[list.size()][list.get(0).length()];


        for (int i = 0; i < list.size(); i++) {
            String line = list.get(i);
            for (int j = 0; j < line.length(); j++) {
                char c = line.charAt(j);
                Node node = new Node(i, j);
                if (c == 'S') {
                    first.add(node);
                    dotSet.add(node.i + "-" + node.j);
                } else if (c == DOT) {
                    dotSet.add(node.i + "-" + node.j);
                }
                nodes[i][j] = node;
            }
        }
    }

    /**
     * The Elf seems confused by your answer until he realizes his mistake: he was reading from a list of his favorite numbers
     * that are both perfect squares and perfect cubes, not his step counter.
     * <p>
     * The actual number of steps he needs to get today is exactly 26501365.
     * <p>
     * He also points out that the garden plots and rocks are set up so that the map repeats infinitely in every direction.
     * <p>
     * So, if you were to look one additional map-width or map-height out from the edge of the example map above, you would find that it keeps repeating:
     * <p>
     * .................................
     * .....###.#......###.#......###.#.
     * .###.##..#..###.##..#..###.##..#.
     * ..#.#...#....#.#...#....#.#...#..
     * ....#.#........#.#........#.#....
     * .##...####..##...####..##...####.
     * .##..#...#..##..#...#..##..#...#.
     * .......##.........##.........##..
     * .##.#.####..##.#.####..##.#.####.
     * .##..##.##..##..##.##..##..##.##.
     * .................................
     * .................................
     * .....###.#......###.#......###.#.
     * .###.##..#..###.##..#..###.##..#.
     * ..#.#...#....#.#...#....#.#...#..
     * ....#.#........#.#........#.#....
     * .##...####..##..S####..##...####.
     * .##..#...#..##..#...#..##..#...#.
     * .......##.........##.........##..
     * .##.#.####..##.#.####..##.#.####.
     * .##..##.##..##..##.##..##..##.##.
     * .................................
     * .................................
     * .....###.#......###.#......###.#.
     * .###.##..#..###.##..#..###.##..#.
     * ..#.#...#....#.#...#....#.#...#..
     * ....#.#........#.#........#.#....
     * .##...####..##...####..##...####.
     * .##..#...#..##..#...#..##..#...#.
     * .......##.........##.........##..
     * .##.#.####..##.#.####..##.#.####.
     * .##..##.##..##..##.##..##..##.##.
     * .................................
     * This is just a tiny three-map-by-three-map slice of the inexplicably-infinite farm layout; garden plots and rocks repeat as far as you can see.
     * The Elf still starts on the one middle tile marked S, though - every other repeated S is replaced with a normal garden plot (.).
     * <p>
     * Here are the number of reachable garden plots in this new infinite version of the example map for different numbers of steps:
     * <p>
     * In exactly 6 steps, he can still reach 16 garden plots.
     * In exactly 10 steps, he can reach any of 50 garden plots.
     * In exactly 50 steps, he can reach 1594 garden plots.
     * In exactly 100 steps, he can reach 6536 garden plots.
     * In exactly 500 steps, he can reach 167004 garden plots.
     * In exactly 1000 steps, he can reach 668697 garden plots.
     * In exactly 5000 steps, he can reach 16733044 garden plots.
     * However, the step count the Elf needs is much larger! Starting from the garden plot marked S on your infinite map,
     * how many garden plots could the Elf reach in exactly 26501365 steps?
     */
    @Test
    public void part2() {


    }
}

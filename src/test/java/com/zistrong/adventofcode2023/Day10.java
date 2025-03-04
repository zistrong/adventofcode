package com.zistrong.adventofcode2023;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day10 {


    List<List<Node>> nodeList;

    char CHAR_F = 'F';
    char CHAR_J = 'J';
    char CHAR_V = '|';
    char CHAR_H = '-';
    char CHAR_L = 'L';
    char CHAR_7 = '7';
    char CHAR_S = 'S';
    char CHAR_DOT = '.';
    Node sNode;

    @Before
    public void init() throws IOException {
        int startX = 0, startY = 0;
        List<String> contents = Files.readAllLines(Path.of("./src/test/resources/2023/", "day10.input"));
        nodeList = new ArrayList<>();
        int j = 0;
        for (String content : contents) {
            List<Node> list = new ArrayList<>();
            for (int i = 0; i < content.length(); i++) {
                char c = content.charAt(i);
                Node node = new Node();
                node.x = j;
                node.y = i;
                list.add(node);
                node.c = c;
                if (c == CHAR_S) {
                    node.start = true;
                    node.visit = true;
                    startX = j;
                    startY = i;
                } else if (c == CHAR_DOT) {
                    node.point = true;
                } else if (c == CHAR_F) {
                    node.e = true;
                    node.s = true;
                } else if (c == CHAR_J) {
                    node.n = true;
                    node.w = true;
                } else if (c == CHAR_L) {
                    node.n = true;
                    node.e = true;
                } else if (c == CHAR_7) {
                    node.s = true;
                    node.w = true;
                } else if (c == CHAR_V) {
                    node.s = true;
                    node.n = true;
                } else if (c == CHAR_H) {
                    node.w = true;
                    node.e = true;
                }
            }
            nodeList.add(list);
            j++;
        }


        sNode = nodeList.get(startX).get(startY);

    }

    /**
     * You use the hang glider to ride the hot air from Desert Island all the way up to the floating metal island.
     * This island is surprisingly cold and there definitely aren't any thermals to glide on, so you leave your hang glider behind.
     * <p>
     * You wander around for a while, but you don't find any people or animals.
     * However, you do occasionally find signposts labeled "Hot Springs" pointing in a seemingly consistent direction;
     * maybe you can find someone at the hot springs and ask them where the desert-machine parts are made.
     * <p>
     * The landscape here is alien; even the flowers and trees are made of metal. As you stop to admire some metal grass,
     * you notice something metallic scurry away in your peripheral vision and jump into a big pipe!
     * It didn't look like any animal you've ever seen; if you want a better look, you'll need to get ahead of it.
     * <p>
     * Scanning the area, you discover that the entire field you're standing on is densely packed with pipes;
     * it was hard to tell at first because they're the same metallic silver color as the "ground".
     * You make a quick sketch of all of the surface pipes you can see (your puzzle input).
     * <p>
     * The pipes are arranged in a two-dimensional grid of tiles:
     * <p>
     * | is a vertical pipe connecting north and south.
     * - is a horizontal pipe connecting east and west.
     * L is a 90-degree bend connecting north and east.
     * J is a 90-degree bend connecting north and west.
     * 7 is a 90-degree bend connecting south and west.
     * F is a 90-degree bend connecting south and east.
     * . is ground; there is no pipe in this tile.
     * S is the starting position of the animal; there is a pipe on this tile, but your sketch doesn't show what shape the pipe has.
     * Based on the acoustics of the animal's scurrying, you're confident the pipe that contains the animal is one large, continuous loop.
     * <p>
     * For example, here is a square loop of pipe:
     * <p>
     * .....
     * .F-7.
     * .|.|.
     * .L-J.
     * .....
     * If the animal had entered this loop in the northwest corner, the sketch would instead look like this:
     * <p>
     * .....
     * .S-7.
     * .|.|.
     * .L-J.
     * .....
     * In the above diagram, the S tile is still a 90-degree F bend: you can tell because of how the adjacent pipes connect to it.
     * <p>
     * Unfortunately, there are also many pipes that aren't connected to the loop! This sketch shows the same loop as above:
     * <p>
     * -L|F7
     * 7S-7|
     * L|7||
     * -L-J|
     * L|-JF
     * In the above diagram, you can still figure out which pipes form the main loop: they're the ones connected to S,
     * pipes those pipes connect to, pipes those pipes connect to, and so on. Every pipe in the main loop connects to its
     * two neighbors (including S, which will have exactly two pipes connecting to it, and which is assumed to connect back to those two pipes).
     * <p>
     * Here is a sketch that contains a slightly more complex main loop:
     * <p>
     * ..F7.
     * .FJ|.
     * SJ.L7
     * |F--J
     * LJ...
     * Here's the same example sketch with the extra, non-main-loop pipe tiles also shown:
     * <p>
     * 7-F7-
     * .FJ|7
     * SJLL7
     * |F--J
     * LJ.LJ
     * If you want to get out ahead of the animal, you should find the tile in the loop that is farthest from the starting position.
     * Because the animal is in the pipe, it doesn't make sense to measure this by direct distance.
     * Instead, you need to find the tile that would take the longest number of steps along the loop to reach from the starting
     * point - regardless of which way around the loop the animal went.
     * <p>
     * In the first example with the square loop:
     * <p>
     * .....
     * .S-7.
     * .|.|.
     * .L-J.
     * .....
     * You can count the distance each tile in the loop is from the starting point like this:
     * <p>
     * .....
     * .012.
     * .1.3.
     * .234.
     * .....
     * In this example, the farthest point from the start is 4 steps away.
     * <p>
     * Here's the more complex loop again:
     * <p>
     * ..F7.
     * .FJ|.
     * SJ.L7
     * |F--J
     * LJ...
     * Here are the distances for each tile on that loop:
     * <p>
     * ..45.
     * .236.
     * 01.78
     * 14567
     * 23...
     * Find the single giant loop starting at S. How many steps along the loop does it take to get from the starting
     * position to the point farthest from the starting position?
     */
    @Test
    public void part1() throws IOException {


        int maxStep = getMaxPath(getNextNode(sNode));
        Assert.assertEquals(6927, maxStep / 2);
    }

    private int getMaxPath(Node startNode) {
        int step = 1;
        while (startNode != null) {
            step++;
            startNode.visit = true;
            startNode = getNextNode(startNode);
        }
        return step;
    }


    private Node getNextNode(Node sNode) {
        int maxX = nodeList.size();
        // check west
        if (sNode.y != 0) {
            Node current = nodeList.get(sNode.x).get(sNode.y - 1);
            if (!current.point && current.e && (sNode.w || sNode.start) && !current.start && !current.visit) {
                return current;
            }
        }
        // check east node
        if (sNode.y != nodeList.get(sNode.x).size() - 1) {
            Node current = nodeList.get(sNode.x).get(sNode.y + 1);
            if (!current.point && current.w && (sNode.e || sNode.start) && !current.start && !current.visit) {
                return current;
            }
        }

        // check north node
        if (sNode.x != 0) {
            Node current = nodeList.get(sNode.x - 1).get(sNode.y);
            if (!current.point && current.s && (sNode.n || sNode.start) && !current.start && !current.visit) {
                return current;
            }
        }
        // check south node
        if (sNode.x != maxX - 1) {
            Node current = nodeList.get(sNode.x + 1).get(sNode.y);
            if (!current.point && current.n && (sNode.s || sNode.start) && !current.start && !current.visit) {
                return current;
            }
        }
        return null;
    }

    /**
     * You quickly reach the farthest point of the loop, but the animal never emerges. Maybe its nest is within the area enclosed by the loop?
     * <p>
     * To determine whether it's even worth taking the time to search for such a nest, you should calculate how many tiles are contained within the loop. For example:
     * <p>
     * ...........
     * .S-------7.
     * .|F-----7|.
     * .||.....||.
     * .||.....||.
     * .|L-7.F-J|.
     * .|..|.|..|.
     * .L--J.L--J.
     * ...........
     * The above loop encloses merely four tiles - the two pairs of . in the southwest and southeast (marked I below). The middle . tiles (marked O below) are not in the loop. Here is the same loop again with those regions marked:
     * <p>
     * ...........
     * .S-------7.
     * .|F-----7|.
     * .||OOOOO||.
     * .||OOOOO||.
     * .|L-7OF-J|.
     * .|II|O|II|.
     * .L--JOL--J.
     * .....O.....
     * In fact, there doesn't even need to be a full tile path to the outside for tiles to count as outside the loop - squeezing between pipes is also allowed! Here, I is still within the loop and O is still outside the loop:
     * <p>
     * ..........
     * .S------7.
     * .|F----7|.
     * .||OOOO||.
     * .||OOOO||.
     * .|L-7F-J|.
     * .|II||II|.
     * .L--JL--J.
     * ..........
     * In both of the above examples, 4 tiles are enclosed by the loop.
     * <p>
     * Here's a larger example:
     * <p>
     * .F----7F7F7F7F-7....
     * .|F--7||||||||FJ....
     * .||.FJ||||||||L7....
     * FJL7L7LJLJ||LJ.L-7..
     * L--J.L7...LJS7F-7L7.
     * ....F-J..F7FJ|L7L7L7
     * ....L7.F7||L7|.L7L7|
     * .....|FJLJ|FJ|F7|.LJ
     * ....FJL-7.||.||||...
     * ....L---J.LJ.LJLJ...
     * The above sketch has many random bits of ground, some of which are in the loop (I) and some of which are outside it (O):
     * <p>
     * OF----7F7F7F7F-7OOOO
     * O|F--7||||||||FJOOOO
     * O||OFJ||||||||L7OOOO
     * FJL7L7LJLJ||LJIL-7OO
     * L--JOL7IIILJS7F-7L7O
     * OOOOF-JIIF7FJ|L7L7L7
     * OOOOL7IF7||L7|IL7L7|
     * OOOOO|FJLJ|FJ|F7|OLJ
     * OOOOFJL-7O||O||||OOO
     * OOOOL---JOLJOLJLJOOO
     * In this larger example, 8 tiles are enclosed by the loop.
     * <p>
     * Any tile that isn't part of the main loop can count as being enclosed by the loop. Here's another example with many bits of junk pipe lying around that aren't connected to the main loop at all:
     * <p>
     * FF7FSF7F7F7F7F7F---7
     * L|LJ||||||||||||F--J
     * FL-7LJLJ||||||LJL-77
     * F--JF--7||LJLJ7F7FJ-
     * L---JF-JLJ.||-FJLJJ7
     * |F|F-JF---7F7-L7L|7|
     * |FFJF7L7F-JF7|JL---7
     * 7-L-JL7||F7|L7F-7F7|
     * L.L7LFJ|||||FJL7||LJ
     * L7JLJL-JLJLJL--JLJ.L
     * Here are just the tiles that are enclosed by the loop marked with I:
     * <p>
     * FF7FSF7F7F7F7F7F---7
     * L|LJ||||||||||||F--J
     * FL-7LJLJ||||||LJL-77
     * F--JF--7||LJLJIF7FJ-
     * L---JF-JLJIIIIFJLJJ7
     * |F|F-JF---7IIIL7L|7|
     * |FFJF7L7F-JF7IIL---7
     * 7-L-JL7||F7|L7F-7F7|
     * L.L7LFJ|||||FJL7||LJ
     * L7JLJL-JLJLJL--JLJ.L
     * In this last example, 10 tiles are enclosed by the loop.
     * <p>
     * Figure out whether you have time to search for the nest by calculating the area within the loop. How many tiles are enclosed by the loop?
     */
    @Test
    public void part2() throws IOException {

        part1();
        // 原理， 对于单连通的封闭曲线， 某点不在曲线包围的范围内的充分条件是： 对于任意从这个点的射线， 与曲线的交点为偶数（不包含切点）
        // 对于这个问题， 斜向的射线可以解决这个问题， 切点就是拐点，比如7, L, F, J节点
        // 注意S, 需要根据S的方向， 替换成7, L, F, J, 这里只检测左上， 所以只需要替换成L或者7
        if (sNode.x > 0 && sNode.y < nodeList.get(sNode.x).size() - 1
                && nodeList.get(sNode.x - 1).get(sNode.y).s
                && nodeList.get(sNode.x).get(sNode.y + 1).w) {
            sNode.c = CHAR_L;
        }
        if (sNode.y > 0 && sNode.x < nodeList.size() - 1
                && nodeList.get(sNode.x + 1).get(sNode.y).n
                && nodeList.get(sNode.x).get(sNode.y - 1).e) {
            sNode.c = CHAR_7;
        }
        long count = 0L;
        for (List<Node> list : this.nodeList) {
            for (Node node : list) {
                if (!node.visit) {
                    // check left-top
                    int x = node.x;
                    int y = node.y;
                    int c = 0;// count cross point
                    while (x >= 0 && y >= 0) {
                        // 交点
                        if (nodeList.get(x).get(y).visit && !(nodeList.get(x).get(y).c == CHAR_7 || nodeList.get(x).get(y).c == CHAR_L)) {
                            c++;
                        }
                        x--;
                        y--;
                    }
                    if (c % 2 != 0) {
                        count++;
                    }
                }
            }
        }
        Assert.assertEquals(467L, count);
    }

    private static class Node {
        int x;
        int y;
        char c;
        boolean point;
        boolean n;
        boolean s;
        boolean w;
        boolean e;
        boolean start;
        boolean visit;
    }


}

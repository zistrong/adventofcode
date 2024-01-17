package com.zistrong.adventofcode2023;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day18 {

    @Before
    public void init() {

    }


    /**
     * Thanks to your efforts, the machine parts factory is one of the first factories up and running since the lavafall came back.
     * However, to catch up with the large backlog of parts requests, the factory will also need a large supply of lava for a while;
     * the Elves have already started creating a large lagoon nearby for this purpose.
     * <p>
     * However, they aren't sure the lagoon will be big enough; they've asked you to take a look at the dig plan (your puzzle input). For example:
     * <p>
     * R 6 (#70c710)
     * D 5 (#0dc571)
     * L 2 (#5713f0)
     * D 2 (#d2c081)
     * R 2 (#59c680)
     * D 2 (#411b91)
     * L 5 (#8ceee2)
     * U 2 (#caa173)
     * L 1 (#1b58a2)
     * U 2 (#caa171)
     * R 2 (#7807d2)
     * U 3 (#a77fa3)
     * L 2 (#015232)
     * U 2 (#7a21e3)
     * The digger starts in a 1 meter cube hole in the ground. They then dig the specified number of meters up (U), down (D),
     * left (L), or right (R), clearing full 1 meter cubes as they go. The directions are given as seen from above,
     * so if "up" were north, then "right" would be east, and so on. Each trench is also listed with the color that
     * the edge of the trench should be painted as an RGB hexadecimal color code.
     * <p>
     * When viewed from above, the above example dig plan would result in the following loop of trench (#) having been
     * dug out from otherwise ground-level terrain (.):
     * <p>
     * #######
     * #.....#
     * ###...#
     * ..#...#
     * ..#...#
     * ###.###
     * #...#..
     * ##..###
     * .#....#
     * .######
     * <p>
     * #######
     * l.....7
     * #7#...#
     * ..#...#
     * ..#...#
     * ###.###
     * l...#..
     * ##..#l#
     * .l....7
     * .######
     * <p>
     * At this point, the trench could contain 38 cubic meters of lava. However, this is just the edge of the lagoon;
     * the next step is to dig out the interior so that it is one meter deep as well:
     * <p>
     * #######
     * #######
     * #######
     * ..#####
     * ..#####
     * #######
     * #####..
     * #######
     * .######
     * .######
     * Now, the lagoon can contain a much more respectable 62 cubic meters of lava. While the interior is dug out,
     * the edges are also painted according to the color codes in the dig plan.
     * <p>
     * The Elves are concerned the lagoon won't be large enough;
     * if they follow their dig plan, how many cubic meters of lava could it hold?
     */
    @Test
    public void part1() throws IOException {
        calculate(1);
    }
    @Test
    public void part2() throws IOException {
        calculate(2);
    }

    private static class Cube {
        int x;
        int y;
        char c = '.';
        char direct = 'S';// 7
    }

    record Command(char direction, int step, String color) {

    }

    private void calculate(int part) throws IOException {
        List<String> commands = Files.readAllLines(Path.of("./src/test/resources/2023/", "day18.input"));
        List<Command> commandList = new ArrayList<>();
        int u = 0;
        int d = 0;
        int l = 0;
        int r = 0;
        // init command

        for (String command : commands) {
            String[] s = command.split(" ");
            Command command1 = null;
            if (part == 1) {
                command1 = new Command(s[0].charAt(0), Integer.parseInt(s[1]), s[2]);
            } else {
                String color = s[2].replace("(","").replace(")","").replace("#","");
                char c = color.charAt(color.length() - 1);
                char direct = 'U';
                if (c == '0') {
                    direct = 'R';
                } else if (c == '1') {
                    direct = 'D';
                } else if (c == '2') {
                    direct = 'L';
                }
                command1 = new Command(direct, Integer.parseInt(color.substring(0, color.length() - 1), 16), s[2]);
            }
            commandList.add(command1);

            if (command1.direction == 'U') {
                u += command1.step;
            } else if (command1.direction == 'D') {
                d += command1.step;
            } else if (command1.direction == 'L') {
                l += command1.step;
            } else if (command1.direction == 'R') {
                r += command1.step;
            }
        }
        // init cubes
        Cube[][] cubes = new Cube[u + d][r + l];
        for (int i = 0; i < cubes.length; i++) {
            Cube[] cc = cubes[i];
            for (int j = 0; j < cc.length; j++) {
                cubes[i][j] = new Cube();
                cubes[i][j].x = i;
                cubes[i][j].y = j;
            }
        }
        int startX = u - 1;
        int startY = l - 1;
        cubes[startX][startY].c = '#';
        for (Command command : commandList) {
            for (int i = 0; i < command.step; i++) {
                if (command.direction == 'U') {// U - L-->7
                    if (cubes[startX][startY].direct == 'L') {
                        cubes[startX][startY].direct = 'p';
                    }
                    startX--;
                } else if (command.direction == 'D') {// D-R-->L
                    if (cubes[startX][startY].direct == 'R') {
                        cubes[startX][startY].direct = 'p';
                    }
                    startX++;
                } else if (command.direction == 'L') {// L-U-->L
                    if (cubes[startX][startY].direct == 'U') {
                        cubes[startX][startY].direct = 'p';
                    }
                    startY--;
                } else if (command.direction == 'R') {// R-D--7
                    if (cubes[startX][startY].direct == 'D') {
                        cubes[startX][startY].direct = 'p';
                    }
                    startY++;
                }
                cubes[startX][startY].c = '#';
            }
            cubes[startX][startY].direct = command.direction;
        }

        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, maxX = 0, maxY = 0;
        long count = 0L;

        //
        Command firstCommand = commandList.get(0);
        Command lastCommand = commandList.get(commandList.size() - 1);
        if ((lastCommand.direction == 'L' && firstCommand.direction == 'U') || (lastCommand.direction == 'R' && firstCommand.direction == 'D')
                || (lastCommand.direction == 'U' && firstCommand.direction == 'L') ||
                (lastCommand.direction == 'D' && firstCommand.direction == 'R')) {
            cubes[u - 1][l - 1].direct = 'p';
        }

        // shrink cubes
        for (int i = 0; i < cubes.length; i++) {
            Cube[] cc = cubes[i];
            for (int j = 0; j < cc.length; j++) {
                if (cc[j].c != '.') {
                    if (i < minX) {
                        minX = i;
                    }
                    if (j < minY) {
                        minY = j;
                    }
                    if (i > maxX) {
                        maxX = i;
                    }
                    if (j > maxY) {
                        maxY = j;
                    }
                }
            }
        }
        for (int i = minX; i <= maxX; i++) {
            Cube[] cc = cubes[i];
            for (int j = minY; j <= maxY; j++) {
                if (cc[j].c == '#') {
                    count++;
                } else {
                    int tempX = i - 1;
                    int tempY = j - 1;
                    int inCount = 0;
                    while (tempY >= minY && tempX >= minX) {
                        if (cubes[tempX][tempY].c == '#' && cubes[tempX][tempY].direct != 'p') {
                            inCount++;
                        }
                        tempY--;
                        tempX--;
                    }
                    if (inCount % 2 == 1) {// in cubes
                        count++;
                    }
                }
            }
        }
        Assert.assertEquals(39039, count);
    }


}

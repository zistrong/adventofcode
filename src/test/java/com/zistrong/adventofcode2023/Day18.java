package com.zistrong.adventofcode2023;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day18 {


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
     * //
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
     * 10*9-((10+9)*2-4)=
     * =90 - 34=56
     * 10+10+9+9  38
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
        calculate();

    }

    /**
     * The Elves were right to be concerned; the planned lagoon would be much too small.
     * <p>
     * After a few minutes, someone realizes what happened; someone swapped the color and instruction parameters when producing the dig plan.
     * They don't have time to fix the bug; one of them asks if you can extract the correct instructions from the hexadecimal codes.
     * <p>
     * Each hexadecimal code is six hexadecimal digits long. The first five hexadecimal digits encode the distance in meters
     * as a five-digit hexadecimal number. The last hexadecimal digit encodes the direction to dig: 0 means R, 1 means D, 2 means L, and 3 means U.
     * <p>
     * So, in the above example, the hexadecimal codes can be converted into the true instructions:
     * <p>
     * #70c710 = R 461937
     * #0dc571 = D 56407
     * #5713f0 = R 356671
     * #d2c081 = D 863240
     * #59c680 = R 367720
     * #411b91 = D 266681
     * #8ceee2 = L 577262
     * #caa173 = U 829975
     * #1b58a2 = L 112010
     * #caa171 = D 829975
     * #7807d2 = L 491645
     * #a77fa3 = U 686074
     * #015232 = L 5411
     * #7a21e3 = U 500254
     * Digging out this loop and its interior produces a lagoon that can hold an impressive 952408144115 cubic meters of lava.
     * <p>231085831998
     * 952408144115
     * Convert the hexadecimal color codes into the correct instructions; if the Elves follow this new dig plan,
     * how many cubic meters of lava could the lagoon hold?
     */
    @Test
    public void part2() throws IOException {
        List<String> commands = Files.readAllLines(Path.of("./src/test/resources/2023/", "day18.input"));

        long s = 0L;
        long startX = 0L;
        long startY = 0L;
        Command prev = null;
        long S = 0L;

        for (String commandLine : commands) {
            String[] s1 = commandLine.split(" ");
            String color = s1[2].replace("(", "").replace(")", "").replace("#", "");
            char c = color.charAt(color.length() - 1);
            Command command = new Command(c, Integer.parseInt(color.substring(0, color.length() - 1), 16));
            s += command.step;
            // 确定坐标
            command.x = startX;
            command.y = startY;
            switch (command.direction) {
                case '0':
                    startY += command.step;
                    break;
                case '1':
                    startX += command.step;
                    break;
                case '2':
                    startY -= command.step;
                    break;
                case '3':
                    startX -= command.step;
            }
            // shoelace定理 高斯面积公式
            // 对于有n个坐标(xi, yi)围成的多边形，其面积为
            // S = (x1 * y2 + x2 * y3 +..+xn * y1 - y1 * x2 + y2 * x3...+yn * x1) / 2
            // 其中(xi,yi)按照逆时针方向排列，如果顺时针则符号相反
            if (prev != null) {
                S += prev.y * command.x - prev.x * command.y;
            }
            prev = command;
        }
        S = S / 2;

        // 皮克定理
        // 给定顶点坐标均是整点（或正方形格点）的简单多边形，
        // 皮克定理说明了其面积S和内部格点数目n、多边形边界上的格点数目s的关系：
        // S = n + s/2 - 1
        // n=S+1-s/2
        // total = n+s = S + s/2 + 1
        Assert.assertEquals(44644464596918L, S + s / 2 + 1);
    }

    private static class Cube {
        int x;
        int y;
        char c = '.';
        char direct = 'S';// 7
    }

    private static class Command {
        char direction;
        int step;
        long x;
        long y;

        public Command(char direction, int step) {
            this.direction = direction;
            this.step = step;
        }
    }

    private void calculate() throws IOException {
        List<String> commands = Files.readAllLines(Path.of("./src/test/resources/2023/", "day18.input"));
        List<Command> commandList = new ArrayList<>();
        int u = 0;
        int d = 0;
        int l = 0;
        int r = 0;
        // init command

        for (String command : commands) {
            String[] s = command.split(" ");
            Command command1 = new Command(s[0].charAt(0), Integer.parseInt(s[1]));

            commandList.add(command1);

            switch (command1.direction) {
                case 'U':
                    u += command1.step;
                    break;
                case 'D':
                    d += command1.step;
                    break;
                case 'L':
                    l += command1.step;
                    break;
                case 'R':
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

package com.zistrong.adventofcode;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day9 {

    /**
     * This rope bridge creaks as you walk along it. You aren't sure how old it is, or whether it can even support your weight.
     * <p>
     * It seems to support the Elves just fine, though. The bridge spans a gorge which was carved out by the massive river far below you.
     * <p>
     * You step carefully; as you do, the ropes stretch and twist. You decide to distract yourself by modeling rope physics;
     * maybe you can even figure out where not to step.
     * <p>
     * Consider a rope with a knot at each end; these knots mark the head and the tail of the rope. If the head moves
     * far enough away from the tail, the tail is pulled toward the head.
     * <p>
     * Due to nebulous reasoning involving Planck lengths, you should be able to model the positions of the knots on a
     * two-dimensional grid. Then, by following a hypothetical series of motions (your puzzle input) for the head,
     * you can determine how the tail will move.
     * <p>
     * Due to the aforementioned Planck lengths, the rope must be quite short; in fact, the head (H) and tail (T) must
     * always be touching (diagonally adjacent and even overlapping both count as touching):
     * <p>
     * ....
     * .TH.
     * ....
     * <p>
     * ....
     * .H..
     * ..T.
     * ....
     * <p>
     * ...
     * .H. (H covers T)
     * ...
     * <p>
     * If the head is ever two steps directly up, down, left, or right from the tail, the tail must also move one
     * step in that direction so it remains close enough:
     * <p>
     * .....    .....    .....
     * .TH.. -> .T.H. -> ..TH.
     * .....    .....    .....
     * <p>
     * ...    ...    ...
     * .T.    .T.    ...
     * .H. -> ... -> .T.
     * ...    .H.    .H.
     * ...    ...    ...
     * <p>
     * Otherwise, if the head and tail aren't touching and aren't in the same row or column,
     * the tail always moves one step diagonally to keep up:
     * <p>
     * .....    .....    .....
     * .....    ..H..    ..H..
     * ..H.. -> ..... -> ..T..
     * .T...    .T...    .....
     * .....    .....    .....
     * <p>
     * .....    .....    .....
     * .....    .....    .....
     * ..H.. -> ...H. -> ..TH.
     * .T...    .T...    .....
     * .....    .....    .....
     * <p>
     * You just need to work out where the tail goes as the head follows a series of motions.
     * Assume the head and the tail both start at the same position, overlapping.
     * <p>
     * For example:
     * <p>
     * R 4
     * U 4
     * L 3
     * D 1
     * R 4
     * D 1
     * L 5
     * R 2
     * <p>
     * This series of motions moves the head right four steps, then up four steps, then left three steps,
     * then down one step, and so on. After each step, you'll need to update the position of
     * the tail if the step means the head is no longer adjacent to the tail. Visually,
     * these motions occur as follows (s marks the starting position as a reference point):
     * <p>
     * == Initial State ==
     * <p>
     * ......
     * ......
     * ......
     * ......
     * H.....  (H covers T, s)
     * <p>
     * == R 4 ==
     * <p>
     * ......
     * ......
     * ......
     * ......
     * TH....  (T covers s)
     * <p>
     * ......
     * ......
     * ......
     * ......
     * sTH...
     * <p>
     * ......
     * ......
     * ......
     * ......
     * s.TH..
     * <p>
     * ......
     * ......
     * ......
     * ......
     * s..TH.
     * <p>
     * == U 4 ==
     * <p>
     * ......
     * ......
     * ......
     * ....H.
     * s..T..
     * <p>
     * ......
     * ......
     * ....H.
     * ....T.
     * s.....
     * <p>
     * ......
     * ....H.
     * ....T.
     * ......
     * s.....
     * <p>
     * ....H.
     * ....T.
     * ......
     * ......
     * s.....
     * <p>
     * == L 3 ==
     * <p>
     * ...H..
     * ....T.
     * ......
     * ......
     * s.....
     * <p>
     * ..HT..
     * ......
     * ......
     * ......
     * s.....
     * <p>
     * .HT...
     * ......
     * ......
     * ......
     * s.....
     * <p>
     * == D 1 ==
     * <p>
     * ..T...
     * .H....
     * ......
     * ......
     * s.....
     * <p>
     * == R 4 ==
     * <p>
     * ..T...
     * ..H...
     * ......
     * ......
     * s.....
     * <p>
     * ..T...
     * ...H..
     * ......
     * ......
     * s.....
     * <p>
     * ......
     * ...TH.
     * ......
     * ......
     * s.....
     * <p>
     * ......
     * ....TH
     * ......
     * ......
     * s.....
     * <p>
     * == D 1 ==
     * <p>
     * ......
     * ....T.
     * .....H
     * ......
     * s.....
     * <p>
     * == L 5 ==
     * <p>
     * ......
     * ....T.
     * ....H.
     * ......
     * s.....
     * <p>
     * ......
     * ....T.
     * ...H..
     * ......
     * s.....
     * <p>
     * ......
     * ......
     * ..HT..
     * ......
     * s.....
     * <p>
     * ......
     * ......
     * .HT...
     * ......
     * s.....
     * <p>
     * ......
     * ......
     * HT....
     * ......
     * s.....
     * <p>
     * == R 2 ==
     * <p>
     * ......
     * ......
     * .H....  (H covers T)
     * ......
     * s.....
     * <p>
     * ......
     * ......
     * .TH...
     * ......
     * s.....
     * <p>
     * After simulating the rope, you can count up all the positions the tail visited at least once.
     * In this diagram, s again marks the starting position (which the tail also visited) and # marks other positions the tail visited:
     * <p>
     * ..##..
     * ...##.
     * .####.
     * ....#.
     * s###..
     * <p>
     * So, there are 13 positions the tail visited at least once.
     * <p>
     * Simulate your complete hypothetical series of motions.
     * How many positions does the tail of the rope visit at least once?
     */
    @Test
    public void part1() {


        String space = " ";
        Set<String> set = new HashSet<>();
        set.add("0$0");

        int h_x = 0, h_y = 0;
        int t_x = 0, t_y = 0;
        for (String step : this.steps) {
            String direction = step.split(space)[0];
            int stepNumber = Integer.parseInt(step.split(space)[1]);
            for (int i = 0; i < stepNumber; i++) {
                switch (direction) {
                    case "D": {
                        h_x--;
                        break;
                    }
                    case "U": {
                        h_x++;
                        break;
                    }
                    case "L": {
                        h_y--;
                        break;
                    }
                    case "R": {
                        h_y++;
                        break;
                    }
                }


                if (Math.abs(t_y - h_y) == 2) {
                    if (t_x != h_x) {
                        t_x = h_x > t_x ? t_x + 1 : t_x - 1;
                    }
                    t_y = h_y > t_y ? t_y + 1 : t_y - 1;
                    set.add(t_x + "$" + t_y);
                }
                if (Math.abs(t_x - h_x) == 2) {
                    if (t_y != h_y) {
                        t_y = h_y > t_y ? t_y + 1 : t_y - 1;
                    }
                    t_x = h_x > t_x ? t_x + 1 : t_x - 1;
                    set.add(t_x + "$" + t_y);
                }

            }
        }
        Assert.assertEquals(5513, set.size());

    }

    /**
     * --- Part Two ---
     *
     * A rope snaps! Suddenly, the river is getting a lot closer than you remember. The bridge is still there,
     * but some ropes that broke are now whipping toward you as you fall through the air!
     *
     * The ropes are moving too quickly to grab; you only have a few seconds to choose how to arch your body to avoid being hit.
     * Fortunately, your simulation can be extended to support longer ropes.
     *
     * Rather than two knots, you now must simulate a rope consisting of ten knots.
     * One knot is still the head of the rope and moves according to the series of motions.
     * Each knot further down the rope follows the knot in front of it using the same rules as before.
     *
     * Using the same series of motions as the above example, but with the knots marked H, 1, 2, ..., 9, the motions now occur as follows:
     *
     * == Initial State ==
     *
     * ......
     * ......
     * ......
     * ......
     * H.....  (H covers 1, 2, 3, 4, 5, 6, 7, 8, 9, s)
     *
     * == R 4 ==
     *
     * ......
     * ......
     * ......
     * ......
     * 1H....  (1 covers 2, 3, 4, 5, 6, 7, 8, 9, s)
     *
     * ......
     * ......
     * ......
     * ......
     * 21H...  (2 covers 3, 4, 5, 6, 7, 8, 9, s)
     *
     * ......
     * ......
     * ......
     * ......
     * 321H..  (3 covers 4, 5, 6, 7, 8, 9, s)
     *
     * ......
     * ......
     * ......
     * ......
     * 4321H.  (4 covers 5, 6, 7, 8, 9, s)
     *
     * == U 4 ==
     *
     * ......
     * ......
     * ......
     * ....H.
     * 4321..  (4 covers 5, 6, 7, 8, 9, s)
     *
     * ......
     * ......
     * ....H.
     * .4321.
     * 5.....  (5 covers 6, 7, 8, 9, s)
     *
     * ......
     * ....H.
     * ....1.
     * .432..
     * 5.....  (5 covers 6, 7, 8, 9, s)
     *
     * ....H.
     * ....1.
     * ..432.
     * .5....
     * 6.....  (6 covers 7, 8, 9, s)
     *
     * == L 3 ==
     *
     * ...H..
     * ....1.
     * ..432.
     * .5....
     * 6.....  (6 covers 7, 8, 9, s)
     *
     * ..H1..
     * ...2..
     * ..43..
     * .5....
     * 6.....  (6 covers 7, 8, 9, s)
     *
     * .H1...
     * ...2..
     * ..43..
     * .5....
     * 6.....  (6 covers 7, 8, 9, s)
     *
     * == D 1 ==
     *
     * ..1...
     * .H.2..
     * ..43..
     * .5....
     * 6.....  (6 covers 7, 8, 9, s)
     *
     * == R 4 ==
     *
     * ..1...
     * ..H2..
     * ..43..
     * .5....
     * 6.....  (6 covers 7, 8, 9, s)
     *
     * ..1...
     * ...H..  (H covers 2)
     * ..43..
     * .5....
     * 6.....  (6 covers 7, 8, 9, s)
     *
     * ......
     * ...1H.  (1 covers 2)
     * ..43..
     * .5....
     * 6.....  (6 covers 7, 8, 9, s)
     *
     * ......
     * ...21H
     * ..43..
     * .5....
     * 6.....  (6 covers 7, 8, 9, s)
     *
     * == D 1 ==
     *
     * ......
     * ...21.
     * ..43.H
     * .5....
     * 6.....  (6 covers 7, 8, 9, s)
     *
     * == L 5 ==
     *
     * ......
     * ...21.
     * ..43H.
     * .5....
     * 6.....  (6 covers 7, 8, 9, s)
     *
     * ......
     * ...21.
     * ..4H..  (H covers 3)
     * .5....
     * 6.....  (6 covers 7, 8, 9, s)
     *
     * ......
     * ...2..
     * ..H1..  (H covers 4; 1 covers 3)
     * .5....
     * 6.....  (6 covers 7, 8, 9, s)
     *
     * ......
     * ...2..
     * .H13..  (1 covers 4)
     * .5....
     * 6.....  (6 covers 7, 8, 9, s)
     *
     * ......
     * ......
     * H123..  (2 covers 4)
     * .5....
     * 6.....  (6 covers 7, 8, 9, s)
     *
     * == R 2 ==
     *
     * ......
     * ......
     * .H23..  (H covers 1; 2 covers 4)
     * .5....
     * 6.....  (6 covers 7, 8, 9, s)
     *
     * ......
     * ......
     * .1H3..  (H covers 2, 4)
     * .5....
     * 6.....  (6 covers 7, 8, 9, s)
     *
     * Now, you need to keep track of the positions the new tail, 9, visits. In this example, the tail never moves,
     * and so it only visits 1 position. However, be careful: more types of motion are possible than before,
     * so you might want to visually compare your simulated rope to the one above.
     *
     * Here's a larger example:
     *
     * R 5
     * U 8
     * L 8
     * D 3
     * R 17
     * D 10
     * L 25
     * U 20
     *
     * These motions occur as follows (individual steps are not shown):
     *
     * == Initial State ==
     *
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ...........H..............  (H covers 1, 2, 3, 4, 5, 6, 7, 8, 9, s)
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     *
     * == R 5 ==
     *
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ...........54321H.........  (5 covers 6, 7, 8, 9, s)
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     *
     * == U 8 ==
     *
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ................H.........
     * ................1.........
     * ................2.........
     * ................3.........
     * ...............54.........
     * ..............6...........
     * .............7............
     * ............8.............
     * ...........9..............  (9 covers s)
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     *
     * == L 8 ==
     *
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ........H1234.............
     * ............5.............
     * ............6.............
     * ............7.............
     * ............8.............
     * ............9.............
     * ..........................
     * ..........................
     * ...........s..............
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     *
     * == D 3 ==
     *
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * .........2345.............
     * ........1...6.............
     * ........H...7.............
     * ............8.............
     * ............9.............
     * ..........................
     * ..........................
     * ...........s..............
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     *
     * == R 17 ==
     *
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ................987654321H
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ...........s..............
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     *
     * == D 10 ==
     *
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ...........s.........98765
     * .........................4
     * .........................3
     * .........................2
     * .........................1
     * .........................H
     *
     * == L 25 ==
     *
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ...........s..............
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * H123456789................
     *
     * == U 20 ==
     *
     * H.........................
     * 1.........................
     * 2.........................
     * 3.........................
     * 4.........................
     * 5.........................
     * 6.........................
     * 7.........................
     * 8.........................
     * 9.........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ...........s..............
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     *
     * Now, the tail (9) visits 36 positions (including s) at least once:
     *
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * ..........................
     * #.........................
     * #.............###.........
     * #............#...#........
     * .#..........#.....#.......
     * ..#..........#.....#......
     * ...#........#.......#.....
     * ....#......s.........#....
     * .....#..............#.....
     * ......#............#......
     * .......#..........#.......
     * ........#........#........
     * .........########.........
     *
     * Simulate your complete series of motions on a larger rope with ten knots.
     * How many positions does the tail of the rope visit at least once?
     */
    @Test
    public void part2() {

        String space = " ";
        Set<String> set = new HashSet<>();
        set.add("0$0");

        int head_x = 0, head_y = 0;
        Map<Integer, Integer> xs = new HashMap<>();
        Map<Integer, Integer> ys = new HashMap<>();
        for (String step : this.steps) {
            String direction = step.split(space)[0];
            int stepNumber = Integer.parseInt(step.split(space)[1]);
            for (int i = 0; i < stepNumber; i++) {
                switch (direction) {
                    case "D": {
                        head_x--;
                        break;
                    }
                    case "U": {
                        head_x++;
                        break;
                    }
                    case "L": {
                        head_y--;
                        break;
                    }
                    case "R": {
                        head_y++;
                        break;
                    }
                }

                int h_x = head_x;
                int h_y = head_y;
                int t_x = 0;
                int t_y = 0;
                
                for(int j = 0;j < 10;j++) {

                    t_x = xs.getOrDefault(j, 0);
                    t_y = ys.getOrDefault(j, 0);
                    if (Math.abs(t_y - h_y) == 2) {
                        if (t_x != h_x) {
                            t_x = h_x > t_x ? t_x + 1 : t_x - 1;
                        }
                        t_y = h_y > t_y ? t_y + 1 : t_y - 1;
                    }
                    if (Math.abs(t_x - h_x) == 2) {
                        if (t_y != h_y) {
                            t_y = h_y > t_y ? t_y + 1 : t_y - 1;
                        }
                        t_x = h_x > t_x ? t_x + 1 : t_x - 1;
                        
                    }
                    xs.put(j, t_x);
                    ys.put(j, t_y);
                    h_x = t_x;
                    h_y = t_y;
                }
                set.add(t_x + "$" + t_y);

            }
        }
        System.out.println(set.size());
        //Assert.assertEquals(5513, set.size());
    }

    List<String> steps;

    @Before
    public void init() throws IOException {
        this.steps = Files.readAllLines(Path.of("./src/test/resources/", "day9.input"));
    }

}

package com.zistrong.adventofcode2023;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day16 {
    List<List<Node>> nodesList;

    /**
     * With the beam of light completely focused somewhere, the reindeer leads you deeper still into the Lava Production Facility.
     * At some point, you realize that the steel facility walls have been replaced with cave, and the doorways are just cave,
     * and the floor is cave, and you're pretty sure this is actually just a giant cave.
     * <p>
     * Finally, as you approach what must be the heart of the mountain, you see a bright light in a cavern up ahead.
     * There, you discover that the beam of light you so carefully focused is emerging from the cavern wall closest to the facility
     * and pouring all of its energy into a contraption on the opposite side.
     * <p>
     * Upon closer inspection, the contraption appears to be a flat, two-dimensional square grid containing empty space (.),
     * mirrors (/ and \), and splitters (| and -).
     * <p>
     * The contraption is aligned so that most of the beam bounces around the grid,
     * but each tile on the grid converts some of the beam's light into heat to melt the rock in the cavern.
     * <p>
     * You note the layout of the contraption (your puzzle input). For example:
     * <p>
     * .|...\....
     * |.-.\.....
     * .....|-...
     * ........|.
     * ..........
     * .........\
     * ..../.\\..
     * .-.-/..|..
     * .|....-|.\
     * ..//.|....
     * The beam enters in the top-left corner from the left and heading to the right.
     * Then, its behavior depends on what it encounters as it moves:
     * <p>
     * If the beam encounters empty space (.), it continues in the same direction.
     * If the beam encounters a mirror (/ or \), the beam is reflected 90 degrees depending on the angle of the mirror.
     * For instance, a rightward-moving beam that encounters a / mirror would continue upward in the mirror's column,
     * while a rightward-moving beam that encounters a \ mirror would continue downward from the mirror's column.
     * If the beam encounters the pointy end of a splitter (| or -), the beam passes through the splitter as if the splitter were empty space.
     * For instance, a rightward-moving beam that encounters a - splitter would continue in the same direction.
     * If the beam encounters the flat side of a splitter (| or -), the beam is split into two beams going
     * in each of the two directions the splitter's pointy ends are pointing. For instance,
     * a rightward-moving beam that encounters a | splitter would split into two beams:
     * one that continues upward from the splitter's column and one that continues downward from the splitter's column.
     * Beams do not interact with other beams; a tile can have many beams passing through it at the same time.
     * A tile is energized if that tile has at least one beam pass through it, reflect in it, or split in it.
     * <p>
     * In the above example, here is how the beam of light bounces around the contraption:
     * <p>
     * >|<<<\....
     * |v-.\^....
     * .v...|->>>
     * .v...v^.|.
     * .v...v^...
     * .v...v^..\
     * .v../2\\..
     * <->-/vv|..
     * .|<<<2-|.\
     * .v//.|.v..
     * Beams are only shown on empty tiles; arrows indicate the direction of the beams.
     * If a tile contains beams moving in multiple directions, the number of distinct directions is shown instead.
     * Here is the same diagram but instead only showing whether a tile is energized (#) or not (.):
     * <p>
     * ######....
     * .#...#....
     * .#...#####
     * .#...##...
     * .#...##...
     * .#...##...
     * .#..####..
     * ########..
     * .#######..
     * .#...#.#..
     * Ultimately, in this example, 46 tiles become energized.
     * <p>
     * The light isn't energizing enough tiles to produce lava; to debug the contraption,
     * you need to start by analyzing the current situation. With the beam starting in the top-left heading right,
     * how many tiles end up being energized?
     */
    @Test
    public void part1() throws IOException {

        Map<String, Beam> map = new HashMap<>();
        Beam startBeam = new Beam();
        startBeam.direction = 'e';
        startBeam.x = 0;
        startBeam.y = -1;
        startBeam.init = true;
        map.put(startBeam.id, startBeam);
        this.visit(map);
        Assert.assertEquals(8389L, getScore(nodesList));

    }

    @Before
    public void init() throws IOException {
        List<String> list = Files.readAllLines(Path.of("./src/test/resources/2023/", "day16.input"));
        nodesList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String s = list.get(i);
            List<Node> nodeList = new ArrayList<>();
            for (int j = 0; j < s.length(); j++) {
                Node node = new Node();
                node.c = s.charAt(j);
                node.x = i;
                node.y = j;
                nodeList.add(node);
            }
            nodesList.add(nodeList);
        }
    }

    /**
     * As you try to work out what might be wrong, the reindeer tugs on your shirt and leads you to a nearby control panel.
     * There, a collection of buttons lets you align the contraption so that the beam enters from any edge tile and heading away from that edge.
     * (You can choose either of two directions for the beam if it starts on a corner; for instance, if the beam starts in the bottom-right corner,
     * it can start heading either left or upward.)
     * <p>
     * So, the beam could start on any tile in the top row (heading downward), any tile in the bottom row (heading upward),
     * any tile in the leftmost column (heading right), or any tile in the rightmost column (heading left).
     * To produce lava, you need to find the configuration that energizes as many tiles as possible.
     * <p>
     * In the above example, this can be achieved by starting the beam in the fourth tile from the left in the top row:
     * <p>
     * .|<2<\....
     * |v-v\^....
     * .v.v.|->>>
     * .v.v.v^.|.
     * .v.v.v^...
     * .v.v.v^..\
     * .v.v/2\\..
     * <-2-/vv|..
     * .|<<<2-|.\
     * .v//.|.v..
     * Using this configuration, 51 tiles are energized:
     * <p>
     * .#####....
     * .#.#.#....
     * .#.#.#####
     * .#.#.##...
     * .#.#.##...
     * .#.#.##...
     * .#.#####..
     * ########..
     * .#######..
     * .#...#.#..
     * Find the initial beam configuration that energizes the largest number of tiles; how many tiles are energized in that configuration?
     */
    @Test
    public void part2() throws IOException {

        long maxScore = 0L;
        Map<String, Beam> map = new HashMap<>();
        for (int i = 0; i < nodesList.size(); i++) {
            Beam startBeam = new Beam();
            startBeam.direction = 'e';
            startBeam.x = i;
            startBeam.y = -1;
            startBeam.init = true;
            map.put(startBeam.id, startBeam);
            this.visit(map);
            maxScore = Math.max(maxScore, getScore(nodesList));
            this.rest();

            startBeam.direction = 'w';
            startBeam.y = nodesList.get(0).size();
            startBeam.init = true;
            map.put(startBeam.id, startBeam);
            this.visit(map);
            maxScore = Math.max(maxScore, getScore(nodesList));
            this.rest();
        }
        for (int i = 0; i < nodesList.get(0).size(); i++) {
            Beam startBeam = new Beam();
            startBeam.direction = 's';
            startBeam.x = -1;
            startBeam.y = i;
            startBeam.init = true;
            map.put(startBeam.id, startBeam);
            this.visit(map);
            maxScore = Math.max(maxScore, getScore(nodesList));
            this.rest();

            startBeam.direction = 'n';
            startBeam.x = nodesList.size();
            startBeam.init = true;
            map.put(startBeam.id, startBeam);
            this.visit(map);
            maxScore = Math.max(maxScore, getScore(nodesList));
            this.rest();
        }


        Assert.assertEquals(8564L, maxScore);

    }

    private void rest() {
        for (List<Node> nodeList : this.nodesList) {
            for (Node node : nodeList) {
                node.visit = 0;
            }
        }
    }

    private void visit(Map<String, Beam> map) {
        while (!map.isEmpty()) {
            Beam current = map.values().stream().toList().get(0);
            while (true) {
                if (current.direction == 'w') {
                    current.y--;
                } else if (current.direction == 'e') {
                    current.y++;
                } else if (current.direction == 'n') {
                    current.x--;
                } else if (current.direction == 's') {
                    current.x++;
                }
                if (isBlock(current)) {
                    break;
                }
                current.init = false;
                Node node = nodesList.get(current.x).get(current.y);
                node.visit++;


                if ((current.direction == 'e' && node.c == '\\') || (current.direction == 'w' && node.c == '/')) {
                    Beam newBean = current.clone();
                    newBean.direction = 's';
                    map.put(newBean.id, newBean);
                    break;
                }
                if ((current.direction == 'w' && node.c == '\\') || (current.direction == 'e' && node.c == '/')) {
                    Beam newBean = current.clone();
                    newBean.direction = 'n';
                    map.put(newBean.id, newBean);
                    break;
                }
                if ((current.direction == 'n' && node.c == '\\') || (current.direction == 's' && node.c == '/')) {
                    Beam newBean = current.clone();
                    newBean.direction = 'w';
                    map.put(newBean.id, newBean);
                    break;
                }
                if ((current.direction == 'n' && node.c == '/') || (current.direction == 's' && node.c == '\\')) {
                    Beam newBean = current.clone();
                    newBean.direction = 'e';
                    map.put(newBean.id, newBean);
                    break;
                }

                if ((current.direction == 'w' || current.direction == 'e') && node.c == '|') {
                    if (node.visit <= 1) {
                        Beam n = new Beam();
                        n.x = node.x;
                        n.y = node.y;
                        n.direction = 'n';
                        map.put(n.id, n);
                        Beam s = new Beam();
                        s.x = node.x;
                        s.y = node.y;
                        s.direction = 's';
                        map.put(s.id, s);
                    }
                    break;
                }

                if ((current.direction == 's' || current.direction == 'n') && node.c == '-') {
                    if (node.visit <= 1) {
                        Beam w = new Beam();
                        w.x = node.x;
                        w.y = node.y;
                        w.direction = 'w';
                        map.put(w.id, w);
                        Beam e = new Beam();
                        e.x = node.x;
                        e.y = node.y;
                        e.direction = 'e';
                        map.put(e.id, e);
                    }
                    break;
                }
            }
            map.remove(current.id);
        }
    }


    private boolean isBlock(Beam beam) {

        return (beam.x >= nodesList.size() || beam.x < 0 || beam.y >= nodesList.get(beam.x).size() || beam.y < 0) && !beam.init;
    }

    private long getScore(List<List<Node>> list) {
        long score = 0L;
        for (List<Node> nodeList : list) {
            score += nodeList.stream().filter(item -> item.visit > 0).count();
        }
        return score;
    }

    private static class Node {
        int visit;
        char c;
        int x;
        int y;
    }

    private static class Beam implements Cloneable {

        public Beam() {
            this.id = String.valueOf(Math.random());
        }

        char direction;
        int x;
        int y;
        String id;
        boolean init;

        @Override
        public Beam clone() {
            try {
                Beam clone = (Beam) super.clone();
                clone.id = String.valueOf(Math.random());
                return clone;
            } catch (CloneNotSupportedException e) {
                throw new AssertionError();
            }
        }
    }
}

package com.zistrong.adventofcode2023;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day8 {


    /**
     * You're still riding a camel across Desert Island when you spot a sandstorm quickly approaching.
     * When you turn to warn the Elf, she disappears before your eyes! To be fair, she had just finished
     * warning you about ghosts a few minutes ago.
     * <p>
     * One of the camel's pouches is labeled "maps" - sure enough, it's full of documents (your puzzle input) about
     * how to navigate the desert. At least, you're pretty sure that's what they are; one of the documents contains
     * a list of left/right instructions, and the rest of the documents seem to describe some kind of network of labeled nodes.
     * <p>
     * It seems like you're meant to use the left/right instructions to navigate the network. Perhaps if you have
     * the camel follow the same instructions, you can escape the haunted wasteland!
     * <p>
     * After examining the maps for a bit, two nodes stick out: AAA and ZZZ. You feel like AAA is where you are now,
     * and you have to follow the left/right instructions until you reach ZZZ.
     * <p>
     * This format defines each node of the network individually. For example:
     * <p>
     * RL
     * <p>
     * AAA = (BBB, CCC)
     * BBB = (DDD, EEE)
     * CCC = (ZZZ, GGG)
     * DDD = (DDD, DDD)
     * EEE = (EEE, EEE)
     * GGG = (GGG, GGG)
     * ZZZ = (ZZZ, ZZZ)
     * Starting with AAA, you need to look up the next element based on the next left/right instruction in your input.
     * In this example, start with AAA and go right (R) by choosing the right element of AAA, CCC.
     * Then, L means to choose the left element of CCC, ZZZ. By following the left/right instructions, you reach ZZZ in 2 steps.
     * <p>
     * Of course, you might not find ZZZ right away. If you run out of left/right instructions,
     * repeat the whole sequence of instructions as necessary: RL really means RLRLRLRLRLRLRLRL... and so on.
     * For example, here is a situation that takes 6 steps to reach ZZZ:
     * <p>
     * LLR
     * <p>
     * AAA = (BBB, BBB)
     * BBB = (AAA, ZZZ)
     * ZZZ = (ZZZ, ZZZ)
     * Starting at AAA, follow the left/right instructions. How many steps are required to reach ZZZ?
     */
    @Test
    public void part1() throws IOException {
        List<String> contents = Files.readAllLines(Path.of("./src/test/resources/2023/", "day8.input"));

        Map<String, Node> map = new HashMap<>();
        String instructions = contents.get(0);
        for (int i = 2; i < contents.size(); i++) {
            String content = contents.get(i);
            String location = content.split("=")[0].trim();
            if (location.endsWith("A") || location.endsWith("Z")) {
                System.out.println(location);
            }
            String d = content.split("=")[1].trim().replace(" ", "").replace("(", "").replace(")", "");
            Node node = new Node(d.split(",")[0].trim(), d.split(",")[1].trim());
            map.put(location, node);
        }

        String location = "AAA";
        int step = 0;
        int insLength = instructions.length();
        while (!"ZZZ".equals(location)) {
            char instruction = instructions.charAt(step % insLength);
            Node node = map.get(location);
            if (instruction == 'L') {
                location = node.left;
            } else {
                location = node.right;
            }
            step++;
        }
        Assert.assertEquals(22411, step);


    }

    private record Node(String left, String right) {
    }

    /**
     * The sandstorm is upon you and you aren't any closer to escaping the wasteland.
     * You had the camel follow the instructions, but you've barely left your starting position. It's going to take significantly more steps to escape!
     * <p>
     * What if the map isn't for people - what if the map is for ghosts? Are ghosts even bound by the laws of spacetime?
     * Only one way to find out.
     * <p>
     * After examining the maps a bit longer, your attention is drawn to a curious fact:
     * the number of nodes with names ending in A is equal to the number ending in Z! If you were a ghost,
     * you'd probably just start at every node that ends with A and follow all of the paths at the same time until
     * they all simultaneously end up at nodes that end with Z.
     * <p>
     * For example:
     * <p>
     * LR
     * <p>
     * 11A = (11B, XXX)
     * 11B = (XXX, 11Z)
     * 11Z = (11B, XXX)
     * 22A = (22B, XXX)
     * 22B = (22C, 22C)
     * 22C = (22Z, 22Z)
     * 22Z = (22B, 22B)
     * XXX = (XXX, XXX)
     * Here, there are two starting nodes, 11A and 22A (because they both end with A). As you follow each left/right instruction,
     * use that instruction to simultaneously navigate away from both nodes you're currently on.
     * Repeat this process until all of the nodes you're currently on end with Z.
     * (If only some of the nodes you're on end with Z, they act like any other node and you continue as normal.)
     * In this example, you would proceed as follows:
     * <p>
     * Step 0: You are at 11A and 22A.
     * Step 1: You choose all of the left paths, leading you to 11B and 22B.
     * Step 2: You choose all of the right paths, leading you to 11Z and 22C.
     * Step 3: You choose all of the left paths, leading you to 11B and 22Z.
     * Step 4: You choose all of the right paths, leading you to 11Z and 22B.
     * Step 5: You choose all of the left paths, leading you to 11B and 22C.
     * Step 6: You choose all of the right paths, leading you to 11Z and 22Z.
     * So, in this example, you end up entirely on nodes that end in Z after 6 steps.
     * <p>
     * Simultaneously start on every node that ends with A. How many steps does it take before you're only on nodes that end with Z?
     */
    @Test
    public void part2() throws IOException {
        List<String> contents = Files.readAllLines(Path.of("./src/test/resources/2023/", "day8.input"));

        Map<String, Node> map = new HashMap<>();
        String instructions = contents.get(0);
        List<String> startANodes = new ArrayList<>();
        for (int i = 2; i < contents.size(); i++) {
            String content = contents.get(i);
            String location = content.split("=")[0].trim();
            if (location.endsWith("A")) {
                startANodes.add(location);
            }
            String d = content.split("=")[1].trim().replace(" ", "").replace("(", "").replace(")", "");
            Node node = new Node(d.split(",")[0].trim(), d.split(",")[1].trim());
            map.put(location, node);
        }
        int count = startANodes.size();

        String location = "AAA";
        int step = 0;
        int insLength = instructions.length();
        while (startANodes.stream().filter(item -> item.endsWith("Z")).count() != count) {
            char instruction = instructions.charAt(step % insLength);

            List<String> list = new ArrayList<>();
            for (String startANode : startANodes) {
                Node node = map.get(startANode);

                if (instruction == 'L') {
                    location = node.left;
                } else {
                    location = node.right;
                }
                list.add(location);
            }
            startANodes = list;

            step++;
        }
        Assert.assertEquals(22411, step);

    }
}

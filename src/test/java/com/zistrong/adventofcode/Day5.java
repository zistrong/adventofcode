package com.zistrong.adventofcode;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day5 {

    /**
     * The expedition can depart as soon as the final supplies have been unloaded from the ships.
     * Supplies are stored in stacks of marked crates, but because the needed supplies are buried under
     * many other crates, the crates need to be rearranged.
     * <p>
     * The ship has a giant cargo crane capable of moving crates between stacks. To ensure none of the
     * crates get crushed or fall over, the crane operator will rearrange them in a series of carefully-planned steps.
     * After the crates are rearranged, the desired crates will be at the top of each stack.
     * <p>
     * The Elves don't want to interrupt the crane operator during this delicate procedure, but they forgot to ask her
     * which crate will end up where, and they want to be ready to unload them as soon as possible so they can embark.
     * <p>
     * They do, however, have a drawing of the starting stacks of crates and the rearrangement procedure (your puzzle input). For example:
     * <p>
     * [D]
     * [N] [C]
     * [Z] [M] [P]
     * 1   2   3
     * <p>
     * move 1 from 2 to 1
     * move 3 from 1 to 3
     * move 2 from 2 to 1
     * move 1 from 1 to 2
     * <p>
     * In this example, there are three stacks of crates. Stack 1 contains two crates: crate Z is on the bottom,
     * and crate N is on top. Stack 2 contains three crates; from bottom to top, they are crates M, C, and D. Finally, stack 3 contains a single crate, P.
     * <p>
     * Then, the rearrangement procedure is given. In each step of the procedure, a quantity of crates is
     * moved from one stack to a different stack. In the first step of the above rearrangement procedure,
     * one crate is moved from stack 2 to stack 1, resulting in this configuration:
     * <p>
     * [D]
     * [N] [C]
     * [Z] [M] [P]
     * 1   2   3
     * <p>
     * In the second step, three crates are moved from stack 1 to stack 3. Crates are moved one at a time,
     * so the first crate to be moved (D) ends up below the second and third crates:
     * <p>
     * [Z]
     * [N]
     * [C] [D]
     * [M] [P]
     * 1   2   3
     * <p>
     * Then, both crates are moved from stack 2 to stack 1. Again, because crates are moved one at a time, crate C ends up below crate M:
     * <p>
     * [Z]
     * [N]
     * [M]     [D]
     * [C]     [P]
     * 1   2   3
     * <p>
     * Finally, one crate is moved from stack 1 to stack 2:
     * <p>
     * [Z]
     * [N]
     * [D]
     * [C] [M] [P]
     * 1   2   3
     * <p>
     * The Elves just need to know which crate will end up on top of each stack; in this example,
     * the top crates are C in stack 1, M in stack 2, and Z in stack 3,
     * so you should combine these together and give the Elves the message CMZ.
     * <p>
     * After the rearrangement procedure completes, what crate ends up on top of each stack?
     */
    @Test
    public void CrateMover9000() {

        this.processStack();
    }

    /**
     * As you watch the crane operator expertly rearrange the crates, you notice the process isn't following your prediction.
     * <p>
     * Some mud was covering the writing on the side of the crane, and you quickly wipe it away. The crane isn't a CrateMover 9000 - it's a CrateMover 9001.
     * <p>
     * The CrateMover 9001 is notable for many new and exciting features: air conditioning, leather seats, an extra cup holder,
     * and the ability to pick up and move multiple crates at once.
     * <p>
     * Again considering the example above, the crates begin in the same configuration:
     * <p>
     * [D]
     * [N] [C]
     * [Z] [M] [P]
     * 1   2   3
     * <p>
     * Moving a single crate from stack 2 to stack 1 behaves the same as before:
     * <p>
     * [D]
     * [N] [C]
     * [Z] [M] [P]
     * 1   2   3
     * <p>
     * However, the action of moving three crates from stack 1 to stack 3 means that those three moved crates
     * stay in the same order, resulting in this new configuration:
     * <p>
     * [D]
     * [N]
     * [C] [Z]
     * [M] [P]
     * 1   2   3
     * <p>
     * Next, as both crates are moved from stack 2 to stack 1, they retain their order as well:
     * <p>
     * [D]
     * [N]
     * [C]     [Z]
     * [M]     [P]
     * 1   2   3
     * <p>
     * Finally, a single crate is still moved from stack 1 to stack 2, but now it's crate C that gets moved:
     * <p>
     * [D]
     * [N]
     * [Z]
     * [M] [C] [P]
     * 1   2   3
     * <p>
     * In this example, the CrateMover 9001 has put the crates in a totally different order: MCD.
     * <p>
     * Before the rearrangement process finishes, update your simulation so that the Elves know where they should
     * stand to be ready to unload the final supplies. After the rearrangement procedure completes, what crate ends up on top of each stack?
     */
    @Test
    public void CrateMover9001() {
        String regEx = "move (\\d+) from (\\d+) to (\\d+)";

        for (String content : contents) {
            Pattern pat = Pattern.compile(regEx);
            Matcher mat = pat.matcher(content);
            if (mat.find()) {
                int moveQuality = Integer.parseInt(mat.group(1));
                int from = Integer.parseInt(mat.group(2)) - 1;
                int to = Integer.parseInt(mat.group(3)) - 1;
                Stack<Character> sb = new Stack<>();
                for (int i = 0; i < moveQuality; i++) {
                    sb.push(this.stacks.get(from).pop());
                }
                while (!sb.isEmpty()) {
                    this.stacks.get(to).push(sb.pop());
                }
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Stack<Character> stack : this.stacks) {
            stringBuilder.append(stack.peek());
        }
        Assert.assertEquals("JNRSCDWPP", stringBuilder.toString());
    }

    private void processStack() {
        String regEx = "move (\\d+) from (\\d+) to (\\d+)";

        for (String content : contents) {
            Pattern pat = Pattern.compile(regEx);
            Matcher mat = pat.matcher(content);
            if (mat.find()) {
                int moveQuality = Integer.parseInt(mat.group(1));
                int from = Integer.parseInt(mat.group(2)) - 1;
                int to = Integer.parseInt(mat.group(3)) - 1;
                for (int i = 0; i < moveQuality; i++) {
                    this.stacks.get(to).push(this.stacks.get(from).pop());
                }
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Stack<Character> stack : this.stacks) {
            stringBuilder.append(stack.peek());
        }
        Assert.assertEquals("TWSGQHNHL", stringBuilder.toString());
    }

    private void initStackInfo() {


        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j < 9; j++) {
                int index = j * 4 + 1;
                String line = contents.get(i);
                if (index >= line.length()) {
                    continue;
                }
                char c = contents.get(i).charAt(index);
                if (Character.isSpaceChar(c)) {
                    continue;
                }
                this.stacks.get(j).push(c);
            }
        }
    }

    List<Stack<Character>> stacks = new ArrayList<>();
    List<String> contents = new ArrayList<>();

    @Before
    public void init() throws IOException {
        contents = Files.readAllLines(Path.of("./src/test/resources/", "day5.input"));
        for (int i = 0; i < 9; i++) {
            stacks.add(new Stack<>());
        }
        this.initStackInfo();
    }
}

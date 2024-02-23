package com.zistrong.adventofcode2023;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day12 {
    List<String> contents;

    char operational = '.';
    char damaged = '#';

    @Before
    public void init() throws IOException {

        contents = Files.readAllLines(Path.of("./src/test/resources/2023/", "day12.input"));

    }

    /**
     * You finally reach the hot springs! You can see steam rising from secluded areas attached to the primary, ornate building.
     * <p>
     * As you turn to enter, the researcher stops you. "Wait - I thought you were looking for the hot springs, weren't you?"
     * You indicate that this definitely looks like hot springs to you.
     * <p>
     * "Oh, sorry, common mistake! This is actually the onsen! The hot springs are next door."
     * <p>
     * You look in the direction the researcher is pointing and suddenly notice the massive metal helixes towering overhead. "This way!"
     * <p>
     * It only takes you a few more steps to reach the main gate of the massive fenced-off area containing the springs.
     * You go through the gate and into a small administrative building.
     * <p>
     * "Hello! What brings you to the hot springs today? Sorry they're not very hot right now;
     * we're having a lava shortage at the moment." You ask about the missing machine parts for Desert Island.
     * <p>
     * "Oh, all of Gear Island is currently offline! Nothing is being manufactured at the moment,
     * not until we get more lava to heat our forges. And our springs. The springs aren't very springy unless they're hot!"
     * <p>
     * "Say, could you go up and see why the lava stopped flowing? The springs are too cold for normal operation,
     * but we should be able to find one springy enough to launch you up there!"
     * <p>
     * There's just one problem - many of the springs have fallen into disrepair,
     * so they're not actually sure which springs would even be safe to use!
     * Worse yet, their condition records of which springs are damaged (your puzzle input) are also damaged!
     * You'll need to help them repair the damaged records.
     * <p>
     * In the giant field just outside, the springs are arranged into rows. For each row,
     * the condition records show every spring and whether it is operational (.) or damaged (#).
     * This is the part of the condition records that is itself damaged; for some springs, it is simply unknown (?) whether the spring is operational or damaged.
     * <p>
     * However, the engineer that produced the condition records also duplicated some of this information in a different format!
     * After the list of springs for a given row, the size of each contiguous group of damaged springs is listed in
     * the order those groups appear in the row. This list always accounts for every damaged spring, and each number is
     * the entire size of its contiguous group (that is, groups are always separated by at least one operational spring: #### would always be 4, never 2,2).
     * <p>
     * So, condition records with no unknown spring conditions might look like this:
     * <p>
     * #.#.### 1,1,3
     * .#...#....###. 1,1,3
     * .#.###.#.###### 1,3,1,6
     * ####.#...#... 4,1,1
     * #....######..#####. 1,6,5
     * .###.##....# 3,2,1
     * However, the condition records are partially damaged; some of the springs' conditions are actually unknown (?). For example:
     * <p>
     * ???.### 1,1,3
     * .??..??...?##. 1,1,3
     * ?#?#?#?#?#?#?#? 1,3,1,6
     * ????.#...#... 4,1,1
     * ????.######..#####. 1,6,5
     * ?###???????? 3,2,1
     * Equipped with this information, it is your job to figure out how many different arrangements of operational
     * and broken springs fit the given criteria in each row.
     * <p>
     * In the first line (???.### 1,1,3), there is exactly one way separate groups of one, one, and three broken
     * springs (in that order) can appear in that row: the first three unknown springs must be broken, then operational,
     * then broken (#.#), making the whole row #.#.###.
     * <p>
     * The second line is more interesting: .??..??...?##. 1,1,3 could be a total of four different arrangements.
     * The last ? must always be broken (to satisfy the final contiguous group of three broken springs), and each ??
     * must hide exactly one of the two broken springs. (Neither ?? could be both broken springs or they would
     * form a single contiguous group of two; if that were true, the numbers afterward would have been 2,3 instead.) Since each ??
     * can either be #. or .#, there are four possible arrangements of springs.
     * <p>
     * The last line is actually consistent with ten different arrangements! Because the first number is 3,
     * the first and second ? must both be . (if either were #, the first number would have to be 4 or higher). However,
     * the remaining run of unknown spring conditions have many different ways they could hold groups of two and one broken springs:
     * <p>
     * ?###???????? 3,2,1
     * .###.##.#...
     * .###.##..#..
     * .###.##...#.
     * .###.##....#
     * .###..##.#..
     * .###..##..#.
     * .###..##...#
     * .###...##.#.
     * .###...##..#
     * .###....##.#
     * In this example, the number of possible arrangements for each row is:
     * <p>
     * ???.### 1,1,3 - 1 arrangement ^[\\.]*$
     * .??..??...?##. 1,1,3 - 4 arrangements
     * ?#?#?#?#?#?#?#? 1,3,1,6 - 1 arrangement
     * ????.#...#... 4,1,1 - 1 arrangement
     * ????.######..#####. 1,6,5 - 4 arrangements
     * ?###???????? 3,2,1 - 10 arrangements
     * Adding all of the possible arrangement counts together produces a total of 21 arrangements.
     * <p>
     * For each row, count all of the different arrangements of operational and broken springs that meet the given criteria. What is the sum of those counts?
     */
    @Test
    public void part1() {

        int sum = 0;
        for (String content : contents) {
            String s = content.split(" ")[0];

            sum += test2(content.split(" ")[0], content.split(" ")[1]);

        }
        System.out.println(sum);

    }


    public int test2(String condition, String record) {

        String reg = Arrays.stream(record.split(",")).toList().stream()
                .collect(Collectors.joining("}\\.+#{", "^\\.*#{", "}\\.*$"));
        int numbers = Arrays.stream(record.split(",")).mapToInt(Integer::parseInt).sum()
                - (int) condition.chars().filter(item -> item == damaged).count();
        int ss = (int) condition.chars().filter(item -> item == '?').count();
        int size = (int) Math.pow(2, ss);

        int count = 0;
        // 1 是# 0 是 .
        while ((size = size - 1) >= 0) {
            String repalceAll = condition;
            String binary = Integer.toBinaryString(size);
            binary = Stream.generate(() -> "0").limit(ss - binary.length()).collect(Collectors.joining()) + binary;
            if (binary.chars().filter(item -> item == '1').count() != numbers) {
                continue;
            }

            for (int i = 0; i < binary.length(); i++) {
                repalceAll = repalceAll.replaceFirst("\\?",
                        binary.charAt(i) == operational ? String.valueOf(operational) : String.valueOf(damaged));

            }
            if (repalceAll.matches(reg)) {
                count++;
            }
        }
        return count;

    }


}

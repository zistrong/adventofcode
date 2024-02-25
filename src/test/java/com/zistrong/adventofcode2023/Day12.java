package com.zistrong.adventofcode2023;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day12 {
    List<String> contents;

    char operational = '.';
    char damaged = '#';

    String sOperational = String.valueOf(operational);
    String sDamaged = String.valueOf(damaged);

    @Before
    public void init() throws IOException {

        contents = Files.readAllLines(Path.of("./src/test/resources/2023/", "day12.input"));
        executor = Executors.newFixedThreadPool(5);

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
     * ???.### 1,1,3 - 1 arrangement
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
    public void part1() throws InterruptedException, ExecutionException {
        int sum = 0;
        List<Callable<Long>> list = new ArrayList<>();
        for (String content : contents) {
            String[] contents = content.split(" ");
            list.add(() -> computeCount(contents[0], contents[1]));
        }
        List<Future<Long>> futures = executor.invokeAll(list);

        for (Future<Long> future : futures) {
            sum += future.get();
        }
        Assert.assertEquals(7191L, sum);// 2141
    }

    private long countOnes(long n, long numbers) {
        long count = 0;
        while (n != 0) {
            count += n & 1; // 如果n的最低位是1，则n & 1的结果为1，否则为0
            if (count > numbers) {
                return count;
            }
            n >>= 1; // 将n右移一位
        }
        return count;

    }

    public long computeCount(String condition, String record) {

        String[] strings = record.split(",");
        String reg = Arrays.stream(strings).toList().stream()
                .collect(Collectors.joining("}\\.+#{", "^\\.*#{", "}\\.*$"));
        long numbers = Arrays.stream(strings).mapToInt(Integer::parseInt).sum()
                - condition.chars().filter(item -> item == damaged).count();
        long ss = condition.chars().filter(item -> item == '?').count();
        long size = 1L << ss;
        long count = 0L;
        // 1 是# 0 是 .
        Pattern p = Pattern.compile(reg);

        int n = 1 << (numbers - 1);
        n = Math.max(0, n);
        while ((size = size - 1) >= n) {
            if (countOnes(size, numbers) != numbers) {
                continue;
            }
            String binary = Long.toBinaryString(size);
            binary = "0".repeat((int) ss - binary.length()) + binary;
            StringBuilder replaceAll = new StringBuilder(condition);
            int k = 0;
            for (int i = 0; i < binary.length(); i++) {
                k = replaceAll.indexOf("?", k);
                replaceAll.replace(k, k + 1, binary.charAt(i) == '0' ? sOperational : sDamaged);
            }
            Matcher m = p.matcher(replaceAll);
            if (m.matches()) {
                count++;
            }
        }
        return count;

    }

    /**
     * As you look out at the field of springs, you feel like there are way more springs than the condition records list.
     * When you examine the records, you discover that they were actually folded up this whole time!
     * <p>
     * To unfold the records, on each row, replace the list of spring conditions with five copies of itself (separated by ?)
     * and replace the list of contiguous groups of damaged springs with five copies of itself (separated by ,).
     * <p>
     * So, this row:
     * <p>
     * .# 1
     * Would become:
     * <p>
     * .#?.#?.#?.#?.# 1,1,1,1,1
     * ???.### 1,1,3
     * The first line of the above example would become:
     * <p>
     * ???.###????.###????.###????.###????.### 1,1,3,1,1,3,1,1,3,1,1,3,1,1,3
     * In the above example, after unfolding, the number of possible arrangements for some rows is now much larger:
     * <p>
     * ???.### 1,1,3 - 1 arrangement
     * .??..??...?##. 1,1,3 - 16384 arrangements
     * ?#?#?#?#?#?#?#? 1,3,1,6 - 1 arrangement
     * ????.#...#... 4,1,1 - 16 arrangements
     * ????.######..#####. 1,6,5 - 2500 arrangements
     * ?###???????? 3,2,1 - 506250 arrangements
     * After unfolding, adding all of the possible arrangement counts together produces 525152.
     * <p>
     * Unfold your condition records; what is the new sum of possible arrangement counts?
     */
    @Test
    public void part2() throws InterruptedException, ExecutionException {
        int sum = 0;
        List<Callable<Long>> list = new ArrayList<>();
        for (String content : contents) {
            String[] contents = content.split(" ");
            list.add(() -> computeCount(IntStream.range(0, 5).mapToObj(i -> contents[0]).collect(Collectors.joining("?")),
                    IntStream.range(0, 5).mapToObj(i -> contents[1]).collect(Collectors.joining(","))));
        }
        List<Future<Long>> futures = executor.invokeAll(list);

        for (Future<Long> future : futures) {
            sum += future.get();
        }
    }

    ExecutorService executor;

    @Test
    public void part3() {

    }


}

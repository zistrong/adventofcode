package com.zistrong.adventofcode2023;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public class Day5 {

    /**
     * The almanac starts by listing which seeds need to be planted: seeds 79, 14, 55, and 13.
     * <p/>
     * The rest of the almanac contains a list of maps which describe how to convert numbers from a source category into
     * numbers in a destination category. That is, the section that starts with seed-to-soil map: describes how to convert
     * a seed number (the source) to a soil number (the destination). This lets the gardener and his team know which soil
     * to use with which seeds, which water to use with which fertilizer, and so on.
     * <p>
     * Rather than list every source number and its corresponding destination number one by one, the maps describe entire
     * ranges of numbers that can be converted. Each line within a map contains three numbers: the destination range start,
     * the source range start, and the range length.
     * <p>
     * Consider again the example seed-to-soil map:
     * <p>
     * 50 98 2
     * 52 50 48
     * The first line has a destination range start of 50, a source range start of 98, and a range length of 2. This line
     * means that the source range starts at 98 and contains two values: 98 and 99. The destination range is the same length,
     * but it starts at 50, so its two values are 50 and 51. With this information, you know that seed number 98 corresponds
     * to soil number 50 and that seed number 99 corresponds to soil number 51.
     * <p>
     * The second line means that the source range starts at 50 and contains 48 values: 50, 51, ..., 96, 97. This corresponds
     * to a destination range starting at 52 and also containing 48 values: 52, 53, ..., 98, 99. So, seed number 53 corresponds to soil number 55.
     * <p>
     * Any source numbers that aren't mapped correspond to the same destination number. So, seed number 10 corresponds to soil number 10.
     * <p>
     * So, the entire list of seed numbers and their corresponding soil numbers looks like this:
     * <p>
     * seed  soil
     * 0     0
     * 1     1
     * ...   ...
     * 48    48
     * 49    49
     * 50    52
     * 51    53
     * ...   ...
     * 96    98
     * 97    99
     * 98    50
     * 99    51
     * With this map, you can look up the soil number required for each initial seed number:
     * <p>
     * Seed number 79 corresponds to soil number 81.
     * Seed number 14 corresponds to soil number 14.
     * Seed number 55 corresponds to soil number 57.
     * Seed number 13 corresponds to soil number 13.
     * The gardener and his team want to get started as soon as possible, so they'd like to know the closest location
     * that needs a seed. Using these maps, find the lowest location number that corresponds to any of the initial seeds.
     * To do this, you'll need to convert each seed number through other categories until you can find its corresponding location number.
     * In this example, the corresponding types are:
     * <p>
     * Seed 79, soil 81, fertilizer 81, water 81, light 74, temperature 78, humidity 78, location 82.
     * Seed 14, soil 14, fertilizer 53, water 49, light 42, temperature 42, humidity 43, location 43.
     * Seed 55, soil 57, fertilizer 57, water 53, light 46, temperature 82, humidity 82, location 86.
     * Seed 13, soil 13, fertilizer 52, water 41, light 34, temperature 34, humidity 35, location 35.
     * So, the lowest location number in this example is 35.
     */
    @Test
    public void part1() throws IOException {

        long minLocation = Long.MAX_VALUE;
        for (Long seed : this.seeds) {
            minLocation = getMinLocation(seed, minLocation);
        }
        Assert.assertEquals(579439039, minLocation);
    }

    private long getMinLocation(long seed, long minLocation) {
        for (List<Node> list : this.lists) {
            seed = getSeed(seed, list);
        }
        if (seed < minLocation) {
            minLocation = seed;
        }
        return minLocation;
    }

    private static long getSeed(long seed, List<Node> list) {
        for (Node node : list) {
            if (seed >= node.source && seed < node.source + node.range) {
                seed = node.destination + seed - node.source;
                return seed;
            }
        }
        return seed;
    }

    private List<Long> getSeedsList(String src) {
        return Arrays.stream(src.replace("seeds: ", "").split(" ")).map(Long::parseLong).collect(Collectors.toList());
    }

    private List<Long> seeds;

    private List<List<Node>> lists;


    static class Node {
        long destination;
        long source;
        long range;
    }

    private Node getNode(List<Long> list) {
        Node node = new Node();
        node.destination = list.get(0);
        node.source = list.get(1);
        node.range = list.get(2);
        return node;
    }

    @Before
    public void init() throws IOException {
        List<String> contents = Files.readAllLines(Path.of("./src/test/resources/2023/", "day5.input"));
        seeds = getSeedsList(contents.get(0));
        this.lists = new ArrayList<>();
        for (int i = 2; i < contents.size(); i++) {
            String line = contents.get(i);
            if (line == null || line.isEmpty()) {
                continue;
            }
            switch (line) {
                case "seed-to-soil map:", "water-to-light map:", "soil-to-fertilizer map:", "fertilizer-to-water map:",
                        "light-to-temperature map:", "temperature-to-humidity map:", "humidity-to-location map:":
                    this.lists.add(new ArrayList<>());
                    continue;
                default:
            }
            this.lists.get(this.lists.size() - 1).add(getNode(getSeedsList(line)));
        }
    }

    /**
     * Everyone will starve if you only plant such a small number of seeds. Re-reading the almanac, it looks like the seeds:
     * line actually describes ranges of seed numbers.
     * <p>
     * The values on the initial seeds: line come in pairs. Within each pair, the first value is the start of the range
     * and the second value is the length of the range. So, in the first line of the example above:
     * <p>
     * seeds: 79 14 55 13
     * This line describes two ranges of seed numbers to be planted in the garden. The first range starts with seed number
     * 79 and contains 14 values: 79, 80, ..., 91, 92. The second range starts with seed number 55 and contains 13 values: 55, 56, ..., 66, 67.
     * <p>
     * Now, rather than considering four seed numbers, you need to consider a total of 27 seed numbers.
     * <p>
     * In the above example, the lowest location number can be obtained from seed number 82, which corresponds to soil 84,
     * fertilizer 84, water 84, light 77, temperature 45, humidity 46, and location 46. So, the lowest location number is 46.
     * <p>
     * Consider all of the initial seed numbers listed in the ranges on the first line of the almanac. What is the lowest
     * location number that corresponds to any of the initial seed numbers?
     *
     * @throws IOException
     */
    @Test
    public void part2() throws IOException, InterruptedException {
        Map<Integer, Long> map = new ConcurrentHashMap<>();
        CountDownLatch countDownLatch = new CountDownLatch(seeds.size()/2);
        for (int i = 0; i < this.seeds.size(); i = i + 2) {
            final int finalI = i;
            new Thread(() -> {
                long min = Long.MAX_VALUE;
                for (long j = seeds.get(finalI); j < seeds.get(finalI) + seeds.get(finalI + 1); j++) {
                    min = getMinLocation(j, min);
                }
                map.put(finalI, min);
                countDownLatch.countDown();
            }).start();

        }
        countDownLatch.await();
        Assert.assertEquals(7873084L, (long) map.values().stream().min(Long::compareTo).orElse(0L));
    }
}

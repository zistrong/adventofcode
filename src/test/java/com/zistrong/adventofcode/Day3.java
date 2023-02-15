package com.zistrong.adventofcode;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day3 {

    @Before
    public void init() {

    }

    /**
     * Lowercase item types a through z have priorities 1 through 26.
     * Uppercase item types A through Z have priorities 27 through 52.
     */
    @Test
    public void part1() throws IOException {
        int score = 0;
        List<String> contents = Files.readAllLines(Path.of("./src/test/resources/", "day3.input"));

        for (String content : contents) {
            String firstHalf = content.substring(0, content.length() / 2);
            String secondHalf = content.substring(content.length() / 2);
            for (int i = 0; i < firstHalf.length(); i++) {
                char c = firstHalf.charAt(i);
                if (secondHalf.indexOf(c) > -1) {
                    score += (Character.isLowerCase(c) ? c - 96 : c - 64 + 26);//ascii value
                    break;
                }
            }
        }
        System.out.println(score);
    }

    /**
     * As you finish identifying the misplaced items, the Elves come to you with another issue.
     * <p>
     * For safety, the Elves are divided into groups of three. Every Elf carries a badge that identifies their group.
     * For efficiency, within each group of three Elves, the badge is the only item type carried by all three Elves.
     * That is, if a group's badge is item type B, then all three Elves will have item type B somewhere in their rucksack,
     * and at most two of the Elves will be carrying any other item type.
     * <p>
     * The problem is that someone forgot to put this year's updated authenticity sticker on the badges. All of the badges
     * need to be pulled out of the rucksacks so the new authenticity stickers can be attached.
     * <p>
     * Additionally, nobody wrote down which item type corresponds to each group's badges. The only way to tell which item
     * type is the right one is by finding the one item type that is common between all three Elves in each group.
     * <p>
     * Every set of three lines in your list corresponds to a single group, but each group can have a different badge item type.
     * So, in the above example, the first group's rucksacks are the first three lines:
     * <p>
     * vJrwpWtwJgWrhcsFMMfFFhFp
     * jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
     * PmmdzqPrVvPwwTWBwg
     * <p>
     * And the second group's rucksacks are the next three lines:
     * <p>
     * wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
     * ttgJtRGJQctTZtZT
     * CrZsJsPPZsGzwwsLwLmpwMDw
     * <p>
     * In the first group, the only item type that appears in all three rucksacks is lowercase r; this must be their badges.
     * In the second group, their badge item type must be Z.
     * <p>
     * Priorities for these items must still be found to organize the sticker attachment efforts: here, they are 18 (r)
     * for the first group and 52 (Z) for the second group. The sum of these is 70.
     * <p>
     * Find the item type that corresponds to the badges of each three-Elf group. What is the sum of the priorities
     * of those item types?
     *
     */
    @Test
    public void part2() throws IOException {
        int score = 0;
        List<String> contents = Files.readAllLines(Path.of("./src/test/resources/", "day3.input"));
        if (contents.size() % 3 != 0) {
            throw new IllegalStateException();
        }

        for (int i = 0; i < contents.size(); i = i + 3) {
            String first = contents.get(i);
            String second = contents.get(i + 1);
            String third = contents.get(i + 2);
            for (int j = 0, length = first.length(); j < length; j++) {
                char c = first.charAt(j);
                if (second.indexOf(c) > -1 && third.indexOf(c) > -1) {
                    score += (Character.isLowerCase(c) ? c - 96 : c - 64 + 26);//ascii value
                    break;
                }
            }
        }
        System.out.println(score);
    }
}

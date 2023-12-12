package com.zistrong.adventofcode2023;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day4 {


    /**
     * Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
     * Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
     * Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
     * Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
     * Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
     * Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
     * <p>
     * card1 83, 86, 17, 48   -> 1, 1, 2, 4
     * 61, 32 -> 1, 1
     * 21,1  ->1, 1
     * 84-> 1
     */
    @Test
    public void part1() throws IOException {

        List<String> contents = Files.readAllLines(Path.of("./src/test/resources/2023/", "day4.input"));

        int sum = 0;
        for (String content : contents) {
            String line = content.replaceAll("Card ", "").replaceAll("\\d+:", "");
            List<Integer> winNumbers = getNumberList(line.split("\\|")[0]);
            List<Integer> allNumbers = getNumberList(line.split("\\|")[1]);

            int count = 0;
            for (Integer allNumber : allNumbers) {
                if (winNumbers.contains(allNumber)) {
                    sum += subSum(count - 1);
                    count++;
                }
            }
        }
        Assert.assertEquals(21485, sum);

    }

    /**
     * Just as you're about to report your findings to the Elf, one of you realizes that the rules have
     * actually been printed on the back of every card this whole time.
     * There's no such thing as "points". Instead, scratchcards only cause you to win more scratchcards
     * equal to the number of winning numbers you have.
     * Specifically, you win copies of the scratchcards below the winning card equal to the number of matches.
     * So, if card 10 were to have 5 matching numbers, you would win one copy each of cards 11, 12, 13, 14, and 15.
     * Copies of scratchcards are scored like normal scratchcards and have the same card number as the card they copied.
     * So, if you win a copy of card 10 and it has 5 matching numbers, it would then win a copy of the same cards that the
     * original card 10 won: cards 11, 12, 13, 14, and 15. This process repeats until none of the copies cause you to win any more cards.
     * (Cards will never make you copy a card past the end of the table.)
     * This time, the above example goes differently:
     * Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53 4 0 1
     * Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19 2 1 1
     * Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1   3
     * Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83   3
     * Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36   1
     * Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
     * Card 1 has four matching numbers, so you win one copy each of the next four cards: cards 2, 3, 4, and 5.
     * Your original card 2 has two matching numbers, so you win one copy each of cards 3 and 4.
     * Your copy of card 2 also wins one copy each of cards 3 and 4.
     * Your four instances of card 3 (one original and three copies) have two matching numbers, so you win four copies each of cards 4 and 5.
     * Your eight instances of card 4 (one original and seven copies) have one matching number, so you win eight copies of card 5.
     * Your fourteen instances of card 5 (one original and thirteen copies) have no matching numbers and win no more cards.
     * Your one instance of card 6 (one original) has no matching numbers and wins no more cards.
     * Once all of the originals and copies have been processed, you end up with 1 instance of card 1, 2 instances of card 2, 4
     * instances of card 3, 8 instances of card 4, 14 instances of card 5, and 1 instance of card 6. In total, this example pile of
     * scratchcards causes you to ultimately have 30 scratchcards!
     */
    @Test
    public void part2() throws IOException {


        List<String> contents = Files.readAllLines(Path.of("./src/test/resources/2023/", "day4.input"));
        int[] numbers = new int[contents.size()];
        Arrays.fill(numbers, 1);

        int i = 0;
        for (String content : contents) {
            String line = content.replaceAll("Card ", "").replaceAll("\\d+:", "");
            final List<Integer> winNumbers = getNumberList(line.split("\\|")[0]);
            List<Integer> allNumbers = getNumberList(line.split("\\|")[1]);
            long count = allNumbers.stream().filter(winNumbers::contains).count();
            for (int j = i + 1; j < Math.min(i + count + 1, contents.size()); j++) {
                numbers[j] += numbers[i];
            }
            i++;
        }

        int sum = Arrays.stream(numbers).sum();
        Assert.assertEquals(11024379, sum);

    }

    private int subSum(int count) {

        if (count < 0) {
            return 1;
        }

        return (int) Math.pow(2, count);
    }

    private List<Integer> getNumberList(String src) {
        List<Integer> list = new ArrayList<>();
        String regEx = "\\d+";
        Pattern pat = Pattern.compile(regEx);
        Matcher mat = pat.matcher(src);
        while (mat.find()) {
            list.add(Integer.parseInt(mat.group()));
        }
        return list;
    }

}

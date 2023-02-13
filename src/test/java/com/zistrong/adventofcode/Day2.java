package com.zistrong.adventofcode;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day2 {

    /**
     * A Y
     * B X
     * C Z
     * <p>
     * The Elf finishes helping with the tent and sneaks back over to you. "Anyway, the second column says how the round
     * needs to end: X means you need to lose, Y means you need to end the round in a draw, and Z means you need to win. Good luck!"
     * <p>
     * The total score is still calculated in the same way, but now you need to figure out what shape to choose so the round ends as indicated. The example above now goes like this:
     * <p>
     * In the first round, your opponent will choose Rock (A), and you need the round to end in a draw (Y), so you also choose Rock. This gives you a score of 1 + 3 = 4.
     * In the second round, your opponent will choose Paper (B), and you choose Rock so you lose (X) with a score of 1 + 0 = 1.
     * In the third round, you will defeat your opponent's Scissors with Rock for a score of 1 + 6 = 7.
     * <p>
     * Now that you're correctly decrypting the ultra top secret strategy guide, you would get a total score of 12.
     * <p>
     * Following the Elf's instructions for the second column, what would your total score be if everything goes exactly according to your strategy guide?
     *
     * @throws IOException
     */

    Map<String, String> relation = new HashMap<>();
    Map<String, Integer> matchScores = new HashMap<>();
    Map<String, Integer> scores2 = new HashMap<>();

    @Test
    public void part2() throws IOException {

        int score = 0;

        String space = " ";
        List<String> contents = Files.readAllLines(Path.of("./src/test/resources/", "day2.input"));
        for (String content : contents) {
            score += (scores2.get(relation.get(content)) + matchScores.get(content.split(space)[1]));
        }
        System.out.println(score);

    }

    @Before
    public void init() {
        //match score
        matchScores.put("X", 0);// lost
        matchScores.put("Y", 3);// draw
        matchScores.put("Z", 6);// win


        scores2.put("A", 1);// rock
        scores2.put("B", 2);// paper
        scores2.put("C", 3); // Scissors


        relation.put("B X", "A");
        relation.put("B Y", "B");
        relation.put("B Z", "C");
        relation.put("A X", "C");
        relation.put("A Y", "A");
        relation.put("A Z", "B");
        relation.put("C X", "B");
        relation.put("C Y", "C");
        relation.put("C Z", "A");
    }


}

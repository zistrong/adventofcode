package com.zistrong.adventofcode;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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

    private Map<String, Integer> patternScore = new HashMap<>();

    //1 for Rock, 2 for Paper, and 3 for Scissors
    // win 6 draw 3 lost 0
    private void initValue() {

        patternScore.put("A X", 4);//1+3
        patternScore.put("A Y", 8);//2+6
        patternScore.put("A Z", 3);//3+0
        patternScore.put("B X", 1);//1+0
        patternScore.put("B Y", 5);//2+3
        patternScore.put("B Z", 9);//3+6
        patternScore.put("C X", 7);//1+6
        patternScore.put("C Y", 2);//2+0
        patternScore.put("C Z", 6);//3+3

    }

    private List<String> getList() {
        try {
            return Files.readAllLines(Paths.get("./src/test/resources/", "day2.input"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<String>();
    }

    /**
     */
    @Test
    public void part1() {

        this.initValue();
        int totalScore = 0;
        List<String> list = getList();
        for (String list2 : list) {
            totalScore += patternScore.get(list2);
        }
        System.out.println(totalScore);

    }
    
    @Test
    public void part2() throws IOException {

        int score = 0;

        String space = " ";
        List<String> contents = Files.readAllLines(Path.of("./src/test/resources/", "day2.input"));
        for (String content : contents) {
            score += (scores2.get(relation.get(content)) + matchScores.get(content.split(space)[1]));
        }
        Assert.assertEquals(10498, score);

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

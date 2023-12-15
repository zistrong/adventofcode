package com.zistrong.adventofcode2023;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Day7 {


    private static class Node implements Comparable<Node> {

        public Node(String label, int strength, int part) {
            this.strength = strength;
            this.originalLabel = label;

            this.label = label;
            this.part = part;
            if (this.part == 2) {
                this.intLabel();
            }
            this.initType();
        }

        private void intLabel() {
            int[] c = this.originalLabel.chars().filter(item -> item != 'J').distinct().toArray();
            if (c.length == 0) {
                return;
            }
            int maxC = 0;
            long max = 0;
            for (int i : c) {
                long count = this.originalLabel.chars().filter(item -> item == i).count();
                if (count > max) {
                    maxC = i;
                    max = count;
                }
            }
            this.label = originalLabel.replace("J", String.valueOf((char) maxC));
        }

        private void initType() {

            if (isFiveOfKind()) {
                this.type = 7;
            } else if (isFourKind()) {
                this.type = 6;
            } else if (isFullHouse()) {
                this.type = 5;
            } else if (isThreeKind()) {
                this.type = 4;
            } else if (isTowPair()) {
                this.type = 3;
            } else if (isOnePair()) {
                this.type = 2;
            }
        }

        String label;
        String originalLabel;

        int strength;

        int type = 1;

        int part;

        @Override
        public int compareTo(Node o) {
            if (this.type == o.type) {

                for (int i = 0; i < this.originalLabel.length(); i++) {

                    char current = this.originalLabel.charAt(i);
                    char other = o.originalLabel.charAt(i);
                    if (this.part == 2) {
                        if (current == 'J' && other != 'J') {
                            return -1;
                        }
                        if (current != 'J' && other == 'J') {
                            return 1;
                        }
                    }


                    if (Character.isDigit(current) && !Character.isDigit(other)) {
                        return -1;
                    }
                    if (!Character.isDigit(current) && Character.isDigit(other)) {
                        return 1;
                    }
                    if (Character.isDigit(current) && Character.isDigit(other)) {
                        int k = current - other;
                        if (k == 0) {
                            continue;
                        }
                        return k;
                    }
                    if (!Character.isDigit(current) && !Character.isDigit(other)) {

                        int k = getPresentNumber(current) - getPresentNumber(other);
                        if (k == 0) {
                            continue;
                        }
                        return k;
                    }
                }

                return 0;
            } else {
                return this.type - o.type;
            }
        }

        private int getPresentNumber(char c) {

            return switch (c) {
                case 'A' -> 14;
                case 'K' -> 13;
                case 'Q' -> 12;
                case 'J' -> 11;
                case 'T' -> 10;
                default -> 0;
            };
        }

        private boolean isFiveOfKind() {
            return this.label.substring(1).chars().allMatch(item -> item == this.label.charAt(0));
        }

        private boolean isFourKind() {
            return this.label.chars().filter(item -> item == this.label.charAt(0)).count() == 4
                    || this.label.chars().filter(item -> item == this.label.charAt(1)).count() == 4;
        }


        private boolean isFullHouse() {
            int[] chars = this.label.chars().sorted().toArray();
            if (chars.length < 5) {
                return false;
            }
            return (Arrays.stream(chars).filter(item -> item == chars[0]).count() == 2 &&
                    Arrays.stream(chars).filter(item -> item == chars[4]).count() == 3)
                    ||
                    (Arrays.stream(chars).filter(item -> item == chars[0]).count() == 3 &&
                            Arrays.stream(chars).filter(item -> item == chars[4]).count() == 2);

        }

        private boolean isThreeKind() {
            int[] chars = this.label.chars().distinct().toArray();
            if (chars.length != 3) {
                return false;
            }

            return this.label.chars().filter(item -> item == chars[0]).count() == 3 ||
                    this.label.chars().filter(item -> item == chars[1]).count() == 3 ||
                    this.label.chars().filter(item -> item == chars[2]).count() == 3;

        }

        private boolean isTowPair() {

            int[] chars = this.label.chars().distinct().toArray();
            if (chars.length != 3) {
                return false;
            }

            return this.label.chars().filter(item -> item == chars[0]).count() == 1 ||
                    this.label.chars().filter(item -> item == chars[1]).count() == 1 ||
                    this.label.chars().filter(item -> item == chars[2]).count() == 1;
        }

        private boolean isOnePair() {

            return this.label.chars().distinct().toArray().length == 4;
        }

        @Override
        public String toString() {

            return this.label + ": " + this.strength + ": " + this.type;
        }
    }

    @Test
    public void part1() throws IOException {
        List<Node> nodeList = new ArrayList<>();
        List<String> contents = Files.readAllLines(Path.of("./src/test/resources/2023/", "day7.input"));
        for (String content : contents) {
            String[] s = content.split(" ");
            Node node = new Node(s[0], Integer.parseInt(s[1]), 1);
            nodeList.add(node);
        }
        Collections.sort(nodeList);
        long sum = 0;
        int i = 1;
        for (Node node : nodeList) {
            sum += ((long) i++ * node.strength);
        }
        Assert.assertEquals(251058093, sum);
    }

    int part = 1;

    /**
     * To make things a little more interesting, the Elf introduces one additional rule. Now, J cards are jokers -
     * wildcards that can act like whatever card would make the hand the strongest type possible.
     * <p>
     * To balance this, J cards are now the weakest individual cards, weaker even than 2. The other cards stay in the same order: A, K, Q, T, 9, 8, 7, 6, 5, 4, 3, 2, J.
     * <p>
     * J cards can pretend to be whatever card is best for the purpose of determining hand type;
     * for example, QJJQ2 is now considered four of a kind. However, for the purpose of breaking ties between two hands of the same type,
     * J is always treated as J, not the card it's pretending to be: JKKK2 is weaker than QQQQ2 because J is weaker than Q.
     * <p>
     * Now, the above example goes very differently:
     * <p>
     * 32T3K 765
     * T55J5 684
     * KK677 28
     * KTJJT 220
     * QQQJA 483
     * 32T3K is still the only one pair; it doesn't contain any jokers, so its strength doesn't increase.
     * KK677 is now the only two pair, making it the second-weakest hand.
     * T55J5, KTJJT, and QQQJA are now all four of a kind! T55J5 gets rank 3, QQQJA gets rank 4, and KTJJT gets rank 5.
     * With the new joker rule, the total winnings in this example are 5905.
     */
    @Test
    public void part2() throws IOException {
        part = 2;
        List<Node> nodeList = new ArrayList<>();
        List<String> contents = Files.readAllLines(Path.of("./src/test/resources/2023/", "day7.input"));
        for (String content : contents) {
            String[] s = content.split(" ");
            Node node = new Node(s[0], Integer.parseInt(s[1]), 2);
            nodeList.add(node);
        }
        Collections.sort(nodeList);
        long sum = 0;
        int i = 1;
        for (Node node : nodeList) {
            sum += ((long) i++ * node.strength);
        }
        Assert.assertEquals(249781879, sum);
    }


}

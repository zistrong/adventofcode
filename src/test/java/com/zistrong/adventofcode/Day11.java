package com.zistrong.adventofcode;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Day11 {

    @Test
    public void part1() {

        int round = 20;// rounds
        for (int i = 0; i < round; i++) {
            for (Monkey monkey : monkeys) {
                while (!monkey.getWorriies().isEmpty()) {// no item
                    if (monkey.inspect()) {
                        monkeys.get(monkey.getTarget()).push(monkey.getPopWorry());
                    }
                }
            }
        }

        this.monkeys.sort(Comparator.comparingInt(Monkey::getInspectTimes).reversed());

        Assert.assertEquals(monkeys.get(0).getInspectTimes() * monkeys.get(1).getInspectTimes(), 111210);
    }

    @Before
    public void init() throws IOException {
        List<String> steps = Files.readAllLines(Path.of("./src/test/resources/", "day11.input"));

        monkeys = new ArrayList<>();
        for (int i = 0; i < steps.size(); i = i + 7) {
            Monkey monkey = new Monkey();
            String worryLine = steps.get(i + 1);
            for (String s : worryLine.split(":")[1].trim().split(",")) {
                monkey.getWorriies().offer(Integer.parseInt(s.trim()));
            }
            String opLine = steps.get(i + 2);
            monkey.setOperation(opLine.split(":")[1].replace("new = ", "").trim());
            monkey.setDividedNumber(Integer.parseInt(steps.get(i + 3).replace("Test: divisible by ", "").trim()));
            monkey.setPositiveTarget(
                    Integer.parseInt(steps.get(i + 4).replace("If true: throw to monkey ", "").trim()));
            monkey.setNegativeTarget(
                    Integer.parseInt(steps.get(i + 5).replace("If false: throw to monkey ", "").trim()));
            monkeys.add(monkey);
        }
    }

    List<Monkey> monkeys;
}

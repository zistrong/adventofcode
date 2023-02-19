package com.zistrong.adventofcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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

        Collections.sort(this.monkeys, (t1, t2) -> {
            return t1.getInspectTimes() <= t2.getInspectTimes() ? 1 : -1;
        });

        Assert.assertEquals(monkeys.get(0).getInspectTimes() * monkeys.get(1).getInspectTimes(), 111210);
    }

    @Before
    public void init() throws IOException {
        this.steps = Files.readAllLines(Path.of("./src/test/resources/", "day11.input"));

        monkeys = new ArrayList<>();
        for (int i = 0; i < steps.size(); i = i + 7) {
            Monkey monkey = new Monkey();
            String worryLine = steps.get(i + 1);
            for (String s : worryLine.split(":")[1].trim().split(",")) {
                monkey.getWorriies().offer(Integer.parseInt(s.trim()));
            }
            String opLine = steps.get(i + 2);
            monkey.setId(monkeys.size());
            monkey.setOperation(opLine.split(":")[1].replace("new = ", "").trim());
            monkey.setDivivedNumber(Integer.parseInt(steps.get(i + 3).replace("Test: divisible by ", "").trim()));
            monkey.setPositiveTarget(
                    Integer.parseInt(steps.get(i + 4).replace("If true: throw to monkey ", "").trim()));
            monkey.setNegtiveTarget(
                    Integer.parseInt(steps.get(i + 5).replace("If false: throw to monkey ", "").trim()));
            monkeys.add(monkey);
        }
    }

    private List<String> steps;
    List<Monkey> monkeys;
}

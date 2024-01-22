package com.zistrong.adventofcode2023;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day19 {


    @Test
    public void part1() throws IOException {
        List<String> list = Files.readAllLines(Path.of("./src/test/resources/2023/", "day19.input"));
        Map<String, String[]> workFlows = new HashMap<>();
        List<Map<String, Integer>> rates = new ArrayList<>();
        boolean israte = false;

        for (String string : list) {

            if (string.isBlank()) {
                israte = true;
                continue;
            }
            if (israte) {
                String[] args = string.replace("{", "").replace("}", "").split(",");
                Map<String, Integer> rate = new HashMap<>();
                for (String arg : args) {

                    rate.put(String.valueOf(arg.charAt(0)), Integer.parseInt(arg.substring(2)));
                }
                rates.add(rate);
            } else {
                String name = string.split("\\{")[0];
                String[] commands = string.split("\\{")[1].replace("}", "").split(",");
                workFlows.put(name, commands);
            }
        }
        long sum = 0L;
        for (Map<String, Integer> item : rates) {
            String result = "";
            String[] currentFlow = workFlows.get("in");
            while (!result.equals("A") && !result.equals("R")) {
                result = getTarget(currentFlow, item);
                currentFlow = workFlows.get(result);
            }
            if (result.equals("A")) {
                sum += item.values().stream().mapToInt(i -> i).sum();
            }
        }

        Assert.assertEquals(420739L, sum);

    }

    private String getTarget(String[] conditions, Map<String, Integer> rate) {
        for (String command : conditions) {
            if (!command.contains(":")) {
                return command;
            } else {
                String co = command.split(":")[0];
                String re = command.split(":")[1];
                String key = String.valueOf(co.charAt(0));
                char c = co.charAt(1);
                if (c == '>' && rate.get(key) > Integer.parseInt(co.substring(2))
                        || (c == '<' && rate.get(key) < Integer.parseInt(co.substring(2)))) {
                    return re;
                }
            }
        }
        return "R";
    }

    @Test
    public void part2() {

    }

}

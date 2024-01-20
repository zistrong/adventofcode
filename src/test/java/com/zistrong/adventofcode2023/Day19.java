package com.zistrong.adventofcode2023;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class Day19 {


    @Test
    public void part1() throws IOException {
        List<String> list = Files.readAllLines(Path.of("./src/test/resources/2023/", "day19.input"));
        Map<String, WorkFlow> workFlows = new HashMap<>();

        List<Rate> rates = new ArrayList<>();

        boolean israte = false;

        for (String string : list) {
            
            if(string.isBlank()) {
                israte = true;
                continue;
            }
            if(israte) {
                String[] args  = string.replace("{", "").replace("}", "").split(",");
                Rate rate = new Rate();
                for(String arg : args) {
                    if(arg.startsWith("x")) {
                        rate.x = Integer.parseInt(arg.replace("x=", ""));
                    } else if(arg.startsWith("m")) {
                        rate.m = Integer.parseInt(arg.replace("m=", ""));
                    }else if(arg.startsWith("a")) {
                        rate.a = Integer.parseInt(arg.replace("a=", ""));
                    }else if(arg.startsWith("s")) {
                        rate.s = Integer.parseInt(arg.replace("s=", ""));
                    }
                }
                rates.add(rate);
            } else {
                String name = string.split("\\{")[0];
                WorkFlow workFlow = new WorkFlow();
                String[] commands = string.split("\\{")[1].replace("}", "").split(",");
                for(String command : commands) {
                    workFlow.conditions.add(command);
                }
                workFlows.put(name, workFlow);
            }
        }
        long count = 0L;
        
        WorkFlow currentFlow = workFlows.get("in");
        for(Rate item : rates) {
            String result = "";
            while (!result.equals("A") && !result.equals("R")) {
                result = getTarget(currentFlow, item);
                currentFlow = workFlows.get(result);
            }
            if(result.equals("A")) {
                count += item.getSum();
            }
            currentFlow = workFlows.get("in");
        }

        Assert.assertEquals(420739L, count);

    }

    private String getTarget(WorkFlow currentFlow, Rate rate) {

        for(String command : currentFlow.conditions) {
            if(!command.contains(":")) {
                return command;
            } else {
                String co = command.split(":")[0];
                String re = command.split(":")[1];
                if(co.startsWith("x>") && rate.x> Integer.parseInt(co.replace("x>", "")) ) {
                    return re;
                } else if (co.startsWith("x<")&& rate.x< Integer.parseInt(co.replace("x<", ""))) {
                    return re;

                }else if (co.startsWith("m>")&& rate.m> Integer.parseInt(co.replace("m>", ""))) {
                    return re;

                }else if (co.startsWith("m<")&& rate.m< Integer.parseInt(co.replace("m<", ""))) {
                    return re;

                }else if (co.startsWith("a>")&& rate.a> Integer.parseInt(co.replace("a>", ""))) {
                    return re;

                }else if (co.startsWith("a<")&& rate.a< Integer.parseInt(co.replace("a<", ""))) {
                    return re;

                }else if (co.startsWith("s>")&& rate.s> Integer.parseInt(co.replace("s>", ""))) {
                    return re;

                }else if (co.startsWith("s<")&& rate.s< Integer.parseInt(co.replace("s<", ""))) {
                    return re;

                }
            }
        }
        return "R";
    }

    private static class Rate {
        int x;
        int m;
        int a;
        int s;

        public long getSum() {
            return x + m + a +s+0L;
        }
    }

    private static class WorkFlow {
        List<String> conditions = new ArrayList<>();
    }
    
}

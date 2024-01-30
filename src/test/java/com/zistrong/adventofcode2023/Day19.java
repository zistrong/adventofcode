package com.zistrong.adventofcode2023;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day19 {


    /**
     * The Elves of Gear Island are thankful for your help and send you on your way.
     * They even have a hang glider that someone stole from Desert Island; s
     * ince you're already going that direction, it would help them a lot if you would use it to get down there and return it to them.
     * <p>
     * As you reach the bottom of the relentless avalanche of machine parts,
     * you discover that they're already forming a formidable heap. Don't worry,
     * though - a group of Elves is already here organizing the parts, and they have a system.
     * <p>
     * To start, each part is rated in each of four categories:
     * <p>
     * x: Extremely cool looking
     * m: Musical (it makes a noise when you hit it)
     * a: Aerodynamic
     * s: Shiny
     * Then, each part is sent through a series of workflows that will ultimately accept or reject the part.
     * Each workflow has a name and contains a list of rules; each rule specifies a condition and where to send
     * the part if the condition is true. The first rule that matches the part being considered is applied immediately,
     * and the part moves on to the destination described by the rule.
     * (The last rule in each workflow has no condition and always applies if reached.)
     * <p>
     * Consider the workflow ex{x>10:one,m<20:two,a>30:R,A}. This workflow is named ex and contains four rules.
     * If workflow ex were considering a specific part, it would perform the following steps in order:
     * <p>
     * Rule "x>10:one": If the part's x is more than 10, send the part to the workflow named one.
     * Rule "m<20:two": Otherwise, if the part's m is less than 20, send the part to the workflow named two.
     * Rule "a>30:R": Otherwise, if the part's a is more than 30, the part is immediately rejected (R).
     * Rule "A": Otherwise, because no other rules matched the part, the part is immediately accepted (A).
     * If a part is sent to another workflow, it immediately switches to the start of that workflow instead and never returns.
     * If a part is accepted (sent to A) or rejected (sent to R), the part immediately stops any further processing.
     * <p>
     * The system works, but it's not keeping up with the torrent of weird metal shapes.
     * The Elves ask if you can help sort a few parts and give you the list of workflows and some part ratings (your puzzle input). For example:
     * <p>
     * px{a<2006:qkq,m>2090:A,rfg}
     * pv{a>1716:R,A}
     * lnx{m>1548:A,A}
     * rfg{s<537:gd,x>2440:R,A}
     * qs{s>3448:A,lnx}
     * qkq{x<1416:A,crn}
     * crn{x>2662:A,R}
     * in{s<1351:px,qqz}
     * qqz{s>2770:qs,m<1801:hdj,R}
     * gd{a>3333:R,R}
     * hdj{m>838:A,pv}
     * <p>
     * {x=787,m=2655,a=1222,s=2876}
     * {x=1679,m=44,a=2067,s=496}
     * {x=2036,m=264,a=79,s=2244}
     * {x=2461,m=1339,a=466,s=291}
     * {x=2127,m=1623,a=2188,s=1013}
     * The workflows are listed first, followed by a blank line, then the ratings of the parts the Elves would like you to sort.
     * All parts begin in the workflow named in. In this example, the five listed parts go through the following workflows:
     * <p>
     * {x=787,m=2655,a=1222,s=2876}: in -> qqz -> qs -> lnx -> A
     * {x=1679,m=44,a=2067,s=496}: in -> px -> rfg -> gd -> R
     * {x=2036,m=264,a=79,s=2244}: in -> qqz -> hdj -> pv -> A
     * {x=2461,m=1339,a=466,s=291}: in -> px -> qkq -> crn -> R
     * {x=2127,m=1623,a=2188,s=1013}: in -> px -> rfg -> A
     * Ultimately, three parts are accepted. Adding up the x, m, a, and s rating for each of the accepted parts gives 7540 for the part with
     * x=787, 4623 for the part with x=2036, and 6951 for the part with x=2127.
     * Adding all of the ratings for all of the accepted parts gives the sum total of 19114.
     */
    @Test
    public void part1() throws IOException {
        List<String> list = Files.readAllLines(Path.of("./src/test/resources/2023/", "day19.input"));
        Map<String, String[]> workFlows = new HashMap<>();
        List<Map<String, Long>> rates = new ArrayList<>();
        boolean israte = false;
        for (String string : list) {

            if (string.isBlank()) {
                israte = true;
                continue;
            }
            if (israte) {
                String[] args = string.replace("{", "").replace("}", "").split(",");
                Map<String, Long> rate = new HashMap<>();
                for (String arg : args) {

                    rate.put(String.valueOf(arg.charAt(0)), Long.parseLong(arg.substring(2)));
                }
                rates.add(rate);
            } else {
                String name = string.split("\\{")[0];
                String[] commands = string.split("\\{")[1].replace("}", "").split(",");
                workFlows.put(name, commands);
            }
        }
        long sum = 0L;
        for (Map<String, Long> item : rates) {
            String result = "";
            String[] currentFlow = workFlows.get("in");
            while (!result.equals("A") && !result.equals("R")) {
                result = getTarget(currentFlow, item);
                currentFlow = workFlows.get(result);
            }
            if (result.equals("A")) {
                sum += item.values().stream().mapToLong(i -> i).sum();
            }
        }

        Assert.assertEquals(420739L, sum);

    }

    private String getTarget(String[] conditions, Map<String, Long> rate) {
        for (String command : conditions) {
            if (!command.contains(":")) {
                return command;
            } else {
                String co = command.split(":")[0];
                String key = String.valueOf(co.charAt(0));
                char c = co.charAt(1);
                if (c == '>' && rate.get(key) > Integer.parseInt(co.substring(2))
                        || (c == '<' && rate.get(key) < Integer.parseInt(co.substring(2)))) {
                    return command.split(":")[1];
                }
            }
        }
        return "R";
    }

    /**
     * Even with your help, the sorting process still isn't fast enough.
     * <p>
     * One of the Elves comes up with a new plan: rather than sort parts individually through all of these workflows,
     * maybe you can figure out in advance which combinations of ratings will be accepted or rejected.
     * <p>
     * Each of the four ratings (x, m, a, s) can have an integer value ranging from a minimum of 1 to a maximum of 4000.
     * Of all possible distinct combinations of ratings, your job is to figure out which ones will be accepted.
     * <p>
     * In the above example, there are 167409079868000 distinct combinations of ratings that will be accepted.
     * <p>                             165932848968860
     * Consider only your list of workflows; the list of part ratings that the Elves wanted you to sort is no longer relevant.
     * How many distinct combinations of ratings will be accepted by the Elves' workflows?
     */
    @Test
    public void part2() throws IOException {
        List<String> list = Files.readAllLines(Path.of("./src/test/resources/2023/", "day19.input"));
        Map<String, List<Long>> workFlowsRange = new HashMap<>();
        List<Map<String, Integer>> rates = new ArrayList<>();
        Map<String, String[]> workFlows = new HashMap<>();

        for (String string : list) {

            if (string.isBlank()) {
                break;
            }
            String name = string.split("\\{")[0];
            String[] commands = string.split("\\{")[1].replace("}", "").split(",");
            workFlows.put(name, commands);

            for (String arg : commands) {
                if (arg.contains(":")) {
                    String co = arg.split(":")[0];
                    String current = String.valueOf(co.charAt(0));
                    long v = Long.parseLong(co.substring(2));
                    workFlowsRange.computeIfAbsent(current, m -> new ArrayList<>()).add(v);
                    //workFlowsRange.computeIfAbsent(current, m -> new ArrayList<>()).add(v-1);
                    //workFlowsRange.computeIfAbsent(current, m -> new ArrayList<>()).add(v+1);



                }
            }
        }
        for (List<Long> value : workFlowsRange.values()) {
            value.add(1L);
            value.add(4000L);
            Collections.sort(value);
        }
        long sum = 0L;
        for (int i = 0; i < workFlowsRange.get("x").size() - 1; i++) {
            for (int i1 = 0; i1 < workFlowsRange.get("a").size() - 1; i1++) {
                for (int i2 = 0; i2 < workFlowsRange.get("m").size() - 1; i2++) {
                    for (int i3 = 0; i3 < workFlowsRange.get("s").size() - 1; i3++) {
                        Map<String, Long> rate = new HashMap<>();
                        long x = workFlowsRange.get("x").get(i);
                        long a = workFlowsRange.get("a").get(i1);
                        long m = workFlowsRange.get("m").get(i2);
                        long s = workFlowsRange.get("s").get(i3);
                        rate.put("x", x + 1);
                        rate.put("a", a + 1);
                        rate.put("m", m + 1);
                        rate.put("s", s + 1);

                        String result = "";
                        String[] currentFlow = workFlows.get("in");
                        while (!result.equals("A") && !result.equals("R")) {
                            result = getTarget(currentFlow, rate);
                            currentFlow = workFlows.get(result);
                        }
                        if (result.equals("A")) {
                            sum += (workFlowsRange.get("x").get(i + 1) - x-1) * (workFlowsRange.get("a").get(i1 + 1) - a-1) * (workFlowsRange.get("m").get(i2 + 1) - m-1) * (workFlowsRange.get("s").get(i3 + 1) - s-1);
                        }
                    }
                }
            }
        }


        System.out.println(sum);

    }

}

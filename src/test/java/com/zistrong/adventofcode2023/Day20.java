package com.zistrong.adventofcode2023;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day20 {


    /**
     * With your help, the Elves manage to find the right parts and fix all of the machines.
     * Now, they just need to send the command to boot up the machines and get the sand flowing again.
     * <p>
     * The machines are far apart and wired together with long cables. The cables don't connect to the machines directly,
     * but rather to communication modules attached to the machines that perform various initialization tasks and also act as communication relays.
     * <p>
     * Modules communicate using pulses. Each pulse is either a high pulse or a low pulse. When a module sends a pulse,
     * it sends that type of pulse to each module in its list of destination modules.
     * <p>
     * There are several different types of modules:
     * <p>
     * Flip-flop modules (prefix %) are either on or off; they are initially off. If a flip-flop module receives a high pulse,
     * it is ignored and nothing happens. However, if a flip-flop module receives a low pulse, it flips between on and off.
     * If it was off, it turns on and sends a high pulse. If it was on, it turns off and sends a low pulse.
     * <p>
     * Conjunction modules (prefix &) remember the type of the most recent pulse received from each of their connected input modules;
     * they initially default to remembering a low pulse for each input. When a pulse is received,
     * the conjunction module first updates its memory for that input. Then, if it remembers high pulses for all inputs,
     * it sends a low pulse; otherwise, it sends a high pulse.
     * <p>
     * There is a single broadcast module (named broadcaster). When it receives a pulse, it sends the same pulse to all of its destination modules.
     * <p>
     * Here at Desert Machine Headquarters, there is a module with a single button on it called, aptly, the button module. When you push the button,
     * a single low pulse is sent directly to the broadcaster module.
     * <p>
     * After pushing the button, you must wait until all pulses have been delivered and fully handled before pushing it again.
     * Never push the button if modules are still processing pulses.
     * <p>
     * Pulses are always processed in the order they are sent. So, if a pulse is sent to modules a, b, and c,
     * and then module a processes its pulse and sends more pulses, the pulses sent to modules b and c would have to be handled first.
     * <p>
     * The module configuration (your puzzle input) lists each module. The name of the module is preceded by a symbol identifying its type,
     * if any. The name is then followed by an arrow and a list of its destination modules. For example:
     * <p>
     * broadcaster -> a, b, c
     * %a -> b
     * %b -> c
     * %c -> inv
     * &inv -> a
     * In this module configuration, the broadcaster has three destination modules named a, b, and c.
     * Each of these modules is a flip-flop module (as indicated by the % prefix).
     * a outputs to b which outputs to c which outputs to another module named inv.
     * inv is a conjunction module (as indicated by the & prefix) which, because it has only one input,
     * acts like an inverter (it sends the opposite of the pulse type it receives); it outputs to a.
     * <p>
     * By pushing the button once, the following pulses are sent:
     * <p>
     * <p>
     * % 只处理low purple   low off->on 发送 high ,  low on->off 发送low，如果收到high， 不处理。
     * & 记住所有连接的发送的最新信号，默认为low，如果所有连接的信号都是high， 发送low, 否则发送high
     * broadcaster 转发广播信号
     * button 发送low 信号给广播broadcaster
     * <p>
     * button -low-> broadcaster
     * broadcaster -low-> a
     * broadcaster -low-> b
     * broadcaster -low-> c
     * a -high-> b
     * b -high-> c
     * c -high-> inv
     * inv -low-> a
     * a -low-> b
     * b -low-> c
     * c -low-> inv
     * inv -high-> a
     * After this sequence, the flip-flop modules all end up off, so pushing the button again repeats the same sequence.
     * <p>
     * Here's a more interesting example:
     * <p>
     * broadcaster -> a
     * %a -> inv, con
     * &inv -> b
     * %b -> con
     * &con -> output
     * This module configuration includes the broadcaster, two flip-flops (named a and b), a single-input conjunction module (inv),
     * a multi-input conjunction module (con), and an untyped module named output (for testing purposes).
     * The multi-input conjunction module con watches the two flip-flop modules and, if they're both on, sends a low pulse to the output module.
     * <p>
     * Here's what happens if you push the button once:
     * <p>
     * button -low-> broadcaster
     * broadcaster -low-> a
     * a -high-> inv
     * a -high-> con
     * inv -low-> b
     * con -high-> output
     * b -high-> con
     * con -low-> output
     * Both flip-flops turn on and a low pulse is sent to output! However, now that both flip-flops are on and con remembers a high pulse from each of its two inputs,
     * pushing the button a second time does something different:
     * <p>
     * button -low-> broadcaster
     * broadcaster -low-> a
     * a -low-> inv
     * a -low-> con
     * inv -high-> b
     * con -high-> output
     * Flip-flop a turns off! Now, con remembers a low pulse from module a, and so it sends only a high pulse to output.
     * <p>
     * Push the button a third time:
     * <p>
     * button -low-> broadcaster
     * broadcaster -low-> a
     * a -high-> inv
     * a -high-> con
     * inv -low-> b
     * con -low-> output
     * b -low-> con
     * con -high-> output
     * This time, flip-flop a turns on, then flip-flop b turns off. However, before b can turn off,
     * the pulse sent to con is handled first, so it briefly remembers all high pulses for its inputs and sends a low pulse to output.
     * After that, flip-flop b turns off, which causes con to update its state and send a high pulse to output.
     * <p>
     * Finally, with a on and b off, push the button a fourth time:
     * <p>
     * button -low-> broadcaster
     * broadcaster -low-> a
     * a -low-> inv
     * a -low-> con
     * inv -high-> b
     * con -high-> output
     * This completes the cycle: a turns off, causing con to remember only low pulses and restoring all modules to their original states.
     * <p>
     * To get the cables warmed up, the Elves have pushed the button 1000 times.
     * How many pulses got sent as a result (including the pulses sent by the button itself)?
     * <p>
     * In the first example, the same thing happens every time the button is pushed: 8 low pulses and 4 high pulses are sent.
     * So, after pushing the button 1000 times, 8000 low pulses and 4000 high pulses are sent. Multiplying these together gives 32000000.
     * <p>
     * In the second example, after pushing the button 1000 times, 4250 low pulses and 2750 high pulses are sent.
     * Multiplying these together gives 11687500.
     * <p>
     * Consult your module configuration; determine the number of low pulses and high pulses that would be sent after
     * pushing the button 1000 times, waiting for all pulses to be fully handled after each push of the button.
     * What do you get if you multiply the total number of low pulses sent by the total number of high pulses sent?
     */
    @Test
    public void part1() throws IOException {
        List<String> list = Files.readAllLines(Path.of("./src/test/resources/2023/", "day20.input"));
        list.forEach(System.out::println);

        int highPulses = 0, lowPulses = 0;

    }

    /**
     *
     * % 只处理low purple ，默认 off,  low off->on 发送 high ,  low on->off 发送low，如果收到high， 不处理。
     * & 记住所有连接的发送的最新信号，默认为low，如果所有连接的信号都是high， 发送low, 否则发送high
     * broadcaster 转发广播信号
     * button 发送low 信号给广播broadcaster
     */
    class FlipFlop implements Module {

        @Override
        public boolean sendPulse() {
            if (dest.isEmpty()) {
                return false;
            }
            if (receivePurple) {
                return false;
            }
            return true;
        }
    }

    class Conjunction implements Module {

        @Override
        public boolean sendPulse() {
            return !dest.isEmpty();
        }
    }

    class Broadcaster implements Module {

        @Override
        public boolean sendPulse() {
            return !dest.isEmpty();
        }
    }


    interface Module {

        String name = "";
        char type = '\0';
        boolean on = false;
        boolean high = false;
        boolean receivePurple = false;// low false, high true
        boolean sendPurple = false;// low false, high true

        List<String> dest = new ArrayList<>();
        Map<String, Boolean> conn = new HashMap<>();

        public boolean sendPulse();
    }


}

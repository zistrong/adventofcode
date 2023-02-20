package com.zistrong.adventofcode;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Queue;

public class Monkey implements Serializable {

    private final Queue<Integer> worriies = new ArrayDeque<>();

    private int dividedNumber;

    private int positiveTarget;

    private int negativeTarget;

    private int target;

    private String operation;

    private int popWorry;

    private int inspectTimes;

    public boolean inspect() {

        if (this.worriies.isEmpty()) {
            return false;
        }

        int num = this.worriies.poll();//pop worry

        //calculate value
        int opNumber1 = Integer.parseInt(operation.split(" ")[0].trim().replace("old", String.valueOf(num)));
        String op = operation.split(" ")[1].trim();
        int opNumber2 = Integer.parseInt(operation.split(" ")[2].trim().replace("old", String.valueOf(num)));
        switch (op) {
            case "+" -> num = opNumber1 + opNumber2;
            case "-" -> num = opNumber1 - opNumber2;
            case "*" -> num = opNumber1 * opNumber2;
            case "/" -> num = opNumber1 / opNumber2;
        }
        // you calculate
        num = num / 3;
        if (num % dividedNumber == 0) {// check target
            target = positiveTarget;
        } else {
            target = negativeTarget;
        }
        this.inspectTimes++;// record time
        this.popWorry = num;
        return true;

    }

    public void push(int worry) {

        this.worriies.offer(worry);

    }

    @Override
    public String toString() {

        return this.worriies.toString();
    }

    public Queue<Integer> getWorriies() {
        return worriies;
    }

    public void setDividedNumber(int dividedNumber) {
        this.dividedNumber = dividedNumber;
    }

    public void setPositiveTarget(int positiveTarget) {
        this.positiveTarget = positiveTarget;
    }

    public void setNegativeTarget(int negativeTarget) {
        this.negativeTarget = negativeTarget;
    }

    public int getTarget() {
        return target;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public int getPopWorry() {
        return popWorry;
    }

    public int getInspectTimes() {
        return inspectTimes;
    }

}

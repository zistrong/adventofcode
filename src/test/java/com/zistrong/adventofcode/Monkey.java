package com.zistrong.adventofcode;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Queue;

public class Monkey implements Serializable {


    static class Worry {
        int value;
        boolean inspected = false;

        public Worry(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "Worry{" +
                    "value=" + value +
                    ", inspected=" + inspected +
                    '}';
        }
    }

    private final Queue<Integer> worries = new ArrayDeque<>();
    private final Queue<Worry> worriesQueue = new ArrayDeque<>();

    int id;
    private int dividedNumber;

    private int positiveTarget;

    private int negativeTarget;

    private int target;

    private String operation;

    private int popWorry;
    private Worry popWorryObj;

    private int inspectTimes;

    private boolean inspected = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean inspect2(boolean allInspected) {

        if (this.worriesQueue.isEmpty()) {
            return false;
        }

        Worry worry = this.worriesQueue.poll();//pop worry
        int num = worry.value;

        //1 monkey inspect item
        //calculate value, you worry is calculated
        int opNumber1 = Integer.parseInt(operation.split(" ")[0].trim().replace("old", String.valueOf(num)));
        String op = operation.split(" ")[1].trim();
        int opNumber2 = Integer.parseInt(operation.split(" ")[2].trim().replace("old", String.valueOf(num)));
        switch (op) {
            case "+" -> num = opNumber1 + opNumber2;
            case "-" -> num = opNumber1 - opNumber2;
            case "*" -> num = opNumber1 * opNumber2;
            case "/" -> num = opNumber1 / opNumber2;
        }
        // after than you worry is divived
        // you calculate
//        if (!(this.worriesQueue.stream().allMatch(item -> item.inspected))) {
//            num = num / 3;
//        }

        if (worry.inspected) {
            num = num / 3;
        }
        if (num % dividedNumber == 0) {// check target
            target = positiveTarget;
        } else {
            target = negativeTarget;
        }

        this.inspected = true;
        worry.inspected = true;
        this.inspectTimes++;// record time
        worry.value = num;

        this.popWorryObj = worry;
        return true;

    }

    public boolean inspect() {

        if (this.worries.isEmpty()) {
            return false;
        }

        int num = this.worries.poll();//pop worry

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

        this.worries.offer(worry);
        this.worriesQueue.offer(new Worry(worry));

    }

    public void push(Worry worry) {

        this.worriesQueue.offer(worry);

    }

    @Override
    public String toString() {

        return this.id + ":" + this.worriesQueue;
    }

    public Queue<Integer> getWorries() {
        return worries;
    }

    public Queue<Worry> getWorriesObjs() {
        return worriesQueue;
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

    public Worry getPopWorryObj() {
        return popWorryObj;
    }

    public int getInspectTimes() {
        return inspectTimes;
    }

}

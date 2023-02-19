package com.zistrong.adventofcode;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Queue;

public class Monkey implements Serializable {

    private int id;

    private Queue<Integer> worriies = new ArrayDeque<>();

    private int divivedNumber;

    private int positiveTarget;

    private int negtiveTarget;

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
            case "+": {
                num = opNumber1 + opNumber2;
                break;
            }
            case "-": {
                num = opNumber1 - opNumber2;
                break;
            }
            case "*": {
                num = opNumber1 * opNumber2;
                break;
            }
            case "/": {
                num = opNumber1 / opNumber2;
                break;
            }
        }
        // you calculate
        num = num / 3;
        if (num % divivedNumber == 0) {// check target
            target = positiveTarget;
        } else {
            target = negtiveTarget;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Queue<Integer> getWorriies() {
        return worriies;
    }

    public void setWorriies(Queue<Integer> worriies) {
        this.worriies = worriies;
    }

    public int getDivivedNumber() {
        return divivedNumber;
    }

    public void setDivivedNumber(int divivedNumber) {
        this.divivedNumber = divivedNumber;
    }

    public int getPositiveTarget() {
        return positiveTarget;
    }

    public void setPositiveTarget(int positiveTarget) {
        this.positiveTarget = positiveTarget;
    }

    public int getNegtiveTarget() {
        return negtiveTarget;
    }

    public void setNegtiveTarget(int negtiveTarget) {
        this.negtiveTarget = negtiveTarget;
    }

    public int getTarget() {
        return target;
    }

    public String getOperation() {
        return operation;
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

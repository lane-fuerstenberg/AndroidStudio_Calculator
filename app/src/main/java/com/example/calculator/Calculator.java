package com.example.calculator;

import java.util.ArrayList;

public class Calculator {
    private ArrayList<String> contents = new ArrayList<String>();
    private String stagingArea = "";
    private final char[] OPERATOR = new char[] { '+', '-', '/', '*' };
    private boolean resetStagingArea = false;

    public void receiveInput(String input) {

        if (isOperator(stagingArea)) {
            if (isOperator(input)) {
                stagingArea = input;
            }

            else {
                contents.add(stagingArea);
                stagingArea = input;
            }

        }

        else {
            if (isOperator(input)) {
                commitStagingArea(input);
                resetStagingArea = false;
            }

            else if (resetStagingArea) {
                stagingArea = input;
                resetStagingArea = false;
            }

            else {
                stagingArea += input;
            }
        }
    }

    public String outputDisplay() {
        String output = "";

        for (String item : contents) {
            output += item;
            output += " ";
        }

        output += stagingArea;
        return output;
    }

    public String calculate() {
        if (stagingArea.equals("")) {
            return outputDisplay();
        }

        commitStagingArea();

        //calculating all multiply and divide
        processOperators('*', '/');
        processOperators('+','-');

        //calculating all add and subtract
        while (contents.contains("+") || contents.contains("-")) {
            int index = findIndex(new char[] { '+', '-'});
            processIndex(index);
        }

        resetStagingArea = true;
        stagingArea = contents.get(0);
        contents.remove(0);
        return stagingArea;
    }

    private void processOperators(char x, char y) {

        while (contents.contains(Character.toString(x))
                || contents.contains(Character.toString(y))) {

            int index = findIndex(new char[] { x, y});
            processIndex(index);
        }
    }

    private int findIndex(char[] searchTerms) {
        //looking for first instance of + or -
        for (int i = 0; i < contents.size(); i++) {

            String result = contents.get(i);
            if (result.equals(Character.toString(searchTerms[0]))
                    || result.equals(Character.toString(searchTerms[1]))) {
                return i;
            }
        }

        //should never occur since we validate that a + or - exists before running
        return -1;
    }

    //receives index in array and processes the value before and after index
    private void processIndex(int index) {
        int result = getResult(index);

        //not a typo removes the middle and last value used to calculate
        contents.remove(index);
        contents.remove(index);

        //replace first value with result
        contents.set(index - 1, Integer.toString(result));
    }

    private int getResult(int index) {
        if (contents.size() == index + 1) {
            return Integer.parseInt(contents.get(index - 1));
        }

        int x = Integer.parseInt(contents.get(index - 1));
        int y = Integer.parseInt(contents.get(index + 1));
        int result = 0;
        char operator = contents.get(index).charAt(0); //only 1 char anyway index is needed

        switch (operator) {
            case '+':
                result = x + y;
                break;

            case '-':
                result = x - y;
                break;

            case '*':
                result = x * y;
                break;

            case '/':
                result = x / y;
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + operator);
        }

        return result;
    }

    private boolean isOperator(String input) {
        for (char operator : OPERATOR) {
            if (input.equals(Character.toString(operator))) {
                return true;
            }
        }

        return false;
    }

    private boolean commitStagingArea(String input) {
        if (stagingArea.equals("")) {
            return false;
        }

        contents.add(stagingArea);
        stagingArea = input;
        return true;
    }

    private boolean commitStagingArea() {
        return commitStagingArea("");
    }

    public void clear() {
        contents.clear();
        stagingArea = "";
    }
}
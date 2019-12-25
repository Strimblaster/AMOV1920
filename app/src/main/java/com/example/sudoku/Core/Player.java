package com.example.sudoku.Core;

import java.io.Serializable;
import java.util.Stack;

public class Player implements Serializable, Comparable {
    private String name;
    private int points;
    private Stack<Integer> timeStack;

    public Player(String name) {
        this.name = name;
        this.points = 0;
        this.timeStack = new Stack<>();
    }
    public Player() {
        this.name = "";
        this.points = 0;
        this.timeStack = new Stack<>();
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int points){
        this.points += points;
    }

    public boolean hasMoreTime(){
        return !timeStack.empty();
    }

    public int getExtraTime(){
        return timeStack.pop();
    }

    public void addExtraTime(){
        timeStack.push(20);
    }

    public void removePoints(int points) {
        this.points -= points;
    }

    private int showExtraTime(){
        return this.timeStack.peek();
    }

    @Override
    public int compareTo(Object o) {
        if (o != null) {
            Player player = (Player) o;
            if (player != null) {
                if (this.getName().equals(player.getName()) && this.getPoints() == player.getPoints()) {
                    if (this.hasMoreTime() && player.hasMoreTime()) {
                        if (this.showExtraTime() == player.showExtraTime()) {
                            return 1;
                        }
                    }
                }
            }
        }
        return -1;
    }
}

package com.company;

import java.util.ArrayList;
import java.util.List;

//this class is to store the current state of the blockGame;
public class Node {
    int fValue;
    int gValue;
    int hValue;
    int blockNums;
    int stackNums;
    ArrayList<ArrayList<Character>> blockGame;

    Node(int fValue, int gValue, int hValue, ArrayList<ArrayList<Character>> blockGame, int blockNums, int stackNums) {

        this.fValue = fValue;
        this.gValue = gValue;
        this.hValue = hValue;
        this.blockGame = blockGame;
        this.blockNums = blockNums;
        this.stackNums = stackNums;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        int outSize = blockGame.size();
        for (int i = 0; i < outSize; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(i + 1).append(" | ");
            if (blockGame.get(i) == null || blockGame.get(i).size() == 0) {
                res.append(sb.toString()).append("\n");
                continue;
            }

            for (int j = 0; j < blockGame.get(i).size() - 1; j++) {
                sb.append(blockGame.get(i).get(j)).append(" , ");
            }
            sb.append(blockGame.get(i).get(blockGame.get(i).size() - 1));
            res.append(sb.toString()).append("\n");

        }
        return res.toString();
    }
}

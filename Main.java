package com.company;

import java.util.ArrayList;
import java.util.List;

public class Main {


    public static void main(String[] args) {
        // write your code here
        Main obj = new Main();
        //build graph
        BlocksGenerator blocksGenerator = new BlocksGenerator();
        Node blGameInitial = blocksGenerator.blockBuilder(Integer.valueOf(args[0]), Integer.valueOf(args[1]));
        //blocksGenerator.blockPrint(blGameInitial);
        AStarSearch searchImplement = new AStarSearch();
        Node target = searchImplement.search(blGameInitial);

    }
}











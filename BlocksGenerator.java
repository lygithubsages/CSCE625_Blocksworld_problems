package com.company;

import java.util.*;

public class BlocksGenerator {

    public Node blockBuilder(int stackNum, int blockNum){
        ArrayList<ArrayList<Character>> blockGame = new ArrayList<ArrayList<Character>>();
        Node result = new Node(0, 0, 0, blockGame, blockNum, stackNum);
        List<Character> blocks = new LinkedList<>();
        for (Character i = 'A'; i < 'A' + blockNum; i++) {
            blocks.add(i);
        }
        Random random = new Random();
        for (int i = 0; i < stackNum; i++) {
            blockGame.add(i, new ArrayList<>());
        }

        for (int i = 0; i < blockNum; i++) {
            int stack = random.nextInt(stackNum);
            blockGame.get(stack).add(blocks.get(i));
        }
        return result;
    }

    public void blockPrint(Node bGame) {
        if (bGame == null) {
            return;
        }
        int size = bGame.blockGame.size();
        for (int i = 0; i < size; i++) {
            int stack = i + 1;
            System.out.println(stack + " | " + bGame.blockGame.get(i));
        }
    }
}

package com.company;

import java.util.*;

class Compare implements Comparator<Node> {
    public int compare(Node n1, Node n2) {
        return n1.fValue - n2.fValue;
    }
}

public class AStarSearch extends BlocksGenerator{

    int iterateNum = 0;
    int MaxQueueSize = 0;
    Node finalNode = null;


    public int getHValue(ArrayList<ArrayList<Character>> modifyStates, int stackSize, int blockSize) {
        int hValue = 0;
        ArrayList<Character> stackOne = modifyStates.get(0);
        Character target = null;
        //if stack one do not start from A, then we need to remove all blocks on stack1, therefore we needs to plus all nodes in stack 1 and total blocks.
        int wrongPosSum = 0;
        
        //since in our target, blocks are all in stack0, therefor, if other stacks have blocks we need to remove all of them.
        for (int k = 1; k < stackSize; k++) {
            if (modifyStates.get(k) == null) {
                continue;
            }
            int wrongPosBloOnStac = modifyStates.get(k).size();
            wrongPosSum = wrongPosSum + wrongPosBloOnStac;
            //if the next target can be move to right place just by one movement, then we should eliminate its effect in hValue;
            for (int blockId = 0; blockId < modifyStates.get(k).size() - 1; blockId++) {
                if (modifyStates.get(k).get(blockId + 1) - modifyStates.get(k).get(blockId) > -1) {
                    wrongPosSum++;
                }
            }
        }
        
        if (stackOne == null || stackOne.size() == 0) {
            hValue = wrongPosSum;
        } else if (stackOne.get(0) != 'A') {
            hValue = hValue + stackOne.size() + wrongPosSum;
        } else { //check when the block is wrong for stack 1.
            //hValue = hValue + blockSize - stackOne.size(); //加上第1列还差多少。
            int j = 1;//start from B to check where is wrong
            int sameCount = 1;
            Character i;
            for ( i = 'B'; i < 'B' + stackOne.size() - 1; i++) {
                if (stackOne.get(j) != i) {
                    break;
                }
                sameCount++;
                j++;
            }
            hValue = hValue + stackOne.size() -  sameCount + wrongPosSum;
            target = i;
        }
        
        return hValue;
    }


    public Node getNewNode(ArrayList<ArrayList<Character>> newStateGame, int stackN, int blockN) {
        int hValue = getHValue(newStateGame, stackN, blockN);
        int fvalue = Integer.MAX_VALUE - 10000 + hValue;
        return new Node(fvalue, Integer.MAX_VALUE - 10000, hValue, newStateGame, blockN, stackN);
    }


    public List<Node> changePosition(int i, int j, Node state, HashMap<Integer,Character> lastChars, List<Node> result) { //把现在状态中的第j个stack进行改变

        ArrayList<ArrayList<Character>> newStateGame = new ArrayList<>(state.stackNums);
        for (int ind = 0; ind < state.stackNums; ind++) {
            ArrayList<Character> newBlocks = new ArrayList<>(state.blockGame.get(ind).size());
            for (int inner = 0; inner < state.blockGame.get(ind).size(); inner++) {
                newBlocks.add(state.blockGame.get(ind).get(inner));
            }
            newStateGame.add(newBlocks);
        }
        newStateGame.get(j).add(lastChars.get(i));
        newStateGame.get(i).remove(lastChars.get(i));
        Node newNode = getNewNode(newStateGame, state.stackNums, state.blockNums);
        result.add(newNode);
        return result;
    }


    public List<Node> successor(Node curState) { //set all successor state's gValue to Integer.MaxValue;
        Node orgState = new Node(curState.fValue, curState.gValue,curState.hValue,curState.blockGame,curState.blockNums,curState.stackNums);
        List<Node> result = new ArrayList<>();
        ArrayList<ArrayList<Character>> blocksWorldNow = new ArrayList<>(curState.blockGame);
        int stacksNum = curState.stackNums;
        HashMap<Integer,Character> lastChars = new HashMap<>();

        for ( int i = 0; i < stacksNum; i++) {
            if (blocksWorldNow.get(i) == null || blocksWorldNow.get(i).size() == 0) {
                continue;
            }
            ArrayList<Character> blocksNow = blocksWorldNow.get(i);
            Character lastChar = blocksNow.get(blocksNow.size() - 1);
            lastChars.put(i, lastChar);
            for (int j = 0; j < stacksNum; j++) {
                if (i == j) {
                    continue;
                }

                changePosition(i, j, orgState, lastChars, result);
            }
        }
        return result;
    }


    public boolean nodeEqualTarget(Node curNode){
        ArrayList<ArrayList<Character>> curState = curNode.blockGame;
        int sizeBlocks = 0;
        for (ArrayList<Character> arrayList : curState) {
            sizeBlocks = sizeBlocks + arrayList.size();
        }
        //blockNums为建图的时候设置的blocks数目；
        if (sizeBlocks != curNode.blockNums) {
            return false;
        }//if all nodes num at now is not equal to the beginning set of blockNum, return false;
        ArrayList<Character> stackOneBlocks = curState.get(0);
        if (stackOneBlocks.size() != curNode.blockNums) {
            return false;
        }
        ArrayList<Character> targetOneBlocks = new ArrayList<>();
        for (Character i = 'A'; i < 'A' + sizeBlocks; i++) {
            targetOneBlocks.add(i);
        }
        int targetIndex = 0;
        for (Character character: stackOneBlocks) {
            if (character != targetOneBlocks.get(targetIndex)) {
                return false;
            }
            targetIndex++;
        }
        return true;
    }


    public void printAllState (HashMap<ArrayList<ArrayList<Character>>, ArrayList<ArrayList<Character>>> comeFrom, Node finalNode) {

        ArrayList<ArrayList<Character>> target = finalNode.blockGame;
        List<ArrayList<ArrayList<Character>>> result = new ArrayList<>();
        result.add(target);

        while (comeFrom.containsKey(target)) {

            ArrayList<ArrayList<Character>> pre = comeFrom.get(target);

            result.add(pre);
            target = pre;
        }
        printReversedList(result);

    }


    public  void printReversedList(List<ArrayList<ArrayList<Character>>> link) {
        System.out.println("from start to end we need go through follow state");

        int count  = 0;
       for (int i = link.size() - 1; i >= 0; i--) {

           System.out.println("the " + count + " iteration ");
           printArrayList(link.get(i));
           System.out.println();
           count++;

        }
    }


    public void printArrayList(ArrayList<ArrayList<Character>> graph) {

        for (int i = 0; i < graph.size(); i++) {
            int stack = i + 1;
            System.out.println(stack + " | " + graph.get(i));

        }

    }


    public Node search(Node game) {

        System.out.println("initial state : ");
        blockPrint(game); //print orgSate;

        HashSet<String> doneNodes = new HashSet<>(); //closed set.
        HashMap<ArrayList<ArrayList<Character>>, ArrayList<ArrayList<Character>>> comeFrom  =new HashMap<>(); //用index记录父子关系

        game.hValue = getHValue(game.blockGame, game.stackNums, game.blockNums); //因为初始化时start的hValue默认为空，设置H
        game.fValue = game.hValue + game.gValue;

        Queue<Node> pq = new PriorityQueue<Node>(new Compare());
        HashMap<String, Node> pqArray = new HashMap<>();


        pq.offer(game);
        pqArray.put(game.toString(), game);

        int iteratortime = 0;

        while (!pq.isEmpty()) {
            MaxQueueSize = Math.max(MaxQueueSize, pq.size());
            Node curState = pq.poll(); //f值最小的Node
            System.out.println("iter = " + iteratortime + " , queue = " +pq.size() +" , " +" f = g + h = " + curState.fValue + " , depth = " + curState.gValue);
            iteratortime++;

            pqArray.remove(curState.toString());
            doneNodes.add(curState.toString());

            if (nodeEqualTarget(curState)) {
                System.out.println("iter = " + iteratortime + " , queue = " +pq.size() + " f = g + h = " + curState.fValue + " , depth = " + curState.gValue);
                System.out.println("Success! depth = " + curState.gValue +" , total_goal_tests = "+ iteratortime + " , max_queue_size = " + MaxQueueSize);
                System.out.println("solution path : ");
                printAllState(comeFrom, curState);
                return curState;
            }

            List<Node> nextStepStates = successor(curState);//get all next state that can be generate from curState;

            for (Node nextState: nextStepStates) {

                if (nodeEqualTarget(nextState)) {
                    comeFrom.put(nextState.blockGame, curState.blockGame);
                    nextState.gValue = curState.gValue + 1;
                    nextState.fValue = nextState.gValue + nextState.hValue;
                    System.out.println("iter = " + iteratortime + " , queue = " +pq.size() + " f = g + h = " + nextState.fValue + " , depth = " + nextState.gValue);
                    System.out.println("Success! depth = " + nextState.gValue +" , total_goal_tests = "+ iteratortime + " , max_queue_size = " + MaxQueueSize);
                    System.out.println("solution path : ");
                    printAllState(comeFrom, nextState);
                    return nextState;
                }

                if (doneNodes.contains(nextState.toString())) {
                    continue;
                }

                int tempNextStateGVal = curState.gValue + 1;

                if (pqArray.containsKey(nextState.toString())) {
                    if (tempNextStateGVal > pqArray.get(nextState.toString()).gValue) {
                        continue;
                    }
                    nextState.gValue = tempNextStateGVal;
                    nextState.fValue = nextState.gValue + nextState.hValue;
                    comeFrom.put(nextState.blockGame, curState.blockGame);
                }

                if (!pqArray.containsKey(nextState.toString())) {
                    nextState.gValue = tempNextStateGVal;
                    nextState.fValue = nextState.gValue + nextState.hValue;
                    comeFrom.put(nextState.blockGame, curState.blockGame);
                    pq.offer(nextState);
                    pqArray.put(nextState.toString(), nextState);
                }
                //doneNodes.add(nextState.toString());
            }
        }
        System.out.println("can not transfer to target state");
        return null;
    }
}

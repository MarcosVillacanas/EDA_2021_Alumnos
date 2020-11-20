package usecase.practica3;

import material.Position;
import material.tree.binarytree.BinaryTree;
import material.tree.binarytree.LinkedBinaryTree;

public class ArithmeticEvaluator {

    LinkedBinaryTree<String> tree;

    public ArithmeticEvaluator(String op) {
        this.tree = splitOp(op);
    }

    private LinkedBinaryTree<String> splitOp(String op) {

        int nextSymbolIndex = lessPrioritySymbol(op);
        LinkedBinaryTree<String> subTree = new LinkedBinaryTree<>();

        if (nextSymbolIndex == -1) {
            String number = op;
            subTree.addRoot(number);
        }
        else {
            String nextSymbol = op.substring(nextSymbolIndex, nextSymbolIndex + 1);
            Position<String> subRoot = subTree.addRoot(nextSymbol);

            subTree.attachLeft(subRoot, this.splitOp(op.substring(0, nextSymbolIndex)));
            subTree.attachRight(subRoot, this.splitOp(op.substring(nextSymbolIndex + 1)));
        }
        return subTree;
    }


    private int lessPrioritySymbol (String op) {
        int firstStarIndex = -1;
        boolean startFound = false;
        for (int index = op.length() - 1; index >= 0; index --) {
            if (op.charAt(index) == '+' || op.charAt(index) == '-') {
                return index;
            } else if (op.charAt(index) == '*' && !startFound) {
                firstStarIndex = index;
                startFound = true;
            }
        }
        return firstStarIndex;
    }

    public int evaluate () {
        return this.getValue(this.tree.root());
    }

    private int getValue(Position<String> p) {
        String symbol = p.getElement();
        switch (symbol) {
            case "+": {
                return this.getValue(this.tree.left(p)) + this.getValue(this.tree.right(p));
            }
            case "-": {
                return this.getValue(this.tree.left(p)) - this.getValue(this.tree.right(p));
            }
            case "*": {
                return this.getValue(this.tree.left(p)) * this.getValue(this.tree.right(p));
            }
            default: {
                return Integer.parseInt(symbol);
            }
        }
    }

}

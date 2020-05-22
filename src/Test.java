import Tree.*;
import Tree.AVLTree.AVLTree;
import Tree.ScapegoatTree.ScapeGoatTree;
import Tree.TreapTree.Treap;

public class Test {
    public Tree<String> test;

    public long[] time(int n){
        int iter = 1;
        long[] t = new long[n];
        long start;
        test = new AVLTree<>();
        for (int i = 0; i < n; i++) {
            start = System.nanoTime();
            test.push(i, "" + i);
            t[i] = System.nanoTime() - start;
        }

        return t;
    }

    public String get(int n){
        return test.find(n);
    }
}

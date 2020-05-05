import Tree.AVLTree.AVLTree;

class Main {
    public static void main(String[] args) {
        AVLTree<String> avl = new AVLTree();
        int[] mas = {55, 14, 0, 16, 60, 57, 70, 15, 58, 100, -9, -10, 101, 102, 103, 19};
        //int[] mas ={10,15,18,0,20,21,22};
        //int[] mas = {10, 5, 1, 6, 15, 14, 17,13};
        //int[] mas = {10, 5, 1, 6, 20, 15, 13, 17, 50};
        //int[] mas = {4, 2, 6, 1, 3, 5, 8, 7};
        //int[] mas = {1, 2, 3, 4, 5, 6, 7, 8};
        //int[] mas = {8, 7, 6, 5, 4, 3, 2, 1};
        for (int i : mas) {
            avl.push(i, ""+i);
        }

        avl.

        System.out.println(avl.pop(100));




    }
}
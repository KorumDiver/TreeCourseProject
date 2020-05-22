import Tree.AVLTree.AVLTree;

class Main {
    public static void main(String[] args) {
        Test test = new Test();
        long[] all = test.time(50000);

        for (int i = 0; i < all.length; i++) {
            System.out.print(all[i] + ";");
        }
    }
}
package Tree.ScapegoatTree;

class SGTNode<T> {
    SGTNode<T> right, left, parent;
    int value;
    T data;

    /* Constructor */
    public SGTNode(int val){
        value = val;
        data = null;
    }
    public SGTNode(int val, T d) {
        value = val;
        data = d;
    }
}

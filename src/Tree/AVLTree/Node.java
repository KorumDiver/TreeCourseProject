package Tree.AVLTree;

public class Node<T> {
    private Node parent;
    private Node left;
    private Node right;
    private int ID;
    private int sizeTree = 1;
    private T data;

    public Node(int ID, T data) {
        this.ID = ID;
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Node<T> getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node<T> getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node<T> getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getSizeTree() {
        return sizeTree;
    }

    public void setSizeTree(int sizeTree) {
        this.sizeTree = sizeTree;
    }
}

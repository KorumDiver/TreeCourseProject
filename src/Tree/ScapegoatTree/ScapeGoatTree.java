package Tree.ScapegoatTree;

import Tree.Tree;

public class ScapeGoatTree<T> implements Tree<T> {
    private SGTNode<T> root;
    private int n, q, maxn; //n is the size of tree and q is overestimate of n, maxn is biggest tree size since rebalancing

    /* Constructor */
    public ScapeGoatTree(){
        root = null;
        n = 0;
        maxn = 0;
    }

    /* Function to check if tree is empty */
    public boolean isEmpty() {
        return root == null;
    }

    /* Function to count number of nodes recursively */
    private int size(SGTNode<T> r) {
        if (r == null)
            return 0;
        else {
            int l = 1;
            l += size(r.left);
            l += size(r.right);
            return l;
        }
    }

    /* Functions to search for an element */
    public T find(int val) {
        return search(root, val);
    }

    /* Function to search for an element recursively */
    private T search(SGTNode<T> r, int val) {
        //has been changed from bool's to T's
        T found = null;
        while ((r != null) && (found == null))
        {
            if (val < r.value)
                r = r.left;
            else if (val > r.value)
                r = r.right;
            else {
                found = r.data;
                break;
            }
            found = search(r, val);
        }
        return found;
    }

    /* Function to return current size of tree */
    public int size() {
        return n;
    }

    /* Function for preorder traversal */
    public void preorder() {
        preorder(root);
    }

    private void preorder(SGTNode<T> r) {
        if (r != null) {
            preorder(r.left);
            preorder(r.right);
        }
    }

    private static final int log32(int q) {
        final double log23 = 2.4663034623764317;
        return (int)Math.ceil(log23*Math.log(q));
    }


    /* Function to insert an element */
    public void push(int x, Object data) {
        /* first do basic insertion keeping track of depth */
        SGTNode<T> u = new SGTNode<T>(x, (T)data);
        int d = addWithDepth(u); //add a node and take depth
        if (d >= log32(q) && d != 0) {
            /* depth exceeded, find scapegoat */
            SGTNode<T> w = u.parent;
            while (3*size(w) <= 2*size(w.parent))
                w = w.parent;
            rebuild(w.parent);
        }
    }

    /* Function to rebuild tree from node u */
    protected void rebuild(SGTNode<T> u) {
        int ns = size(u);
        SGTNode<T> p = u.parent;


        @SuppressWarnings("unchecked")
        SGTNode<T>[] a = (SGTNode<T>[])new SGTNode[ns];
        packIntoArray(u, a, 0);
        if (p == null) {
            root = buildBalanced(a, 0, ns);
            root.parent = null;
        }
        else if (p.right == u) {
            p.right = buildBalanced(a, 0, ns);
            p.right.parent = p;
        }
        else {
            p.left = buildBalanced(a, 0, ns);
            p.left.parent = p;
        }
        maxn = n;
    }

    /* Function to packIntoArray */
    protected int packIntoArray(SGTNode<T> u, SGTNode<T>[] a, int i) {
        if (u == null) {
            return i;
        }

        i = packIntoArray(u.left, a, i);
        a[i++] = u;

        return packIntoArray(u.right, a, i);
    }

    /* Function to build balanced nodes */
    protected SGTNode<T> buildBalanced(SGTNode<T>[] a, int i, int ns) {
        if (ns == 0)
            return null;

        int m = ns / 2;
        a[i + m].left = buildBalanced(a, i, m);

        if (a[i + m].left != null)
            a[i + m].left.parent = a[i + m];

        a[i + m].right = buildBalanced(a, i + m + 1, ns - m - 1);

        if (a[i + m].right != null)
            a[i + m].right.parent = a[i + m];

        return a[i + m];
    }

    /* Function add with depth */
    public int addWithDepth(SGTNode<T> u) {
        SGTNode<T> w = root;
        if (w == null) {
            root = u;
            n++;
            q++;
            maxn = n > maxn ? n : maxn;
            return 0;
        }

        boolean done = false;
        int d = 0;
        do {
            if (u.value < w.value) {
                if (w.left == null) {
                    w.left = u;
                    u.parent = w;
                    done = true;
                }
                else {
                    w = w.left;
                }
            }
            else if (u.value > w.value) {
                if (w.right == null) {
                    w.right = u;
                    u.parent = w;
                    done = true;
                }
                w = w.right;
            }
            else {
                return -1;
            }
            d++;
        } while (!done);

        n++;
        q++;
        maxn = n > maxn ? n : maxn;
        return d;
    }

    public T pop(int key) {
        SGTNode<T> current = root;
        SGTNode<T> parent = root;
        boolean isLeftChild = true;

        while(current.value != key) {
            parent = current;
            if (key < current.value) {
                isLeftChild = true;
                current = current.left;
            }
            else {
                isLeftChild = false;
                current = current.right;
            }

            if (current == null) return null;
        }
        T obj = (T)current.data;
        if (current.left == null && current.right == null) {
            //node we're about to delete has no child nodes
            if (current == root) root = null;
            else if (isLeftChild) parent.left = null;
            else parent.right = null;
        }
        else if (current.right == null) {
            //node we're about to delete has right child
            if (current == root)
                root = current.left;
            else if(isLeftChild)
                parent.left = current.left;
            else
                parent.right = current.left;
        }
        else if(current.left == null) {
            //node we're about to delete has left child
            if (current == root)
                root = current.right;
            else if (isLeftChild)
                parent.left = current.right;
            else
                parent.right = current.right;
        }
        else {
            //node has both children
            SGTNode<T> successor = getSuccessor(current); //looking for successor
            if (current == root) root = successor;
            else if (isLeftChild) parent.left = successor;
            else parent.right = successor;
            successor.left = current.left;
        }
        n--; q--;
        if (n < 2/3*maxn) rebuild(root);
        return obj;
    }

    protected SGTNode<T> getSuccessor(SGTNode<T> delNode) {
        SGTNode<T> successorParent = delNode;
        SGTNode<T> successor = delNode;
        SGTNode<T> current = delNode.right;
        while(current != null) {
            successorParent = successor;
            successor = current;
            current = current.left;
        }

        if (successor != delNode.right) {
            successorParent.left = successor.right;
            successor.right = delNode.right;
        }
        return successor;
    }
}

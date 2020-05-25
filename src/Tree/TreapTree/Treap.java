package Tree.TreapTree;

import java.util.Random;
import Tree.Tree;

public class Treap<T> implements Tree<T> {

    static private Random rand = new Random();
    private int id;
    private int y;
    public Treap Left;
    public Treap Right;
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Treap(int id, T data){
        this(id, rand.nextInt(), data);
    }

    private Treap(int id, int y, T data){
        this.id = id;
        this.y = y;
        this.Left = null;
        this.Right = null;
        this.data = data;
    }

    private Treap(int id, int y, Treap left, Treap right, T data){
        this.id = id;
        this.y = y;
        this.Left = left;
        this.Right = right;
        this.data = data;
    }

    /**
     * Слияние
     *
     * Параметр L левое поддерево
     * Параметр R правое поддерево
     * Возвращает Объединённое дерево
     */

    public static Treap Merge(Treap L, Treap R){
        if (L == null) return R;
        if(R == null) return L;

        if(L.y > R.y) {
            return new Treap(L.id, L.y, L.Left, Merge(L.Right, R),L.data);
        }else{
            return new Treap(R.id, R.y, Merge(L, R.Left), R.Right,R.data);
        }

    }

    /**
     * Разрезание дерева
     *
     * Параметр L левое дерево
     * Параметр R правое дерево
     * Параметр newTree это измененная ветка правого или левого поддерева, в зависимости от случая
     * Возвращает Массив, состоящий из левого и правого дерева после разрезания
     */

    public Treap[] Split(int id){
        Treap newTree = null;
        Treap L,R;
        if(this.id < id){
            if(Right == null)
                R = null;
            else{
                Treap[] gg = Right.Split(id);
                newTree = gg[0];
                R = gg[1];
            }
            L = new Treap(this.id, y, Left, newTree,this.data);
        }else{
            if(Left == null)
                L = null;
            else{
                Treap[] gg = Left.Split(id);
                L = gg[0];
                newTree = gg[1];
            }
            R = new Treap(this.id, y, newTree, Right,this.data);
        }
        return new Treap[]{L,R};
    }

    /**
     * Добавление элемента
     * Добавление происходит посредством вызова вспомогательного метода Add, который возвращает новое дерево
     * После чего текущее дерево заменяется на новое
     * Возвращает правду
     */

    @Override
    public void push(int id, T data){
        Treap tree = Add(id,data);
        this.id = tree.id;
        this.y = tree.y;
        this.Left = tree.Left;
        this.Right = tree.Right;
        this.data = (T) tree.data;
    }

    /**
     * Вспомогательный метод для добавления элемента
     * Добавление происходит посредством того, что по элементу, который мы хотим добавить, разрезается дерево
     * После чего мы методом слияние соединяем полученные правое и левое дерево и новый элемент.
     * Возвращает новое дерево
     */

    private Treap Add(int id, T data) {
        Treap[] t = Split(id);
        Treap l = t[0];
        Treap r = t[1];
        Treap m = new Treap(id, data);
        return  Merge(Merge(l,m),r);
    }

    /**
     * Метод удаления элемента
     * Параметр l - дерево, которое содержит элементы мыеньше искомого
     * Параметр m - дерево, которое содержит только искомый элемент
     * Параметр r - дерево, которое содержит элементы больше искомого
     * Двумя методами Split мы делим дерево на три части: 1)все элементы меньше искомого 2)искомый элемент 3)все элементы больше искомого
     * Далее с помощью метода Merge мы соединяем 1 и 3 дерево и получаем новое, которое не содержит элемент, который мы хотим удалить
     * Возвращает данные элемента, который мы удалили
     */

    @Override
    public T pop(int id) {
        Treap l, m, r;
        Treap[] gg =  this.Split(id);
        l = gg[0];
        gg = gg[1].Split(id+1);
        m = gg[0];
        r = gg[1];
        Treap tree = Merge(l,r);
        this.id = tree.id;
        this.Left = tree.Left;
        this.Right = tree.Right;
        this.data = (T) tree.data;
        return (T) m.data;
    }

     /**
     * Метод поиска элемента
     * Производим спуск через потомков
     * Возвращает данные найденного элемента, если такой элемент существует
     */

    @Override
    public T find(int value){
        if(id == value)
            return data;
        if(id > value)
            if(Left != null)
                return (T) Left.find(value);
        if(id < value)
            if(Right != null)
                return (T) Right.find(value);
        return null;

    }

    /*
     * Метод нахождения роста дерева
     * Производим спуск через потомков
     * Параметр deep считывает высоту, увеличиваясь, во время прохождения по этажам
     * Возвращает высоту самой длинной ветки
     */

    private int deep(Treap L, Treap R) {
        int deep = 1;
        int right = 0;
        int left = 0;
        if(L == null & R == null)
            return deep;
        if(L != null)
            right += deep + deep(L.Left, L.Right);
        if(R != null)
            left += deep + deep(R.Left, R.Right);
        if(left > right)
            return left;
        else
            return right;
    }
}

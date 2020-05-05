package Tree.AVLTree;

import Tree.Tree;

public class AVLTree<T> implements Tree<T> {
    private Node<T> first;

    @Override
    public boolean push(int id, T data) {
        Node newNode = new Node(id, data);//Создаем элемент добавления(тот который будет вставлен в дерево)
        if (first == null) {//Проверка на первый элемент(Есть ли корень у дерева)
            this.first = newNode;
        } else {
            newNode.setParent(this.first);//Создаем указатель на родителя
            while (true) {//"Бесконечный" цикл прохождения по дереву
                if (newNode.getID() == newNode.getParent().getID()) {//Если элемент уже существует, то возвращаем False
                    return false;
                }
                if (newNode.getID() < newNode.getParent().getID()) {//Сравнение элемента в вставляемом объекте и его родителя
                    if (newNode.getParent().getLeft() == null) {//Если элемент меньше родительского, то проверяем существование левого потомка
                        newNode.getParent().setLeft(newNode);//Если его нет, то родителю нашего элемента назначаем его левым потомком
                        break;//выходим из цикла
                    } else {//если левый потомок существует, то присваеваем левый потомок родителя, родителм нашего элемента
                        newNode.setParent(newNode.getParent().getLeft());
                    }
                } else {//Если ключ элемента оказался больше, проделываем эдентичные операций для правого потомка
                    if (newNode.getParent().getRight() == null) {
                        newNode.getParent().setRight(newNode);
                        break;
                    } else {
                        newNode.setParent(newNode.getParent().getRight());
                    }
                }
            }
        }

        ballans(newNode.getParent());

        return true;
    }

    @Override
    public T pop(int id) {
        Node<T> node = getNode(id);//Находим требуемый для удаления элемент
        if (node.getLeft() == null && node.getRight() == null) {//У нашего элемента есть 2 потомка?
            Node bal;
            if (node.getParent() == null) {//Да. У нашего элемента есть родитель?
                this.first = null;//Нет. Значит это первый и единственный элеменит в дереве
                return node.getData();//Возвращаем удачное удаление
            } else {//Да, у нашего элемента еть родитель
                if (node.getParent().getLeft() == node) {//Наш элемент левый?
                    bal = node.getParent();
                    node.getParent().setLeft(null);//Да. Удаляем связь родителя с левым потомком
                    ballans(bal);
                    return node.getData();//Возвращаем удачное удаление
                } else {//Нет, он правый
                    bal = node.getParent();
                    node.getParent().setRight(null);//Удаляем связь родителя с правым потомком
                    ballans(bal);
                    return node.getData();//Возвращаем удачное удаление
                }
            }
        }
        if (!(node.getLeft() == null) && node.getRight() == null) {//Наш элемент имеет только левый потомок?
            if (node.getParent() == null) {//Да. У нашего элемента есть родитель?
                this.first = node.getLeft();//Нет. Делаем корневым элементом левый потомок преведущего
                this.first.setParent(null);//Обнуляем значение родителя
                sizeTree(this.first);
                return node.getData();//Возвращаем удачное удаление
            } else {//Да, есть родитель
                Node ret = node.getParent();//Создаем ссылку на родителя нашего элемента
                if (node.getParent().getLeft().equals(node)) {
                    ret.setLeft(node.getLeft());
                    node.getLeft().setParent(ret);
                    ballans(ret);
                    return node.getData();
                } else {
                    ret.setRight(node.getLeft());
                    node.getLeft().setParent(ret);
                    ballans(ret);
                    return node.getData();
                }
            }
        }
        if (node.getLeft() == null && !(node.getRight() == null)) {
            if (node.getParent() == null) {
                this.first = node.getRight();
                this.first.setParent(null);
                sizeTree(this.first);
                return node.getData();
            } else {
                Node ret = node.getParent();
                if (node.getParent().getLeft() == node) {
                    ret.setLeft(node.getRight());
                    node.getRight().setParent(ret);
                    ballans(ret);
                    return node.getData();
                } else {
                    ret.setRight(node.getRight());
                    node.getRight().setParent(ret);
                    ballans(ret);
                    return node.getData();
                }
            }
        }
        if (!(node.getLeft() == null) && !(node.getRight() == null)) {
            Node nodeNext = next(node);
            Node bal;
            if (nodeNext.getRight() == null) {//Нет правого потомка
                if (nodeNext.getParent().getLeft() == nodeNext) {//Наш элемент левый?
                    nodeNext.getParent().setLeft(null);//Да, ставим родителю на левого потомка нуль
                } else {
                    nodeNext.getParent().setRight(null);//Иначе, на правого
                }
                bal = nodeNext.getParent();
                insertNode(node, nodeNext, bal);
                return node.getData();
            } else {//если правый потомок есть
                if (nodeNext.getParent().getLeft() == nodeNext) {//наш элеменрт левый?
                    nodeNext.getParent().setLeft(nodeNext.getRight());//Да, ставим родителю левым элементом правый от потомка
                    nodeNext.getRight().setParent(nodeNext.getParent());//Ставим потомку родителем наш элемент
                    nodeNext.setRight(null);//обнуляем правого потомка у удаляемого элемента
                } else {
                    nodeNext.getParent().setRight(nodeNext.getRight());//зеркально, ставим правым элементом правый от потомка
                    nodeNext.getRight().setParent(nodeNext.getParent());//Ставим потомку родителем наш элемент
                    nodeNext.setRight(null);//обнуляем правого потомка у удаляемого элемента
                }
                bal = nodeNext.getParent();
                insertNode(node, nodeNext, bal);

                return node.getData();
            }
        }

        return null;
    }

    @Override
    public T find(int id) {
        return getNode(id).getData();
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------
    private void ballans(Node<T> node) {
        if (node == null) return;
        Node<T> traveler = node;
        Node<T> helper;
        while (true) {
            helper = traveler;
            int sizePast = traveler.getSizeTree();//Запоминаем значение размера до пересчета
            sizeTree(traveler);//Пересчитываем
            if (sizePast == traveler.getSizeTree()) {//Сравниваем размеры до и после
                if (traveler.getParent() != null) {//если это лист переходим на одну вершину наверх
                    traveler = traveler.getParent();
                    continue;
                } else {
                    return;//Если это первый элемент выходим
                }
            } else {
                if (differenceSize(traveler) >= 2) {
                    if (traveler.getRight().getLeft() != null && traveler.getRight().getRight() == null) {
                        helper = leftBig(traveler);
                    } else if (traveler.getRight().getLeft() == null && traveler.getRight().getRight() != null) {
                        helper = leftSmall(traveler);
                    } else if (differenceSize(traveler.getRight()) >= 0) {
                        helper = leftSmall(traveler);
                    } else if (differenceSize(traveler.getRight()) < 0) {
                        helper = leftBig(traveler);
                    }
                } else if (differenceSize(traveler) <= -2) {
                    if (traveler.getLeft().getRight() != null && traveler.getLeft().getLeft() == null) {
                        helper = rightBig(traveler);
                    } else if (traveler.getLeft().getRight() == null && traveler.getLeft().getLeft() != null) {
                        helper = rightSmall(traveler);
                    } else if (differenceSize(traveler.getLeft()) <= 0) {
                        helper = rightSmall(traveler);
                    } else if (differenceSize(traveler.getLeft()) > 0) {
                        helper = rightBig(traveler);
                    }
                }
                if (helper.getParent() != null) {
                    traveler = helper.getParent();
                } else return;
            }
        }
    }

    private int differenceSize(Node<T> node) {
        int left, right;
        if (node.getLeft() == null && node.getRight() == null) {
            left = 0;
            right = 0;
        } else if (node.getLeft() != null && node.getRight() == null) {
            left = node.getLeft().getSizeTree();
            right = 0;
        } else if (node.getLeft() == null && node.getRight() != null) {
            left = 0;
            right = node.getRight().getSizeTree();
        } else {
            left = node.getLeft().getSizeTree();
            right = node.getRight().getSizeTree();
        }
        return (right - left);
    }

    private Node<T> leftSmall(Node<T> node) {
        Node<T> a = node;
        Node<T> b = node.getRight();
        smallHelpAB(a, b);
        a.setParent(b);
        a.setRight(b.getLeft());
        b.setLeft(a);
        if (a.getRight() != null) {
            a.getRight().setParent(a);
        }
        sizeTree(a);
        sizeTree(b);
        return b;
    }

    private Node<T> rightSmall(Node<T> node) {
        Node<T> a = node;
        Node<T> b = node.getLeft();
        smallHelpAB(a, b);
        a.setParent(b);
        a.setLeft(b.getRight());
        b.setRight(a);
        if (a.getLeft() != null) {
            a.getLeft().setParent(a);
        }
        sizeTree(a);
        sizeTree(b);
        return b;
    }

    private Node<T> leftBig(Node<T> node) {
        rightSmall(node.getRight());
        return leftSmall(node);
    }

    private Node<T> rightBig(Node<T> node) {
        leftSmall(node.getLeft());
        return rightSmall(node);
    }

    private void sizeTree(Node<T> node) {
        int left, right;
        if (node.getLeft() == null && node.getRight() == null) {
            left = 0;
            right = 0;
        } else if (node.getLeft() != null && node.getRight() == null) {
            left = node.getLeft().getSizeTree();
            right = 0;
        } else if (node.getLeft() == null && node.getRight() != null) {
            left = 0;
            right = node.getRight().getSizeTree();
        } else {
            left = node.getLeft().getSizeTree();
            right = node.getRight().getSizeTree();
        }
        node.setSizeTree(Math.max(left, right) + 1);
    }

    private void smallHelpAB(Node<T> a, Node<T> b) {
        if (a == this.first) {
            this.first = b;
            b.setParent(null);
        } else {
            b.setParent(a.getParent());
            if (a.getParent().getLeft() == a) {
                a.getParent().setLeft(b);
            } else {
                a.getParent().setRight(b);
            }
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------
    private void insertNode(Node delNode, Node nodeNext, Node bal) {//Вставка элемента вместо существующего
        nodeNext.setSizeTree(delNode.getSizeTree());
        nodeNext.setParent(delNode.getParent());
        nodeNext.setLeft(delNode.getLeft());
        nodeNext.setRight(delNode.getRight());
        if (delNode.getLeft() != null) {
            delNode.getLeft().setParent(nodeNext);
        }
        if (delNode.getRight() != null) {
            delNode.getRight().setParent(nodeNext);
        }
        if (delNode.getParent() != null) {
            if (delNode.getParent().getLeft() == delNode) {
                delNode.getParent().setLeft(nodeNext);
            }
            if (delNode.getParent().getRight() == delNode) {
                delNode.getParent().setRight(nodeNext);
            }
        } else {
            this.first = nodeNext;
        }
        sizeTree(nodeNext);
        ballans(bal);
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------
    private Node<T> getNode(int e) {
        Node<T> node = this.first;
        while (true) {
            if (node.getID() == e) {
                return node;
            }
            if (e < node.getID()) {
                if (node.getLeft() == null) {
                    return null;
                }
                node = node.getLeft();
            }
            if (e > node.getID()) {
                if (node.getRight() == null) {
                    return null;
                }
                node = node.getRight();
            }
        }
    }

    private Node<T> next(Node<T> node) {//Следующий элемент
        if (node.getRight() == null) {//Проверка на существование правого потомка
            while (true) {//Цикл подъема
                if (node.getParent() == null) {//Если дошли до родительского возвращаем Null
                    return null;//Возврат Null
                } else {//Если родительский элемент существует
                    if (node.equals(node.getParent().getLeft())) {//Проверяем, если наш элемент левый
                        return node.getParent();//Возвращаем родительский элемент
                    } else {//Если элемент правый
                        node = node.getParent();//Поднимаемся пока наш элемент не правый или родитель Null
                    }
                }
            }
        } else {//Если существует правый потомок
            return min(node.getRight());//Возвращаем минимум из правого поддерева
        }
    }

    private Node<T> min(Node Rode) {
        Node<T> node = Rode;
        while (node.getLeft() != null) {
            node = node.getLeft();
        }
        return node;
    }

    //------------------------------------
    public void visual(Node<T> node) {
        if (node.getLeft() != null) {
            visual(node.getLeft());
        }
        testNode(node);
        if (node.getRight() != null) {
            visual(node.getRight());
        }
    }

    public void testNode(Node<T> node) {
        if (node != null) {
            System.out.println("Tree.AVLTree.Node: " + node.getID());
            if (node.getParent() != null) {
                System.out.println("Parent: " + node.getParent().getID());
            }
            if (node.getLeft() != null) {
                System.out.println("Left: " + node.getLeft().getID());
            }
            if (node.getRight() != null) {
                System.out.println("Right: " + node.getRight().getID());
            }
            System.out.println("Size: " + node.getSizeTree());
            System.out.println();
        } else {
            System.out.println("Don't node");
            System.out.println();
        }
    }
}

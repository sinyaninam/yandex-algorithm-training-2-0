/*
 * E. Логическое дерево
 * 
 * // Условие
 * Ограничение времени 	2 секунды
 * Ограничение памяти 	256Mb
 * Ввод 	стандартный ввод или input.txt
 * Вывод 	стандартный вывод или output.txt
 * 
 * Рассмотрим разновидность двоичного дерева, которую мы назовем логическим деревом. 
 * В этом дереве каждый уровень полностью заполнен, за исключением, возможно, последнего 
 * (самого глубокого) уровня. При этом все вершины последнего уровня находятся максимально
 * слева. Дополнительно, каждая вершина дерева имеет ноль или двоих детей.
 * 
 * Каждая вершина дерева имеет связанное с ней логическое значение (1 или 0). Кроме этого,
 * каждая внутренняя вершина имеет связанную с ней логическую операцию (И или ИЛИ). Значение
 * вершины с операцией И — это логическое И значений её детей. Аналогично, значение вершины
 * с операцией ИЛИ — это логическое ИЛИ значений её детей. Значения всех листьев задаются
 * во входном файле, поэтому значения всех вершин дерева могут быть найдены.
 * 
 * Наибольший интерес для нас представляет корень дерева. Мы хотим, чтобы он имел заданное
 * логическое значение v, которое может отличаться от текущего. К счастью, мы можем изменять 
 * логические операции некоторых внутренних вершин (заменить И на ИЛИ и наоборот).
 * 
 * Дано описание логического дерева и набор вершин, операции в которых могут быть изменены.
 * Найдите наименьшее количество вершин, которые следует изменить, чтобы корень дерева принял
 * заданное значение v. Если это невозможно, то выведите строку “IMPOSSIBLE” (без кавычек).
 * 
 * // Формат ввода
 * В первой строке входного файла находятся два числа n и v (1 ≤ n ≤ 10 000, 0 ≤ v ≤ 1) —
 * — количество вершин в дереве и требуемое значение в корне соответственно. Поскольку все
 * вершины имеют ноль или двоих детей, то n нечётно. Следующие n строк описывают вершины дерева.
 * Вершины нумеруются от 1 до n.
 * 
 * Первые (n-1)/2 строк описывают внутренние вершины. Каждая из них содержит два числа —
 * — g и c, которые принимают значение либо 0, либо 1. Если g=1, то вершина представляет
 * логическую операцию И, иначе она представляет логическую операцию ИЛИ. Если c=1, то 
 * операция в вершине может быть изменена, иначе нет. Внутренняя вершина с номером i
 * имеет детей 2i и 2i+1.
 * 
 * Следующие (n+1)/2 строк описывают листья. 
 * Каждая строка содержит одно число 0 или 1 — значение листа.
 * 
 * // Формат вывода
 * В выходной файл выведите ответ на задачу.
 * 
 * // Примеры
 * 9 1
 * 1 0
 * 1 1
 * 1 1
 * 0 0
 * 1
 * 0
 * 1
 * 0
 * 1
 * 
 * 1
 * 
 * 5 0
 * 1 1
 * 0 0
 * 1
 * 1
 * 0
 * 
 * IMPOSSIBLE
 * 
 */

import java.util.*;
import java.util.function.Consumer;

public class Test {

    //rewrite without using indexes in Node (use index in postorder instead of Node)

    static BinaryTree tree;

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        String[] input = scanner.nextLine().split(" ");
        int treeSize = Integer.parseInt(input[0]), targetValue = Integer.parseInt(input[1]);

        tree = new BinaryTree(treeSize);
        for (int i = 1; i <= (treeSize - 1) / 2; i++) {
            input = scanner.nextLine().split(" ");
            int logicalOperator = Integer.parseInt(input[0]), canSwitchOperator = Integer.parseInt(input[1]);
            tree.addNode(new BinaryTree.InnerNode(i, logicalOperator, canSwitchOperator));
        }
        for (int i = (treeSize - 1) / 2 + 1; i <= treeSize; i++) {
            int value = Integer.parseInt(scanner.nextLine());
            tree.addNode(new BinaryTree.Node(i, value));
        }

        System.out.println();
        tree.postorder(node -> {
            if (node instanceof BinaryTree.InnerNode) {
                BinaryTree.InnerNode currentNode = (BinaryTree.InnerNode) node;
                currentNode.value = (currentNode.operator == BinaryTree.InnerNode.LogicalOperator.AND)
                        ? tree.getLeftChild(currentNode).value && tree.getRightChild(currentNode).value
                        : tree.getLeftChild(currentNode).value || tree.getRightChild(currentNode).value;

                currentNode.minNumOfSwitchesToTrue = getMinNumOfSwitchesToTrue(currentNode.operator, currentNode);
                currentNode.minNumOfSwitchesToFalse = getMinNumOfSwitchesToFalse(currentNode.operator, currentNode);
                if (currentNode.canSwitchOperator) {
                    BinaryTree.InnerNode.LogicalOperator newOperator = (currentNode.operator == BinaryTree.InnerNode.LogicalOperator.AND) 
                            ? BinaryTree.InnerNode.LogicalOperator.OR
                            : BinaryTree.InnerNode.LogicalOperator.AND;
                    currentNode.minNumOfSwitchesToTrue =
                            Math.min(currentNode.minNumOfSwitchesToTrue, incrementWithoutOverflow(getMinNumOfSwitchesToTrue(newOperator, currentNode)));
                    currentNode.minNumOfSwitchesToFalse =
                            Math.min(currentNode.minNumOfSwitchesToFalse, incrementWithoutOverflow(getMinNumOfSwitchesToFalse(newOperator, currentNode)));
                }
            } else {
                node.minNumOfSwitchesToFalse = (!node.value) ? 0 : Integer.MAX_VALUE;
                node.minNumOfSwitchesToTrue = (node.value) ? 0 : Integer.MAX_VALUE;
            }
            System.out.println("id " + node.index + " value " + node.value);
            System.out.println("minToTrue " + node.minNumOfSwitchesToTrue + " minToFalse " + node.minNumOfSwitchesToFalse);
        });
    }

    static int getMinNumOfSwitchesToFalse(BinaryTree.InnerNode.LogicalOperator operator, BinaryTree.Node node) {
        List<Integer> listOfSwitches = new ArrayList<>();
        switch(operator) {
            case AND:
                Collections.addAll(listOfSwitches,
                        addWithoutOverflow(tree.getLeftChild(node).minNumOfSwitchesToFalse, tree.getRightChild(node).minNumOfSwitchesToFalse),
                        addWithoutOverflow(tree.getLeftChild(node).minNumOfSwitchesToTrue, tree.getRightChild(node).minNumOfSwitchesToFalse),
                        addWithoutOverflow(tree.getLeftChild(node).minNumOfSwitchesToFalse, tree.getRightChild(node).minNumOfSwitchesToTrue));
                break;
            case OR:
                listOfSwitches.add(
                        addWithoutOverflow(tree.getLeftChild(node).minNumOfSwitchesToFalse, tree.getRightChild(node).minNumOfSwitchesToFalse));
                break;
            default: throw new IllegalArgumentException("only AND and OR are supported");
        }
        return listOfSwitches.stream().mapToInt(Integer::intValue).min().getAsInt();
    }

    static int getMinNumOfSwitchesToTrue(BinaryTree.InnerNode.LogicalOperator operator, BinaryTree.Node node) {
        List<Integer> listOfSwitches = new ArrayList<>();
        switch(operator) {
            case AND:
                listOfSwitches.add(
                        addWithoutOverflow(tree.getLeftChild(node).minNumOfSwitchesToTrue, tree.getRightChild(node).minNumOfSwitchesToTrue));
                break;
            case OR:
                Collections.addAll(listOfSwitches,
                        addWithoutOverflow(tree.getLeftChild(node).minNumOfSwitchesToTrue, tree.getRightChild(node).minNumOfSwitchesToTrue),
                        addWithoutOverflow(tree.getLeftChild(node).minNumOfSwitchesToTrue, tree.getRightChild(node).minNumOfSwitchesToFalse),
                        addWithoutOverflow(tree.getLeftChild(node).minNumOfSwitchesToFalse, tree.getRightChild(node).minNumOfSwitchesToTrue));
                break;
            default: throw new IllegalArgumentException("only AND and OR are supported");
        }
        return listOfSwitches.stream().mapToInt(Integer::intValue).min().getAsInt();
    }

    static int addWithoutOverflow(int x, int y) {
        if (x == Integer.MAX_VALUE || y == Integer.MAX_VALUE)
            return Integer.MAX_VALUE;
        return x + y;
    }

    static int incrementWithoutOverflow(int x) {
        if (x == Integer.MAX_VALUE)
            return Integer.MAX_VALUE;
        return x + 1;
    }
}

class BinaryTree {

    Node[] nodes;

    BinaryTree(int size) {
        nodes = new Node[size + 1];
    }

    static class Node {

        int index;

        boolean value;
        int minNumOfSwitchesToTrue;
        int minNumOfSwitchesToFalse;

        Node(int index, int value) {
            this.index = index;
            this.value = (value == 1);
        }
    }

    static class InnerNode extends Node {

        enum LogicalOperator {
            OR, AND;
        }

        LogicalOperator operator;
        boolean canSwitchOperator;

        InnerNode(int index, int operator, int canSwitchOperator) {
            super(index, 0);
            this.operator = (operator == 1) ? LogicalOperator.AND : LogicalOperator.OR;
            this.canSwitchOperator = (canSwitchOperator == 1);
        }
    }

    void addNode(Node node) {
        nodes[node.index] = node;
    }

    Node getParent(Node node) {
        int parentIndex = node.index / 2;
        return (parentIndex != 0) ? nodes[parentIndex] : null;
    }

    Node getLeftChild(Node node) {
        int leftChildIndex = node.index * 2;
        return (leftChildIndex < nodes.length) ? nodes[leftChildIndex] : null;
    }

    Node getRightChild(Node node) {
        int rightChildIndex = node.index * 2 + 1;
        return (rightChildIndex < nodes.length) ? nodes[rightChildIndex] : null;
    }

    void postorder(Consumer<Node> consumer) {
        Node root = nodes[1];
        postorder(root, consumer);
    }

    void postorder(Node node, Consumer<Node> consumer) {
        if (getLeftChild(node) != null)
            postorder(getLeftChild(node), consumer);
        if (getRightChild(node) != null)
            postorder(getRightChild(node), consumer);
        consumer.accept(node);
    }
}
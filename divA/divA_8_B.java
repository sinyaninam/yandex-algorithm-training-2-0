/*
 * B. Города-2
 * 
 * // Условие
 * Ограничение времени 	2 секунды
 * Ограничение памяти 	256Mb
 * Ввод 	стандартный ввод или input.txt
 * Вывод 	стандартный вывод или output.txt
 * 
 * Дорожная сеть в Байтландии обладает следующими свойствами:
 * 
 *    - Неориентированность: На всех дорогах движение является двусторонним.
 *    - Связность: Из любого города Байтландии можно проехать в любой другой по сети дорог.
 *    - Отсутствие циклов: Между любыми двумя городами Байтландии существует ровно один путь.
 * 
 * Назовём суммарной удалённостью города сумму расстояний от него до других городов. 
 * Требуется найти все города с минимальной суммарной удалённостью.
 * 
 * // Формат ввода
 * Первая строка входа содержит целое число N — количество городов (1 ≤ N ≤ 10^5). 
 * Каждая из последующих N-1 строк содержит по два целых числа — номера городов, соединённых
 * очередной дорогой. Города занумерованы последовательными целыми числами от 1 до N.
 * 
 * // Формат вывода
 * Выведите в одной строке через пробел минимальную суммарную удалённость, количество
 * городов, для которых она достигается, а также список этих городов, отсортированный 
 * по возрастанию номеров. 
 * 
 * // Примеры
 * 2
 * 1 2
 * 
 * 1 2 1 2
 * 
 */

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class Test {

    static class Tree {

        Node root;
        Map<Integer, Node> quickAccessMap = new HashMap<>();

        class Node {
            List<Node> children = new ArrayList<>();
            Node parent;
            CityData data;

            Node(Node parent, CityData data) {
                this.parent = parent;
                this.data = data;
            }

            void addChild(Node node) {
                children.add(node);
            }
        }

        class Pair {
            Node node;
            int childIndex;

            Pair(Node node, int childIndex) {
                this.node = node;
                this.childIndex = childIndex;
            }
        }

        void addNode(int firstCity, int secondCity) {
            if (!quickAccessMap.containsKey(firstCity) && !quickAccessMap.containsKey(secondCity))
                addRoot(firstCity);

            int parentCity = (quickAccessMap.containsKey(firstCity)) ? firstCity : secondCity;
            int childCity = (quickAccessMap.containsKey(firstCity)) ? secondCity : firstCity;

            Node parentNode = quickAccessMap.get(parentCity);
            Node newNode = new Node(parentNode, new CityData(childCity));
            parentNode.addChild(newNode);
            quickAccessMap.put(childCity, newNode);
        }

        void addRoot(int city) {
            Node root = new Node(null, new CityData(city));
            this.root = root;
            quickAccessMap.put(city, root);
        }

        void preorder(Consumer<Node> consumer) {
            ArrayDeque<Pair> deque = new ArrayDeque<>();
            deque.add(new Pair(root, -1));

            while (!deque.isEmpty()) {
                Node currentNode = deque.getLast().node;
                consumer.accept(currentNode);

                if (currentNode.children.size() > 0) {
                    deque.add(new Pair(currentNode.children.get(0), 0));
                } else {
                    int lastChildIndex; Node parentNode;
                    do {
                        lastChildIndex = deque.getLast().childIndex;
                        deque.removeLast();
                        parentNode = (!deque.isEmpty()) ? deque.getLast().node : null;
                    } while (parentNode != null && parentNode.children.size() == lastChildIndex + 1);

                    if (parentNode != null)
                        deque.add(new Pair(parentNode.children.get(lastChildIndex + 1), lastChildIndex + 1));
                }
            }
        }

        void postorder(Consumer<Node> consumer) {
            ArrayDeque<Pair> deque = new ArrayDeque<>();
            deque.add(new Pair(root, -1));

            while (!deque.isEmpty()) {
                Node currentNode = deque.getLast().node;

                if (currentNode.children.size() > 0) {
                    deque.add(new Pair(currentNode.children.get(0), 0));
                } else {
                    consumer.accept(currentNode);

                    int lastChildIndex = deque.getLast().childIndex;
                    deque.removeLast();
                    Node parentNode = deque.getLast().node;
                    while (parentNode != null && parentNode.children.size() == lastChildIndex + 1) {
                        consumer.accept(parentNode);
                        lastChildIndex = deque.getLast().childIndex;
                        deque.removeLast();
                        parentNode = (!deque.isEmpty()) ? deque.getLast().node : null;
                    }
                    if (parentNode != null)
                        deque.add(new Pair(parentNode.children.get(lastChildIndex + 1), lastChildIndex + 1));
                }
            }
        }
    }

    static class CityData {

        int id;
        int sumFromChildrenBranches;
        int totalSum;
        int numOfChildrenIncluded;

        CityData(int id) {
            this.id = id;
        }
    }

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        int numOfCities = Integer.parseInt(scanner.nextLine());
        Tree tree = new Tree();
        for (int i = 0; i < numOfCities - 1; i++) {
            int[] input = Arrays.stream(scanner.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            tree.addNode(input[0], input[1]);
        }

        tree.postorder(node -> {
            int sum = 0, num = 0;
            for (Tree.Node child : node.children) {
                sum += child.data.sumFromChildrenBranches;
                num += child.data.numOfChildrenIncluded;
            }
            node.data.sumFromChildrenBranches = sum + num;
            node.data.numOfChildrenIncluded = num + 1;
        });

        AtomicInteger min = new AtomicInteger(Integer.MAX_VALUE);
        tree.preorder(node -> {
            //System.out.println(node.data.id + " : num " + node.data.numOfChildrenIncluded + " sum " + node.data.sumFromChildrenBranches);
            node.data.totalSum = (node.parent == null)
                    ? node.data.sumFromChildrenBranches
                    : (node.parent.data.totalSum - node.data.numOfChildrenIncluded) + (tree.root.data.numOfChildrenIncluded - node.data.numOfChildrenIncluded);
            if (node.data.totalSum < min.intValue())
                min.set(node.data.totalSum);
            //System.out.println(node.data.totalSum);
        });

        ArrayList<Integer> result = new ArrayList<>();
        tree.preorder(node -> {
            if (node.data.totalSum == min.intValue())
                result.add(node.data.id);
        });
        System.out.println(min + " " + result);
    }
}
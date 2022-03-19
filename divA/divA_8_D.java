/*
 * D. Бюрократия
 * 
 * // Условие
 * Ограничение времени 	3 секунды
 * Ограничение памяти 	64Mb
 * Ввод 	стандартный ввод или input.txt
 * Вывод 	стандартный вывод или output.txt
 * 
 * Мирко стал генеральным директором крупной корпорации. В компании работает N человек,
 * пронумерованных от 1 до N, Мирко имеет номер 1. У всех кроме Мирко есть начальник.
 * Начальник может иметь несколько подчинённых, но не более одного своего начальника.
 * 
 * Когда Мирко получает задание от инвесторов, он передаёт его своему подчинённому 
 * с наименьшим номером. Этот подчинённый также передаёт его своему подчинённому 
 * с наименьшим номером, и так далее, пока задание не перейдёт несчастливому работнику
 * без подчинённых, который должен сделать задание.
 * 
 * Этот работник получает 1 монету, его начальник получает 2 монеты, начальник этого
 * начальника получает 3 и так далее. Потом тот, кто на самом деле сделал работу, осознаёт,
 * насколько эта капиталистическая система несправедлива и увольняется с работы.
 * 
 * Мирко получает задания до тех пор, пока в корпорации не останется всего один сотрудник —
 * — сам Мирко. Тогда он выполняет это задание, получает 1 монету и уходит из корпорации.
 * Ему стало интересно, сколько всего монет получил каждый бывший сотрудник. 
 * Помогите ему с этим.
 * 
 * // Формат ввода
 * Первая строка содержит одно натуральное число N (1 ≤ N ≤ 2·10^5) — число сотрудников
 * компании. Следующая строка содержит N -1 чисел a2, a3, ... an (1 ≤ ai < i), 
 * a i — номер начальника i-го сотрудника.
 * 
 * // Формат вывода
 * Выведите N чисел, i-е число должно означать, сколько монет получил i-й сотрудник
 * 
 * // Примеры
 * 3
 * 1 1
 * 
 * 5 1 1 
 * 
 * 5
 * 1 2 2 4
 * 
 * 13 8 1 3 1 
 * 
 */

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Test {

    static class Tree {

        Node root = new Node(null, new EmployeeData(1));
        Map<Integer, Node> quickAccessMap = new HashMap<Integer, Node>() {{ put(1, root); }};

        class Node {
            List<Node> children = new ArrayList<>();
            Node parent;
            EmployeeData data;

            Node(Node parent, EmployeeData data) {
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

        void addNode(int subordinate, int boss) {
            Node parent = quickAccessMap.get(boss);
            Node child = new Node(parent, new EmployeeData(subordinate));
            parent.addChild(child);
            quickAccessMap.put(subordinate, child);
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

    static class EmployeeData {

        int id;
        int sumFromChildrenBranches;
        int numOfChildrenIncluded;

        EmployeeData(int id) {
            this.id = id;
        }

    }

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();

        Tree tree = new Tree();
        AtomicInteger employeeID = new AtomicInteger(1);
        Arrays.stream(scanner.nextLine().split(" ")).mapToInt(Integer::parseInt).forEach(boss -> {
            int subordinate = employeeID.incrementAndGet();
            tree.addNode(subordinate, boss);
        });

        System.out.println();
        tree.postorder(node -> {
            int numOfChildren = 0, sumFromChildBranches = 0;
            for (Tree.Node child : node.children) {
                numOfChildren += child.data.numOfChildrenIncluded;
                sumFromChildBranches += child.data.sumFromChildrenBranches;
            }
            node.data.sumFromChildrenBranches = sumFromChildBranches + numOfChildren + 1;
            node.data.numOfChildrenIncluded = numOfChildren + 1;
            //System.out.println("id " + node.data.id + " num " + node.data.numOfChildrenIncluded + " sum " + node.data.sumFromChildrenBranches);
        });

        tree.quickAccessMap.keySet().stream().sorted().forEach(employee -> System.out.println(tree.quickAccessMap.get(employee).data.sumFromChildrenBranches));
    }
}
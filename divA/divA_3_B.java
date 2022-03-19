/*
 * B. Мультиграф
 * 
 * // Условие
 * Ограничение времени 	1 секунда
 * Ограничение памяти 	64Mb
 * Ввод 	стандартный ввод или multigraph.in
 * Вывод 	стандартный вывод или multigraph.out
 * 
 * Дан неориентированный невзвешенный граф. В графе возможны петли и кратные рёбра. 
 * Постройте такой новый граф без петель и кратных рёбер, что для любых двух вершин в нём 
 * расстояние равно расстоянию в исходном графе. Если вершины не связны, расстояние между
 * ними бесконечность.
 * 
 * // Формат ввода
 * На первой строке число вершин n и число рёбер m (1 ≤ n, m ≤ 100000). Следующие m строк 
 * содержат пары чисел от 1 до n – рёбра графа.
 * 
 * // Формат вывода
 * Новый граф в таком же формате. Рёбра можно выводить в произвольном формате. 
 * 
 * // Примеры
 * 3 5
 * 1 1
 * 1 3
 * 2 1
 * 1 2
 * 2 3
 * 
 * 3 3
 * 1 2
 * 1 3
 * 2 3
 * 
 * // Решение
 * Всё, что требуется в задаче - удалить кратные ребра и петли. Петли можно предварительно
 * отфильтровать, проверяя равенство первой и второй вершины, ребра следует хранить как 
 * неупорядоченную пару в set'е, проверяя коллекцию на наличие в ней существующей пары 
 * независимо от порядка вершин, либо как упорядоченную, но с отсортированным порядком вершин.
 * 
 */

import java.util.*;
import java.util.stream.Collectors;

public class Test {

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        String[] input = scanner.nextLine().split(" ");
        int numOfVertices = Integer.parseInt(input[0]), numOfEdges = Integer.parseInt(input[1]);

        Set<UnorderedPair> set = new HashSet<>();
        for (int i = 0; i < numOfEdges; i++) {
            input = scanner.nextLine().split(" ");
            int vert1 = Integer.parseInt(input[0]), vert2 = Integer.parseInt(input[1]);
            if (vert1 != vert2)
                set.add(new UnorderedPair(vert1, vert2));
        }
        
        System.out.println(numOfVertices  + " " + set.size());
        set.forEach(System.out::println);
    }

    public static class UnorderedPair {
        private final Set<Integer> set;

        public UnorderedPair(int a, int b) {
            set = new HashSet<>();
            set.add(a);
            set.add(b);
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UnorderedPair pair = (UnorderedPair) o;
            return this.set.equals(pair.set);
        }

        public int hashCode() {
            return set.hashCode();
        }

        public String toString() {
            return set.stream().map(Object::toString).collect(Collectors.joining(" "));
        }
    }
}
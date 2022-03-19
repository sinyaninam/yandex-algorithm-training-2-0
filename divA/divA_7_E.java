/*
 * E. Отрезки на прямой возвращаются
 * 
 * // Условие
 * Ограничение времени 	1 секунда
 * Ограничение памяти 	64Mb
 * Ввод 	стандартный ввод или input.txt
 * Вывод 	стандартный вывод или output.txt
 * 
 * На прямой задано N попарно различных отрезков [ai, bi] (i = 1, 2, ..., N, ai < bi). 
 * Будем говорить, что отрезок номер i непосредственно содержится в отрезке номер j (i ≠ j),
 * если:
 * 
 * 1. он полностью принадлежит j-му (то есть aj ≤ ai и bi ≤ bj), 
 * 2. среди заданных N отрезков не найдётся такого отрезка (с номером k), 
 *    что i-й отрезок принадлежит k-му и k-й принадлежит j-му (здесь i, j и k - различные числа).
 * 
 * Ваша задача - для каждого из данных отрезков найти тот, в котором он непосредственно
 * содержится, либо сообщить, что таких нет. Если данный отрезок непосредственно содержится
 * сразу в нескольких - подходит любой из них.
 * 
 * // Формат ввода
 * Сначала вводится целое число N (1 ≤ N ≤ 100000). Далее идут N пар целых чисел
 * ai, bi (-10^9 ≤ ai < bi ≤ 10^9).
 * 
 * // Формат вывода
 * Выведите N чисел. Число номер i должно быть равно номеру отрезка, в котором
 * непосредственно содержится отрезок номер i, либо 0 - если такого не существует.
 * 
 * Если существует несколько решений, выведите любое.
 * 
 * // Примеры
 * 4
 * 2 3
 * 0 4
 * 1 6
 * 0 5
 * 
 * 3 4 0 0
 * 
 */

import java.util.*;
import java.util.function.BiConsumer;

public class Test {

    static class SegmentsMap {

        TreeMap<Integer, TreeMap<Integer, Integer>> segments = new TreeMap<>();

        void put(int[] segment, int id) {
            int start = segment[0], end = segment[1];
            if (!segments.containsKey(start)) {
                TreeMap<Integer, Integer> mapOfEnds = new TreeMap<>(Collections.reverseOrder());
                mapOfEnds.put(end, id);
                segments.put(start, mapOfEnds);
            }
            else
                segments.get(start).put(end, id);
        }

        void forEach(BiConsumer<Integer, Integer> consumer) {
            segments.forEach((start, mapOfEnds) -> mapOfEnds.forEach(consumer));
        }
    }

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        int segmentsNum = Integer.parseInt(scanner.nextLine());

        SegmentsMap segmentsMap = new SegmentsMap();
        for (int i = 1; i <= segmentsNum; i++) {
            int[] segment = Arrays.stream(scanner.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            segmentsMap.put(segment, i);
        }

        Map<Integer, Integer> resultMap = new HashMap<>();
        TreeMap<Integer, Integer> activeSegments = new TreeMap<>();
        segmentsMap.forEach((end, id) -> {
            Integer biggerThanCurrentEnd = activeSegments.higherKey(end - 1);
            if (biggerThanCurrentEnd == null)
                resultMap.put(id, 0);
            else
                resultMap.put(id, activeSegments.get(biggerThanCurrentEnd));
            activeSegments.put(end, id);
        });
        System.out.println(resultMap);
    }
}
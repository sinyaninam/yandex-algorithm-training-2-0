/*
 * B. Покраска забора
 * 
 * // Условие
 * Ограничение времени 	2 секунды
 * Ограничение памяти 	64Mb
 * Ввод 	стандартный ввод или input.txt
 * Вывод 	стандартный вывод или output.txt
 * 
 * Том Сойер уговорил n своих друзей помочь ему в нелегком деле покраски забора,
 * окружающего дом тетушки Полли. Забор представляет собой k последовательных досок,
 * пронумерованных от 1 до k, причем после k-й доски опять идет первая.
 * 
 * Друзья Тома очень привередливы, i-й друг согласен участвовать в покраске только в том 
 * случае, если ему дадут покрасить участок из ровно ai последовательных досок.
 * 
 * Кисточка у Тома только одна, поэтому друзья будут красить по очереди и сразу весь 
 * отведенный им отрезок. Тому остается лишь выбрать порядок, в котором приглашать друзей,
 * а также выбрать для каждого желаемое количество последовательных досок.
 * 
 * При этом каждый из друзей Тома готов красить как еще неокрашенную доску забора, так и
 * доску, которую уже покрасил один из его предшественников. Тем не менее, друзья получают
 * больше удовольствия от покраски неокрашенной доски. Том хочет выбрать число x и 
 * распределить отрезки забора для покраски таким образом, чтобы каждый из его друзей
 * покрасил хотя бы x неокрашенных досок. Том очень любит своих друзей и хочет, чтобы
 * каждый из них получил от процесса покраски забора максимальное удовольствие, поэтому
 * он пытается максимизировать x.
 * 
 * Помогите Тому понять, сколько радости он сможет доставить своим друзьям.
 * 
 * // Формат ввода
 * Первая строка входного файла содержит два целых числа n (1 ≤ n ≤ 10^5) и k (1 ≤ k ≤ 10^9).
 * Следующая строка содержит n целых чисел - значения ai (1 ≤ ai ≤ k).
 * 
 * // Формат вывода
 * Выведите одно число - максимальное возможное значение x.
 * 
 * // Примеры
 * 2 100
 * 5 10
 * 
 * 5
 * 
 * 4 10
 * 7 8 3 5
 * 
 * 2
 * 
 * // Примечания
 * В первом примере x=5, так как один из друзей просто не хочет красить больше пяти досок.
 * Он придет первым, покрасит свои пять, после чего еще 10 неокрашенных досок достанется
 * второму другу Тома. Оставшиеся 85 досок Тому придется красить самому.
 * 
 * Во втором примере достичь x=2 можно, например, так. Сначала третий друг красит доски 
 * с 4 по 6 (3 неокрашенных доски). Затем четвертый друг красит доски с 1 по 5 (3 неокрашенных
 * доски). Затем второй друг красит доски с 1 по 8 (2 неокрашенных доски). Наконец, первый
 * друг красит доски с 6 по 10 и с 1 по 2 (2 неокрашенных доски, заметим, что забор идет
 * по циклу и эти доски образуют последовательный отрезок).
 * 
 */

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Test {

    static class GapStats {

        int shortestBoardLength;
        int numOfGaps;
        TreeMap<Integer, Stats> gapStats = new TreeMap<>();

        class Stats {
            int totalCount;
            int totalSum;

            Stats(int totalCount, int totalSum) {
                this.totalCount = totalCount;
                this.totalSum = totalSum;
            }
        }

        GapStats(List<Integer> list) {
            shortestBoardLength = list.get(0);
            numOfGaps = list.size() - 1;

            TreeMap<Integer, Integer> gaps = new TreeMap<>();
            for (int i = 1; i < list.size(); i++) {
                int currentGap = list.get(i) - list.get(i-1);
                if (!gaps.containsKey(currentGap))
                    gaps.put(currentGap, 1);
                else
                    gaps.put(currentGap, gaps.get(currentGap) + 1);
            }

            gapStats.put(gaps.lastKey(), new Stats(gaps.get(gaps.lastKey()), gaps.lastKey() * gaps.get(gaps.lastKey())));
            int prev = gaps.lastKey();
            for (Integer key : gaps.descendingKeySet()) {
                if (key.equals(gaps.lastKey()))
                    continue;
                int newCount = gapStats.get(prev).totalCount + gaps.get(key);
                int newSum = gapStats.get(prev).totalSum + gaps.get(key) * key;
                gapStats.put(key, new Stats(newCount, newSum));
                prev = key;
            }
        }

        int getTotalBoardsDrawn(int targetGap) {
            Integer higherThanTargetGap = gapStats.higherKey(targetGap);
            if (higherThanTargetGap == null)
                return shortestBoardLength + numOfGaps * targetGap;
            else
                return shortestBoardLength + (numOfGaps - gapStats.get(higherThanTargetGap).totalCount) * targetGap + gapStats.get(higherThanTargetGap).totalSum;
        }
    }

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        int boardsNum = Integer.parseInt(scanner.nextLine().split(" ")[1]);
        List<Integer> boards = Arrays.stream(scanner.nextLine().split(" ")).map(Integer::parseInt).sorted().collect(Collectors.toList());
        GapStats stats = new GapStats(boards);
        System.out.println(rightmostBS(0, boards.get(0), gap -> stats.getTotalBoardsDrawn(gap) <= boardsNum));
    }

    static int rightmostBS(int l, int r, Predicate<Integer> predicate) {
        while (l < r) {
            int mid = (l + r + 1) / 2;
            if (predicate.test(mid))
                l = mid;
            else
                r = mid - 1;
        }
        return r;
    }
}
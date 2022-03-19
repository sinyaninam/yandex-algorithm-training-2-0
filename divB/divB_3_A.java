/*
 * A. Количество совпадающих
 * 
 * // Условие
 * Ограничение времени 	3 секунды
 * Ограничение памяти 	64Mb
 * Ввод 	стандартный ввод или input.txt
 * Вывод 	стандартный вывод или output.txt
 * 
 * Даны два списка чисел, которые могут содержать до 100000 чисел каждый. Посчитайте,
 * сколько чисел содержится одновременно как в первом списке, так и во втором.
 * 
 * // Формат ввода
 * Вводятся два списка чисел. Все числа каждого списка находятся на отдельной строке.
 * 
 * // Формат вывода
 * Выведите ответ на задачу. 
 * 
 * // Примеры
 * 1 3 2
 * 4 3 2
 * 
 * 2
 * 
 * 1 2 6 4 5 7
 * 10 2 3 4 8
 * 
 * 2
 * 
 * 1 7 3 8 10 2 5
 * 6 5 2 8 4 3 7
 * 
 * 5
 * 
 */

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


public class Test {

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Long> first = Arrays.stream(scanner.nextLine().split(" "))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        Map<String, Long> second = Arrays.stream(scanner.nextLine().split(" "))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        long result = first.entrySet().stream()
                .filter(entry -> second.containsKey(entry.getKey()))
                .mapToLong(entry -> Math.min(entry.getValue(), second.get(entry.getKey())))
                .sum();
        System.out.println(result);
    }
}
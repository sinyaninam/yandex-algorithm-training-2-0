/*
 * C. Уникальные элементы
 * 
 * // Условие
 * Ограничение времени 	3 секунды
 * Ограничение памяти 	64Mb
 * Ввод 	стандартный ввод или input.txt
 * Вывод 	стандартный вывод или output.txt
 * 
 * Дан список. Выведите те его элементы, которые встречаются в списке только один раз.
 * Элементы нужно выводить в том порядке, в котором они встречаются в списке.
 * 
 * // Формат ввода
 * Вводится список чисел. Все числа списка находятся на одной строке.
 * 
 * // Формат вывода
 * Выведите ответ на задачу. 
 * 
 * // Примеры
 * 1 2 2 3 3 3
 * 
 * 1 
 * 
 * 4 3 5 2 5 1 3 5
 * 
 * 4 2 1 
 * 
 */

import java.util.*;
import java.util.stream.Collectors;

public class Test {

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        List<String > input = Arrays.stream(scanner.nextLine().split(" ")).collect(Collectors.toList());

        Map<String, Boolean> elements = new HashMap<>();
        input.stream().forEach(e -> elements.put(e, (!elements.containsKey(e))));
        String result = input.stream()
                .filter(elements::get)
                .collect(Collectors.joining(" "));
        System.out.println(result);
    }
}
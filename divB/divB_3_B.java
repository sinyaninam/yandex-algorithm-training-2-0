/*
 * B. Встречалось ли число раньше
 * 
 * // Условие
 * Ограничение времени 	3 секунды
 * Ограничение памяти 	64Mb
 * Ввод 	стандартный ввод или input.txt
 * Вывод 	стандартный вывод или output.txt
 * 
 * Во входной строке записана последовательность чисел через пробел. Для каждого числа 
 * выведите слово YES (в отдельной строке), если это число ранее встречалось
 * в последовательности или NO, если не встречалось.
 * 
 * // Формат ввода
 * Вводится список чисел. Все числа списка находятся на одной строке.
 * 
 * // Формат вывода
 * Выведите ответ на задачу. 
 * 
 * // Примеры
 * 1 2 3 2 3 4
 * 
 * NO
 * NO
 * NO
 * YES
 * YES
 * NO
 * 
 */

import java.util.*;

public class Test {

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        Set<String> setOfNums = new HashSet<>();
        for (String s : scanner.nextLine().split(" ")) {
            System.out.println((setOfNums.contains(s) ? "YES" : "NO"));
            setOfNums.add(s);
        }
    }
}
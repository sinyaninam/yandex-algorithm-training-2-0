/*
 * A. Палиндром
 * 
 * // Условие
 * Ограничение времени 	1 секунда
 * Ограничение памяти 	64Mb
 * Ввод 	стандартный ввод или input.txt
 * Вывод 	стандартный вывод или output.txt
 * 
 * Палиндром - это строка, которая читается одинаково как справа налево, так и слева направо.
 * 
 * На вход программы поступает набор больших латинских букв (не обязательно различных).
 * Разрешается переставлять буквы, а также удалять некоторые буквы. Требуется из данных
 * букв по указанным правилам составить палиндром наибольшей длины, а если таких палиндромов
 * несколько, то выбрать первый из них в алфавитном порядке.
 * 
 * // Формат ввода
 * В первой строке входных данных содержится число N (1 ≤ N ≤ 100000). Во второй строке 
 * задается последовательность из N больших латинских букв (буквы записаны без пробелов).
 * 
 * // Формат вывода
 * В единственной строке выходных данных выдайте искомый палиндром. 
 * 
 * // Примеры
 * 3
 * AAB
 * 
 * ABA
 * 
 * 6
 * QAZQAZ
 * 
 * AQZZQA
 * 
 * 6
 * ABCDEF
 * 
 * A
 * 
 * // Решение
 * Нужно составить словарь, где ключом будет буква, а значением - её количество в наборе.
 * В палиндроме максимальной длины будет использовано чётное количество каждой из доступных
 * букв (по равному числу букв до и после центра). Если есть буквы с нечётным количеством,
 * то нужно взять ещё одну - ту из них, которая идёт в алфавите раньше, и поставить её в
 * центр, в этом случае для неё не потребуется пары. Все остальные нечётные остатки
 * будут отброшены.  
 * 
 */

import java.util.*;
import java.util.stream.*;

public class Test {

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        String input = scanner.nextLine();
        Map<Character, Long> charCountMap = Arrays.stream(input.split("")).collect(Collectors.groupingBy(s -> s.charAt(0), Collectors.counting()));
        String centerChar = charCountMap.entrySet().stream()
                .filter(entry -> entry.getValue() % 2 == 1)
                .map(entry -> entry.getKey().toString())
                .sorted()
                .findFirst().orElse("");
        StringBuilder output = new StringBuilder(centerChar);

        charCountMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
                .forEach(entry -> {
                    String chars = getStringOfNChars(entry.getKey(), entry.getValue() / 2);
                    output.insert(0, chars).append(chars);
                });
        System.out.println(output);
    }

    static String getStringOfNChars(char c, long length) {
        char[] chars = new char[(int)length];
        Arrays.fill(chars, c);
        return new String(chars);
    }
}
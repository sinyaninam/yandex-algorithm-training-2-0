/*
 * D. Числа
 * 
 * // Условие
 * Ограничение времени 	1 секунда
 * Ограничение памяти 	64Mb
 * Ввод 	стандартный ввод или input.txt
 * Вывод 	стандартный вывод или output.txt
 * 
 * Саша и Катя учатся в начальной школе. Для изучения арифметики при этом используются 
 * карточки, на которых написаны цифры (на каждой карточке написана ровно одна цифра).
 * Однажды они пришли на урок математики, и Саша, используя все свои карточки, показал 
 * число A, а Катя показала число B. Учитель тогда захотел дать им такую задачу, чтобы 
 * ответ на нее смогли показать и Саша, и Катя, каждый используя только свои карточки.
 * При этом учитель хочет, чтобы искомое число было максимально возможным.
 * 
 * // Формат ввода
 * Во входном файле записано два целых неотрицательных числа A и B (каждое число в одной 
 * строке). Длина каждого из чисел не превосходит 100000 цифр.
 * 
 * // Формат вывода
 * Выведите одно число — максимальное целое число, которое можно составить используя как 
 * цифры первого числа, так и цифры второго числа. Если же ни одного такого числа 
 * составить нельзя, выведите -1. 
 * 
 * // Примеры
 * 280138
 * 798081
 * 
 * 8810
 * 
 * 123
 * 456
 * 
 * -1
 * 
 * // Решение
 * В первую очередь необходимо найти общие цифры между числами. Составляем словарь для каждого
 * числа, ключ - цифра, значение - количество вхождений цифры в число. Для любого из словарей
 * делаем следующее - оставляем только те цифры, которые есть в другом словаре, для каждой
 * цифры берем минимальное количество вхождений.
 * Чтобы получить максимальное число, цифры в словаре нужно отсортировать по убыванию. 
 * "Мешать" цифры смысла нет, очевидно, что на первом месте должны идти самые большие из 
 * них независимо от количества, поэтому следующим шагом составляем числовые 
 * последовательности из повторяющихся цифр и собираем их в одно число.
 * 
 * Если общих цифр нет, итоговая строка будет пустой. Если из общих цифр у двух чисел есть
 * только нули, то в итоговой строке в этом и только в этом случае первый символ будет нулём.
 * Оба случая можно обработать в конце.
 * 
 */

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class Test {

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        String firstNum = scanner.nextLine(), secondNum = scanner.nextLine();
        Map<Integer, Long> firstNumDigits =
                Arrays.stream(firstNum.split(""))
                        .map(Integer::parseInt)
                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        Map<Integer, Long> secondNumDigits =
                Arrays.stream(secondNum.split(""))
                        .map(Integer::parseInt)
                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));


        String result = firstNumDigits.keySet().stream()
                .filter(secondNumDigits::containsKey)
                .sorted(Comparator.reverseOrder())
                .map(digit -> getDigitSequence(digit, Math.min(firstNumDigits.get(digit), secondNumDigits.get(digit))))
                .collect(Collectors.joining());

        System.out.println(result.isEmpty() || result.charAt(0) == '0' ? -1 : result);
    }

    static String getDigitSequence(int digit, long number) {
        return Stream.generate(() -> String.valueOf(digit))
                .limit(number)
                .collect(Collectors.joining());
    }
}
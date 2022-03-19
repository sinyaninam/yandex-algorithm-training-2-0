/*
 * D. Угадай число
 * 
 * // Условие
 * Ограничение времени 	3 секунды
 * Ограничение памяти 	64Mb
 * Ввод 	стандартный ввод или input.txt
 * Вывод 	стандартный вывод или output.txt
 * 
 * Август и Беатриса играют в игру. Август загадал натуральное число от 1 до n. Беатриса
 * пытается угадать это число, для этого она называет некоторые множества натуральных чисел.
 * Август отвечает Беатрисе YES, если среди названных ей чисел есть задуманное или NO в
 * противном случае. После нескольких заданных вопросов Беатриса запуталась в том, какие 
 * вопросы она задавала и какие ответы получила и просит вас помочь ей определить, какие 
 * числа мог задумать Август.
 * 
 * // Формат ввода
 * Первая строка входных данных содержит число n — наибольшее число, которое мог загадать 
 * Август. Далее идут строки, содержащие вопросы Беатрисы. Каждая строка представляет собой 
 * набор чисел, разделенных пробелами. После каждой строки с вопросом идет ответ Августа: YES
 * или NO. Наконец, последняя строка входных данных содержит одно слово HELP.
 * 
 * // Формат вывода
 * Вы должны вывести (через пробел, в порядке возрастания) все числа, которые мог 
 * задумать Август. 
 * 
 * // Примеры
 * 10
 * 1 2 3 4 5
 * YES
 * 2 4 6 8 10
 * NO
 * HELP
 * 
 * 1 3 5
 * 
 * 10
 * 1 2 3 4 5 6 7 8 9 10
 * YES
 * 1
 * NO
 * 2
 * NO
 * 3
 * NO
 * 4
 * NO
 * 6
 * NO
 * 7
 * NO
 * 8
 * NO
 * 9
 * NO
 * 10
 * NO
 * HELP
 * 
 * 5
 * 
 */

import java.util.*;
import java.util.stream.*;

public class Test {

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        int maxNum = Integer.parseInt(scanner.nextLine());
        Set<Integer> rightAnswers = new HashSet<>();
        Set<Integer> wrongAnswers = new HashSet<>();

        String input = scanner.nextLine();
        while (!input.equals("HELP")) {
            Set<Integer> currentQuestion = Arrays.stream(input.split(" ")).map(Integer::parseInt).collect(Collectors.toSet());
            input = scanner.nextLine();
            if (input.equals("YES")) {
                if (rightAnswers.size() == 0) {
                    currentQuestion.removeAll(wrongAnswers);
              		wrongAnswers.clear();
                    rightAnswers.addAll(currentQuestion);
                } else
                    rightAnswers.retainAll(currentQuestion);
            } else {
                if (rightAnswers.size() == 0)
                    wrongAnswers.addAll(currentQuestion);
                else
                    rightAnswers.removeAll(currentQuestion);

            }
            input = scanner.nextLine();
        }
        if (rightAnswers.size() == 0)
            System.out.println(IntStream.rangeClosed(1, maxNum).boxed().filter(i -> !wrongAnswers.contains(i)).map(Objects::toString).sorted().collect(Collectors.joining(" ")));
        else
            System.out.println(rightAnswers.stream().map(Object::toString).sorted().collect(Collectors.joining(" ")));
    }
}
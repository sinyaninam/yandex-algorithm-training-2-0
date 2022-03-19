/*
 * B. Выборы в США
 * 
 * // Условие
 * Ограничение времени 	2 секунды
 * Ограничение памяти 	64Mb
 * Ввод 	стандартный ввод или input.txt
 * Вывод 	стандартный вывод или output.txt
 * 
 * Как известно, в США президент выбирается не прямым голосованием, а путем двухуровневого
 * голосования. Сначала проводятся выборы в каждом штате и определяется победитель выборов 
 * в данном штате. Затем проводятся государственные выборы: на этих выборах каждый штат имеет
 * определенное число голосов — число выборщиков от этого штата. На практике, все выборщики
 * от штата голосуют в соответствии с результами голосования внутри штата, то есть на
 * заключительной стадии выборов в голосовании участвуют штаты, имеющие различное число голосов.
 * Вам известно за кого проголосовал каждый штат и сколько голосов было отдано данным штатом.
 * Подведите итоги выборов: для каждого из участника голосования определите число отданных
 * за него голосов.
 * 
 * // Формат ввода
 * Каждая строка входного файла содержит фамилию кандидата, за которого отдают голоса
 * выборщики этого штата, затем через пробел идет количество выборщиков, отдавших голоса
 * за этого кандидата.
 * 
 * // Формат вывода
 * Выведите фамилии всех кандидатов в лексикографическом порядке, затем, через пробел,
 * количество отданных за них голосов. 
 * 
 * // Примеры
 * McCain 10
 * McCain 5
 * Obama 9
 * Obama 8
 * McCain 1
 * 
 * McCain 16
 * Obama 17
 * 
 * ivanov 100
 * ivanov 500
 * ivanov 300
 * petr 70
 * tourist 1
 * tourist 2
 * 
 * ivanov 900
 * petr 70
 * tourist 3
 * 
 * bur 1
 * 
 * bur 1
 * 
 */

import java.util.*;

public class Test {
     public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Integer> map = new HashMap<>();
        String input = scanner.nextLine();
        while (!input.equals("")) {
            String[] record = input.split(" ");
            String person = record[0];
            int votes = Integer.parseInt(record[1]);
            if (!map.containsKey(person))
                map.put(person, votes);
            else
                map.put(person, map.get(person) + votes);
            input = scanner.nextLine();
        }
        map.entrySet().stream()
                .sorted((entry1, entry2) -> entry1.getKey().compareTo(entry2.getKey()))
                .map(entry -> entry.getKey() + " " + entry.getValue())
                .forEach(System.out::println);
     }
}
/*
 * E. Автомобильные номера
 * 
 * // Условие
 * Ограничение времени 	2 секунды
 * Ограничение памяти 	64Mb
 * Ввод 	стандартный ввод или input.txt
 * Вывод 	стандартный вывод или output.txt
 * 
 * Неизвестный водитель совершил ДТП и скрылся с места происшествия. Полиция опрашивает 
 * свидетелей. Каждый из них говорит, что запомнил какие-то буквы и цифры номера. Но при этом 
 * свидетели не помнят порядок этих цифр и букв. Полиция хочет проверить несколько подозреваемых
 * автомобилей. Будем говорить, что номер согласуется с показанием свидетеля, если все
 * символы, которые назвал свидетель, присутствуют в этом номере (не важно, сколько раз).
 * 
 * // Формат ввода
 * Сначала задано число - количество свидетелей. Далее идет M строк, каждая из которых 
 * описывает показания очередного свидетеля. Эти строки непустые и состоят из не более чем
 * 20 символов. Каждый символ в строке - либо цифра, либо заглавная латинская буква, 
 * причём символы могут повторяться.
 * 
 * Затем идёт число - количество номеров. Следующие строки представляют из себя номера
 * подозреваемых машин и имеют такой же формат, как и показания свидетелей.
 * 
 * // Формат вывода
 * Выпишите номера автомобилей, согласующиеся с максимальным количеством свидетелей. Если
 * таких номеров несколько, то выведите их в том же порядке, в котором они были заданы на входе. 
 * 
 * // Примеры
 * 3
 * ABC
 * A37
 * BCDA
 * 2
 * A317BD
 * B137AC
 * 
 * B137AC
 * 
 * 2
 * 1ABC
 * 3A4B
 * 3
 * A143BC
 * C143AB
 * AAABC1
 * 
 * A143BC
 * C143AB
 * 
 */

import java.util.*;
import java.util.stream.Collectors;

public class Test {

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        int numOfWitnesses = Integer.parseInt(scanner.nextLine());
        List<Set<String>> testimonies = new ArrayList<>();
        for (int i = 0; i < numOfWitnesses; i++)
            testimonies.add(Arrays.stream(scanner.nextLine().split("")).collect(Collectors.toSet()));

        int numOfCarNumbers = Integer.parseInt(scanner.nextLine());
        List<String> carNumbers = new ArrayList<>();
        for (int i = 0; i < numOfCarNumbers; i++)
            carNumbers.add(scanner.nextLine());

        final int[] max = {0};
        Map<Integer, List<String>> map = carNumbers.stream().collect(Collectors.groupingBy(
                (String carNumber) ->
                    (int) testimonies.stream()
                            .filter(testimony -> Arrays.stream(carNumber.split("")).collect(Collectors.toList()).containsAll(testimony))
                            .count(),
                Collectors.toList()));
        map.get(map.keySet().stream().max(Comparator.naturalOrder()).get()).forEach(System.out::println);
    }
}
/*
 * D. Выборы Государственной Думы
 * 
 * // Условие
 * Ограничение времени 	2 секунды
 * Ограничение памяти 	64Mb
 * Ввод 	стандартный ввод или input.txt
 * Вывод 	стандартный вывод или output.txt
 * 
 * Статья 83 закона “О выборах депутатов Государственной Думы Федерального Собрания 
 * Российской Федерации” определяет следующий алгоритм пропорционального распределения 
 * мест в парламенте.
 * 
 * Необходимо распределить 450 мест между партиями, участвовавших в выборах. Сначала
 * подсчитывается сумма голосов избирателей, поданных за каждую партию и подсчитывается 
 * сумма голосов, поданных за все партии. Эта сумма делится на 450, получается величина, 
 * называемая “первое избирательное частное” (смысл первого избирательного частного - это 
 * количество голосов избирателей, которое необходимо набрать для получения одного места
 * в парламенте).
 * 
 * Далее каждая партия получает столько мест в парламенте, чему равна целая часть от
 * деления числа голосов за данную партию на первое избирательное частное.
 * 
 * Если после первого раунда распределения мест сумма количества мест, отданных партиям, 
 * меньше 450, то оставшиеся места передаются по одному партиям, в порядке убывания дробной
 * части частного от деления числа голосов за данную партию на первое избирательное частное.
 * Если же для двух партий эти дробные части равны, то преимущество отдается той партии, 
 * которая получила большее число голосов.
 * 
 * // Формат ввода
 * На вход программе подается список партий, участвовавших в выборах. Каждая строка 
 * входного файла содержит название партии (строка, возможно, содержащая пробелы), затем,
 * через пробел, количество голосов, полученных данной партией – число, не превосходящее 108.
 * 
 * // Формат вывода
 * Программа должна вывести названия всех партий и количество голосов в парламенте, 
 * полученных данной партией. Названия необходимо выводить в том же порядке, в котором 
 * они шли во входных данных.
 *  
 * // Примеры
 * Party One 100000
 * Party Two 200000
 * Party Three 400000
 * 
 * Party One 64
 * Party Two 129
 * Party Three 257
 * 
 * Party number one 100
 * Partytwo 100
 * 
 * Party number one 225
 * Partytwo 225
 * 
 * Party number one 449
 * Partytwo 1
 * 
 * Party number one 449
 * Partytwo 1
 * 
 */

import java.util.*;

public class Test {

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Float> map = new HashMap<>();
        int index = 0;
        int sum = 0;
        String input = scanner.nextLine();
        while (!input.equals("")) {
            String name = input.substring(0, input.lastIndexOf(" "));
            int votes = Integer.parseInt(input.substring(input.lastIndexOf(" ") + 1));
            map.put(index + "_" + name, (float) votes);
            index++;
            input = scanner.nextLine();
            sum += votes;
        }

        int votesSum = sum;
        int seatsSum = map.values().stream().map(integer -> integer * 450 / votesSum).mapToInt(Float::intValue).sum();
        map.entrySet().stream()
                .sorted((entry1, entry2) -> {
                    Float mod1 = entry1.getValue() * 450 % votesSum;
                    Float mod2 = entry2.getValue() * 450 % votesSum;
                    if (mod1.equals(mod2))
                        return entry1.getValue().compareTo(entry2.getValue());
                    else
                        return mod2.compareTo(mod1); })
                .limit(450 - seatsSum)
                .forEach(entry -> map.put(entry.getKey(), entry.getValue() + votesSum / 450));
        map.entrySet().stream()
                .peek(entry -> map.put(entry.getKey(), entry.getValue() * 450 / votesSum))
                .sorted((entry1, entry2) -> {
                    Integer i1 = Integer.parseInt(entry1.getKey().substring(0, entry1.getKey().indexOf("_")));
                    Integer i2 = Integer.parseInt(entry2.getKey().substring(0, entry2.getKey().indexOf("_")));
                    return i1.compareTo(i2); })
                .map(entry -> entry.getKey().substring(entry.getKey().indexOf("_") + 1) + " " + entry.getValue().intValue())
                .forEach(System.out::println);
    }
}
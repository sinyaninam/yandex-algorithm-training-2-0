/*
 * D. Эльфы и олени
 * 
 * // Условие
 * Ограничение времени 	2 секунды
 * Ограничение памяти 	64Mb
 * Ввод 	стандартный ввод или input.txt
 * Вывод 	стандартный вывод или output.txt
 * 
 * Скоро новый год и Санта-Клаус уже начал готовить свою волшебную оленью упряжку, на которой
 * он развозит подарки детям. Известно, что упряжку везут несколько волшебных оленей, на
 * каждом из которых едут два эльфа.
 * 
 * Но волшебные олени – строптивые животные, поэтому не любые два эльфа могут ехать на любом
 * олене. А именно, каждый олень характеризуется некоторой строптивостью ai, а каждый эльф –
 * – темпераментом bi. Два эльфа j и k могут ехать на i-м олене в том и только в том случае,
 * если либо bj < ai < bk, либо bk < ai < bj.
 * 
 * Чтобы его появление было максимально зрелищным, Санта-Клаус хочет, чтобы в его упряжке
 * было как можно больше оленей. Про каждого оленя Санта знает его строптивость, а про
 * каждого эльфа – его темперамент.
 * 
 * Помогите Санте выяснить, какое максимальное количество оленей он сможет включить в
 * упряжку, каких оленей ему следует выбрать, и какие эльфы должны на них ехать.
 * 
 * // Формат ввода
 * В первой строке вводятся два целых числа m и n – количество оленей и эльфов, соответственно
 * (1 ≤ m, n ≤ 100 000).
 * 
 * Вторая строка содержит m целых чисел ai – строптивость оленей (0 ≤ ai ≤ 10^9). В третьей
 * строке записаны n целых чисел bi – темперамент эльфов (0 ≤ bi ≤ 10^9).
 * 
 * // Формат вывода
 * В первой строке выведите одно число k – максимальное количество оленей, которое 
 * Санта-Клаус может включить в свою упряжку. В следующих k строках выведите по три целых 
 * числа: di, ei, 1, ei, 2 – для каждого оленя в упряжке выведите его номер и номера эльфов,
 * которые на нем поедут. Если решений несколько, выведите любое.
 * 
 * И эльфы, и олени пронумерованы, начиная с единицы, в том порядке, в котором они заданы
 * во входных данных.
 * 
 * // Примеры
 * 4 6
 * 2 3 4 5
 * 1 3 2 2 5 2
 * 
 * 2
 * 1 1 2
 * 2 4 5
 * 
 */

import java.util.*;
import java.util.stream.*;

public class Test {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        List<Integer> deerInput = Arrays.stream(scanner.nextLine().split(" ")).map(Integer::parseInt).collect(Collectors.toList());
        List<Integer> elvesInput = Arrays.stream(scanner.nextLine().split(" ")).map(Integer::parseInt).collect(Collectors.toList());
        List<Deer> deerList = IntStream.range(0, deerInput.size()).boxed()
                .map(index -> new Deer(index, deerInput.get(index)))
                .sorted()
                .collect(Collectors.toList());
        List<Elf> elvesList = IntStream.range(0, elvesInput.size()).boxed()
                .map(index -> new Elf(index, elvesInput.get(index)))
                .sorted()
                .collect(Collectors.toList());


        StringBuilder result = new StringBuilder();
        int deerCnt = 0;
        int deerIndex = 0;
        int leftElfIndex = 0, rightElfIndex = (elvesInput.size() + 1) / 2;
        while (rightElfIndex < elvesInput.size()) {
            while (deerIndex != deerList.size() && deerList.get(deerIndex).obstinacy <= elvesList.get(leftElfIndex).temper)
                deerIndex++;
            if (deerIndex == deerList.size())
                break;
            if (deerList.get(deerIndex).obstinacy < elvesList.get(rightElfIndex).temper) {
                deerCnt++;
                result.append(deerList.get(deerIndex).index).append(" ")
                        .append(elvesList.get(leftElfIndex).index).append(" ")
                        .append(elvesList.get(rightElfIndex).index).append("\n");
                leftElfIndex++;
                deerIndex++;
            }
            rightElfIndex++;
        }
        result.insert(0, deerCnt + "\n");
        System.out.println(result);
    }

    static class Deer implements Comparable<Deer> {
        int index, obstinacy;

        Deer(int index, int obstinacy) {
            this.index = index + 1;
            this.obstinacy = obstinacy;
        }

        @Override
        public int compareTo(Deer o) {
            return Integer.compare(this.obstinacy, o.obstinacy);
        }
    }

    static class Elf implements Comparable<Elf> {
        int index, temper;

        Elf(int index, int temper) {
            this.index = index + 1;
            this.temper = temper;
        }

        @Override
        public int compareTo(Elf o) {
            return Integer.compare(this.temper, o.temper);
        }
    }
}
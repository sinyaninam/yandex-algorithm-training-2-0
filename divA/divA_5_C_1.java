/*
 * C. Лучшие друзья девушек - это фуллерены
 * 
 * // Условие
 * Ограничение времени 	3 секунды
 * Ограничение памяти 	64Mb
 * Ввод 	стандартный ввод или input.txt
 * Вывод 	стандартный вывод или output.txt
 * 
 * Развитие химической науки привело к тому, что высшие фуллерены (сложные молекулы
 * углерода в виде шарика или продолговатой трубки) стали недорогими в производстве. 
 * Благодаря своим уникальным оптическим свойствам они нашли свое место и в ювелирной
 * промышленности. Ювелирный дом «Кёрл, Крото и Смолли» выпустил уникальную коллекцию
 * украшений из фуллеренов. Украшение продается в виде набора трубок-фуллеренов различной 
 * длины, из которых можно составить украшение самостоятельно.
 * 
 * Норма Джин очень любит сложные углеродные соединения и купила себе набор фуллеренов для
 * составления украшений. Ее фирменный стиль состоит в том, чтобы носить украшения, 
 * составленные ровно из трех трубок фуллерена, причем в результате должен получаться 
 * тупоугольный треугольник. Норма Джин — объект постоянной охоты папарацци, поэтому не 
 * может позволить себе дважды появиться на людях с одним и тем же украшением.
 * 
 * Помогите Норме Джин узнать, сколько вечеров она сможет посетить с имеющимся у нее набором
 * фуллереновых трубок. Фуллереновые трубки одинаковой длины считаются различными. Треугольники
 * считаются различными, если они отличаются хотя бы одной трубкой. Треугольники, состоящие из
 * одних и тех же трубок, считаются одинаковыми независимо от порядка трубок.
 * 
 * // Формат ввода
 * Первая строка входного файла содержит одно число N (1 ≤ N ≤ 5000) — количество
 * фуллереновых трубок в наборе Нормы Джин.
 * 
 * Вторая строка содержит N упорядоченных по возрастанию целых чисел Li (1 ≤ Li ≤ 2×10^9).
 * 
 * // Формат вывода
 * Выведите одно целое число — количество вечеров, на которые сможет сходить Норма Джин.
 * 
 * // Примеры
 * 4
 * 2 2 3 4
 * 
 * 3
 * 
 * // Решение
 * В отсортированном списке перебираем первые две стороны без повторов (для второй стороны
 * игнорируем уже рассмотренные варианты первой) и для каждой комбинации определяем в каких
 * границах будет третья. Нам необходимо убедиться, что полученная в итоге фигура будет
 * треугольником, т.е. сумма первых двух сторон больше третьей с одной стороны, а также 
 * что треугольник получится тупоугольный, т.е. квадрат большей стороны (а это третья по
 * умолчанию, поскольку массив отсортирован) больше суммы квадратов первой и второй. 
 * Бинарным поиском можно найти и ту, и другую границы, а дальше посчитать количество
 * элементов, вычитая из индекса правой индекс левой. Список возрастающий, а условие
 * тупоугольности гарантирует, что обнаруженная бинпоиском левая граница третьей стороны
 * будет больше, чем вторая, таким образом тоже исключая повторы.
 * 
 * То же самое можно посчитать быстрее, если перед вложенным циклом завести указатели на
 * левую и правую границу. Увеличение второй стороны будет толкать и левую и правую границы
 * строго вправо (увеличение одной из меньших сторон увеличивает их сумму квадратов, т.е.
 * третья сторона либо остаётся той же, если сохраняется условие тупоугольности, либо 
 * увеличивается = двигается вправо; увеличение одной из меньших сторон так же увеличивает 
 * сумму этих сторон, гарантируя, что для уже намеченной правой границы сохраняется условие
 * того, что полученная фигура - треугольник, а так же позволяет сдвиг правой границы дальше
 * вправо, поскольку теперь сторона большего размера может оказаться меньшей, чем сумма двух
 * других сторон), гарантируя только один проход по массиву для третьей стороны.
 * 
 */

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Test {

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        List<Integer> tubes = Arrays.stream(scanner.nextLine().split(" ")).map(Integer::parseInt).sorted().collect(Collectors.toList());
        int numOfCombs = 0;
        for (int i = 0; i < tubes.size(); i++) {
            for (int j = i + 1; j < tubes.size() - 1; j++) {
                int firstTube = tubes.get(i), secondTube = tubes.get(j);
                int firstOfThirdTubeIndexes = leftmostBS(tubes, thirdTube -> Math.pow(thirdTube, 2) > Math.pow(firstTube, 2) + Math.pow(secondTube, 2));
                int lastOfThirdTubeIndexes = rightmostBS(tubes, thirdTube -> thirdTube < firstTube + secondTube);
                if (firstOfThirdTubeIndexes != -1 && lastOfThirdTubeIndexes != -1)
                    numOfCombs += Math.max(lastOfThirdTubeIndexes - firstOfThirdTubeIndexes + 1, 0);
            }
        }
        System.out.println(numOfCombs);
    }

    static int leftmostBS(List<Integer> list, Predicate<Integer> predicate) {
        int start = 0, end = list.size() - 1;

        if (!predicate.test(list.get(end)))
            return -1;
        while (start < end) {
            int mid = (start + end) / 2;
            if (predicate.test(list.get(mid)))
                end = mid;
            else
                start = mid + 1;
        }
        return end;
    }

    static int rightmostBS(List<Integer> list, Predicate<Integer> predicate) {
        int start = 0, end = list.size() - 1;

        if  (!predicate.test(list.get(start)))
            return -1;
        while (start < end) {
            int mid = (start + end + 1) / 2;
            if (!predicate.test(list.get(mid)))
                end = mid - 1;
            else
                start = mid;
        }
        return start;
    }

}
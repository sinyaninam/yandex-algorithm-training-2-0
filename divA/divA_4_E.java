/*
 * E. Имена
 * 
 * // Условие
 * Ограничение времени 	1 секунда
 * Ограничение памяти 	64Mb
 * Ввод 	стандартный ввод или input.txt
 * Вывод 	стандартный вывод или output.txt
 * 
 * На далекой планете Тау Кита есть непонятные нам обычаи. Например, таукитяне очень 
 * необычно для землян выбирают имена своим детям. Родители так выбирают имя ребенку, 
 * чтобы оно могло быть получено как удалением некоторого набора букв из имени отца, так 
 * и удалением некоторого набора букв из имени матери. Например, если отца зовут «abacaba»,
 * а мать — «bbccaa», то их ребенок может носить имена «a», «bba», «bcaa», но не может носить
 * имена «aaa», «ab» или «bbc». Возможно, что имя ребенка совпадает с именем отца и/или матери,
 * если оно может быть получено из имени другого родителя удалением нескольких (возможно, ни 
 * одной) букв.
 * 
 * Пусть отец по имени X и мать по имени Y выбирают имя своему новорожденному ребенку. Так
 * как в таукитянских школах учеников часто вызывают к доске в лексикографическом порядке имен
 * учеников, то есть в порядке следования имен в словаре, то они хотят выбрать своему ребенку 
 * такое имя, чтобы оно лексикографически следовало как можно позже.
 * 
 * Формально, строка S лексикографически больше строки T, если выполняется одно из двух 
 * условий: строка T получается из S удалением одной или более букв с конца строки S; 
 * первые (i - 1) символов строк T и S не различаются, а буква в i-й позиции строки T следует 
 * в алфавите раньше буквы в i-й позиции строки S. Требуется написать программу, которая по 
 * именам отца и матери находит лексикографически наибольшее имя для их ребенка.
 * 
 * // Формат ввода
 * Первая строка входного файла содержит X — имя отца. Вторая строка входного файла 
 * содержит Y — имя матери. Каждое имя состоит из строчных букв латинского алфавита, 
 * включает хотя бы одну букву и имеет длину не более 10^5 букв.
 * 
 * // Формат вывода
 * Выходной файл должен содержать искомое лексикографически наибольшее из возможных имен
 * ребенка. В случае, если подходящего имени для ребенка не существует, выходной файл должен
 * быть пустым. 
 * 
 * // Примеры
 * abcabca
 * abcda
 * 
 * ca
 * 
 * ccba
 * accbbaa
 * 
 * ccba
 * 
 * // Решение
 * Для понимания приоритетов при выборе очередной буквы из обоих имен для составления нового,
 * можно посмотреть на эти примеры:
 * z > xysysfsafafs...
 * zz > zxfofkfsgge...
 * Мы стремимся составить слово из как можно большего количества букв, стоящих как можно 
 * дальше по алфавиту, причём второй пункт важнее первого. 
 * Для этого составим словари для оригинальных имен, где ключом будет буква, а значением -
 * - список позиций этой буквы в слове. Первым шагом мы хотим оставить в любом из словарей
 * только те буквы, которые есть во втором, вторым - отсортировать их по убыванию. Следует
 * помнить, что по условию мы не можем брать буквы в произвольном порядке - все буквы в слове,
 * которые находятся левее последней взятой недоступны для нас. Чтобы отслеживать текущее
 * положение последней взятой буквы в каждом слове, заведем по указателю для них.
 * Далее для каждой общей буквы мы ищем минимальное количество её вхождений в слова
 * (в словах zazab и zczczzz мы можем взять только zz) и сдвигаем указатель на индекс
 * последней взятой буквы (в примере выше указатели будут стоять здесь: za(z)ab, zc(z)czzz.
 * Во втором слове больше букв z, но указатель необходимо держать как можно левее, чтобы 
 * остаток слова был как можно длиннее и содержал потенциально большее количество подходящих
 * букв). Для последующих букв после первой количество считается заново в части слова после
 * указателя. Начальную позицию новой буквы можно найти бинарным поиском, зная, что её индекс
 * в слове должен быть как можно ближе к указателю с правой стороны. 
 * Рассмотрим на примере:
 * ccba, accbbaa
 * c: c(c)ba, ac(c)bbaa; cc
 * b: cc(b)a, acc(b)baa; ccb
 * a: ccb(a), accbb(a)a; ccba
 * 
 */

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.*;

public class Test {

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        String first = scanner.nextLine();
        String second = scanner.nextLine();
        Map<Character, List<Integer>> firstNameCharOccurrences =
                IntStream.range(0, first.length()).boxed().collect(Collectors.groupingBy(first::charAt, Collectors.toList()));
        Map<Character, List<Integer>> secondNameCharOccurrences =
                IntStream.range(0, second.length()).boxed().collect(Collectors.groupingBy(second::charAt, Collectors.toList()));

        AtomicInteger firstPointer = new AtomicInteger(-1), secondPointer = new AtomicInteger(-1);
        StringBuilder result = new StringBuilder();
        firstNameCharOccurrences.keySet().stream()
                .sorted(Comparator.reverseOrder())
                .filter(secondNameCharOccurrences::containsKey)
                .forEach(letter -> {
                    List<Integer>
                            firstLetterPositions = firstNameCharOccurrences.get(letter),
                            secondLetterPositions = secondNameCharOccurrences.get(letter);
                    int firstIndex = leftmostBS(firstLetterPositions, letterPos -> letterPos > firstPointer.get());
                    int secondIndex = leftmostBS(secondLetterPositions, letterPos -> letterPos > secondPointer.get());
                    if (firstIndex != -1 && secondIndex != -1) {
                        int firstNameLettersNum = firstLetterPositions.size() - firstIndex;
                        int secondNameLettersNum = secondLetterPositions.size() - secondIndex;
                        int resultLettersNum = Math.min(firstNameLettersNum, secondNameLettersNum);
                        firstPointer.set(firstLetterPositions.get(firstIndex + resultLettersNum - 1));
                        secondPointer.set(secondLetterPositions.get(secondIndex + resultLettersNum - 1));
                        result.append(getStringOfNRepeatedLetters(letter, resultLettersNum));
                    }
                });
        System.out.println(result);
    }

    private static String getStringOfNRepeatedLetters(char letter, int n) {
        char[] chars = new char[n];
        Arrays.fill(chars, letter);
        return new String(chars);
    }

    private static int leftmostBS(List<Integer> list, Predicate<Integer> predicate) {
        int start = 0, end = list.size() - 1;
        if (!predicate.test(list.get(end)))
            return -1;
        while(start < end) {
            int mid = (start + end) / 2;
            if (predicate.test(list.get(mid)))
                end = mid;
            else
                start = mid + 1;
        }
        return start;
    }
}
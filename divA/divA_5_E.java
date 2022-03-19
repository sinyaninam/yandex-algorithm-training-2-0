/*
 * E. Странные строки
 * 
 * // Условие
 * Ограничение времени 	1 секунда
 * Ограничение памяти 	64Mb
 * Ввод 	стандартный ввод или input.txt
 * Вывод 	стандартный вывод или output.txt
 * 
 * Рассмотрим строку s, состоящую из строчных букв латинского алфавита. Примером такой
 * строки является, например, строка «abba».
 * 
 * Подстрокой строки s называется строка, составленная из одного или нескольких подряд 
 * идущих символов строки s. Обозначим как W(s) множество, состоящее из всех возможных
 * подстрок строки s. При этом каждая подстрока входит в это множество не более одного раза, 
 * даже если она встречается в строке s несколько раз.
 * 
 * Например, W(«abba») = «a», «b», «ab», «ba», «bb», «abb», «bba», «abba».
 * 
 * Подпоследовательностью строки s называется строка, которую можно получить из s удалением
 * произвольного числа символов. Обозначим как Y(s) множество, состоящее из всех возможных 
 * подпоследовательностей строки s. Аналогично W(s), каждая подпоследовательность строки s 
 * включается в Y(s) ровно один раз, даже если она может быть получена несколькими способами
 * удаления символов из строки s. Поскольку любая подстрока строки s является также ее
 * подпоследовательностью, то множество Y(s) включает в себя W(s), но может содержать 
 * также и другие строки.
 * 
 * Например, Y(«abba») = W(«abba») ∪ «aa», «aba». Знак ∪ обозначает объединение множеств.
 * 
 * Будем называть строку s странной, если для нее W(s) = Y(s). Так, строка «abba» не
 * является странной, а, например, строка «abb» является, так как для нее 
 * W(«abb») = Y(«abb») = «a», «b», «ab», «bb», «abb».
 * 
 * Будем называть странностью строки число ее различных странных подстрок. При вычислении 
 * странности подстрока считается один раз, даже если она встречается в строке s в качестве
 * подстроки несколько раз. Так, для строки «abba» ее странность равна 7, любая ее подстрока,
 * кроме всей строки, является странной.
 * 
 * Требуется написать программу, которая по заданной строке s определяет ее странность.
 * 
 * // Формат ввода
 * Входной файл содержит строку s, состоящую из строчных букв латинского алфавита. 
 * Строка имеет длину от 1 до 200 000.
 * 
 * // Формат вывода
 * Выходной файл должен содержать одно целое число: странность заданной во входном файле строки.
 * 
 * // Примеры
 * ab
 * 
 * 3
 * 
 */

import java.util.*;
import java.util.stream.Collectors;

public class Test {

    static class RLEUnit {
        char letter;
        int num;

        RLEUnit(char letter, int num) {
            this.letter = letter;
            this.num = num;
        }

        public char getLetter() {
            return letter;
        }

        public int getNum() {
            return num;
        }
    }

    static class TwoLettersCombos {
        Map<String, Map<Integer, Integer>> map = new HashMap<>();

        void add(RLEUnit first, RLEUnit second) {
            String letters = "" + first.getLetter() + second.getLetter();
            if (!map.containsKey(letters)) {
                Map<Integer, Integer> numsMap = new TreeMap<>(Comparator.reverseOrder());
                numsMap.put(first.getNum(), second.getNum());
                map.put(letters, numsMap);
            } else
                map.get(letters).merge(first.getNum(), second.getNum(), Math::max);
        }

        Collection<Map<Integer, Integer>> getValues() {
            return map.values();
        }

        static int getUnion(Map<Integer, Integer> numsMap) {
            int prev = 0, union = 0;
            for (Integer first : numsMap.keySet()) {
                int second = numsMap.get(first);
                if (second > prev) {
                    union += first * (second - prev);
                    prev = second;
                }
            }
            return union;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        char[] input = scanner.nextLine().toCharArray();
        List<RLEUnit> rleSequence = new ArrayList<>();
        for (int i = 0; i < input.length; i++) {
            char letter = input[i];
            int start = i;
            while (i < input.length && input[i] == letter)
                i++;
            i--;
            rleSequence.add(new RLEUnit(letter, (i - start + 1)));
        }

        int union = 0;
        Map<Character, Integer> sameLetterCombos = rleSequence.stream().collect(Collectors.toMap(RLEUnit::getLetter, RLEUnit::getNum, Math::max));
        union += sameLetterCombos.values().stream().mapToInt(Integer::intValue).sum();

        TwoLettersCombos twoLettersCombos = new TwoLettersCombos();
        for (int i = 0; i < rleSequence.size() - 1; i++)
            twoLettersCombos.add(rleSequence.get(i), rleSequence.get(i + 1));
        union += twoLettersCombos.getValues().stream().mapToInt(TwoLettersCombos::getUnion).sum();

        //twoLettersCombos.map.forEach((k,v) -> System.out.println(k + " " + v));
        System.out.println(union);
    }
}
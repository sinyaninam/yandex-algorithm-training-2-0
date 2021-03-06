/*
 * B. Изобретательный Петя
 * 
 * // Условие
 * Ограничение времени 	1 секунда
 * Ограничение памяти 	64Mb
 * Ввод 	стандартный ввод или input.txt
 * Вывод 	стандартный вывод или output.txt
 * 
 * Петя нашел на чердаке старый телеграфный аппарат и приделал к нему хитроумное устройство,
 * которое может печатать на телеграфной ленте определенное слово (обозначим его X). Петино
 * устройство может напечатать на ленте это слово сколько угодно раз. Петя может заставить
 * аппарат напечатать на ленте и любое другое сообщение, но для этого ему нужно разобрать
 * свое хитроумное устройство, и после этого он уже не сможет печатать сообщение X. А самое
 * главное, что напечатать даже один символ другого сообщения потребует от Пети больше усилий,
 * чем напечатать на ленте слово X с помощью хитроумного устройства.
 * 
 * Петя хочет сделать так, чтобы всем казалось, что ему по телеграфу пришло сообщение Z.
 * Для этого он может (строго в этой последовательности):
 * 
 * - сколько угодно раз напечатать сообщение X
 * - разобрать хитроумное устройство и посимвольно напечатать еще что-нибудь (назовем это Y)
 * - оторвать и выбросить начало ленты так, чтобы на оставшейся ленте было напечатано в
 *   точности сообщение Z
 * 
 * Поскольку набирать отдельные символы сообщения Y довольно сложно, Петя хочет, чтобы в 
 * сообщении Y было как можно меньше символов.
 * 
 * Для лучшего понимания задачи смотрите примеры и пояснения к ним.
 * 
 * // Формат ввода
 * В первой строке вводится слово X, которое Петя может печатать с помощью хитроумного 
 * устройства сколько угодно раз. Во второй строке вводится сообщение Z, которое хочет 
 * получить Петя. Каждое сообщение состоит только из маленьких латинских букв и имеет длину
 * не более 100 символов.
 * 
 * // Формат вывода
 * Выведите минимальное по длине сообщение Y, которое Пете придется допечатать вручную.
 * 
 * // Примеры
 * mama
 * amamam
 * 
 * m
 * 
 * ura
 * mura
 * 
 * mura
 * 
 * computer
 * comp
 * 
 * comp
 * 
 * ejudge
 * judge
 * 
 * --
 * 
 * m
 * mmm
 * 
 * --
 * 
 * // Примечания
 * 1. Сначала Петя два раза напечатает слово mama, потом к нему припечатает букву m, а 
 * затем отрежет и выбросит три начальных символа (mam). Ответом является допечатываемая
 * отдельно буква m.
 * 
 * 2. Казалось бы, Пете стоит сначала напечатать букву m, а затем слово ura, которое он
 * умеет печатать. Однако для того, чтобы напечатать m, ему придется разобрать свое
 * устройство, и печатать ura ему придется также посимвольно.
 * 
 * 3. Казалось бы, Петя может напечатать слово computer, а затем отрезать и выбросить его
 * конец — однако он не может так поступить, потому что отрезать и выбросить он может 
 * только начало ленты.
 * 
 * 4. Пете достаточно один раз напечатать слово ejudge, а затем отрезать и выбросить
 * букву e. Ничего посимвольно выводить ему не придется, поэтому ответом является пустая
 * строка.
 * 
 * 5. Достаточно трижды напечатать исходное слово и нужный результат будет получен.
 * Ничего добавлять не надо, поэтому ответ – пустая строка.
 * 
 * // Решение
 * Первое, что нужно понять - входит ли в состав спец. слова с конца упорядоченная 
 * последовательность символов из начала сообщения хоть какой-нибудь длины. Используя 
 * пример выше, для сообщения amamam и слова mama, таких последовательностей две - ama и a.
 * На этой стадии ещё нельзя быть уверенным в том, какая из этих последовательностей даст
 * лучший результат, необходимо для каждой из последовательностей проверить, каким окажется
 * остаток. Для примера выше результат примет вид ama(mam) и a-mama(m) соответственно, но 
 * для сообшения amamama результат уже будет ama-mama() и a-mama(ma). 
 * Алгоритм - проверяем наличие спец-слова в начале сообщения, поочередно удаляя по букве
 * с начала. Если таких последовательностей нет, значит спец-слово не пригодится и всё
 * сообщение придётся писать целиком. В противном случае для каждой полученной 
 * последовательности считаем количество оставшихся букв и записываем их, если оно минимально.
 * 
 */

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        String originalSpecialWord = scanner.nextLine();
        String message = scanner.nextLine();

        String cutSpecialWord = originalSpecialWord;
        List<String> cutSpecialWordCombinations = new ArrayList<>();
        while (cutSpecialWord.length() != 0) {
            Pattern p = Pattern.compile("^" + cutSpecialWord + "(.*)");
            Matcher m = p.matcher(message);
            if (m.matches())
                cutSpecialWordCombinations.add(cutSpecialWord);
            cutSpecialWord = cutSpecialWord.substring(1);
        }

        if (cutSpecialWordCombinations.isEmpty()) {
            System.out.println(message);
            return;
        }

        String leftovers = "";
        int minLength = Integer.MAX_VALUE;
        for (String specialWord : cutSpecialWordCombinations) {
            Pattern p = Pattern.compile("^" + specialWord + "(" + originalSpecialWord + ")*(.*)");
            Matcher m = p.matcher(message);
            if (m.matches()) {
                String currentLeftovers = m.group(2);
                if (currentLeftovers.length() < minLength) {
                    leftovers = currentLeftovers;
                    minLength = currentLeftovers.length();
                }
            }
        }
        System.out.println(leftovers);
    }
}
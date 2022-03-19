/*
 * B. Головоломка
 * 
 * // Условие
 * Ограничение времени 	1 секунда
 * Ограничение памяти 	64Mb
 * Ввод 	стандартный ввод или input.txt
 * Вывод 	стандартный вывод или output.txt
 * 
 * Петя разгадывает головоломку, которая устроена следующим образом. Дана квадратная 
 * таблица размера NxN, в каждой клетке которой записана какая-нибудь латинская буква.
 * Кроме того, дан список ключевых слов. Пете нужно, взяв очередное ключевое слово, 
 * найти его в таблице. То есть найти в таблице все буквы этого слова, причем они должны 
 * быть расположены так, чтобы клетка, в которой расположена каждая последующая буква слова,
 * была соседней с клеткой, в которой записана предыдущая буква (клетки называются соседними,
 * если они имеют общую сторону — то есть соседствуют по вертикали или по горизонтали). 
 * Например, на рисунке ниже показано, как может быть расположено в таблице слово olympiad.
 * 
 * Когда Петя находит слово, он вычеркивает его из таблицы. Использовать уже вычеркнутые 
 * буквы в других ключевых словах нельзя.
 * 
 * После того, как найдены и вычеркнуты все ключевые слова, в таблице остаются еще несколько
 * букв, из которых Петя должен составить слово, зашифрованное в головоломке.
 * 
 * Помогите Пете в решении этой головоломки, написав программу, которая по данной таблице 
 * и списку ключевых слов выпишет, из каких букв Петя должен сложить слово, то есть какие
 * буквы останутся в таблице после вычеркивания ключевых слов.
 * 
 * // Формат ввода
 * В первой строке входного файла записаны два числа N (1 ≤ N ≤ 10) и M (0 ≤ M ≤ 200). 
 * Следующие N строк по N заглавных латинских букв описывают ребус. Следующие M строк
 * содержат слова. Слова состоят только из заглавных латинских букв, каждое слово не длиннее
 * 200 символов. Гарантируется, что в таблице можно найти и вычеркнуть по описанным выше
 * правилам все ключевые слова.
 * 
 * // Формат вывода
 * В единственную строку выходного файла выведите в любом порядке буквы, которые останутся
 * в таблице.
 * 
 * // Примеры
 * 5 3
 * POLTE
 * RWYMS
 * OAIPT
 * BDANR
 * LEMES
 * OLYMPIAD
 * PROBLEM
 * TEST
 * 
 * AENRSW
 * 
 * // Решение
 * Нужны два метода - один для поиска всех возможных комбинаций одного слова в таблице,
 * второй - для поиска комбинации всех слов. 
 * Первый метод перебирает все символы таблицы, в случае совпадения текущего символа с
 * первой буквой нужного слова вычеркивает из копии таблицы текущий символ и передаёт его 
 * в следующий метод, который ищет следующую букву в соседних клетках. В случае нахождения 
 * вызывается рекурсивно с новой копией таблицы, пока слово не закончится. Если слово найдено,
 * то копия таблицы со всеми вычеркнутыми буквами записывается в список. Список содержит все
 * возможные комбинации слова. Частный случай - "слово" из одной буквы - должен обрабатываться
 * в первом же методе.
 * Второй метод перебирает таблицы с комбинациями первого слова и вызывается рекурсивно 
 * для каждой комбинации, пока последовательность слов не закончится. По условию, впрочем,
 * достаточно найти любую из комбинаций, поэтому из полученного списка берём первую.
 * 
 */

import java.util.*;
import java.util.stream.*;

public class Test {

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        int[] input = Arrays.stream(scanner.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        int size = input[0], requests = input[1];
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < size; i++)
            strings.add(scanner.nextLine());
        Table table = new Table(strings);

        List<String> words = new ArrayList<>();
        for (int i = 0; i < requests; i++)
            words.add(scanner.nextLine());
        table.findWords(words);
        System.out.println(table.getRemainingLetters());
    }
}

class Table {
    private final char[][] field;

    Table(List<String> strings) {
        field = new char[strings.size()][strings.size()];
        for (int i = 0; i < strings.size(); i++)
            field[i] = strings.get(i).toCharArray();
    }

    private List<String> allWords;
    private List<char[][]> allWordsCombinations = new ArrayList<>();

    void findWords(List<String> words) {
        allWords = words;
        if (words.size() != 0)
            findWords(field, 0);
    }

    void findWords(char[][] field, int wordPos) {
        findAllWordOccurrences(field, allWords.get(wordPos));
        List<char[][]> allCurrentWordOccurrences = new ArrayList<>(allWordOccurrences);
        allCurrentWordOccurrences.forEach(currentField -> {
            char[][] testField = Arrays.stream(currentField).map(char[]::clone).toArray(char[][]::new);
            if (wordPos == allWords.size() - 1)
                allWordsCombinations.add(testField);
            else
                findWords(testField, wordPos + 1);
        });
    }

    private String currentWord;
    private List<char[][]> allWordOccurrences;
    private static final char emptyChar = '#';

    void findAllWordOccurrences(char[][] field, String word) {
        allWordOccurrences = new ArrayList<>();
        currentWord = word;
        for (int x = 0; x < field.length; x++) {
            for (int y = 0; y < field[x].length; y++) {
                if (field[x][y] == currentWord.charAt(0)) {
                    char[][] testField = Arrays.stream(field).map(char[]::clone).toArray(char[][]::new);
                    testField[x][y] = emptyChar;
                    if (word.length() == 1)
                        allWordOccurrences.add(testField);
                    else
                        findWord(testField,1, x, y);
                }
            }
        }
    }

    void findWord(char[][] field, int letterPos, int posX, int posY) {
        int[][] nextLetterPossibleCoordinates = {{posX, posY + 1}, {posX, posY - 1}, {posX + 1, posY}, {posX - 1, posY}};
        for (int[] nextLetterCoords : nextLetterPossibleCoordinates) {
            int x = nextLetterCoords[0], y = nextLetterCoords[1];
            if (x < 0 || x >= field.length || y < 0 || y >= field.length)
                continue;
            if (field[x][y] == currentWord.charAt(letterPos)) {
                char[][] testField = Arrays.stream(field).map(char[]::clone).toArray(char[][]::new);
                testField[x][y] = emptyChar;
                if (letterPos == currentWord.length() - 1)
                    allWordOccurrences.add(testField);
                else
                    findWord(testField, letterPos + 1, x, y);
            }
        }
    }

    String getRemainingLetters() {
        char[][] resultField = (allWordsCombinations.size() == 0)
                ? field
                : allWordsCombinations.get(0);

        return Arrays.stream(resultField)
                .flatMap(this::getCharacterStreamFromArray)
                .filter(character -> character != emptyChar)
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    private Stream<Character> getCharacterStreamFromArray(char[] chars) {
        return new String(chars).chars().mapToObj(c -> (char) c);
    }
}
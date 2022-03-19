/*
 * A. Горячо-Холодно
 * 
 * // Условие 
 * Ограничение времени 	1 секунда
 * Ограничение памяти 	512Mb
 * Ввод 	стандартный ввод или input.txt
 * Вывод 	стандартный вывод или output.txt
 * 
 * Это интерактивная задача.
 * 
 * Егор и Пётр играют в игру «Горячо-Холодно» на двумерной плоскости. В начале игры Егор
 * загадывает точке с неотрицательными целыми координатами, не превышающими 10^9. После чего
 * Пётр пытается угадать эту точку: на i-м ходу он выбирает некоторую точку с целыми 
 * координатами (xi, yi) и сообщает её Егору. Если эта точка ближе к загаданной, чем
 * предыдущая (xi-1, yi-1), Егор отвечает “1”. В противном случае (в том числе и на самый 
 * первый вопрос Петра в начале игры) Егор отвечает “0”.
 * 
 * Когда Пётр считает, что у него достаточно информации, он останавливает игру и сообщает 
 * ответ (это действие ходом уже не считается). Если ответ правильный и Пётр сделал не более
 * 500 ходов, Пётр считается победителем. В противном случае выиграл Егор. Пётр просит Вас
 * написать программу, которая гарантированно будет выигрывать у Егора.
 * 
 * Гарантируется, что Егор играет честно и не будет менять координаты загаданной точки 
 * в процессе игры.
 * 
 * Протокол взаимодействия
 * 
 * Когда игрок делает ход, он должен вывести два целых числа от 0 до 10^9, разделённых
 * пробелом - координаты очередной точки, о которой он спрашивает. Если игрок хочет 
 * остановить игру и сообщить ответ, он должен вывести символ `A', а после него через пробел -
 * - два целых числа: x и y-координаты загаданной точки, после чего завершить выполнение 
 * программы.
 * 
 * После каждого вывода вы обязаны выводить один символ перевода строки, делать команду 
 * flush, очищая поток вывода, и считывать ответ. Если ваша программа получит на стандартный
 * вход EOF, она обязана завершить выполнение. В противном случае возможно получение 
 * ошибки TimeLimitExceeded.
 * 
 * На каждый ход программа жюри выводит “1” в случае, когда названная программой-игроком
 * точка ближе к загаданной, чем предыдущая, и “0” в противном случае (то есть когда названная
 * точка не ближе предыдущей или предыдущей точки названо не было, то есть если ход первый).
 * 
 * Гарантируется, что координаты загаданной точки целые, неотрицательные и не превосходят 10^9.
 * 
 * Пример взаимодействия
 * 
 * Система Участник
 * 
 *         1 1
 * 
 * 0
 * 
 *         0 0
 * 
 * 0
 * 
 *         20 20
 * 
 * 1
 * 
 *         20 20
 * 
 * 0
 * 
 *         17 239
 * 
 * 1
 * 
 *         17 240
 * 
 * 0
 * 
 *         A 17 239
 * 
 * // Примечания
 * В приведённом примере загадана точка (x = 17, y = 239) и приведён следующий сценарий.
 * 
 *    - Участник называет точку (1, 1), программа жюри отвечает 0, так как это первый ход.
 *    - Участник называет точку (0, 0), которая дальше от точки (x = 17, y = 239), чем 
 *      точка (1, 1), так что программа жюри снова отвечает 0.
 *    - Следующая попытка участника — (20, 20), и сейчас ответ — 1, так как точка ближе
 *      к загаданной.
 *    - Участник снова называет (20, 20), ответ — 0, так как в случае одинакового расстояния
 *      точка не ближе.
 *    - Участник называет точку (17, 239)... которая и была загадана, но просто получает 
 *      ответ 1 — если правильная точка названа не как ответ, то игра продолжается.
 *    - Программа жюри отвечает 0 на точку (17, 240).
 *    - Участник решает рискнуть и называет точку (17, 239) в качестве ответа. 
 *      Заметим, что он не имел нужной информации. Ему просто повезло. Вам на такое везение
 *      рассчитывать не стоит.
 * 
 */

import java.util.*;

public class Test {

    static Scanner scanner;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        target = Arrays.stream(scanner.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();

        System.out.println("A" + findValueOnAxis(Axis.X) + " " + findValueOnAxis(Axis.Y));
    }

    static int findValueOnAxis(Axis axis) {
        int left = 0, right = (int) Math.pow(10, 9);
        setDotAsPrevious(left, axis);
        boolean isLastDotCloser = isDotCloser(right, axis);
        boolean isLastMovedBoundaryRight = true;

        int value = -1;
        while (value == -1) {
            if (isLastDotCloser) {
                if (isLastMovedBoundaryRight) {
                    left = (left + right) / 2;
                    isLastDotCloser = isDotCloser(left, axis);
                    isLastMovedBoundaryRight = false;
                } else {
                    right = (left + right + 1) / 2;
                    isLastDotCloser = isDotCloser(right, axis);
                    isLastMovedBoundaryRight = true;
                }
            } else {
                if (left + 1 == right)
                    value = isLastMovedBoundaryRight ? left : right;

                if (isLastMovedBoundaryRight) {
                    setDotAsPrevious(left, axis);
                    right = (left + right + 1) / 2;
                    isLastDotCloser = isDotCloser(right, axis);
                } else {
                    setDotAsPrevious(right, axis);
                    left = (left + right) / 2;
                    isLastDotCloser = isDotCloser(left, axis);
                }
            }
        }
        return value;
    }

    enum Axis {X, Y}

    static void setDotAsPrevious(int value, Axis axis) {
        isDotCloser(value, axis);
    }

    static final int IS_CLOSER = 1;

    static boolean isDotCloser(int value, Axis axis) {
        int x = (axis == Axis.X) ? value : 0, y = (axis == Axis.Y) ? value: 0;
        System.out.println(x + " " + y + "\n");
        System.out.flush();
        return getAnswer() == IS_CLOSER;
    }

    static int[] target = {0, 99};
    static int[] prevDot = {-1, -1};
    static final int X = 0, Y = 1;

    static int getAnswer(int value, Axis axis) {
        int x = (axis == Axis.X) ? value : 0, y = (axis == Axis.Y) ? value: 0;
        int answer = (prevDot[X] != - 1)
                ? ((Math.pow(target[X] - x, 2) + Math.pow(target[Y] - y, 2) < Math.pow(target[X] - prevDot[X], 2) + Math.pow(target[Y] - prevDot[Y], 2)) ? 1 : 0)
                : 0;
        prevDot = new int[]{x, y};
        return answer;
    }

    static int getAnswer() {
        return Integer.parseInt(scanner.nextLine());
    }
}
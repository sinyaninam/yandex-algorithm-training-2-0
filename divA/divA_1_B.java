/*
 * B. Параллелограмм
 * 
 * // Условие
 * Ограничение времени 	1 секунда
 * Ограничение памяти 	64Mb
 * Ввод 	стандартный ввод или input.txt
 * Вывод 	стандартный вывод или output.txt
 * 
 * На уроке геометрии семиклассники Вася и Петя узнали, что такое параллелограмм. 
 * На перемене после урока они стали играть в игру: Петя называл координаты четырех точек 
 * в произвольном порядке, а Вася должен был ответить, являются ли эти точки вершинами 
 * параллелограмма.
 * 
 * Вася, если честно, не очень понял тему про параллелограммы, и ему требуется программа, 
 * умеющая правильно отвечать на Петины вопросы.
 * 
 * Напомним, что параллелограммом называется четырехугольник, противоположные стороны
 * которого равны и параллельны.
 * 
 * // Формат ввода
 * В первой строке входного файла записано целое число N (1 ≤ N ≤ 10) - количество заданных
 * Петей вопросов. Каждая из N последующих строк содержит описание четырех точек - четыре
 * пары целых чисел X и Y (−100 ≤ X ≤ 100, −100 ≤ Y ≤ 100), обозначающих координаты точки.
 * Гарантируется, что четыре точки, о которых идет речь в одном вопросе, не лежат на одной
 * прямой.
 * 
 * // Формат вывода
 * Для каждого из вопросов выведите "YES", если четыре заданные точки могут образовать 
 * параллелограмм, и "NO" в противном случае. Ответ на каждый из запросов должен быть в 
 * отдельной строке без кавычек. 
 * 
 * // Примеры
 * 3
 * 1 1 4 2 3 0 2 3
 * 1 1 5 2 2 3 3 0
 * 0 0 5 1 6 3 1 2
 * 
 * YES
 * NO
 * YES
 * 
 * // Решение
 * Для проверки, является ли четырехугольник параллелограммом, достаточно убедиться, 
 * например, что его противоположные стороны попарно параллельны. Параллельность можно 
 * определить составлением уравнений прямых, на которых лежат стороны, и проверкой 
 * равенства коэффициента k (наклона прямой) у этих прямых. 
 * Поскольку точки даны в произвольном порядке, следует убедиться, какие точки образуют 
 * стороны, а какие - диагонали. Для этого выбираем две произвольные точки и находим 
 * уравнение прямой, которая проходит через них (y = kx + b). Пусть координаты выбранных 
 * точек - (x1,y1), (x2,y2), координаты оставшихся точек - (x3,y3), (x4,y4). 
 * Если точки находятся на одной вертикали (x1 = x2), то выбранный отрезок является
 * диагональю, если x3 > x1 > x4 или x3 < x1 < x4
 * В противном случае, найдём координаты y точек, лежащих на пересечении прямой y = kx + b
 * и вертикалей, проходящих через точки (x3,y3), (x4,y4), y3' = k*x3 + b, y4' = k*x4 + b;
 * Если (y3 - y3' > 0 и y4 - y4' < 0) или (y3 - y3' < 0 и y4 - y4' > 0), то выбранный
 * отрезок является диагональю.
 * 
 */

import java.util.*;

public class Test {

    static Point[] firstSide, secondSide, thirdSide, fourthSide;
    static Point firstPoint, secondPoint, thirdPoint, fourthPoint;

    public static void main(String args[]) {
        Scanner scan = new Scanner(System.in);
        int length = Integer.parseInt(scan.nextLine());
        String[][] inputArray = new String[length][8];
        for (int i = 0; i < length; i++) {
            String[] input = scan.nextLine().split(" +");
            inputArray[i] = input;
        }

        for (String[] coords : inputArray) {
            firstPoint = new Point(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
            secondPoint = new Point(Integer.parseInt(coords[2]), Integer.parseInt(coords[3]));
            thirdPoint = new Point(Integer.parseInt(coords[4]), Integer.parseInt(coords[5]));
            fourthPoint = new Point(Integer.parseInt(coords[6]), Integer.parseInt(coords[7]));

            defineSides();
            if (areSidesParallel(firstSide, thirdSide) && areSidesParallel(secondSide, fourthSide))
                System.out.println("YES");
            else
                System.out.println("NO");
        }
    }

    private static boolean areSidesParallel(Point[] oneSide, Point[] anotherSide) {
        int x1 = oneSide[0].x, y1 = oneSide[0].y, x2 = oneSide[1].x, y2 = oneSide[1].y,
                x3 = anotherSide[0].x, y3 = anotherSide[0].y, x4 = anotherSide[1].x, y4 = anotherSide[1].y;

        if (x1 == x2 ^ x3 == x4) {
            return false;
        } else {
            if (x1 == x2) {
                return true;
            } else {
                float k1 = (float)(y2 - y1) / (x2 - x1);
                float k2 = (float)(y4 - y3) / (x4 - x3);
                return k1 == k2;
            }
        }
    }

    private static void defineSides() {
        if (isLineASide(firstPoint, secondPoint, thirdPoint, fourthPoint)) {
            firstSide = new Point[]{firstPoint, secondPoint};
            thirdSide = new Point[]{thirdPoint, fourthPoint};
            if (isLineASide(firstPoint, thirdPoint, secondPoint, fourthPoint)) {
                secondSide = new Point[]{firstPoint, thirdPoint};
                fourthSide = new Point[]{secondPoint, fourthPoint};
            } else {
                secondSide = new Point[]{firstPoint, fourthPoint};
                fourthSide = new Point[]{secondPoint, thirdPoint};
            }
        } else {
            firstSide = new Point[]{firstPoint, thirdPoint};
            secondSide = new Point[]{secondPoint, thirdPoint};
            thirdSide = new Point[]{secondPoint, fourthPoint};
            fourthSide = new Point[]{fourthPoint, firstPoint};
        }
    }

    private static boolean isLineASide(Point firstTargetPoint, Point secondTargetPoint, Point firstRemainingPoint, Point lastRemainingPoint) {
        int x1 = firstTargetPoint.x, y1 = firstTargetPoint.y, x2 = secondTargetPoint.x, y2 = secondTargetPoint.y,
                x3 = firstRemainingPoint.x, y3 = firstRemainingPoint.y, x4 = lastRemainingPoint.x, y4 = lastRemainingPoint.y;

        if (x1 != x2) {
            float k = (float)(y2 - y1) / (x2 - x1);
            float b = y1 - k * x1;

            float y3_offset = y3 - k*x3 - b;
            float y4_offset = y4 - k*x4 - b;
            return y3_offset > 0 == y4_offset > 0;
        } else {
            int x3_offset = x3 - x1;
            int x4_offset = x4 - x1;
            return x3_offset > 0 == x4_offset > 0;
        }
    }

    static class Point {
        int x;
        int y;

        Point (int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
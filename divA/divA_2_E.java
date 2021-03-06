/*
 * E. Газон
 * 
 * // Условие
 * Ограничение времени 	1 секунда
 * Ограничение памяти 	64Mb
 * Ввод 	стандартный ввод или input.txt
 * Вывод 	стандартный вывод или output.txt
 * 
 * Фермер Иван с юности следит за своим газоном. Газон можно считать плоскостью, на которой
 * в каждой точке с целыми координатами растет один пучок травы.
 * 
 * В одно из воскресений Иван воспользовался газонокосилкой и постриг некоторый 
 * прямоугольный участок газона. Стороны этого участка параллельны осям координат, а две 
 * противоположные вершины расположены в точках (x1, y1) и (x2, y2). Следует отметить, что 
 * пучки травы, находящиеся на границе этого прямоугольника, также были пострижены.
 * 
 * Довольный результатом Иван купил и установил на газоне дождевальную установку. Она была
 * размещена в точке с координатами (x3, y3) и имела радиус действия струи r. Таким образом,
 * установка начала поливать все пучки, расстояние от которых до точки (x3, y3) не превышало r.
 * 
 * Все было хорошо, но Ивана заинтересовал следующий вопрос: сколько пучков травы оказалось 
 * и пострижено, и полито в это воскресенье?
 * 
 * Требуется написать программу, которая позволит дать ответ на вопрос Ивана.
 * 
 * // Формат ввода
 * В первой строке входного файла содержатся четыре целых числа x1, y1, x2, y2 
 * (−100 000 ≤ x1 < x2 ≤ 100 000; −100 000 ≤ y1 < y2 ≤ 100 000).
 * 
 * Во второй строке входного файла содержатся три целых числа x3, y3, r
 * (−100 000 ≤ x3, y3 ≤ 100 000; 1 ≤ r ≤ 100 000)
 * 
 * // Формат вывода
 * В выходной файл необходимо вывести одно целое число — число пучков травы, которые были
 * и пострижены, и политы.
 * 
 * // Примеры
 * 0 0 5 4
 * 4 0 3
 * 
 * 14
 * 
 * // Решение
 * В теории задачу можно было бы решить за O(1), определив относительное местоположение
 * круга и прямоугольника и точки их пересечения, и посчитав площадь круга, вычитая 
 * "отрезанные" куски, если бы не требовалось найти целочисленное решение. Проверять каждую 
 * из точек круга/прямоугольника на принадлежность обоим - излишество, потому что количество
 * целочисленных точек, принадлежащих и кругу и прямоугольнику на линии (как горизонтали, 
 * так и вертикали) можно посчитать за O(1) следующим алогоритмом:
 * - Рассмотрим горизонтальные линии.
 * - Определим верхние границы прямоугольника и круга, используем наименьшую из них для 
 *   верхнего y-ограничителя. 
 * - Определим нижние границы, используем наибольшую из них для нижнего y-ограничителя.
 * - В пределах этих ограничителей переберём все значения y. Для каждого из них:
 * - Определим левую и правую границы круга. Рассмотрев прямоугольный треугольник, 
 *   образованный текущей горизонталью, вертикалью, проходящей через центр круга, и линией,
 *   соединяющей центр круга с одной из точек пересечения, можно увидеть, что длина 
 *   вертикального катета известна и представляет из себя разницу y-координат текущей 
 *   горизонтали и центра круга, длина гипотенузы - это радиус круга, соответственно по теореме
 *   Пифагора можно вычислить второй катет. Если горизонталь не пересекает круг, то разность
 *   квадратов даст отрицательное значение, в этом случае на данной горизонтали обших точек 
 *   нет и мы переходим к следующей. В остальных случаях получив длину катета, вычисляем левую
 *   и правую границы круга, вычитая/складывая длину катета с координатой x центра круга.
 * - Определим левую и правую границы прямоугольника. Сделать это нужно вне цикла, поскольку
 *   от координаты y они не зависят. 
 * - Из двух левых границ возьмём наибольшую, из двух правых - наименьшую. Между этими 
 *   границами и будут находиться все точки, принадлежащие одновременно и кругу и
 *   прямоугольнику на данной горизонтали. Очевидно, если левая граница получается больше 
 *   правой, то таких точек нет. Суммируем точки для каждой из горизонталей.
 * 
 */

import java.util.*;

public class Test {

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        int[] coords = Arrays.stream(scanner.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        int x1 = coords[0], y1 = coords[1], x2 = coords[2], y2 = coords[3];
        coords = Arrays.stream(scanner.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        int x3 = coords[0], y3 = coords[1], r3 = coords[2];

        long union = 0;
        int rectangleBottomBorder = Math.min(y1, y2), rectangleTopBorder = Math.max(y1, y2);
        int rectangleLeftBorder = Math.min(x1, x2), rectangleRightBorder = Math.max(x1, x2);
        int circleBottomBorder = (int)Math.ceil(y3 - r3), circleTopBorder = (int)Math.floor(y3 + r3);
        for (int y = Math.max(rectangleBottomBorder, circleBottomBorder); y <= Math.min(rectangleTopBorder, circleTopBorder); y++) {
            double sqrt = Math.sqrt(Math.pow(r3, 2) - Math.pow(y - y3, 2));
            if (Double.isNaN(sqrt))
                continue;
            int circleLeftBorder = (int)Math.ceil(-sqrt + x3);
            int circleRightBorder = (int)Math.floor(sqrt + x3);
            int commonLeftBorder = Math.max(rectangleLeftBorder, circleLeftBorder);
            int commonRightBorder = Math.min(rectangleRightBorder, circleRightBorder);
            if (commonRightBorder >= commonLeftBorder)
                union += commonRightBorder - commonLeftBorder + 1;
        }
        System.out.println(union);
    }
}
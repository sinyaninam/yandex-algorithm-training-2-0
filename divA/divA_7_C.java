/*
 * C. Объединение прямоугольников
 * 
 * // Условие
 * Ограничение времени 	2 секунды
 * Ограничение памяти 	64Mb
 * Ввод 	стандартный ввод или input.txt
 * Вывод 	стандартный вывод или output.txt
 * Легенда
 * 
 * На плоскости задано N прямоугольников с вершинами в точках с целыми координатами и
 * сторонами, параллельными осям координат. Необходимо найти площадь их объединения.
 * 
 * // Формат ввода
 * В первой строке входного файла указано число N (0 ≤ N ≤ 1500). В следующих N строках 
 * заданы по 4 целых числа x1, y1, x2, y2 — сначала координаты левого нижнего угла
 * прямоугольника, потом правого верхнего (0 ≤ x1 ≤ x2 ≤ 10^9, 0 ≤ y1 ≤ y2 ≤ 10^9).
 * Обратите внимание, что прямоугольники могут вырождаться в отрезки и даже в точки.
 * 
 * // Формат вывода
 * В выходной файл выведите единственное число — ответ на задачу. 
 * 
 * // Примеры
 * 3
 * 1 1 3 5
 * 5 2 7 4
 * 2 4 6 7
 * 
 * 23
 * 
 */

import java.util.*;

public class Test {

    enum EventType {
        OPENING, CLOSING
    }

    static class RectangleEvent {
        int x, yT, yB;
        EventType type;

        RectangleEvent(int x, int yT, int yB, EventType type) {
            this.x = x;
            this.yT = yT;
            this.yB = yB;
            this.type = type;
        }
    }

    static class IntervalEvent {
        int y;
        EventType type;

        IntervalEvent(int y, EventType type) {
            this.y = y;
            this.type = type;
        }
    }

    static class Interval {
        int first, last;

        Interval(int first, int last) {
            this.first = first;
            this.last = last;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Interval)) return false;
            Interval interval = (Interval) o;
            return first == interval.first &&
                    last == interval.last;
        }

        @Override
        public int hashCode() {
            return Objects.hash(first, last);
        }
    }

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        int rectanglesNum = Integer.parseInt(scanner.nextLine());
        List<RectangleEvent> rectangleEvents = new ArrayList<>();
        for (int i = 0; i < rectanglesNum; i++) {
            int[] rectangleInput = Arrays.stream(scanner.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            int xL = rectangleInput[0], yB = rectangleInput[1], xR = rectangleInput[2], yT = rectangleInput[3];
            rectangleEvents.add(new RectangleEvent(xL, yT, yB, EventType.OPENING));
            rectangleEvents.add(new RectangleEvent(xR, yT, yB, EventType.CLOSING));
        }
        Collections.sort(rectangleEvents, (e1, e2) -> Integer.compare(e1.x, e2.x));

        int unionArea = 0;
        int openedRectangles = 0;
        int prevX = rectangleEvents.get(0).x;
        List<Interval> presentIntervals = new ArrayList<>();
        for (RectangleEvent event : rectangleEvents) {
            if (openedRectangles > 0)
                unionArea += (event.x - prevX) * getIntervalsUnion(presentIntervals);
            switch (event.type) {
                case OPENING:
                    openedRectangles++;
                    presentIntervals.add(new Interval(event.yB, event.yT));
                    break;
                case CLOSING:
                    openedRectangles--;
                    presentIntervals.remove(new Interval(event.yB, event.yT)); // O(n) in the worst case, remember
                    break;
            }
            prevX = event.x;
            System.out.println("area " + unionArea);
        }

    }

    static int getIntervalsUnion(List<Interval> intervals) {
        List<IntervalEvent> intervalEvents = new ArrayList<>();
        intervals.forEach(interval -> {
            intervalEvents.add(new IntervalEvent(interval.first, EventType.OPENING));
            intervalEvents.add(new IntervalEvent(interval.last, EventType.CLOSING));
        });
        Collections.sort(intervalEvents, (e1, e2) -> Integer.compare(e1.y, e2.y));

        int unionLength = 0;
        int openedIntervals = 0;
        int prevY = intervalEvents.get(0).y;
        for (IntervalEvent event : intervalEvents) {
            if (openedIntervals > 0)
                unionLength += event.y - prevY;
            switch (event.type) {
                case OPENING:
                    openedIntervals++;
                    break;
                case CLOSING:
                    openedIntervals--;
                    break;
            }
            prevY = event.y;
        }
        System.out.println(unionLength);
        return unionLength;
    }
}
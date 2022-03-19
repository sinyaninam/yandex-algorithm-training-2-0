/*
 * C. Шахматная доска
 * 
 * // Условие
 * Ограничение времени 	1 секунда
 * Ограничение памяти 	64Mb
 * Ввод 	стандартный ввод или input.txt
 * Вывод 	стандартный вывод или output.txt
 * 
 * Из шахматной доски по границам клеток выпилили связную (не распадающуюся на части)
 * фигуру без дыр. Требуется определить ее периметр.
 * 
 * // Формат ввода
 * Сначала вводится число N (1 ≤ N ≤ 64) – количество выпиленных клеток. В следующих N
 * строках вводятся координаты выпиленных клеток, разделенные пробелом (номер строки и
 * столбца – числа от 1 до 8). Каждая выпиленная клетка указывается один раз.
 * 
 * // Формат вывода
 * Выведите одно число – периметр выпиленной фигуры (сторона клетки равна единице).
 * 
 * // Примеры
 * 3
 * 1 1
 * 1 2
 * 2 1
 * 
 * 8
 * 
 * 1
 * 8 8
 * 
 * 4
 * 
 * // Примечания
 * 1) Вырезан уголок из трех клеток. Сумма длин его сторон равна 8.
 * 2) Вырезана одна клетка. Ее периметр равен 4.
 * 
 * // Решение
 * Каждая новая клетка, добавленная к фигуре, увеличивает её периметр на 4 (по количеству 
 * сторон), но в случае наличия соседних клеток каждая из контактирующих сторон уменьшает 
 * периметр на 2 (поскольку эта сторона перестаёт быть частью периметра и входит в состав
 * двух клеток). Для каждой добавленной клетки можно посчитать количество соседних клеток,
 * которые уже находятся в составе фигуры, поочередно проверив наличие в заведённом словаре
 * каждой из 4х соседних координат, определить на сколько увеличивается/уменьшается периметр 
 * в результате добавления этой клетки и добавить итоговое значение к суммарному периметру.
 * 
 */

import java.util.*;

public class Test {

    private static Set<Cell> cells = new HashSet<>();

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        int length = Integer.parseInt(scanner.nextLine());

        int P = 0;
        for (int i = 0; i < length; i++) {
            Cell cell = new Cell(scanner.nextLine());
            cells.add(cell);
            P += 4 - getNumOfBorderConflicts(cell) * 2;
        }
        System.out.println(P);
    }

    private static int getNumOfBorderConflicts(Cell cell) {
        int x = cell.x;
        int y = cell.y;

        int cnt = 0;
        if (cells.contains(new Cell(x, y + 1)))
            cnt++;
        if (cells.contains(new Cell(x, y - 1)))
            cnt++;
        if (cells.contains(new Cell(x - 1, y)))
            cnt++;
        if (cells.contains(new Cell(x + 1, y)))
            cnt++;
        return cnt;
    }
}

class Cell {
    int x, y;

    Cell(String cell) {
        String[] coordinates = cell.split(" ");
        x = Integer.parseInt(coordinates[0]);
        y = Integer.parseInt(coordinates[1]);
    }

    Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cell)) return false;
        Cell cell = (Cell) o;
        return x == cell.x && y == cell.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
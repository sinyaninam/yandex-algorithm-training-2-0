/*
 * C. Даты
 * 
 * //Условие
 * Ограничение времени 	2 секунды
 * Ограничение памяти 	512Mb
 * Ввод 	стандартный ввод или input.txt
 * Вывод 	стандартный вывод или output.txt
 * 
 * Как известно, два наиболее распространённых формата записи даты — это европейский
 * (сначала день, потом месяц, потом год) и американски (сначала месяц, потом день, потом год).
 * Системный администратор поменял дату на одном из бэкапов и сейчас хочет вернуть дату обратно.
 * Но он не проверил, в каком формате дата используется в системе.
 * Может ли он обойтись без этой информации?
 * 
 * Иначе говоря, вам даётся запись некоторой корректной даты. Требуется выяснить, однозначно ли
 * по этой записи определяется дата даже без дополнительной информации о формате.
 * 
 * // Формат ввода
 * Первая строка входных данных содержит три целых числа — x, y и z (1 ≤ x ≤ 31, 1 ≤ y ≤ 31,
 * 1970 ≤ z ≤ 2069). Гарантируется, что хотя бы в одном формате запись xyz задаёт корректную дату.
 * 
 * // Формат вывода
 * Выведите 1, если дата определяется однозначно, и 0 в противном случае. 
 * 
 * // Примеры
 * 1 2 2003
 * 
 * 0
 * 
 * 2 29 2008
 * 
 * 1
 * 
 */

import java.util.Scanner;

public class Test {

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        String[] input = scanner.nextLine().split(" ");

        int dd = Integer.parseInt(input[0]), mm = Integer.parseInt(input[1]), yy = Integer.parseInt(input[2]);
        System.out.println(((dd > 12) || (mm > 12) || (dd == mm)) ? 1 : 0);
    }
}
/*
 * A. Забавный конфуз
 * 
 * // Условие
 * Ограничение времени 	1 секунда
 * Ограничение памяти 	64Mb
 * Ввод 	стандартный ввод или input.txt
 * Вывод 	стандартный вывод или output.txt
 * 
 * Пусть A — массив, состоящий из N элементов A1, …, AN. Обозначим его максимальное и
 * минимальное значение через max(A) и min(A) соответственно. Вычислим сумму элементов 
 * S, S = A1 + A2 + … + AN. Заменим каждый элемент массива на разницу S и этого элемента.
 * 
 * Такое преобразование массива A назовем операцией Confuse. Напишите программу, которая 
 * по массиву B, полученному в результате K-кратного применения операции Confuse к 
 * некоторому массиву A, вычислит разность max(A)-min(A).
 * 
 * // Формат ввода
 * Первая строка входного файла содержит целые числа N и K, где N — количество элементов
 * массива B (2 ≤ N 10000), а K — количество применений операции Confuse к начальному 
 * массиву A, 1 ≤ K 100. Вторая строка файла содержит N элементов массива B. Элементы
 * массива B — целые числа, принадлежащие диапазону от -2 000 000 000 до 2 000 000 000.
 * 
 * // Формат вывода
 * Единственная строка выходного файла должна содержать целое число - разность max(A) и min(A).
 * 
 * // Примеры
 * 4 2
 * 45 52 47 46
 * 
 * 7
 * 
 * // Решение
 * В результате операции confuse происходит следующее. Обозначим сумму элементов текущего
 * массива константой С. Максимумом нового массива будет число C - min, а минимумом C - max,
 * т.е. индексы максимального и минимального значений меняются местами. При этом их разница
 * останется неизменной: С - min - (C - max) = max - min. Операция confuse никак на неё
 * не влияет, таким образом, всё, что нужно сделать в задаче - найти максимум и минимум
 * текущего массива, количество операций confuse можно игнорировать.
 *
 * Хоть элементы массива со скрипом и пролезают по памяти в integer, перед вычитанием
 * значения нужно привести к типу long, в противном случае есть риск переполнения.
 * 
 */

import java.util.*;
import java.util.stream.Collectors;

public class Test {

    public static void main(String args[]) {
        Scanner scan = new Scanner(System.in);
        scan.nextLine();
        List<Integer> numbers = Arrays.stream(scan.nextLine().split(" ")).map(Integer::parseInt).collect(Collectors.toList());
        if (numbers.size() > 0)
            System.out.println((long)numbers.stream().max(Integer::compareTo).get() - numbers.stream().min(Integer::compareTo).get());
    }
}
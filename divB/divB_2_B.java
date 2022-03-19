/*
 * B. Дома и магазины
 * 
 * // Условие
 * Ограничение времени 	1 секунда
 * Ограничение памяти 	64Mb
 * Ввод 	стандартный ввод или input.txt
 * Вывод 	стандартный вывод или output.txt
 * 
 * На Новом проспекте построили подряд 10 зданий. Каждое здание может быть либо жилым
 * домом, либо магазином, либо офисным зданием.
 * 
 * Но оказалось, что жителям некоторых домов на Новом проспекте слишком далеко приходится 
 * идти до ближайшего магазина. Для разработки плана развития общественного транспорта на
 * Новом проспекте мэр города попросил вас выяснить, какое же наибольшее расстояние 
 * приходится преодолевать жителям Нового проспекта, чтобы дойти от своего дома до 
 * ближайшего магазина.
 * 
 * // Формат ввода
 * Программа получает на вход десять чисел, разделенных пробелами. Каждое число задает 
 * тип здания на Новом проспекте: число 1 обозначает жилой дом, число 2 обозначает магазин,
 * число 0 обозначает офисное здание. Гарантируется, что на Новом проспекте есть хотя бы
 * один жилой дом и хотя бы один магазин.
 * 
 * // Формат вывода
 * Выведите одно целое число: наибольшее расстояние от дома до ближайшего к нему магазина.
 * Расстояние между двумя соседними домами считается равным 1 (то есть если два дома стоят
 * рядом, то между ними расстояние 1, если между двумя домами есть еще один дом, то
 * расстояние между ними равно 2 и т.д.) 
 * 
 * // Примеры
 * 2 0 1 1 0 1 0 2 1 2
 * 
 * 3
 * 
 * // Примечания
 * В примере из условия дальше всего идти до ближайшего магазина жителям четвертого дома:
 * ближайший к их дому магазин находится в первом доме, и им нужно пройти три дома до него.
 * Жителям других домов придется пройти меньшее расстояние до ближайшего магазина, поэтому ответ 3. 
 * 
 */

import java.util.*;
import java.util.stream.Collectors;

public class Test {

    static List<Integer> houses;

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        houses = Arrays.stream(scanner.nextLine().split(" ")).map(Integer::parseInt).collect(Collectors.toList());

        int maxDistance = 0;
        for (int i = 0; i < houses.size(); i++) {
            if (houses.get(i) == 1) {
                int dis = getNearestShopDistance(i);
                if (dis > maxDistance)
                    maxDistance = dis;
            }
        }
        System.out.println(maxDistance);
    }

    public static int getNearestShopDistance(int homePos) {
        int leftShopPos = -1, rightShopPos = -1;
        for (int i = homePos - 1; i >= 0; i--) {
            if (houses.get(i) == 2) {
                leftShopPos = i;
                break;
            }
        }
        for (int i = homePos + 1; i < houses.size(); i++) {
            if (houses.get(i) == 2) {
                rightShopPos = i;
                break;
            }
        }
        if (leftShopPos == -1)
            return rightShopPos - homePos;
        if (rightShopPos == -1)
            return  homePos - leftShopPos;
        return Math.min(homePos - leftShopPos, rightShopPos - homePos);
    }
}
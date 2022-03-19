/*
 * B. Билеты
 * 
 * // Условие
 * Ограничение времени 	1 секунда
 * Ограничение памяти 	64Mb
 * Ввод 	стандартный ввод или input.txt
 * Вывод 	стандартный вывод или output.txt
 * 
 * В одной театральной кассе есть в продаже билеты любой стоимости, выражающейся натуральным
 * числом. При покупке билетов по цене за билет от A до B рублей включительно нужно 
 * дополнительно оплатить сервисный сбор в размере C процентов от номинальной стоимости 
 * билетов (сервисный сбор не обязательно выражается целым числом рублей, но всегда выражается
 * целым числом копеек). При покупке билетов стоимостью менее A рублей за билет, а также
 * более B рублей за билет, сервисный сбор не берется.
 * 
 * У вас есть X рублей и вам нужно K билетов одинаковой цены (цена обязательно должна 
 * выражаться натуральным числом рублей, 0 не считается натуральным). Билеты какого самого
 * дорогого номинала вы можете себе позволить?
 * 
 * // Формат ввода
 * Вводятся целые A, B, C, X, K (1 ≤ A ≤ B ≤ 10^9, 0 ≤ C ≤ 1000, 0 ≤ X ≤ 10^9, 1 ≤ K ≤ 100000).
 * 
 * // Формат вывода
 * Если на имеющиеся деньги невозможно приобрести ни одного билета, выведите 0. Иначе выведите
 * натуральное число – номинальную стоимость приобретённых билетов. 
 * 
 * // Примеры
 * 1 10 0 5 5
 * 
 * 1
 * 
 * 10 100 50 50 5
 * 
 * 9
 * 
 */

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Test {

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        String[] input = scanner.nextLine().split(" ");
        int A = Integer.parseInt(input[0]), B = Integer.parseInt(input[1]), C = Integer.parseInt(input[2]), X = Integer.parseInt(input[3]), K = Integer.parseInt(input[4]);
        int maxPrice = X / K;
        if (maxPrice >= A && maxPrice <= B) {
            int newPrice = (int)(X / (round(K * (1 + (float)C / 100))));
            if (newPrice >= A)
                maxPrice = newPrice;
            else
                maxPrice = A - 1;
        }
        System.out.println(maxPrice);
    }

    public static float round(float d) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
}
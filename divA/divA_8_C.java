/*
 * C. Свинки-копилки
 * 
 * // Условие
 * Ограничение времени 	1 секунда
 * Ограничение памяти 	64Mb
 * Ввод 	стандартный ввод или input.txt
 * Вывод 	стандартный вывод или output.txt
 * 
 * У Васи есть N свинок-копилок, свинки занумерованы числами от 1 до N. Каждая копилка 
 * может быть открыта единственным соответствующим ей ключом или разбита.
 * 
 * Вася положил ключи в некоторые из копилок (он помнит, какой ключ лежит в какой из копилок).
 * Теперь Вася собрался купить машину, а для этого ему нужно достать деньги из всех копилок.
 * При этом он хочет разбить как можно меньшее количество копилок (ведь ему еще нужно копить
 * деньги на квартиру, дачу, вертолет…). Помогите Васе определить, какое минимальное
 * количество копилок нужно разбить.
 * 
 * // Формат ввода
 * В первой строке содержится число N — количество свинок-копилок (1 ≤ N ≤ 100000). Далее
 * идет N строк с описанием того, где лежит ключ от какой копилки: в i-ой из этих строк 
 * записан номер копилки, в которой находится ключ от i-ой копилки.
 * 
 * // Формат вывода
 * Выведите единственное число: минимальное количество копилок, которые необходимо разбить.
 * 
 * // Примеры
 * 4
 * 2
 * 1
 * 2
 * 4
 * 
 * 2
 * 
 * // Примечания
 * Ключи от первой и третьей копилки лежат в копилке 2, ключ от второй — в первой, 
 * а от четвертой — в ней самой.
 * 
 * Чтобы открыть все копилки, достаточно разбить, например, копилки с номерами 1 и 4.
 * 
 */

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Test {

    static class PiggiesMap {

        class PiggyBank {
            boolean isOpened;
            int piggyWithKey;

            PiggyBank(int piggyWithKey) {
                this.piggyWithKey = piggyWithKey;
            }
        }

        HashMap<Integer, PiggyBank> piggies = new HashMap<>();

        void put(int thisPiggy, int piggyWithKey) {
            piggies.put(thisPiggy, new PiggyBank(piggyWithKey));
        }

        Set<Integer> getPiggyIDs() {
            return piggies.keySet();
        }

        int getPiggyWithKey(int piggyId) {
            return piggies.get(piggyId).piggyWithKey;
        }

        void open(int piggyId) {
            piggies.get(piggyId).isOpened = true;
        }

        boolean isPiggyOpened(int piggyId) {
            return piggies.get(piggyId).isOpened;
        }
    }

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        int numOfPiggyBanks = Integer.parseInt(scanner.nextLine());
        PiggiesMap piggiesMap = new PiggiesMap();
        for (int piggyId = 1; piggyId <= numOfPiggyBanks; piggyId++) {
            int piggyWithKey = Integer.parseInt(scanner.nextLine());
            piggiesMap.put(piggyId, piggyWithKey);
        }

        AtomicInteger brokenPiggies = new AtomicInteger(0);
        piggiesMap.getPiggyIDs().forEach(piggyId -> {
            if (piggiesMap.isPiggyOpened(piggyId))
                return;
            int initialPiggy = piggyId;

            piggiesMap.open(piggyId);
            while (!piggiesMap.isPiggyOpened(piggiesMap.getPiggyWithKey(piggyId))) {
                piggyId = piggiesMap.getPiggyWithKey(piggyId);
                piggiesMap.open(piggyId);
            }

            if (piggiesMap.getPiggyWithKey(piggyId) == initialPiggy || piggyId == piggiesMap.getPiggyWithKey(piggyId))
                brokenPiggies.incrementAndGet();
        });
        System.out.println(brokenPiggies);
    }
}
/*
 * D. Древние цивилизации
 * 
 * // Условие
 * Ограничение времени 	1 секунда
 * Ограничение памяти 	64Mb
 * Ввод 	стандартный ввод или input.txt
 * Вывод 	стандартный вывод или output.txt
 * 
 * Недавно Петя занялся изучением древних цивилизаций. Он нашел в энциклопедии даты 
 * рождения и гибели N различных древних цивилизаций и теперь хочет узнать о влиянии 
 * культуры одних цивилизаций на культуру других.
 * 
 * Петя предположил, что между цивилизациями A и B происходил культурный обмен, если они
 * сосуществовали в течение некоторого ненулевого промежутка времени. Например, если 
 * цивилизация A зародилась в 600 году до н.э. и существовала до 400 года до н.э., а
 * цивилизация B зародилась в 450 году до н.э. и существовала до 300 года до н.э., то 
 * культура каждой из этих цивилизаций оказывала влияние на развитие другой цивилизации 
 * в течение 50 лет. В то же время, если цивилизация C зародилась в 400 году до н.э. и 
 * существовала до 50 года до н.э., то она не смогла осуществить культурного обмена с 
 * цивилизацией A, в то время как культурный обмен с цивилизацией B продолжался 
 * в течение 100 лет.
 * 
 * Теперь для выполнения своих исследований Петя хочет найти такую пару цивилизаций, 
 * культурный обмен между которыми имел место на протяжении наименьшего ненулевого 
 * промежутка времени. Помогите ему!
 * 
 * // Формат ввода
 * В первой строке вводится число N – количество цивилизаций, культура которых интересует
 * Петю (1 ≤ N ≤ 100 000). Следующие N строк содержат описание цивилизаций – в каждой строке
 * задаются два целых числа Si и Ei – год зарождения и год гибели соответствующей цивилизации.
 * Все числа не превосходят 10^9 по абсолютной величине, Si < Ei.
 * 
 * // Формат вывода
 * Выведите два числа – номера цивилизаций, периоды существования которых имеют наименьшее
 * ненулевое пересечение. Если никакие две цивилизации не пересекаются во времени, выведите
 * единственное число 0. 
 * 
 * // Примеры
 * 3
 * -600 -400
 * -450 -300
 * -400 -50
 * 
 * 1 2
 * 
 * 2
 * 10 20
 * 15 21
 * 
 * 1 2
 * 
 */

import java.util.*;

public class Test {

    static class Civilisation {

        int id, startDate, endDate;

        Civilisation(int id, int startDate, int endDate) {
            this.id = id;
            this.startDate = startDate;
            this.endDate = endDate;
        }
    }

    static class CivEvent {

        enum Type {
            OPENING, CLOSING
        }

        Civilisation civ;
        Type type;

        CivEvent(Civilisation civ, Type type) {
            this.civ = civ;
            this.type = type;
        }

        int getDate() {
            switch (type) {
                case OPENING:
                    return civ.startDate;
                case CLOSING:
                    return civ.endDate;
                default:
                    throw new RuntimeException("no way");
            }
        }
    }

    static class CivMap {

        int size;

        TreeMap<Integer, Set<Integer>> presentCivs = new TreeMap<>();

        void put(Civilisation civ) {
            if (!presentCivs.containsKey(civ.startDate))
                presentCivs.put(civ.startDate, new HashSet<>(Collections.singletonList(civ.id)));
            else
                presentCivs.get(civ.startDate).add(civ.id);
            size++;
        }

        void remove(Civilisation civ) {
            if (presentCivs.get(civ.startDate).size() == 1)
                presentCivs.remove(civ.startDate);
            else
                presentCivs.get(civ.startDate).remove(civ.id);
            size--;
        }

        int size() {
            return size;
        }

        int getMostRecentStartDate() {
            return presentCivs.lastKey();
        }

        Set<Integer> getMostRecentCivs() {
            return presentCivs.get(getMostRecentStartDate());
        }

        int getStartDatePrecedingMostRecent() {
            return presentCivs.lowerKey(presentCivs.lastKey());
        }

        Set<Integer> getCivsPrecedingMostRecent() {
            return presentCivs.get(getStartDatePrecedingMostRecent());
        }
    }


    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        int civNum = Integer.parseInt(scanner.nextLine());
        List<CivEvent> civEvents = new ArrayList<>();
        for (int i = 0; i < civNum; i++) {
            int[] input = Arrays.stream(scanner.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            Civilisation civ = new Civilisation(i, input[0], input[1]);
            civEvents.add(new CivEvent(civ, CivEvent.Type.OPENING));
            civEvents.add(new CivEvent(civ, CivEvent.Type.CLOSING));
        }
        Collections.sort(civEvents, (e1, e2) -> Integer.compare(e1.getDate(), e2.getDate()));

        CivMap presentCivs = new CivMap();
        int minDuration = Integer.MAX_VALUE;
        Set<Integer> civsWithShortestCulturalChange = new HashSet<>();
        for (CivEvent event : civEvents) {
            switch (event.type) {
                case OPENING:
                    presentCivs.put(event.civ);
                    break;
                case CLOSING:
                    Set<Integer> mostRecentCivs = presentCivs.getMostRecentCivs();
                    if (presentCivs.size() > 1) {
                        if (mostRecentCivs.size() == 1 && mostRecentCivs.stream().findFirst().get() == event.civ.id)
                            mostRecentCivs = presentCivs.getCivsPrecedingMostRecent();
                        if (event.getDate() - presentCivs.getMostRecentStartDate() < minDuration) {
                            civsWithShortestCulturalChange.clear();
                            minDuration = event.getDate() - presentCivs.getMostRecentStartDate();
                        }
                        if (event.getDate() - presentCivs.getMostRecentStartDate() == minDuration) {
                            civsWithShortestCulturalChange.addAll(mostRecentCivs);
                            civsWithShortestCulturalChange.add(event.civ.id);
                        }
                    }
                    presentCivs.remove(event.civ);
            }
        }
        System.out.println();
        System.out.println(minDuration);
        System.out.println(civsWithShortestCulturalChange);
    }
}
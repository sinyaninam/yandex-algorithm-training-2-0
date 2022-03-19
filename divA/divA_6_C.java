/*
 * C. Проходной балл на олимпиаду
 * 
 * // Условие
 * Ограничение времени 	1 секунда
 * Ограничение памяти 	64Mb
 * Ввод 	стандартный ввод или input.txt
 * Вывод 	стандартный вывод или output.txt
 * 
 * Одна Очень Престижная Олимпиада, как и все престижные олимпиады в последнее время, состоит
 * из двух туров - регионального и заключительного. Правила отбора во второй тур 
 * (заключительный этап) просты:
 * 
 * 1. Призеры олимпиады прошлого года приглашаются на заключительный этап вне зависимости 
 *    от набранных ими в первом туре баллов.
 * 2. Все участники, набравшие не меньше баллов, чем установленный жюри проходной балл, 
 *    проходят во второй тур.
 * 3. Если в каком-либо из регионов ни один участник по первым двум правилам во второй тур
 *    не прошел, то на заключительный этап приглашается участник из этого региона, набравший в 
 *    нем максимальное количество баллов (это не касается регионов, от которых участников 
 *    не было).
 * 
 * На второй тур можно пригласить не более M участников.
 * 
 * Известно, что никакие два участника не набрали одинаковое количество баллов. По информации
 * о результатах первого тура помогите жюри установить минимально возможный проходной балл,
 * при котором все правила отбора будут выполнены.
 * 
 * // Формат ввода
 * В первой строке входного файла содержатся три целых числа N, M и R - число участников 
 * первого тура, максимально возможное число участников второго тура и число регионов, из 
 * которых могли быть участники (1 ≤ M ≤ N). Далее в N строках содержатся результаты каждого
 * из участников. Каждая строка состоит из четырех целых чисел. Сначала идет id - уникальный 
 * идентификатор участника (1 ≤ id ≤ N), далее номер региона region, в котором данный участник
 * учится (1 ≤ region ≤ R), затем score - число баллов, набранных участником, четвертое число
 * равно 1, если участник является призером олимпиады прошлого года, и 0 - в противном случае.
 * 
 * Гарантируется, что все идентификаторы участников различны, никакие два участника не
 * набрали одинаковое число баллов, и выполнить все правила отбора возможно.
 * 
 * // Формат вывода
 * Выведите одно число - минимальный проходной балл, который можно установить.
 * 
 * // Примеры
 * 9 6 5
 * 6 1 799 0
 * 2 4 995 0
 * 1 4 989 1
 * 7 2 538 0
 * 5 4 984 0
 * 8 2 1000 0
 * 3 2 998 0
 * 4 2 823 1
 * 9 1 543 0
 * 
 * 985
 * 
 */

import java.util.*;
import java.util.stream.Collectors;

public class Test {

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        String[] input = scanner.nextLine().split(" ");
        int N = Integer.parseInt(input[0]), M = Integer.parseInt(input[1]);
        List<Participant> participants = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            input = scanner.nextLine().split(" ");
            participants.add(new Participant(input[0], input[1], input[2], input[3]));
        }

        Map<Integer, List<Participant>> participantsByRegion = participants.stream()
                        .collect(Collectors.groupingBy(Participant::getRegion, Collectors.toList()));

        participantsByRegion.values().stream()
                .forEach(participantsInRegion -> {
                    boolean hasWinnersInRegion = participantsInRegion.stream().anyMatch(Participant::isWinner);
                    if (!hasWinnersInRegion)
                        participantsInRegion.stream()
                                .max(Comparator.naturalOrder())
                                .get().isWinner = true;
                });
        long preliminaryWinnersCnt = participants.stream()
                .filter(Participant::isWinner)
                .count();
        int passingScore = participants.stream()
                .filter(Participant::isNotWinner)
                .sorted(Comparator.reverseOrder())
                .skip(M - preliminaryWinnersCnt)
                .findFirst().get().score + 1;
        System.out.println(passingScore);
    }
}

class Participant implements Comparable<Participant> {
    int id;
    int region;
    int score;
    boolean isWinner;

    Participant(String id, String region, String score, String isWinner) {
        this.id = Integer.parseInt(id);
        this.region = Integer.parseInt(region);
        this.score = Integer.parseInt(score);
        this.isWinner = isWinner.equals("1");
    }

    int getRegion() {
        return region;
    }

    boolean isWinner() {
        return isWinner;
    }

    boolean isNotWinner() {
        return !isWinner;
    }

    @Override
    public int compareTo(Participant o) {
        return Integer.compare(this.score, o.score);
    }
}
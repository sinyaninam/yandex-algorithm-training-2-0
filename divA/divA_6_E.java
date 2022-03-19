/*
 * E. Выборы
 * 
 * // Условие
 * Ограничение времени 	2 секунды
 * Ограничение памяти 	64Mb
 * Ввод 	стандартный ввод или input.txt
 * Вывод 	стандартный вывод или output.txt
 * 
 * В одной демократической стране приближаются парламентские выборы. Выборы проходят по 
 * следующей схеме: каждый житель страны, достигший восемнадцатилетнего возраста, отдает 
 * свой голос за одну из политических партий. После этого партия, которая набрала максимальное
 * количество голосов, считается победившей на выборах и формирует правительство. Если 
 * несколько партий набрали одинаковое максимальное количество голосов, то они должны 
 * сформировать коалиционное правительство, что обычно приводит к длительным переговорам.
 * 
 * Один бизнесмен решил выгодно вложить свои средства и собрался поддержать на выборах
 * некоторые партии. В результате поддержки он планирует добиться победы одной из этих партий,
 * которая затем сформирует правительство, которое будет действовать в его интересах.
 * При этом возможность формирования коалиционного правительства его не устраивает, поэтому
 * он планирует добиться строгой победы одной из партий.
 * 
 * Чтобы повлиять на исход выборов, бизнесмен собирается выделить деньги на агитационную
 * работу среди жителей страны. Исследование рынка показало, что для того, чтобы один житель
 * сменил свои политические воззрения, требуется потратить одну условную единицу. Кроме того,
 * чтобы i-я партия в случае победы сформировала правительство, которое будет действовать
 * в интересах бизнесмена, необходимо дать лидеру этой партии взятку в размере pi условных 
 * единиц. При этом некоторые партии оказались идеологически устойчивыми и не согласны на 
 * сотрудничество с бизнесменом ни за какие деньги.
 * 
 * По результатам последних опросов известно, сколько граждан планируют проголосовать за
 * каждую партию перед началом агитационной компании. Помогите бизнесмену выбрать, какую
 * партию следует подкупить, и какое количество граждан придется убедить сменить свои
 * политические воззрения, чтобы выбранная партия победила, учитывая, что бизнесмен хочет 
 * потратить на всю операцию минимальное количество денег.
 * 
 * // Формат ввода
 * В первой строке вводится целое число n – количество партий (1 ≤ n ≤ 10^5). Следующие 
 * n строк описывают партии. Каждая из этих строк содержит по два целых числа: vi – количество
 * жителей, которые собираются проголосовать за эту партию перед началом агитационной компании,
 * и pi – взятка, которую необходимо дать лидеру партии для того, чтобы сформированное ей
 * в случае победы правительство действовало в интересах бизнесмена 
 * (1 ≤ vi ≤ 10^6, 1 ≤ pi ≤ 10^6 или pi = -1).
 * Если партия является идеологически устойчивой, то pi равно -1. Гарантируется, что 
 * хотя бы одно pi не равно -1.
 * 
 * // Формат вывода
 * В первой строке выведите минимальную сумму, которую придется потратить бизнесмену. Во
 * второй строке выведите номер партии, лидеру которой следует дать взятку. В третьей 
 * строке выведите n целых чисел – количество голосов, которые будут отданы за каждую из
 * партий после осуществления операции. Если оптимальных решений несколько, выведите любое.
 * 
 * // Примеры
 * 3
 * 7 -1
 * 2 8
 * 1 2
 * 
 * 6
 * 3
 * 3 2 5 
 * 
 */

import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.function.Predicate;

public class Test {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int partiesNum = Integer.parseInt(scanner.nextLine());
        List<Party> parties = new ArrayList<>();
        for (int i = 1; i <= partiesNum; i++) {
            int[] input = Arrays.stream(scanner.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            int votes = input[0], bribe = input[1];
            parties.add(new Party(i, votes, bribe));
        }
        Collections.sort(parties);

        int[] votesPrefixSum = new int[partiesNum + 1];
        for (int i = 1; i <= parties.size(); i++) {
            votesPrefixSum[i] = votesPrefixSum[i - 1] + parties.get(parties.size() - i).votes;
        }

        AtomicInteger minBribe = new AtomicInteger(Integer.MAX_VALUE), opponentVotes = new AtomicInteger(-1);
        AtomicReference<Party> bestParty = new AtomicReference<>();
        parties.stream()
                .filter(Party::isCorrupt)
                .forEach(party -> {
                    AtomicInteger purchasedVotes = new AtomicInteger(0);
                    int highestOpponentVotes = rightmostBS(party.votes, parties.get(parties.size() - 1).votes,
                            votes -> {
                                int leftmostPartyWithMoreVotes = leftmostBS(0, parties.size() - 1, index -> parties.get(index).votes > votes);
                                int numOfPartiesWithMoreVotes = (leftmostPartyWithMoreVotes != - 1)
                                        ? parties.size() - leftmostPartyWithMoreVotes
                                        : 0;
                                purchasedVotes.set(votesPrefixSum[numOfPartiesWithMoreVotes] - votes * numOfPartiesWithMoreVotes);
                                return party.votes + purchasedVotes.get() > votes;
                            });
                    int diffVotes = party.votes + purchasedVotes.get() - highestOpponentVotes;
                    if (diffVotes > 2)
                        purchasedVotes.set(purchasedVotes.get() - (diffVotes - 2));
                    if (purchasedVotes.get() + party.bribe < minBribe.get()) {
                        minBribe.set(purchasedVotes.get() + party.bribe);
                        opponentVotes.set(highestOpponentVotes);
                        bestParty.set(party);
                    }
                });

        System.out.println(minBribe);
        System.out.println(bestParty.get().id);

        int leftmostPartyWithMoreVotes = leftmostBS(0, parties.size() - 1, index -> parties.get(index).votes > opponentVotes.get());
        int numOfPartiesWithMoreVotes = (leftmostPartyWithMoreVotes != - 1)
                ? parties.size() - leftmostPartyWithMoreVotes
                : 0;
        int purchasedVotes = votesPrefixSum[numOfPartiesWithMoreVotes] - opponentVotes.get() * numOfPartiesWithMoreVotes;
        AtomicInteger redundantVotes = new AtomicInteger(Math.max(0, bestParty.get().votes + purchasedVotes - opponentVotes.get() - 2));
        bestParty.get().votes += purchasedVotes - redundantVotes.get();
        parties.stream().sorted((party, otherParty) -> Integer.compare(party.id, otherParty.id)).forEach(party -> {
            if (party.votes > opponentVotes.get() && party != bestParty.get()) {
                party.votes = opponentVotes.get();
                if (redundantVotes.get() > 0) {
                    party.votes++;
                    redundantVotes.decrementAndGet();
                }
            }
            System.out.print(party.votes + " ");
        });
    }

    static int leftmostBS(int start, int end, Predicate<Integer> predicate) {
        if (!predicate.test(end))
            return - 1;
        while (start < end) {
            int mid = (start + end) / 2;
            if (predicate.test(mid))
                end = mid;
            else
                start = mid + 1;
        }
        return start;
    }

    static int rightmostBS(int start, int end, Predicate<Integer> predicate) {
        if (!predicate.test(start))
            return -1;
        while (start < end) {
            int mid = (start + end + 1) / 2;
            if (predicate.test(mid))
                start = mid;
            else
                end = mid - 1;
        }
        return end;
    }

    static class Party implements Comparable<Party> {
        int id, votes, bribe;

        Party(int id, int votes, int bribe) {
            this.id = id;
            this.votes = votes;
            this.bribe = bribe;
        }

        boolean isCorrupt() {
            return bribe != -1;
        }

        @Override
        public int compareTo(Party o) {
            return Integer.compare(this.votes, o.votes);
        }
    }
}

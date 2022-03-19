/*
 * A. Катание на автобусах
 * 
 * // Условие
 * Ограничение времени 	2 секунды
 * Ограничение памяти 	64Mb
 * Ввод 	стандартный ввод или input.txt
 * Вывод 	стандартный вывод или output.txt
 * 
 * В городе n автобусных остановок, через которые проходят k кольцевых автобусных маршрутов.
 * Каждый маршрут задается списком номеров остановок, через которые он проходит, i-ый маршрут
 * проходит по остановкам ai, 1, ai, 2, …, ai, li (в этом порядке). По маршруту ходит ровно
 * один автобус. В момент времени 0 этот автобус находится на остановке ai1. На то, чтобы
 * доехать до следующей на своем маршруте остановки, автобус тратит ровно одну минуту.
 * Временем стоянки автобуса на остановке можно пренебречь. Все маршруты кольцевые, то есть
 * через минуту после остановки ai, li автобус оказывается на остановке ai, 1 и едет по
 * маршруту еще раз.
 * 
 * Несколько человек в этом городе решили покататься на автобусах. При этом каждый из них
 * составил план своего катания. План j-го человека состоит из остановки bj, на которой 
 * человек начнет свое катание и последовательности чисел cj, 1, cj, 2, …, cj, mj. Эти числа
 * означают следующее: в момент времени 0 человек придет на остановку bj и дождется ближайшего
 * автобуса (если в этот момент какой-то автобус находится на остановке bj, человек сядет 
 * в него). На этом автобусе он проедет cj, 1 остановок, после чего выйдет и дождется
 * следующего автобуса на той остановке, где он окажется. На нем он проедет cj, 2 остановок,
 * снова выйдет и снова дождется следующего автобуса. И так далее. Если в какой-то момент 
 * к остановке подъедет сразу несколько автобусов, то человек сядет в автобус с минимальным
 * номером маршрута. Когда человек выходит из автобуса на какой-то остановке, он может 
 * уехать с этой остановки не раньше, чем через минуту.
 * 
 * Для каждого человека определите, через сколько минут после начального момента и на 
 * какой остановке закончится его катание.
 * 
 * // Формат ввода
 * Во входном файле записано сначала число n, затем число k. Далее записано k строк, 
 * задающих автобусные маршруты. Каждая строка начинается с числа li, задающего длину маршрута,
 * затем идет список остановок, через которые проходит маршрут: ai, 1, ai, 2, …, ai, li. 
 * Маршрут может несколько раз проходить через одну и ту же остановку.
 * 
 * Далее идет число p – количество людей, и затем p строк, задающих планы людей. Каждая 
 * строка содержит сначала числа bj – номер начальной остановки и mj – количество чисел в 
 * последовательности. Затем идут числа cj, 1, cj, 2, …, cj, mj.
 * 
 * Все числа во входном файле натуральные и не превышают 50.
 * 
 * // Формат вывода
 * В выходной файл для каждого человека выведите два числа: время в минутах, когда
 * закончится его катание, и номер остановки, на которой это произойдет. Если же человек 
 * не сможет реализовать свой план до конца (на какой-либо остановке он не дождется автобуса),
 * выведите для него два нуля. 
 * 
 * // Примеры
 * 6 4
 * 4 1 2 3 5
 * 2 3 4
 * 5 5 2 1 3 2
 * 2 4 3
 * 3
 * 1 4 1 2 3 4
 * 2 1 1
 * 6 3 1 2 3
 * 
 * 20 1
 * 2 3
 * 0 0
 * 
 */

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.*;

public class Test {

    static Map<Integer, Route> routes = new TreeMap<>();

    static class Route {
        List<Integer> stops;
        Map<Integer, List<Integer>> stopOffsets;

        Route(List<Integer> stops) {
            this.stops = stops;
            stopOffsets = IntStream.range(0, stops.size()).boxed()
                    .collect(Collectors.groupingBy(stops::get, Collectors.toList()));
        }

        boolean hasStop(int stopNumber) {
            return stopOffsets.containsKey(stopNumber);
        }

        int getStopNumberAt(int time) {
            return stops.get(time % stops.size());
        }

        int getWaitingTime(int currentTime, int targetStop) {
            int currentTimeOffset = currentTime % stops.size();
            List<Integer> targetStopOffsets = stopOffsets.get(targetStop);
            int offsetIndex = leftmostMoreThanBS(targetStopOffsets, currentTimeOffset);
            if (offsetIndex == -1)
                return (stops.size() + targetStopOffsets.get(0)) - currentTimeOffset; //stop.size == time of this route
            else
                return targetStopOffsets.get(offsetIndex) - currentTimeOffset;
        }
    }

    static int leftmostMoreThanBS(List<Integer> list, int target) {
        if (list.get(list.size() - 1) <= target)
            return -1;
        int l = 0;
        int r = list.size() - 1;
        while (l < r) {
            int mid = (l + r) / 2;
            if (list.get(mid) > target)
                r = mid;
            else
                l = mid + 1;
        }
        return l;
    }

    static int getClosestRouteNum(int currentTime, int targetStop) {
        AtomicInteger closestRouteNum = new AtomicInteger(-1);
        AtomicInteger minArrivalTime = new AtomicInteger(Integer.MAX_VALUE);
        routes.entrySet().stream()
                .filter(route -> route.getValue().hasStop(targetStop))
                .forEach(route -> {
                    int busArrivesIn = route.getValue().getWaitingTime(currentTime, targetStop);
                    if (busArrivesIn < minArrivalTime.get()) {
                        minArrivalTime.set(busArrivesIn);
                        closestRouteNum.set(route.getKey());
                    }
                });
        return closestRouteNum.intValue();
    }

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        int numberOfRoutes = Integer.parseInt(scanner.nextLine().split(" ")[1]);


        IntStream.range(0, numberOfRoutes).forEach(routeNumber -> {
            String[] routeInput = scanner.nextLine().split(" +");
            List<Integer> stops = Arrays.stream(routeInput).skip(1).map(Integer::parseInt).collect(Collectors.toList());
            Route route = new Route(stops);
            routes.put(routeNumber, route);
        });

        int passengersNum = Integer.parseInt(scanner.nextLine());
        StringBuilder sb = new StringBuilder();
        IntStream.range(0, passengersNum).forEach(passenger -> {
            String[] passengerInput = scanner.nextLine().split(" +");
            int currentStop = Integer.parseInt(passengerInput[0]);
            int time = -1;
            for (int i = 2; i < passengerInput.length; i++) {
                Route incomingBusRoute = routes.get(getClosestRouteNum(time, currentStop));
                if (incomingBusRoute == null)
                    break;
                time += incomingBusRoute.getWaitingTime(time, currentStop) + Integer.parseInt(passengerInput[i]);
                currentStop = incomingBusRoute.getStopNumberAt(time);
            }

            if (time == -1)
                sb.append("0 0\n");
            else
                sb.append(time).append(" ").append(currentStop).append("\n");
        });
        System.out.println(sb);
    }
}
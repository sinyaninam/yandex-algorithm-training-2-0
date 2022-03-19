/*
 * A. Стильная одежда 2
 * 
 * // Условие
 * Ограничение времени 	2 секунды
 * Ограничение памяти 	256Mb
 * Ввод 	стандартный ввод или input.txt
 * Вывод 	стандартный вывод или output.txt
 * 
 * Глеб обожает шоппинг. Как-то раз он загорелся идеей подобрать себе кепку, майку, штаны 
 * и ботинки так, чтобы выглядеть в них максимально стильно. В понимании Глеба стильность 
 * одежды тем больше, чем меньше разница в цвете элементов его одежды.
 * 
 * В наличии имеется N1 кепок, N2 маек, N3 штанов и N4 пар ботинок (1 ≤ Ni ≤ 100000). Про 
 * каждый элемент одежды известен его цвет (целое число от 1 до 100000). Комплект одежды — это
 * одна кепка, майка, штаны и одна пара ботинок. Каждый комплект характеризуется максимальной
 * разницей между любыми двумя его элементами. Помогите Глебу выбрать максимально стильный
 * комплект, то есть комплект с минимальной разницей цветов.
 * 
 * // Формат ввода
 * Для каждого типа одежды i (i=1,2,3,4) сначала вводится количество Ni элементов одежды 
 * этого типа, далее в следующей строке — последовательность из Ni целых чисел, описывающих
 * цвета элементов. Все четыре типа подаются на вход последовательно, начиная с кепок и 
 * заканчивая ботинками. Все вводимые числа целые, положительные и не превосходят 100000.
 * 
 * // Формат вывода
 * Выведите четыре целых числа — цвета соответственно для кепки, майки, штанов и ботинок, 
 * которые должен выбрать Глеб из имеющихся для того, чтобы выглядеть наиболее стильно.
 * Если ответов несколько, выведите любой. 
 * 
 * // Примеры
 * 3
 * 1 2 3
 * 2
 * 1 3
 * 2
 * 3 4
 * 2
 * 2 3
 * 
 * 3 3 3 3 
 * 
 * 1
 * 5
 * 4
 * 3 6 7 10
 * 4
 * 18 3 9 11
 * 1
 * 20
 * 
 * 5 6 9 20 
 * 
 * // Решение
 * Решения два, одно хитромудрое, второе попроще. Как водится, хитромудрое работает медленнее.
 * 
 * Первое решение:
 * 1) Для каждого цвета в наборе первого типа одежды произведем следующие операции. 
 *    Во всех последующих наборах бинарным поиском найдём ближайший левый цвет и ближайший 
 *    правый. И того, и другого может не оказаться, есть следующие случаи:
 * а) В наборе есть точно такой же цвет, т.е. нет необходимости искать ближайшие.
 * б) Значения цвета в наборе строго меньше выбранного цвета, соответственно из ближайщих 
 *    есть только левый.
 * в) Значения цвета в наборе строго больше выбранного цвета, аналогично, но ближайший 
 *    цвет правый.
 * г) Есть оба ближайших цвета.
 * Задача в том, чтобы найти общие границы цветов в других наборах одежды для заданного цвета
 * из первого набора. 
 * Первый случай отбрасываем, поскольку совпадающий цвет в любом случае попадёт в границы.
 * Второй и третий случаи позволяют использовать ближайший левый/правый цвет в качестве 
 * соответствующей границы, поскольку для этого набора они являются ближайшими к заданному
 * цвету в принципе. Из всех наборов для общей левой границы выбираем наименьшее значение,
 * для правой - наибольшее.
 * Последний случай неочевиден, поскольку выбор ближайшей границы из двух может не обеспечить
 * минимальную разницу цветов - в том случае, если "дальняя" граница уже внутри общих, 
 * обеспеченных другими наборами, либо её "непокрытая" границами других наборов часть меньше,
 * чем разница между цветом ближней границы и заданным цветом.
 * В худшем случае по условиям задачи можно ожидать 4 набора последнего типа и можно было 
 * бы просто перебрать все комбинации (левая и правая граница для каждого набора - 2^4 = 16
 * комбинаций), но тогда это было бы не хитромудрое решение. Все комбинации, впрочем, 
 * перебирать нет необходимости - если отсортировать все границы, начальной комбинацией
 * принять "у каждого набора выбрана левая граница" и далее поочередно менять левую границу
 * на правую. Поскольку правые границы отсортированы по возрастанию, то каждая новая правая
 * граница "покрывает" предыдущую и исключает возможность предыдущего набора одежды влиять
 * на общую границу. Вместо 2^n таким образом получаем 2*n комбинаций - потребуется всего 
 * один проход.
 * К сожалению в стандартных библиотеках отсутствует SortedList, а TreeSet применить не 
 * выйдет из-за возможного наличия дубликатов (хотя, можно извернуться и вместо голого 
 * значения границы хранить объект-обертку с указанным номером набора), городить свой 
 * велосипед лень, поэтому в текущей реализации максимальные/минимальные значения границ
 * в массиве после добавления/удаления границы ищутся заново - и в этой части алгоритма 
 * получаем O(n^2) вместо O(n*log(n)).
 * Высчитав общие границы, переберем ещё раз каждый набор и определим, какой цвет будет 
 * в итоговом наборе.
 * Из "итоговых наборов" выберем тот, у которого разница границ меньше. 
 * 
 * Второе решение. Объединить все четыре набора в один отсортированный массив. Двигаясь 
 * слева направо, сохраняем значения цветов из разных наборов в массив "текущей одежды" - 
 * - сначала сдвигаем правую границу, пока в массив не попадут цвета из всех 4 наборов, 
 * потом сдвигаем левую границу, пока в массиве присутствуют цвета из всех 4 наборов, 
 * обеспечивая таким образом минимально возможные интервалы между цветами. Чередуя сдвиг 
 * левой и правой границы перебираем весь объединённый массив.
 * 
 */

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Test {

    static Scanner scanner = new Scanner(System.in);;

    public static void main(String args[]) {
        List<Integer> c1 = getClothingList(), c2 = getClothingList(), c3 = getClothingList(), c4 = getClothingList();

        AtomicInteger minDiff = new AtomicInteger(Integer.MAX_VALUE);
        List<Integer> clothingList = new ArrayList<>();
        c1.forEach(i -> {
            List<Borders> bordersList = Arrays.asList(new Borders(c2, i), new Borders(c3, i), new Borders(c4, i));
            Borders commonBorders = Borders.getCommonBorders(bordersList, i);
            if (commonBorders.right.get() - commonBorders.left.get() < minDiff.get()) {
                minDiff.set(commonBorders.right.get() - commonBorders.left.get());
                clothingList.clear();
                clothingList.add(i);
                bordersList.forEach(borders -> {
                    switch (getBorderType(commonBorders, borders)) {
                        case NO_BORDERS:
                            clothingList.add(i);
                            break;
                        case LEFT_BORDER_INSIDE:
                            clothingList.add(borders.left.get());
                            break;
                        case RIGHT_BORDER_INSIDE:
                            clothingList.add(borders.right.get());
                    }
                });
            }
        });
        String result = clothingList.stream().map(String::valueOf).collect(Collectors.joining(" "));
        System.out.println(result);
    }

    enum BorderType {
        NO_BORDERS, LEFT_BORDER_INSIDE, RIGHT_BORDER_INSIDE
    }

    static BorderType getBorderType(Borders commonBorders, Borders currentBorders) {
        if (!currentBorders.left.isPresent() && !currentBorders.right.isPresent())
            return BorderType.NO_BORDERS;
        if (currentBorders.left.isPresent() && commonBorders.isInBorders(currentBorders.left.get()))
            return BorderType.LEFT_BORDER_INSIDE;
        if (currentBorders.right.isPresent() && commonBorders.isInBorders(currentBorders.right.get()))
            return BorderType.RIGHT_BORDER_INSIDE;
        throw new RuntimeException("Miscalculated common borders");
    }

    static List<Integer> getClothingList() {
        scanner.nextLine();
        return Arrays.stream(scanner.nextLine().split(" ")).distinct().map(Integer::parseInt).sorted().collect(Collectors.toList());
    }
}

class Borders {
    Optional<Integer> left = Optional.empty(), right = Optional.empty();

    Borders(List<Integer> list, int target) {
        int BSIndex = Collections.binarySearch(list, target);
        if (BSIndex < 0) {
            leftMost(list, BSIndex).ifPresent(border -> left = Optional.of(border));
            rightMost(list, BSIndex).ifPresent(border -> right = Optional.of(border));
        }
    }

    Borders(int left, int right) {
        this.left = Optional.of(left);
        this.right = Optional.of(right);
    }

    private Optional<Integer> leftMost(List<Integer> list, int BSIndex) {
        int insertIndex = - BSIndex - 1;
        return (insertIndex == 0) ? Optional.empty() : Optional.of(list.get(insertIndex - 1));
    }

    private Optional<Integer> rightMost(List<Integer> list, int BSIndex) {
        int insertIndex = - BSIndex - 1;
        return (insertIndex == list.size()) ? Optional.empty() : Optional.of(list.get(insertIndex));
    }

    static Borders getCommonBorders(List<Borders> list, int target) {
        List<Event> events = new ArrayList<>();
        list.stream()
                .filter(borders -> borders.left.isPresent() || borders.right.isPresent())
                .forEach(borders -> {
                    borders.left.ifPresent(border -> events.add(new Event(Event.Type.START, borders, border)));
                    borders.right.ifPresent(border -> events.add(new Event(Event.Type.END, borders, border)));
                });
        Collections.sort(events);

        ArrayList<Integer> leftBorders = new ArrayList<>(), rightBorders = new ArrayList<>();
        AtomicInteger minDiff = new AtomicInteger(Integer.MAX_VALUE),
                leftBorder = new AtomicInteger(-1), rightBorder = new AtomicInteger(-1);
        events.forEach(event -> {
            switch (event.type) {
                case START:
                    leftBorders.add(event.border);
                    break;
                case END:
                    if (rightBorders.isEmpty())
                        leftBorders.stream().min(Integer::compareTo).ifPresent(border -> {
                            minDiff.set(target - border);
                            leftBorder.set(border);
                            rightBorder.set(target);
                        });

                    rightBorders.add(event.border);
                    event.borders.left.ifPresent(leftBorders::remove);

                    int left = leftBorders.stream().min(Integer::compareTo).orElse(target), right = event.border;
                    if (left - right < minDiff.get()) {
                        minDiff.set(left - right);
                        leftBorder.set(left);
                        rightBorder.set(right);
                    }
            }

            if (rightBorders.isEmpty())
                leftBorders.stream().min(Integer::compareTo).ifPresent(border -> {
                    leftBorder.set(border);
                    rightBorder.set(target);
                });
        });

        return (events.isEmpty())
                ? new Borders(target, target)
                : new Borders(leftBorder.get(), rightBorder.get());
    }

    static class Event implements Comparable<Event> {

        enum Type {START, END}

        Borders borders;
        Type type;
        int border;

        Event(Type type, Borders borders, int border) {
            this.borders = borders;
            this.type = type;
            this.border = border;
        }

        @Override
        public int compareTo(Event o) {
            return Integer.compare(this.border, o.border);
        }
    }

    boolean isInBorders(int target) {
        return target >= left.get() && target <= right.get();
    }
}
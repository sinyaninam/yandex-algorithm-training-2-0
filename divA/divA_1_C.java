/*
 * C. Проверьте правильность ситуации
 * 
 * // Условие
 * Ограничение времени 	1 секунда
 * Ограничение памяти 	64Mb
 * Ввод 	стандартный ввод или input.txt
 * Вывод 	стандартный вывод или output.txt
 * 
 * Напишите программу, которая по изображению поля для игры в «Крестики-нолики» определит,
 * могла ли такая ситуация возникнуть в результате игры с соблюдением всех правил.
 * 
 * Напомним, что игра в «Крестики-нолики» ведется на поле 3*3. Два игрока ходят по очереди.
 * Первый ставит крестик, а второй – нолик. Ставить крестик и нолик разрешается в любую еще
 * не занятую клетку поля. Когда один из игроков поставит три своих знака в одной 
 * горизонтали, вертикали или диагонали, или когда все клетки поля окажутся заняты, игра
 * заканчивается.
 * 
 * // Формат ввода
 * Вводится три строки по три числа в каждой, описывающих игровое поле. Число 0 обозначает
 * пустую клетку, 1 – крестик, 2 – нолик. Числа в строке разделяются пробелами.
 * 
 * // Формат вывода
 * Требуется вывести слово YES, если указанная ситуация могла возникнуть в ходе игры, и 
 * NO в противном случае.
 * 
 * // Примеры
 * 1 1 1
 * 1 1 1
 * 1 1 1
 * 
 * NO
 * 
 * 2 1 1
 * 1 1 2
 * 2 2 1
 * 
 * YES
 * 
 * 1 1 1
 * 2 0 2
 * 0 0 0
 * 
 * YES
 * 
 * 0 0 0
 * 0 1 0
 * 0 0 0
 * 
 * YES
 * 
 * // Решение
 * В первую очередь определяем наличие и количество выигрышных комбинаций, учитывая, что
 * последним ходом игрок может получить две выигрышные комбинации, и определяем победителя.
 * Далее смотрим на количество ходов каждого игрока в зависимости от победителя. 
 * Пусть x - количество ходов первого игрока, o - второго. Если выиграл первый игрок, то 
 * x = o + 1, если второй, то x = o; Если никто не выиграл, то o + 1 >= x >= o;
 * Если эти условия не выполняются, то ситуация невозможна.
 * 
 */

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class Test {

    public static void main(String args[]) {
        Scanner scan = new Scanner(System.in);

        String[][] gameField = new String[3][3];
        for (int i = 0; i < gameField.length; i++) {
            String[] input = scan.nextLine().split(" +");
            for (int j = 0; j < gameField[i].length; j++)
                gameField[i][j] = input[j];
        }
        GameStats gameStats = new GameStats(gameField);

        if ((gameStats.areGameStatsPossible()))
            System.out.println("YES");
        else
            System.out.println("NO");
    }
}

class GameStats {

    private final static String EMPTY = "0", X = "1", O = "2";

    enum Winner {
        X, O, NONE
    }

    String[][] gameField;
    int xNum, oNum;

    GameStats(String[][] gameField) {
        this.gameField = gameField;
        defineNumOfTurns();
    }

    private void defineNumOfTurns() {
        for (String[] cells : gameField) {
            for (String cell : cells) {
                if (cell.equals(X))
                    xNum++;
                if (cell.equals(O))
                    oNum++;
            }
        }
    }

    private Winner getVerticalWinner() {
        Winner winner = Winner.NONE;
        String firstCell;
        v_outer: for (int col = 0; col < gameField[0].length; col++) {
            firstCell = gameField[0][col];
            if (firstCell.equals(EMPTY))
                continue;
            for (String[] cells : gameField) {
                if (!cells[col].equals(firstCell))
                    continue v_outer;
            }
            if (winner != Winner.NONE)
                throw new RuntimeException("there can be only one vertical win combo");
            winner = (firstCell.equals(X)) ? Winner.X : Winner.O;
        }
        return winner;
    }

    private Winner getHorizontalWinner() {
        Winner winner = Winner.NONE;
        String firstCell;
        h_outer: for (String[] cells : gameField) {
            firstCell = cells[0];
            if (firstCell.equals(EMPTY))
                continue;
            for (String cell : cells) {
                if (!cell.equals(firstCell))
                    continue h_outer;
            }
            if (winner != Winner.NONE)
                throw new RuntimeException("there can be only one horizontal win combo");
            winner = (firstCell.equals(X)) ? Winner.X : Winner.O;
        }
        return winner;
    }

    private Winner getMainDiagonalWinner() {
        Winner winner = Winner.NONE;
        String firstCell = gameField[0][0];
        m_outer: if (!firstCell.equals(EMPTY)) {
            for (int i = 0; i < gameField.length; i++) {
                if (!gameField[i][i].equals(firstCell))
                    break m_outer;
            }
            winner = (firstCell.equals(X)) ? Winner.X : Winner.O;
        }
        return winner;
    }

    private Winner getAntiDiagonalWinner() {
        Winner winner = Winner.NONE;
        String firstCell = gameField[0][gameField.length - 1];
        a_outer: if (!firstCell.equals(EMPTY)) {
            for (int i = 0; i < gameField.length; i++) {
                if (!gameField[i][gameField.length - 1 - i].equals(firstCell))
                    break a_outer;
            }
            winner = (firstCell.equals(X)) ? Winner.X : Winner.O;
        }
        return winner;
    }

    private Winner getWinner() {
        List<Winner> winners = Arrays.asList(getVerticalWinner(),getHorizontalWinner(),getMainDiagonalWinner(),getAntiDiagonalWinner());
        List<Winner> lineWinners = winners.stream()
                .filter(lineWinner -> lineWinner != Winner.NONE)
                .distinct()
                .collect(Collectors.toList());

        if (lineWinners.size() > 1)
            throw new RuntimeException("there can only be one winner");
        return (lineWinners.size() == 1)
                ? lineWinners.get(0)
                : Winner.NONE;
    }


    public boolean areGameStatsPossible() {
        Winner winner;
        try {
            winner = getWinner();
        } catch (RuntimeException e) {
            return false;
        }

        switch (winner) {
            case O:
                return xNum == oNum;
            case X:
                return xNum == oNum + 1;
            default:
                return (xNum - oNum >= 0) && (xNum - oNum <= 1);
        }
    }
}
package TicTacToe_on_java;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Класс TicTacToe представляет собой реализацию игры в крестики-нолики.
 */
public class TicTacToe
{
    // Сканнер для ввода данных
    private final Scanner scanner = new Scanner(System.in);

    // Игроки
    private final Player playerX = new Player("X");
    private final Player player0 = new Player("O");

    // Игровое поле
    private final String[][] field = {
            {" ", " ", " "},
            {" ", " ", " "},
            {" ", " ", " "}
    };

    // Карта ходов
    private final Map<Integer, int[]> moves = Map.of(
            7, new int[]{0, 0},
            8, new int[]{0, 1},
            9, new int[]{0, 2},
            4, new int[]{1, 0},
            5, new int[]{1, 1},
            6, new int[]{1, 2},
            1, new int[]{2, 0},
            2, new int[]{2, 1},
            3, new int[]{2, 2}
    );

    public TicTacToe()
    {
        while (true)
        {
            step(playerX, player0);
        }
    }

    public static void main(String[] args)
    {
        new TicTacToe();
    }

    private void step(Player playerX, Player playerO) {
        choose(playerX);
        checkWin(playerX);
        checkWin(playerO);

        choose(playerO);
        checkWin(playerX);
        checkWin(playerO);
    }

    private void choose(Player player) throws InputMismatchException
    {
        boolean validInput = false;

        while (!validInput) {
            printField();
            System.out.print("Выбирает " + player.name + ": ");
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Очищаем буфер после ввода числа

                int[] coordinates = moves.get(choice);

                if (coordinates == null) {
                    throw new InputMismatchException();
                }

                int line = coordinates[0];
                int col = coordinates[1];

                if (field[line][col].equals(" "))
                {
                    field[line][col] = player.name;
                    validInput = true;
                }
                else
                {
                    System.out.println("Ячейка уже занята! Пробуйте снова.");
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException ex) {}
                }
            }
            catch (InputMismatchException e) {
                System.out.println("Вы ввели не число или не число от 1 до 9! Попробуйте снова.");
                try {
                    TimeUnit.SECONDS.sleep(2);
                }
                catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                scanner.nextLine(); // Очищаем буфер после некорректного ввода
            }
        }
    }

    /**
     * Метод для проверки победы игрока.
     *
     * @param player Игрок, чью победу проверяем
     */
    private void checkWin(Player player) {
        for (int i : new int[]{0, 1, 2})
        {
            // Проверка строк
            if (field[i][0].equals(player.name) && field[i][1].equals(player.name) && field[i][2].equals(player.name)) {
                win(player);
            }
            // Проверка столбцов
            if (field[0][i].equals(player.name) && field[1][i].equals(player.name) && field[2][i].equals(player.name)) {
                win(player);
            }
        }
        // Проверка диагоналей
        if (field[0][0].equals(player.name) && field[1][1].equals(player.name) && field[2][2].equals(player.name)) {
            win(player);
        }
        if (field[0][2].equals(player.name) && field[1][1].equals(player.name) && field[2][0].equals(player.name)) {
            win(player);
        }

        for (String[] line : field)
        {
            for (String i : line)
            {
                if (i.equals(" "))
                    return;
                else if (i.equals(playerX.name) || i.equals(player0.name))
                    continue;
            }
        }
        draw();
    }
    private void printField()
    {
        // Очистка консоли:
        System.out.print("\033[H\033[2J");
        System.out.flush();

        // Вывод поля:
        System.out.println();
        System.out.println(field[0][0] + "|" + field[0][1] + "|" + field[0][2] + "\n-+-+-");
        System.out.println(field[1][0] + "|" + field[1][1] + "|" + field[1][2] + "\n-+-+-");
        System.out.println(field[2][0] + "|" + field[2][1] + "|" + field[2][2]);
    }

    // Метод для объявления победителя.
    private void win(Player player) throws InputMismatchException
    {
        printField();
        player.points++;

        System.out.println(String.format("\n%s победил!\n\nХотите сыграть снова?\n1 - да\n0 - нет", player.name));

        playAgain();
    }

    // Метод для объявления ничьи.
    private void draw() throws InputMismatchException
    {
        printField();

        System.out.println("\nНИЧЬЯ\n\nХотите сыграть снова?\n1 - да\n0 - нет");

        playAgain();
    }


    //Метод для повторного запуска игры или выхода из программы.
    private void playAgain()
    {
        while (true)
        {
            try {
                int choice = scanner.nextInt();

                if (choice == 1) {
                    resetField();

                    System.out.println(String.format("Очки игроков:\nИгрок %s - %d\nИгрок %s - %d",
                            playerX.name, playerX.points, player0.name, player0.points));

                    main(new String[]{});
                }
                else if (choice == 0) {
                    System.out.println(String.format("Очки игроков:\nИгрок %s - %d\nИгрок %s - %d",
                            playerX.name, playerX.points, player0.name, player0.points));
                    scanner.close();
                    System.exit(0);
                }
                else {
                    System.out.print("Не тот ввод!\nВведите снова: ");
                    playAgain();
                }
                break;
            }
            catch (InputMismatchException e) {}
        }
    }
    //Метод для сброса игрового поля.
    private void resetField()
    {
        for (String[] line : field) {
            line = new String[] {" ", " ", " "};
        }
    }

    //Класс Игрок
    private class Player
    {
        private final String name;
        private int points = 0;

        public Player(String name) {
            this.name = name;
        }
    }
}
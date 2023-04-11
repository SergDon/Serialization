package ru.netology;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static final Product[] goods = new Product[]{
            new Product("Кокос", 220.0),
            new Product("Рис", 55.0),
            new Product("Сахар", 61.0),
            new Product("Кефир", 82.0),
            new Product("Чай", 150.0),
            new Product("Колбасный сыр", 240.0)
    };

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String s;
        Basket shoppingCart;
        int selectedItem;
        int itemCount;

        var basketFile = new File("basket.txt");
        var jsonFile = new File("basket.json");
        var logFile = new File("log.csv");
        var clientLog = new ClientLog();

        if (jsonFile.exists()) {
            System.out.println("Добро пожаловать!");
            System.out.println("Желаете загрузить корзину покупок?  - нажмите <ENTER> ");
            if (scanner.nextLine().equals("")) {
                shoppingCart = Basket.loadFromJSON(jsonFile);
            } else {
                shoppingCart = new Basket(goods);
            }
        } else {
            shoppingCart = new Basket(goods);
        }
        while (true) {
            shoppingCart.printGoodsList();
            s = scanner.nextLine();
            String[] inputValues = s.split(" ");
            if (inputValues.length == 2) {
                try {
                    selectedItem = Integer.parseInt(inputValues[0]);
                    itemCount = Integer.parseInt(inputValues[1]);

                    if (selectedItem <= 0 || selectedItem > goods.length) {
                        System.out.print("\nВнимание! Неправильный номер товара\n");
                        continue;
                    }
                    if (itemCount <= 0) {
                        continue;
                    }
                    shoppingCart.addToCart(selectedItem - 1, itemCount);
                    shoppingCart.saveToJSON(jsonFile);
                    clientLog.getLog(selectedItem, itemCount);
                } catch (NumberFormatException nfe) {

                    System.out.println("\nПожалуйста! Нужно для работы только 2 аргумента - 2 целых числа через пробел!\"");
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else if (s.equals("end")) {
                break;
            }
            System.out.println("\nВнимание! Нужно для работы только 2 аргумента через пробел");
        }
        clientLog.exportAsCSV(logFile);
        scanner.close();
        shoppingCart.printCart();
    }
}
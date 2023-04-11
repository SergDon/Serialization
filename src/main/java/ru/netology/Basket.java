package ru.netology;

import com.google.gson.*;
import ru.netology.Product;

import java.io.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class Basket {
    private final Product[] goods;
    private double totalValue = 0;

    public Basket(Product[] goods) {
        this.goods = goods.clone();
    }

    public void addToCart(int productNum, int amount) {
        goods[productNum].changeItemInBasket(amount);
        totalValue += goods[productNum].getPrice() * amount;
    }

    public void printGoodsList() {

        System.out.print(" №. Название продукта. Цена за единицу. Количество. Стоимость по позиции\n " + "");

        double currentValue;
        totalValue = 0;
        for (int i = 0; i < goods.length; i++) {
            currentValue = goods[i].getInBasket() * goods[i].getPrice();
            totalValue += currentValue;
            System.out.printf("%2d. %13s %12.2f %10d %17.2f\n", i + 1,
                    goods[i].getName(), goods[i].getPrice(),
                    goods[i].getInBasket(), currentValue);
        }
        System.out.printf("ИТОГО: Всех Ваших товаров в корзине на сумму %10.2f\n\n", totalValue);
        System.out.println("Пожалуйста, добавьте товар в корзину или для завершения работы введите end");
        System.out.print("");
    }

    public void printCart() {
        System.out.print("Окончательный результат: в Вашей корзине находятся товары:\n");
        for (Product item : goods) {
            if (item.getInBasket() > 0) {
                System.out.printf("%13s %3d шт. %6.2f руб/шт. %8.2f на сумму\n",
                        item.getName(), item.getInBasket(), item.getPrice(),
                        item.getInBasket() * item.getPrice());
            }
        }
        System.out.printf("ИТОГО: всех Ваших товаров в корзине на общую сумму (руб)  %10.2f\n\n", totalValue);
    }

    public void saveTxt(File textFile) throws FileNotFoundException {
        var pw = new PrintWriter(textFile);
        Stream.of(goods).forEach(p ->
                pw.printf("%s@%.4f@%d\n", p.getName(), p.getPrice(), p.getInBasket()));
        pw.close();
    }

    public void saveToJSON(File textFile) throws IOException {
        FileWriter writer = new FileWriter(textFile);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        writer.write(gson.toJson(this, Basket.class));
        writer.close();
    }

    public static Basket loadFromTxtFile(File textFile) throws FileNotFoundException, ParseException {
        Scanner scanner = new Scanner(textFile);
        List<Product> goods = new ArrayList<>();
        String name;
        double price;
        int inBasket;
        NumberFormat nf = NumberFormat.getInstance();
        while (scanner.hasNext()) {
            String[] d = scanner.nextLine().split("@");
            name = d[0];
            price = nf.parse(d[1]).doubleValue();
            inBasket = Integer.parseInt(d[2]);
            goods.add(new Product(name, price, inBasket));
        }


        return new Basket(goods.toArray(Product[]::new));
    }

    public static Basket loadFromJSON(File textFile) throws FileNotFoundException {
        var gson = new Gson();
        var reader = new FileReader(textFile);

        return gson.fromJson(reader, Basket.class);
    }
}
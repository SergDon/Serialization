import java.io.*;

public class Basket implements Serializable {
    private final Product[] goods;
    private double nameOfProducts = 0;

    public Basket(Product[] goods) {
        this.goods = goods.clone();
    }

    public void addToCart(int productNum, int amount) {
        goods[productNum].changeItemInBasket(amount);
        nameOfProducts += goods[productNum].getPrice() * amount;
    }

    public void printGoodsList() {

        System.out.print(" №. Название продукта. Цена за единицу. Количество. Стоимость по позиции\n " + "");

        double currentValue;
        nameOfProducts = 0;
        for (int i = 0; i < goods.length; i++) {
            currentValue = goods[i].getInBasket() * goods[i].getPrice();
            nameOfProducts += currentValue;
            System.out.printf("%2d. %13s %12.2f %10d %17.2f\n", i + 1,
                    goods[i].getName(), goods[i].getPrice(),
                    goods[i].getInBasket(), currentValue);
        }
        System.out.printf("ИТОГО: Всех Ваших товаров в корзине на сумму %10.2f\n\n", nameOfProducts);
        System.out.println("Пожалуйста, добавьте товар в корзину или для завершения работы введите end");
        System.out.print("");
    }

    public void printCart() {
        System.out.print("Окончательный результат: в Вашей корзине находятся товары:\n");
        for (Product item : goods) {
            if (item.getInBasket() > 0) {
                System.out.printf("%13s %3d шт. %6.2f руб/шт.   на  сумму %8.2f \n",
                        item.getName(), item.getInBasket(), item.getPrice(),
                        item.getInBasket() * item.getPrice());
            }
        }
        System.out.printf("ИТОГО: всех Ваших товаров в корзине на общую сумму (руб)  %10.2f\n\n", nameOfProducts);
        System.out.println("Удачных покупок!");
    }

    public void saveBin(File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this);
        oos.close();
    }

    public static Basket loadFromTxtFile(File textFile) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(textFile);
        ObjectInputStream ois = new ObjectInputStream(fis);
        return (Basket) ois.readObject();
    }
}
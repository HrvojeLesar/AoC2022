import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

public class Main {
    public static void main(String[] args) {
        new Task();
    }
}

public class Task {
    int maxCalories = 0;
    int currentCount = 0;
    SortedSet<Integer> results = new TreeSet<>();

    Task() {
        try {
            File file = new File("input");
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                this.countCalories(reader.nextLine());
            }
            countCalories("");
            System.out.println("Task 1: " + this.maxCalories);
            top();
            System.out.println("Task 2: " + this.maxCalories);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void countCalories(String calories) {
        if (calories == "") {
            if (this.currentCount > this.maxCalories) {
                this.maxCalories = this.currentCount;
            }
            this.results.add(this.currentCount);
            this.currentCount = 0;
        } else {
            int caloriesNumber = Integer.parseInt(calories);
            this.currentCount += caloriesNumber;
        }
    }

    void top() {
        int len = results.size();
        Object[] arr = this.results.toArray();
        this.maxCalories = (int)arr[len - 1] + (int)arr[len - 2] + (int)arr[len - 3];
    }
}

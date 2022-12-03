package aoc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class App {
    public static void main(String[] args) {
        new Aoc();
    }

    private static class Aoc {
        private List<Character> duplicates = new ArrayList<Character>();
        private List<String> groups = new ArrayList<String>();
        private List<Character> badges = new ArrayList<Character>();

        Aoc() {
            try {
                File file = new File("input");
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    this.findDuplicateItems(line);
                    this.groups.add(line);
                }
                scanner.close();

                System.out.println("Part 1: " + this.calculateItemPriority(this.duplicates));
                this.badges();
                System.out.println("Part 2: " + this.calculateItemPriority(this.badges));
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        private void findDuplicateItems(String items) {
            String firstHalf = items.substring(0, items.length() / 2);
            String secondHalf = items.substring(items.length() / 2);

            List<Character> unique = firstHalf.chars().distinct().mapToObj(c -> (char) c).collect(Collectors.toList());

            for (char c : unique) {
                if (secondHalf.contains(String.valueOf(c))) {
                    this.duplicates.add(c);
                }
            }
        }

        private int calculateItemPriority(List<Character> list) {
            List<Integer> priorities = new ArrayList<Integer>();
            for (char c : list) {
                if (Character.isLowerCase(c)) {
                    priorities.add(c - 96);
                } else {
                    priorities.add(c - 64 + 26);
                }
            }
            return priorities.stream().reduce(0, (a, b) -> a + b);
        }

        private void badges() {
            for (int i = 0; i < this.groups.size(); i += 3) {
                List<Character> unique = this.groups.get(i).chars().distinct().mapToObj(c -> (char) c)
                        .collect(Collectors.toList());

                for (char c : unique) {
                    if (this.groups.get(i + 1).contains(String.valueOf(c)) &&
                            this.groups.get(i + 2).contains(String.valueOf(c))) {
                        this.badges.add(c);
                    }
                }
            }
        }
    }
}

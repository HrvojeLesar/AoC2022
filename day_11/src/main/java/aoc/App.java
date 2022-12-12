package aoc;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class App {
    public static void main(String[] args) {
        new AoC();
    }

    private static class Monkey {
        LinkedList<Long> items;
        Function<Long, Long> operation;
        long test;
        int trueThrowTo;
        int falseThrowTo;
        long timesInspected = 0;

        Monkey(LinkedList<Long> items, Function<Long, Long> operation, long test, int trueThrowTo,
                int falseThrowTo) {
            this.items = items;
            this.operation = operation;
            this.test = test;
            this.trueThrowTo = trueThrowTo;
            this.falseThrowTo = falseThrowTo;
        }
    }

    private static class AoC {
        ArrayList<Monkey> monkeysP1 = new ArrayList<>();
        ArrayList<Monkey> monkeysP2 = new ArrayList<>();
        long roundsP1 = 15;
        long roundsP2 = 10000;
        long part_2_modulo = 0;

        AoC() {
            try {
                Path filename = Path.of("input");
                String input = Files.readString(filename);
                this.parseInput(input);
                this.playP1();
                this.playP2();
                List<Monkey> part1 = this.monkeysP1.stream()
                        .sorted((m1, m2) -> Long.compare(m2.timesInspected, m1.timesInspected))
                        .collect(Collectors.toList());
                System.out.println("Part 1: " + part1.get(0).timesInspected * part1.get(1).timesInspected);
                List<Monkey> part2 = this.monkeysP2.stream()
                        .sorted((m1, m2) -> Long.compare(m2.timesInspected, m1.timesInspected))
                        .collect(Collectors.toList());
                System.out.println("Part 2: " + part2.get(0).timesInspected * part2.get(1).timesInspected);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void parseInput(String input) {
            String[] lines = input.split("\n");
            long numberOfMonekys = lines.length / 6;
            for (int i = 0; i < numberOfMonekys; i++) {
                int index = i * 6 + i;
                if (index > lines.length) {
                    break;
                }
                LinkedList<Long> items = this.getListFromItemsString(lines[index + 1]);
                LinkedList<Long> itemsp2 = this.getListFromItemsString(lines[index + 1]);
                Function<Long, Long> operation = this.parseOperation(lines[index + 2].strip());
                long test = Long.parseLong(lines[index + 3].strip().split(" ")[3]);
                int trueThrowTo = Integer.parseInt(lines[index + 4].strip().split(" ")[5]);
                int falseThrowTo = Integer.parseInt(lines[index + 5].strip().split(" ")[5]);
                this.monkeysP1.add(new Monkey(items, operation, test, trueThrowTo, falseThrowTo));
                this.monkeysP2.add(new Monkey(itemsp2, operation, test, trueThrowTo, falseThrowTo));
            }
        }

        private void playP1() {
            for (int round = 0; round < this.roundsP1; round++) {
                for (int i = 0; i < this.monkeysP1.size(); i++) {
                    Monkey current = this.monkeysP1.get(i);
                    while (!current.items.isEmpty()) {
                        current.timesInspected += 1;
                        long itemWorryLevel = current.items.removeFirst();
                        long newWorryLevel = current.operation.apply(itemWorryLevel) / 3;
                        if (newWorryLevel % current.test == 0) {
                            this.monkeysP1.get(current.trueThrowTo).items.addLast(newWorryLevel);
                        } else {
                            this.monkeysP1.get(current.falseThrowTo).items.addLast(newWorryLevel);
                        }
                    }
                }
            }
        }

        private void playP2() {
            for (int i = 0; i < this.monkeysP2.size(); i++) {
                if (i == 0) {
                    this.part_2_modulo = this.monkeysP2.get(i).test;
                } else {
                    this.part_2_modulo *= this.monkeysP2.get(i).test;
                }
            }
            for (int round = 0; round < this.roundsP2; round++) {
                for (int i = 0; i < this.monkeysP2.size(); i++) {
                    Monkey current = this.monkeysP2.get(i);
                    while (!current.items.isEmpty()) {
                        current.timesInspected += 1;
                        long itemWorryLevel = current.items.removeFirst();
                        long newWorryLevel = current.operation.apply(itemWorryLevel) % this.part_2_modulo;
                        if (newWorryLevel % current.test == 0) {
                            this.monkeysP2.get(current.trueThrowTo).items.addLast(newWorryLevel);
                        } else {
                            this.monkeysP2.get(current.falseThrowTo).items.addLast(newWorryLevel);
                        }
                    }
                }
            }
        }

        private LinkedList<Long> getListFromItemsString(String items) {
            LinkedList<Long> startingItems = new LinkedList<>();
            String[] split = items.trim().split(" ");
            for (int i = 2; i < split.length; i++) {
                int commaIndex = split[i].indexOf(',');
                if (commaIndex != -1) {
                    startingItems.addLast(Long.parseLong(split[i].substring(0, commaIndex)));
                } else {
                    startingItems.addLast(Long.parseLong(split[i]));
                }
            }
            return startingItems;
        }

        private Function<Long, Long> parseOperation(String operation) {
            String[] split = operation.split(" ");

            String val1 = split[3];
            String val2 = split[5];
            String sign = split[4];

            if (val1.equals("old") && val2.equals("old")) {
                if (sign.equals("+")) {
                    return (old) -> {
                        return old + old;
                    };
                } else {
                    return (old) -> {
                        return old * old;
                    };
                }
            } else {
                long numberValue;
                if (!val1.equals("old")) {
                    numberValue = Long.parseLong(val1);
                } else {
                    numberValue = Long.parseLong(val2);
                }
                if (sign.equals("+")) {
                    return (old) -> {
                        return numberValue + old;
                    };
                } else {
                    return (old) -> {
                        return numberValue * old;
                    };
                }
            }
        }
    }
}

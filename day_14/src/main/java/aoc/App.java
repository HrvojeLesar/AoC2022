package aoc;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        new AoC();
    }

    enum Environment {
        Air,
        Rock,
        Sand
    }

    private static class Position {
        public int depth;
        public int width;

        Position(int depth, int width) {
            this.depth = depth;
            this.width = width;
        }
    }

    private static class Pair<T> {
        public T left;
        public T right;

        Pair(T left, T right) {
            this.left = left;
            this.right = right;
        }
    }

    private static class AoC {
        private HashMap<Integer, HashMap<Integer, Environment>> newMap = new HashMap<>();
        private Position current;
        private Position last;
        private int abyssAfter = 0;
        private int numberSettled = 0;
        private int floorDepth = 0;

        AoC() {
            try {
                this.fillMap();
                File file = new File("input");
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    this.current = null;
                    this.last = null;
                    String line = scanner.nextLine();
                    this.parseLine(line);
                }
                scanner.close();
                this.floorDepth = this.abyssAfter + 2;
                this.simulate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void parseLine(String line) {
            String[] rockPositions = line.split(" -> ");
            for (String positionPair : rockPositions) {
                String[] positions = positionPair.split(",");
                if (this.current != null) {
                    this.last = this.current;
                }
                this.current = new Position(Integer.parseInt(positions[1]), Integer.parseInt(positions[0]));
                if (this.abyssAfter < this.current.depth) {
                    this.abyssAfter = this.current.depth;
                }
                this.drawRocks();
            }
        }

        private void drawRocks() {
            if (this.last == null) {
                this.newMap.get(this.current.depth).put(this.current.width, Environment.Rock);
            } else {
                if (this.current.depth != this.last.depth) {
                    Pair<Integer> sortedNums = this.getSortedPosition(this.current.depth, this.last.depth);
                    for (int x = sortedNums.left; x <= sortedNums.right; x++) {
                        this.newMap.get(x).put(this.last.width, Environment.Rock);
                    }

                } else if (this.current.width != this.last.width) {
                    Pair<Integer> sortedNums = this.getSortedPosition(this.current.width, this.last.width);
                    for (int y = sortedNums.left; y <= sortedNums.right; y++) {
                        this.newMap.get(this.last.depth).put(y, Environment.Rock);
                    }
                } else {
                    System.out.println("UNREACHABLE!!");
                }
            }
        }

        private Pair<Integer> getSortedPosition(int num1, int num2) {
            if (num1 < num2) {
                return new Pair<Integer>(num1, num2);
            } else {
                return new Pair<Integer>(num2, num1);
            }
        }

        private void fillMap() {
            for (int y = 0; y < 1000; y++) {
                this.newMap.put(y, new HashMap<Integer, Environment>());
            }
        }

        private void simulate() {
            boolean part1Outputed = false;
            while (true) {
                Position sandPosition = new Position(0, 500);
                // move
                while (true) {
                    int depth = sandPosition.depth + 1;
                    int width = sandPosition.width;
                    if (this.newMap.get(0).get(500) == Environment.Sand) {
                        System.out.println("Part 2: " + this.numberSettled);
                        return;
                    }
                    if (sandPosition.depth == this.abyssAfter && part1Outputed == false) {
                        System.out.println("Part 1: " + this.numberSettled);
                        part1Outputed = true;
                    }
                    if (depth < this.floorDepth) {
                        if (this.newMap.get(depth).get(width) == null) {
                            sandPosition.depth += 1;
                        } else {
                            if (this.newMap.get(depth).get(width - 1) == null) {
                                sandPosition.depth += 1;
                                sandPosition.width -= 1;
                            } else if (this.newMap.get(depth).get(width + 1) == null) {
                                sandPosition.depth += 1;
                                sandPosition.width += 1;
                            } else {
                                this.newMap.get(sandPosition.depth).put(sandPosition.width, Environment.Sand);
                                this.numberSettled += 1;
                                break;
                            }
                        }
                    } else {
                        this.newMap.get(sandPosition.depth).put(sandPosition.width, Environment.Sand);
                        this.numberSettled += 1;
                        break;
                    }
                }
            }
        }
    }
}

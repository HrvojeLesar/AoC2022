package aoc;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        new Aoc();
    }

    enum Mode {
        LoadContainers,
        LoadInstructions
    }

    private static class Aoc {
        Mode currentMode = Mode.LoadContainers;
        ArrayList<LinkedList<Character>> containersP1;
        ArrayList<LinkedList<Character>> containersP2;

        Aoc() {
            try {
                File file = new File("input");
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.isEmpty()) {
                        this.currentMode = Mode.LoadInstructions;
                        continue;
                    }
                    switch (this.currentMode) {
                        case LoadContainers: {
                            parseContainers(line);
                            break;
                        }
                        case LoadInstructions: {
                            parseInstructionsP1(line);
                            parseInstructionsP2(line);
                            break;
                        }
                    }
                }
                this.print(this.containersP1, "1");
                this.print(this.containersP2, "2");
                scanner.close();

            } catch (Exception e) {
                System.out.println(e);
            }
        }

        private void parseContainers(String line) {
            this.createContainers(line);
            for (int i = 0; i < line.length() / 4 + 1; i++) {
                int stringIndex = i * 4 + 1;
                char containerName = line.charAt(stringIndex);
                if (containerName >= '0' && containerName <= '9') {
                    return;
                }
                if (containerName != ' ') {
                    LinkedList<Character> containerStackP1 = this.containersP1.get(i);
                    LinkedList<Character> containerStackP2 = this.containersP2.get(i);
                    containerStackP1.push(containerName);
                    containerStackP2.push(containerName);
                }
            }
        }

        private void createContainers(String line) {
            if (this.containersP1 == null) {
                this.containersP1 = new ArrayList<LinkedList<Character>>();
                this.containersP2 = new ArrayList<LinkedList<Character>>();
                for (int i = 0; i < line.length() / 4 + 1; i++) {
                    this.containersP1.add(new LinkedList<>());
                    this.containersP2.add(new LinkedList<>());
                }
            }
        }

        private void parseInstructionsP1(String instruction) {
            // move 1
            // from 3
            // to 5
            String[] splitInstruction = instruction.split(" ");
            int move = Integer.parseInt(splitInstruction[1]);
            int from = Integer.parseInt(splitInstruction[3]) - 1;
            int to = Integer.parseInt(splitInstruction[5]) - 1;

            LinkedList<Character> fromContainer = this.containersP1.get(from);
            LinkedList<Character> toContainer = this.containersP1.get(to);
            for (int i = 0; i < move; i++) {
                toContainer.addLast(fromContainer.removeLast());
            }
        }

        private void parseInstructionsP2(String instruction) {
            String[] splitInstruction = instruction.split(" ");
            int move = Integer.parseInt(splitInstruction[1]);
            int from = Integer.parseInt(splitInstruction[3]) - 1;
            int to = Integer.parseInt(splitInstruction[5]) - 1;

            LinkedList<Character> fromContainer = this.containersP2.get(from);
            LinkedList<Character> toContainer = this.containersP2.get(to);
            LinkedList<Character> elements = new LinkedList<>();
            for (int i = 0; i < move; i++) {
                elements.addFirst(fromContainer.removeLast());
            }
            int elSize = elements.size();
            for (int i = 0; i < elSize; i++) {
                toContainer.addLast(elements.removeFirst());
            }
        }

        private void print(ArrayList<LinkedList<Character>> container, String part) {
            String solution = "";
            for (int i = 0; i < container.size(); i++) {
                solution += container.get(i).peekLast();
            }

            System.out.println("Part " + part + ": " + solution);
        }
    }
}

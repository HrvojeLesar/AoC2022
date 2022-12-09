package aoc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        new AoC();
    }

    private static class Move {
        enum Direction {
            Up,
            Down,
            Left,
            Right,
            UNREACHABLE
        }

        Direction direction;
        int numberOfSteps;

        Move(String instruction) {
            String[] split = instruction.strip().split(" ");
            this.direction = this.stringToDirection(split[0]);
            this.numberOfSteps = Integer.parseInt(split[1]);
        }

        private Direction stringToDirection(String directionString) {
            switch (directionString) {
                case "U":
                    return Direction.Up;
                case "D":
                    return Direction.Down;
                case "L":
                    return Direction.Left;
                case "R":
                    return Direction.Right;
            }
            return Direction.UNREACHABLE;
        }

        public int[] getMove() {
            switch (this.direction) {
                case Up: {
                    return new int[] { 0, 1 };
                }
                case Down: {
                    return new int[] { 0, -1 };
                }
                case Left: {
                    return new int[] { -1, 0 };
                }
                case Right: {
                    return new int[] { 1, 0 };
                }
                default:
                    return new int[] { 0, 0 };
            }
        }

        public int getNumberOfSteps() {
            return this.numberOfSteps;
        }
    }

    private static class Pair {
        int pair[] = new int[2];

        Pair() {
        }

        Pair(int first, int second) {
            this.pair[0] = first;
            this.pair[1] = second;
        }

        Pair(Pair oldPair, int[] move) {
            this.pair[0] = oldPair.pair[0] + move[0];
            this.pair[1] = oldPair.pair[1] + move[1];
        }

        public int getFirst() {
            return this.pair[0];
        }

        public int getSecond() {
            return this.pair[1];
        }

        public boolean eq(Pair other) {
            return this.getFirst() == other.getFirst() && this.getSecond() == other.getSecond();
        }

        public boolean isAdjacentTo(Pair other) {
            int firstPositionDistance = Math.abs(this.getFirst() - other.getFirst());
            int secondPositionDistance = Math.abs(this.getSecond() - other.getSecond());
            if (firstPositionDistance > 1 || secondPositionDistance > 1) {
                return false;
            }
            return true;
        }
    }

    private static class AoC {
        Pair headPosition = new Pair(0, 0);
        Pair tailPosition = new Pair(0, 0);
        Pair lastHeadPosition = new Pair();
        List<Pair> tailVisited = new ArrayList<Pair>() {
            {
                add(new Pair(0, 0));
            }
        };

        // part 2
        List<Pair> rope = new ArrayList<Pair>() {
            {
                add(new Pair(0, 0)); // head
                add(new Pair(0, 0)); // knot 1 ...
                add(new Pair(0, 0));
                add(new Pair(0, 0));
                add(new Pair(0, 0));
                add(new Pair(0, 0));
                add(new Pair(0, 0));
                add(new Pair(0, 0));
                add(new Pair(0, 0));
                add(new Pair(0, 0));
            }
        };

        List<Pair> tailVisitedP2 = new ArrayList<Pair>() {
            {
                add(new Pair(0, 0));
            }
        };

        AoC() {
            try {
                File file = new File("input");
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    doMove(line);
                }
                System.out.println("Part 1: " + this.tailVisited.size());
                System.out.println("Part 2: " + this.tailVisitedP2.size());
                scanner.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        private void doMove(String instruction) {
            Move move = new Move(instruction);
            for (int i = 0; i < move.getNumberOfSteps(); i++) {
                this.lastHeadPosition = this.headPosition;
                this.headPosition = new Pair(this.headPosition, move.getMove());
                this.moveTail();

                // part 2
                this.rope.set(0, new Pair(this.rope.get(0), move.getMove()));

                for (int j = 1; j < rope.size(); j++) {
                    Pair current = this.rope.get(j);
                    Pair following = this.rope.get(j - 1);
                    int firstPositionDistance = Math.abs(current.getFirst() - following.getFirst());
                    int secondPositionDistance = Math.abs(current.getSecond() - following.getSecond());
                    if ((current.getFirst() == following.getFirst() || current.getSecond() == following.getSecond())
                            && (firstPositionDistance > 1 || secondPositionDistance > 1)) {
                        int[] up = new int[] { 0, 1 };
                        int[] down = new int[] { 0, -1 };
                        int[] left = new int[] { -1, 0 };
                        int[] right = new int[] { 1, 0 };
                        // move up or down
                        if (current.getFirst() == following.getFirst()) {
                            if (current.getSecond() + 2 == following.getSecond()) {
                                this.rope.set(j, new Pair(current, up));
                            } else {
                                this.rope.set(j, new Pair(current, down));
                            }
                        } else {
                            if (current.getFirst() + 2 == following.getFirst()) {
                                this.rope.set(j, new Pair(current, right));
                            } else {
                                this.rope.set(j, new Pair(current, left));
                            }
                        }
                    } else if (current.getFirst() != following.getFirst()
                            && current.getSecond() != following.getSecond()
                            && !current.isAdjacentTo(following)) {
                        Pair tRight = new Pair(current, new int[] { 1, 1 });
                        Pair tLeft = new Pair(current, new int[] { -1, 1 });
                        Pair bRight = new Pair(current, new int[] { 1, -1 });
                        Pair bLeft = new Pair(current, new int[] { -1, -1 });
                        if (tRight.isAdjacentTo(following)) { // check top right
                            this.rope.set(j, tRight);
                        } else if (tLeft.isAdjacentTo(following)) { // check top left
                            this.rope.set(j, tLeft);
                        } else if (bRight.isAdjacentTo(following)) { // check bottom right
                            this.rope.set(j, bRight);
                        }
                        if (bLeft.isAdjacentTo(following)) { // check bottom left
                            this.rope.set(j, bLeft);
                        }
                    }
                    if (j == rope.size() - 1) {
                        boolean add = true;
                        for (int x = 0; x < tailVisitedP2.size(); x++) {
                            if (tailVisitedP2.get(x).eq(this.rope.get(j))) {
                                add = false;
                                break;
                            }
                        }
                        if (add) {
                            tailVisitedP2.add(this.rope.get(j));
                        }
                    }
                }
            }
        }

        private boolean tailNeedsMoving() {
            int firstPositionDistance = Math.abs(this.headPosition.getFirst() - this.tailPosition.getFirst());
            int secondPositionDistance = Math.abs(this.headPosition.getSecond() - this.tailPosition.getSecond());
            if (firstPositionDistance > 1 || secondPositionDistance > 1) {
                return true;
            } else if (firstPositionDistance == 1 || secondPositionDistance == 1) {
                return false;
            } else {
                return false;
            }
        }

        private void moveTail() {
            if (this.tailNeedsMoving()) {
                boolean add = true;
                for (int i = 0; i < tailVisited.size(); i++) {
                    if (tailVisited.get(i).eq(this.lastHeadPosition)) {
                        add = false;
                        break;
                    }
                }
                if (add) {
                    tailVisited.add(this.lastHeadPosition);
                }
                this.tailPosition = this.lastHeadPosition;
            }
        }
    }
}

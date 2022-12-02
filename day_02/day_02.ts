import { readFileSync, readSync } from "fs"

enum ShapesElf {
    Rock = "A",
    Paper = "B",
    Scissors = "C",
}

enum ShapesMe {
    Rock = "X",
    Paper = "Y",
    Scissors = "Z",
}


enum ShapeScores {
    Rock = 1,
    Paper = 2,
    Scissors = 3,
}

enum RoundOutcomeScore {
    Loss = 0,
    Draw = 3,
    Win = 6,
}

enum Outcome {
    Loss = "X",
    Draw = "Y",
    Win = "Z",
}

const convert = (shape: ShapesMe) => {
    switch (shape) {
        case ShapesMe.Rock:
            return ShapesElf.Rock;
        case ShapesMe.Paper:
            return ShapesElf.Paper;
        case ShapesMe.Scissors:
            return ShapesElf.Scissors;
    }
}

const beats = (shape: ShapesElf) => {
    switch (shape) {
        case ShapesElf.Rock:
            return ShapesElf.Paper;
        case ShapesElf.Paper:
            return ShapesElf.Scissors;
        case ShapesElf.Scissors:
            return ShapesElf.Rock;
    }
}

const loses = (shape: ShapesElf) => {
    switch (shape) {
        case ShapesElf.Rock:
            return ShapesElf.Scissors;
        case ShapesElf.Paper:
            return ShapesElf.Rock;
        case ShapesElf.Scissors:
            return ShapesElf.Paper;
    }
}

const getShapesScore = (shape: ShapesMe) => {
    switch (shape) {
        case ShapesMe.Rock:
            return ShapeScores.Rock;
        case ShapesMe.Paper:
            return ShapeScores.Paper;
        case ShapesMe.Scissors:
            return ShapeScores.Scissors;
    }
}

const getShapesScoreElf = (shape: ShapesElf) => {
    switch (shape) {
        case ShapesElf.Rock:
            return ShapeScores.Rock;
        case ShapesElf.Paper:
            return ShapeScores.Paper;
        case ShapesElf.Scissors:
            return ShapeScores.Scissors;
    }
}

const playRound = (elf: ShapesElf, me: ShapesMe) => {
    const shape = convert(me);
    if (elf === shape) {
        return RoundOutcomeScore.Draw;
    }
    if (
        (elf === ShapesElf.Rock && shape === ShapesElf.Scissors) ||
        (elf === ShapesElf.Paper && shape === ShapesElf.Rock) ||
        (elf === ShapesElf.Scissors && shape === ShapesElf.Paper)
    ) {
        return RoundOutcomeScore.Loss;
    } else {
        return RoundOutcomeScore.Win;
    }
}

const playRound2 = (elf: ShapesElf, outcome: Outcome) => {
    switch (outcome) {
        case Outcome.Win: {
            return [getShapesScoreElf(beats(elf)), RoundOutcomeScore.Win];
        }
        case Outcome.Draw: {
            return [getShapesScoreElf(elf), RoundOutcomeScore.Draw];
        }
        case Outcome.Loss: {
            return [getShapesScoreElf(loses(elf)), RoundOutcomeScore.Loss];
        }
    }
}

const main = () => {
    const fileContent = readFileSync("./input").toString();
    const rounds = fileContent.split("\n").map((round) => { return round.split(" "); });
    rounds.pop();

    const roundScoresP1 = rounds.map((round) => {
        const roundScore = playRound(round[0] as ShapesElf, round[1] as ShapesMe);
        const shapeScore = getShapesScore(round[1] as ShapesMe);
        return roundScore + shapeScore;
    });

    const roundScoresP2 = rounds.map((round) => {
        const [shapeScore, roundScore] = playRound2(round[0] as ShapesElf, round[1] as Outcome);
        return roundScore + shapeScore;
    });

    const part1 = roundScoresP1.reduce((sum, val) => (sum + val), 0);
    const part2 = roundScoresP2.reduce((sum, val) => (sum + val), 0);
    console.log("Part 1: ", part1);
    console.log("Part 2: ", part2);
}

main()

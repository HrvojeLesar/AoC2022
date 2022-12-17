use std::collections::HashSet;

#[derive(Clone, Debug, PartialEq, Eq, Hash)]
struct Position {
    x: i128,
    y: i128,
}

fn main() {
    let pattern = std::fs::read_to_string("input").unwrap();
    let horizontal_pipe = vec![
        Position { x: 0, y: 0 },
        Position { x: 1, y: 0 },
        Position { x: 2, y: 0 },
        Position { x: 3, y: 0 },
    ];
    let plus = vec![
        Position { x: 0, y: 1 },
        Position { x: 1, y: 0 },
        Position { x: 1, y: 1 },
        Position { x: 1, y: 2 },
        Position { x: 2, y: 1 },
    ];
    let l = vec![
        Position { x: 0, y: 0 },
        Position { x: 1, y: 0 },
        Position { x: 2, y: 0 },
        Position { x: 2, y: 1 },
        Position { x: 2, y: 2 },
    ];
    let vertical_pipe = vec![
        Position { x: 0, y: 0 },
        Position { x: 0, y: 1 },
        Position { x: 0, y: 2 },
        Position { x: 0, y: 3 },
    ];
    let square = vec![
        Position { x: 0, y: 0 },
        Position { x: 0, y: 1 },
        Position { x: 1, y: 0 },
        Position { x: 1, y: 1 },
    ];
    let pieces = vec![horizontal_pipe, plus, l, vertical_pipe, square];
    let mut pieces = pieces.iter().cycle();
    let mut instructions = pattern.chars().cycle();
    let mut area = HashSet::with_capacity(100);
    let mut starting_y = 3;

    (0..2022).for_each(|_| {
        let mut piece = pieces.next().unwrap().clone().to_owned();
        let mut jammed = false;

        // set starting position
        piece.iter_mut().for_each(|pos| {
            pos.x += 2;
            pos.y += starting_y;
        });

        while !jammed {
            let instruction = instructions.next().unwrap();
            let shift = match instruction {
                '<' => -1,
                '>' => 1,
                _ => unreachable!(),
            };

            piece.iter_mut().for_each(|pos| pos.x += shift);

            for pos in piece.iter() {
                if area.contains(pos) || pos.x < 0 || pos.x > 6 {
                    // collision fix position
                    for square in piece.iter_mut() {
                        square.x -= shift;
                    }
                    break;
                }
            }

            // move down
            piece.iter_mut().for_each(|pos| pos.y -= 1);

            for pos in piece.iter() {
                if area.contains(pos) || pos.y < 0 {
                    for pos in piece.iter_mut() {
                        pos.y += 1;
                    }
                    jammed = true;
                    break;
                }
            }
        }

        if (0..7).all(|x| {
            area.contains(&Position {
                x,
                y: starting_y - 4,
            })
        }) {
            println!("aaa");
            return;
        }

        piece.iter().for_each(|pos| {
            area.insert(pos.clone());
            if starting_y < pos.y + 4 {
                starting_y = pos.y + 4;
            }
        });
    });
    println!("Part 1: {}", starting_y - 3);
}

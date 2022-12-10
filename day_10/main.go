package main

import (
	"io/ioutil"
	"log"
	"strconv"
	"strings"
)

type InstructionType int

const (
	noop InstructionType = 0
	addx                 = 1
)

type Instruction struct {
	execution_time   int
	value            int
	instruction_type InstructionType
}

func parse_command_from_string(instruction string) Instruction {
	split := strings.Split(instruction, " ")
	if split[0] == "noop" {
		return Instruction{
			execution_time:   1,
			value:            0,
			instruction_type: noop,
		}
	} else {
		num, err := strconv.Atoi(split[1])
		if err != nil {
			log.Fatal(err)
		}
		return Instruction{
			execution_time:   2,
			value:            num,
			instruction_type: addx,
		}
	}
}

type ProgramInstructionFlow struct {
	instruction Instruction
	execute_at  int
}

type Program struct {
	instructions        []Instruction
	current_instruction *Instruction
	register_x          int
	cycle               int
	signal_strenght_sum int
	snapshot_cycle      int
	crt                 []string
	sprite_row          int
}

func new_program(instructions []Instruction) Program {
	return Program{
		instructions:        instructions,
		current_instruction: nil,
		register_x:          1,
		cycle:               0,
		signal_strenght_sum: 0,
		snapshot_cycle:      20,
		crt:                 make([]string, 240),
		sprite_row:          0,
	}
}

func (p *Program) run() {
	for _, instruction := range p.instructions {
		p.current_instruction = &instruction
		if p.cycle+instruction.execution_time >= p.snapshot_cycle {
			p.signal_strenght_sum += p.register_x * p.snapshot_cycle
			p.snapshot_cycle += 40
            p.sprite_row += 1
		}
		p.draw()
		switch instruction.instruction_type {
		case noop:
			break
		case addx:
			p.register_x += instruction.value
			break
		}
		p.cycle += instruction.execution_time
	}

	println("Part 1: ", p.signal_strenght_sum)
	println("Part 2: ")
	p.print_crt()
}

func (p *Program) draw() {
	if p.current_instruction != nil {
		for i := 0; i < p.current_instruction.execution_time; i++ {
			cycle := p.cycle + i
            adjusted_cycle := cycle % 40
			if adjusted_cycle == p.register_x-1 || adjusted_cycle == p.register_x || adjusted_cycle == p.register_x+1 {
				p.crt[cycle] = "#"
			} else {
				p.crt[cycle] = " "
			}
		}
	}
}

func (p *Program) print_crt() {
	for row := 0; row < 6; row++ {
		for column := 0; column < 40; column++ {
			print(p.crt[(row*40)+column])
		}
		print("\n")
	}
}

func main() {
	f, err := ioutil.ReadFile("input")
	if err != nil {
		log.Fatal(err)
	}

	instructions_blob := string(f)
	instructions := strings.Split(instructions_blob, "\n")
	instructions = instructions[:len(instructions)-1]

	parsed_instructions := make([]Instruction, 0)
	for _, element := range instructions {
		parsed_instructions = append(parsed_instructions, parse_command_from_string(element))
	}

	program := new_program(parsed_instructions)
	program.run()
}

package main

import (
	"io/ioutil"
	"log"
	"strings"
)

type Queue struct {
	nodes []*Node
}

func (q *Queue) len() int {
	return len(q.nodes)
}

func (q *Queue) enqueue(node *Node) {
	q.nodes = append(q.nodes, node)
}

func (q *Queue) dequeue() *Node {
	if len(q.nodes) == 0 {
		return nil
	}
	node := q.nodes[0]
	q.nodes = q.nodes[1:]
	return node
}

type Node struct {
	value    byte
	distance int
	edges    []*Node
	checked  bool
}

func new_node(value byte) Node {
	return Node{
		value:    value,
		distance: 0,
		checked:  false,
		edges:    make([]*Node, 0),
	}
}

func allocate_graph(rows int, columns int) [][]*Node {
	graph := make([][]*Node, rows)
	for row := 0; row < rows; row++ {
		graph[row] = make([]*Node, columns)
	}
	return graph
}

func make_graph(elevation_map []string, rows int, columns int) ([][]*Node, *Node, *Node) {
	graph := allocate_graph(rows, columns)
	start_node := (*Node)(nil)
	end_node := (*Node)(nil)
	for row := 0; row < rows; row++ {
		for column := 0; column < columns; column++ {
			value := elevation_map[row][column]
			if graph[row][column] == nil {
				node := new_node(value)
				graph[row][column] = &node
			}
			if value == 'S' {
				graph[row][column].distance = 0
				start_node = graph[row][column]
				start_node.value = 'a'
			}
			if value == 'E' {
				end_node = graph[row][column]
				end_node.value = 'z'
			}

			// neighbours
			// up
			direction := row - 1
			if direction >= 0 {
				if graph[direction][column] != nil {
					graph[row][column].edges = append(graph[row][column].edges, graph[direction][column])
				} else {
					new_neightbour := new_node(elevation_map[direction][column])
					graph[row][column].edges = append(graph[row][column].edges, &new_neightbour)
					graph[direction][column] = &new_neightbour
				}
			}

			// down
			direction = row + 1
			if direction < rows {
				if graph[direction][column] != nil {
					graph[row][column].edges = append(graph[row][column].edges, graph[direction][column])
				} else {
					new_neightbour := new_node(elevation_map[direction][column])
					graph[row][column].edges = append(graph[row][column].edges, &new_neightbour)
					graph[direction][column] = &new_neightbour
				}
			}

			// left
			direction = column - 1
			if direction >= 0 {
				if graph[row][direction] != nil {
					graph[row][column].edges = append(graph[row][column].edges, graph[row][direction])
				} else {
					new_neightbour := new_node(elevation_map[row][direction])
					graph[row][column].edges = append(graph[row][column].edges, &new_neightbour)
					graph[row][direction] = &new_neightbour
				}
			}

			// right
			direction = column + 1
			if direction < columns {
				if graph[row][direction] != nil {
					graph[row][column].edges = append(graph[row][column].edges, graph[row][direction])
				} else {
					new_neightbour := new_node(elevation_map[row][direction])
					graph[row][column].edges = append(graph[row][column].edges, &new_neightbour)
					graph[row][direction] = &new_neightbour
				}
			}
		}
	}
	return graph, start_node, end_node
}

func bfs(graph [][]*Node, starting_node *Node, rows int, columns int) {
	for row := 0; row < rows; row++ {
		for column := 0; column < columns; column++ {
			graph[row][column].checked = false
		}
	}
	queue := Queue{
		nodes: make([]*Node, 0),
	}
	starting_node.checked = true
	starting_node.distance = 0
	queue.enqueue(starting_node)
	for queue.len() > 0 {
		node := queue.dequeue()
		for _, adj := range node.edges {
			if adj.checked == false && (node.value+1 == adj.value || node.value >= adj.value) {
				adj.checked = true
				adj.distance = node.distance + 1
				queue.enqueue(adj)
			}
		}
	}
}

func main() {
	f, err := ioutil.ReadFile("input")
	if err != nil {
		log.Fatal(err)
	}
	elevation_map := strings.Split(string(f), "\n")
	elevation_map = elevation_map[:len(elevation_map)-1]
	rows, columns := len(elevation_map), len(elevation_map[0])
	graph, starting_node, end := make_graph(elevation_map, rows, columns)

	bfs(graph, starting_node, rows, columns)

	println("Part 1:", end.distance)
	min := -1
	for row := 0; row < rows; row++ {
		for column := 0; column < columns; column++ {
			if graph[row][column].value == 'a' {
				bfs(graph, graph[row][column], rows, columns)
				if min == -1 || end.distance < min {
					min = end.distance
				}
			}
		}
	}
	println("Part 2:", min)
}

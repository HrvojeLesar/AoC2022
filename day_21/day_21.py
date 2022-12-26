from typing import Union

class Node:
    def __init__(self, identifier: str, value: Union[int, None], operation: Union[str, None]):
        self.id = identifier
        self.value = value
        self.operation = operation
        self.left = None
        self.right = None

    def add_left(self, left: "Node"):
        self.left = left

    def add_right(self, left: "Node"):
        self.left = left

if __name__ == "__main__" :
    terminal_output = open("examplein", "r")
    monkeys = {}
    yelled = {}
    nodes = {}
    lines = terminal_output.readlines();
    for line in lines:
        monkey_name = line[0:4]
        monkey_yell = line[6:].strip().split(" ")
        nodes[monkey_name] = monkey_yell
        if len(monkey_yell) == 1:
            yelled[monkey_name] = int(monkey_yell[0])
        else:
            monkeys[monkey_name] = monkey_yell

    more_nodes = {}
    for key in nodes:
        l = len(nodes[key])
        if l == 1:
            more_nodes[key] = Node(key, nodes[key][0], None)
        else:
            more_nodes[key] = Node(key, None, nodes[key][1])
            left = nodes[nodes[key][0]]
            right = nodes[nodes[key][2]]

    while len(monkeys.keys()) > 0:
        found = None
        for monkey in monkeys.keys():
            num1 = yelled.get(monkeys[monkey][0])
            operation = monkeys[monkey][1]
            num2 = yelled.get(monkeys[monkey][2])
            if num1 != None and num2 != None:
                found = monkey
                if operation == "+":
                    yelled[monkey] = num1 + num2
                elif operation == "-":
                    yelled[monkey] = num1 - num2
                elif operation == "*":
                    yelled[monkey] = num1 * num2
                elif operation == "/":
                    yelled[monkey] = num1 / num2
                break
        if found != None:
            monkeys.pop(found)

    print(int(yelled["root"]))

from typing import Union


ROOT = "/"
PART_1_MAX_FILE_SIZE = 100000
TOTAL_DISK_SPACE = 70000000
AT_LEAST_NEEDED = 30000000

class File:
    def __init__(self, filename: str, size: int):
        self.filename = filename
        self.size = size

class Dir:
    def __init__(self, dir: "Node", size: int) -> None:
        self.dir = dir
        self.size = size


class Node:
    def __init__(self, dir: str, parent: Union["Node",  None] = None) -> None:
        self.dir = dir
        self.files: list[File] = [];
        self.directories: list[Node] = [];
        self.parent = parent

class FileTree:
    def __init__(self) -> None:
        self.head = Node(ROOT)
        self.current_node: Node = self.head
        self.part_1_result = 0
        self.all_dirs_sized: list[Dir] = []

    def cd(self, dir: str):
        if dir == "..":
            if self.current_node.dir != ROOT and self.current_node.parent:
                self.current_node = self.current_node.parent
        elif dir == "/":
            self.current_node = self.head
        else: 
            goto_dir = [x for x in self.current_node.directories if x.dir == dir]
            if len(goto_dir) == 1:
                self.current_node = goto_dir[0]

    def create_dir(self, dir: str):
        self.current_node.directories.append(Node(dir, self.current_node))

    def add_file(self, filename: str, size: int):
        self.current_node.files.append(File(filename, size))

    def part_1(self):
        self.dir_sizes();
        print("Part 1: " + str(self.part_1_result))

    def dir_sizes(self, start: Union[Node, None] = None) -> int:
        size = 0
        if start == None:
            current = self.head
        else:
            current = start
        for file in current.files:
                size += file.size
        for dir in current.directories:
            size += self.dir_sizes(dir)

        if size <= PART_1_MAX_FILE_SIZE:
            self.part_1_result += size;

        self.all_dirs_sized.append(Dir(current, size))

        return size

    def part_2(self):
        self.all_dirs_sized = []
        unused_space = TOTAL_DISK_SPACE - self.dir_sizes()
        sorted_sizes = sorted(self.all_dirs_sized, key=lambda dir: dir.size)
        for sizes in sorted_sizes:
            if (sizes.size + unused_space >= AT_LEAST_NEEDED):
                print("Part 2: " + str(sizes.size))
                break


    def print_tree(self, start: Union[Node, None] = None, indent: int = 0):
        if start == None:
            current = self.head
        else:
            current = start
        print(indent * "  " + " - "+ current.dir + " (dir)")
        for file in current.files:
            print((indent + 1) * "  " + " - "+ file.filename + " (file, size=" + str(file.size) + ")")
        for dir in current.directories:
            self.print_tree(dir, indent + 1)


if __name__ == "__main__" :
    terminal_output = open("input", "r")
    lines = terminal_output.readlines();
    file_tree = FileTree()
    for line in lines:
        split = line.strip().split(" ")
        if line[0] == '$':
            command = split[1]
            if command == "cd":
                file_tree.cd(split[2])
        elif split[0] == "dir":
            file_tree.create_dir(split[1])
        else:
            file_tree.add_file(split[1], int(split[0]))

    file_tree.part_1()
    file_tree.part_2()

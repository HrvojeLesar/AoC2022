#include <cstddef>
#include <fstream>
#include <iostream>
#include <string>

class AoC {
  private:
    std::string sequence;
    int position{4};
    int position2{14};

  public:
    AoC() {
        std::ifstream file("input");
        std::getline(file, this->sequence);
        std::cout << "Part 1: " << this->part1() << "\n";
        std::cout << "Part 2: " << this->part2() << "\n";
    }

  private:
    int part1() {
        for (size_t i = 0; i < this->sequence.length(); i++) {
            if (i + 4 >= this->sequence.length()) {
                break;
            }
            std::string marker = this->sequence.substr(i, 4);
            if (are_characters_unique(marker)) {
                return this->position;
            }
            this->position += 1;
        }
        return -1;
    }

    int part2() {
        for (size_t i = 0; i < this->sequence.length(); i++) {
            if (i + 14 >= this->sequence.length()) {
                break;
            }
            std::string marker = this->sequence.substr(i, 14);
            if (are_characters_unique(marker)) {
                return this->position2;
            }
            this->position2 += 1;
        }
        return -1;
    }

    bool are_characters_unique(std::string &marker) {
        for (size_t i = 0; i < marker.length(); i++) {
            for (size_t j = i + 1; j < marker.length(); j++) {
                if (marker.at(i) == marker.at(j)) {
                    return false;
                }
            }
        }
        return true;
    }
};

int main() {
    AoC a{};
    return 0;
}

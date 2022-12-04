#include <fstream>
#include <iostream>
#include <string>
#include <vector>

class AoC {
    std::vector<std::string> pairs{};
    int part_1_count{0};
    int part_2_count{0};

  public:
    AoC() {
        std::ifstream file("input");
        std::string buffer;
        while (std::getline(file, buffer)) {
            pairs.push_back(buffer);
        }
        this->solve();
    }

  private:
    void solve() {
        for (const auto &pair : this->pairs) {
            auto member1 = pair.substr(0, pair.find(","));
            auto member2 = pair.substr(pair.find(",") + 1, pair.length());
            int member1_nums[2] = {
                std::stoi(member1.substr(0, member1.find("-"))),
                std::stoi(
                    member1.substr(member1.find("-") + 1, member1.length())),
            };
            int member2_nums[2] = {
                std::stoi(member2.substr(0, member2.find("-"))),
                std::stoi(
                    member2.substr(member2.find("-") + 1, member2.length())),
            };
            if ((this->is_in_range(member1_nums, member2_nums[0]) &&
                 this->is_in_range(member1_nums, member2_nums[1])) ||
                (this->is_in_range(member2_nums, member1_nums[0]) &&
                 this->is_in_range(member2_nums, member1_nums[1]))) {
                part_1_count += 1;
            }

            if ((this->is_in_range(member1_nums, member2_nums[0]) ||
                 this->is_in_range(member1_nums, member2_nums[1])) ||
                (this->is_in_range(member2_nums, member1_nums[0]) ||
                 this->is_in_range(member2_nums, member1_nums[1]))) {
                part_2_count += 1;
            }
        }
        std::cout << "Part 1: " << this->part_1_count << "\n";
        std::cout << "Part 2: " << this->part_2_count << "\n";
    }

    bool is_in_range(int from_to[2], int &num) {
        return (from_to[0] <= num && from_to[1] >= num);
    }
};

int main() {
    AoC a{};
    return 0;
}

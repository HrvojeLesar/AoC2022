#include <fstream>
#include <iostream>
#include <string>
#include <vector>

class AoC {
    std::vector<std::string> trees{};
    long maximum_score{-1};
    int rows{};
    int columns{};

  public:
    AoC() {
        std::ifstream file("input");
        std::string buffer;
        while (std::getline(file, buffer)) {
            trees.push_back(buffer);
        }

        this->rows = trees.size();
        this->columns = trees[0].length();

        this->solve();
    }

  private:
    void solve() {
        int visible_count{this->rows * 2 + this->columns * 2 - 4};
        for (int i = 1; i < this->rows - 1; i++) {
            for (int j = 1; j < this->columns - 1; j++) {
                if (is_visible(this->trees[i][j], i, j)) {
                    visible_count += 1;
                }
                this->set_max_score(this->trees[i][j], i, j);
            }
        }
        std::cout << "Part 1: " << visible_count << "\n";
        std::cout << "Part 2: " << this->maximum_score << "\n";
    }

    bool is_visible(char &tree, int &row, int &col) {
        bool visible_directions[4] = {true, true, true, true};
        for (int i = row - 1; i >= 0; i--) {
            if (tree <= this->trees[i][col]) {
                visible_directions[0] = false;
            }
        }

        for (int i = row + 1; i < this->rows; i++) {
            if (tree <= this->trees[i][col]) {
                visible_directions[1] = false;
            }
        }

        for (int i = col - 1; i >= 0; i--) {
            if (tree <= this->trees[row][i]) {
                visible_directions[2] = false;
            }
        }

        for (int i = col + 1; i < this->columns; i++) {
            if (tree <= this->trees[row][i]) {
                visible_directions[3] = false;
            }
        }

        return visible_directions[0] || visible_directions[1] ||
               visible_directions[2] || visible_directions[3];
    }

    void set_max_score(char &tree, int &row, int &col) {
        long tree_score{0};
        int scores[4]{};
        for (int i = row - 1; i >= 0; i--) {
            scores[0] += 1;
            if (tree <= this->trees[i][col]) {
                break;
            }
        }

        for (int i = row + 1; i < this->rows; i++) {
            scores[1] += 1;
            if (tree <= this->trees[i][col]) {
                break;
            }
        }

        for (int i = col - 1; i >= 0; i--) {
            scores[2] += 1;
            if (tree <= this->trees[row][i]) {
                break;
            }
        }

        for (int i = col + 1; i < this->columns; i++) {
            scores[3] += 1;
            if (tree <= this->trees[row][i]) {
                break;
            }
        }

        tree_score = scores[0] * scores[1] * scores[2] * scores[3];
        if (tree_score > this->maximum_score) {
            this->maximum_score = tree_score;
        }
    }
};

int main() {
    AoC a{};
    return 0;
}

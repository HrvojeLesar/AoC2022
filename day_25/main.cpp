#include <algorithm>
#include <cmath>
#include <fstream>
#include <iostream>
#include <vector>

class AoC {
    long part1_decimal{};

  public:
    AoC() {
        std::ifstream file("input");
        if (!file.is_open()) {
            std::cout << "File not found!\n";
        } else {
            std::string buffer;
            while (std::getline(file, buffer)) {
                this->part1_decimal += this->snafu_to_decimal(buffer);
            }
            std::cout << "Part 1: " << this->decimal_to_snafu(this->part1_decimal) << "\n";
        }
    }

  private:
    long snafu_to_decimal(std::string snafu) {
        long decimal = 0;
        int snafu_len = snafu.size();
        for (auto &chr : snafu) {
            snafu_len -= 1;
            switch (chr) {
            case '2':
            case '1':
            case '0':
                decimal += (chr - '0') * std::pow(5, snafu_len);
                break;
            case '-': { // -1
                decimal += (chr - '-' - 1) * std::pow(5, snafu_len);
            } break;
            case '=': { // -2
                decimal += (chr - '=' - 2) * std::pow(5, snafu_len);
            } break;
            }
        }
        return decimal;
    }

    std::string decimal_to_snafu(long val) {
        std::string snafu{};

        while (val > 0) {
            long remainder = val % 5;
            val /= 5;
            if (remainder > 2) {
                val += 1;
                remainder -= 5;
            }
            switch (remainder) {
            case -2: {
                snafu.push_back('=');
                break;
            }
            case -1: {
                snafu.push_back('-');
                break;
            }
            case 0: {
                snafu.push_back('0');
                break;
            }
            case 1: {
                snafu.push_back('1');
                break;
            }
            case 2: {
                snafu.push_back('2');
                break;
            }
            }
        }
        std::reverse(snafu.begin(), snafu.end());
        return snafu;
    }
};

int main() {
    AoC a{};
    return 0;
};

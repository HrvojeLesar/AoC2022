#include <algorithm>
#include <cmath>
#include <fstream>
#include <iostream>
#include <optional>
#include <ostream>
#include <stack>
#include <string>
#include <unordered_map>
#include <utility>
#include <vector>

class Position {
  public:
    long long x;
    long long y;

  public:
    Position(long long x, long long y) : x(x), y(y) {}

    long long manhattan_distance(const Position &other) {
        return std::abs(this->x - other.x) + std::abs(this->y - other.y);
    }
};

std::ostream &operator<<(std::ostream &out, const Position &p) {
    return out << "x=" << p.x << ", y=" << p.y;
}

template <typename T> class Range {
  public:
    T from;
    T to;
    Range<T>(T from, T to) : from(from), to(to) {}
    Range<T>(Range const &other) : from(other.from), to(other.to) {}
    static bool compare(Range first, Range second) {
        return first.from < second.from;
    }
};

class AoC {
    std::vector<std::pair<Position, Position>> sensor_and_nearest_beacon{};
    std::unordered_map<long long, std::vector<Range<long long>>> rows2{};

  public:
    AoC() {
        std::ifstream file("input");
        if (!file.is_open()) {
            std::cout << "File not found!\n";
        } else {
            std::string buffer;
            while (std::getline(file, buffer)) {
                int comma_positions[2]{-1, -1};
                int x_positions[2]{-1, -1};
                int y_positions[2]{-1, -1};
                int colon_position{-1};
                for (int i = 0; i < buffer.size(); i++) {
                    switch (buffer[i]) {
                    case ',': {
                        if (comma_positions[0] == -1) {
                            comma_positions[0] = i;
                        } else {
                            comma_positions[1] = i;
                        }
                        break;
                    }
                    case 'x': {
                        if (x_positions[0] == -1) {
                            x_positions[0] = i;
                        } else {
                            x_positions[1] = i;
                        }
                        break;
                    }
                    case 'y': {
                        if (y_positions[0] == -1) {
                            y_positions[0] = i;
                        } else {
                            y_positions[1] = i;
                        }
                        break;
                    }
                    case ':': {
                        colon_position = i;
                        break;
                    }
                    default: {
                        break;
                    }
                    }
                }
                long long sensor_x = std::stol(
                    buffer.substr(x_positions[0] + 2, comma_positions[0]));
                long long sensor_y = std::stol(
                    buffer.substr(y_positions[0] + 2, colon_position));
                long long beacon_x = std::stol(
                    buffer.substr(x_positions[1] + 2, comma_positions[1]));
                long long beacon_y = std::stol(
                    buffer.substr(y_positions[1] + 2, buffer.size() - 1));
                std::pair<Position, Position> pair{
                    Position(sensor_x, sensor_y), Position(beacon_x, beacon_y)};
                this->sensor_and_nearest_beacon.push_back(std::move(pair));
            }
            this->solve();
        }
    }

    void solve() {
        for (auto &pair : this->sensor_and_nearest_beacon) {
            auto distance = pair.first.manhattan_distance(pair.second);
            for (long long i = 0; i <= distance; i++) {
                long long leftmost_position = pair.first.x - i;
                long long rightmost_position = pair.first.x + i;

                long long upper = pair.first.y - distance + i;
                if (this->rows2.find(upper) == this->rows2.end()) {
                    this->rows2[upper] =
                        std::vector<Range<long long>>{Range<long long>{
                            leftmost_position, rightmost_position}};
                } else {
                    this->rows2[upper].push_back(std::move(Range<long long>{
                        leftmost_position, rightmost_position}));
                }

                long long downer = pair.first.y + distance - i;
                if (this->rows2.find(downer) == this->rows2.end()) {
                    this->rows2[downer] =
                        std::vector<Range<long long>>{Range<long long>{
                            leftmost_position, rightmost_position}};
                } else {
                    this->rows2[downer].push_back(std::move(Range<long long>{
                        leftmost_position, rightmost_position}));
                }
            }
        }

        std::pair<long long, long long> part2;
        for (auto &ranges : this->rows2) {
            auto &range = ranges.second;
            std::sort(range.begin(), range.end(), Range<long long>::compare);

            std::stack<Range<long long>> stack{};
            stack.push(range[0]);

            for (int i = 1; i < range.size(); i++) {
                auto top_range = stack.top();

                if (top_range.to < range[i].from)
                    stack.push(range[i]);

                else if (top_range.to < range[i].to) {
                    top_range.to = range[i].to;
                    stack.pop();
                    stack.push(top_range);
                }
            }
            range.clear();
            while (!stack.empty()) {
                auto top = stack.top();
                range.push_back(std::move(top));
                stack.pop();
            }
            std::sort(range.begin(), range.end(), Range<long long>::compare);
            if (range.size() == 2) {
                if (range[0].to + 1 == range[1].from - 1 &&
                    range[0].to + 1 >= 0 && range[0].to + 1 <= 4000000 &&
                    ranges.first >= 0 && ranges.first <= 4000000) {
                    part2.first = range[0].to + 1;
                    part2.second = ranges.first;
                }
            }
        }
        std::cout << "Part 1: "
                  << this->rows2[2000000][0].to - this->rows2[2000000][0].from
                  << "\n";
        std::cout << "Part 2: " << part2.first * 4000000 + part2.second << "\n";
    }
};

int main() {
    AoC a{};
    return 0;
}

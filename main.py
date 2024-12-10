# Sample input map (as given in the problem description)
lab_map = [
"................................##..........................................................................#.......#.............",
"...#.......................................................................................................#......................",
".#.....................#.............................................#................#..................#...#..#.#..#...#........",
".#........................#....#.#.#..........................................#...............................................#...",
"..................#.......................................................#.......#.............#.................................",
"........................................#...................................#.........#......#...............#......#......#......",
"......#...#.......#..............#.....#..#....................#.................................................#......#.........",
"......................#...................#....#....................................#.#........................#.........#...#....",
"....................#..........................................................................................#..................",
"..#.........................#.............#...............#.......................................................................",
".#...............................#...............................#...#................#.........#......................#..........",
"...#.....................................#.........#.............#...................#.......#........................#......#....",
"#...........................#............................#.............................#........................................#.",
".........................#................#...........#..........................#.#..............................................",
"...........#.#.......#...#.............#.................................#............................................#...#.......",
"....#.......................#.......#..........#....................................#...............................#.#..#........",
"....#............#.......#.............................#..................#......#......#..............#.............#.....#.#....",
"...................................#..............#............#...#..............................................#...............",
"..............................#....#.#........#......................#...................#........#..#............................",
"....................................#.#....#.............................................#....#......#...................#........",
"..................##..................#........#.....#.........................................#.......#............#.............",
".............#......................................................##...................................................#......#.",
"#......#................#.......................##...............#.....#.......................#..#.........#.............#...#...",
".............................................................................................................#.....#..............",
".......#..........#...............#.....##......#.......#.................#............#..........................................",
".......#.............#.........................................................#................................................#.",
"...........#.......................#.....#...............#..............#........#.....#.........#.........................#......",
".........#........#...............#................................................................#......#..........#............",
"..............#..................#...#...............................#.#.................................#.............#........#.",
".....................#.......#..............................#........................#...........#....#.................#.......#.",
"..........#..................................#......#..#................##.........................................#..........#...",
".....................#.....#.........#.....#...............#..........................................#.........................#.",
"..........#...#..............................#...................#.........#........................................#.............",
".......#.........................................................................#..........#............#..#..........#..........",
".....#..#..#...............#....................#..............#...........#.....#.....................#..........................",
"................................................#.............................................................................#...",
"................#.......................................................................................#.........................",
".............#...#.......................#..................................................#....#................................",
"...............................#.......#.#.............................................#....................#....#................",
"......#....................................................#.......#......#.................................................#.....",
"...........................................................#..........................#.#.....#................#.................#",
"....#....#............#.........................#..#.........................#..................#......#...#.........##...........",
"...#..................#................................#......#.............#.#............................#......................",
"..........................#.............................................................................##........................",
"..............................................................................#.................#..........##.....................",
"....#.....................#.................#......................#...........#.........................................#........",
"..................................#..#....#.................................................................#............#.....#.#",
".#.........................#.........................#....................#......................................#................",
"................##...........................#....................................................................................",
".........#........................................#...........................#........#.........#.......#......#.......#...#...#.",
".#..................................................................................#.........#..................................#",
"....#...............#..........................#.....#....................................................#.......................",
".......................#.............................#......#...........^........#................................................",
"#.#..#.....#...................##..........#...................................................................................#..",
"..#....#.....................................................#...#.............#....#............#..#............................#",
"........................#..........#.....#............#...........#...............#............#....#.................#........#..",
".................................#.............#...#..................#................................................#..........",
"...........#............................................................................#................#....................#...",
"..#.........#.............................#............................#....................#....................#................",
".................................#..#................#................#...........................................................",
".......................#.......................................#........................#................#......#...........#.....",
"#..............#.......#.........#...#.....#......#.....................#...#....#.......................................#........",
".........#.#.............................#....#.......#..........................................................#................",
"...#............................................................................................#.................................",
"....................#...................#....................#..............#......#..#.............#.............................",
"...........#.....................................................##...............................................................",
".....#......#...............................................................................#........#...................#........",
"..............................................#.............#....#................................................................",
"......................#.....................#........................................................#...#.........#..#.....#.....",
"..#...............................................................#..#....................#.#.....................................",
"............................................#...............#.....#.....#...#..#..................................................",
".#...........................................................................#....................................................",
".............................................................................................................#....................",
"#....................................................#..............##.......................................#....................",
"#..#....................#.......#...................................................................#...#.........................",
"..#........................................#............#...............................................................#.........",
"...............#...........................#...................#....#......#..................................#....#...........#.#",
"..........##........#.........................#.......#..#.........#...............#........#........#..........#.................",
"....##.......................................#...........#...................#......#.............................................",
"..............................#.........#............................#.............................................#..............",
"......#........#...........................#..................................#.....#.............................................",
"...............#.........#.....#.............#.............#...............#.#...............................................#....",
"........................................#...............................................................................##.#...#..",
"................#.......#.....#........................................................................#........................#.",
"....................................................................................#.........................................#...",
"........................................................................................#...#.....................................",
"........................#.#.........................................................................................#......#....##",
"......#.........................#.............#...................................................................................",
"................#...........................#..............#...#..........................#.......#......##.................#..#..",
"....................................................#.#.................................#....#....................................",
"#..................................#...............................................#......#.#.............#.......................",
".....#..........#.....................................................................................#.#.......#............#....",
"..#.........................................................#...................#..................................#..###.#.....#.",
"...#.#..........#.........#..#......................#..##...#.......#..#.#.#...............#.....#....#...........................",
"...........#.....#...........#........................................................#..................#........................",
".................#........#.......#...................#...#.......#.....#........#..#.............................................",
"...............................................#.......................#..................#.......................................",
"....#...................#.#......#............#...................#..........#..##................................................",
"....#........................#...........................................................#..................#..#.............#....",
"............#...............................................#.....................................................................",
".........#.......#...................#...........#...............#....................#................#...#..#.......#...........",
"...............#...........................................................................#.....................#................",
".............#....#.........................................................#............#....#.#....#.....#..#................#..",
"..#...............#.........................................................#........##.....#...#.........................#.......",
"..........................#.....................................#.................................#...............................",
"..#.............#...#...............................#.................................................................##..........",
"......#........#........................#..........................#..............................................#...............",
"......#.........................#..#.............................................................................................#",
"..................................................#..................#.....#...........#....#...#..........#................#.....",
"....#.....................................#..........#.....#......#..........#....................................................",
"...............................#...#...###.#.............................................................#...................#....",
"...#...................................#.....#...#....................#........................#..#.......................#......#",
"....#...#........#...................#..........#......................................#............#.............................",
"#.................................#.....................#.................#................#...#.#................................",
".................................#.......#...........#..#...#......#.......#.........................#............................",
".......................................##..........................#..................................................#...........",
"......#............#.##.........#...............#..........#....#.......................................#..#.................#....",
".....#............................#.....#..................................................#.......#......................#.......",
".............#.......................................................#...#...#....#...................#...........................",
"......#.................#.#................................#..#...............#.............................#....#...........#....",
"...............#...##......#.............................#.......................................#................................",
"#..................................................#.........#....#....#.....#...................#.#....#.....#...................",
"......#...#...##.................................................................#............................#...............#...",
".#..................................#.#...............#................................#...#......................................",
".........#.......#...#.......................#..................................................#.#................#..............",
"..................#............................#.....#.....#........#.....................#........................#.......#..#...",
"..#.............................#.............#.#................................................#.#.##....................#......",
".......#.#......................#.#.....................................................##.............#..........................",
"..............#...#........................#................................#.........#.......................#..........#........",
".......................#...............................#..#.........#.............................................................",
]

def main():

    # Directions: up, right, down, left
    DIRECTIONS = {
        '^': (-1, 0),
        'v': (1, 0),
        '<': (0, -1),
        '>': (0, 1)
    }

    # Rotation order for turning right (if facing up '^', turning right yields '>')
    DIRECTION_ORDER = ['^', '>', 'v', '<']

    def turn_right(direction):
        idx = DIRECTION_ORDER.index(direction)
        return DIRECTION_ORDER[(idx + 1) % 4]

    # Create a mutable version of the map
    grid = [list(row) for row in lab_map]
    rows = len(grid)
    cols = len(grid[0])

    # Find the guard's initial position and direction
    guard_row = guard_col = None
    guard_dir = None

    for r in range(rows):
        for c in range(cols):
            if grid[r][c] in DIRECTIONS.keys():
                guard_row, guard_col = r, c
                guard_dir = grid[r][c]
                break
        if guard_row is not None:
            break

    # Mark the starting position as visited
    visited_positions = set()
    visited_positions.add((guard_row, guard_col))

    # Replace the guard's symbol with '.' since we will mark visited places with 'X' later
    grid[guard_row][guard_col] = '.'

    print(guard_row, guard_col)

    # Simulation
    while True:
        # Check what's in front
        dr, dc = DIRECTIONS[guard_dir]
        front_r = guard_row + dr
        front_c = guard_col + dc

        # If next step is out of bounds, simulation ends
        if not (0 <= front_r < rows and 0 <= front_c < cols):
            break

        # If there's an obstacle in front, turn right
        if grid[front_r][front_c] == '#':
            guard_dir = turn_right(guard_dir)
        else:
            # Step forward
            guard_row, guard_col = front_r, front_c
            visited_positions.add((guard_row, guard_col))

            # If this step makes the guard out of bounds (double check after step)
            if not (0 <= guard_row < rows and 0 <= guard_col < cols):
                break

    # Mark visited positions
    for (r, c) in visited_positions:
        grid[r][c] = 'X' if grid[r][c] != '#' else '#'

    # Count distinct visited positions
    count_visited = len(visited_positions)

    print("Number of distinct positions visited:", count_visited)
    # print("Final map:")
    for row in grid:
        print("".join(row))
    for p in visited_positions:
        print(p)

if __name__ == '__main__':
    main()
import os
import curses
import re

GRIDS = "01"

def load_files():
    """Load all files that match the grid pattern and sort them numerically."""
    files = [f for f in os.listdir(GRIDS) if f.startswith("grid_") and f.endswith(".txt")]

    # Extract numeric parts and sort numerically
    def extract_number(filename):
        match = re.search(r'(\d+)', filename)
        return int(match.group(1)) if match else float('inf')  # Use `inf` for files without numbers

    return sorted(files, key=extract_number)

def display_file(stdscr, filename, index, total_files):
    """Clear screen and display the contents of the file."""
    stdscr.clear()
    stdscr.addstr(0, 0, f"File {index + 1} of {total_files}: {filename}\n")
    with open(f'{GRIDS}/{filename}', 'r') as f:
        content = f.read()
    stdscr.addstr(1, 0, content)
    stdscr.addstr(curses.LINES - 2, 0, "Use Arrow Keys: [← Previous | → Next] | [Q Quit]")
    stdscr.refresh()

def main(stdscr):
    files = load_files()
    if not files:
        stdscr.addstr(0, 0, "No grid files found. Press any key to exit.")
        stdscr.getch()
        return

    index = 92  # Start with the first file
    total_files = len(files)

    display_file(stdscr, files[index], index, total_files)

    while True:
        key = stdscr.getch()
        if key == curses.KEY_RIGHT:  # Right arrow key
            if index < total_files - 1:
                index += 1
                display_file(stdscr, files[index], index, total_files)
            else:
                stdscr.addstr(curses.LINES - 1, 0, "You are at the last file. ")
                stdscr.refresh()
        elif key == curses.KEY_LEFT:  # Left arrow key
            if index > 0:
                index -= 1
                display_file(stdscr, files[index], index, total_files)
            else:
                stdscr.addstr(curses.LINES - 1, 0, "You are at the first file. ")
                stdscr.refresh()
        elif key == ord('q'):  # 'Q' to quit
            break

if __name__ == "__main__":
    curses.wrapper(main)
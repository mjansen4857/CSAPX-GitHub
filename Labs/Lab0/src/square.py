"""
CSapx Lab 0: Square

A program that draws a square of a given side length.  If the program is run
as:

$ python3 square.py #

The side length is gotten from the command line.

If the program is run as:

$ python3 square.py

The side length is gotten from standard input.

author: Sean Strout, Michael Jansen @ RIT CS
"""

import turtle       # forward, length, mainloop
import sys          # argv

def draw_square(length : int) -> None:
    """
    Draws a square of a given length.
    :param length: the length in pixels of one side of the square
    :return: None
    """
    for _ in range(4):
        turtle.forward(length)
        turtle.left(90)

def main() -> None:
    """
    The main function.
    :return: None
    """

    # Get the side length from either the command line (if present), or by
    # prompting the user
    if len(sys.argv) == 2:
        length = int(sys.argv[1])
    else:
        length = int(input('Enter side length: '))

    print("Drawing square with side length", length)
    draw_square(length)
    turtle.right(90)
    draw_square(length)
    print('Close the graphic window when done.')
    turtle.mainloop()

# only run this program if it is not being imported by another main module
if __name__ == '__main__':
    main()
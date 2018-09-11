"""
CSAPX Lab1: Tiny Turtle

A program that draws an image with a string of Tiny Turtle (TT) commands

author: Michael Jansen
"""
import turtle

def evaluate(s):
    """
    Evaluates a group of TT commands and executes them with turtle
    :param s: The string of TT commands
    :return: None
    """
    commands = s.split(' ')
    for s in commands:
        command = s[0]

        if command == 'U':
            turtle.up()
            print('up()')
        elif command == 'D':
            turtle.down()
            print('down()')
        elif command == 'F':
            turtle.forward(int(s[1:]))
            print('forward(' + str(int(s[1:])) + ')')
        elif command == 'B':
            turtle.backward(int(s[1:]))
            print('backward(' + str(int(s[1:])) + ')')
        elif command == 'L':
            turtle.left(int(s[1:]))
            print('left(' + str(int(s[1:])) + ')')
        elif command == 'R':
            turtle.right(int(s[1:]))
            print('right(' + str(int(s[1:])) + ')')
        elif command == 'C':
            turtle.circle(int(s[1:]))
            print('circle(' + str(int(s[1:])) + ')')

def expand_iterate(s):
    """
    Expands one or more Iterate commands into the full sequence of basic TT commands.
    Also works with nested Iterate commands
    :param s: The string to be expanded
    :return: The expanded string
    """
    # Keep expanding while there are iterate commands in the string
    while 'I' in s:
        start = s.find('I')
        end = s.find('@')
        # Checks if there is another iterate command before the end of the first one (nested)
        # Expands the inner most command first then works outwards
        while s.find('I', start + 1) < end and s.find('I', start + 1) != -1:
            start = s.index('I', start + 1)
        iterations = int(s[start + 1])
        # Insert the basic TT commands where the iterate command used to be
        s = s[:start] + (s[start + 3:end] * iterations) + s[end + 2:]
    return s.rstrip()

def expand_polygon(s):
    """
    Expands a polygon command into iterate commands that will draw the shape
    :param s: The string to be expanded
    :return: The expanded string
    """
    # Keep expanding while there are polygon commands in the string
    while 'P' in s:
        # Finds the first polygon command in the string, changes it into an iterate command, then moves to the next one
        index = s.find('P')
        sides = int(s[index + 1:index + 2])
        angle = int(360/sides)
        length = s[index + 3:index + 6]
        # Insert the new iterate command where the polygon command used to be
        s = s[:index] + 'I' + str(sides) + ' F' + length + ' L' + str(angle) + ' @' + s[index + 6:]
    return s

def main():
    """
    The main function
    :return: None
    """
    turtle.Screen()
    # Get the string of commands from the user
    command_string = input('Enter a Tiny Turtle program: ')
    # Expand polygon commands into iterate commands to be drawn
    command_string = expand_polygon(command_string)
    # Expands iterate commands into basic TT commands
    command_string = expand_iterate(command_string)
    print('Expanded Program: ' + command_string)
    # Evaluates each TT command and performs it with the turtle
    evaluate(command_string)
    turtle.done()

# Only run the program if it is not being imported by another module
if __name__ == '__main__':
    main()

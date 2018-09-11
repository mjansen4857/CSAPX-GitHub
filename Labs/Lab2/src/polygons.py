"""
Draws polygons recursively with random colors

Author: Michael Jansen
"""
import turtle
import random
import sys

NAME_POSITION = -350, -300
POLYGON_POSITION = -100, -150

def draw_polygons_rec(sides, length, fill):
    """
    Draws polygons recursively. Polygons will get smaller and have less sides as they get further out
    :param sides: The number of sides to start with
    :param length: The side length to start with
    :param fill: Should the polygons be filled
    :return: The total length of the sides drawn
    """
    total = 0
    if sides > 2:
        for i in range(0, sides):
            turtle.up()
            # Draw the polygon on the first iteration,  then go side by side
            if i == 0:
                r = random.random()
                g = random.random()
                b = random.random()
                turtle.fillcolor(r, g, b)
                turtle.down()
                if fill:
                    turtle.begin_fill()
                for j in range(0, sides):
                    turtle.forward(length)
                    turtle.left(360/sides)
                if fill:
                    turtle.end_fill()
            turtle.forward(length)
            # Add the current side length and the recursive polygon length to the total
            total += length
            total += draw_polygons_rec(sides-1, length/2, fill)
            turtle.left(360/sides)
    return total

def turtle_init():
    """
    Draws my name and sets up the turtle to draw the polygons
    :return: None
    """
    turtle.tracer(0, 0)
    turtle.color('black')
    turtle.up()
    turtle.setx(NAME_POSITION[0])
    turtle.sety(NAME_POSITION[1])
    turtle.down()
    turtle.write('Michael Jansen', font=('Arial', 20, 'normal'))
    turtle.up()
    turtle.setx(POLYGON_POSITION[0])
    turtle.sety(POLYGON_POSITION[1])

def main():
    """
    The main function
    :return: None
    """
    # Draw name and init turtle
    turtle_init()
    # Check that there are valid inputs
    if len(sys.argv) < 2:
        print('Invalid Inputs! Correct usage: $ python3 polygons.py #_sides [fill|unfill}')
        return
    num_sides = int(sys.argv[1])
    fill = False
    if len(sys.argv) == 3 and sys.argv[2] == 'fill':
        fill = True
    print('Total Length: ' + str(draw_polygons_rec(num_sides, 150, fill)))
    turtle.update()
    turtle.mainloop()

# Starts the program
if __name__ == '__main__':
    main()

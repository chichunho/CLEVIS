# CLEVIS

CLEVIS stands for **C**ommand **L**in**E** 
**V**ector graph**I**cs **S**oftware. 
This is a object-oriented programming course project.

This repository contains:
1. Description
2. Dependencies
3. How to install and execute
4. Supported commands
5. Authors

## Description

Year: 2021

CLEVIS provide function of creating and manipulating vector graphics containing shapes like line segments, circle, rectangles and squares.
Common features like undo and redo are also supported.


## Dependencies

- Java 11 environment
- Intellij IDEA IDE (Recommended, but it can also be compiled in shell.)

## How to install and execute

To install this project, you can simply download the source code 
under folder "src/hk/edu/polyu/comp/comp2021/clevis/"
to your desired directory.

To compile this project in intellij IDEA,
access the "Edit Configurations" in "Application.java".
For extra command parameters, enter
"-html log.html -txt log.txt".

If the parameters are set correctly, you should see the following lines
in the first launch:
```sh
New txt file created at {current directory}\log.txt
```

## Supported commands

- To create rectangle.
```sh
rectangle {name} {x-coordinate} {y-coordinate} {width} {height}
```
i.e.
```sh
rectangle rec1 2 3 3 4
```

- To create a square.
```sh
square {name} {x-coordinate} {y-coordinate} {length}
```
i.e.
```sh
square squ1 4 5 6
```

- To create a circle.
```sh
circle {name} {x-coordinate} {y-coordinate} {radius}
```
i.e.
```sh
circle cir1 4 6 3
```

- To create a line segement.
```sh
line {name} {x-coordinate(start)} {y-coordinate(start)} {x-coordinate(end)} {y-coordinate(end)}
```
i.e.
```sh
line line1 1 1 2 2
```

- To delete a shape or a grouped shape.
```sh
delete {name}
```
i.e.
```sh
delete squ1
```

- To group shape(s).
```sh
group {group name} {shape names...}
```
i.e.
```sh
group group1 rec1 cir1
```

- To ungroup a grouped shape.
```sh
ungroup {grouped shape name}
```
i.e.
```sh
ungroup group1
```

- To obtain all shape information existed.
```sh
listall
```

- To obtain a specific shape information.
```sh
list {shape name}
```
i.e.
```sh
list squ1
```

- To obtain the minimum bounding box of a shape.
```sh
boundingbox {shape name}
```
i.e.
```sh
boundingbox cir1
```

- To move a shape by name.
```sh
move {shape name} {x-axis displacement} {y-axis displacement}
```
i.e.
```
move cir1 -3 2
```

- To move a shape by coordinate.
```sh
pick-and-move {x-coordinate} {y-coordinate}
```
i.e.
```sh
pick-and-move 3 4
```

- To know if a shape A is intersected with another shape B.
```sh
intersect {shape A name} {shape B name}
```
i.e.
```sh
intersect squ1 cir1
```

- To redo a command.
```sh
redo
```

- To undo a command.
```sh
undo
```

- To quit the application.
```sh
quit
```


## Authors

- [@chichunho](https://github.com/chichunho)
- [@CheungKW0301](https://github.com/CheungKW0301)


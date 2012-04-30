This is a program that allows the user to input various populations and determine the simplest pattern that will produce enough unique plates.  Included is an executable .jar file, as well as the source code in a separate folder.

To use it, simply run "License Plate.jar" and enter the population you want to find a pattern for.  The program automatically removes non-numerical characters from the input field and supports populations up to 10 quadrillion.  The option to allow alphanumeric characters is enabled by default, and lets a single character in the pattern be filled by either a letter or a number.

Interpretation of the pattern:
'A' represents any letter (A-Z)
'#' represents any number (0-9)
'*' represents any letter or number (A-Z, 0-9)

Example:
A pattern of AA#** would produce 8,760,960 plates and the following plates would all be valid:
BK904, BK9AB, ZR3P5
However B904K would not be valid, as the second character is not a letter.
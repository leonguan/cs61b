61bproj2
========

To run the project on eclipse, change run configurations to include two parameters - the types of players involved (human/machine/random).

READ THE PROJECT README: http://www.cs.berkeley.edu/~jrs/61b/hw/pj2/readme   ESPECIALLY the grading guidelines on the bottom


------------
This is how chips/networks are related.

Each chip knows which board it is from:

  When we construct a chip, we pass in information of which board it is from.
  When we construct a new board, we construct a new set of chips for that board, whose chips know that it is from the new board
  By knowing which board a chip is from, a chip can access all of the other chips on the board

Each chip will know which chips it can see, aka which chips it can form a connection with:
  
  The methods sameColorChips and otherColorChips return a Chip[] of all the other chips of the designated color on the same board
  The method canSee will take in another chip and return whether or not the current chip can see the other chip on the board and thus can form a connection with it
  
  A third method, visibleChips will iterate through all of the chips of sameColorChips and use canSee to determine whether or not it can connect with those chips, returning an array of all the chips it can see. also, it might save it
  
I'm guessing another method will randomly form connections in sequence using:

  recursive calls that keep track of which chips have been used in the connection already
  starts at one of the goals
  does not stop until it hits a goal
  chooses not to go towards a goal unless it's length is already >= 5
  returns false if no more possible choices
  

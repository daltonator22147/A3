import becker.robots.*;

/**
 * CS1A - Assignment 3 - "The Maze" <br>
 * Quarter: <br>
 * TODO: THE SUMMARY GOES HERE <br>
 * 
 * @author student's full name 1
 * @author student's full name 2
 * @author student's full name 3
 */
class MazeBot extends RobotSE
{
    // TODO: Instance Variables will be declared and initialized here
    // one each for totalMoves, movesWest, movesEast, movesSouth, and MovesNorth
    int totalMoves;
    int movesWest;
    int movesEast;
    int movesSouth;
    int movesNorth;

    public MazeBot(City theCity, int str, int ave, Direction dir, int numThings)
    {
        super(theCity, str, ave, dir, numThings);
    }

    /*
     * TODO: Override here the move method and make it count everything it is
     * supposed to by adding to the instance variables as well as moving the
     * MazeBot. This overridden move method must be called by the NavigateMaze
     * method or by some method(s) called from NavigateMaze.
     */
    public void moveBot()
    {
       this.botPutThing();
        this.move();
        this.botPutThing();
        totalMoves++;
        if (this.getDirection() == Direction.EAST)
        {
            movesEast++;
        } else if (this.getDirection() == Direction.NORTH)
        {
            movesNorth++;
        } else if (this.getDirection() == Direction.SOUTH)
        {
            movesSouth++;
        } else if (this.getDirection() == Direction.WEST)
        {
            movesWest++;
        }
        
    }

    // TODO: You must override the putThing method here.
    public void botPutThing(){
        if(this.countThingsInBackpack()!=0){
            this.putThing();
        }
    }

    public void printEverything()// Or printTotalNumberOfSpacesMoved(),
    // whichever you decide
    {
        System.out.println("Total moves: "+totalMoves);
        System.out.println("Moved west: "+movesWest);
        System.out.println("Moved east: "+movesEast);
        System.out.println("Moved north: "+movesNorth);
        System.out.println("Moved south: "+movesSouth);
    }

    // The isAtEndSpot() method below is what's called a 'helper method' It
    // exists just to make another command (in this case, NavigateMaze) easier
    // to understand. It does this by replacing some code that otherwise would
    // be in NavigateMaze with it's name, and doing that work here, instead.
    // Declaring it "private" means that only the MazeBot is allowed to call
    // upon it.
    private boolean isAtEndSpot()
    {
        return getAvenue() == 9 && getStreet() == 10;
    }

    // THIS IS THE METHOD WE WILL USE TO DO EVERYTHING (IT WILL CALL
    // OTHER METHODS LIKE isAtEndSpot, ETC)
    public void navigateMaze()
    {
       
       while(!isAtEndSpot()) {
          this.turnRight();
          if(this.frontIsClear()) {
             this.moveBot();
          }
          
          else {
             this.turnLeft();
          }
          
          if(this.frontIsClear()) {
             this.moveBot();
          }
    }
  }
}

// ###################################################################################################
// NO NEED TO TOUCH ANYTHING FROM HERE ON DOWN, EXCEPT TO CHANGE NUMBER OF
// THINGS IN BACKPACK IN MAIN AND ADDING JavaDoc
// The NavigateMaze() method is already set up and called by don the robot down
// in main
// ###################################################################################################
public class Maze extends Object
{
    private static void makeMaze(City theCity)
    {
        for (int i = 1; i < 11; i++)
        {
            // north wall
            new Wall(theCity, 1, i, Direction.NORTH);

            // Second to north wall
            if (i <= 9)
                new Wall(theCity, 1, i, Direction.SOUTH);

            // Third to north wall
            if (i >= 4)
                new Wall(theCity, 4, i, Direction.SOUTH);

            // south wall
            if (i != 9) // (9, 10, SOUTH), is where the 'exit' is
                new Wall(theCity, 10, i, Direction.SOUTH);

            // west wall
            if (i != 1) // (1, 1, WEST) is where the 'entrance' is
                new Wall(theCity, i, 1, Direction.WEST);

            // second to most western wall
            if (i >= 3 && i < 6)
                new Wall(theCity, i, 6, Direction.WEST);

            // east wall
            new Wall(theCity, i, 10, Direction.EAST);
        }

        // Cul de Sac
        new Wall(theCity, 3, 10, Direction.WEST);
        new Wall(theCity, 3, 10, Direction.SOUTH);

        new Wall(theCity, 2, 8, Direction.WEST);
        new Wall(theCity, 2, 8, Direction.SOUTH);

        new Wall(theCity, 10, 8, Direction.NORTH);
        new Wall(theCity, 10, 9, Direction.EAST);
        new Wall(theCity, 10, 9, Direction.NORTH);
        makeSpiral(theCity, 8, 9, 3);
        new Wall(theCity, 8, 10, Direction.SOUTH);

        makeSpiral(theCity, 10, 5, 4);
    }

    public static void makeSpiral(City theCity, int st, int ave, int size)
    {
        // We start out building the wall northward
        // the walls will be built on the east face of the current
        // intersection
        Direction facing = Direction.EAST;

        while (size > 0)
        {
            int spacesLeft = size;
            int aveChange = 0;
            int stChange = 0;
            switch (facing)
            {
            case EAST:
                stChange = -1;
                break;
            case NORTH:
                aveChange = -1;
                break;
            case WEST:
                stChange = 1;
                break;
            case SOUTH:
                aveChange = 1;
                break;
            }

            while (spacesLeft > 0)
            {
                new Wall(theCity, st, ave, facing);
                ave += aveChange;
                st += stChange;
                --spacesLeft;
            }
            // back up one space
            ave -= aveChange;
            st -= stChange;

            switch (facing)
            {
            case EAST:
                facing = Direction.NORTH;
                break;
            case NORTH:
                facing = Direction.WEST;
                size--;
                break;
            case WEST:
                facing = Direction.SOUTH;
                break;
            case SOUTH:
                facing = Direction.EAST;
                size--;
                break;
            }
        }
    }

    // ###########################################################################################
    // Main Method
    // ###########################################################################################
    public static void main(String[] args)
    {
        City calgary = new City(12, 12);
        MazeBot don = new MazeBot(calgary, 1, 1, Direction.EAST, 1000); // TODO: <-- YOU WILL NEED TO CHANGE THIS FROM ZERO

        Maze.makeMaze(calgary);

        calgary.showThingCounts(true); // This will help you see if you incorrectly put more than 1 thing down in any
                                       // intersections

        //don.navigateMaze(); // <-- HERE'S WHERE THE NavigateMaze() method is
        // called. NO NEED TO TOUCH AT ALL
        //don.printEverything();
        don.navigateMaze();
    }
}

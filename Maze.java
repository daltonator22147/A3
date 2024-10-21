import becker.robots.*;

/**
 * CS1A - Assignment 3 - "The Maze" <br>
 * Quarter: Fall 2024 <br>
 * This robot follows the right wall while dropping items everytime it moves. As it moves it records its steps and where
 * it faces as it moves until it escapes the maze and finally outputs its movement data. <br>
 * moveBot function: The moveBot function first checks if there is already a thing it can pick up. If theres nothing to pick
 * up it wont drop a thing, otherwise it will drop a thing. Then it moves, records that it has moved a space and finally
 * checks which direction its facing and increase one of the direction values by 1 depending on which direction its
 * facing.
 * botPutThing function: This function first checks if the bot has any things in his backpack. If he does he drops them,
 * otherwise the function ends.
 * printEverything function: This function prints all the data of the bots movement including which direction it moved
 * in.
 * navigateMaze function: This function combines all the functions together with a few other methods to help the bot
 * escape the maze. While the bot isnt at the end checkpoint it turns right to check if theres a wall and depending on
 * the result it will either move, turn left and move forward or keep turning left until its front is clear. And lastly
 * prints the bots movement data results.
 * @author Leo Ismail
 * @author Dalton Langely
 */
class MazeBot extends RobotSE
{
    int totalMoves;
    int movesWest;
    int movesEast;
    int movesSouth;
    int movesNorth;

    public MazeBot(City theCity, int str, int ave, Direction dir, int numThings)
    {
        super(theCity, str, ave, dir, numThings);
    }

    public void moveBot()
    {
        if(!this.canPickThing()){
            this.botPutThing();
        }
        this.move();
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
    public void botPutThing()
    {
        if (this.countThingsInBackpack() != 0)
        {
            this.putThing();
        }
    }

    public void printEverything()
    {
        System.out.println("Total number of spaces moved: " + totalMoves);
        System.out.println("Total number of westward movements: " + movesWest);
        System.out.println("Total number of eastward movements: " + movesEast);
        System.out.println("Total number of northward movements: " + movesNorth);
        System.out.println("Total number of southward movements: " + movesSouth);
        System.out.println("Hooray! don has solved the maze.");
    }

    private boolean isAtEndSpot()
    {
        return getAvenue() == 9 && getStreet() == 10;
    }

    public void navigateMaze()
    {
        while (!isAtEndSpot())
        {
            this.turnRight();
            if (this.frontIsClear())
            {
                this.moveBot();
            } else
            {
                while (!this.frontIsClear())
                {
                    this.turnLeft();
                }

                this.moveBot();
            }
        }
        this.printEverything();
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
        MazeBot don = new MazeBot(calgary, 1, 1, Direction.EAST,
                1000); // TODO: <-- YOU WILL NEED TO CHANGE THIS FROM ZERO

        Maze.makeMaze(calgary);

        calgary.showThingCounts(true); // This will help you see if you incorrectly put more than 1 thing down in any
        // intersections

        don.navigateMaze(); // <-- HERE'S WHERE THE NavigateMaze() method is
        // called. NO NEED TO TOUCH AT ALL
        don.printEverything();
    }
}

import comp1110.universe.*;
import static comp1110.universe.Colour.*;
import static comp1110.universe.Image.*;
import static comp1110.universe.Universe.*;
import static comp1110.lib.Functions.*;

import comp1110.lib.*;
import comp1110.lib.Date;
import static comp1110.testing.Comp1110Unit.*;

/* The width and height of the world (in pixels) */
int WORLD_WIDTH = 300;
int WORLD_HEIGHT = 500;

/* Radius of the balls */
int BALL_RADIUS = 10;

/* Initial X and Y position of the balls */
int INITIAL_BALL1_POSX = 75;
int INITIAL_BALL1_POSY = 125;
int INITIAL_BALL2_POSX = 225;
int INITIAL_BALL2_POSY = 125;
int INITIAL_BALL3_POSX = 75;
int INITIAL_BALL3_POSY = 375;
int INITIAL_BALL4_POSX = 225;
int INITIAL_BALL4_POSY = 375;

/* Speed of the ball's movement */
int BALL_SPEED = 1;

/* Bottom, Top, Left, Right boundary of the world */
int AtBottom = WORLD_HEIGHT - BALL_RADIUS;
int AtTop = BALL_RADIUS;
int AtLeft = BALL_RADIUS;
int AtRight = WORLD_WIDTH - BALL_RADIUS;


enum Direction {
    North,
    South,
    East,
    West,
    NorthEast,
    NorthWest,
    SouthEast,
    SouthWest
}

/**
 * A ball represented by its X and Y position and its movement direction.
 */
record Ball(int posX, int posY, Direction dir, Colour colour) {}

record World(Ball b1, Ball b2, Ball b3, Ball b4) {}

/**
 * Given a marble's current direction, return its movement vector.
 * Cardinal directions move 1 pixel in one axis (North/South/East/West),
 * ordinal directions move 1 pixel in both axis (NorthEast/NorthWest/SouthEast/SouthWest).
 * Example:
 *    - Given: Ball b = new Ball(100, 200, Direction.North, BLUE);
 *             Pair<Integer, Integer> dir = moveDirection(Direction.North);
 *    - Expect: Pair(0,-1), move to North
 *    - Given: Pair<Integer, Integer> dir = moveDirection(Direction.SouthEast);
 *    - Expect: Pair(1,1), move to SouthEast
 * @param dir the current direction of the marble
 * @return a Pair representing the change in (x, y) coordinates per step
 */
Pair<Integer, Integer> moveDirection(Direction dir) {
    return switch (dir) {
        case North -> new Pair<Integer, Integer>(0,-1);
        case South -> new Pair<Integer, Integer>(0,1);
        case East -> new Pair<Integer, Integer>(1,0);
        case West -> new Pair<Integer, Integer>(-1,0);
        case NorthEast -> new Pair<Integer, Integer>(1,-1);
        case NorthWest -> new Pair<Integer, Integer>(-1,-1);
        case SouthEast -> new Pair<Integer, Integer>(1,1);
        case SouthWest -> new Pair<Integer, Integer>(-1,1);
    };
}

/**
 * Move the ball one step based on its current direction.
 * This method determine the movement vector for the ball's direction, then adds to the ball's current position.
 * Example:
 *    - Given:  Ball b = new Ball(100, 200, Direction.North, BLUE);
 *              Pair<Integer, Integer> dir = moveDirection(Direction.North);
 *      Expect: dir = Pair(0, -1), new Ball(100, 199, Direction.North, BLUE)
 *    - Given:  Ball b = new Ball(50, 50, Direction.SouthEast, RED);
 *              Pair<Integer, Integer> dir = moveDirection(Direction.SouthEast);
 *      Expect: dir = Pair(1, 1), new Ball(51, 51, Direction.SouthEast, RED)
 *    - Given:  Ball b = new Ball(75, 100, Direction.West, GREEN);
 *              Pair<Integer, Integer> dir = moveDirection(Direction.West);
 *      Expect: dir = Pair(-1, 0), new Ball(74, 100, Direction.West, GREEN)
 * @param ball the ball to be moved
 * @return a new Ball with updated (x, y) position but same direction and colour
 */
Ball moveBall(Ball ball) {
    Pair<Integer,Integer> posXY = moveDirection(ball.dir);
    return new Ball(ball.posX + posXY.first() * BALL_SPEED, ball.posY + posXY.second() * BALL_SPEED, ball.dir, ball.colour);
}

/**
 * Change the marble's direction if it has reached the TOP edge of the world.
 * It reverses the vertical component of movement for directions that go North and keeps other directions unchanged.
 *   - If direction is North, change to South.
 *   - If direction is NorthEast, change to SouthEast.
 *   - If direction is NorthWest, change to SouthWest.
 *   - Otherwise, keep the same direction.
 * Example:
 *    - Given:  dir = North
 *      Expect: South
 *    - Given:  dir = NorthEast
 *      Expect: SouthEast
 *    - Given:  dir = East
 *      Expect: East (unchanged because not moving North)
 * @param dir the current direction of the marble
 * @return the new direction after bouncing on the top edge
 */
Direction changeBallDirectionAtTop(Direction dir) {
    return switch (dir) {
        case North -> Direction.South;
        case NorthEast -> Direction.SouthEast;
        case NorthWest -> Direction.SouthWest;
        default -> dir;
    };
}

Direction changeBallDirectionAtBottom(Direction dir) {
    return switch (dir) {
        case South -> Direction.North;
        case SouthEast -> Direction.NorthEast;
        case SouthWest -> Direction.NorthWest;
        default -> dir;
    };
}

Direction changeBallDirectionAtLeft(Direction dir) {
    return switch (dir) {
        case West -> Direction.East;
        case NorthWest -> Direction.NorthEast;
        case SouthWest -> Direction.SouthEast;
        default -> dir;
    };
}

Direction changeBallDirectionAtRight(Direction dir) {
    return switch (dir) {
        case East -> Direction.West;
        case NorthEast -> Direction.NorthWest;
        case SouthEast -> Direction.SouthWest;
        default -> dir;
    };
}

Direction changeBallDirectionAtTopLeft(Direction dir) {
    return switch (dir) {
        case North -> Direction.South;
        case West -> Direction.East;
        case NorthEast -> Direction.SouthWest;
        case NorthWest -> Direction.SouthEast;
        case SouthEast -> Direction.SouthWest;
        default -> dir;
    };
}

Direction changeBallDirectionAtTopRight(Direction dir) {
    return switch (dir) {
        case North -> Direction.South;
        case East -> Direction.West;
        case NorthEast -> Direction.SouthWest;
        case NorthWest -> Direction.SouthWest;
        case SouthEast -> Direction.SouthWest;
        default -> dir;
    };
}

Direction changeBallDirectionAtBottomLeft(Direction dir) {
    return switch (dir) {
        case South -> Direction.North;
        case West -> Direction.East;
        case SouthEast -> Direction.NorthWest;
        case SouthWest -> Direction.NorthEast;
        case NorthWest -> Direction.SouthEast;
        default -> dir;
    };
}

Direction changeBallDirectionAtBottomRight(Direction dir) {
    return switch (dir) {
        case South -> Direction.North;
        case East -> Direction.West;
        case SouthEast -> Direction.NorthWest;
        case SouthWest -> Direction.NorthEast;
        case NorthEast -> Direction.SouthWest;
        default -> dir;
    };
}

/**
 * Reverse the direction of the ball if it is on a boundary.
 *   - Check corners first (top-left, top-right, bottom-left, bottom-right).
 *   - Otherwise, check single edges (top, bottom, left, right).
 *   - If none match, keep the direction.
 * Examples:
 *   - Given:  Ball(10, 10, North, BLUE) with AtTop=10, AtLeft=10
 *     Expect: newDir = SouthEast  (top-left corner)
 *   - Given:  Ball(290, 10, NorthEast, RED) with AtTop=10, AtRight=290
 *     Expect: newDir = SouthWest  (top-right corner)
 *   - Given:  Ball(150, 490, South, GREEN) with AtBottom=490
 *     Expect: newDir = North      (bottom edge)
 * @param ball the ball at the start
 * @return a new Ball with possibly updated direction (position unchanged)
 */
Ball changeBallDirection(Ball ball) {
    Direction newDir = ball.dir;
    // corners
    if (ball.posY == AtTop && ball.posX == AtLeft) {
        newDir = changeBallDirectionAtTopLeft(ball.dir);
    } else if (ball.posY == AtTop && ball.posX == AtRight) {
        newDir = changeBallDirectionAtTopRight(ball.dir);
    } else if (ball.posY == AtBottom && ball.posX == AtLeft) {
        newDir = changeBallDirectionAtBottomLeft(ball.dir);
    } else if (ball.posY == AtBottom && ball.posX == AtRight) {
        newDir = changeBallDirectionAtBottomRight(ball.dir);
    // single edges
    } else if (ball.posY == AtTop) {
        newDir = changeBallDirectionAtTop(ball.dir);
    } else if (ball.posY == AtBottom) {
        newDir = changeBallDirectionAtBottom(ball.dir);
    } else if (ball.posX == AtLeft) {
        newDir = changeBallDirectionAtLeft(ball.dir);
    } else if (ball.posX == AtRight) {
        newDir = changeBallDirectionAtRight(ball.dir);
    }
    return new Ball(ball.posX, ball.posY, newDir, ball.colour);
}

/**
 * Update the ball's state for the next step.
 * 1. Check if the ball is currently touch any boundary.
 * 2. If so, change its direction according to the bounce rules for that boundary.
 * 3. Move the ball exactly one step in its new direction.
 * Examples:
 *   - Given: Ball(10, 10, North, BLUE) at top-left corner
 *     Expect: Direction changes to SouthEast, Position becomes (11, 11).
 *   - Given: Ball(150, 10, North, RED) at top edge
 *     Expect: Direction changes to South, Position becomes (150, 11).
 * @param myBall the ball before updating
 * @return a new Ball after bouncing and moving one step
 */
Ball step(Ball b) {
    return moveBall(changeBallDirection(b));
} 

/**
 * Process a key event: 
 * If the spacebar is pressed, change all marbles' directions 
 * to a random cardinal direction (North, South, East, West).
 * Positions and colours remain unchanged.
 *  Example:
 *    - Given: World with b1 = Ball(100, 100, NorthEast, BLUE),
 *                        b2 = Ball(200, 100, NorthEast, RED),
 *                        b3 = Ball(100, 200, NorthEast, GREEN),
 *                        b4 = Ball(200, 200, NorthEast, BLACK), keyEventKind = KEY_PRESSED, key = "Space"
 *      Expect: A new World where b1–b4 keep their positions and colours, but each has a random cardinal direction.
 * @param w the current world containing four balls
 * @param keyEventKind the type of keyboard event (eg. KEY_PRESSED, KEY_RELEASED, KEY_TYPED)
 * @param key the key string ("Space")
 * @return a new World with updated directions if pressed, otherwise no change
 */
World processKeyEvent(World w, KeyEventKind keyEventKind, String key) {
  if (keyEventKind == KeyEventKind.KEY_PRESSED && Equals("Space",key)) {
    return new World(
        new Ball(w.b1().posX(), w.b1().posY(), randomCardinal(), w.b1().colour()),
        new Ball(w.b2().posX(), w.b2().posY(), randomCardinal(), w.b2().colour()),
        new Ball(w.b3().posX(), w.b3().posY(), randomCardinal(), w.b3().colour()),
        new Ball(w.b4().posX(), w.b4().posY(), randomCardinal(), w.b4().colour())
    );
  } else {
    return w;
  }
}

/**
 * Handle a full key event object.
 * Get the event kind and key string from the given KeyEvent,
 * and passes them to processKeyEvent to determine if and how the world's state should change.
 * Example:
 *    - Given:  World w with four balls all moving NorthEast, keyEvent = KeyEvent(KEY_PRESSED, "Space")
 *      Expect: Returns a new World where all balls keep their positions and colours, but each has a random cardinal direction.
 * @param w the current world containing four balls
 * @param keyEvent the KeyEvent object
 * @return a new World with updated ball directions if pressed, otherwise no change
 */
World keyEvent(World w, KeyEvent keyEvent) {
    return processKeyEvent(w, keyEvent.kind(), keyEvent.key());
}

/**
 * Process a mouse event: 
 * If the left mouse is clicked, change all marbles' directions 
 * to a random ordinal direction (NorthEast, NorthWest, SouthEast, SouthWest).
 * Positions and colours remain unchanged.
 * Example:
 *    - Given: World with b1 = Ball(100, 100, North, BLUE),
 *                        b2 = Ball(200, 100, South, RED),
 *                        b3 = Ball(100, 200, East, GREEN),
 *                        b4 = Ball(200, 200, West, BLACK), mouseEventKind = MOUSE_CLICKED
 *      Expect: A new World  with balls keep their positions and colours, but each has a random ordinal direction.
 * @param w the current world containing four balls
 * @param mouseEventKind the type of mouse event (eg. LEFT_CLICK, RIGHT_CLICK...)
 * @return a new World with updated directions if clicked, otherwise no change
 */
World processMouseEvent(World w, MouseEventKind mouseEventKind) {
    if (mouseEventKind == MouseEventKind.LEFT_CLICK) {
        return new World(
            new Ball(w.b1().posX(), w.b1().posY(), randomOrdinal(), w.b1().colour()),
            new Ball(w.b2().posX(), w.b2().posY(), randomOrdinal(), w.b2().colour()),
            new Ball(w.b3().posX(), w.b3().posY(), randomOrdinal(), w.b3().colour()),
            new Ball(w.b4().posX(), w.b4().posY(), randomOrdinal(), w.b4().colour())
        );
    } else {
        return w;
    }
}

/**
 * Handle a full mouse event object.
 * Get the event kind from the given MouseEvent,
 * and pass them to processMouseEvent to determine if and how the world's state should change.
 * Example:
 *    - Given: World with four balls, which all moving North, mouseEvent = MouseEvent(MOUSE_CLICKED, "Left")
 *      Expect: Returns a new World where all balls keep their positions and colours,
 *              but each has a random ordinal direction.
 * @param w the current world containing four balls
 * @param mouseEvent the MouseEvent object
 * @return a new World with updated ball directions if clicked, otherwise no change
 */
World mouseEvent(World w, MouseEvent mouseEvent) {
    return processMouseEvent(w, mouseEvent.kind());
}

/**
 * Advance the world by one time step.
 * - If it is at a boundary, adjust its direction (corner bounce or edge bounce).
 * - Move the ball one step in its current direction.
 * Example:
 *    - Given: World with b1 = Ball(0, 0, NorthWest, BLUE),
 *                        b2 = Ball(299, 0, NorthEast, RED),
 *                        b3 = Ball(0, 499, SouthWest, GREEN),
 *                        b4 = Ball(299, 499, SouthEast, BLACK),
 *      Expect: A new World where each ball bounces off the corner and moves one step away from the wall.
 * @param w the current world containing four balls
 * @return a new World with all balls updated for the next step
 */
World step(World w) {
    return new World(
        moveBall(changeBallDirection(w.b1())),
        moveBall(changeBallDirection(w.b2())),
        moveBall(changeBallDirection(w.b3())),
        moveBall(changeBallDirection(w.b4()))
    );
}

/**
 * Draw the world by placing the ball at its current position.
 */
Image drawWorld(World w) {
    Image backgroundImage = Rectangle(WORLD_WIDTH, WORLD_HEIGHT, WHITE);
    Image ballImage1 = Circle(BALL_RADIUS, w.b1().colour());
    Image ballImage2 = Circle(BALL_RADIUS, w.b2().colour());
    Image ballImage3 = Circle(BALL_RADIUS, w.b3().colour());
    Image ballImage4 = Circle(BALL_RADIUS, w.b4().colour());
    Image ballOnTopOfBackground1 = PlaceXY(backgroundImage, ballImage1, w.b1().posX(), w.b1().posY());
    Image ballOnTopOfBackground2 = PlaceXY(ballOnTopOfBackground1, ballImage2, w.b2().posX(), w.b2().posY());
    Image ballOnTopOfBackground3 = PlaceXY(ballOnTopOfBackground2, ballImage3, w.b3().posX(), w.b3().posY());
    Image ballOnTopOfBackground4 = PlaceXY(ballOnTopOfBackground3, ballImage4, w.b4().posX(), w.b4().posY());
    return ballOnTopOfBackground4;
}

/**
 * Random direction
 */
Direction randomCardinal() {
    int random = RandomNumber(0, 4);
    return switch (random) {
        case 0 -> Direction.North;
        case 1 -> Direction.South;
        case 2 -> Direction.East;
        default -> Direction.West;
    };
}

Direction randomOrdinal() {
    int random = RandomNumber(0, 4);
    return switch (random) {
        case 0 -> Direction.NorthEast;
        case 1 -> Direction.NorthWest;
        case 2 -> Direction.SouthEast;
        default -> Direction.SouthWest;
    };
}

Direction randomAnyDirection() {
    int pick = RandomNumber(0, 8);
    return switch (pick) {
        case 0 -> Direction.North;
        case 1 -> Direction.South;
        case 2 -> Direction.East;
        case 3 -> Direction.West;
        case 4 -> Direction.NorthEast;
        case 5 -> Direction.NorthWest;
        case 6 -> Direction.SouthEast;
        default -> Direction.SouthWest;
    };
}

/** 
 * direction
 */
boolean isNorth(Direction d) {
    return Equals(d, Direction.North);
}

boolean isSouth(Direction d) {
    return Equals(d, Direction.South);
}

boolean isEast(Direction d) {
    return Equals(d, Direction.East);
}

boolean isWest(Direction d) {
    return Equals(d, Direction.West);
}

boolean isNorthEast(Direction d) {
    return Equals(d, Direction.NorthEast);
}

boolean isNorthWest(Direction d) {
    return Equals(d, Direction.NorthWest);
}

boolean isSouthEast(Direction d) {
    return Equals(d, Direction.SouthEast);
}

boolean isSouthWest(Direction d) {
    return Equals(d, Direction.SouthWest);
}

/** 
 * Return the initial state of your world.
 */
World getInitialState() {
    return new World(
        new Ball(INITIAL_BALL1_POSX, INITIAL_BALL1_POSY, randomAnyDirection(), BLUE),
        new Ball(INITIAL_BALL2_POSX, INITIAL_BALL2_POSY, randomAnyDirection(), RED),
        new Ball(INITIAL_BALL3_POSX, INITIAL_BALL3_POSY, randomAnyDirection(), GREEN),
        new Ball(INITIAL_BALL4_POSX, INITIAL_BALL4_POSY, randomAnyDirection(), BLACK)
    );
}

/** 
 * The coordinates of the center of a marble.
*/
int getX(Ball m) {
    return m.posX(); 
}

int getY(Ball m) {
    return m.posY();
}

/** 
 * The direction of a marble.
 */
Direction getDirection(Ball m) {
    return m.dir();
}

/** 
 * Return the marbles in the world.
 */
Ball getMarble1(World w) {
    return w.b1();
}

Ball getMarble2(World w) {
    return w.b2();
}

Ball getMarble3(World w) {
    return w.b3();
}

Ball getMarble4(World w) {
    return w.b4();
}

/**
 * The main entry point of the Bouncing Marbles program, which need these function:
 *   - Window title: "Bouncing Marbles"
 *   - Initial state: a World from getInitialState()
 *   - Drawing function: drawWorld()
 *   - Step function: step()
 *   - Key event: keyEvent()
 *   - Mouse event: mouseEvent()
 * Example:
 *   - Given: run the file
 *   - Expected: a window showing four coloured marbles bouncing in a white rectangle.
 *               Pressing the Space key changes all marbles' directions to random cardinal directions.
 *               Left-clicking changes all marbles' directions to random ordinal directions.
 */
void main() {
    BigBang("Bouncing Marbles", getInitialState(), this::drawWorld, this::step, this::keyEvent, this::mouseEvent);
}

void test() {

}


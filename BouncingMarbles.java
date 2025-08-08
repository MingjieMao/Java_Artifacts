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

/**
 * Represents the eight possible movement directions for a marble.
 * Directions:
 *   - Cardinal: North, South, East, West
 *   - Ordinal (diagonal): NorthEast, NorthWest, SouthEast, SouthWest
 */
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
 * Each Ball has:
 *   - An (x, y) position: the coordinates of the ball's center within the rectangular world.
 *   - A movement direction: one of the eight possible (cardinal or ordinal).
 *   - A colour: the visual colour of the ball.
 * Examples:
 *   - Ball(100, 150, Direction.North, Colour.BLUE)
 *     A blue ball located at (100, 150), moving upward.
 *   - Ball(250, 300, Direction.SouthWest, Colour.RED)
 *     A red ball located at (250, 300), moving diagonally down-left.
 * @param posX   The x-coordinate of the ball's center (in pixels).
 * @param posY   The y-coordinate of the ball's center (in pixels).
 * @param dir    The current movement direction of the ball.
 * @param colour The display colour of the ball.
 */
record Ball(int posX, int posY, Direction dir, Colour colour) {}

/**
 * Represents the complete state of the bouncing marbles world.
 * The World contains four balls, each with its own position, movement direction, and colour.
 * Examples:
 *   - World(new Ball(100, 150, Direction.North, Colour.BLUE),
 *         new Ball(200, 150, Direction.South, Colour.RED),
 *         new Ball(100, 250, Direction.East,  Colour.GREEN),
 *         new Ball(200, 250, Direction.West,  Colour.BLACK))
 *     A world where four balls in a square, each moving in a different cardinal direction.
 * @param b1 The first ball in the world.
 * @param b2 The second ball in the world.
 * @param b3 The third ball in the world.
 * @param b4 The fourth ball in the world.
 */
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
 * Bottom, Left, Right, TopLeft, TopRight, BottomLeft, and BottomRight corners follow similar logic above.
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
 * Example:
 *   - World w = new World(new Ball(100, 100, Direction.North, Colour.BLUE),
 *                         new Ball(200, 100, Direction.South, Colour.RED),
 *                         new Ball(100, 200, Direction.East,  Colour.GREEN),
 *                         new Ball(200, 200, Direction.West,  Colour.BLACK));
 *   - Image will display four marbles at the given coordinates on a white background.
 * @param w the current World containing four Balls
 * @return an Image showing all four marbles placed at their positions on a white background
 */
Image draw(World w) {
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
 * Random direction:
 * Generates a random cardinal direction (North, South, East, or West).
 * Example:
 *   - Direction dir = randomCardinal();
 *     dir could be North, South, East, or West, chosen at random
 * @return a random Direction from the four cardinal directions
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

/**
 * Random direction:
 * Generates a random ordinal direction (NorthEast, NorthWest, SouthEast, or SouthWest).
 * Example:
 *   - Direction dir = randomOrdinal();
 *     dir could be NorthEast, NorthWest, SouthEast, or SouthWest, chosen at random
 * @return a random Direction from the four ordinal directions
 */
Direction randomOrdinal() {
    int random = RandomNumber(0, 4);
    return switch (random) {
        case 0 -> Direction.NorthEast;
        case 1 -> Direction.NorthWest;
        case 2 -> Direction.SouthEast;
        default -> Direction.SouthWest;
    };
}

/**
 * Random direction:
 * Generates a random direction from all eight possible compass directions
 * (North, South, East, West, NorthEast, NorthWest, SouthEast, SouthWest).
 * Example:
 *   - Direction dir = randomAnyDirection();
 *     dir could be any of the eight directions, chosen at random
 * @return a random Direction from all eight compass directions
 */
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
 * Checks if the given direction is North.
 * The following other directions are same as this.
 * Example:
 *   - Given: Direction.North
 *     Expect: true
 *   - Given: Direction.South
 *     Expect: false
 * @param d the direction to check
 * @return true if d is North, false otherwise
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
 * Returns the initial state of the world.
 * Creates a new World object containing four balls, each with:
 *   - A predefined initial position (X, Y)
 *   - A random initial direction (8 possible directions)
 *   - A colour
 * Example:
 *   - World w = getInitialState();
 *     w.b1() might be at (100, 150), facing East, coloured BLUE
 *     w.b2() might be at (200, 150), facing SouthWest, coloured RED
 * @return a new World with four balls initialized
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
 * Example:
 *   Ball b = new Ball(120, 200, Direction.North, BLUE);
 *   getX(b), returns 120
 * getY is same as getX.
 * @param m the Ball whose X-coordinate is to be retrieved
 * @return the X-coordinate of the marble's center
 */
int getX(Ball m) {
    return m.posX(); 
}

int getY(Ball m) {
    return m.posY();
}

/** 
 * Gets the movement direction of a given marble.
 * Example:
 *   - Ball b = new Ball(120, 200, Direction.NorthEast, BLUE);
 *     getDirection(b), returns Direction.NorthEast
 * @param m the Ball whose direction is to be retrieved
 * @return the Direction the marble is currently moving in
 */
Direction getDirection(Ball m) {
    return m.dir();
}


/** 
 * Returns the first marble in the world.
 * Example:
 *   World w = new World(b1, b2, b3, b4);
 *   getMarble1(w), returns b1
 * @param w the World containing the marbles
 * @return the first Ball in the world
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
    BigBang("Bouncing Marbles", getInitialState(), this::draw, this::step, this::keyEvent, this::mouseEvent);
}

/**
 * Test the moveDirection function to ensure that each Direction maps to the correct (dx, dy) movement vector.
 * The Pair represents the x and y step values:
 *   -x: -1 = left, 0 = no movement, 1 = right
 *   -y: -1 = up,   0 = no movement, 1 = down
 */
void test_moveDirection() {
    testEqual(new Pair<Integer,Integer>(0,-1), moveDirection(Direction.North), "Should be North(0,-1);");
    testEqual(new Pair<Integer,Integer>(0,1),  moveDirection(Direction.South), "Should be South(0,1);");
    testEqual(new Pair<Integer,Integer>(1,0),  moveDirection(Direction.East),  "Should be East(1,0);");
    testEqual(new Pair<Integer,Integer>(-1,0), moveDirection(Direction.West),  "Should be West(-1,0);");

    testEqual(new Pair<Integer,Integer>(1,-1),  moveDirection(Direction.NorthEast), "Should be NorthEast(1,-1);");
    testEqual(new Pair<Integer,Integer>(-1,-1), moveDirection(Direction.NorthWest), "Should be NorthWest(-1,-1);");
    testEqual(new Pair<Integer,Integer>(1,1),   moveDirection(Direction.SouthEast), "Should be SouthEast(1,1);");
    testEqual(new Pair<Integer,Integer>(-1,1),  moveDirection(Direction.SouthWest), "Should be SouthWest(-1,1);");
}

/**
 * Test that moveBall() updates a ball’s position correctly based on its direction,
 * while keeping its colour and direction unchanged.
 * Example:
 *  - North: x should not change, y should decrease by 1
 *  - SouthEast: x should increase by 1, y should increase by 1
 */
void test_moveBall() {
    Ball b1 = new Ball(100, 200, Direction.North, BLUE);
    Ball a1 = moveBall(b1);
    testEqual(100, a1.posX(), "When moving North, x should stay 100, got 1;");
    testEqual(199, a1.posY(), "When moving North, y should be 199, got 1;");
    testEqual(Direction.North, a1.dir(), "Direction should remain North, got 1;");

    Ball b2 = new Ball(50, 50, Direction.SouthEast, RED);
    Ball a2 = moveBall(b2);
    testEqual(51, a2.posX(), "When moving SouthEast, x should be 51, got 1;");
    testEqual(51, a2.posY(), "When moving SouthEast, y should be 51, got 1;");
}

/**
 * Test that changeBallDirectionAtTop() correctly changes the direction when hitting the top boundary.
 *  - North -> South
 *  - NorthEast -> SouthEast
 *  - NorthWest -> SouthWest
 *  - East stays East
 */
void test_changeAtTop() {
    testEqual(Direction.South, changeBallDirectionAtTop(Direction.North), "At top edge, North should change to South, got 1;");
    testEqual(Direction.SouthEast, changeBallDirectionAtTop(Direction.NorthEast), "At top edge, NE should change to SE, got 1;");
    testEqual(Direction.SouthWest, changeBallDirectionAtTop(Direction.NorthWest), "At top edge, NW should change to SW, got 1;");
    testEqual(Direction.East, changeBallDirectionAtTop(Direction.East), "At top edge, East should remain East, got 1;");
}

/**
 * Test that changeBallDirectionAtBottom() correctly changes the direction
 * when hitting the bottom boundary.
 */
void test_changeAtBottom() {
    testEqual(Direction.North, changeBallDirectionAtBottom(Direction.South), "At bottom edge, South should change to North, got 1;");
    testEqual(Direction.NorthEast, changeBallDirectionAtBottom(Direction.SouthEast), "At bottom edge, SE should change to NE, got 1;");
    testEqual(Direction.NorthWest, changeBallDirectionAtBottom(Direction.SouthWest), "At bottom edge, SW should change to NW, got 1;");
    testEqual(Direction.West, changeBallDirectionAtBottom(Direction.West), "At bottom edge, West should remain West, got 1;");
}

/**
 * Test that changeBallDirectionAtLeft() correctly changes the direction
 * when hitting the left boundary.
 */
void test_changeAtLeft() {
    testEqual(Direction.East, changeBallDirectionAtLeft(Direction.West), "At left edge, West should change to East, got 1;");
    testEqual(Direction.NorthEast, changeBallDirectionAtLeft(Direction.NorthWest), "At left edge, NW should change to NE, got 1;");
    testEqual(Direction.SouthEast, changeBallDirectionAtLeft(Direction.SouthWest), "At left edge, SW should change to SE, got 1;");
    testEqual(Direction.North, changeBallDirectionAtLeft(Direction.North), "At left edge, North should remain North, got 1;");
}

/**
 * Test that changeBallDirectionAtRight() correctly changes the direction
 * when hitting the right boundary.
 */
void test_changeAtRight() {
    testEqual(Direction.West, changeBallDirectionAtRight(Direction.East), "At right edge, East should change to West, got 1;");
    testEqual(Direction.NorthWest, changeBallDirectionAtRight(Direction.NorthEast), "At right edge, NE should change to NW, got 1;");
    testEqual(Direction.SouthWest, changeBallDirectionAtRight(Direction.SouthEast), "At right edge, SE should change to SW, got 1;");
    testEqual(Direction.South, changeBallDirectionAtRight(Direction.South), "At right edge, South should remain South, got 1;");
}

/**
 * Test that changeBallDirection() changes direction at all four corners.
 */
void test_changeAtCorners() {
    Ball tl = new Ball(AtLeft, AtTop, Direction.NorthWest, BLUE);
    Ball tr = new Ball(AtRight, AtTop, Direction.NorthEast, RED);
    Ball bl = new Ball(AtLeft, AtBottom, Direction.SouthWest, GREEN);
    Ball br = new Ball(AtRight, AtBottom, Direction.SouthEast, BLACK);
    testEqual(false, Equals(changeBallDirection(tl).dir(), Direction.NorthWest), "At top-left, NW should change, got 1;");
    testEqual(false, Equals(changeBallDirection(tr).dir(), Direction.NorthEast), "At top-right, NE should change, got 1;");
    testEqual(false, Equals(changeBallDirection(bl).dir(), Direction.SouthWest), "At bottom-left, SW should change, got 1;");
    testEqual(false, Equals(changeBallDirection(br).dir(), Direction.SouthEast), "At bottom-right, SE should change, got 1;");
}

/**
 * Test that changeBallDirection() returns a new Ball object with the same position but possibly a new direction.
 */
void test_changeBallDirection_returnsNewBall() {
    Ball b = new Ball(AtTop, 150, Direction.North, BLUE);
    Ball c = changeBallDirection(b);
    testEqual(AtTop, c.posY(), "Y position should remain AtTop, got 1;");
    testEqual(150, c.posX(), "X position should remain 150, got 1;");
    testEqual(Direction.South, c.dir(), "At top edge, North should change to South, got 1;");
}

/**
 * Test that Ball moves one step when not bouncing.
 */
void test_step_oneBall_noBounce() {
    Ball b = new Ball(100, 100, Direction.East, BLUE);
    Ball after = step(b);
    testEqual(101, after.posX(), "Moving East, x should be +1, got 1;");
    testEqual(100, after.posY(), "Moving East, y should remain, got 1;");
    testEqual(Direction.East, after.dir(), "Direction should stay East, got 1;");
}

/**
 * Test that Ball bounces correctly from the top edge.
 */
void test_step_oneBall_bounceTop() {
    Ball b = new Ball(150, AtTop, Direction.North, BLUE);
    Ball after = step(b);
    testEqual(Direction.South, after.dir(), "After top bounce, direction should be South, got 1;");
    testEqual(150, after.posX(), "After bounce, x should remain 150, got 1;");
    testEqual(AtTop + 1, after.posY(), "After bounce, should move down 1, got 1;");
}

/**
 * Test that World updates positions of all balls.
 */
void test_step_world_updates() {
    World w = new World(
        new Ball(AtLeft, AtTop, Direction.NorthWest, BLUE),
        new Ball(AtRight, AtTop, Direction.NorthEast, RED),
        new Ball(AtLeft, AtBottom, Direction.SouthWest, GREEN),
        new Ball(AtRight, AtBottom, Direction.SouthEast, BLACK)
    );
    World a = step(w);
    testEqual(false, Equals(a.b1().posX(), w.b1().posX()) && Equals(a.b1().posY(), w.b1().posY()), 
              "b1 should move at least one step, got 1;");
    testEqual(false, Equals(a.b4().posX(), w.b4().posX()) && Equals(a.b4().posY(), w.b4().posY()), 
              "b4 should move at least one step, got 1;");
}

/**
 * Test that pressing the Space key sets all marbles’ directions to a cardinal direction.
 */
void test_keyEvent_space_sets_cardinals() {
    World w = getInitialState();
    World a = keyEvent(w, new KeyEvent(KeyEventKind.KEY_PRESSED, "Space"));
    boolean b1Card = isNorth(a.b1().dir()) || isSouth(a.b1().dir()) || isEast(a.b1().dir())  || isWest(a.b1().dir());
    boolean b4Card = isNorth(a.b4().dir()) || isSouth(a.b4().dir()) || isEast(a.b4().dir())  || isWest(a.b4().dir());
    testEqual(true, b1Card, "After Space, b1 should be cardinal, got 1;");
    testEqual(true, b4Card, "After Space, b4 should be cardinal, got 1;");
}

/**
 * Test that left mouse click sets all marbles’ directions to an ordinal direction.
 */
void test_mouseEvent_left_sets_ordinals() {
    World w = getInitialState();
    World a = mouseEvent(w, new MouseEvent(MouseEventKind.LEFT_CLICK, 0, 0));
    boolean b2Ord = isNorthEast(a.b2().dir()) || isNorthWest(a.b2().dir()) || isSouthEast(a.b2().dir()) || isSouthWest(a.b2().dir());
    boolean b3Ord = isNorthEast(a.b3().dir()) || isNorthWest(a.b3().dir()) || isSouthEast(a.b3().dir()) || isSouthWest(a.b3().dir());
    testEqual(true, b2Ord, "After left click, b2 should be ordinal, got 1;");
    testEqual(true, b3Ord, "After left click, b3 should be ordinal, got 1;");
}

/**
 * Test all get functions for marbles and ball properties.
 */
void test_get() {
    World w = getInitialState();
    testEqual(w.b1(), getMarble1(w), "getMarble1 returned wrong value, got 1;");
    testEqual(w.b2(), getMarble2(w), "getMarble2 returned wrong value, got 1;");
    testEqual(w.b3(), getMarble3(w), "getMarble3 returned wrong value, got 1;");
    testEqual(w.b4(), getMarble4(w), "getMarble4 returned wrong value, got 1;");
    Ball b = w.b1();
    testEqual(b.posX(), getX(b), "getX should return posX, got 1;");
    testEqual(b.posY(), getY(b), "getY should return posY, got 1;");
    testEqual(b.dir(), getDirection(b), "getDirection should return dir, got 1;");
}

/**
 * Test that drawWorld() never returns null.
 */
void test_draw_notNull() {
    Image img = drawWorld(getInitialState());
    testEqual(false, Equals(img, null), "drawWorld should not return null, got 1;");
}

/**
 * Basic test for getInitialState(): ensures marbles are inside correctly.
 */
void test_getInitialState_basic() {
    World w = getInitialState();
    testEqual(true, w.b1().posX() >= 0 && w.b1().posX() <= WORLD_WIDTH, "b1 x should be inside canvas, got 1;");
    testEqual(true, w.b1().posY() >= 0 && w.b1().posY() <= WORLD_HEIGHT, "b1 y should be inside canvas, got 1;");
    testEqual(true, Equals(w.b4().colour(), BLACK), "b4 colour should be BLACK, got 1;");
}

/** 
 * test entry point.
 */
void test() {
    runAsTest(this::test_moveDirection);
    runAsTest(this::test_moveBall);
    runAsTest(this::test_changeAtTop);
    runAsTest(this::test_changeAtBottom);
    runAsTest(this::test_changeAtLeft);
    runAsTest(this::test_changeAtRight);
    runAsTest(this::test_changeAtCorners);
    runAsTest(this::test_changeBallDirection_returnsNewBall);
    runAsTest(this::test_step_oneBall_noBounce);
    runAsTest(this::test_step_oneBall_bounceTop);
    runAsTest(this::test_step_world_updates);
    runAsTest(this::test_keyEvent_space_sets_cardinals);
    runAsTest(this::test_mouseEvent_left_sets_ordinals);
    runAsTest(this::test_get);
    runAsTest(this::test_draw_notNull);
    runAsTest(this::test_getInitialState_basic);
}
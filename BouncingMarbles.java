import comp1110.universe.*;
import static comp1110.universe.Colour.*;
import static comp1110.universe.Image.*;
import static comp1110.universe.Universe.*;
import static comp1110.lib.Functions.*;

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
        case North -> Pair(0,-1);
        case South -> Pair(0,1);
        case East -> Pair(1,0);
        case West -> Pair(-1,0);
        case NorthEast -> Pair(1,-1);
        case NorthWest -> Pair(-1,-1);
        case SouthEast -> Pair(1,1);
        case SouthWest -> Pair(-1,1);
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
    return new Ball(ball.posX+first(posXY), ball.posY+second(posXY), ball.dir, ball.colour);
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
        case North -> South;
        case NorthEast -> SouthEast;
        case NorthWest -> SouthWest;
        default -> dir;
    };
}

Direction changeBallDirectionAtBottom(Direction dir) {
    return switch (dir) {
        case South -> North;
        case SouthEast -> NorthEast;
        case SouthWest -> NorthWest;
        default -> dir;
    };
}

Direction changeBallDirectionAtLeft(Direction dir) {
    return switch (dir) {
        case West -> East;
        case NorthWest -> NorthEast;
        case SouthWest -> SouthEast;
        default -> dir;
    };
}

Direction changeBallDirectionAtRight(Direction dir) {
    return switch (dir) {
        case East -> West;
        case NorthEast -> NorthWest;
        case SouthEast -> SouthWest;
        default -> dir;
    };
}

Direction changeBallDirectionAtTopLeft(Direction dir) {
    return switch (dir) {
        case North -> South;
        case West -> East;
        case NorthEast -> SouthWest;
        case NorthWest -> SouthEast;
        case SouthEast -> SouthWest;
        default -> dir;
    };
}

Direction changeBallDirectionAtTopRight(Direction dir) {
    return switch (dir) {
        case North -> South;
        case East -> West;
        case NorthEast -> SouthWest;
        case NorthWest -> SouthWest;
        case SouthEast -> SouthWest;
        default -> dir;
    };
}

Direction changeBallDirectionAtBottomLeft(Direction dir) {
    return switch (dir) {
        case South -> North;
        case West -> East;
        case SouthEast -> NorthWest;
        case SouthWest -> NorthEast;
        case NorthWest -> SouthEast;
        default -> dir;
    };
}

Direction changeBallDirectionAtBottomRight(Direction dir) {
    return switch (dir) {
        case South -> North;
        case East -> West;
        case SouthEast -> NorthWest;
        case SouthWest -> NorthEast;
        case NorthEast -> SouthWest;
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
    return new Ball(ball.posX, ball.posY, ball.dir, ball.colour);
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
Ball step(Ball bl) {
    return moveBall(changeBallDirection(b));
} 

/**
 * Process a key event; if the spacebar is pressed, reverse direction.
 * @param b the current ball
 * @param keyEventKind the type of keyboard event (pressed, released, etc.)
 * @param key the key string (e.g. "Space", "A", "ArrowUp")
 * @return a new Ball with updated direction if SPACE is pressed, else unchanged
 */
Ball processKeyEvent(Ball b, KeyEventKind keyEventKind, String key)
{
  if (keyEventKind == KeyEventKind.KEY_PRESSED && Equals("Space",key)) {
      return changeBallDirection(b);
  } else {
      return b;
  }
}

/**
 * Handle a full key event object.
 */
Ball keyEvent(Ball b, KeyEvent keyEvent) {
    return processKeyEvent(b, keyEvent.kind(), keyEvent.key());
}

World step(World w) {
    return new World();
}

/**
 * Draw the world by placing the ball at its current position.
 */
Image drawWorld(Ball myBall) {
    Image backgroundImage = Rectangle(WORLD_WIDTH, WORLD_HEIGHT, WHITE);
    Image ballImage1 = Circle(BALL_RADIUS, BLUE);
    Image ballImage2 = Circle(BALL_RADIUS, RED);
    Image ballImage3 = Circle(BALL_RADIUS, GREEN);
    Image ballImage4 = Circle(BALL_RADIUS, BLACK);
    Image ballOnTopOfBackground = PlaceXY(backgroundImage, ballImage, INITIAL_BALL_POSX, myBall.posY());
    return ballOnTopOfBackground;
}

void main() {
    Ball ball = new Ball();
    BigBang("Bouncing Ball", ball, this::drawWorld, this::step, this::keyEvent);
}

void test() {

}


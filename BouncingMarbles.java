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

Pair<Integer, Integer> moveDirection(Direction dir) {
    return switch (dir) {
        case North -> Pair(0,-BALL_RADIUS);
        case South -> Pair(0,BALL_RADIUS);
        case East -> Pair(BALL_RADIUS,0);
        case West -> Pair(-BALL_RADIUS,0);
        case NorthEast -> Pair(BALL_RADIUS,-BALL_RADIUS);
        case NorthWest -> Pair(-BALL_RADIUS,-BALL_RADIUS);
        case SouthEast -> Pair(BALL_RADIUS,BALL_RADIUS);
        case SouthWestNorth -> Pair(-BALL_RADIUS,BALL_RADIUS);
    };
}

/**
 * Move the ball one step based on its current direction.
 */
Ball moveBall(Ball ball) {
    Pair<Integer,Integer> posXY = moveDirection(ball.dir);
    return new Ball(ball.posX+first(posXY), ball.posY+second(posXY), ball.dir, ball.colour);
}


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
 * Reverse the direction of the ball.
 */
Ball changeBallDirection(Ball ball) {
    if (AtTop && AtLeft) {
        newDir = changeBallDirectionAtTopLeft(ball.dir);
    } else if (AtTop && AtRight) {
        newDir = changeBallDirectionAtTopRight(ball.dir);
    } else if (AtBottom && AtLeft) {
        newDir = changeBallDirectionAtBottomLeft(ball.dir);
    } else if (AtBottom && AtRight) {
        newDir = changeBallDirectionAtBottomRight(ball.dir);
    } else if (AtTop) {
        newDir = changeBallDirectionAtTop(ball.dir);
    } else if (AtBottom) {
        newDir = changeBallDirectionAtBottom(ball.dir);
    } else if (AtLeft) {
        newDir = changeBallDirectionAtLeft(ball.dir);
    } else if (AtRight) {
        newDir = changeBallDirectionAtRight(ball.dir);
    }
    return new Ball(ball.posX, ball.posY, ball.dir, ball.colour);
}

/**
 * Update the ball's state for the next step, including bouncing at boundaries.
 */
Ball step(Ball myBall) {
    return moveBall(changeBallDirection(myBall));
} 

/**
 * Process a key event; if the spacebar is pressed, reverse direction.
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


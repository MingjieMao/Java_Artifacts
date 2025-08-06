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

/* Initial movement direction: true = downward, false = upward */
boolean BALL1_INITIAL_DIRECTION = ;

/* Speed of the ball's movement */
int BALL_SPEED = 1;

/* Bottom and Top boundary of the world */
int BOTTOM_MOST_POSY = WORLD_HEIGHT - BALL_RADIUS;
int TOP_MOST_POSY = BALL_RADIUS;

enum Direction {
    North,
    South,
    East,
    West,
    NorthEast,
    NorthWest,
    SouthEast,
    SouthWestNorth
}

/**
 * A ball represented by its X and Y position and its movement direction.
 */
record Ball(int posX, int posY, Direction dir) {}


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
    return new Ball(ball.posX+first(posXY), ball.posY+second(posXY), ball.dir);
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


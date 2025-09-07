In Parts 1 to 3
Model interstellar scavengers that find and trade artifacts recovered from the depths of space. All parts are to be completed in a single file called Artifacts.java.
There are 3 categories of artifacts and various types of scavengers, including rational agents and risk-taking agents, which we will model. Each scavenger differs in how they evaluate these artifacts and uses a decision-making process to determine whether a newly found artifact is more valuable than the one they currently possess. When analysing two artifacts, scavengers will classify the new artifact in relation to the owned one and get a result that can be either VALUABLE, MUNDANE, HAZARDOUS, INCOMPATIBLE, or UNKNOWN.

Part 1: Artifact Analysis
Each artifact falls into one of three categories:
A Star Chart: A complex navigational tool. It is defined by its destination (String), a calculated risk factor (int), and its origin point, defined by a sector and system (both ints).
An Energy Crystal: A simple power source, defined only by its power level (int).
A Inert Rock: An inert and mysterious object, defined only by its color (String).
Rational Scavenger
Design a function representing how a rational scavengers decides which artifact they prefer between an artifact they have and a new artifact. The analysis rules are as follows:
When comparing two Star Charts, if the new star chart has a lower risk factor than the star chart they own, then the new star chart is considered VALUABLE otherwise it is considered MUNDANE.
When comparing two Energy Crystals, if the new crystal has a higher power level than the crystal they own, the new crystal is considered VALUABLE; otherwise, it is MUNDANE.
When comparing two Inert Rocks, if they are the same color, the new rock is considered MUNDANE. If their colors are different, they are INCOMPATIBLE.
When comparing a Star Chart with an Energy Crystal, the result of the analysis depends on which one is the new artifact. If the new artifact is a Star Chart, the result is HAZARDOUS due to the risks of travel. If the new artifact is an Energy Crystal, the result is MUNDANE.
Any comparison involving an Inert Rock and a different type of artifact (a Star Chart or an Energy Crystal) yields an UNKNOWN result, as their value cannot be determined relative to one another.

Risk Taker Scavenger
You will design a function representing how a risk taking scavengers decides which artifact they prefer between an artifact they have and a new artifact. The analysis rules are as follows:
If the new artifact is a Star Chart, this new star chart is always considered VALUABLE regardless of the owned artifact.
When an owned Star Chart is compared to a new Energy Crystal or a new Inert Rock, the result is MUNDANE.
When comparing two Energy Crystals, if the new crystal has a higher power level than the crystal they own, the new crystal is considered VALUABLE; otherwise, it is MUNDANE.
When comparing two Inert Rocks, if they are the same color, the new rock is considered MUNDANE. If their colors are different, a coin flip determines the outcome: there’s a 50% chance the new rock is considered VALUABLE, and INCOMPATIBLE otherwise.
When comparing Energy Crystals and Inert Rock in any order yields an UNKNOWN result.

Part 2: The Scavenger Fleet
Galactic Scavengers are intrepid explorers. Each scavenger has a name, a single artifact in their cargo hold, and a unique personal analysis protocol for evaluating artifacts, which is a BiFunction. The functions rationalScavengerAnalysis and riskTakerScavengerAnalysis are examples of analysis protocols. We will model two distinct scenarios that use different analysis methods.

Scenario 1: Exploring an Asteroid
When a scavenger finds an artifact on an asteroid, the outcome is determined by an analysis based on their analysis protocol from Part 1 for all scavengers.

Scenario 2: Trading at a Starport
When two scavengers meet to trade, the outcome is determined by their analysis protocols and a trade occurs if and only if the trade is mutually beneficial.

Part 3: Simulating Encounters
Write a main function that simulates a Rational Scavenger’s encounter based on a single line of input from the console. The program will parse the line, simulate the correct event using your functions from Parts 1 and 2, and print a description of the artifact the scavenger possesses after the encounter. Assume that all other scavengers found in trading posts are Risk Taker Scavengers.

1. Syntax for describing an Artifact
This is the format representing an artifact. Your describeArtifact helper function must produce this format.
For a Star Chart: StarChart:destination;RISK=risk;SEC=sector;SYS=system
Example: "StarChart:Proxima Centauri;RISK=3;SEC=7;SYS=42"
For an Energy Crystal: EnergyCrystal:POWER=power
Example: "EnergyCrystal:POWER=900"
For an Inert Rock: InertRock:COLOR=color
Example: "InertRock:COLOR=obsidian"

2. Syntax for a Log Entry

This is the full line of input your main function must parse. This line contains the type of encounter and two strings describing the current owned artifact and the other artifact using the syntax described above. Note that the delimiter is a pipe character | surrounded by single spaces.
For an Asteroid Encounter: ASTEROID | [ownedArtifact] | [foundArtifact]
Example: ASTEROID | InertRock:COLOR=quartz | StarChart:Proxima Centauri;RISK=3;SEC=7;SYS=42
For a Trading Post Encounter: TRADING_POST | [ownedArtifact] | [otherScavengersArtifact]
Example: TRADING_POST | EnergyCrystal:POWER=500 | EnergyCrystal:POWER=900

Output Format
Since the log is written from the perspective of a Rational Scavenger, all asteroid encounters use the rationalScavengerAnalysis function from Part 1. Assume that all trades are done with Risk Taker Scavengers, that is, the otherScavengersArtifact is owned by a Risk Taker Scavenger. 
Format: FINAL CARGO: [final_artifact]
Example Output: FINAL CARGO: EnergyCrystal:POWER=900


Part 4
In a file called BouncingMarbles.java

Design a world program which simulates four different marbles moving around at a constant speed within a rectangular world 300 pixels wide and 500 pixels high. The world background color is white. All marbles are drawn as circles of radius 10 pixels. Each marble has its own colour different from each other’s colour. The marble colours are BLUE, RED, GREEN, and BLACK for the first, second, third, and fourth marbles, respectively.

At any world program step, every marble has a position and a direction it is heading towards. The position of the marble is defined as the position of the pin of the marble (i.e., the center of the marble) within the background rectangle. The direction of the marble can be one of 8 different possibilities: (1) North, (2) South, (3) East and (4) West (known as cardinal directions); (5) NorthEast, (6) NorthWest, (7) SouthEast and (8) SouthWest (known as ordinal directions). Marbles move one pixel per step for the cardinal directions, and one pixel per direction per step in the case of ordinal directions. Thus, for example, if the current direction of a marble is North, then it moves one pixel North per step, and if the current direction is SouthWest, then it moves one pixel South and one pixel West per step.

If right at the beginning of a given world program step, the program detects that a marble has reached the boundary of the background rectangle, then the marble first bounces, i.e., its direction changes, and then it moves in the new direction within the same step. Which direction it changes to is a function of (1) the current’s marble direction; and (2) the part of the boundary the marble has reached. We can split the boundary into 8 different parts: (1) top edge, (2) bottom edge, (3) left edge; (4) right edge; (5) top left corner; (6) top right corner; (7) bottom left corner, and (8) bottom right corner.

Thus, for example, if the marble reaches the top edge, we can distinguish among the following three scenarios:
Marble’s current direction is North; its direction should change to South.
Marble’s current direction is NorthEast; its direction should change to SouthEast.
Marble’s current direction is NorthWest; its direction should change SouthWest.
For any other current direction, the Marble’s direction should NOT change.

As another example, if the marble reaches the top right corner, we can distinguish among the following five scenarios:

Marble’s current direction is North; its direction should change to South.
Marble’s current direction is East; its direction should change to West.
Marble’s current direction is NorthEast; its direction should change to SouthWest.
Marble’s current direction is SouthEast; its direction should change to SouthWest.
Marble’s current direction is NorthWest; its direction should change to SouthWest.

For any other current direction, the Marble’s direction should NOT change.

For simplicity, the program should not handle in any way collisions among marbles. However, the user might be able to change the direction of the marbles with the mouse and keyboard. In particular, if the user hits the SPACE bar, the direction for every marble should randomly change to one of the cardinal directions. Besides, if the user left clicks the mouse, the direction for every marble should randomly change to one of the ordinal directions. Note that the position of the marble should not be altered as a consequence of these mouse and keyboard actions. Only their direction.

The initial position of (the centers of) the first, second, third and fourth marbles should be (X=75,Y=125), (X=225,Y=125), (X=75,Y=375), and (X=225,Y=375), respectively, and their initial direction randomly decided.

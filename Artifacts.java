import comp1110.lib.*;
import comp1110.lib.Date;
import static comp1110.lib.Functions.*;

import static comp1110.testing.Comp1110Unit.*;

// Part 1

// [R] is type for the analysis result
/**
 * Represents the result of comparing a new artifact with an owned artifact.
 */
enum Result {
    /** The newArtifact is more valuable */
    isValuable,
    /** The newArtifact is ordinary */
    isMundane,
    /** The newArtifact is dangerous */
    isHazardous,
    /** The newArtifact is incompatible with owned one */
    isIncompatible,
    /** Artifact types are incomparable */
    isUnknown
}

/**
 * A StarChart is a Artifact characterized by its destination, risk factor, 
 * and origin point (defined by sector and system).
 * Examples:
 * - A StarChart to "C" with risk 2 from sector 3, system 4
 * - A StarChart to "S" with risk 1 from sector 4, system 7
 * @param dest - destination of StarChart
 * @param risk - risk factor of StarChart (0 is safest)
 * @param sector - the sector of origin point about StarChart
 * @param system - the system of origin point about StarChart
 */
record StarChart(String dest, int risk, int sector, int system) implements Artifact {}

/**
 * Factory function to create a StarChart artifact.
 * Example:
 *   - makeStarChart("C", 2, 3, 4) 
 *     StarChart("C", 2, 3, 4)
 * @param dest   the destination of the StarChart
 * @param risk   the risk factor of the StarChart
 * @param sector the sector of the origin point of the StarChart
 * @param system the system of the origin point of the StarChart
 * @return a new StarChart
 */
StarChart makeStarChart(String dest, int risk, int sector, int system) {
    return new StarChart(dest, risk, sector, system);
}

/**
 * An EnergyCrystal is a Artifact characterized by its power level. 
 * Examples:
 * - A EnergyCrystal of power level 1
 * - A EnergyCrystal of power level 5
 * @param power - the power level of EnergyCrystal
 */
record EnergyCrystal(int power) implements Artifact {}

/**
 * Factory function to create an EnergyCrystal artifact.
 * Example:
 *   - makeEnergyCrystal(5) 
 *     EnergyCrystal(5)
 * @param power the power level of the EnergyCrystal 
 * @return a new EnergyCrystal instance
 */
EnergyCrystal makeEnergyCrystal(int power) {
    return new EnergyCrystal(power);
}

/**
 * An InertRock is a record representing an inert and mysterious object.
 * The value of rocks cannot be determined. 
 * Examples:
 * - A InertRock of color "blue"
 * - A InertRock of color "red"
 * @param color - the color of InertRock
 */
record InertRock(String color) implements Artifact {}

/**
 * Factory function to create an InertRock artifact.
 * Example:
 *   - makeInertRock("blue") 
 *     InertRock("blue")
 * @param color the color of the InertRock
 * @return a new InertRock
 */
InertRock makeInertRock(String color) {
    return new InertRock(color);
}

// [A] is type for an artifact.
/**
 * A Artifact represents a useful object and is one of:
 * - StarChart, representing a complex navigational tool
 * - EnergyCrystal, representing a simple power source
 * - InertRock, representing an inert and mysterious object
 */
sealed interface Artifact permits StarChart, EnergyCrystal, InertRock {}

/**
 * Rational Scavenger's comparative analysis:
 * Given ownedArtifact and newArtifact by a Rational Scavenger, 
 * through comparative analysis of the newArtifact and the ownedArtifact, 
 * returns the results, which can be valuable, hazardous, mundane, incompatible, or unknown.
 * Examples:
 *     - Given: ownedArtifact = StarChart("A", 8, 8, 8), newArtifact = StarChart("C", -1, 7, 9)
 *          Expect: isValuable
 *     - Given: ownedArtifact = StarChart("C", -2, 4, 8), newArtifact = StarChart("S", 7, -5, 9)
 *          Expect: isMundane
 *     - Given: ownedArtifact = StarChart("F", 0, 0, 0), newArtifact = StarChart("H", 0, 7, -3)
 *          Expect: isMundane
 *     - Given: ownedArtifact = EnergyCrystal(-1), newArtifact = EnergyCrystal(2)
 *          Expect: isValuable
 *     - Given: ownedArtifact = EnergyCrystal(4), newArtifact = EnergyCrystal(-2)
 *          Expect: isMundane
 *     - Given: ownedArtifact = EnergyCrystal(0), newArtifact = EnergyCrystal(0)
 *          Expect: isMundane
 *     - Given: ownedArtifact = InertRock("black"), newArtifact = InertRock("black")
 *          Expect: isMundane
 *     - Given: ownedArtifact = InertRock("red"), newArtifact = InertRock("blue")
 *          Expect: isIncompatible
 *     - Given: ownedArtifact = EnergyCrystal(-2), newArtifact = StarChart("A", -1, 4, -8)
 *          Expect: isHazardous
 *     - Given: ownedArtifact = StarChart("A", 2, -1, 9), newArtifact = EnergyCrystal(0)
 *          Expect: isMundane
 *     - Given: ownedArtifact = InertRock("yellow"), newArtifact = StarChart("G", -3, -1, 6)
 *          Expect: isUnknown
 *     - Given: ownedArtifact = InertRock("green"), newArtifact = EnergyCrystal(-3)
 *          Expect: isUnknown
 * Design strategy: template application
 * @param ownedArtifact the artifact already owned by Rational Scavenger.
 * @param newArtifact the new artifact Rational Scavenger got.
 * @return return the result through comparative analysis of the newArtifact and the ownedArtifact.
 */
Result rationalScavengerAnalysis(Artifact ownedArtifact, Artifact newArtifact) {
    return switch(ownedArtifact) {
        case StarChart(String dest1, int risk1, int sector1, int system1) -> compareOwnedStarChart(risk1, newArtifact);
        case EnergyCrystal(int power1) -> compareOwnedEnergyCrystal(power1, newArtifact);
        case InertRock(String color1) -> compareOwnedInertRock(color1, newArtifact);
    };
}

/** 
 * Rational Scavenger: When ownedArtifact is StarChart, analysis a newArtifact.
 * If the new artifact is a StarChart, compare their risks.
 * If the new artifact is an EnergyCrystal, return isMundane;
 * If the new artifact is an InertRock, return isUnknown.
 * Examples:
 *     - Given: ownedArtifact = StarChart("A", 8, 8, 8), newArtifact = StarChart("C", -1, 7, 9)
 *          Expect: isValuable
 *     - Given: ownedArtifact = StarChart("C", -2, 4, 8), newArtifact = StarChart("S", 7, -5, 9)
 *          Expect: isMundane
 *     - Given: ownedArtifact = StarChart("F", 0, 0, 0), newArtifact = StarChart("H", 0, 7, -3)
 *          Expect: isMundane
 *     - Given: ownedArtifact = StarChart("A", 2, -1, 9), newArtifact = EnergyCrystal(0)
 *          Expect: isMundane
 *     - Given: ownedArtifact = StarChart("G", -3, -1, 6), newArtifact = InertRock("yellow")
 *          Expect: isUnknown
 * Design strategy: template application
 * @param risk1 the risk factor of StarChart, which is Star Chart already owned by Rational Scavenger.
 * @param newArtifact the new artifact got by Rational Scavenger.
 * @return return the result through comparative analysis of the newArtifact and the ownedArtifact when ownedArtifact is StarChart.
 */
Result compareOwnedStarChart(int risk1, Artifact newArtifact) {
    return switch(newArtifact) {
        case StarChart(String dest2, int risk2, int sector2, int system2) -> compareTwoStarCharts(risk1, risk2);
        case EnergyCrystal(int power2) -> Result.isMundane;
        case InertRock(String color2) -> Result.isUnknown;
    };
}

/** 
 * When comparing two Star Charts, if the new star chart has a lower risk factor than the star chart they own, 
 * then the new star chart is considered VALUABLE otherwise it is considered MUNDANE.
 * Examples:
 *     - Given: ownedArtifact = StarChart("A", 8, 8, 8), newArtifact = StarChart("C", -1, 7, 9)
 *          Expect: isValuable
 *     - Given: ownedArtifact = StarChart("C", -2, 4, 8), newArtifact = StarChart("S", 7, -5, 9)
 *          Expect: isMundane
 *     - Given: ownedArtifact = StarChart("F", 0, 0, 0), newArtifact = StarChart("H", 0, 7, -3)
 *          Expect: isMundane
 * @param risk1 the risk factor of StarChart, which is Star Chart already owned by Rational Scavenger.
 * @param risk2 the risk factor of StarChart, which is the new Star Chart got by Rational Scavenger.
 * @return return the result through comparative analysis of of the newArtifact and the ownedArtifact when both Star Chart.
 */
Result compareTwoStarCharts(int risk1, int risk2) {
    if (risk1 > risk2) {
        return Result.isValuable;
    } else {
        return Result.isMundane;
    }
}

/** 
 * Rational Scavenger: When ownedArtifact is EnergyCrystal, analysis a newArtifact.
 * If the new artifact is a StarChart, return isHazardous;  
 * If the new artifact is an EnergyCrystal, compare their power level;
 * If the new artifact is an InertRock, return isUnknown.
 * Examples:
 *     - Given: ownedArtifact = EnergyCrystal(-1), newArtifact = EnergyCrystal(2)
 *          Expect: isValuable
 *     - Given: ownedArtifact = EnergyCrystal(4), newArtifact = EnergyCrystal(-2)
 *          Expect: isMundane
 *     - Given: ownedArtifact = EnergyCrystal(0), newArtifact = EnergyCrystal(0)
 *          Expect: isMundane
 *     - Given: ownedArtifact = EnergyCrystal(-2), newArtifact = StarChart("A", -1, 4, -8)
 *          Expect: isHazardous 
 *     - Given: ownedArtifact = EnergyCrystal(-3), newArtifact = InertRock("green")
 *          Expect: isUnknown
 * Design strategy: template application
 * @param power1 the power level of EnergyCrystal, which is Energy Crystal already owned by Rational Scavenger.
 * @param newArtifact the new artifact got by Rational Scavenger.
 * @return return the result through comparative analysis of the newArtifact and the ownedArtifact when ownedArtifact is EnergyCrystal.
 */
Result compareOwnedEnergyCrystal(int power1, Artifact newArtifact) {
    return switch(newArtifact) {
        case StarChart(String dest2, int risk2, int sector2, int system2) -> Result.isHazardous;
        case EnergyCrystal(int power2) -> compareTwoEnergyCrystals(power1, power2);
        case InertRock(String color2) -> Result.isUnknown;
    };
}

/** 
 * When comparing two Energy Crystals, if the new crystal has a higher power level than the crystal they own, 
 * the new crystal is considered VALUABLE; otherwise, it is MUNDANE.
 * Examples:
 *     - Given: ownedArtifact = EnergyCrystal(-1), newArtifact = EnergyCrystal(2)
 *          Expect: isValuable
 *     - Given: ownedArtifact = EnergyCrystal(4), newArtifact = EnergyCrystal(-2)
 *          Expect: isMundane
 *     - Given: ownedArtifact = EnergyCrystal(0), newArtifact = EnergyCrystal(0)
 *          Expect: isMundane
 * @param power1 the power level of EnergyCrystal, which is Energy Crystal already owned by Rational Scavenger.
 * @param power2 the power level of EnergyCrystal, which is the new Energy Crystal got by Rational Scavenger.
 * @return return the result through comparative analysis of the newArtifact and the ownedArtifact when both EnergyCrystal.
 */
Result compareTwoEnergyCrystals(int power1, int power2) {
    if (power1 < power2) {
        return Result.isValuable;
    } else {
        return Result.isMundane;
    }
}

/** 
 * Rational Scavenger: When ownedArtifact is InertRock, analysis a newArtifact.
 * If the new artifact is a StarChart, return isUnknown;  
 * If the new artifact is an EnergyCrystal, return isUnknown; 
 * If the new artifact is an InertRock, compare their color.
 * Examples:
 *     - Given: ownedArtifact = InertRock("black"), newArtifact = InertRock("black")
 *          Expect: isMundane
 *     - Given: ownedArtifact = InertRock("red"), newArtifact = InertRock("blue")
 *          Expect: isIncompatible
 *     - Given: ownedArtifact = InertRock("yellow"), newArtifact = StarChart("G", -3, -1, 6)
 *          Expect: isUnknown
 *     - Given: ownedArtifact = InertRock("green"), newArtifact = EnergyCrystal(-3)
 *          Expect: isUnknown
 * Design strategy: template application
 * @param color1 the color of InertRock, which is the Inert Rock already owned by Rational Scavenger.
 * @param newArtifact the new artifact got by Rational Scavenger.
 * @return return the result through comparative analysis of the newArtifact and the ownedArtifact when ownedArtifact is InertRock.
 */
Result compareOwnedInertRock(String color1, Artifact newArtifact) {
    return switch(newArtifact) {
        case StarChart(String dest2, int risk2, int sector2, int system2) -> Result.isUnknown;
        case EnergyCrystal(int power2) -> Result.isUnknown;
        case InertRock(String color2) -> compareTwoInertRocksRationalScavenger(color1, color2);
    };
}

/** 
 * When comparing two Inert Rocks, if they are the same color, the new rock is considered MUNDANE. 
 * If their colors are different, they are INCOMPATIBLE.
 * Examples:
 *     - Given: ownedArtifact = InertRock("black"), newArtifact = InertRock("black")
 *          Expect: isMundane
 *     - Given: ownedArtifact = InertRock("red"), newArtifact = InertRock("blue")
 *          Expect: isIncompatible
 * @param color1 the color of InertRock, which is the Inert Rock already owned by Rational Scavenger.
 * @param color2 the color of InertRock, which is the new Inert Rock got by Rational Scavenger.
 * @return return the result through Rational Scavenger's comparative analysis of the newArtifact and the ownedArtifact when both InertRock.
 */
Result compareTwoInertRocksRationalScavenger(String color1, String color2) {
    if (Equals(color1, color2)) {
        return Result.isMundane;
    } else {
        return Result.isIncompatible;
    }
}


/**
 * Risk Taker Scavenger's comparative analysis
 * Given ownedArtifact and newArtifact by a Risk Taker Scavenger, 
 * through comparative analysis of the newArtifact and the ownedArtifact, 
 * returns the results, which can be valuable, hazardous, mundane, incompatible, or unknown.
 * Examples:
 *     - Given: ownedArtifact = StarChart("D", 2, -8, 8), newArtifact = StarChart("B", -4, 7, -9)
 *          Expect: isValuable
 *     - Given: ownedArtifact = EnergyCrystal(-2), newArtifact = StarChart("B", 0, 7, 9)
 *          Expect: isValuable
 *     - Given: ownedArtifact = InertRock("black"), newArtifact = StarChart("B", 9, 4, 5)
 *          Expect: isValuable
 *     - Given: ownedArtifact = StarChart("G", -4, 8, 34), newArtifact = EnergyCrystal(2)
 *          Expect: isMundane
 *     - Given: ownedArtifact = StarChart("A", 0, -3, -2), newArtifact = InertRock("yellow")
 *          Expect: isMundane
 *     - Given: ownedArtifact = EnergyCrystal(-1), newArtifact = EnergyCrystal(2)
 *          Expect: isValuable
 *     - Given: ownedArtifact = EnergyCrystal(3), newArtifact = EnergyCrystal(-6)
 *          Expect: isMundane
 *     - Given: ownedArtifact = EnergyCrystal(0), newArtifact = EnergyCrystal(0)
 *          Expect: isMundane
 *     - Given: ownedArtifact = InertRock("blue"), newArtifact = InertRock("blue")
 *          Expect: isMundane
 *     - Given: ownedArtifact = InertRock("red"), newArtifact = InertRock("blue")
 *          Expect: isValuable or isIncompatible
 *     - Given: ownedArtifact = InertRock("black"), newArtifact = InertRock("blue")
 *          Expect: isValuable or isIncompatible
 *     - Given: ownedArtifact = InertRock("green"), newArtifact = EnergyCrystal(0)
 *          Expect: isUnknown
 * Design strategy: template application
 * @param ownedArtifact the artifact already owned by Risk Taker Scavenger.
 * @param newArtifact the new artifact Risk Taker Scavenger got.
 * @return return the result through comparative analysis of the newArtifact and the ownedArtifact.
 */
Result riskTakerScavengerAnalysis(Artifact ownedArtifact, Artifact newArtifact) {
    return switch(newArtifact) {
        case StarChart(String dest2, int risk2, int sector2, int system2) -> Result.isValuable;
        case EnergyCrystal(int power2) -> compareNewEnergyCrystal(ownedArtifact, power2);
        case InertRock(String color2) -> compareNewInertRock(ownedArtifact, color2);
    };
}


/** 
 * Risk Taker Scavenger: When newArtifact is EnergyCrystal, analysis the ownedArtifact.
 * newArtifact is EnergyCrystal and ownedArtifact is StarChart: return isMundane;
 * newArtifact is EnergyCrystal and ownedArtifact is EnergyCrystal: comparing two Energy Crystals, 
 * if the new crystal has a higher power level than the crystal they own, return isValuable; otherwise, return isMundane.
 * newArtifact is EnergyCrystal and ownedArtifact is InertRock: return isUnknown.
 * Examples:
 *     - Given: ownedArtifact = StarChart("G", -4, 8, 34), newArtifact = EnergyCrystal(2)
 *          Expect: isMundane
 *     - Given: ownedArtifact = EnergyCrystal(-1), newArtifact = EnergyCrystal(2)
 *          Expect: isValuable
 *     - Given: ownedArtifact = EnergyCrystal(3), newArtifact = EnergyCrystal(-6)
 *          Expect: isMundane
 *     - Given: ownedArtifact = EnergyCrystal(0), newArtifact = EnergyCrystal(0)
 *          Expect: isMundane
 *     - Given: ownedArtifact = InertRock("green"), newArtifact = new EnergyCrystal(0)
 *          Expect: isUnknown
 * Design strategy: template application
 * @param ownedArtifact the artifact already owned by Risk Taker Scavenger.
 * @param power2 the power level of EnergyCrystal, which is new Energy Crystal got by Risk Taker Scavenger.
 * @return return the result through comparative analysis of the newArtifact and the ownedArtifact when newArtifact is EnergyCrystal.
 */
Result compareNewEnergyCrystal(Artifact ownedArtifact, int power2) {
    return switch(ownedArtifact) {
        case StarChart(String dest1, int risk1, int sector1, int system1) -> Result.isMundane;
        case EnergyCrystal(int power1) -> compareTwoEnergyCrystals(power1, power2);
        case InertRock(String color1) -> Result.isUnknown;
    };
}

/** 
 * Risk Taker Scavenger: When newArtifact is InertRock, analysis the ownedArtifact.
 * newArtifact is InertRock and ownedArtifact is StarChart: return isMundane;
 * newArtifact is InertRock and ownedArtifact is EnergyCrystal: return isUnknown.
 * newArtifact is InertRock and ownedArtifact is InertRock: comparing two Inert Rocks.
 * Examples:
 *     - Given: ownedArtifact = StarChart("A", 0, -3, -2), newArtifact = InertRock("yellow")
 *          Expect: isMundane
 *.    - Given: ownedArtifact = InertRock("blue"), newArtifact = InertRock("blue")
 *          Expect: isMundane
 *     - Given: ownedArtifact = InertRock("red"), newArtifact = InertRock("blue")
 *          Expect: isValuable or isIncompatible
 *     - Given: ownedArtifact = InertRock("black"), newArtifact = InertRock("blue")
 *          Expect: isValuable or isIncompatible
 *     - Given: ownedArtifact = EnergyCrystal(0), newArtifact = new InertRock("green") 
 *          Expect: isUnknown
 * Design strategy: template application
 * @param ownedArtifact the artifact already owned by Risk Taker Scavenger.
 * @param color2 the color of InertRock, which is new Inert Rock got by Risk Taker Scavenger.
 * @return return the result through comparative analysis of the newArtifact and the ownedArtifact when newArtifact is InertRock.
 */
Result compareNewInertRock(Artifact ownedArtifact, String color2) {
    return switch(ownedArtifact) {
        case StarChart(String dest1, int risk1, int sector1, int system1) -> Result.isMundane;
        case EnergyCrystal(int power1) -> Result.isUnknown;
        case InertRock(String color1) -> compareTwoInertRocksRiskTakerScavenger(color1, color2);
    };
}

/** 
 * Risk Taker Scavenger: comparing two Inert Rocks. 
 * If they are the same color, return isMundan; 
 * otherwise, a coin flip determines the outcome: there’s a 50% chance the new rock is considered valuable, and incompatible otherwise.
 * Examples:
 *     - Given: ownedArtifact = InertRock("blue"), newArtifact = InertRock("blue")
 *          Expect: isMundane
 *     - Given: ownedArtifact = InertRock("red"), newArtifact = InertRock("blue")
 *          Expect: isValuable or isIncompatible
 *     - Given: ownedArtifact = InertRock("black"), newArtifact = InertRock("blue")
 *          Expect: isValuable or isIncompatible
 * @param color1 the color of InertRock, which is the Inert Rock already owned by Risk Taker Scavenger.
 * @param color2 the color of InertRock, which is new Inert Rock got by Risk Taker Scavenger.
 * @return return the result through comparative analysis of comparing two Inert Rocks.
 */
Result compareTwoInertRocksRiskTakerScavenger(String color1, String color2) {
    if (Equals(color1, color2)) {
        return Result.isMundane;
    } else {
        return coinFlip();
    }
}

/** 
 * When the colors of two Inert Rocks are different, Risk Taker Scavenger use coin flip determines the outcome: 
 * there’s a 50% chance the new rock is considered valuable, and incompatible otherwise.
 * @return return isValuable or isIncompatible in random.
 */
Result coinFlip() {
    if (Equals(RandomNumber(0, 2), 0)) {
        return Result.isValuable;
    } else {
        return Result.isIncompatible;
    }
}

boolean isValuable(Result result) {
    return result == Result.isValuable;
}

boolean isMundane(Result result) {
    return result == Result.isMundane;
}

boolean isHazardous(Result result) {
    return result == Result.isHazardous;
}

boolean isIncompatible(Result result) {
    return result == Result.isIncompatible;
}

boolean isUnknown(Result result) {
    return result == Result.isUnknown;
}

void testRationalScavenger() {
    println(rationalScavengerAnalysis(new StarChart("A", 8, 8, 8), new StarChart("C", -1, 7, 9)));  // Expected: VALUABLE
    println(rationalScavengerAnalysis(new StarChart("C", -2, 4, 8), new StarChart("S", 7, -5, 9))); // Expected: MUNDANE
    println(rationalScavengerAnalysis(new StarChart("F", 0, 0, 0), new StarChart("H", 0, 7, -3)));  // Expected: MUNDANE
    println(rationalScavengerAnalysis(new EnergyCrystal(-1), new EnergyCrystal(2)));                // Expected: VALUABLE
    println(rationalScavengerAnalysis(new EnergyCrystal(4), new EnergyCrystal(-2)));               // Expected: MUNDANE
    println(rationalScavengerAnalysis(new EnergyCrystal(0), new EnergyCrystal(0)));                // Expected: MUNDANE
    println(rationalScavengerAnalysis(new InertRock("black"), new InertRock("black")));           // Expected: MUNDANE
    println(rationalScavengerAnalysis(new InertRock("red"), new InertRock("blue")));              // Expected: INCOMPATIBLE
    println(rationalScavengerAnalysis(new EnergyCrystal(-2), new StarChart("A", -1, 4, -8)));      // Expected: HAZARDOUS
    println(rationalScavengerAnalysis(new StarChart("A", 2, -1, 9), new EnergyCrystal(0)));        // Expected: MUNDANE
    println(rationalScavengerAnalysis(new InertRock("yellow"), new StarChart("G", -3, -1, 6)));    // Expected: UNKNOWN
    println(rationalScavengerAnalysis(new InertRock("green"), new EnergyCrystal(-3)));            // Expected: UNKNOWN
}

void testRiskTakerScavenger() {
    println(riskTakerScavengerAnalysis(new StarChart("D", 2, -8, 8), new StarChart("B", -4, 7, -9)));
    println(riskTakerScavengerAnalysis(new EnergyCrystal(-2), new StarChart("B", 0, 7, 9)));
    println(riskTakerScavengerAnalysis(new InertRock("black"), new StarChart("B", 9, 4, 5)));
    println(riskTakerScavengerAnalysis(new StarChart("G", -4, 8, 34), new EnergyCrystal(2)));
    println(riskTakerScavengerAnalysis(new StarChart("A", 0, -3, -2), new InertRock("yellow")));
    println(riskTakerScavengerAnalysis(new EnergyCrystal(-1), new EnergyCrystal(2)));
    println(riskTakerScavengerAnalysis(new EnergyCrystal(3), new EnergyCrystal(-6)));
    println(riskTakerScavengerAnalysis(new EnergyCrystal(0), new EnergyCrystal(0)));
    println(riskTakerScavengerAnalysis(new InertRock("blue"), new InertRock("blue")));
    println(riskTakerScavengerAnalysis(new InertRock("red"), new InertRock("blue")));
    println(riskTakerScavengerAnalysis(new InertRock("black"), new InertRock("blue")));
    println(riskTakerScavengerAnalysis(new InertRock("green"), new EnergyCrystal(0)));
}

// Part 1 - Rational Scavenger tests
void testRationalAnalysis_TwoStarChart1() {
    Artifact ownedArtifact = new StarChart("A", 8, 8, 8);
    Artifact newArtifact = new StarChart("C", -1, 7, 9);
    Result result = rationalScavengerAnalysis(ownedArtifact, newArtifact);
    testEqual(true, isValuable(result), "RationalScavenger: Lower risk factor of new StarChart should be valuable to owned StarChart.");
}

void testRationalAnalysis_TwoStarChart2() {
    Artifact ownedArtifact = new StarChart("C", -2, 4, 8);
    Artifact newArtifact = new StarChart("S", 7, -5, 9);
    Result result = rationalScavengerAnalysis(ownedArtifact, newArtifact);
    testEqual(true, isMundane(result), "RationalScavenger: Higher risk factor of new StarChart should be mundane with owned StarChart.");
}

void testRationalAnalysis_TwoStarChart3() {
    Artifact ownedArtifact = new StarChart("F", 0, 0, 0);
    Artifact newArtifact = new StarChart("H", 0, 7, -3);
    Result result = rationalScavengerAnalysis(ownedArtifact, newArtifact);
    testEqual(true, isMundane(result), "RationalScavenger: Same risk factor of new StarChart should be mundane with owned StarChart.");
}

void testRationalAnalysis_TwoEnergyCrystal1() {
    Artifact ownedArtifact = new EnergyCrystal(-1);
    Artifact newArtifact = new EnergyCrystal(2);
    Result result = rationalScavengerAnalysis(ownedArtifact, newArtifact);
    testEqual(true, isValuable(result), "RationalScavenger: EnergyCrystal(2) should be more valuable than EnergyCrystal(-1).");
}

void testRationalAnalysis_TwoEnergyCrystal2() {
    Artifact ownedArtifact = new EnergyCrystal(4);
    Artifact newArtifact = new EnergyCrystal(-2);
    Result result = rationalScavengerAnalysis(ownedArtifact, newArtifact);
    testEqual(true, isMundane(result), "RationalScavenger: EnergyCrystal(-2) should be mundane with EnergyCrystal(4).");
}

void testRationalAnalysis_TwoEnergyCrystal3() {
    Artifact ownedArtifact = new EnergyCrystal(0);
    Artifact newArtifact = new EnergyCrystal(0);
    Result result = rationalScavengerAnalysis(ownedArtifact, newArtifact);
    testEqual(true, isMundane(result), "RationalScavenger: EnergyCrystal(0) should be mundane with EnergyCrystal(0).");
}

void testRationalAnalysis_TwoInertRocks1() {
    Artifact ownedArtifact = new InertRock("black");
    Artifact newArtifact = new InertRock("black");
    Result result = rationalScavengerAnalysis(ownedArtifact, newArtifact);
    testEqual(true, isMundane(result), "RationalScavenger: Same colors of two InertRocks should be mundane.");
}

void testRationalAnalysis_TwoInertRocks2() {
    Artifact ownedArtifact = new InertRock("red");
    Artifact newArtifact = new InertRock("blue");
    Result result = rationalScavengerAnalysis(ownedArtifact, newArtifact);
    testEqual(true, isIncompatible(result), "RationalScavenger: Different colors of two InertRocks should be incompatible.");
}

void testRationalAnalysis_EnergyCrystalStarChart() {
    Artifact ownedArtifact = new EnergyCrystal(-2);
    Artifact newArtifact = new StarChart("A", -1, 4, -8);
    Result result = rationalScavengerAnalysis(ownedArtifact, newArtifact);
    testEqual(true, isHazardous(result), "RationalScavenger: NewArtifact is StarChart, result should be hazardous.");
}

void testRationalAnalysis_StarChartEnergyCrystal() {
    Artifact ownedArtifact = new StarChart("A", 2, -1, 9);
    Artifact newArtifact = new EnergyCrystal(0);
    Result result = rationalScavengerAnalysis(ownedArtifact, newArtifact);
    testEqual(true, isMundane(result), "RationalScavenger: NewArtifact is EnergyCrystal, result should be mundane.");
}

void testRationalAnalysis_InertRockStarChart() {
    Artifact ownedArtifact = new InertRock("yellow");
    Artifact newArtifact = new StarChart("G", -3, -1, 6);
    Result result = rationalScavengerAnalysis(ownedArtifact, newArtifact);
    testEqual(true, isUnknown(result), "RationalScavenger: InertRock with different type should be unknown.");
}

void testRationalAnalysis_InertRockEnergyCrystal() {
    Artifact ownedArtifact = new InertRock("green");
    Artifact newArtifact = new EnergyCrystal(-3);
    Result result = rationalScavengerAnalysis(ownedArtifact, newArtifact);
    testEqual(true, isUnknown(result), "RationalScavenger: InertRock with different type should be unknown.");
}

// Part 1 - riskTakerScavengerAnalysis tests
void testRiskTaker_NewStarChart1() {
    Artifact owned = new StarChart("D", 2, -8, 8);
    Artifact found = new StarChart("B", -4, 7, -9);
    Result result = riskTakerScavengerAnalysis(owned, found);
    testEqual(true, isValuable(result), "RiskTaker: any new StarChart should be valuable.");
}
void testRiskTaker_NewStarChart2() {
    Artifact owned = new EnergyCrystal(-2);
    Artifact found = new StarChart("B", 0, 7, 9);
    Result result = riskTakerScavengerAnalysis(owned, found);
    testEqual(true, isValuable(result), "RiskTaker: any new StarChart should be valuable.");
}
void testRiskTaker_NewStarChart3() {
    Artifact owned = new InertRock("black");
    Artifact found = new StarChart("B", 9, 4, 5);
    Result result = riskTakerScavengerAnalysis(owned, found);
    testEqual(true, isValuable(result), "RiskTaker: any new StarChart should be valuable.");
}

void testRiskTaker_StarChartEnergyCrystal() {
    Artifact owned = new StarChart("G", -4, 8, 34);
    Artifact found = new EnergyCrystal(2);
    Result result = riskTakerScavengerAnalysis(owned, found);
    testEqual(true, isMundane(result), "RiskTaker: StarChart vs EnergyCrystal should be mundane.");
}

void testRiskTaker_StarChartInertRock() {
    Artifact owned = new StarChart("A", 0, -3, -2);
    Artifact found = new InertRock("yellow");
    Result result = riskTakerScavengerAnalysis(owned, found);
    testEqual(true, isMundane(result), "RiskTaker: StarChart vs InertRock should be mundane.");
}

void testRiskTaker_TwoEnergyCrystal1() {
    Artifact owned = new EnergyCrystal(-1);
    Artifact found = new EnergyCrystal(2);
    Result result = riskTakerScavengerAnalysis(owned, found);
    testEqual(true, isValuable(result), "RiskTaker: new EnergyCrystal has more power.");
}

void testRiskTaker_TwoEnergyCrystal2() {
    Artifact owned = new EnergyCrystal(3);
    Artifact found = new EnergyCrystal(-6);
    Result result = riskTakerScavengerAnalysis(owned, found);
    testEqual(true, isMundane(result), "RiskTaker: new EnergyCrystal has less power.");
}

void testRiskTaker_TwoEnergyCrystal3() {
    Artifact owned = new EnergyCrystal(0);
    Artifact found = new EnergyCrystal(0);
    Result result = riskTakerScavengerAnalysis(owned, found);
    testEqual(true, isMundane(result), "RiskTaker: same power EnergyCrystals are mundane.");
}

void testRiskTaker_TwoInertRocks1() {
    Artifact owned = new InertRock("blue");
    Artifact found = new InertRock("blue");
    Result result = riskTakerScavengerAnalysis(owned, found);
    testEqual(true, isMundane(result), "RiskTaker: same color InertRock is mundane.");
}

void testRiskTaker_TwoInertRocks2() {
    Artifact owned = new InertRock("red");
    Artifact found = new InertRock("blue");
    Result result = riskTakerScavengerAnalysis(owned, found);
    testEqual(true, isValuable(result) || isIncompatible(result), 
    "RiskTaker: different color InertRock should be 50% valuable or incompatible.");
}

void testRiskTaker_InertRockEnergyCrystal() {
    Artifact owned = new InertRock("green");
    Artifact found = new EnergyCrystal(0);
    Result result = riskTakerScavengerAnalysis(owned, found);
    testEqual(true, isUnknown(result), "RiskTaker: EnergyCrystal vs InertRock should be unknown.");
}

void test() {
    // Part 1 - rationalScavengerAnalysis tests
    runAsTest(this::testRationalAnalysis_TwoStarChart1);
    runAsTest(this::testRationalAnalysis_TwoStarChart2);
    runAsTest(this::testRationalAnalysis_TwoStarChart3);
    runAsTest(this::testRationalAnalysis_TwoEnergyCrystal1);
    runAsTest(this::testRationalAnalysis_TwoEnergyCrystal2);
    runAsTest(this::testRationalAnalysis_TwoEnergyCrystal3);
    runAsTest(this::testRationalAnalysis_TwoInertRocks1);
    runAsTest(this::testRationalAnalysis_TwoInertRocks2);
    runAsTest(this::testRationalAnalysis_EnergyCrystalStarChart);
    runAsTest(this::testRationalAnalysis_StarChartEnergyCrystal);
    runAsTest(this::testRationalAnalysis_InertRockStarChart);
    runAsTest(this::testRationalAnalysis_InertRockEnergyCrystal);

    // Part 1 - riskTakerScavengerAnalysis tests
    runAsTest(this::testRiskTaker_NewStarChart1);
    runAsTest(this::testRiskTaker_NewStarChart2);
    runAsTest(this::testRiskTaker_NewStarChart3);
    runAsTest(this::testRiskTaker_StarChartEnergyCrystal);
    runAsTest(this::testRiskTaker_StarChartInertRock);
    runAsTest(this::testRiskTaker_TwoEnergyCrystal1);
    runAsTest(this::testRiskTaker_TwoEnergyCrystal2);
    runAsTest(this::testRiskTaker_TwoEnergyCrystal3);
    runAsTest(this::testRiskTaker_TwoInertRocks1);
    runAsTest(this::testRiskTaker_TwoInertRocks2);
    runAsTest(this::testRiskTaker_InertRockEnergyCrystal);

    // Part 2 - exploreAsteroid tests
    runAsTest(this::testDifferentStrategies_withTestEqual);

    // Part 2 - tradeAtStarport tests
    runAsTest(this::testTradeAtStarport_withTestEqual);

    // Part 3
    runAsTest(this::testParseRationalScavengerLog);
}

// Part 2: The Scavenger Fleet
// Scenario 1: Exploring an Asteroid

/**
 * Represents a scavenger, and each scavenger has:
 * - A unique name,
 * - A single artifact in their cargo hold,
 * - A unique personal analysis protocol for evaluating artifacts, which is a BiFunction.
 * Examples:
 * Scavenger s1 = new Scavenger("Jessica", new StarChart("Sydney", 1, 4, 7), Artifacts::rationalScavengerAnalysis);
 * Scavenger s2 = new Scavenger("Alex", new EnergyCrystal(5), Artifacts::riskTakerScavengerAnalysis);
 * Scavenger s3 = new Scavenger("Luna", new InertRock("blue"), Artifacts::rationalScavengerAnalysis);
 * @param name The unique name of the scavenger
 * @param analysisFunc The scavenger's personal evaluation protocol implemented as a BiFunction:
 *                     - First Artifact: the ownedArtifact
 *                     - Second Artifact: the new foundArtifact
 *                     - Returns: a Result indicating the evaluation outcome
 * @param cargo The single artifact in the scavenger's cargo hold
 */
record Scavenger(String name, BiFunction<Artifact, Artifact, Result> analysisFunc, Artifact cargo) {}

Scavenger makeScavenger(String name, BiFunction<Artifact,Artifact,Result> analysisFunc, Artifact cargo) {
    return new Scavenger(name, analysisFunc, cargo);
}

/**
 * Returns the artifact currently held by the given scavenger.
 * Examples:
 *     - Given: scavenger = RationalScavenger("Alice", EnergyCrystal(100))  
 *       Expect: EnergyCrystal(100)
 *     - Given: scavenger = RiskTakerScavenger("Bob", InertRock("red"))  
 *       Expect: InertRock(red)
 *     - Given: scavenger = RationalScavenger("Carol", StarChart("Mars", 3, 7, 9))  
 *       Expect: StarChart(Mars, 3, 7, 9)
 * @param scavenger the scavenger whose cargo is being retrieved
 * @return the artifact held in the scavenger's cargo
 */
Artifact getCargo(Scavenger scavenger) {
    return scavenger.cargo();
}

/**
 * Returns the name of the given scavenger.
 * Examples:
 *     - Given: scavenger = RationalScavenger("Alice", EnergyCrystal(50))  
 *       Expect: "Alice"
 *     - Given: scavenger = RiskTakerScavenger("Bob", InertRock("red"))  
 *       Expect: "Bob"
 * @param scavenger - the scavenger whose name is being retrieved
 * @return - the name of the scavenger
 */
String getName(Scavenger scavenger) {
    return scavenger.name();
}

/**
 * The function returns a Pair of the scavenger's new state and the artifact left behind.
 * Use the analysis protocol for the scavenger to get the evaluation of the new artifact,
 * then apply the following rules to determine the final state of the scavenger and their cargo:
 * - If the result is VALUABLE, the scavenger swaps artifacts.
 * - If MUNDANE, the scavenger ignores the new artifact.
 * - If HAZARDOUS, there is a 50% chance the ship's shields hold.
 *   If they hold, the scavenger swaps. 
 *   If they fail, the scavenger's current cargo is destroyed and replaced with a Inert Rock of color "dull grey".
 * - If INCOMPATIBLE or UNKNOWN, the scavenger ignores the new artifact.
 * Examples:
 *     - Given: scavenger = RationalScavenger("Alice", EnergyCrystal(5)), foundArtifact = EnergyCrystal(10)  
 *       Expect: Alice now holds EnergyCrystal(10), leaves behind EnergyCrystal(5)
 *     - Given: scavenger = RationalScavenger("Bob", InertRock("blue")), foundArtifact = InertRock("blue")  
 *       Expect: No change; Bob still holds InertRock("blue")
 *     - Given: scavenger = RationalScavenger("Carol", StarChart("Mars", 2, 3, 4)), foundArtifact = InertRock("red")  
 *       Expect: - If shields hold, cargo is InertRock("red")
 *               - If shields fail, cargo is InertRock("dull grey")
 * @param scavenger the scavenger exploring the asteroid
 * @param foundArtifact the artifact found on the asteroid
 * @return Pair<Scavenger, Artifact>: the updated scavenger and the artifact left behind after the encounter
 */
Pair<Scavenger, Artifact> exploreAsteroid(Scavenger scavenger, Artifact foundArtifact) {
    Result result = evaluateArtifact(scavenger.analysisFunc(), scavenger.cargo(), foundArtifact);
    return switch(result) {
        case isValuable -> swapArtifacts(scavenger, foundArtifact);
        case isMundane, isIncompatible, isUnknown -> ignoreArtifact(scavenger, foundArtifact);
        case isHazardous -> handleHazardousArtifact(scavenger, foundArtifact);
    };
}

/**
 * Use the analysis protocol for the scavenger to get the evaluation of the new artifact.
 * Examples:
 *     - Given: protocol = rationalScavengerAnalysis, owned = InertRock("blue"), found = InertRock("blue")  
 *       Expect: isMundane
 *     - Given: protocol = rationalScavengerAnalysis, owned = InertRock("red"), found = InertRock("blue")  
 *       Expect: isValuable or isIncompatible
 *     - Given: protocol = riskTakerScavengerAnalysis, owned = EnergyCrystal(10), found = EnergyCrystal(20)  
 *       Expect: isValuable
 * @param protocol the artifact evaluation function (rational or risk-taker analysis)
 * @param ownedArtifact the artifact currently held by the scavenger
 * @param foundArtifact the new artifact found
 * @return the result of evaluating the new artifact against the owned one
 */
Result evaluateArtifact(BiFunction<Artifact,Artifact,Result> protocol, Artifact ownedArtifact, Artifact foundArtifact) {
    return protocol.apply(ownedArtifact, foundArtifact);
}

/**
 * If the result is VALUABLE, the scavenger swaps artifacts. 
 * Return a Pair of the scavenger's new state and the artifact left behind，which is the scavenger had before in cargo.
 * Examples:
 *     - Given: scavenger = RationalScavenger("Alice", EnergyCrystal(10)), found = EnergyCrystal(99)  
 *       Expect: Alice now holds EnergyCrystal(99)
 *     - Given: scavenger = RiskTakerScavenger("Bob", InertRock("red")), found = StarChart("Venus", 1, 2, 3)  
 *       Expect: Bob now holds StarChart("Venus", 1, 2, 3)
 * @param scavenger the scavenger who found the new artifact
 * @param foundArtifact the artifact found on the asteroid
 * @return Pair<Scavenger, Artifact>: (updated scavenger, artifact that was replaced)
 */
Pair<Scavenger, Artifact> swapArtifacts(Scavenger scavenger, Artifact foundArtifact) {
    Scavenger updated = new Scavenger(scavenger.name(), scavenger.analysisFunc(), foundArtifact);
    return new Pair<Scavenger, Artifact>(updated, scavenger.cargo());
}

/**
 * If MUNDANE, the scavenger ignores the new artifact.
 * If INCOMPATIBLE or UNKNOWN, the scavenger ignores the new artifact.
 * Return a Pair of the scavenger's new state and the artifact left behind, which is the new found artifact.
 * Examples:
 *     - Given: scavenger = RationalScavenger("Alice", EnergyCrystal(10)), found = EnergyCrystal(10)   
 *       Expect: isMundane. Alice keeps EnergyCrystal(10), leaves behind EnergyCrystal(10)
 *     - Given: scavenger = RiskTakerScavenger("Bob", InertRock("black")), found = InertRock("black")  
 *       Expect: isMundane. Bob keeps InertRock("black"), leaves behind InertRock("black")
 * @param scavenger the scavenger who found the new artifact
 * @param foundArtifact the artifact found but ignored
 * @return Pair<Scavenger, Artifact>: (unchanged scavenger, found artifact that was ignored)
 */
Pair<Scavenger, Artifact> ignoreArtifact(Scavenger scavenger, Artifact foundArtifact) {
    return new Pair<Scavenger, Artifact>(scavenger, foundArtifact);
}

/**
 * If HAZARDOUS, there is a 50% chance the ship's shields hold.
 * - If they hold, the scavenger swaps. 
 * - If they fail, the scavenger's current cargo is destroyed and replaced with a Inert Rock of color "dull grey".
 *   Return a Pair of the scavenger's new state and the artifact left behind, which is the new found artifact.
 * Examples:
 *     Given: scavenger = RationalScavenger("Alice", EnergyCrystal(5)), found = StarChart("Mars", 2, 3, 4)  
 *       Result: isHazardous  
 *         - If shields hold, Alice now holds StarChart("Mars", 2, 3, 4), leaves behind EnergyCrystal(5)  
 *         - If shields fail, Alice now holds InertRock("dull grey"), leaves behind StarChart("Mars", 2, 3, 4)
 * @param scavenger the scavenger encountering a hazardous artifact
 * @param foundArtifact the hazardous artifact found on the asteroid
 * @return Pair<Scavenger, Artifact>: (updated scavenger after handle hazardous artifact, left behind artifact)
 */
Pair<Scavenger, Artifact> handleHazardousArtifact(Scavenger scavenger, Artifact foundArtifact) {
    boolean shieldHolds = Equals(RandomNumber(0, 2), 0);
    if (shieldHolds) {
        return swapArtifacts(scavenger, foundArtifact);
    } else {
        Artifact destroyed = new InertRock("dull grey");
        Scavenger updated = new Scavenger(scavenger.name(), scavenger.analysisFunc(), destroyed);
        return new Pair<Scavenger, Artifact>(updated, foundArtifact);
    }
}

/**
 * Test for Part2 Scenario 1.
 */
void testDifferentStrategies() {
    Artifact lowerStarChart = new StarChart("Safe", -1, 0, 0);
    Artifact higherStarChart = new StarChart("Danger", 8, 2, 0);
    Artifact higherEnergyCrystal = new EnergyCrystal(8);
    Artifact blueInertRock = new InertRock("blue");

    // Both own same StarChart, found lowerStarChart
    Scavenger rationalScavengerWithStarChart = new Scavenger("M", this::rationalScavengerAnalysis, new StarChart("M", 5, 1, 7));
    Scavenger riskTakerScavengerWithStarChart = new Scavenger("M", this::riskTakerScavengerAnalysis, new StarChart("M", 5, 1, 7));
    println("1. Found lowerStarChart when they both owned StarChart(M, 5, 1, 7): ");
    Pair<Scavenger, Artifact> rationalScavengerResult1 = exploreAsteroid(rationalScavengerWithStarChart, lowerStarChart);
    Pair<Scavenger, Artifact> riskTakerScavengerResult1 = exploreAsteroid(riskTakerScavengerWithStarChart, lowerStarChart);
    println("Rational Scavenger: " + rationalScavengerResult1.first().cargo());     // StarChart("Safe", -1, 0, 0)
    println("Risk Taker Scavenger: " + riskTakerScavengerResult1.first().cargo());  // StarChart("Safe", -1, 0, 0)
    
    // Both own same StarChart, found higherStarChart
    println("2. Found higherStarChart when they both owned StarChart(M, 5, 1, 7): ");
    Pair<Scavenger, Artifact> rationalScavengerResult2 = exploreAsteroid(rationalScavengerWithStarChart, higherStarChart);
    Pair<Scavenger, Artifact> riskTakerScavengerResult2 = exploreAsteroid(riskTakerScavengerWithStarChart, higherStarChart);
    println("Rational Scavenger: " + rationalScavengerResult2.first().cargo());     // StarChart("M", 5, 1, 7)
    println("Risk Taker Scavenger: " + riskTakerScavengerResult2.first().cargo());  // StarChart("Danger", 8, 2, 0)

    // Both own same EnergyCrystal, found higherEnergyCrystal
    println("3. Found higherEnergyCrystal when they both owned EnergyCrystal(5): ");
    Scavenger rationalScavengerWithEnergyCrystal = new Scavenger("M", this::rationalScavengerAnalysis, new EnergyCrystal(5));
    Scavenger riskTakerScavengerWithEnergyCrystal = new Scavenger("M", this::riskTakerScavengerAnalysis, new EnergyCrystal(5));
    Pair<Scavenger, Artifact> rationalScavengerResult3 = exploreAsteroid(rationalScavengerWithEnergyCrystal, higherEnergyCrystal);
    Pair<Scavenger, Artifact> riskTakerScavengerResult3 = exploreAsteroid(riskTakerScavengerWithEnergyCrystal, higherEnergyCrystal);
    println("Rational Scavenger: " + rationalScavengerResult3.first().cargo());     // EnergyCrystal(8)
    println("Risk Taker Scavenger: " + riskTakerScavengerResult3.first().cargo());  // EnergyCrystal(8)

    // Both own same InertRock, found blueInertRock
    println("4. Found blueInertRock when they both owned redInertRock: ");
    Scavenger rationalScavengerWithInertRock = new Scavenger("M", this::rationalScavengerAnalysis, new InertRock("red"));
    Scavenger riskTakerScavengerWithInertRock = new Scavenger("M", this::riskTakerScavengerAnalysis, new InertRock("red"));
    Pair<Scavenger, Artifact> rationalScavengerResult4 = exploreAsteroid(rationalScavengerWithInertRock, blueInertRock);
    Pair<Scavenger, Artifact> riskTakerScavengerResult4 = exploreAsteroid(riskTakerScavengerWithInertRock, blueInertRock);
    println("Rational Scavenger: " + rationalScavengerResult4.first().cargo());     // InertRock("red")
    println("Risk Taker Scavenger: " + riskTakerScavengerResult4.first().cargo());  // InertRock("red") or InertRock("blue")
}

void testDifferentStrategies_withTestEqual() {
    Artifact lowerStarChart = new StarChart("Safe", -1, 0, 0);
    Artifact higherStarChart = new StarChart("Danger", 8, 2, 0);
    Artifact higherEnergyCrystal = new EnergyCrystal(8);
    Artifact blueInertRock = new InertRock("blue");

    // Both own same StarChart, found lowerStarChart
    Scavenger rationalStarChart = new Scavenger("M", this::rationalScavengerAnalysis, new StarChart("M", 5, 1, 7));
    Scavenger riskTakerStarChart = new Scavenger("M", this::riskTakerScavengerAnalysis, new StarChart("M", 5, 1, 7));
    Result result1_rational = rationalScavengerAnalysis(rationalStarChart.cargo(), lowerStarChart);
    Result result1_risk = riskTakerScavengerAnalysis(riskTakerStarChart.cargo(), lowerStarChart);
    testEqual(true, isValuable(result1_rational), "Rational: lower risk StarChart is valuable");
    testEqual(true, isValuable(result1_risk), "RiskTaker: always takes StarChart");

    // Both own same StarChart, found higherStarChart
    Result result2_rational = rationalScavengerAnalysis(rationalStarChart.cargo(), higherStarChart);
    Result result2_risk = riskTakerScavengerAnalysis(riskTakerStarChart.cargo(), higherStarChart);
    testEqual(true, isMundane(result2_rational), "Rational: higher risk StarChart is mundane");
    testEqual(true, isValuable(result2_risk), "RiskTaker: always takes StarChart");

    // Both own same EnergyCrystal, found higherEnergyCrystal
    Scavenger rationalEnergy = new Scavenger("M", this::rationalScavengerAnalysis, new EnergyCrystal(5));
    Scavenger riskTakerEnergy = new Scavenger("M", this::riskTakerScavengerAnalysis, new EnergyCrystal(5));
    Result result3_rational = rationalScavengerAnalysis(rationalEnergy.cargo(), higherEnergyCrystal);
    Result result3_risk = riskTakerScavengerAnalysis(riskTakerEnergy.cargo(), higherEnergyCrystal);
    testEqual(true, isValuable(result3_rational), "Rational: higher power EnergyCrystal is valuable");
    testEqual(true, isValuable(result3_risk), "RiskTaker: higher power EnergyCrystal is valuable");

    // Both own same InertRock, found blueInertRock
    Scavenger rationalInert = new Scavenger("M", this::rationalScavengerAnalysis, new InertRock("red"));
    Scavenger riskTakerInert = new Scavenger("M", this::riskTakerScavengerAnalysis, new InertRock("red"));
    Result result4_rational = rationalScavengerAnalysis(rationalInert.cargo(), blueInertRock);
    Result result4_risk = riskTakerScavengerAnalysis(riskTakerInert.cargo(), blueInertRock);
    testEqual(true, isIncompatible(result4_rational), "Rational: different color InertRock is incompatible");
    boolean validRiskResult = isValuable(result4_risk) || isIncompatible(result4_risk);
    testEqual(true, validRiskResult, "RiskTaker: coin flip result for different color InertRock");
} 


// Scenario 2: Trading at a Starport

/**
 * Computes the result of two scavengers meeting to trade.
 * A trade occurs only if Scavenger A evaluates ScavengerB's artifact as VALUABLE (using A's personal protocol),
 * AND Scavenger B evaluates Scavenger A's artifact as VALUABLE (using B's personal protocol). 
 * The function returns a Pair of the scavengers' final states, in their original order.
 * Examples:
 *     - Given: scavengerA = RationalScavenger("Alice", InertRock("red"))
 *              scavengerB = RiskTakerScavenger("Bob", EnergyCrystal(10))
 *       Expect: Both VALUABLE. Alice now holds EnergyCrystal(10), Bob now holds InertRock("red")
 *     - Given: scavengerA = RationalScavenger("Charlie", EnergyCrystal(5))
 *              scavengerB = RiskTakerScavenger("Dana", EnergyCrystal(5))
 *       Expect: no trade 
 * @param scavengerA the first scavenger (evaluates B's artifact using A's protocol)
 * @param scavengerB the second scavenger (evaluates A's artifact using B's protocol)
 * @return Pair<Scavenger, Scavenger>: (scavengerA after trade, scavengerB after trade)
 */
Pair<Scavenger, Scavenger> tradeAtStarport(Scavenger scavengerA, Scavenger scavengerB) {
    Result resultA = evaluateArtifact(scavengerA.analysisFunc(), scavengerA.cargo(), scavengerB.cargo());
    Result resultB = evaluateArtifact(scavengerB.analysisFunc(), scavengerB.cargo(), scavengerA.cargo());
    if (Equals(resultA, Result.isValuable) && Equals(resultB, Result.isValuable)) {
        Scavenger newScavengerA = new Scavenger(scavengerA.name(), scavengerA.analysisFunc(), scavengerB.cargo());
        Scavenger newScavengerB = new Scavenger(scavengerB.name(), scavengerB.analysisFunc(), scavengerA.cargo());
        return new Pair<Scavenger, Scavenger>(newScavengerA, newScavengerB);
    }
    return new Pair<Scavenger, Scavenger>(scavengerA, scavengerB);
}

void testTradeAtStarport() {
    // Case 1: Both value each other (Trade happens)
    Scavenger rational1 = makeScavenger("RationalScavenger", this::rationalScavengerAnalysis, new StarChart("A", 5, 2, 3));
    Scavenger riskTaker1 = makeScavenger("RiskTakerScavenger", this::riskTakerScavengerAnalysis, new StarChart("B", 3, 4, 6));
    Pair<Scavenger, Scavenger> result1 = tradeAtStarport(rational1, riskTaker1);
    println("Case 1 (Both valuable):");
    println(getName(result1.first()) + " has: " + getCargo(result1.first()));    // Expect: StarChart("B", 3, 4, 6)
    println(getName(result1.second()) + " has: " + getCargo(result1.second()));  // Expect: StarChart("A", 5, 2, 3)

    // Case 2: rationalScavenger thinks VALUABLE, riskTakerScavenger thinks MUNDANE (no trade)
    Scavenger rational2 = makeScavenger("RationalScavenger", this::rationalScavengerAnalysis, new EnergyCrystal(5));
    Scavenger riskTaker2 = makeScavenger("RiskTakerScavenger", this::riskTakerScavengerAnalysis, new EnergyCrystal(10));
    Pair<Scavenger, Scavenger> result2 = tradeAtStarport(rational2, riskTaker2);
    println("Case 2 (Rational thinks valuable, RiskTaker does not):");
    println(getName(result2.first()) + " has: " + getCargo(result2.first()));    // Expect: EnergyCrystal 10
    println(getName(result2.second()) + " has: " + getCargo(result2.second()));  // Expect: EnergyCrystal 5

    // CASE 3: rationalScavenger thinks MUNDANE, riskTakerScavenger thinks VALUABLE (no trade)
    Scavenger rational3 = makeScavenger("RationalScavenger", this::rationalScavengerAnalysis, new StarChart("A", 5, 2, 3));
    Scavenger riskTaker3 = makeScavenger("RiskTakerScavenger", this::riskTakerScavengerAnalysis, new EnergyCrystal(10));
    Pair<Scavenger, Scavenger> result3 = tradeAtStarport(rational3, riskTaker3);
    println("\nCase 3 (RiskTaker thinks valuable, Rational does NOT):");
    println(getName(result3.first()) + " has: " + getCargo(result3.first()));  // Expect: EnergyCrystal 5
    println(getName(result3.second()) + " has: " + getCargo(result3.second())); // Expect: EnergyCrystal 10

    // CASE 4: Neither finds the other's cargo VALUABLE (no trade)
    Scavenger rational4 = makeScavenger("RationalScavenger", this::rationalScavengerAnalysis, new EnergyCrystal(5));
    Scavenger riskTaker4 = makeScavenger("RiskTakerScavenger", this::riskTakerScavengerAnalysis, new InertRock("red"));
    Pair<Scavenger, Scavenger> result4 = tradeAtStarport(rational4, riskTaker4);
    println("\nCase 4 (neither finds valuable):");
    println(getName(result4.first()) + " has: " + getCargo(result4.first()));  // Expect: InertRock blue
    println(getName(result4.second()) + " has: " + getCargo(result4.second())); // Expect: InertRock red
}

void testTradeAtStarport_withTestEqual() {
    // Case 1: Both value each other (should trade)
    Scavenger rational1 = makeScavenger("RationalScavenger", this::rationalScavengerAnalysis, new StarChart("A", 5, 2, 3));
    Scavenger riskTaker1 = makeScavenger("RiskTakerScavenger", this::riskTakerScavengerAnalysis, new StarChart("B", 3, 4, 6));
    Pair<Scavenger, Scavenger> result1 = tradeAtStarport(rational1, riskTaker1);
    testEqual(true,
        Equals(getCargo(result1.first()), new StarChart("B", 3, 4, 6)) &&
        Equals(getCargo(result1.second()), new StarChart("A", 5, 2, 3)),
        "Case 1: Should trade when both think valuable"
    );

    // Case 2: Rational thinks VALUABLE, RiskTaker thinks MUNDANE → no trade
    Scavenger rational2 = makeScavenger("RationalScavenger", this::rationalScavengerAnalysis, new EnergyCrystal(5));
    Scavenger riskTaker2 = makeScavenger("RiskTakerScavenger", this::riskTakerScavengerAnalysis, new EnergyCrystal(10));
    Pair<Scavenger, Scavenger> result2 = tradeAtStarport(rational2, riskTaker2);
    testEqual(true,
        Equals(getCargo(result2.first()), new EnergyCrystal(5)) &&
        Equals(getCargo(result2.second()), new EnergyCrystal(10)),
        "Case 2: No trade when RiskTaker thinks MUNDANE"
    );

    // Case 3: Rational thinks MUNDANE, RiskTaker thinks VALUABLE → no trade
    Scavenger rational3 = makeScavenger("RationalScavenger", this::rationalScavengerAnalysis, new StarChart("A", 5, 2, 3));
    Scavenger riskTaker3 = makeScavenger("RiskTakerScavenger", this::riskTakerScavengerAnalysis, new EnergyCrystal(10));
    Pair<Scavenger, Scavenger> result3 = tradeAtStarport(rational3, riskTaker3);
    testEqual(true,
        Equals(getCargo(result3.first()), new StarChart("A", 5, 2, 3)) &&
        Equals(getCargo(result3.second()), new EnergyCrystal(10)),
        "Case 3: No trade when Rational thinks MUNDANE"
    );

    // Case 4: Neither finds other's cargo VALUABLE → no trade
    Scavenger rational4 = makeScavenger("RationalScavenger", this::rationalScavengerAnalysis, new InertRock("blue"));
    Scavenger riskTaker4 = makeScavenger("RiskTakerScavenger", this::riskTakerScavengerAnalysis, new InertRock("red"));
    Pair<Scavenger, Scavenger> result4 = tradeAtStarport(rational4, riskTaker4);
    testEqual(true,
        Equals(getCargo(result4.first()), new InertRock("blue")) &&
        Equals(getCargo(result4.second()), new InertRock("red")),
        "Case 4: No trade when neither thinks valuable"
    );
}


// Part 3 

// 1. Syntax for describing an Artifact
/**
 * Takes an artifact and returns a string of its details, formatted exactly as required for the log entries.
 * - Star Chart: "StarChart:destination;RISK=risk;SEC=sector;SYS=system"
 * - Energy Crystal: "EnergyCrystal:POWER=power"
 * - Inert Rock: "InertRock:COLOR=color"
 * Examples:
 *     - Given: describeArtifact(new StarChart("Alpha", 3, 5, 7)) 
 *     - Expect: "StarChart:Alpha;RISK=3;SEC=5;SYS=7"
 *     - Given: describeArtifact(new EnergyCrystal(10)) 
 *     - Expect: "EnergyCrystal:POWER=10"
 *     - Given: describeArtifact(new InertRock("blue")) 
 *     - Expect: "InertRock:COLOR=blue"
 * @param artifact the artifact need to describe
 * @return string representation in log-entry format
 */
String describeArtifact(Artifact artifact) {
   return switch (artifact) {
    case StarChart sc -> "StarChart:" + sc.dest() + ";RISK=" + sc.risk() + ";SEC=" + sc.sector() + ";SYS=" + sc.system();
    case EnergyCrystal ec -> "EnergyCrystal:POWER=" + ec.power();
    case InertRock ir -> "InertRock:COLOR=" + ir.color(); 
    default -> throw new IllegalArgumentException("Unknown artifact type: " + artifact);
   };
}

// 2. Syntax for a Log Entry
/**
 * Parses a log string from a Rational Scavenger that takes one of the following forms:
 * - ASTEROID | [ownedArtifact] | [foundArtifact]
 * - TRADING_POST | [ownedArtifact] | [otherScavengersArtifact]
 * Then it simulates the encounter and returns the artifact the Rational Scavenger possesses after log entry.
 * Examples:
 *     - Given: "ASTEROID | StarChart(A,5,1,7) | StarChart(B,3,4,6)""
 *       Expect: StarChart(B,3,4,6) 
 *     - Given: "ASTEROID | EnergyCrystal(10) | EnergyCrystal(5)"
 *       Expect: EnergyCrystal(10) 
 *     - Given: "ASTEROID | InertRock(blue) | StarChart(C,6,3,5)"
 *       Expect: InertRock(blue) 
 * @param log a string representing the log entry
 * @return Artifact the Rational Scavenger ends up with
 */
Artifact parseRationalScavengerLog(String log) {
    // get three part in "ASTEROID | [ownedArtifact] | [foundArtifact]"
    int firstBar = IndexOf("|", log, 0);
    String encounter = SubString(log, 0, firstBar).trim();
    int secondBar = IndexOf("|", log, firstBar + 1);
    String ownedStr = SubString(log, firstBar + 1, secondBar).trim();
    String otherStr = SubString(log, secondBar + 1, Length(log)).trim();

    // transfer from String to Artifact
    Artifact owned = parseArtifact(ownedStr);
    Artifact other = parseArtifact(otherStr);

    return encounter(encounter, owned, other);
 }

/**
 * Simulates an encounter between the Rational Scavenger and another artifact.
 * The behavior depends on the type of encounter:
 * - "ASTEROID": The Rational Scavenger encounters an artifact in space, 
 *   uses Rational Scavenger's analysis protocol to decide whether to keep the owned artifact or swap.
 * - "TRADING_POST": The Rational Scavenger is on a trade by another scavenger, 
 *   uses the RiskTaker's protocol to determine if the trade is accepted.
 * The result is the artifact the Rational Scavenger ends up with after the encounter.
 * Examples:
 *   - Given: encounter("ASTEROID", StarChart("A",5,2,3), StarChart("B",3,4,6))
 *     Expect: StarChart("B",3,4,6), swap  
 *   - Given: encounter("ASTEROID", EnergyCrystal(10), EnergyCrystal(5))
 *     Expect: EnergyCrystal(10)   , no trade
 *   - Given: encounter("TRADING_POST", EnergyCrystal(5), InertRock("red"))
 *     Expect: EnergyCrystal(5)    , no trade
 * @param encounterType the type of encounter ("ASTEROID" or "TRADING_POST")
 * @param owned the artifact currently owned by the Rational Scavenger
 * @param other the encountered artifact (either found in asteroid or in trade)
 * @return the artifact Rational Scavenger has after the encounter
 */
Artifact encounter(String encounterType, Artifact owned, Artifact other) {
    if (Equals(encounterType, "ASTEROID")) {
        return handleAsteroidEncounter(owned, other);
    } else {
        return handleTradingPostEncounter(owned, other);
    }
}

// Encounter Asteroid
/**
 * Simulates an encounter between a Rational Scavenger and an artifact found on an asteroid.
 * According to the Rational Scavenger rules:
 * - If the found artifact is considered VALUABLE, the scavenger replaces their current artifact with it.
 * - Otherwise, the scavenger keeps their owned artifact.
 * Examples:
 *    - Given: owned = StarChart("A", 6, 1, 2), found = StarChart("B", 3, 4, 7)
 *      Expect: StarChart("B", 3, 4, 7) 
 *    - Given: owned = EnergyCrystal(10), found = EnergyCrystal(5)
 *      Expect: EnergyCrystal(10)
 *    - Given: owned = InertRock("blue"), found = InertRock("red")
 *      Expect: InertRock("blue")
 *    - Given: owned = EnergyCrystal(4), found = StarChart("C", 2, 1, 5)
 *      Expect: EnergyCrystal(4)
 *    - Given: owned = InertRock("green"), found = StarChart("D", 5, 2, 1)
 *      Expect: InertRock("green")
 * @param owned the artifact currently owned by the Rational Scavenger
 * @param found the artifact found on the asteroid
 * @return the artifact the scavenger ends up holding after the encounter
 */
Artifact handleAsteroidEncounter(Artifact owned, Artifact found) {
    Result result = rationalScavengerAnalysis(owned, found);
    if (isValuable(result)) {
        return found;
    } else {
        return owned;
    }
}

// Encounter TradingPost
/**
 * Simulates a trade encounter at a Trading Post between a Rational Scavenger and a Risk Taker Scavenger.
 * The Rational Scavenger is the primary actor. They will trade only if:
 * - The RiskTakerScavenger considers the Rational Scavenger's artifact to be VALUABLE, according to RiskTakerScavengerAnalysis.
 * - In that case, the Rational Scavenger receives the other scavenger’s artifact. Otherwise, they keep their current artifact.
 * Examples:
 *    - Given: owned = InertRock("blue"), other = StarChart("X", 1, 1, 1)
 *      Expect: StarChart("X", 1, 1, 1)
 *    - Given: owned = EnergyCrystal(10), other = EnergyCrystal(3)
 *      Expect: EnergyCrystal(10)
 *    - Given: owned = EnergyCrystal(3), other = InertRock("red")
 *      Expect: EnergyCrystal(3)
 * @param owned the artifact currently owned by the Rational Scavenger
 * @param other the artifact owned by the Risk Taker Scavenger
 * @return the artifact the Rational Scavenger ends up owned after the trade
 */
Artifact handleTradingPostEncounter(Artifact owned, Artifact other) {
    Result riskTakerResult = riskTakerScavengerAnalysis(other, owned);
    if (isValuable(riskTakerResult)) {
        return other;
    } else {
        return owned;
    }
}

// Parses String to StarChart / EnergyCrystal / InertRock
/**
 * Parses a string representation of an artifact and returns the corresponding Artifact object.
 * The string must be in one of the following formats:
 * - StarChart:destination;RISK=risk;SEC=sector;SYS=system
 * - EnergyCrystal:POWER=power
 * - InertRock:COLOR=color
 * The function identifies the type based on the prefix (before the colon ':'),
 * and extracts the necessary fields to construct the corresponding object.
 * Examples:
 *    - Given: "StarChart:Proxima Centauri;RISK=3;SEC=7;SYS=42"
 *      Expect: new StarChart("Proxima Centauri", 3, 7, 42)
 *    - Given: "EnergyCrystal:POWER=500"
 *      Expect: new EnergyCrystal(500)
 *    - Given: "InertRock:COLOR=quartz"
 *      Expect: new InertRock("quartz")
 * @param s a string representation of an artifact
 * @return an Artifact object corresponding to the input string
 * @throws IllegalArgumentException if the input string has an unknown or malformed format
 */
Artifact parseArtifact(String s) {
    // get type（before the ":"）
    int colonIndex = IndexOf(":", s, 0);
    String type = SubString(s, 0, colonIndex);

    // StarChart, eg.| StarChart:Proxima Centauri;RISK=3;SEC=7;SYS=42 |
    if (Equals(type, "StarChart")) {
        // get destination（between ":" and ";"）in 
        int semi1 = IndexOf(";", s, colonIndex);
        String dest = SubString(s, colonIndex + 1, semi1);

        // get risk (after "RISK=" and before second semicolon ":")
        int riskStart = IndexOf("RISK=", s, semi1) + 5;
        int semi2 = IndexOf(";", s, riskStart);
        String riskStr = SubString(s, riskStart, semi2);
        int risk = StringToInt(riskStr);

        // get sector (after "SEC=" and before third semicolon ":")
        int sectorStart = IndexOf("SEC=", s, semi2) + 4;
        int semi3 = IndexOf(";", s, sectorStart);
        String sectorStr = SubString(s, sectorStart, semi3);
        int sector = StringToInt(sectorStr);

        // get system (after "SYS=" to the end of string)
        int systemStart = IndexOf("SYS=", s, semi3) + 4;
        String systemStr = SubString(s, systemStart, Length(s));
        int system = StringToInt(systemStr);

        return new StarChart(dest, risk, sector, system);
    }

    // EnergyCrystal, eg.| EnergyCrystal:POWER=500 | 
    if (Equals(type, "EnergyCrystal")) {
        // get power (after "=" to the end of string)
        int equal = IndexOf("=", s, colonIndex);
        String powerStr = SubString(s, equal + 1, Length(s));
        int power = StringToInt(powerStr);
        return new EnergyCrystal(power);
    }

    // InertRock, eg. | InertRock:COLOR=quartz |
    if (Equals(type, "InertRock")) {
        // get color (after "=" to the end of string)
        int equal = IndexOf("=", s, colonIndex);
        String color = SubString(s, equal + 1, Length(s));
        return new InertRock(color);
    }

    throw new IllegalArgumentException("Unknown artifact format: " + s);
}

void main() {
    test();
    println("Rational Scavenger Tests: ");
    testRationalScavenger();

    println("Risk Taker Scavenger Tests: ");
    testRiskTakerScavenger();

    println("Part2 Scenario 1 Tests: ");
    testDifferentStrategies();

    println("Part2 Scenario 2 Tests: ");
    testTradeAtStarport();

    println("Part3: ");
    String input = readln("Enter Rational Scavenger log: ");
    Artifact finalArtifact = parseRationalScavengerLog(input);
    String finalDecription = describeArtifact(finalArtifact);
    println("FINAL CARGO: " + finalDecription);
}

void testParseRationalScavengerLog() {
     // ASTEROID: Higher power crystal should be picked
    String log1 = "ASTEROID | EnergyCrystal:POWER=5 | EnergyCrystal:POWER=10";
    Artifact result1 = parseRationalScavengerLog(log1);
    testEqual("EnergyCrystal:POWER=10", describeArtifact(result1), "RationalScavenger should pick higher power EnergyCrystal.");

    // ASTEROID: Lower risk StarChart should be picked
    String log2 = "ASTEROID | StarChart:A;RISK=5;SEC=3;SYS=9 | StarChart:B;RISK=3;SEC=4;SYS=8";
    Artifact result2 = parseRationalScavengerLog(log2);
    testEqual("StarChart:B;RISK=3;SEC=4;SYS=8", describeArtifact(result2), "RationalScavenger should pick lower risk StarChart.");

    // ASTEROID: StarChart vs EnergyCrystal - keep owned
    String log3 = "ASTEROID | StarChart:A;RISK=5;SEC=3;SYS=9 | EnergyCrystal:POWER=10";
    Artifact result3 = parseRationalScavengerLog(log3);
    testEqual("StarChart:A;RISK=5;SEC=3;SYS=9", describeArtifact(result3),
              "Should keep StarChart when found artifact is different type and not valuable."); 
    
    // ASTEROID: InertRock vs InertRock - keep owned
    String log4 = "ASTEROID | InertRock:COLOR=blue | InertRock:COLOR=red";
    Artifact result4 = parseRationalScavengerLog(log4);
    testEqual("InertRock:COLOR=blue", describeArtifact(result4), 
              "Should keep owned InertRock when both are rocks.");

    // ASTEROID: EnergyCrystal lower power - keep owned
    String log5 = "ASTEROID | EnergyCrystal:POWER=15 | EnergyCrystal:POWER=5";
    Artifact result5 = parseRationalScavengerLog(log5);
    testEqual("EnergyCrystal:POWER=15", describeArtifact(result5), 
              "Should keep higher power crystal.");

    // TRADING_POST: Both StarCharts, Risk Taker Scavenge has lower level - trade
    String log6 = "TRADING_POST | StarChart:A;RISK=9;SEC=4;SYS=5 | StarChart:C;RISK=5;SEC=4;SYS=6";
    Artifact result6 = parseRationalScavengerLog(log6);
    testEqual("StarChart:C;RISK=5;SEC=4;SYS=6", describeArtifact(result6),
              "Should trade for higher-level StarChart.");

    // TRADING_POST: Both StarCharts, owned has higher level - no trade
    String log7 = "TRADING_POST | StarChart:A;RISK=8;SEC=4;SYS=6 | StarChart:B;RISK=9;SEC=3;SYS=4";
    Artifact result7 = parseRationalScavengerLog(log7);
    testEqual("StarChart:B;RISK=9;SEC=3;SYS=4", describeArtifact(result7),
              "Should keep higher-level StarChart.");

    // TRADING_POST: One StarChart, one EnergyCrystal - no trade
    String log8 = "TRADING_POST | EnergyCrystal:POWER=5 | StarChart:B;RISK=3;SEC=4;SYS=6";
    Artifact result8 = parseRationalScavengerLog(log8);
    testEqual("EnergyCrystal:POWER=5", describeArtifact(result8),
              "No trade when artifact types differ.");

    // TRADING_POST: Two non-StarCharts - no trade
    String log9 = "TRADING_POST | InertRock:COLOR=green | EnergyCrystal:POWER=8";
    Artifact result9 = parseRationalScavengerLog(log9);
    testEqual("InertRock:COLOR=green", describeArtifact(result9),
              "No trade when neither artifact is a StarChart.");
}
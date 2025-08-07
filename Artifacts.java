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
 * An EnergyCrystal is a Artifact characterized by its power level. 
 * Examples:
 * - A EnergyCrystal of power level 1
 * - A EnergyCrystal of power level 5
 * @param power - the power level of EnergyCrystal
 */
record EnergyCrystal(int power) implements Artifact {}

/**
 * An InertRock is a record representing an inert and mysterious object.
 * The value of rocks cannot be determined. 
 * Examples:
 * - A InertRock of color "blue"
 * - A InertRock of color "red"
 * @param color - the color of InertRock
 */
record InertRock(String color) implements Artifact {}

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
    testEqual(true, isValuable(result), "Lower risk factor of new StarChart should be valuable to owned StarChart.");
}

void testRationalAnalysis_TwoStarChart2() {
    Artifact ownedArtifact = new StarChart("C", -2, 4, 8);
    Artifact newArtifact = new StarChart("S", 7, -5, 9);
    Result result = rationalScavengerAnalysis(ownedArtifact, newArtifact);
    testEqual(true, isMundane(result), "Higher risk factor of new StarChart should be mundane with owned StarChart.");
}

void testRationalAnalysis_TwoStarChart3() {
    Artifact ownedArtifact = new StarChart("F", 0, 0, 0);
    Artifact newArtifact = new StarChart("H", 0, 7, -3);
    Result result = rationalScavengerAnalysis(ownedArtifact, newArtifact);
    testEqual(true, isMundane(result), "Same risk factor of new StarChart should be mundane with owned StarChart.");
}

void testRationalAnalysis_TwoEnergyCrystal1() {
    Artifact ownedArtifact = new EnergyCrystal(-1);
    Artifact newArtifact = new EnergyCrystal(2);
    Result result = rationalScavengerAnalysis(ownedArtifact, newArtifact);
    testEqual(true, isValuable(result), "EnergyCrystal(2) should be more valuable than EnergyCrystal(-1).");
}

void testRationalAnalysis_TwoEnergyCrystal2() {
    Artifact ownedArtifact = new EnergyCrystal(4);
    Artifact newArtifact = new EnergyCrystal(-2);
    Result result = rationalScavengerAnalysis(ownedArtifact, newArtifact);
    testEqual(true, isMundane(result), "EnergyCrystal(-2) should be mundane with EnergyCrystal(4).");
}

void testRationalAnalysis_TwoEnergyCrystal3() {
    Artifact ownedArtifact = new EnergyCrystal(0);
    Artifact newArtifact = new EnergyCrystal(0);
    Result result = rationalScavengerAnalysis(ownedArtifact, newArtifact);
    testEqual(true, isMundane(result), "EnergyCrystal(0) should be mundane with EnergyCrystal(0).");
}

void testRationalAnalysis_TwoInertRocks1() {
    Artifact ownedArtifact = new InertRock("black");
    Artifact newArtifact = new InertRock("black");
    Result result = rationalScavengerAnalysis(ownedArtifact, newArtifact);
    testEqual(true, isMundane(result), "Same colors of two InertRocks should be mundane.");
}

void testRationalAnalysis_TwoInertRocks2() {
    Artifact ownedArtifact = new InertRock("red");
    Artifact newArtifact = new InertRock("blue");
    Result result = rationalScavengerAnalysis(ownedArtifact, newArtifact);
    testEqual(true, isIncompatible(result), "Different colors of two InertRocks should be incompatible.");
}

void testRationalAnalysis_EnergyCrystalStarChart() {
    Artifact ownedArtifact = new EnergyCrystal(-2);
    Artifact newArtifact = new StarChart("A", -1, 4, -8);
    Result result = rationalScavengerAnalysis(ownedArtifact, newArtifact);
    testEqual(true, isHazardous(result), "NewArtifact is StarChart, result should be hazardous.");
}

void testRationalAnalysis_StarChartEnergyCrystal() {
    Artifact ownedArtifact = new StarChart("A", 2, -1, 9);
    Artifact newArtifact = new EnergyCrystal(0);
    Result result = rationalScavengerAnalysis(ownedArtifact, newArtifact);
    testEqual(true, isMundane(result), "NewArtifact is EnergyCrystal, result should be mundane.");
}

void testRationalAnalysis_InertRockStarChart() {
    Artifact ownedArtifact = new InertRock("yellow");
    Artifact newArtifact = new StarChart("G", -3, -1, 6);
    Result result = rationalScavengerAnalysis(ownedArtifact, newArtifact);
    testEqual(true, isUnknown(result), "InertRock with different type should be unknown.");
}

void testRationalAnalysis_InertRockEnergyCrystal() {
    Artifact ownedArtifact = new InertRock("green");
    Artifact newArtifact = new EnergyCrystal(-3);
    Result result = rationalScavengerAnalysis(ownedArtifact, newArtifact);
    testEqual(true, isUnknown(result), "InertRock with different type should be unknown.");
}

// Part 1 - riskTakerScavengerAnalysis tests
void testRiskTaker_NewStarChart1() {
    Artifact ownedArtifact = new StarChart("D", 2, -8, 8);
    Artifact newArtifact = new StarChart("B", -4, 7, -9);
    Result result = riskTakerScavengerAnalysis(ownedArtifact, newArtifact);
    println(result);
}

void testRiskTaker_NewStarChart2() {
    Artifact ownedArtifact = new EnergyCrystal(-2);
    Artifact newArtifact = new StarChart("B", 0, 7, 9);
    Result result = riskTakerScavengerAnalysis(ownedArtifact, newArtifact);
    println(result);
}

void testRiskTaker_NewStarChart3() {
    Artifact ownedArtifact = new InertRock("black");
    Artifact newArtifact = new StarChart("B", 9, 4, 5);
    Result result = riskTakerScavengerAnalysis(ownedArtifact, newArtifact);
    println(result);
}

void testRiskTaker_StarChartEnergyCrystal() {
    Artifact ownedArtifact = new StarChart("G", -4, 8, 34);
    Artifact newArtifact = new EnergyCrystal(2);
    Result result = riskTakerScavengerAnalysis(ownedArtifact, newArtifact);
    println(result);
}

void testRiskTaker_StarChartInertRock() {
    Artifact ownedArtifact = new StarChart("A", 0, -3, -2);
    Artifact newArtifact = new InertRock("yellow");
    Result result = riskTakerScavengerAnalysis(ownedArtifact, newArtifact);
    println(result);
}

void testRiskTaker_TwoEnergyCrystal1() {
    Artifact ownedArtifact = new EnergyCrystal(-1);
    Artifact newArtifact = new EnergyCrystal(2);
    Result result = riskTakerScavengerAnalysis(ownedArtifact, newArtifact);
    println(result);
}

void testRiskTaker_TwoEnergyCrystal2() {
    Artifact ownedArtifact = new EnergyCrystal(3);
    Artifact newArtifact = new EnergyCrystal(-6);
    Result result = riskTakerScavengerAnalysis(ownedArtifact, newArtifact);
    println(result);
}

void testRiskTaker_TwoEnergyCrystal3() {
    Artifact ownedArtifact = new EnergyCrystal(0);
    Artifact newArtifact = new EnergyCrystal(0);
    Result result = riskTakerScavengerAnalysis(ownedArtifact, newArtifact);
    println(result);
}

void testRiskTaker_TwoInertRocks1() {
    Artifact ownedArtifact = new InertRock("blue");
    Artifact newArtifact = new InertRock("blue");
    Result result = riskTakerScavengerAnalysis(ownedArtifact, newArtifact);
    println(result);
}

void testRiskTaker_TwoInertRocks2() {
    Artifact ownedArtifact = new InertRock("red");
    Artifact newArtifact = new InertRock("blue");
    Result result = riskTakerScavengerAnalysis(ownedArtifact, newArtifact);
    println(result);
}

void testRiskTaker_TwoInertRocks3() {
    Artifact ownedArtifact = new InertRock("black");
    Artifact newArtifact = new InertRock("blue");
    Result result = riskTakerScavengerAnalysis(ownedArtifact, newArtifact);
    println(result);
}

void testRiskTaker_InertRockEnergyCrystal() {
    Artifact ownedArtifact = new InertRock("green");
    Artifact newArtifact = new EnergyCrystal(0);
    Result result = riskTakerScavengerAnalysis(ownedArtifact, newArtifact);
    println(result);
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
    runAsTest(this::testRiskTaker_TwoInertRocks3);
    runAsTest(this::testRiskTaker_InertRockEnergyCrystal);

    // Part 2 - exploreAsteroid tests
    runAsTest(this::testExploreAsteroidValuable);
    runAsTest(this::testExploreAsteroidMundane);
    runAsTest(this::testExploreAsteroidHazardousOutcome);
    runAsTest(this::testExploreAsteroidIncompatible);
    runAsTest(this::testExploreAsteroidUnknown);

    // Part 2 - tradeAtStarport tests
    runAsTest(this::testTradeMutualValuable);
    runAsTest(this::testTradeOneSideNotValuable);
}

void main() {
    println("Rational Scavenger Tests: ");
    testRationalScavenger();

    println("Risk Taker Scavenger Tests: ");
    testRiskTakerScavenger();

    println("Part2 Scenario 1 Tests: ");
    testDifferentStrategies();

    println("Part2 Scenario 2 Tests: ");
    testTradeAtStarport();
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
 * @param cargo The single artifact in the scavenger's cargo hold
 * @param analysisFunc The scavenger's personal evaluation protocol implemented as a BiFunction:
 *                     - First Artifact: the ownedArtifact
 *                     - Second Artifact: the new foundArtifact
 *                     - Returns: a Result indicating the evaluation outcome
 */
record Scavenger(String name, Artifact cargo, BiFunction<Artifact, Artifact, Result> analysisFunc) {}

Scavenger makeScavenger(String name, Artifact cargo, BiFunction<Artifact,Artifact,Result> analysisFunc) {
    return new Scavenger(name, cargo, analysisFunc);
}

/**
 * Get Cargo from
 * @param scavenger 
 * @return artifact
 */
Artifact getCargo(Scavenger scavenger) {
    return scavenger.cargo();
}

/**
 * Get name from
 * @param scavenger 
 * @return name
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
 * Example:
 * 
 * @param protocol
 * @param ownedArtifact
 * @param foundArtifact
 * @return
 */
Result evaluateArtifact(BiFunction<Artifact,Artifact,Result> protocol, Artifact ownedArtifact, Artifact foundArtifact) {
    return protocol.apply(ownedArtifact, foundArtifact);
}

/**
 * If the result is VALUABLE, the scavenger swaps artifacts. 
 * Return a Pair of the scavenger's new state and the artifact left behind，which is the scavenger had before in cargo.
 */
Pair<Scavenger, Artifact> swapArtifacts(Scavenger scavenger, Artifact foundArtifact) {
    Scavenger updated = new Scavenger(scavenger.name(), foundArtifact, scavenger.analysisFunc());
    return new Pair<Scavenger, Artifact>(updated, scavenger.cargo());
}

/**
 * If MUNDANE, the scavenger ignores the new artifact.
 * If INCOMPATIBLE or UNKNOWN, the scavenger ignores the new artifact.
 * Return a Pair of the scavenger's new state and the artifact left behind, which is the new found artifact.
 */
Pair<Scavenger, Artifact> ignoreArtifact(Scavenger scavenger, Artifact foundArtifact) {
    return new Pair<Scavenger, Artifact>(scavenger, foundArtifact);
}

/**
 * If HAZARDOUS, there is a 50% chance the ship's shields hold.
 * - If they hold, the scavenger swaps. 
 * - If they fail, the scavenger's current cargo is destroyed and replaced with a Inert Rock of color "dull grey".
 *   Return a Pair of the scavenger's new state and the artifact left behind, which is the new found artifact.
 */
Pair<Scavenger, Artifact> handleHazardousArtifact(Scavenger scavenger, Artifact foundArtifact) {
    boolean shieldHolds = Equals(RandomNumber(0, 2), 0);
    if (shieldHolds) {
        return swapArtifacts(scavenger, foundArtifact);
    } else {
        Artifact destroyed = new InertRock("dull grey");
        Scavenger updated = new Scavenger(scavenger.name(), destroyed, scavenger.analysisFunc());
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

    Scavenger rationalScavengerWithStarChart = new Scavenger("M", new StarChart("M", 5, 1, 7), this::rationalScavengerAnalysis);
    Scavenger riskTakerScavengerWithStarChart = new Scavenger("M", new StarChart("M", 5, 1, 7), this::riskTakerScavengerAnalysis);
    println("1. Found lowerStarChart when they both owned StarChart(M, 5, 1, 7): ");
    Pair<Scavenger, Artifact> rationalScavengerResult1 = exploreAsteroid(rationalScavengerWithStarChart, lowerStarChart);
    Pair<Scavenger, Artifact> riskTakerScavengerResult1 = exploreAsteroid(riskTakerScavengerWithStarChart, lowerStarChart);
    println("Rational Scavenger: " + rationalScavengerResult1.first().cargo());     // StarChart("Safe", -1, 0, 0)
    println("Risk Taker Scavenger: " + riskTakerScavengerResult1.first().cargo());  // StarChart("Safe", -1, 0, 0)
    println("2. Found higherStarChart when they both owned StarChart(M, 5, 1, 7): ");
    Pair<Scavenger, Artifact> rationalScavengerResult2 = exploreAsteroid(rationalScavengerWithStarChart, higherStarChart);
    Pair<Scavenger, Artifact> riskTakerScavengerResult2 = exploreAsteroid(riskTakerScavengerWithStarChart, higherStarChart);
    println("Rational Scavenger: " + rationalScavengerResult2.first().cargo());     // StarChart("M", 5, 1, 7)
    println("Risk Taker Scavenger: " + riskTakerScavengerResult2.first().cargo());  // StarChart("Danger", 8, 2, 0)

    println("3. Found higherEnergyCrystal when they both owned EnergyCrystal(5): ");
    Scavenger rationalScavengerWithEnergyCrystal = new Scavenger("M", new EnergyCrystal(5), this::rationalScavengerAnalysis);
    Scavenger riskTakerScavengerWithEnergyCrystal = new Scavenger("M", new EnergyCrystal(5), this::riskTakerScavengerAnalysis);
    Pair<Scavenger, Artifact> rationalScavengerResult3 = exploreAsteroid(rationalScavengerWithEnergyCrystal, higherEnergyCrystal);
    Pair<Scavenger, Artifact> riskTakerScavengerResult3 = exploreAsteroid(riskTakerScavengerWithEnergyCrystal, higherEnergyCrystal);
    println("Rational Scavenger: " + rationalScavengerResult3.first().cargo());     // EnergyCrystal(8)
    println("Risk Taker Scavenger: " + riskTakerScavengerResult3.first().cargo());  // EnergyCrystal(8)

    println("4. Found blueInertRock when they both owned redInertRock: ");
    Scavenger rationalScavengerWithInertRock = new Scavenger("M", new InertRock("red"), this::rationalScavengerAnalysis);
    Scavenger riskTakerScavengerWithInertRock = new Scavenger("M", new InertRock("red"), this::riskTakerScavengerAnalysis);
    Pair<Scavenger, Artifact> rationalScavengerResult4 = exploreAsteroid(rationalScavengerWithInertRock, blueInertRock);
    Pair<Scavenger, Artifact> riskTakerScavengerResult4 = exploreAsteroid(riskTakerScavengerWithInertRock, blueInertRock);
    println("Rational Scavenger: " + rationalScavengerResult4.first().cargo());     // InertRock("red")
    println("Risk Taker Scavenger: " + riskTakerScavengerResult4.first().cargo());  // InertRock("red") or InertRock("blue")
}


// Scenario 2: Trading at a Starport

/**
 * Computes the result of two scavengers meeting to trade.
 * A trade occurs only if Scavenger A evaluates ScavengerB's artifact as VALUABLE (using A's personal protocol),
 * AND Scavenger B evaluates Scavenger A's artifact as VALUABLE (using B's personal protocol). 
 * The function returns a Pair of the scavengers' final states, in their original order.
 */
Pair<Scavenger, Scavenger> tradeAtStarport(Scavenger scavengerA, Scavenger scavengerB) {
    Result resultA = evaluateArtifact(scavengerA.analysisFunc(), scavengerA.cargo(), scavengerB.cargo());
    Result resultB = evaluateArtifact(scavengerB.analysisFunc(), scavengerB.cargo(), scavengerA.cargo());
    if (Equals(resultA, Result.isValuable) && Equals(resultB, Result.isValuable)) {
        Scavenger newScavengerA = new Scavenger(scavengerA.name(), scavengerB.cargo(), scavengerA.analysisFunc());
        Scavenger newScavengerB = new Scavenger(scavengerB.name(), scavengerA.cargo(), scavengerB.analysisFunc());
        return new Pair<Scavenger, Scavenger>(newScavengerA, newScavengerB);
    }
    return new Pair<Scavenger, Scavenger>(scavengerA, scavengerB);
}

void testTradeAtStarport() {
    Scavenger rationalScavenger = makeScavenger("RationalScavenger", new StarChart("A", 9, 1, 7), this::rationalScavengerAnalysis);
    Scavenger riskTakerScavenger = makeScavenger("RiskTakerScavenger", new EnergyCrystal(5), this::riskTakerScavengerAnalysis);
    Pair<Scavenger, Scavenger> result = tradeAtStarport(rationalScavenger, riskTakerScavenger);
    println("After trade: ");
    println(getName(rationalScavenger) + " has: " + getCargo(result.first()));
    println(getName(riskTakerScavenger) + " has: " + getCargo(result.second()));
}

// Part 3 

/**
 * 1. Syntax for describing an Artifact
 * Takes an artifact and returns a string of its details, formatted exactly as required for the log entries.
 * - Star Chart: "StarChart:destination;RISK=risk;SEC=sector;SYS=system"
 * - Energy Crystal: "EnergyCrystal:POWER=power"
 * - Inert Rock: "InertRock:COLOR=color"
 */
String describeArtifact(Artifact artifact) {
   return switch (artifact) {
    case StarChart sc -> "StarChart:" + sc.dest() + ";RISK=" + sc.risk() + ";SEC=" + sc.sector() + ";SYS=" + sc.system();
    case EnergyCrystal ec -> "EnergyCrystal:POWER=" + ec.power();
    case InertRock ir -> "InertRock:COLOR=" + ir.color(); 
    default -> throw new IllegalArgumentException("Unknown artifact type: " + artifact);
   }
}

 /**
  * 2. Syntax for a Log Entry
  * Parses a log string from a Rational Scavenger that takes one of the following forms:
  * - ASTEROID | [ownedArtifact] | [foundArtifact]
  * - TRADING_POST | [ownedArtifact] | [otherScavengersArtifact]
  * Then it simulates the encounter and returns the
  * artifact the Rational Scavenger possesses *after*
  * the log entry
  */
Artifact parseRationalScavengerLog(String log) {
    // get ASTEROID or TRADING_POST in "ASTEROID | [ownedArtifact] | [foundArtifact]"
    int firstBar = IndexOf("|", log, 0);
    String encounter = Substring(log, 0, firstSep).trim();

    // get [ownedArtifact]
    int secondSep = IndexOf("|", log, firstSep + 1);
    String ownedStr = Substring(log, firstSep + 1, secondSep).trim();

    // get [foundArtifact]
    String otherStr = Substring(log, secondSep + 1, Length(log)).trim();

    // 把 ownedStr 解析成 Artifact 对象
    Artifact owned = parseArtifact(ownedStr);

    // 把 otherStr 解析成 Artifact 对象
    Artifact other = parseArtifact(otherStr);

    // If ASTEROID，ues rationalScavengerAnalysis
    boolean isAsteroid = Equals(encounter, "ASTEROID");
    Result asteroidResult = rationalScavengerAnalysis(owned, other);

 }


Artifact encounter(String encounterType, Artifact owned, Artifact other) {
    if (Equals(encounterType, "ASTEROID")) {
        return handleAsteroidEncounter(owned, other);
    } else {
        return handleTradingPostEncounter(owned, other);
    }
}

// Encounter Asteroid
Artifact handleAsteroidEncounter(Artifact owned, Artifact found) {
    Result result = rationalScavengerAnalysis(owned, found);
    if (isValuable(result)) {
        return found;
    } else {
        return owned;
    }
}

// Encounter TradingPost
Artifact handleTradingPostEncounter(Artifact owned, Artifact other) {
    Result riskTakerResult = riskTakerScavengerAnalysis(other, owned);
    Result rationalResult = rationalScavengerAnalysis(owned, other);
}




import comp1110.lib.*;
import comp1110.lib.Date;
import static comp1110.lib.Functions.*;

import static comp1110.testing.Comp1110Unit.*;

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
 * - A StarChart to "Canberra" with risk 2 from sector 3, system 4
 * - A StarChart to "Sydney" with risk 1 from sector 4, system 7
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
    if (RandomNumber(0, 2) == 0) {
        return Result.isValuable;
    } else {
        return Result.isIncompatible;
    }
}


void main() {
    //Tests for Rational Scavenger
    Artifact ownedArtifact1 = new StarChart("A", 8, 8, 8);
    Artifact newArtifact1 = new StarChart("C", -1, 7, 9);
    println(rationalScavengerAnalysis(ownedArtifact1, newArtifact1));

    Artifact ownedArtifact2 = new StarChart("C", -2, 4, 8);
    Artifact newArtifact2 = new StarChart("S", 0, -5, 9);
    println(rationalScavengerAnalysis(ownedArtifact2, newArtifact2));

    Artifact ownedArtifact3 = new StarChart("F", 0, 0, 0);
    Artifact newArtifact3 = new StarChart("H", 0, 7, -3);
    println(rationalScavengerAnalysis(ownedArtifact3, newArtifact3));

    Artifact ownedArtifact4 = new EnergyCrystal(-1);
    Artifact newArtifact4 = new EnergyCrystal(2);
    println(rationalScavengerAnalysis(ownedArtifact4, newArtifact4));

    Artifact ownedArtifact5 = new EnergyCrystal(4);
    Artifact newArtifact5 = new EnergyCrystal(-2);
    println(rationalScavengerAnalysis(ownedArtifact5, newArtifact5));

    Artifact ownedArtifact6 = new EnergyCrystal(0);
    Artifact newArtifact6 = new EnergyCrystal(0);
    println(rationalScavengerAnalysis(ownedArtifact6, newArtifact6));

    Artifact ownedArtifact7 = new InertRock("black");
    Artifact newArtifact7 = new InertRock("black");
    println(rationalScavengerAnalysis(ownedArtifact7, newArtifact7));

    Artifact ownedArtifact8 = new InertRock("red");
    Artifact newArtifact8 = new InertRock("blue");
    println(rationalScavengerAnalysis(ownedArtifact8, newArtifact8));

    Artifact ownedArtifact9 = new EnergyCrystal(-2);
    Artifact newArtifact9 = new StarChart("A", -1, 4, -8);
    println(rationalScavengerAnalysis(ownedArtifact9, newArtifact9));

    Artifact ownedArtifact10 = new StarChart("A", 2, -1, 9);
    Artifact newArtifact10 = new EnergyCrystal(0);
    println(rationalScavengerAnalysis(ownedArtifact10, newArtifact10));

    Artifact ownedArtifact11 = new InertRock("yellow");
    Artifact newArtifact11 = new StarChart("G", -3, -1, 6);
    println(rationalScavengerAnalysis(ownedArtifact11, newArtifact11));

    Artifact ownedArtifact12 = new InertRock("green");
    Artifact newArtifact12 = new EnergyCrystal(-3);
    println(rationalScavengerAnalysis(ownedArtifact12, newArtifact12));

    //Tests for Risk Taker Scavenger
    Artifact ownedArtifact13 = new StarChart("D", 2, -8, 8);
    Artifact newArtifact13 = new StarChart("B", -4, 7, -9);
    println(riskTakerScavengerAnalysis(ownedArtifact13, newArtifact13));

    Artifact ownedArtifact14 = new EnergyCrystal(-2);
    Artifact newArtifact14 = new StarChart("B", 0, 7, 9);
    println(riskTakerScavengerAnalysis(ownedArtifact14, newArtifact14));

    Artifact ownedArtifact15 = new InertRock("black");
    Artifact newArtifact15 = new StarChart("B", 9, 4, 5);
    println(riskTakerScavengerAnalysis(ownedArtifact15, newArtifact15));

    Artifact ownedArtifact16 = new StarChart("G", -4, 8, 34);
    Artifact newArtifact16 = new EnergyCrystal(2);
    println(riskTakerScavengerAnalysis(ownedArtifact16, newArtifact16));

    Artifact ownedArtifact17 = new StarChart("A", 0, -3, -2);
    Artifact newArtifact17 = new InertRock("yellow");
    println(riskTakerScavengerAnalysis(ownedArtifact17, newArtifact17));

    Artifact ownedArtifact18 = new EnergyCrystal(-1);
    Artifact newArtifact18 = new EnergyCrystal(2);
    println(riskTakerScavengerAnalysis(ownedArtifact18, newArtifact18));

    Artifact ownedArtifact19 = new EnergyCrystal(3);
    Artifact newArtifact19 = new EnergyCrystal(-6);
    println(riskTakerScavengerAnalysis(ownedArtifact19, newArtifact19));

    Artifact ownedArtifact20 = new EnergyCrystal(0);
    Artifact newArtifact20 = new EnergyCrystal(0);
    println(riskTakerScavengerAnalysis(ownedArtifact20, newArtifact20));

    Artifact ownedArtifact21 = new InertRock("blue");
    Artifact newArtifact21 = new InertRock("blue");
    println(riskTakerScavengerAnalysis(ownedArtifact21, newArtifact21));

    Artifact ownedArtifact22 = new InertRock("red");
    Artifact newArtifact22 = new InertRock("blue");
    println(riskTakerScavengerAnalysis(ownedArtifact22, newArtifact22));

    Artifact ownedArtifact23 = new InertRock("black");
    Artifact newArtifact23 = new InertRock("blue");
    println(riskTakerScavengerAnalysis(ownedArtifact23, newArtifact23));

    Artifact ownedArtifact24 = new InertRock("green");
    Artifact newArtifact24 = new EnergyCrystal(0);
    println(riskTakerScavengerAnalysis(ownedArtifact24, newArtifact24));
}


void testTwoStarChartExample1() {
    Artifact ownedArtifact = new StarChart("A", 8, 8, 8);
    Artifact newArtifact = new StarChart("C", -1, 7, 9);
    Result result = rationalScavengerAnalysis(ownedArtifact, newArtifact);
    testEqual(true, isValuable(result), "Lower risk factor of new StarChart should be valuable to owned StarChart.");
}

void testTwoStarChartExample2() {
    Artifact ownedArtifact = new StarChart("C", -2, 4, 8);
    Artifact newArtifact = new StarChart("S", 7, -5, 9);
    Result result = rationalScavengerAnalysis(ownedArtifact, newArtifact);
    testEqual(true, isMundane(result), "Higher risk factor of new StarChart should be mundane with owned StarChart.");
}

void testTwoStarChartExample3() {
    Artifact ownedArtifact = new StarChart("F", 0, 0, 0);
    Artifact newArtifact = new StarChart("H", 0, 7, -3);
    Result result = rationalScavengerAnalysis(ownedArtifact, newArtifact);
    testEqual(true, isMundane(result), "Same risk factor of new StarChart should be mundane with owned StarChart.");
}

void testTwoEnergyCrystalExample1() {
    Artifact ownedArtifact = new EnergyCrystal(-1);
    Artifact newArtifact = new EnergyCrystal(2);
    Result result = rationalScavengerAnalysis(ownedArtifact, newArtifact);
    testEqual(true, isValuable(result), "EnergyCrystal(2) should be more valuable than EnergyCrystal(-1).");
}

void testTwoEnergyCrystalExample2() {
    Artifact ownedArtifact = new EnergyCrystal(4);
    Artifact newArtifact = new EnergyCrystal(-2);
    Result result = rationalScavengerAnalysis(ownedArtifact, newArtifact);
    testEqual(true, isMundane(result), "EnergyCrystal(-2) should be mundane with EnergyCrystal(4).");
}

void testTwoEnergyCrystalExample3() {
    Artifact ownedArtifact = new EnergyCrystal(0);
    Artifact newArtifact = new EnergyCrystal(0);
    Result result = rationalScavengerAnalysis(ownedArtifact, newArtifact);
    testEqual(true, isMundane(result), "EnergyCrystal(0) should be mundane with EnergyCrystal(0).");
}

void testTwoInertRocksExample1() {
    Artifact ownedArtifact = new InertRock("black");
    Artifact newArtifact = new InertRock("black");
    Result result = rationalScavengerAnalysis(ownedArtifact, newArtifact);
    testEqual(true, isMundane(result), "Same colors of two InertRocks should be mundane.");
}

void testTwoInertRocksExample2() {
    Artifact ownedArtifact = new InertRock("red");
    Artifact newArtifact = new InertRock("blue");
    Result result = rationalScavengerAnalysis(ownedArtifact, newArtifact);
    testEqual(true, isIncompatible(result), "Different colors of two InertRocks should be incompatible.");
}

void testEnergyCrystalStarChartExample() {
    Artifact ownedArtifact = new EnergyCrystal(-2);
    Artifact newArtifact = new StarChart("A", -1, 4, -8);
    Result result = rationalScavengerAnalysis(ownedArtifact, newArtifact);
    testEqual(true, isHazardous(result), "NewArtifact is StarChart, result should be hazardous.");
}

void testStarChartEnergyCrystalExample() {
    Artifact ownedArtifact = new StarChart("A", 2, -1, 9);
    Artifact newArtifact = new EnergyCrystal(0);
    Result result = rationalScavengerAnalysis(ownedArtifact, newArtifact);
    testEqual(true, isMundane(result), "NewArtifact is EnergyCrystal, result should be mundane.");
}

void testInertRockStarChartExample() {
    Artifact ownedArtifact = new InertRock("yellow");
    Artifact newArtifact = new StarChart("G", -3, -1, 6);
    Result result = rationalScavengerAnalysis(ownedArtifact, newArtifact);
    testEqual(true, isUnknown(result), "InertRock with different type should be unknown.");
}

void testInertRockEnergyCrystalExample() {
    Artifact ownedArtifact = new InertRock("green");
    Artifact newArtifact = new EnergyCrystal(-3);
    Result result = rationalScavengerAnalysis(ownedArtifact, newArtifact);
    testEqual(true, isUnknown(result), "InertRock with different type should be unknown.");
}

void test() {
    runAsTest(this::testTwoStarChartExample1);
    runAsTest(this::testTwoStarChartExample2);
    runAsTest(this::testTwoStarChartExample3);
    runAsTest(this::testTwoEnergyCrystalExample1);
    runAsTest(this::testTwoEnergyCrystalExample2);
    runAsTest(this::testTwoEnergyCrystalExample3);
    runAsTest(this::testTwoInertRocksExample1);
    runAsTest(this::testTwoInertRocksExample2);
    runAsTest(this::testEnergyCrystalStarChartExample);
    runAsTest(this::testStarChartEnergyCrystalExample);
    runAsTest(this::testInertRockStarChartExample);
    runAsTest(this::testInertRockEnergyCrystalExample);
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



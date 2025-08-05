import comp1110.lib.*;
import comp1110.lib.Date;
import static comp1110.lib.Functions.*;

import static comp1110.testing.Comp1110Unit.*;

// [R] is type for the analysis result
/**
 * An enumeration of results when a scavenger
 * compares the newArtifact and the ownedArtifact.
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
 * @param system - the sector of origin point about StarChart
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
 * The value of rocks cannot be determined. Examples:
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

/** Code template for the Artifact general itemization
 * {
 * ...
 * ... return switch(ownedArtifact) {
 *        case(StarChart(String dest, int risk, int sector, int system)) -> ...;
 *        case(EnergyCrystal(int power)) -> ...;
 *        case(InertRock(String color)) -> ...;
 *     }...;
 * }
 * 
 */

/**
 * Function's purpose statement: 
 * Given ownedArtifact and newArtifact by a Rational Scavenger, 
 * through comparative analysis of the newArtifact and the ownedArtifact, 
 * returns the results, which can be valuable, hazardous, mundane, incompatible, or unknown.
 * Examples:
 *     - Given: ownedArtifact = StarChart("A", 2, 8, 8), newArtifact = StarChart("B", 4, 7, 9)
 *          Expect: isMundane
 *     - Given: ownedArtifact = EnergyCrystal(1), newArtifact = EnergyCrystal(2)
 *          Expect: isValuable
 *     - Given: ownedArtifact = InertRock("red"), newArtifact = InertRock("blue")
 *          Expect: isIncompatible
 *     - Given: ownedArtifact = EnergyCrystal(2), newArtifact = new StarChart("A", 2, 8, 8)
 *          Expect: isHazardous
 *     - Given: ownedArtifact = InertRock("blue"), newArtifact = new EnergyCrystal(3)
 *          Expect: isUnknown
 * Design strategy: Template 
 * @param ownedArtifact the artifact already owned by Rational Scavenger.
 * @param newArtifact the new artifact Rational Scavenger got.
 * @return returns the result through comparative analysis of the newArtifact and the ownedArtifact.
 */
Result rationalScavengerAnalysis(Artifact ownedArtifact, Artifact newArtifact) {
    return switch(ownedArtifact) {
        case StarChart(String dest1, int risk1, int sector1, int system1) -> compareOwnedStarChart(risk1, newArtifact);
        case EnergyCrystal(int power1) -> compareOwnedEnergyCrystal(power1, newArtifact);
        case InertRock(String color1) -> compareOwnedInertRock(color1, newArtifact);
    };
}


/**
 * Function's purpose statement: 
 * Given ownedArtifact and newArtifact by a Risk Taker Scavenger, 
 * through comparative analysis of the newArtifact and the ownedArtifact, 
 * returns the results, which can be valuable, hazardous, mundane, incompatible, or unknown.
 * Examples:
 *     - Given: ownedArtifact = StarChart("A", 2, 8, 8), newArtifact = StarChart("B", 4, 7, 9)
 *          Expect: 
 * 
 * Design strategy: Template 
 * @param ownedArtifact the artifact already owned by Risk Taker Scavenger.
 * @param newArtifact the new artifact Risk Taker Scavenger got.
 * @return returns the result through comparative analysis of the newArtifact and the ownedArtifact.
 */
Result riskTakerScavengerAnalysis(Artifact ownedArtifact, Artifact newArtifact) {
    return switch(newArtifact) {
        case StarChart(String dest2, int risk2, int sector2, int system2) -> Result.isValuable;
        case EnergyCrystal(int power2) -> compareNewEnergyCrystal(ownedArtifact, power2);
        case InertRock(String color2) -> compareNewInertRock(ownedArtifact, color2);
    };
}


/** Risk Taker Scavenger: 新文物是EnergyCrystal
 * Risk Taker Scavenger: When newArtifact is EnergyCrystal, analysis the ownedArtifact.
 * newArtifact is EnergyCrystal and ownedArtifact is StarChart: return isMundane;
 * newArtifact is EnergyCrystal and ownedArtifact is EnergyCrystal: comparing two Energy Crystals, 
 * if the new crystal has a higher power level than the crystal they own, return isValuable; otherwise, return isMundane.
 * newArtifact is EnergyCrystal and ownedArtifact is InertRock: return isUnknown.
 * 
 */
Result compareNewEnergyCrystal(Artifact ownedArtifact, int power2) {
    return switch(ownedArtifact) {
        case StarChart(String dest1, int risk1, int sector1, int system1) -> Result.isMundane;
        case EnergyCrystal(int power1) -> compareTwoEnergyCrystals(power1, power2);
        case InertRock(String color1) -> Result.isUnknown;
    };
}


/** Risk Taker Scavenger: 新文物是InertRock
 * Risk Taker Scavenger: When newArtifact is InertRock, analysis the ownedArtifact.
 * newArtifact is InertRock and ownedArtifact is StarChart: return isMundane;
 * newArtifact is InertRock and ownedArtifact is EnergyCrystal: return isUnknown.
 * newArtifact is InertRock and ownedArtifact is InertRock: comparing two Inert Rocks, 
 * if they are the same color, return isMundan; otherwise, a coin flip determines the outcome: 
 * there’s a 50% chance the new rock is considered VALUABLE, and INCOMPATIBLE otherwise.
 */
Result compareNewInertRock(Artifact ownedArtifact, String color2) {
    return switch(ownedArtifact) {
        case StarChart(String dest1, int risk1, int sector1, int system1) -> Result.isMundane;
        case EnergyCrystal(int power1) -> Result.isUnknown;
        case InertRock(String color1) -> compareTwoInertRocksRiskTakerScavenger(color1, color2);
    };
}


/**
 * Compare an owned artifact with a new artifact.
 * If the new artifact is a StarChart, compare their risks.
 * If the new artifact is an EnergyCrystal, return isMundane;
 * If the new artifact is an InertRock, return isUnknown.
 *  Examples:
 *     - Given: StarChart("A", 2, 8, 8)
 *          Expect: 
 *     - Given: EnergyCrystal(1)
 *          Expect: 
 *     - Given: InertRock("blue")
 *          Expect: 
 * Design strategy: Template 
 * @param risk1
 * @param newArtifact
 * @return returns the result after comparing the newArtifact and the ownedArtifact.
 */
Result compareOwnedStarChart(int risk1, Artifact newArtifact) {
    return switch(newArtifact) {
        case StarChart(String dest2, int risk2, int sector2, int system2) -> compareTwoStarCharts(risk1, risk2);
        case EnergyCrystal(int power2) -> Result.isMundane;
        case InertRock(String color2) -> Result.isUnknown;
    };
}

Result compareOwnedEnergyCrystal(int power1, Artifact newArtifact) {
    return switch(newArtifact) {
        case StarChart(String dest2, int risk2, int sector2, int system2) -> Result.isHazardous;
        case EnergyCrystal(int power2) -> compareTwoEnergyCrystals(power1, power2);
        case InertRock(String color2) -> Result.isUnknown;
    };
}

Result compareOwnedInertRock(String color1, Artifact newArtifact) {
    return switch(newArtifact) {
        case StarChart(String dest2, int risk2, int sector2, int system2) -> Result.isUnknown;
        case EnergyCrystal(int power2) -> Result.isUnknown;
        case InertRock(String color2) -> compareTwoInertRocksRationalScavenger(color1, color2);
    };
}

Result compareTwoStarCharts(int risk1, int risk2) {
    if (risk1 > risk2) {
        return Result.isValuable;
    } else {
        return Result.isMundane;
    }
}

// power1 is owned, power2 is new
Result compareTwoEnergyCrystals(int power1, int power2) {
    if (power1 < power2) {
        return Result.isValuable;
    } else {
        return Result.isMundane;
    }
}

// RationalScavenger
Result compareTwoInertRocksRationalScavenger(String color1, String color2) {
    if (Equals(color1, color2)) {
        return Result.isMundane;
    } else {
        return Result.isIncompatible;
    }
}

// RiskTakerScavenger 
// 比较两个惰性岩石InertRock：如果颜色color相同 → isMundane；如果颜色不同 → 掷硬币决定：50% 概率是 isValuable，50%是isIncompatible
Result compareTwoInertRocksRiskTakerScavenger(String color1, String color2) {
    if (Equals(color1, color2)) {
        return Result.isMundane;
    } else {
        return coinFlip();
    }
}

//掷硬币决定：50% 概率是 isValuable，50%是isIncompatible。 RandomNumber(0, 2)
Result coinFlip() {
    if (RandomNumber(0, 2) == 0) {
        return Result.isValuable;
    } else {
        return Result.isIncompatible;
    }
}


void main() {
    Artifact ownedArtifact1 = new StarChart("A", 2, 8, 8);
    Artifact newArtifact1 = new StarChart("B", 4, 7, 9);
    println(rationalScavengerAnalysis(ownedArtifact1, newArtifact1));

    Artifact ownedArtifact2 = new EnergyCrystal(1);
    Artifact newArtifact2 = new EnergyCrystal(2);
    println(rationalScavengerAnalysis(ownedArtifact2, newArtifact2));

    Artifact ownedArtifact3 = new InertRock("red");
    Artifact newArtifact3 = new InertRock("blue");
    println(rationalScavengerAnalysis(ownedArtifact3, newArtifact3));

    Artifact ownedArtifact4 = new EnergyCrystal(2);
    Artifact newArtifact4 = new StarChart("A", 2, 8, 8);
    println(rationalScavengerAnalysis(ownedArtifact4, newArtifact4));

    Artifact ownedArtifact5 = new InertRock("blue");
    Artifact newArtifact5 = new EnergyCrystal(3);
    println(rationalScavengerAnalysis(ownedArtifact5, newArtifact5));
}

void testTwoStarChartExample() {
    Artifact ownedArtifact = new StarChart("A", 2, 8, 8);
    Artifact newArtifact = new StarChart("B", 4, 7, 9);
    Result result = rationalScavengerAnalysis(ownedArtifact, newArtifact);
    testEqual(true, isMundane(result), "Higher risk factor of new StarChart should be mundane with owned StarChart.");
}

void testTwoEnergyCrystalExample() {
    Artifact ownedArtifact = new EnergyCrystal(1);
    Artifact newArtifact = new EnergyCrystal(2);
    Result result = rationalScavengerAnalysis(ownedArtifact, newArtifact);
    testEqual(true, isValuable(result), "EnergyCrystal(2) should be more valuable than EnergyCrystal(1).");
}

void testTwoInertRocksExample() {
    Artifact ownedArtifact = new InertRock("red");
    Artifact newArtifact = new InertRock("blue");
    Result result = rationalScavengerAnalysis(ownedArtifact, newArtifact);
    testEqual(true, isIncompatible(result), "Different colors of two InertRocks should be incompatible.");
}

void testEnergyCrystalStarChartExample() {
    Artifact ownedArtifact = new EnergyCrystal(2);
    Artifact newArtifact = new StarChart("A", 2, 8, 8);
    Result result = rationalScavengerAnalysis(ownedArtifact, newArtifact);
    testEqual(true, isHazardous(result), "NewArtifact is StarChart, result should be hazardous.");
}

void testInertRockEnergyCrystalExample() {
    Artifact ownedArtifact = new InertRock("blue");
    Artifact newArtifact = new EnergyCrystal(3);
    Result result = rationalScavengerAnalysis(ownedArtifact, newArtifact);
    testEqual(true, isUnknown(result), "InertRock with different type should be unknown.");
}

void test() {
    runAsTest(this::testTwoStarChartExample);
    runAsTest(this::testTwoEnergyCrystalExample);
    runAsTest(this::testTwoInertRocksExample);
    runAsTest(this::testEnergyCrystalStarChartExample);
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

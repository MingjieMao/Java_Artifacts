import comp1110.lib.*;
import comp1110.lib.Date;
import static comp1110.lib.Functions.*;

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
 * returns the results after comparing the newArtifact and the ownedArtifact, 
 * which can be valuable, hazardous, mundane, incompatible, or unknown.
 * Examples:
 *     - Given: StarChart("Sydney",1,4,7)
 *          Expect: 
 *     - Given: EnergyCrystal(1)
 *          Expect: 
 *     - Given: InertRock("blue")
 *          Expect: 
 * Design strategy: Template 
 * @param ownedArtifact 
 * @param newArtifact
 * @return returns the result after comparing the newArtifact and the ownedArtifact.
 */
Result rationalScavengerAnalysis(Artifact ownedArtifact, Artifact newArtifact) {
    return switch(ownedArtifact) {
        case StarChart(String dest1, int risk1, int sector1, int system1) -> compareOwnedStarChart(risk1, newArtifact);
        case EnergyCrystal(int power1) -> compareOwnedEnergyCrystal(power1, newArtifact);
        case InertRock(String color1) -> compareOwnedInertRock(color1, newArtifact);
    };
}

/**
 * Compare an owned artifact with a new artifact.
 * If the new artifact is a StarChart, compare their risks.
 * If the new artifact is an EnergyCrystal, return isMundane;
 * If the new artifact is an InertRock, return isUnknown.
 *  Examples:
 *     - Given: StarChart("Sydney",1,4,7)
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
        case InertRock(String color2) -> compareTwoInertRocks(color1, color2);
    };
}

Result compareTwoStarCharts(int risk1, int risk2) {
    if (risk1 > risk2) {
        return Result.isValuable;
    } else {
        return Result.isMundane;
    }
}

Result compareTwoEnergyCrystals(int power1, int power2) {
    if (power1 < power2) {
        return Result.isValuable;
    } else {
        return Result.isMundane;
    }
}

Result compareTwoInertRocks(String color1, String color2) {
    if (Equals(color1, color2)) {
        return Result.isMundane;
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

void main() {
    Artifact ownedArtifact = new EnergyCrystal(1);
    Artifact newArtifact = new EnergyCrystal(2);
    println(rationalScavengerAnalysis(ownedArtifact, newArtifact));
}

void testRationalScavengerAnalysisExample() {
    Artifact ownedArtifact = new EnergyCrystal(1);
    Artifact newArtifact = new EnergyCrystal(2);
    Result result = rationalScavengerAnalysis(ownedArtifact, newArtifact);
    testEqual(true, isValuable(result), "EnergyCrystal(2) should be more valuable than EnergyCrystal(1).");
}

void test() {
    runAsTest(this::testRationalScavengerAnalysisExample);
}

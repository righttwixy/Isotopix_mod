package by.righttwixys.isotopix.radiation;

public class IsotopeRadiationProfile {
    private final double activityBq;
    private final double gammaDoseConst;
    private final double betaDoseConst;
    private final double alphaContactDoseSv;
    private final double neutronYieldPerSec;
    private final double halfLifeSeconds;

    public IsotopeRadiationProfile(double activityBq, double gammaDoseConst, double betaDoseConst, double alphaContactDoseSv, double neutronYieldPerSec, double halfLifeSeconds) {
        this.activityBq = activityBq;
        this.gammaDoseConst = gammaDoseConst;
        this.betaDoseConst = betaDoseConst;
        this.alphaContactDoseSv = alphaContactDoseSv;
        this.neutronYieldPerSec = neutronYieldPerSec;
        this.halfLifeSeconds = halfLifeSeconds;
    }

    public IsotopeRadiationProfile(double activityBq, double gammaDoseConst, double betaDoseConst, double alphaContactDoseSv, double neutronYieldPerSec) {
        this(activityBq, gammaDoseConst, betaDoseConst, alphaContactDoseSv, neutronYieldPerSec, Double.POSITIVE_INFINITY);
    }

    public double getActivityBq() {
        return activityBq;
    }

    public double getGammaDoseConst() {
        return gammaDoseConst;
    }

    public double getBetaDoseConst() {
        return betaDoseConst;
    }

    public double getAlphaContactDoseSv() {
        return alphaContactDoseSv;
    }

    public double getNeutronYieldPerSec() {
        return neutronYieldPerSec;
    }

    public double getHalfLifeSeconds() {
        return halfLifeSeconds;
    }

    public boolean isRadioactive() {
        return activityBq > 0.0 || neutronYieldPerSec > 0.0;
    }
}
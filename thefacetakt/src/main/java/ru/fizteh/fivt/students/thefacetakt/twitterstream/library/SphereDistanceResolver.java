package ru.fizteh.fivt.students.thefacetakt.twitterstream.library;

import static java.lang.Math.toRadians;

/**
 * Created by thefacetakt on 06.10.15.
 */
class SphereDistanceResolver {
    static double sqr(double x) {
        return x * x;
    }

    //https://en.wikipedia.org/wiki/Great-circle_distance#Formulas
    static final double EARTH_RADIUS = 6371;

    static double sphereDistance(double phi1, double lambda1,
                                 double phi2, double lambda2) {
        phi1 = toRadians(phi1);
        phi2 = toRadians(phi2);
        lambda1 = toRadians(lambda1);
        lambda2 = toRadians(lambda2);

        double deltaPhi = phi2 - phi1;
        double deltaLambda = lambda2 - lambda1;

        return 2 * Math.asin(Math.sqrt(sqr(Math.sin(deltaPhi / 2))
                + Math.cos(phi1) * Math.cos(phi2)
                * sqr(Math.sin(deltaLambda / 2))))
                * EARTH_RADIUS;
    }
}

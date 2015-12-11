package ru.fizteh.fivt.students.zakharovas.twitterstream.library;

/**
 * Created by alexander on 04.11.15.
 */
public class RussianDeclension {
    public static String declensionWithNumber(int number, String[] endings) {
        number %= Numbers.HUNDRED;
        if (number >= Numbers.ELEVEN
                && number <= Numbers.NINETEEN) {
            return endings[0];
        }
        if (number % Numbers.TEN == Numbers.ZERO) {
            return endings[0];
        } else if (number % Numbers.TEN == Numbers.ONE) {
            return endings[1];
        } else if (number % Numbers.TEN >= Numbers.TWO
                && number % Numbers.TEN <= Numbers.FOUR) {
            return endings[2];
        } else {
            return endings[0];
        }
    }
}

package ru.fizteh.fivt.students.xmanatee.twittster;

class Word4declension {
    //секунда или секунду
    private static final int FIRST_BOUND = 2; //секунды
    private static final int SECOND_BOUND = 5; //секунд
    private static final int LEFT_BOUND = 6;
    private static final int RIGHT_BOUND = 21;
    private static final int GREAT_MODULO = 100;
    private static final int TINY_MODULO = 10;

    private String[] cases;

    Word4declension(String... inCases) {
        cases = inCases;
    }
    public String getCase(int caseNumber) {
        return cases[caseNumber];
    }

    public String declension4Number(long number) {
        number = number % GREAT_MODULO;
        if (LEFT_BOUND <= number && number < RIGHT_BOUND) {
            return cases[2];
        } else {
            number = number % TINY_MODULO;
            if (number == 0) {
                return cases[2];
            } else if (number < FIRST_BOUND) {
                return cases[0];
            } else if (number < SECOND_BOUND) {
                return cases[1];
            } else {
                return cases[2];
            }
        }
    }
}

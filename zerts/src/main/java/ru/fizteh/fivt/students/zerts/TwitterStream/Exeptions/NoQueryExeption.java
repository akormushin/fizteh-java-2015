package ru.fizteh.fivt.students.zerts.TwitterStream.Exeptions;

public class NoQueryExeption extends Exception {
    public NoQueryExeption() {
    }
    public NoQueryExeption(String message) {
        super(message);
    }
}

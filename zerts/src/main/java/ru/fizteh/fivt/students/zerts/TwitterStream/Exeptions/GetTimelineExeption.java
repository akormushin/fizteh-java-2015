package ru.fizteh.fivt.students.zerts.TwitterStream.Exeptions;

public class GetTimelineExeption extends Exception {
    public GetTimelineExeption() {
    }
    public GetTimelineExeption(String message) {
        super(message);
    }
}

package ru.fizteh.fivt.students.okalitova.moduletests.library;

import twitter4j.Status;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * Created by nimloth on 01.11.15.
 */
public class StatusParser {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_PURPLE =  "\u001b[35m";
    public static final int MILSEC_IN_SEC = 1000;
    private ParametersParser param;

    public StatusParser(ParametersParser paramDef) {
        param = paramDef;
    }

    public String getStatus(Status status) {
        StringBuilder stringStatus = new StringBuilder();
        if (!param.isStream()) {
            LocalDateTime statusTime = LocalDateTime
                    .ofEpochSecond(status.getCreatedAt().getTime() / MILSEC_IN_SEC,
                            MILSEC_IN_SEC, ZoneOffset.UTC);
            stringStatus.append(TimeParser
                            .getTime(statusTime,
                                    LocalDateTime.now(ZoneId.ofOffset("", ZoneOffset.UTC))));
        }
        if (status.isRetweet()) {
            if (!param.isHideRetwitts()) {
                stringStatus.append(ANSI_PURPLE)
                        .append("@").append(status.getUser().getScreenName())
                        .append(ANSI_RESET).append(": ретвитнул " + ANSI_PURPLE)
                        .append("@")
                        .append(status.getRetweetedStatus().getUser().getScreenName())
                        .append(ANSI_RESET).append(": ")
                        .append(status.getRetweetedStatus().getText());
            }
        } else {
            stringStatus.append(ANSI_PURPLE)
                    .append("@").append(status.getUser().getScreenName())
                    .append(ANSI_RESET).append(": ").append(status.getText());
            if (status.getRetweetCount() != 0) {
                stringStatus.append("(").append(status.getRetweetCount()).append(" ретвитов)");
            }
        }
        return stringStatus.toString();
    }
}

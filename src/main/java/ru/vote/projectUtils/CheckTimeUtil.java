package ru.vote.projectUtils;

import ru.vote.projectUtils.exception.IllegalRequestDataException;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

/**
разрешает или запрещает изменение голоса пользователем, в зависимости от тещего времени
 */
public class CheckTimeUtil {
    private static LocalTime timeToVote = LocalTime.parse("11:00:00");

    public static boolean checkTimeToVote(LocalTime localTime){
        try {
            return localTime.compareTo(timeToVote) <= 0;
        } catch (DateTimeParseException e) {
            throw new IllegalRequestDataException("Wrong parameter for time: "+ e);
        }
    }

    public static void setTimeToVote(String timeToVote) {
        CheckTimeUtil.timeToVote = LocalTime.parse(timeToVote);
    }

    public static LocalTime getTimeToVote() {
        return timeToVote;
    }
}

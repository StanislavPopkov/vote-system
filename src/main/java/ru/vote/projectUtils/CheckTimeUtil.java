package ru.vote.projectUtils;

import java.time.LocalTime;

//разрешает или запрещает изменение голоса пользователем, в зависимости от тещего времени
public class CheckTimeUtil {
    private static LocalTime timeToVote = LocalTime.parse("11:00:00");

    public static boolean checkTimeToVote(LocalTime localTime){
            return localTime.compareTo(timeToVote) <= 0;
    }

    public static void setTimeToVote(LocalTime timeToVote) {
        CheckTimeUtil.timeToVote = timeToVote;
    }

    public static LocalTime getTimeToVote() {
        return timeToVote;
    }
}

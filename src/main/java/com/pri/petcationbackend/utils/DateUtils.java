package com.pri.petcationbackend.utils;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@UtilityClass
public final class DateUtils {

    public static LocalDate convertToLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public long differenceInDays(Date from, Date to) {
        if (from == null || to == null) {
            return 0;
        }
        LocalDate fromLD = convertToLocalDate(from);
        LocalDate toLD = convertToLocalDate(to);
        LocalDate now = LocalDate.now();
        if (fromLD.isBefore(now) || toLD.isBefore(now) || toLD.isBefore(fromLD) ) {
            return 0;
        }
        return ChronoUnit.DAYS.between(fromLD, toLD);
    }
}

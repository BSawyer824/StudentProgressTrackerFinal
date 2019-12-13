package com.wgu_android.studentprogresstracker.Utilities;

import androidx.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ConvertTypes {

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    public static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd h:mm a z", Locale.getDefault());

    @TypeConverter
    public static Date fromDate(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long toDate(Date date) {
        return date == null ? null : date.getTime();
    }

    public static long fromTimeToLong(String dateInput) {

        try {
            Date date = dateFormat.parse(dateInput + TimeZone.getDefault().getDisplayName());
            return date.getTime();
        } catch (ParseException e) {
            return 0;
        }
    }

}

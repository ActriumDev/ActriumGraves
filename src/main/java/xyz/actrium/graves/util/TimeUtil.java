package xyz.actrium.graves.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeUtil {

    public static long convertStringToDuration(String input) {
        Pattern pattern = Pattern.compile("(\\d+)([shmdwy])");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            long value = Long.parseLong(matcher.group(1));
            switch (matcher.group(2)) {
                case "s":
                    return value * 1000L;
                case "m":
                    return value * 60L * 1000L;
                case "h":
                    return value * 60L * 60L * 1000L;
                case "d":
                    return value * 24L * 60L * 60L * 1000L;
                case "w":
                    return value * 7L * 24L * 60L * 60L * 1000L;
                case "y":
                    return value * 365L * 24L * 60L * 60L * 1000L;
                default:
                    return Long.MIN_VALUE;
            }
        } else {
            return Long.MIN_VALUE;
        }
    }

    public static String convertLongToReadableDuration(long durationInMillis) {
        if (durationInMillis == Long.MAX_VALUE) {
            return "Forever";
        } else {
            long years = TimeUnit.MILLISECONDS.toDays(durationInMillis) / 365L;
            long months = TimeUnit.MILLISECONDS.toDays(durationInMillis) / 30L % 12L;
            long days = TimeUnit.MILLISECONDS.toDays(durationInMillis) % 30L;
            long hours = TimeUnit.MILLISECONDS.toHours(durationInMillis) % 24L;
            long minutes = TimeUnit.MILLISECONDS.toMinutes(durationInMillis) % 60L;
            long seconds = TimeUnit.MILLISECONDS.toSeconds(durationInMillis) % 60L;
            StringBuilder result = new StringBuilder();
            if (years > 0L) {
                result.append(years).append(" Year");
                if (years > 1L) {
                    result.append("s");
                }

                result.append(" ");
            }

            if (months > 0L) {
                result.append(months).append(" Month");
                if (months > 1L) {
                    result.append("s");
                }

                result.append(" ");
            }

            if (days > 0L) {
                result.append(days).append(" Day");
                if (days > 1L) {
                    result.append("s");
                }

                result.append(" ");
            }

            if (hours > 0L) {
                result.append(hours).append(" Hour");
                if (hours > 1L) {
                    result.append("s");
                }

                result.append(" ");
            }

            if (minutes > 0L) {
                result.append(minutes).append(" Minute");
                if (minutes > 1L) {
                    result.append("s");
                }

                result.append(" ");
            }

            if (seconds > 0L) {
                result.append(seconds).append(" Second");
                if (seconds > 1L) {
                    result.append("s");
                }

                result.append(" ");
            }

            if (result.length() == 0) {
                result.append("0 Seconds");
            }

            return result.toString().trim();
        }
    }

    public static String convertLongToRemainingDuration(long createdAtInMillis, long durationInMillis) {
        long currentTimeMillis = System.currentTimeMillis();
        long remainingTimeMillis = createdAtInMillis + durationInMillis - currentTimeMillis;
        long years = TimeUnit.MILLISECONDS.toDays(remainingTimeMillis) / 365L;
        long months = TimeUnit.MILLISECONDS.toDays(remainingTimeMillis) / 30L % 12L;
        long days = TimeUnit.MILLISECONDS.toDays(remainingTimeMillis) % 30L;
        long hours = TimeUnit.MILLISECONDS.toHours(remainingTimeMillis) % 24L;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(remainingTimeMillis) % 60L;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(remainingTimeMillis) % 60L;
        StringBuilder result = new StringBuilder();
        if (years > 0L) {
            result.append(years).append(" Year");
            if (years > 1L) {
                result.append("s");
            }

            result.append(" ");
        }

        if (months > 0L) {
            result.append(months).append(" Month");
            if (months > 1L) {
                result.append("s");
            }

            result.append(" ");
        }

        if (days > 0L) {
            result.append(days).append(" Day");
            if (days > 1L) {
                result.append("s");
            }

            result.append(" ");
        }

        if (hours > 0L) {
            result.append(hours).append(" Hour");
            if (hours > 1L) {
                result.append("s");
            }

            result.append(" ");
        }

        if (minutes > 0L) {
            result.append(minutes).append(" Minute");
            if (minutes > 1L) {
                result.append("s");
            }

            result.append(" ");
        }

        if (seconds > 0L) {
            result.append(seconds).append(" Second");
            if (seconds > 1L) {
                result.append("s");
            }

            result.append(" ");
        }

        if (result.length() == 0) {
            result.append("0 Seconds");
        }

        return result.toString().trim();
    }

    public static String convertLongToReadableDate(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        return dateFormat.format(date);
    }
}

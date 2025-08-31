package org.example.taskpilot_taskmanager.task.utils;

import org.springframework.stereotype.Component;

import java.time.Duration;


@Component
public class DurationFormatterUtil {

    public static String formatDuration(Duration duration) {
        if (duration==null || duration.isZero()) return "0 seconds";
        long totalSeconds = duration.getSeconds();

        long days = totalSeconds / (24 * 3600);
        long hours = (totalSeconds % (24 * 3600)) / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        StringBuilder sb = new StringBuilder();
        if (days > 0) sb.append(days).append(days==1 ? " day " :" days ");
        if (hours > 0) sb.append(hours).append(hours==1 ? " hour " : " hours ");
        if (minutes > 0) sb.append(minutes).append(minutes==1 ? " minute " : " minutes ");
        if (seconds > 0 || sb.isEmpty()) sb.append(seconds).append(seconds==1 ? " second " : " seconds ");

        return sb.toString().trim();
    }



    public static String generateTimeSummaryMessage(String entityName, Duration totalTimeTaken, Duration expectedDuration) {
        StringBuilder message = new StringBuilder();

        // Total time formatting
        String formattedTotalTime = formatDuration(totalTimeTaken);
        message.append("Total time taken for the ").append(entityName.toLowerCase()).append(": ").append(formattedTotalTime).append(". ");

        // Calculate difference
        Duration difference = totalTimeTaken.minus(expectedDuration);
        String formattedDifference = formatDuration(difference.abs());

        if (difference.isZero()) {
            message.append("It was completed exactly as expected.");
        } else if (difference.isNegative()) {
            message.append("It was completed ").append(formattedDifference).append(" before the expected time.");
        } else {
            message.append("It was completed ").append(formattedDifference).append(" after the expected time.");
        }

        return message.toString();
    }



}

package us.thehealthyway.healthywayadminandroid;

public class ConstantsHTMLforEmail {
    public static String LANDSCAPE = " (easier to read in landscape mode) ";
    public static String JOURNAL_DAY_HEADER =
            "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "<title>Healthy Way</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<h2>Journal</h2>\n" +
                    "<h3>HW_EMAIL_INSTRUCTION</h3>\n"
            ;
    public static String JOURNAL_DAILY_TOTALS_ROW =
            "<p>HW_RECORDED_DATE</p>\n" +
                    "<table style=\"border: 1px solid black;border-collapse: collapse;\">\n" +
                    "<tr>\n" +
                    "<th style=\"width: 17%; border: 1px solid black;border-collapse: collapse;\"><font size =\"1\">Meal</th>\n" +
                    "<th style=\"width: 30%; border: 1px solid black;border-collapse: collapse;\"><font size =\"1\">Food eaten</th>\n" +
                    "<th style=\"width: 7%; border: 1px solid black;border-collapse: collapse;\"><font size =\"1\">P</th>\n" +
                    "<th style=\"width: 7%; border: 1px solid black;border-collapse: collapse;\"><font size =\"1\">S</th>\n" +
                    "<th style=\"width: 7%; border: 1px solid black;border-collapse: collapse;\"><font size =\"1\">V</th>\n" +
                    "<th style=\"width: 7%; border: 1px solid black;border-collapse: collapse;\"><font size =\"1\">Fr</th>\n" +
                    "<th style=\"width: 7%; border: 1px solid black;border-collapse: collapse;\"><font size =\"1\">F</th>\n" +
                    "<th style=\"width: 18%; border: 1px solid black;border-collapse: collapse;\"><font size =\"1\">Feelings/Comments</th>\n" +
                    "</tr>\n" +
                    "<tr>\n" +
                    "<td style=\"width: 17%; border: 1px solid black;border-collapse: collapse;\"><font size =\"1\">Daily Totals</td>\n" +
                    "<td style=\"width: 30%; border: 1px solid black;border-collapse: collapse;\"> </td>\n" +
                    "<td style=\"width: 7%; border: 1px solid black;border-collapse: collapse;\"><font size =\"1\">HW_DAILY_TOTAL_PROTEIN_VALUE</td>\n" +
                    "<td style=\"width: 7%; border: 1px solid black;border-collapse: collapse;\"><font size =\"1\">HW_DAILY_TOTAL_STARCH_VALUE</td>\n" +
                    "<td style=\"width: 7%; border: 1px solid black;border-collapse: collapse;\"><font size =\"1\">HW_DAILY_TOTAL_VEGGIES_VALUE</td>\n" +
                    "<td style=\"width: 7%; border: 1px solid black;border-collapse: collapse;\"><font size =\"1\">HW_DAILY_TOTAL_FRUIT_VALUE</td>\n" +
                    "<td style=\"width: 7%; border: 1px solid black;border-collapse: collapse;\"><font size =\"1\">HW_DAILY_TOTAL_FAT_VALUE</td>\n" +
                    "<td style=\"width: 18%; border: 1px solid black;border-collapse: collapse;\"> </td>\n" +
                    "</tr>\n"
            ;
    public static String JOURNAL_MEAL_ROW =
            "<tr>\n" +
                    "<td style=\"width: 17%; border: 1px solid black;border-collapse: collapse;\"><font size =\"1\">HW_MEAL_NAME</td>\n" +
                    "<td style=\"width: 30%; border: 1px solid black;border-collapse: collapse;\"><font size =\"1\">HW_MEAL_CONTENTS_DESCRIPTION</td>\n" +
                    "<td style=\"width: 7%; border: 1px solid black;border-collapse: collapse;\"><font size =\"1\">HW_MEAL_PROTEIN_COUNT</td>\n" +
                    "<td style=\"width: 7%; border: 1px solid black;border-collapse: collapse;\"><font size =\"1\">HW_MEAL_STARCH_COUNT</td>\n" +
                    "<td style=\"width: 7%; border: 1px solid black;border-collapse: collapse;\"><font size =\"1\">HW_MEAL_VEGGIES_COUNT</td>\n" +
                    "<td style=\"width: 7%; border: 1px solid black;border-collapse: collapse;\"><font size =\"1\">HW_MEAL_FRUIT_COUNT</td>\n" +
                    "<td style=\"width: 7%; border: 1px solid black;border-collapse: collapse;\"><font size =\"1\">HW_MEAL_FAT_COUNT</td>\n" +
                    "<td style=\"width: 18%; border: 1px solid black;border-collapse: collapse;\"><font size =\"1\">HW_MEAL_COMMENTS</td>\n" +
                    "</tr>\n"
            ;
    public static String JOURNAL_DATE_TOTALS =
            "<tr>\n" +
                    "<td style=\"width: 17%; border: 1px solid black;border-collapse: collapse;\"><font size =\"1\">Totals</td>\n" +
                    "<td style=\"width: 30%; border: 1px solid black;border-collapse: collapse;\"> </td>\n" +
                    "<td style=\"width: 7%; border: 1px solid black;border-collapse: collapse;\"><font size =\"1\">HW_DATE_TOTAL_PROTEIN</td>\n" +
                    "<td style=\"width: 7%; border: 1px solid black;border-collapse: collapse;\"><font size =\"1\"; color=\"HW_TOTAL_STARCH_COLOR\">HW_DATE_TOTAL_STARCH</font></td>\n" +
                    "<td style=\"width: 7%; border: 1px solid black;border-collapse: collapse;\"><font size =\"1\">HW_DATE_TOTAL_VEGGIES</td>\n" +
                    "<td style=\"width: 7%; border: 1px solid black;border-collapse: collapse;\"><font size =\"1\"; color=\"HW_TOTAL_FRUIT_COLOR\">HW_DATE_TOTAL_FRUIT</font></td>\n" +
                    "<td style=\"width: 7%; border: 1px solid black;border-collapse: collapse;\"><font size =\"1\"; color=\"HW_TOTAL_FAT_COLOR\">HW_DATE_TOTAL_FAT</font></td>\n" +
                    "<td style=\"width: 18%; border: 1px solid black;border-collapse: collapse;\"> </td>\n" +
                    "</tr>\n" +
                    "</table>\n"
            ;
    public static String JOURNAL_DATE_STATS =
            "<font size=\"1\">     Water: HW_DATE_WATER_CHECKS Supplements: HW_DATE_SUPPLEMENTS_CHECKS Exercise: HW_DATE_EXERCISE_CHECKS</font>\n" +
                    "<p>"
            ;
    public static String JOURNAL_DATE_COMMENTS = "<font size=\"1\">HW_COMMENTS</font>";
    public static String JOURNAL_DATE_TRAILER =
            "</p>\n" +
                    "</body>\n" +
                    "</html>\n"
            ;

}

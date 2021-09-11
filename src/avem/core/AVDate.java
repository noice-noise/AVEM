package avem.core;

import java.util.Calendar;
import java.util.StringTokenizer;

public class AVDate {


    private String month;
    private String day;
    private String year;

    public AVDate(String month, String day, String year) {
        verifyDateData(month, day, year);
        this.month = month;
        this.day = day;
        this.year = year;
    }

    //  7/2/2020    8 chars
    //  12/2/2020   9 chars
    //  12/12/2020  10 chars

    /***
     * Allows easy implementation using String when parsing the data from the parent Event Manager.
     * @param simplifiedDateFormat
     */

    public AVDate(String simplifiedDateFormat) {

        boolean ok = isValid(simplifiedDateFormat);

        if (isValid(simplifiedDateFormat)) {
            int traverseCounter = 0;
            String toParse;
            StringTokenizer st = new StringTokenizer(simplifiedDateFormat);
            while (st.hasMoreTokens()) {
                toParse = st.nextToken("/");

                if (traverseCounter == 0) {
                    this.month = toParse;
                } else if (traverseCounter == 1) {
                    this.day = toParse;
                } else if (traverseCounter == 2) {
                    this.year = toParse;
                }
                traverseCounter++;
            }

        } else {
            throw new IllegalArgumentException("Date format not valid. Must be MM/DD/YYYY");
        }
    }

    private boolean isValid(String simplifiedDate) {
        boolean isBetweenEightAndTen = isBetweenEightAndTen(simplifiedDate);
        boolean hasOneOrTwoCharactersBeforeDelimiter = hasOneOrTwoCharactersBeforeDelimiter(simplifiedDate);

        return isBetweenEightAndTen && hasOneOrTwoCharactersBeforeDelimiter;
    }



    private boolean isBetweenEightAndTen(String simplifiedTimeFormat) {
        return simplifiedTimeFormat.length() >= 8 && simplifiedTimeFormat.length() <= 10;
    }

    // 12/2/2013     8 chars
    // 12/12/2020   10 chars

    private boolean hasOneOrTwoCharactersBeforeDelimiter(String simplifiedTimeFormat) {
        String contents = simplifiedTimeFormat;
        String toParse;
        char[] traversedChars = {};

        int traverseCounter = 0;

        StringTokenizer st = new StringTokenizer(contents);
        while (st.hasMoreTokens()) {
            toParse = st.nextToken("/");
            traversedChars = toParse.toCharArray();
            if (traverseCounter < 2) {
                if (toParse.length() < 1 || toParse.length() > 2) {
                    return false;
                }
            } else if (traverseCounter == 2) {
                if (toParse.length() != 4) {
                    return false;
                }
            }
            traverseCounter++;
        }

        return true;
    }

    private void verifyDateData(String month, String day, String year) {
        if (month.length() < 1 || month.length() > 2) {
            throw new IllegalArgumentException("Month must be between 1 or 2 characters");
        } else  if (day.length() < 1 || day.length() > 2) {
            throw new IllegalArgumentException("Day must be between 1 or 2 characters");
        } else if (year.length() != 4) {
            throw new IllegalArgumentException("Year must be in 4-character format");
        }
    }

    public String getCurrentYear(){
        String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        return currentYear;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AVDate && obj != null) {
            AVDate avDate = (AVDate) obj;
            if (this.month.equals(avDate.getMonth()) &&
                    this.day.equals(avDate.getDay()) &&
                        this.year.equals(avDate.year)
            ) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return  this.month + "/" + this.day + "/" + this.year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}

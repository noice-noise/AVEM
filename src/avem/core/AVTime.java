package avem.core;


import java.util.StringTokenizer;

/***
 * AVEM-customized class for and simplified implementation learning purposes.
 * Features:
 *      12-hour format for easy access for computers and humans (users and programmers of course).
 *      allows easy instance with String with isValid() checker. i.e. new AVTime("12:34 AM")
 *          to ease implementation complexity.
 */
public class AVTime {

    private String hours;
    private String minutes;
    private String timeOfDay;

    public AVTime(String hours, String minutes, String timeOfDay) {
        this.hours = hours;
        this.minutes = minutes;
        this.timeOfDay = timeOfDay;
    }

    /***
     * Create time using a simple string.
     * Make sure that the AM/PM (timeOfDay) has preceding space.
     * Characters must be 4-5 only.
     * Example: "1:59 PM" -> new AVTime("1:59 PM)
     *
     * @param simplifiedTimeFormat
     */
    public AVTime(String simplifiedTimeFormat) {
        boolean ok = isValid(simplifiedTimeFormat);
//        System.out.println("OK: " + ok);
        if (ok) {
            if (simplifiedTimeFormat.length() == 8) {
//                System.out.println("Valid Simlified Time Format..");
                int count = 0;
                char[] timeChars = simplifiedTimeFormat.toCharArray();
                this.hours = "" + timeChars[count++] +  timeChars[count++];
                count++;    // to exlude ':'
                this.minutes = "" + timeChars[count++] +  timeChars[count++];
                count++;    // to exlude ' '
                this.timeOfDay = "" + timeChars[count++] +  timeChars[count++];
//                System.out.println(getData());
            } else if (simplifiedTimeFormat.length() == 7) {
//                System.out.println("Valid Simlified Time Format..");
                int count = 0;
                char[] timeChars = simplifiedTimeFormat.toCharArray();
                this.hours = "" + timeChars[count++];
                count++;    // to exlude ':'
                this.minutes = "" + timeChars[count++] +  timeChars[count++];
                count++;    // to exlude ' '
                this.timeOfDay = "" + timeChars[count++] +  timeChars[count++];
//                System.out.println(getData());
            }
        } else {
            throw new NullPointerException("Time format must be 12-hour hh:mm aa (hour:minutes AM/PM");
        }
    }

    private boolean isValid(String simplifiedTimeFormat) {
        return isBetweenFourToFiveCharacters(simplifiedTimeFormat) &&
                validTimeOfDayID(simplifiedTimeFormat) &&
                hasOneOrTwoCharactersBeforeSpace(simplifiedTimeFormat) &&
                hasOneOrTwoCharactersBeforeDelimiter(simplifiedTimeFormat);
    }

    private boolean hasOneOrTwoCharactersBeforeSpace(String simplifiedTimeFormat) {
//        System.out.println("simplifiedDate: " + simplifiedTimeFormat);
        String contents = simplifiedTimeFormat;
        String toParse;
        char[] traversedChars = {};

        StringTokenizer st = new StringTokenizer(contents);
        while (st.hasMoreTokens()) {
            toParse = st.nextToken(" ");
            traversedChars = toParse.toCharArray();

            if (simplifiedTimeFormat.length() == 8 && traversedChars.length > 4) {
//                System.out.println("[IN1] traversedChars: " + String.copyValueOf(traversedChars));
                //need to break or else the next parse will be the AM/PM
                break;
            } else if (simplifiedTimeFormat.length() == 7 && traversedChars.length > 3) {
//                System.out.println("[IN2] traversedChars: " + String.copyValueOf(traversedChars));
                break;
            }
        }

//        System.out.println("traversedChars: " + String.copyValueOf(traversedChars));

//        System.out.println("len-3: " + traversedChars[traversedChars.length - 3]);
//        System.out.println("len-3: " + traversedChars[traversedChars.length - 1]);
        if (traversedChars[traversedChars.length - 3] == ':') {
            return true;
        }
        else {
            return false;
        }
    }

    private boolean validTimeOfDayID(String simplifiedTimeFormat) {

//        System.out.println(simplifiedTimeFormat);
        char[] toVerify = simplifiedTimeFormat.toCharArray();
//        System.out.println("toVerify: " +   String.copyValueOf(toVerify));
        int len = toVerify.length;
        char[] lastTwoCharacters = {toVerify[len - 2], toVerify[len - 1]};
//        System.out.println("\nlastTwoCharacters: " + String.copyValueOf(lastTwoCharacters));

        char mustSpace = toVerify[len - 3];

        if (( String.copyValueOf(lastTwoCharacters).equals("AM") ||
                String.copyValueOf(lastTwoCharacters).equals("PM")) &&
                mustSpace == ' ') {
//            System.out.println("[VALID] TIME OF DAY");
            return true;

        } else {

//            System.out.println("[FALSE] validTimeOfDayID");
//            System.out.println("\nlastTwoCharacters: " + lastTwoCharacters);
//            System.out.println("\nmustSpace: " + mustSpace);
            return false;
        }
    }

    private boolean hasOneOrTwoCharactersBeforeDelimiter(String simplifiedTimeFormat) {
//        System.out.println(("hasOneOrTwoCharactersBeforeDelimiter") + simplifiedTimeFormat);
        String contents = simplifiedTimeFormat;
        String toParse;

        StringTokenizer st = new StringTokenizer(contents);
        while (st.hasMoreTokens()) {
            toParse = st.nextToken(":");
//            System.out.println("toParse: " + toParse);
            if (toParse.length() >= 1 && toParse.length() <= 2) {
//                System.out.println("[OVRR TRUE] hasOneOrTwoCharactersBeforeDelimiter");
                return true;
            }
        }
//        System.out.println("[FALSE] hasOneOrTwoCharactersBeforeDelimiter");
        return false;
    }
            // 1:43
    private boolean isBetweenFourToFiveCharacters(String simplifiedTimeFormat) {
//        System.out.println("[" + (simplifiedTimeFormat.length() < 4 || simplifiedTimeFormat.length() > 5) + "]" + "four-five func");
        return simplifiedTimeFormat.length() < 4 || simplifiedTimeFormat.length() > 5;
    }



    /***
     * Returns the variable and stored time data.
     * @return
     */
    public String getData() {
        return "AVTime{" +
                "hours='" + hours + '\'' +
                ", minutes='" + minutes + '\'' +
                ", timeOfDay='" + timeOfDay + '\'' +
                '}';
    }

    @Override
    public String toString() {
        return this.hours + ":" + this.minutes + " " + this.timeOfDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AVTime avTime = (AVTime) o;
        return this.hours.equals(avTime.getHours()) && this.minutes.equals(avTime.getMinutes()) && this.timeOfDay.equals(avTime.getTimeOfDay());
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public String getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(String timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public int getTwentyFourHour() {
        if (this.timeOfDay.equals("AM")) {
            if (this.hours.equals("12")) {
                return Integer.parseInt(this.hours) - 12;
            } else {
                return Integer.parseInt(this.hours);
            }
        } else if (this.timeOfDay.equals("PM")) {
            if (this.hours.equals("12")) {
                return Integer.parseInt(this.hours);
            } else {
                return Integer.parseInt(this.hours) + 12;
            }
        } else {
            return -1;
        }
    }
}

package avem.core;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AVReservationManager {


    private File reservationManagerDIR;
    private File reservationManagerFile;

    private HashMap<String, AVReservation> reservationEntries;

    private ArrayList<AVReservation> sortedReservations;


    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private DateFormat timeFormatter = new SimpleDateFormat("hh:mm a");


    public AVReservationManager() {
        initialize();
    }

    private void initialize() {
        initDirectoryWithPath("reservation-manager");
        createReservationEntries();
        createSortedReservations();
        createDirectory();
        createFile();
        parseFileContents();
        syncMomentReservationStatus();
    }

    private void syncMomentReservationStatus() {
        try {
            processToday();
            processFinished();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processFinished() {
        LocalDate today = LocalDate.now();
        AVDate avToday = new AVDate(
                String.valueOf(today.getMonthValue()),
                String.valueOf(today.getDayOfMonth()),
                String.valueOf(today.getYear()));

        HashMap<String, AVReservation> reservationsToday = getReservationsFor(avToday);

        LocalTime now = LocalTime.now();

        for (AVReservation r :
                reservationsToday.values()) {
            if (isFinished(r, now)) {
                r.getEvent().setEventStatus(AVEvent.FINISHED_STATUS);
            }
        }
    }

    private boolean isFinished(AVReservation r, LocalTime now) {

        AVTime avStart = r.getEvent().getEventStartTime();
        AVTime avEnd = r.getEvent().getEventEndTime();

        LocalTime start = LocalTime.of(avStart.getTwentyFourHour(), Integer.valueOf(avStart.getMinutes()));
        LocalTime end = LocalTime.of(avEnd.getTwentyFourHour(), Integer.valueOf(avEnd.getMinutes()));

        if (r.getEvent().getEventStatus().equals(AVEvent.RESERVED_STATUS) ||
                r.getEvent().getEventStatus().equals(AVEvent.ONGOING_STATUS)) {
            if (now.isAfter(end) || (now.equals(end))) {
                return true;
            }
        }

        return false;
    }

    private void processToday() {
        LocalDate today = LocalDate.now();
        AVDate avToday = new AVDate(
                String.valueOf(today.getMonthValue()),
                String.valueOf(today.getDayOfMonth()),
                String.valueOf(today.getYear()));

        HashMap<String, AVReservation> reservationsToday = getReservationsFor(avToday);

        LocalTime now = LocalTime.now();

        for (AVReservation r :
                reservationsToday.values()) {
            if (isOngoing(r, now)) {
                r.getEvent().setEventStatus(AVEvent.ONGOING_STATUS);
            }
        }
    }

    private boolean isOngoing(AVReservation r, LocalTime now) {

        AVTime avStart = r.getEvent().getEventStartTime();
        AVTime avEnd = r.getEvent().getEventEndTime();

        LocalTime start = LocalTime.of(avStart.getTwentyFourHour(), Integer.valueOf(avStart.getMinutes()));
        LocalTime end = LocalTime.of(avEnd.getTwentyFourHour(), Integer.valueOf(avEnd.getMinutes()));

        if (now.isAfter(start) && now.isBefore(end) || (now.equals(start))) {
            return true;
        } else {
            return false;
        }
    }

    private void createSortedReservations() {
        sortedReservations = new ArrayList<>();
    }


    public void resetAllFilesAndDirectory() {
        deleteFiles();
        createDirectory();
        createFile();
    }

    private boolean createFile() {
        reservationManagerFile = new File(reservationManagerDIR, "reservation-info.data");
        try {
            if (fileExists()) {
            } else {
                reservationManagerFile.createNewFile();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean fileExists() {
        return reservationManagerFile.exists();
    }

    public boolean createDirectory() {
        if (directoryExists()) {
            return false;
        } else {
            reservationManagerDIR.mkdir();
            return true;
        }
    }

    public boolean directoryExists() {
        return reservationManagerDIR.exists();
    }

    private void initDirectoryWithPath(String directoryName) {
        reservationManagerDIR = new File(directoryName);
    }


    private void deleteFiles() {
        reservationManagerFile.delete();
        reservationManagerDIR.delete();
    }

    public boolean isEmptyFile() {
        return reservationManagerFile.length() == 0;
    }


    private void createReservationEntries() {
        reservationEntries = new HashMap<>();
    }

    public File getDirectory() {
        return reservationManagerDIR;
    }

    public File getFile() {
        return reservationManagerFile;
    }

    /***
     *
     * @param toGet
     */
    public AVReservation getReservation(AVReservation toGet) {
        if (toGet == null) {
            throw new IllegalArgumentException("Invalid argument, course must not be null");
        }
        parseFileContents();
        return reservationEntries.get(toGet.getReservationID());
    }

    /***
     *
     * @param toAdd
     */
    public void addReservation(AVReservation toAdd) {
        reservationEntries.put(toAdd.getReservationID(), toAdd);
        updateReservationManagerFile();
    }

    /***
     *
     * @param toRemove
     */
    public void removeReservation(AVReservation toRemove) {
        if (toRemove != null) {
            reservationEntries.remove(toRemove.getReservationID(), toRemove);
            updateReservationManagerFile();
        } else {
            throw new IllegalArgumentException("Argument Invalid.");
        }
    }

    /***
     *
     * @param toEdit
     */
    public void updateReservation(AVReservation toEdit, AVReservation withThisAVEquipment) {

        AVReservation parsed = reservationEntries.get(toEdit.getReservationID());

        if (parsed == null) {
            throw new IllegalArgumentException("toEdit Reservation must not be null.");
        } else {
            reservationEntries.remove(toEdit.getReservationID());
            reservationEntries.put(withThisAVEquipment.getReservationID(), withThisAVEquipment);
        }
        updateReservationManagerFile();
    }

    public void updateReservationManagerFile() {
        try {
            String contents = getFormattedReservation();
            FileWriter fw = new FileWriter(reservationManagerFile);
            fw.append(contents);
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFormattedReservation() {
        String contents = "";
        try {
            for (AVReservation avr :
                    reservationEntries.values()) {
                contents += "<" +
                        avr.getFormattedString()
                        + ">"
                ;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return contents;
    }

    public void parseFileContents() {
        try {
            String contents;
            String toParse;
            contents = getFileContents();
            StringTokenizer st = new StringTokenizer(contents);
            while (st.hasMoreTokens()) {
                toParse = st.nextToken("<>");
                if (toParse.length() > 1) {
                    AVReservation avReservation = parseReservationFrom(toParse.trim());
                    reservationEntries.put(avReservation.getReservationID(), avReservation);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFileContents() {
        String contents = "";

        try {
            FileReader fr = new FileReader(reservationManagerFile);
            int currentCharacter = 0;
            while ((currentCharacter = fr.read()) != -1) {
                contents += (char) currentCharacter;
            }
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return contents;
    }

    private AVReservation parseReservationFrom(String toParse) {

        StringTokenizer st = new StringTokenizer(toParse, "<>");
        AVEvent parsedEvent = null;
        HashMap<String, AVEquipment> equipmentBundle = new HashMap<>();
        String equipmentStatus = "";

        while (st.hasMoreTokens()) {

            String token = st.nextToken();


            parsedEvent = parseEventFrom(token);
            equipmentBundle = parseEquipmentBundleFrom(token);
            equipmentStatus = parseEquipmentStatusFrom(token);

        }

        AVReservation avReservation = new AVReservation(parsedEvent, equipmentBundle);
        avReservation.setEquipmentStatus(equipmentStatus);
        return avReservation;
    }

    private String parseEquipmentStatusFrom(String toParseStatus) {
        String eventDelimiter = "/\\";
        StringTokenizer st = new StringTokenizer(toParseStatus, eventDelimiter);
        String eqBundleString = "";

        while (st.hasMoreTokens()) {
            eqBundleString = st.nextToken();
        }

        return eqBundleString;
    }

    private HashMap<String, AVEquipment> parseEquipmentBundleFrom(String toParseEqBundle) {
        String eventDelimiter = "()";
        StringTokenizer st = new StringTokenizer(toParseEqBundle, eventDelimiter);

        // get 2nd token for it is the EQUIPMENT BUNDLE
        st.nextToken(); //skip event token
        String eqBundleString = st.nextToken(); //equipment bundle token

        eventDelimiter = "[]";
        st = new StringTokenizer(eqBundleString, eventDelimiter);

        HashMap<String, AVEquipment> parsedEqBundle = new HashMap<>();

        // get equipment bundle
        while (st.hasMoreTokens()) {

            String anEquipment = st.nextToken();
            StringTokenizer est = new StringTokenizer(anEquipment, "\n");
            ArrayList<String> eqVar = new ArrayList<>();


            while (est.hasMoreTokens()) {
                String var = est.nextToken();
                eqVar.add(var);
            }

            int count = 1;
            AVEquipment avEquipment = new AVEquipment(
                    eqVar.get(count++),
                    eqVar.get(count++),
                    eqVar.get(count++),
                    eqVar.get(count++),
                    eqVar.get(count++),
                    eqVar.get(count++),
                    eqVar.get(count++),
                    Boolean.valueOf(eqVar.get(count++)));

            parsedEqBundle.put(avEquipment.getEquipmentID(), avEquipment);

        }

        return parsedEqBundle;
    }

    private AVEvent parseEventFrom(String toParseEvent) {

        String eventDelimiter = "{}";
        StringTokenizer st = new StringTokenizer(toParseEvent, eventDelimiter);

        String eventString = st.nextToken();


        eventDelimiter = "\n";
        st = new StringTokenizer(eventString, eventDelimiter);

        ArrayList<String> eventVars = new ArrayList<>();

        while (st.hasMoreTokens()) {
            String anEventVar = st.nextToken();
            eventVars.add(anEventVar);

        }

        int count = 1;  // starts with one to skip the ID (it will be generated automatically)

        // refer AVEvent constructor for the arrangement of instance
        AVEvent avEvent = new AVEvent(
                eventVars.get(count++),
                new AVDate(eventVars.get(count++)),
                new AVTime(eventVars.get(count++)),
                new AVTime(eventVars.get(count++)),
                eventVars.get(count++),
                eventVars.get(count++),
                eventVars.get(count++),
                eventVars.get(count++),
                eventVars.get(count++),
                eventVars.get(count++),
                eventVars.get(count++),
                eventVars.get(count++));

        return avEvent;
    }


    public boolean isEmptyReservations() {
        return reservationEntries.isEmpty();
    }

    public boolean isAllEmpty() {
        return isEmptyReservations() && isEmptyFile();
    }

    public Integer reservationSize() {
        return reservationEntries.size();
    }

    public HashMap<String, AVReservation> getReservationsFor(AVDate avDate) {

        HashMap<String, AVReservation> datesReservation = new HashMap<>();

        for (AVReservation avr :
                reservationEntries.values()) {
            if (avr.getEvent().getEventDate().equals(avDate)) {

                datesReservation.put(avr.getReservationID(), avr);
            }
        }

        for (AVReservation av :
                datesReservation.values()) {
        }

        return datesReservation;
    }

    private ArrayList<AVReservation> sortByTime(HashMap<String, AVReservation> datesReservation) {

        ArrayList<AVReservation> sortedReservationByTime = new ArrayList<>();

        // get time list arrangement
        List<Date> timeList = new ArrayList<>();
        try {
            for (AVReservation av :
                    datesReservation.values()) {
                AVTime currT = av.getEvent().getEventStartTime();
                Date time = timeFormatter.parse(currT.toString());
                timeList.add(time);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        timeList = removeDuplicateTime(timeList);

        // sort time list arrangement
        Collections.sort(timeList);

        // find compatible reservation per time item then add to sortedReservationByTime
        for (Date currentTime :
                timeList) {

            // format currentTime to AVTime compatible
            String ft = timeFormatter.format(currentTime).toUpperCase(Locale.ROOT);

            AVTime avTime;
            if (ft.charAt(0) == '0') {
                avTime = new AVTime(ft.substring(1));
            } else {
                avTime = new AVTime(ft);
            }


            for (AVReservation r :
                    datesReservation.values()) {
                if (r.getEvent().getEventStartTime().equals(avTime)) {

                    sortedReservationByTime.add(r);
                }
            }
        }

        return sortedReservationByTime;
    }

    private List<Date> removeDuplicateTime(List<Date> timeList) {
        List<Date> timeList2 = new ArrayList<>();

        for (Date d :
                timeList) {
            if (!timeList2.contains(d)) {
                timeList2.add(d);
            }
        }

        return timeList2;
    }

    /**
     * Note that the key for finding the Reservations for the date is an Integer value of "DAY"
     * i.e: 7/21/2021   - You can access all the reservations by using the key : '21'
     * so, someHashMap.get(21) which will return another HashMap<String, AVReservations>
     *
     * @param startDate
     * @param endDate
     * @return
     */

    public HashMap<Integer, ArrayList<AVReservation>> getReservationRanging(AVDate startDate, AVDate endDate) {

        HashMap<Integer, ArrayList<AVReservation>> datesReservations = new HashMap<>();
        ArrayList<AVReservation> currentDayHashmap = new ArrayList<>();

        int currentDay = Integer.parseInt(startDate.getDay());
        AVDate dayCounter = startDate;

        String endDatePlusOne = String.valueOf(Integer.parseInt(endDate.getDay()));
        AVDate dayLimit = new AVDate(endDate.getMonth(), endDatePlusOne, endDate.getYear());

        for (AVReservation currReservation :
                reservationEntries.values()) {
            boolean reachedDayLimit = dayCounter.equals(dayLimit);

            for (AVReservation curr2Reservation :
                    reservationEntries.values()) {
                boolean isCurrentReservationMatchesCurrentDayCounter = curr2Reservation.getEvent().getEventDate().equals(dayCounter);

                if (isCurrentReservationMatchesCurrentDayCounter) {
                   currentDayHashmap.add(curr2Reservation);
                }

            }

            datesReservations.put(currentDay, currentDayHashmap);
            currentDayHashmap = new ArrayList<>();

            String moveNextDay = String.valueOf(currentDay + 1);
            dayCounter.setDay(moveNextDay);

            if (reachedDayLimit) {
                break;
            }

            currentDay++;
        }

        return datesReservations;
    }


    public void sortReservations() {
        sortedReservations.clear();

        List<LocalDate> datesCollection = getReservationDates();
        sortDate(datesCollection);
    }

    private void sortDate(List<LocalDate> reservationDates) {
        try {
            for (LocalDate dayCounter :
                    reservationDates) {
                AVDate dateToGet = getAVDateValueOf(dayCounter);
                HashMap<String, AVReservation> datesToSort = getReservationsFor(dateToGet);
                ArrayList<AVReservation> sortedDatesByTime = sortByTime(datesToSort);
                addToSortedReservations(sortedDatesByTime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addToSortedReservations(ArrayList<AVReservation> sortedDates) {

        for (AVReservation avReservation :
                sortedDates) {
            sortedReservations.add(avReservation);
        }
    }

    /**
     * Need to convert the LocalDate value to AVDate because AVDate doesn't consider '0' in single
     * digit months: instead of 07/08/2021 it can only receive: 7/8/2021
     *
     * @param dayCounter
     * @return
     */
    private AVDate getAVDateValueOf(LocalDate dayCounter) {
        return new AVDate(String.valueOf(dayCounter.getMonthValue()), String.valueOf(dayCounter.getDayOfMonth()), String.valueOf(dayCounter.getYear()));
    }

    private List<LocalDate> getReservationDates() {
        List<LocalDate> datesList = new ArrayList<>();
        for (AVReservation av :
                reservationEntries.values()) {

            AVDate currD = av.getEvent().getEventDate();
            LocalDate d = LocalDate.of(Integer.valueOf(currD.getYear()), Integer.valueOf(currD.getMonth()), Integer.valueOf(currD.getDay()));

            datesList.add(d);
        }
        Collections.sort(datesList);

        List<LocalDate> nonDuplicateDateList = getNonDuplicateDateList(datesList);
        return nonDuplicateDateList;
    }

    private List<LocalDate> getNonDuplicateDateList(List<LocalDate> datesList) {

        List<LocalDate> noDuplicates = new ArrayList<>();

        for (LocalDate ld :
                datesList) {
            if (!noDuplicates.contains(ld)) {
                noDuplicates.add(ld);
            }
        }

        return noDuplicates;
    }

    public ArrayList<AVReservation> getSortedReservationsFor(AVDate avDate) {
        sortReservations();

        ArrayList<AVReservation> sorted = new ArrayList<>();

        for (AVReservation ave :
                sortedReservations) {
            if (ave.getEvent().getEventDate().equals(avDate)) {
                sorted.add(ave);
            }
        }


        return sorted;
    }

    public ArrayList<AVReservation> getSortedReservations() {
        return sortedReservations;
    }

    public boolean checkConflicts(AVReservation reservation) {
        for (AVReservation avr :
                reservationEntries.values()) {
            if (reservation.equals(avr)) {
                return false;
            } else if (checkTimeConflict(avr, reservation)) {

            }
        }

        //TODO to implement
        return false;
    }

    private boolean checkTimeConflict(AVReservation res1, AVReservation res2) {
        //TODO check four variations:
        // - if res1 start and end is between res2 AND res2 start and end conflicts with res1
        AVDate avDate1 = res1.getEvent().getEventDate();
        AVDate avDate2 = res2.getEvent().getEventDate();

        if (!avDate1.equals(avDate2)) {
            return false;
        }

        LocalTime target = LocalTime.now();


        return false;
    }

    public void setCheckedIn(AVReservation reservation) {
        reservationEntries.get(reservation.getReservationID()).setEquipmentStatus(AVReservation.CHECKED_IN);
        updateReservationManagerFile();
    }

    public void setCheckedOut(AVReservation reservation) {
        reservationEntries.get(reservation.getReservationID()).setEquipmentStatus(AVReservation.CHECKED_OUT);
        updateReservationManagerFile();
    }
}

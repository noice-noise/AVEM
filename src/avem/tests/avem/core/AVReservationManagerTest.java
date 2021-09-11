package avem.core;

import junit.framework.TestCase;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

public class AVReservationManagerTest extends TestCase {


    public void testInstance() {
        AVReservationManager rm = new AVReservationManager();
        assertEquals(true, rm.isAllEmpty());
    }

    public void testAddReservation() {
        AVReservationManager rm = new AVReservationManager();
        assertEquals(true, rm.isAllEmpty());

        AVEvent avEvent = new AVEvent("10th Research Congress", new AVDate("1/23/2021"), new AVTime("12:54 PM"), new AVTime("1:54 PM"), "Gymnasium", "Raol");
        HashMap<String, AVEquipment> equipmentHashMap = new HashMap<>();
        AVReservation reservation = new AVReservation(avEvent, equipmentHashMap);
        rm.addReservation(reservation);

        assertEquals(true, reservation.equals(rm.getReservation(reservation)));
        assertEquals(true, rm.reservationSize() == 1);
        assertEquals(false, rm.isAllEmpty());

        rm.removeReservation(reservation);
        assertEquals(true, rm.isAllEmpty());
    }

    public void testRemove() {
        AVReservationManager rm = new AVReservationManager();
        assertEquals(true, rm.isAllEmpty());
        HashMap<String, AVEquipment> equipmentHashMap = new HashMap<>();

        //try remove tests

        try {
            rm.removeReservation(null);
            fail();
        } catch (Exception e) {
            e.printStackTrace();
        }

        AVEvent avEvent = new AVEvent("10th Research Congress", new AVDate("1/23/2021"), new AVTime("12:54 PM"), new AVTime("1:54 PM"), "Gymnasium", "Raol");
        AVEvent avEvent2 = new AVEvent("11th Research Congress", new AVDate("1/23/2021"), new AVTime("12:54 PM"), new AVTime("1:54 PM"), "Gymnasium", "Raol");
        HashMap<String, AVEquipment> equipmentHashMap2 = new HashMap<>();
        AVReservation reservation2 = new AVReservation(avEvent2, equipmentHashMap);
        AVReservation reservation = new AVReservation(avEvent, equipmentHashMap);

        assertEquals(true, rm.reservationSize() == 0);
        rm.removeReservation(reservation2);
        assertEquals(true, rm.reservationSize() == 0);
    }

    public void testTestAppendFile() {
        AVEvent avEvent = new AVEvent("10th Research Congress", new AVDate("1/23/2021"), new AVTime("12:54 PM"), new AVTime("1:54 PM"), "Gymnasium", "Raol");
        AVEvent avEvent2 = new AVEvent("Weekly Mass", new AVDate("2", "3", "2012"), new AVTime("8", "00", "AM"), new AVTime("9", "00", "AM"), "Chapel", "Sire");
        HashMap<String, AVEquipment> equipmentHashMap = new HashMap<>();

        AVEquipment eq1 = new AVEquipment("Canon", "Camera", "CNS99123");
        AVEquipment eq2 = new AVEquipment("Onee-san", "Waifu", "Untrackable");
        AVEquipment eq3 = new AVEquipment("Tri", "Tripoddo", "CSPOEW231");
        equipmentHashMap.put(eq1.getEquipmentID(), eq1);
        equipmentHashMap.put(eq2.getEquipmentID(), eq2);
        equipmentHashMap.put(eq3.getEquipmentID(), eq3);

        assertEquals(true, equipmentHashMap.size() == 3);

        AVReservation avReservation1 = new AVReservation(avEvent, equipmentHashMap);
        AVReservationManager rm = new AVReservationManager();
        rm.addReservation(avReservation1);
        assertEquals(true, rm.reservationSize() == 1);
//        assertEquals(false, );

    }

    public void testRetrieveFile() {
        AVEvent avEvent = new AVEvent("10th Research Congress", new AVDate("1/23/2021"), new AVTime("12:54 PM"), new AVTime("1:54 PM"), "Gymnasium", "Raol");
        AVEvent avEvent2 = new AVEvent("Weekly Mass", new AVDate("2", "3", "2012"), new AVTime("8", "00", "AM"),  new AVTime("1:54 PM"),  "Chapel", "Sire");
        HashMap<String, AVEquipment> equipmentHashMap = new HashMap<>();

        AVEquipment eq1 = new AVEquipment("Canon", "Camera", "CNS99123");
        AVEquipment eq2 = new AVEquipment("Onee-san", "Waifu", "Untrackable");
        AVEquipment eq3 = new AVEquipment("Tri", "Tripoddo", "CSPOEW231");
        equipmentHashMap.put(eq1.getEquipmentID(), eq1);
        equipmentHashMap.put(eq2.getEquipmentID(), eq2);
        equipmentHashMap.put(eq3.getEquipmentID(), eq3);

        assertEquals(true, equipmentHashMap.size() == 3);

        AVReservation avReservation1 = new AVReservation(avEvent, equipmentHashMap);
        AVReservation avReservation2 = new AVReservation(avEvent2, equipmentHashMap);

        AVReservationManager rm = new AVReservationManager();
        rm.resetAllFilesAndDirectory();
        assertEquals(true, rm.isAllEmpty());

        rm.addReservation(avReservation1);
        assertEquals(true, rm.reservationSize() == 1);

        AVReservation parsedReservation = rm.getReservation(avReservation1);
        System.out.println("AVRESERVATION: " + avReservation1.getFormattedString());
        System.out.println("PARSED RESERVATION: " + parsedReservation.getFormattedString());
        assertEquals(true, parsedReservation.equals(avReservation1));

        rm.addReservation(avReservation2);
        assertEquals(true, rm.reservationSize() == 2);

        parsedReservation = rm.getReservation(avReservation2);
        assertEquals(true, parsedReservation.equals(avReservation2));

    }

    public void testAVReservationMethods() {
        AVEvent event1 = new AVEvent("EVENT1NAME", new AVDate("1", "23", "2312"), new AVTime("32", "23", "PM"),  new AVTime("1:54 PM"),  "Gym", "Raol");
        AVEvent event2 = new AVEvent("EVENT2NAME", new AVDate("21/23/2312"), new AVTime("1:23 AM"),  new AVTime("1:54 PM"), "Gym", "Raol");

        AVEquipment eq1 = new AVEquipment("Canon", "Camera", "SERIAL");
        AVEquipment eq2 = new AVEquipment("Lenovo", "Laptop", "WEWSDQWE");
        AVEquipment eq3 = new AVEquipment("Canon", "Camera", "123123");
        AVEquipment eq4 = new AVEquipment("Tri", "Tripod", "WEPPP");
        AVEquipment eq5 = new AVEquipment("Tri", "Tripod", "WEPPP");
        AVEquipment eq6 = new AVEquipment("Tri", "Tripod", "WEPPP");

        HashMap<String, AVEquipment> equipmentBundle1 = new HashMap<>();
        HashMap<String, AVEquipment> equipmentBundle2 = new HashMap<>();
        HashMap<String, AVEquipment> equipmentBundle3 = new HashMap<>();

        equipmentBundle1.put(eq1.getEquipmentID(), eq1);
        equipmentBundle2.put(eq1.getEquipmentID(), eq1);
        equipmentBundle2.put(eq2.getEquipmentID(), eq2);
        equipmentBundle2.put(eq3.getEquipmentID(), eq3);

        AVReservation reservation1 = new AVReservation(event1, equipmentBundle1);
        AVReservation reservation11 = new AVReservation(event1, equipmentBundle1);
        AVReservation reservation2 = new AVReservation(event2, equipmentBundle1);

        AVReservationManager rm = new AVReservationManager();
        rm.resetAllFilesAndDirectory();
        assertTrue(rm.isAllEmpty());
        System.out.println(rm.reservationSize());
        rm.addReservation(reservation1);
        assertEquals(true, 1 == rm.reservationSize());
        rm.addReservation(reservation11);
        assertEquals(true, 1 == rm.reservationSize());
        assertEquals(false, rm.isAllEmpty());
        rm.addReservation(reservation2);
        System.out.println(rm.reservationSize());
        assertEquals(true, 2 == rm.reservationSize());

        rm.removeReservation(reservation1);
        AVReservation parsed = rm.getReservation(reservation1);
        assertNull(parsed);

        assertEquals(true, 1 == rm.reservationSize());

        rm.removeReservation(reservation2);
        assertEquals(true, rm.isAllEmpty());

        rm.addReservation(reservation1);
        rm.updateReservation(reservation1, reservation2);

        assertEquals(true, rm.reservationSize() == 1);

        System.out.println("RES" + rm.getReservation(reservation1));
        parsed = rm.getReservation(reservation2);
        assertEquals(true, parsed.equals(reservation2));
    }


    public void testGetSpecificDateReservation() throws ParseException {
        AVEvent event1 = new AVEvent("EVENT1NAME", new AVDate("7", "8", "2021"), new AVTime("5", "23", "PM"), new AVTime("10", "00", "AM"), "Gym", "Raol");
        AVEvent event2 = new AVEvent("EVENT2NAME", new AVDate("7", "8", "2021"), new AVTime("6", "21", "PM"), new AVTime("10", "00", "AM"), "Gym", "Raol");
        AVEvent event3 = new AVEvent("EVENT3NAME", new AVDate("7", "9", "2021"), new AVTime("8", "30", "PM"), new AVTime("10", "00", "AM"), "Gym", "Raol");
        AVEvent event4 = new AVEvent("EVENT4NAME", new AVDate("7", "9", "2021"), new AVTime("8", "00", "PM"), new AVTime("10", "00", "AM"), "Gym", "Raol");

        AVEvent event5 = new AVEvent("5NAME", new AVDate("7", "9", "2021"), new AVTime("11", "00", "AM"), new AVTime("10", "00", "AM"), "Chapel", "Raol");
        AVEvent event6 = new AVEvent("6NAME", new AVDate("7", "10", "2021"), new AVTime("1", "00", "AM"), new AVTime("10", "00", "AM"), "Conference", "Raol");
        AVEvent event7 = new AVEvent("7NAME", new AVDate("7", "10", "2021"), new AVTime("5", "00", "AM"), new AVTime("10", "00", "AM"), "lol", "Raol");
        AVEvent event8 = new AVEvent("8NAME", new AVDate("7", "12", "2021"), new AVTime("4", "00", "AM"), new AVTime("10", "00", "AM"), "Nani!?", "Raol");


        AVEquipment eq1 = new AVEquipment("Canon", "Camera", "SERIAL");
        AVEquipment eq2 = new AVEquipment("Lenovo", "Laptop", "WEWSDQWE");
        AVEquipment eq3 = new AVEquipment("Canon", "Camera", "123123");
        HashMap<String, AVEquipment> equipmentBundle1 = new HashMap<>();
        equipmentBundle1.put(eq1.getEquipmentID(), eq1);
        equipmentBundle1.put(eq1.getEquipmentID(), eq2);
        equipmentBundle1.put(eq2.getEquipmentID(), eq3);

        AVReservationManager rm = new AVReservationManager();
        rm.resetAllFilesAndDirectory();
        assertEquals(true, rm.isAllEmpty());

        AVReservation r1 = new AVReservation(event1, equipmentBundle1);
        AVReservation r2 = new AVReservation(event2, equipmentBundle1);
        AVReservation r3 = new AVReservation(event3, equipmentBundle1);
        AVReservation r4 = new AVReservation(event4, equipmentBundle1);

        AVReservation r5 = new AVReservation(event5, equipmentBundle1);
        AVReservation r6 = new AVReservation(event6, equipmentBundle1);
        AVReservation r7 = new AVReservation(event7, equipmentBundle1);
        AVReservation r8 = new AVReservation(event8, equipmentBundle1);


        rm.addReservation(r1);

        HashMap<String, AVReservation> daysReservation = rm.getReservationsFor(new AVDate("7/8/2021"));
        assertEquals(true, rm.reservationSize() == 1);
        assertEquals(true, daysReservation.size() == 1);
        assertEquals(true, daysReservation.containsValue(r1));

        rm.addReservation(r2);

        daysReservation = rm.getReservationsFor(new AVDate("7/8/2021"));
        assertEquals(true, rm.reservationSize() == 2);
        assertEquals(true, daysReservation.size() == 2);
        assertEquals(true, daysReservation.containsValue(r1));
        assertEquals(true, daysReservation.containsValue(r2));

        rm.addReservation(r3);
        rm.addReservation(r4);

        HashMap<Integer, ArrayList<AVReservation>> weeksReservation = rm.getReservationRanging(new AVDate("7/8/2021"), new AVDate("7/9/2021"));
        assertEquals(true, weeksReservation.size() == 2);
        System.out.println("8 size: " +weeksReservation.get(8).size());
        assertEquals(true, weeksReservation.get(8).size() == 2);
        assertEquals(true, weeksReservation.get(9).size() == 2);

        rm.addReservation(r5);
        rm.addReservation(r6);
        rm.addReservation(r7);
        rm.addReservation(r8);

        HashMap<Integer, ArrayList<AVReservation>> wideRangeSeek = rm.getReservationRanging(new AVDate("7/8/2021"), new AVDate("7/14/2021"));

        assertEquals(true, rm.reservationSize() == 8);
        assertEquals(true, wideRangeSeek.size() == 7);

        assertEquals(true, wideRangeSeek.get(8).size() == 2);
        assertEquals(true, wideRangeSeek.get(9).size() == 3);
        assertEquals(true, wideRangeSeek.get(10).size() == 2);
        assertEquals(true, wideRangeSeek.get(11).size() == 0);
        assertEquals(true, wideRangeSeek.get(12).size() == 1);
        assertEquals(true, wideRangeSeek.get(13).size() == 0);
        assertEquals(true, wideRangeSeek.get(14).size() == 0);


        /*  SORT TEST */

        assertEquals(true, rm.reservationSize() == 8);
        HashMap<String, AVReservation> got = rm.getReservationsFor(new AVDate("7/9/2021"));
        assertEquals(true, got.size() == 3);
        rm.sortReservations();

    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        AVReservationManager tearDownRM = new AVReservationManager();
        tearDownRM.resetAllFilesAndDirectory();
    }
}
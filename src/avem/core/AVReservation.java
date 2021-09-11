package avem.core;

import java.util.HashMap;

public class AVReservation {

    public static String RESERVED = "Reserved";
    public static String CHECKED_IN = "Checked in";
    public static String CHECKED_OUT = "Checked out";


    private AVEvent event;
    private HashMap<String, AVEquipment> equipmentBundle;
    private String equipmentStatus;

    public AVReservation(AVEvent event) {
        this.event = event;
        this.equipmentStatus = RESERVED;
    }

    public AVReservation(AVEvent event, HashMap<String, AVEquipment> equipmentBundle) {
        this.event = event;
        this.equipmentBundle = equipmentBundle;
        this.equipmentStatus = RESERVED;
    }

    public AVEvent getEvent() {
        return event;
    }

    public void setEvent(AVEvent event) {
        this.event = event;
    }

    public HashMap<String, AVEquipment> getEquipmentBundle() {
        return equipmentBundle;
    }

    public void setEquipmentBundle(HashMap<String, AVEquipment> equipmentBundle) {
        this.equipmentBundle = equipmentBundle;
    }

    public String getEquipmentStatus() {
        return equipmentStatus;
    }

    /**
     * Access the collection of status convention in the static variables of this class.
     * @param STATUS
     */
    public void setEquipmentStatus(String STATUS) {
        this.equipmentStatus = STATUS;
    }

    @Override
    public String toString() {
        return "AVReservation{" +
                "event=" + event +
                ", equipmentBundle=" + "\n\n" + equipmentBundle.values() + "\n\n" +
                ", status='" + equipmentStatus + '\'' +
                '}';
    }

    /***
     * Returns a batch of string from AVEvent and collection of AVEquipment.
     * { eventString }
     * ([equipment1]
     * [equipment2]
     * [equipment3])
     * @return
     */
    public String getFormattedString() {
        return
                "{" + event.getFormattedString() + "}" +
                "(" + getEquipmentBundleFormattedString() + ")" +
                "/" + equipmentStatus + "\\"
                ;
    }

    private String getEquipmentBundleFormattedString() {
        String contents = "";
        try {
            for (AVEquipment ave :
                    equipmentBundle.values()) {
                contents += "[" +
                        ave.getFormattedString()
                        + "]"
                ;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contents;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AVReservation && obj != null) {
            AVReservation avReservation = (AVReservation) obj;
            if (this.event.equals(avReservation.getEvent()) &&
                    this.equipmentBundle.equals(avReservation.getEquipmentBundle())) {
                return true;
            }
        }
        return false;
    }

    //The id for reservation is similar to the event ID, events IDs are supposed to be unique considering all the name, date, and time
    public String getReservationID() {
        return event.getID();
    }
}

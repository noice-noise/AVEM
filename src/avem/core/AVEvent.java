package avem.core;

public class AVEvent {

    public static final String RESERVED_STATUS = "Reserved";
    public static final String ONGOING_STATUS = "Ongoing";
    public static final String CANCELLED_STATUS = "Cancelled";
    public static final String POSTPONED_STATUS = "Postponed";
    public static final String FINISHED_STATUS = "Finished";

    private String avID;
    private String eventName;
    private AVDate eventDate;
    private AVTime eventStartTime;
    private AVTime eventEndTime;

    private String eventVenue;
    private String eventDepartment;
    private String contactPerson;
    private String contactNumber;
    private String approvedBy;
    private String additionalNotes;
    private String encodedBy;
    private String eventStatus;

    public AVEvent(String eventName, AVDate eventDate, AVTime eventStartTime, AVTime eventEndTime, String eventVenue, String encodedBy, String eventStatus) {
        verifyTime(eventStartTime, eventEndTime);
        this.avID = (eventDate.toString().trim() + eventStartTime.toString().trim() + eventName.trim());
        this.eventName = eventName.trim();
        this.eventDate = eventDate;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
        this.eventVenue = eventVenue.trim();
        this.eventDepartment = "Not set";
        this.contactPerson = "Not set";
        this.contactNumber = "Not set";
        this.approvedBy = "Not set";
        this.additionalNotes = "None";
        this.encodedBy = encodedBy.trim();
        this.eventStatus = RESERVED_STATUS;
    }

    /***
     * Follow the sequence of the required data on at all times to prevent problems in loops and format.
     * Minimum required variable is 5, and maximum variable in this data type is 10.
     * All variables has initial values i.e. "Not set"
     * @param eventName
     * @param eventDate
     * @param eventStartTime
     * @param eventVenue
     * @param encodedBy
     */
    //NOTE: Don't try to re-arrange variables as they are referenced to all usage of this specific class.
    public AVEvent(String eventName, AVDate eventDate, AVTime eventStartTime, AVTime eventEndTime, String eventVenue, String encodedBy) {
        verifyTime(eventStartTime, eventEndTime);
        this.avID = (eventDate.toString().trim() + eventStartTime.toString().trim() + eventName.trim());
        this.eventName = eventName.trim();
        this.eventDate = eventDate;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
        this.eventVenue = eventVenue.trim();
        this.eventDepartment = "Not set";
        this.contactPerson = "Not set";
        this.contactNumber = "Not set";
        this.approvedBy = "Not set";
        this.additionalNotes = "None";
        this.encodedBy = encodedBy.trim();
        this.eventStatus = RESERVED_STATUS;
    }

    // constructor used for parsing by reservation manager
    public AVEvent(String eventName, AVDate eventDate,
                   AVTime eventStartTime, AVTime eventEndTime, String eventVenue, String eventDepartment,
                   String contactPerson, String contactNumber, String approvedBy,
                   String additionalNotes, String encodedBy, String eventStatus) {
        verifyTime(eventStartTime, eventEndTime);
        this.avID = (eventDate.toString().trim() + eventStartTime.toString().trim() + eventName.trim());
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
        this.eventVenue = eventVenue;
        this.eventDepartment = eventDepartment;
        this.contactPerson = contactPerson;
        this.contactNumber = contactNumber;
        this.approvedBy = approvedBy;
        this.additionalNotes = additionalNotes;
        this.encodedBy = encodedBy;
        this.eventStatus = eventStatus;
    }

    private void verifyTime(AVTime eventStartTime, AVTime eventEndTime) {
        if (eventStartTime.equals(eventEndTime)) {
            throw new IllegalArgumentException("Start and End time must not be the same.");
        }
    }

    @Override
    public boolean equals (Object obj) {
        if (obj instanceof AVEvent && obj != null) {
            AVEvent avEvent = (AVEvent) obj;
            if (this.eventName.equals(avEvent.getEventName()) &&
                    this.eventDate.equals(avEvent.getEventDate()) &&
                    this.eventStartTime.equals(avEvent.getEventStartTime()) &&
                    this.eventEndTime.equals(avEvent.getEventEndTime()) &&
                    this.eventVenue.equals(avEvent.getEventVenue()) &&
                    this.eventDepartment.equals(avEvent.getEventDepartment()) &&
                    this.eventStatus.equals(avEvent.eventStatus)) {
                return true;
            }
        }
        return false;
    }


    public String getFormattedString(){
        return
                this.avID + "\n" +
                this.eventName + "\n" +
                this.eventDate + "\n" +
                this.eventStartTime + "\n" +
                this.eventEndTime + "\n" +
                this.eventVenue + "\n" +
                this.eventDepartment + "\n" +
                this.contactPerson + "\n" +
                this.contactNumber + "\n" +
                this.approvedBy + "\n" +
                this.additionalNotes + "\n" +
                this.encodedBy+ "\n" +
                this.eventStatus;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public AVDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(AVDate eventDate) {
        this.eventDate = eventDate;
    }

    public AVTime getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(AVTime eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public String getEventVenue() {
        return eventVenue;
    }

    public void setEventVenue(String eventVenue) {
        this.eventVenue = eventVenue;
    }

    public String getEventDepartment() {
        return eventDepartment;
    }

    public void setEventDepartment(String eventDepartment) {
        this.eventDepartment = eventDepartment;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getAdditionalNotes() {
        return additionalNotes;
    }

    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }

    public String getEncodedBy() {
        return encodedBy;
    }

    public void setEncodedBy(String encodedBy) {
        this.encodedBy = encodedBy;
    }

    public String getID() {
        return avID;
    }


    public AVTime getEventEndTime() {
        return eventEndTime;
    }

    public void setEventEndTime(AVTime eventEndTime) {
        this.eventEndTime = eventEndTime;
    }

    public Boolean isEqualDateTime(AVDate avDate, AVTime startTime, AVTime endTime) {
        return this.eventDate.equals(avDate) && this.eventStartTime.equals(startTime) && this.eventEndTime.equals(endTime);
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }
}

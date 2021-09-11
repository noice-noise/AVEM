package avem.core;

import java.util.Locale;

public class AVEquipment {

    private String equipmentID;
    private String name;
    private String type;
    private String serialNumber;
    private String brand;
    private String imgFilePath;
    private String notes;
    private String currentLocation;
    private boolean isAvailable;

    public AVEquipment(String name, String type, String serialNumber) {
        this.name = name;
        this.type = type;
        this.serialNumber = serialNumber;
        this.brand = "N/A";
        this.imgFilePath = "N/A";
        this.notes = "None";
        this.currentLocation = "Storage";
        this.isAvailable = true;
        createEquipmentID();
    }

    public AVEquipment(
            String name,
            String type,
            String serialNumber,
            String brand,
            String imgFilePath,
            String notes,
            String currentLocation,
            boolean isAvailable) {
        this.name = name;
        this.type = type;
        this.serialNumber = serialNumber;
        this.brand = brand;
        this.imgFilePath = imgFilePath;
        this.notes = notes;
        this.currentLocation = currentLocation;
        this.isAvailable = isAvailable;
        createEquipmentID();
    }


    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getEquipmentID() {
        return equipmentID;
    }

    public void setEquipmentID(String equipmentID) {
        this.equipmentID = equipmentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }


    public void trimContents() {
        this.name = name.trim();
        this.type = type.trim();
        this.serialNumber = serialNumber.trim();
        this.brand = brand.trim();
        this.notes = notes.trim();
        this.currentLocation = currentLocation.trim();
        this.isAvailable = true;
    }

    @Override
    public String toString() {
        return "AVEquipment{" +
                ", \nequipmentID='" + equipmentID + '\'' +
                ", \nequipmentName='" + name + '\'' +
                ", \nequipmentType='" + type + '\'' +
                ", \nequipmentSerialNumber='" + serialNumber + '\'' +
                ", \nequipmentBrand='" + brand + '\'' +
                ", \nimgFilePath='" + imgFilePath + '\'' +
                ", \nnotes='" + notes + '\'' +
                ", \ncurrentLocation='" + currentLocation + '\'' +
                ", \nisAvailable='" + isAvailable + '\'' +
                '}';
    }

    public String getFormattedString() {
        return
                equipmentID + "\n" +
                        name + "\n" +
                        type + "\n" +
                        serialNumber + "\n" +
                        brand + "\n" +
                        imgFilePath + "\n" +
                        notes + "\n" +
                        currentLocation + "\n" +
                        isAvailable;
    }

    private void createEquipmentID() {
        int i = serialNumber.charAt(0);
        int i2 = name.charAt(0);
        int i3 = type.charAt(0);
        int i4 = serialNumber.charAt(serialNumber.length()-1);
        int i5 = name.charAt(name.length()-1);
        int i6 = type.charAt(type.length()-1);
        String str = (i) + (i2) + Integer.toString(i3) + i4 + i5 + i6;
        this.equipmentID = str;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AVEquipment && obj != null) {
            AVEquipment avEquipment = (AVEquipment) obj;
            if (this.serialNumber.equals(avEquipment.getSerialNumber()) &&
                    this.equipmentID.equals(avEquipment.getEquipmentID())
            ) {
                return true;
            }
        }
        return false;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = removeNewLines(notes);
    }

    public void appendNotes(String notes) {
        if (this.notes.toLowerCase(Locale.ROOT).equals("none")) {
            this.notes = removeNewLines(notes);
        } else {
            this.notes += removeNewLines(notes);
        }
    }

    public String removeNewLines(String str) {
        String newStr = new String();
        for (char c:
             str.toCharArray()) {
            if (c == '\n') {
                c = ' ';
            }
            newStr += c;

        }
        return newStr;
    }

    public String getImgFilePath() {
        return imgFilePath;
    }

    public void setImgFilePath(String imgFilePath) {
        this.imgFilePath = imgFilePath;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}

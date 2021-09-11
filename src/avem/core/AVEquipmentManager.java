package avem.core;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class AVEquipmentManager {

    private HashMap<String, AVEquipment> equipmentEntries;
    private ArrayList<AVEquipment> equipmentArrayList;
    private File equipmentManagerDIR;
    private File equipmentManagerFile;

    private boolean isTest;         // this humble variable used to check if the current activity is for Testing
    private boolean sessionDone;    // this another humble variable is used to as signifier to delete all recent library called for testing

    public AVEquipmentManager() {
        initialize();
    }

    public AVEquipmentManager(boolean isTest) {
        this.isTest = isTest;
        initializeForTest();
    }

    private void initializeForTest() {
        initDirectoryWithPath("Test");
        createEquipmentEntries();
        createEquipmentArrayList();
        createDirectory();
        createFile();
        parseFileContents();
    }


    private void initialize() {
        initDirectoryWithPath("equipment-manager");
        createEquipmentEntries();
        createEquipmentArrayList();
        createDirectory();
        createFile();
        parseFileContents();
    }


    public void resetAllFilesAndDirectory() {
        deleteFiles();
        createDirectory();
        createFile();
    }

    private boolean createFile() {
        equipmentManagerFile = new File(equipmentManagerDIR, "equipment-info.data");
        try {
            if (fileExists()) {
            } else {
                equipmentManagerFile.createNewFile();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean fileExists() {
        return equipmentManagerFile.exists();
    }

    public boolean createDirectory() {
        if (directoryExists()) {
            return false;
        } else {
            equipmentManagerDIR.mkdir();
            return true;
        }
    }

    public boolean directoryExists() {
        return equipmentManagerDIR.exists();
    }

    private void initDirectoryWithPath(String directoryName) {
        equipmentManagerDIR = new File(directoryName);
    }

    public void setSessionDone() {
        if (isTest)
            deleteFiles();
        sessionDone = true;
    }


    private void deleteFiles() {
        equipmentManagerFile.delete();
        equipmentManagerDIR.delete();
    }

    public boolean fileEmpty() {
        return equipmentManagerFile.length() == 0;
    }

    public File getFile() {
        return equipmentManagerFile;
    }

    private void createEquipmentArrayList() {
        equipmentArrayList = new ArrayList();
    }

    private void createEquipmentEntries() {
        equipmentEntries = new HashMap<>();
    }

    public boolean getSessionStatus() {
        return sessionDone;
    }

    public File getDirectory() {
        return equipmentManagerDIR;
    }

    /***
     *
     * @param toGet
     */
    public AVEquipment getEquipment(AVEquipment toGet) {
        if (toGet == null) {
            throw new IllegalArgumentException("Invalid argument, course must not be null");
        }
        return equipmentEntries.get(toGet.getEquipmentID());
    }

    /***
     *
     * @param toAdd
     */
    public void addEquipment(AVEquipment toAdd) {
        toAdd.trimContents();
        equipmentEntries.put(toAdd.getEquipmentID(), toAdd);
        updateEquipmentManager();
        System.out.println(equipmentEntries.size());
    }


    public void updateEquipmentManager() {
        try {
            String contents = getFormattedEquipment();
            FileWriter fw = new FileWriter(equipmentManagerFile);
            fw.append(contents);
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFormattedEquipment() {
        String contents = "";
        try {
            for (AVEquipment ave :
                    equipmentEntries.values()) {
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


    /***
     *
     * @param toRemove
     */
    public void removeEquipment(AVEquipment toRemove) {
        equipmentEntries.remove(toRemove.getEquipmentID(), toRemove);
        updateEquipmentManager();
    }

    /***
     *
     * @param toEdit
     */
    public void editEquipment(AVEquipment toEdit, AVEquipment withThisAVEquipment) {
        parseFileContents();
        equipmentEntries.remove(toEdit.getEquipmentID());
        // preserves the availability status as constructor doesn't allow input of availability
        withThisAVEquipment.setAvailable(toEdit.isAvailable());

        equipmentEntries.put(withThisAVEquipment.getEquipmentID(), withThisAVEquipment);

        updateEquipmentManager();
    }

    public ArrayList<AVEquipment> getAllEquipment() {
        parseFileContents();
        Collection<AVEquipment> equipmentCollection = equipmentEntries.values();
        ArrayList<AVEquipment> listOfEquipment
                = new ArrayList<AVEquipment>(equipmentCollection);
        return listOfEquipment;

    }

    public String getFileContents() {
        String contents = "";

        try {
            FileReader fr = new FileReader(equipmentManagerFile);
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

    public void parseFileContents() {
        try {
            String contents;
            String toParse;
            contents = getFileContents();
            StringTokenizer st = new StringTokenizer(contents);
            while (st.hasMoreTokens()) {
                toParse = st.nextToken("[]");
                if (toParse.length() > 1) {
                    AVEquipment avEquipment = parseCourseFrom(toParse.trim());
                    equipmentEntries.put(avEquipment.getEquipmentID(), avEquipment);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (AVEquipment ave :
                equipmentEntries.values()) {
        }
    }

    private AVEquipment parseCourseFrom(String toParse) {
        StringTokenizer st = new StringTokenizer(toParse, "\n");

        //String[] info = new String[9];
        ArrayList<String> info = new ArrayList<>();


        while (st.hasMoreTokens()) {
            info.add(st.nextToken());

        }

        AVEquipment avEquipment = new AVEquipment(info.get(1), info.get(2), info.get(3));
        avEquipment.setBrand(info.get(4));
//        avEquipment.setImgFilePath(info.get(5));  [future] img file here
        avEquipment.setNotes(info.get(6));
        avEquipment.setCurrentLocation(info.get(7));
        avEquipment.setAvailable(Boolean.valueOf(info.get(8)));
        return avEquipment;
    }

    public HashMap<String, AVEquipment> getEquipmentEntries() {
        return this.equipmentEntries;
    }

    public void setEquipmentUnavailable(AVEquipment avEquipment) {
        equipmentEntries.get(avEquipment.getEquipmentID()).setAvailable(false);
        updateEquipmentManager();
    }

    public void setEquipmentAvailable(AVEquipment avEquipment) {
        equipmentEntries.get(avEquipment.getEquipmentID()).setAvailable(true);
        updateEquipmentManager();
    }

    public void setEquipmentNote(AVEquipment avEquipment, String note) {
        equipmentEntries.get(avEquipment.getEquipmentID()).setNotes(note);
        updateEquipmentManager();
    }
}

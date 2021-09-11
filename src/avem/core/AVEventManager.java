package avem.core;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class AVEventManager {

    private HashMap<String, AVEvent> eventEntries;
    private ArrayList<AVEvent> eventArrayList;
    private File eventManagerDIR;
    private File eventManagerFile;


    // variables for testing purposes (please treat with respect)

    private boolean isTest;         // this humble variable used to check if the current activity is for Testing
    private boolean sessionDone;    // this another humble variable is used to as signifier to delete all recent library called for testing

    public AVEventManager() {
        initialize();
    }

    public AVEventManager(boolean isTest) {
        this.isTest = isTest;
        initializeForTest();
    }

    private void initialize() {
        initDirectoryWithPath("event-manager");
        createEquipmentEntries();
        createEquipmentArrayList();
        createDirectory();
        createFile();
    }

    private void initializeForTest() {
        initDirectoryWithPath("Test");
        createEquipmentEntries();
        createEquipmentArrayList();
        createDirectory();
        createFile();
    }

    public void resetAllFilesAndDirectory() {
        deleteFiles();
        createDirectory();
        createFile();
    }

    private boolean createFile() {
        eventManagerFile = new File(eventManagerDIR, "events-info.data");
        try {
            if (fileExists()) {
                System.out.println("File already existed...");
            } else {
                eventManagerFile.createNewFile();
                System.out.println("Event file created successfully...");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean fileExists() {
        return eventManagerFile.exists();
    }

    public boolean createDirectory() {
        if (directoryExists()) {
            System.out.println("EventManager Directory already existed...");
            return false;
        } else {
            eventManagerDIR.mkdir();
            System.out.println("EventManager file directory successfully created...");
            System.out.println("DIR: " + eventManagerDIR.getAbsolutePath());
            System.out.println("Name: " + eventManagerDIR.getName());
            System.out.println("Path: " + eventManagerDIR.getPath());
            return true;
        }
    }

    public boolean directoryExists() {
        return eventManagerDIR.exists();
    }

    private void initDirectoryWithPath(String directoryName) {
        eventManagerDIR = new File(directoryName);
        System.out.println("\nDirectory initialized...");
    }

    public void setSessionDone() {
        if (isTest)
            deleteFiles();

        sessionDone = true;
//        System.out.println("Session done.");
    }


    private void deleteFiles() {
        eventManagerFile.delete();
        System.out.println("EventManager file deleted...");
        eventManagerDIR.delete();
        System.out.println("EventManager directory deleted...");
    }

    public boolean fileEmpty() {
        return eventManagerFile.length() == 0;
    }

    public File getFile() {
        return eventManagerFile;
    }

    private void createEquipmentArrayList() {
        eventArrayList = new ArrayList();
    }

    private void createEquipmentEntries() {
        eventEntries = new HashMap<>();
    }

    public boolean getSessionStatus() {
        return sessionDone;
    }

    public File getDirectory() {
        return eventManagerDIR;
    }

    public AVEvent getEquipment(AVEvent toGet) {
        AVEvent event;
        if (toGet == null) {
            throw new IllegalArgumentException("Invalid argument, course must not be null");
        } else {
            event = eventEntries.get(toGet.getEventName());
        }

        if (event.equals(toGet)){
            return event;
        }

        return null;
    }

    /***
     *
     * @param toAdd
     */
    public void addEvent(AVEvent toAdd) {
        try {
            eventEntries.put(toAdd.getEventName(), toAdd);
            updateEquipmentManagerFile();
        } catch (IllegalArgumentException eia) {
            throw new IllegalArgumentException("Argument not valid.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     *
     * @param toRemove
     */
    public void removeEquipment(AVEvent toRemove) {
        try {
            eventEntries.remove(toRemove.getEventName(), toRemove);
            updateEquipmentManagerFile();
        } catch (IllegalArgumentException eia) {
            throw new IllegalArgumentException("Argument not valid.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     *
     * @param toEdit
     */
    public void editEquipment(AVEvent toEdit, AVEvent withThisAVEvent) {
        try {
            eventEntries.replace(toEdit.getEventName(), withThisAVEvent);
            updateEquipmentManagerFile();
        } catch (IllegalArgumentException eia) {
            throw new IllegalArgumentException("Argument not valid.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateEquipmentManagerFile() {
        try {
            String contents = getFormattedEquipment();
            FileWriter fw = new FileWriter(eventManagerFile);
            fw.append(contents);
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFormattedEquipment() {
        String contents = "";
        try {
            for (AVEvent ave :
                    eventEntries.values()) {
                contents += "{" +
                        ave.getFormattedString()
                        + "}"
                ;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return contents;
    }

    public ArrayList<AVEvent> getAllEquipment() {
        parseFileContents();
        Collection<AVEvent> eventCollection = eventEntries.values();
        ArrayList<AVEvent> listOfEvent
                = new ArrayList<AVEvent>(eventCollection);
        return listOfEvent;
    }

    public void displayConsoleCourseEntries() {
        int count = 0;
        for (AVEvent ave :
                eventEntries.values()) {
            System.out.println("Item: " + count + " ");
            System.out.println(ave);
        }
    }

    public String getFileContents() {
        String contents = "";

        try {
            FileReader fr = new FileReader(eventManagerFile);
            int currentCharacter = 0;
            while((currentCharacter = fr.read()) != -1) {
                contents += (char)currentCharacter;
            }
            fr.close();
        } catch (Exception e){
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
                toParse = st.nextToken("{}");
                if (toParse.length() > 1){
                    AVEvent avEvent = parseCourseFrom(toParse.trim());
                    eventEntries.put(avEvent.getEventName(), avEvent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AVEvent parseCourseFrom(String toParse) {
        StringTokenizer st = new StringTokenizer(toParse, "\n");

        //String[] info = new String[10];
        ArrayList<String> info = new ArrayList<>();

        while (st.hasMoreTokens()) {
            info.add(st.nextToken());
        }

        int count = 0;
        AVEvent avEvent = new AVEvent(
                info.get(count++),  new AVDate(info.get(count++)),  new AVTime(info.get(count++)), new AVTime(info.get(count++)),
                info.get(count++),  info.get(count++),  info.get(count++),
        info.get(count++), info.get(count++), info.get(count++), info.get(count++), info.get(count++));
        return avEvent;

    }

}

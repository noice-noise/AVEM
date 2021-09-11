package avem.core;


import junit.framework.TestCase;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;


public class AVEquipmentManagerTest extends TestCase {

    @Test
    public void testAddEquipment() {
        AVEquipment camera = new AVEquipment("Sony", "Camera", "SN20398");
        AVEquipment tripod = new AVEquipment("Benson", "Tripod", "TRPOS23");
        AVEquipmentManager em = new AVEquipmentManager(true);
        assertEquals(true, em.getAllEquipment().toArray().length == 0);
        em.addEquipment(camera);
        assertEquals(false, em.getAllEquipment().toArray().length == 0);
        assertEquals(true, em.getAllEquipment().toArray().length == 1);
        em.addEquipment(camera);
    }

    @Test
    public void testAddDuplicate() {
        AVEquipment camera = new AVEquipment("Sony", "Camera", "SN20398");
        AVEquipment tripod = new AVEquipment("Benson", "Tripod", "TRPOS23");
        AVEquipmentManager em = new AVEquipmentManager();
        assertEquals(true, em.getAllEquipment().toArray().length == 0);
        em.addEquipment(camera);
        assertEquals(false, em.getAllEquipment().toArray().length == 0);
        assertEquals(true, em.getAllEquipment().toArray().length == 1);
        em.addEquipment(camera);
        assertEquals(true, em.getAllEquipment().toArray().length == 1);
    }

    @Test
    public void testRemoveOne() {
        AVEquipment camera = new AVEquipment("Sony", "Camera", "SN20398");
        AVEquipmentManager em = new AVEquipmentManager(true);
        assertEquals(true, em.getAllEquipment().toArray().length == 0);
        em.addEquipment(camera);
        assertEquals(true, em.getAllEquipment().toArray().length == 1);
        em.removeEquipment(camera);
        assertEquals(true, em.getAllEquipment().toArray().length == 0);
    }


    @Test
    public void testNull() {
        AVEquipment camera = new AVEquipment("Sony", "Camera", "SN20398");
        AVEquipmentManager em = new AVEquipmentManager(true);
        em.resetAllFilesAndDirectory();
        assertEquals(true, em.getAllEquipment().toArray().length == 0);

        try {
            em.getEquipment(null);
            fail();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.out.println("Null handled well.");
        }

    }

    @Test
    public void testRemoveEquip() {
        AVEquipment camera = new AVEquipment("Sony", "Camera", "SN20398");
        AVEquipment tripod = new AVEquipment("Benson", "Tripod", "TRPOS23");
        AVEquipmentManager em = new AVEquipmentManager(true);
        assertEquals(true, em.getAllEquipment().toArray().length == 0);
        em.addEquipment(camera);
        assertEquals(false, em.getAllEquipment().toArray().length == 0);
        assertEquals(true, em.getAllEquipment().toArray().length == 1);
        em.addEquipment(tripod);
        assertEquals(true, em.getAllEquipment().toArray().length == 2);
        em.removeEquipment(camera);
        assertEquals(true, em.getAllEquipment().toArray().length == 1);
    }


    @Test
    public void testEquipmentManagerInit() {
        AVEquipmentManager em = new AVEquipmentManager();
    }

    @Test
    public void createInstance() {
        AVEquipmentManager em = new AVEquipmentManager();
        assertEquals(true, em.directoryExists());
        assertEquals(true, em.fileExists());
        em.setSessionDone();
        assertEquals(true, em.directoryExists());
        assertEquals(true, em.fileExists());
    }

    @Test
    public void createTestInstance() {
        //When testing functionality, set testStatus to true during the instantiation
        AVEquipmentManager cl = new AVEquipmentManager(true);
        assertEquals(true, cl.directoryExists());
        assertEquals(true, cl.fileExists());
        assertEquals(false, cl.getSessionStatus());

        //All test files created in the instantiation will be deleted after sessionDone == true
        cl.setSessionDone();
        assertEquals(true, cl.getSessionStatus());
        assertEquals(false, cl.directoryExists());
        assertEquals(false, cl.fileExists());
    }

    @Test
    public void resetFilesAndDirectory() {
        AVEquipmentManager cl = new AVEquipmentManager();
        File file = cl.getFile();

        appendInFile(file, "test test test test, tdd rocks (/-_-)/");

        assertEquals(true, file.length() > 0);

        cl.resetAllFilesAndDirectory();

        assertEquals(true, file.length() == 0);
        assertEquals(true, cl.directoryExists());
        assertEquals(true, cl.fileExists());

        cl.setSessionDone();
    }

    private void appendInFile(File sourceFile, String stringToAppend) {
        try {
            FileWriter fw = new FileWriter(sourceFile);
            fw.append(stringToAppend);
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deletedTestFilesOnExit() {
        AVEquipmentManager cl = new AVEquipmentManager(true);

        File dirFile = cl.getDirectory();
        File libFile = cl.getFile();
        assertEquals(true, dirFile.exists());
        assertEquals(true, libFile.exists());

        assertEquals(false, cl.getSessionStatus());
        assertEquals(true, cl.directoryExists());
        assertEquals(true, cl.fileExists());

        cl.setSessionDone();
        assertEquals(true, cl.getSessionStatus());
        assertEquals(false, cl.directoryExists());
        assertEquals(false, cl.fileExists());

        assertEquals(false, dirFile.exists());
        assertEquals(false, libFile.exists());
    }

    @Test
    public void appendSimpleToEMFile() {
        AVEquipmentManager em = new AVEquipmentManager(true);
        assertEquals(true, em.fileEmpty());
        System.out.println(em.getFileContents());

        appendInFile(em.getFile(), "[");
        assertEquals(false, em.fileEmpty());
        assertEquals(true, em.getFileContents().equals("["));
    }

    @Test
    public void appendEquipmentBundle() {
        AVEquipment projector = new AVEquipment("EPSON", "Projector", "PRJQWEUO");
        AVEquipment camera = new AVEquipment("Sony", "Camera", "SN20398");
        AVEquipment tripod = new AVEquipment("Benson", "Tripod", "TRPOS23");

        AVEquipmentManager em = new AVEquipmentManager(true);
        em.resetAllFilesAndDirectory();
        assertEquals(true, em.fileEmpty());
        em.addEquipment(projector);
        em.addEquipment(camera);
        em.addEquipment(tripod);
        assertEquals(false, em.fileEmpty());
//        em.resetAllFilesAndDirectory();
    }

    @Test
    public void testAddAndRetrieveFromFileData() {
        AVEquipmentManager em = new AVEquipmentManager(true);
        em.resetAllFilesAndDirectory();
        assertEquals(true, em.fileEmpty());

        assertEquals(true, em.getAllEquipment().toArray().length == 0);
        AVEquipment ringlight = new AVEquipment("Ringlight A", "Light", "None");
        String id = ringlight.getEquipmentID();

        em.addEquipment(ringlight);
        assertEquals(false, em.fileEmpty());
        assertEquals(true, em.getAllEquipment().toArray().length == 1);

        String fileContents = em.getFileContents();
        String mustContain =
                "[" +
                id + "\n" +
                "Ringlight A\n" +
                "Light\n" +
                "None\n" +
                "N/A\n" +
                "N/A\n" +
                "None\n" +
                "Storage\n" +
                "true]";

        System.out.println("mustContain: " + mustContain);
        System.out.println("File contents: " + fileContents);
        assertEquals(true, mustContain.equals(fileContents));
    }

    @Test
    public void AddBundleThenRetrieve() {
        AVEquipmentManager em = new AVEquipmentManager(true);
        AVEquipment ringlight = new AVEquipment("Ringlight A", "Light", "None");
        AVEquipment ringlight2 = new AVEquipment("Ringlight B", "Light", "WEW");
        em.resetAllFilesAndDirectory();
        assertEquals(true, em.fileEmpty());
        em.setSessionDone();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // normal run
        AVEquipmentManager eq = new AVEquipmentManager();
        eq.getFile().delete();
        eq.getDirectory().delete();

        // test run
        AVEquipmentManager eqt = new AVEquipmentManager(true);
        eqt.getFile().delete();
        eqt.getDirectory().delete();
    }


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        // normal run
        AVEquipmentManager eq = new AVEquipmentManager();
        eq.getFile().delete();
        eq.getDirectory().delete();

        // test run
        AVEquipmentManager eqt = new AVEquipmentManager(true);
        eqt.getFile().delete();
        eqt.getDirectory().delete();
    }
}
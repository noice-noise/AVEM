package avem.developer;

import avem.basic.AVEMInfo;
import avem.core.AVAccount;
import avem.core.AVEquipment;
import avem.core.AVEquipmentManager;

public class AdminTools {

    public static void createInitialEquipmentFiles() {

        AVEquipmentManager em = new AVEquipmentManager();

        em.addEquipment(new AVEquipment(
                "Lenovo L1",
                "Laptop",
                "SN20398",
                "Lenovo",
                "Not set",
                "None",
                "Main Storage",
                true));

        em.addEquipment(new AVEquipment(
                "YogaBoy X1",
                "Laptop",
                "SNWPO298",
                "Lenovo",
                "Not set",
                "None",
                "Annex Storage",
                true));

        em.addEquipment(new AVEquipment(
                "Acer X10",
                "Laptop",
                "S020398",
                "Lenovo",
                "Not set",
                "None",
                "Office",
                true));

        em.addEquipment(new AVEquipment(
                "Canon C1",
                "DSLR Camera",
                "KSN29378",
                "Canon",
                "Not set",
                "None",
                "Office",
                true));

        em.addEquipment(new AVEquipment(
                "Canon VC1",
                "Video Camera",
                "CV0924852",
                "Canon",
                "Not set",
                "None",
                "Office",
                true));

        em.addEquipment(new AVEquipment(
                "Sony S1",
                "Video Camera",
                "SS8412",
                "Sony",
                "Not set",
                "None",
                "Office",
                true));

        em.addEquipment(new AVEquipment(
                "Sony S2",
                "Video Camera",
                "S3232",
                "Sony",
                "Not set",
                "None",
                "Office",
                true));

        em.addEquipment(new AVEquipment(
                "Benson 1",
                "Tripon",
                "BB098371",
                "Benn",
                "Not set",
                "None",
                "Main Storage",
                true));

        em.addEquipment(new AVEquipment(
                "Benson 2",
                "Tripon",
                "BB92381",
                "Benn",
                "Not set",
                "None",
                "Annex Storage",
                true));

        em.addEquipment(new AVEquipment(
                "Benson 3",
                "Tripon",
                "BB092233",
                "Benn",
                "Not set",
                "None",
                "Office",
                true));

        em.addEquipment(new AVEquipment(
                "EPSON E1",
                "Projector Small",
                "EP99912",
                "Epson",
                "Not set",
                "None",
                "Office",
                true));

        em.addEquipment(new AVEquipment(
                "EPSON X1",
                "Projector Big",
                "EP83838",
                "Epson",
                "Not set",
                "None",
                "Office",
                true));

        em.addEquipment(new AVEquipment(
                "EPSON L2",
                "Projector Small",
                "EP293888",
                "Epson",
                "Not set",
                "None",
                "Office",
                true));

        em.addEquipment(new AVEquipment(
                "EPSON B1",
                "Projector Big",
                "EP99912",
                "Epson",
                "Not set",
                "None",
                "Office",
                true));

        em.addEquipment(new AVEquipment(
                "Speaker A1",
                "Speakers Big",
                "HS909785",
                "Hercules",
                "Not set",
                "None",
                "Office",
                true));

        em.addEquipment(new AVEquipment(
                "Speaker A2",
                "Speakers Big",
                "HS084175",
                "Hercules",
                "Not set",
                "None",
                "Office",
                true));

        em.addEquipment(new AVEquipment(
                "Speaker A3",
                "Speakers Big",
                "HS038775",
                "Hercules",
                "Not set",
                "None",
                "Annex",
                true));

        em.addEquipment(new AVEquipment(
                "Speaker S1",
                "Speakers Small",
                "HS585289",
                "Amex",
                "Not set",
                "None",
                "Storage",
                true));

        em.addEquipment(new AVEquipment(
                "Speaker S2",
                "Speakers Small",
                "HS585289",
                "Amex",
                "Not set",
                "None",
                "Office",
                true));

        em.addEquipment(new AVEquipment(
                "Speaker S3",
                "Speakers Small",
                "HS585289",
                "Amex",
                "Not set",
                "None",
                "Office",
                true));

        em.addEquipment(new AVEquipment(
                "Mic M1",
                "Microphone",
                "MC09812",
                "Hercules",
                "Not set",
                "None",
                "Storage",
                true));

        em.addEquipment(new AVEquipment(
                "Mic M2",
                "Microphone",
                "MC88837",
                "Hercules",
                "Not set",
                "None",
                "Storage",
                true));

        em.addEquipment(new AVEquipment(
                "Mic M3",
                "Microphone",
                "MC091238",
                "Hercules",
                "Not set",
                "None",
                "Annex Storage",
                true));

        em.addEquipment(new AVEquipment(
                "Mic M4",
                "Microphone",
                "MC87471",
                "Hercules",
                "Not set",
                "None",
                "Office",
                true));

        System.out.println("createInitialEquipmentFiles done...");
    }

    public static void createInitialBulletinBoard() {
        System.out.println("createInitialBulletinBoard called...");
        AVEMInfo.updateAnnouncement("" +
                "7/28/21\n Crew Meeting for Overtime schedules - Sir Manny" +
                "\n\n8/01/21\n Arrival of New Uniform" +
                "\n\n\n[Sir Tusoy] Hoy kinsay nagkuha sakong payong? Palihug iuli T.T" +
                "\n\n\nPROTOCOL FOR MANDALA 2021 SUPPORT CREW: \n" +
                "\n1. Make sure to punch-in and inform your supervisors." +
                "\n2. See equipment's check list provided by Sir Agapito. " +
                "\n3. Always ask for help when you don't know what you are doing." +
                "\n4. Stackoverflow is your friend.");
    }

    public static void createInitialAccounts() {
        AVEMInfo avemInfo = new AVEMInfo();
        avemInfo.addAccount(new AVAccount("raol", "comeros", AVAccount.ADMIN, "Raul"));
        avemInfo.addAccount(new AVAccount("saycon", "saycon", AVAccount.ADMIN, "Renzo"));
        avemInfo.addAccount(new AVAccount("seblos", "seblos", AVAccount.ADMIN, "Sam"));
        avemInfo.addAccount(new AVAccount("quiros", "quiros", AVAccount.ADMIN, "Pol"));
        System.out.println("--------ALL ACCOUNTS INITIALIZED: " + avemInfo.getHashMap().values() + "\n----");
    }
}

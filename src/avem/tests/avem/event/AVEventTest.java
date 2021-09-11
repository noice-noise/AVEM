package avem.event;

import avem.core.AVDate;
import avem.core.AVTime;
import junit.framework.TestCase;
import org.junit.Test;

public class AVEventTest extends TestCase {

    @Test
    public void testDate() {
        AVDate date = new AVDate("12", "12", "2012");
        System.out.println(date.toString());
        assertFalse("12/12/2012" == date.toString());
        assertTrue("12/12/2012".equals(date.toString()));

        AVDate newDate = new AVDate("72/22/2021");
        System.out.println("newDate: " + newDate);
        assertEquals(true, "72/22/2021".equals(newDate.toString()));
        assertEquals(false, "723/232/2021".equals(newDate.toString()));
        assertEquals(false, "1/1/2021".equals(newDate.toString()));


        try {
            new AVDate("1/2/20");
            fail();
        } catch (Exception e) {

        }
    }

    public void testDateConstructor() {
        try {
            new AVDate("2222/2221/2222");
            fail();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            new AVDate("2323213245");
            fail();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            new AVDate("1/2/");
            fail();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            new AVDate(null);
            fail();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void testInvalidTime() {
        try {
            new AVTime("123:32 ZM");
            fail();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void testSingleNumberTime(){
        try {
            new AVTime("1:32 AM");
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testAnotherInvalidTime(){
        try {
            new AVTime(": AM");
            fail();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testTimeEquals() {
        AVTime time = new AVTime("12", "12", "PM");
        assertTrue("12:12 PM".equals(time.toString()));
        assertTrue(time.equals(new AVTime("12:12 PM")));

        assertEquals(false, time.equals(new AVTime("10:32 PM")));
    }
}
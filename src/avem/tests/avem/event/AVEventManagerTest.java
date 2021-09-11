package avem.event;

import avem.core.AVEventManager;
import junit.framework.TestCase;

public class AVEventManagerTest extends TestCase {


    public void testInstance() {
        AVEventManager ev = new AVEventManager();
    }


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        AVEventManager ev = new AVEventManager();
        ev.getFile().delete();
        ev.getDirectory().delete();
    }
}
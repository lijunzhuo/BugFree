package com.example.mac.bugfree;

import android.test.ActivityInstrumentationTestCase2;

import com.example.mac.bugfree.activity.MainActivity;
import com.example.mac.bugfree.exception.MoodStateNotAvailableException;
import com.example.mac.bugfree.module.MoodEvent;
import com.example.mac.bugfree.module.MoodEventList;
import com.example.mac.bugfree.module.User;

import java.util.ArrayList;
import java.util.GregorianCalendar;


/**
 * @author Zhi Li
 */

public class UserTest extends ActivityInstrumentationTestCase2 {
    public UserTest(){ super(MainActivity.class);}

    // Test Getter and Setters
    public void testGetterSetter(){
        User usr = new User("John");

        ArrayList<String> AL1 = new ArrayList<>();
        ArrayList<String> AL2 = new ArrayList<>();
        ArrayList<String> AL3 = new ArrayList<>();

        AL1.add("Sam");
        AL1.add("Lily");
        AL1.add("Ray");
        AL2.addAll(AL1);

        usr.setFolloweeIDs(AL1);
        usr.setFollowerIDs(AL2);

        usr.setPendingPermissions(AL3);

        MoodEventList MEL = new MoodEventList();
        try{MoodEvent mood = new MoodEvent("Happy", usr.getUsr());
            MEL.addMoodEvent(mood);
            MoodEvent mood1 = new MoodEvent("Sad",usr.getUsr());
            mood1.setDateOfRecord(new GregorianCalendar(2017,2,2,15,16,17));
            mood1.setRealtime(new GregorianCalendar(2017,2,2,15,16,17));

            MEL.addMoodEvent(mood1);
        }
        catch (MoodStateNotAvailableException e){}
        usr.setMoodEventList(MEL);
        try {
            assertEquals("John",usr.getUsr());
            assertEquals(AL1,usr.getFolloweeIDs());
            assertEquals(AL2,usr.getFollowerIDs());
            assertEquals(AL3,usr.getPendingPermission());
            assertEquals(MEL, usr.getMoodEventList());
        }catch (Exception e){
            fail();
        }
    }

}

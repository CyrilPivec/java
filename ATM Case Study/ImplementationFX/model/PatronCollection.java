// specify the package
package model;

// system imports
import java.util.Properties;
import java.util.Vector;
import javafx.scene.Scene;

// project imports
import exception.InvalidPrimaryKeyException;
import event.Event;
import database.*;

import impresario.IView;

import userinterface.View;
import userinterface.ViewFactory;


/** The class containing the PatronCollection for the ATM application */
//==============================================================
public class PatronCollection  extends EntityBase implements IView
{
    private static final String myTableName = "Patron";

    private Vector<Patron> patrons;
    // GUI Components

    // constructor for this class
    //----------------------------------------------------------
    public PatronCollection( ) throws
            Exception
    {
        super(myTableName);
        patrons = new Vector<Patron>();

    }

    public void findPatronsOlderThan(String date) {
        String query = "SELECT * FROM " + myTableName + " WHERE(dateOfBirth < " + date + ")";

        Vector allDataRetrieved = getSelectQueryResult(query);
        if (allDataRetrieved != null) {
            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++) {
                Properties nextAccountData = (Properties) allDataRetrieved.elementAt(cnt);

                Patron patron = new Patron(nextAccountData);

                if (patron != null) {
                    addPatron(patron);
                    //System.out.println(books);
                }
            }
        }
        System.out.println(allDataRetrieved);

    }

    public void findPatronsYoungerThan(String date) {
        String query = "SELECT * FROM " + myTableName + " WHERE(dateOfBirth > " + date + ")";

        Vector allDataRetrieved = getSelectQueryResult(query);
        if (allDataRetrieved != null) {
            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++) {
                Properties nextAccountData = (Properties) allDataRetrieved.elementAt(cnt);

                Patron patron = new Patron(nextAccountData);

                if (patron != null) {
                    addPatron(patron);
                    //System.out.println(books);
                }
            }
        }
        System.out.println(allDataRetrieved);

    }

    public void findPatronsAtZipCode(String zip) {
        String query = "SELECT * FROM " + myTableName + " WHERE(zip = " + zip + ")";

        Vector allDataRetrieved = getSelectQueryResult(query);
        if (allDataRetrieved != null) {
            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++) {
                Properties nextAccountData = (Properties) allDataRetrieved.elementAt(cnt);

                Patron patron = new Patron(nextAccountData);

                if (patron != null) {
                    addPatron(patron);
                    //System.out.println(books);
                }
            }
        }
        System.out.println(allDataRetrieved);

    }

    public void findPatronsWithNameLike(String name) {
        String query = "SELECT * FROM " + myTableName + " WHERE(name LIKE '%" + name + "%')";

        Vector allDataRetrieved = getSelectQueryResult(query);
        if (allDataRetrieved != null) {
            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++) {
                Properties nextAccountData = (Properties) allDataRetrieved.elementAt(cnt);

                Patron patron = new Patron(nextAccountData);

                if (patron != null) {
                    addPatron(patron);
                    //System.out.println(books);
                }
            }
        }
        System.out.println(allDataRetrieved);

    }


    //----------------------------------------------------------------------------------
    private void addPatron(Patron a)
    {
        //patrons.add(a);
        int index = findIndexToAdd(a);
        patrons.insertElementAt(a,index); // To build up a collection sorted on some key
    }

    //----------------------------------------------------------------------------------
    private int findIndexToAdd(Patron a)
    {
        //users.add(u);
        int low=0;
        int high = patrons.size()-1;
        int middle;

        while (low <=high)
        {
            middle = (low+high)/2;

            Patron midSession = patrons.elementAt(middle);

            int result = Patron.compare(a,midSession);

            if (result ==0)
            {
                return middle;
            }
            else if (result<0)
            {
                high=middle-1;
            }
            else
            {
                low=middle+1;
            }


        }
        return low;
    }


    /**
     *
     */
    //----------------------------------------------------------
    public Object getState(String key)
    {
        if (key.equals("Patrons"))
            return patrons;
        else
        if (key.equals("PatronList"))
            return this;
        return null;
    }

    //----------------------------------------------------------------
    public void stateChangeRequest(String key, Object value)
    {

        myRegistry.updateSubscribers(key, this);
    }

    //----------------------------------------------------------
    public Patron retrieve(String patronNumber)
    {
        Patron retValue = null;
        for (int cnt = 0; cnt < patrons.size(); cnt++)
        {
            Patron nextAcct = patrons.elementAt(cnt);
            String nextAccNum = (String)nextAcct.getState("PatronNumber");
            if (nextAccNum.equals(patronNumber) == true)
            {
                retValue = nextAcct;
                return retValue; // we should say 'break;' here
            }
        }

        return retValue;
    }

    /** Called via the IView relationship */
    //----------------------------------------------------------
    public void updateState(String key, Object value)
    {
        stateChangeRequest(key, value);
    }

    //------------------------------------------------------
    protected void createAndShowView()
    {

        Scene localScene = myViews.get("PatronCollectionView");

        if (localScene == null)
        {
            // create our new view
            View newView = ViewFactory.createView("PatronCollectionView", this);
            localScene = new Scene(newView);
            myViews.put("PatronCollectionView", localScene);
        }
        // make the view visible by installing it into the frame
        swapToView(localScene);

    }

    //-----------------------------------------------------------------------------------
    protected void initializeSchema(String tableName)
    {
        if (mySchema == null)
        {
            mySchema = getSchemaInfo(tableName);
        }
    }
}

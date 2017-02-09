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


/** The class containing the BookCollection for the ATM application */
//==============================================================
public class BookCollection  extends EntityBase implements IView
{
	private static final String myTableName = "Book";

	private Vector<Book> books;
	// GUI Components

	// constructor for this class
	//----------------------------------------------------------
	public BookCollection( BookHolder cust) throws
		Exception
	{
		super(myTableName);

		if (cust == null)
		{
			new Event(Event.getLeafLevelClassName(this), "<init>",
				"Missing book holder information", Event.FATAL);
			throw new Exception
				("UNEXPECTED ERROR: BookCollection.<init>: book holder information is null");
		}

		String bookHolderId = (String)cust.getState("ID");

		if (bookHolderId == null)
		{
			new Event(Event.getLeafLevelClassName(this), "<init>",
				"Data corrupted: Book Holder has no id in database", Event.FATAL);
			throw new Exception
			 ("UNEXPECTED ERROR: BookCollection.<init>: Data corrupted: book holder has no id in repository");
		}

		String query = "SELECT * FROM " + myTableName + " WHERE (OwnerID = " + bookHolderId + ")";

		Vector allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null)
		{
			books = new Vector<Book>();

			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
			{
				Properties nextBookData = (Properties)allDataRetrieved.elementAt(cnt);

				Book book = new Book(nextBookData);

				if (book != null)
				{
					addBook(book);
				}
			}

		}
		else
		{
			throw new InvalidPrimaryKeyException("No books for customer : "
				+ bookHolderId + ". Name : " + cust.getState("Name"));
		}

	}

	//----------------------------------------------------------------------------------
	private void addBook(Book a)
	{
		//books.add(a);
		int index = findIndexToAdd(a);
		books.insertElementAt(a,index); // To build up a collection sorted on some key
	}

	//----------------------------------------------------------------------------------
	private int findIndexToAdd(Book a)
	{
		//users.add(u);
		int low=0;
		int high = books.size()-1;
		int middle;

		while (low <=high)
		{
			middle = (low+high)/2;

			Book midSession = books.elementAt(middle);

			int result = Book.compare(a,midSession);

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
		if (key.equals("Books"))
			return books;
		else
		if (key.equals("BookList"))
			return this;
		return null;
	}

	//----------------------------------------------------------------
	public void stateChangeRequest(String key, Object value)
	{
		
		myRegistry.updateSubscribers(key, this);
	}

	//----------------------------------------------------------
	public Book retrieve(String bookNumber)
	{
		Book retValue = null;
		for (int cnt = 0; cnt < books.size(); cnt++)
		{
			Book nextAcct = books.elementAt(cnt);
			String nextAccNum = (String)nextAcct.getState("BookNumber");
			if (nextAccNum.equals(bookNumber) == true)
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

		Scene localScene = myViews.get("BookCollectionView");

		if (localScene == null)
		{
				// create our new view
				View newView = ViewFactory.createView("BookCollectionView", this);
				localScene = new Scene(newView);
				myViews.put("BookCollectionView", localScene);
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

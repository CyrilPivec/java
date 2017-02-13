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

	private Vector<Book> book;
	// GUI Components

	// constructor for this class
	//----------------------------------------------------------
	public BookCollection( ) throws
		Exception
	{
		super(myTableName);
		book = new Vector<Book>();
	}

	public void findBooksOlderThanDate(String year) {
		String query = "SELECT * FROM " + myTableName + " WHERE(pubYear > " + year + ")";

		Vector allDataRetrieved = getSelectQueryResult(query);
		if (allDataRetrieved != null) {
			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++) {
				Properties nextAccountData = (Properties) allDataRetrieved.elementAt(cnt);

				Book books = new Book(nextAccountData);

				if (books != null) {
					addBook(books);
					//System.out.println(books);
				}
			}
		}
		System.out.println(allDataRetrieved);

	}

	public void findBooksNewerThanDate(String year) {

		String query = "SELECT * FROM " + myTableName + " WHERE(pubYear < " + year + ")";

		Vector allDataRetrieved = getSelectQueryResult(query);
		if (allDataRetrieved != null) {
			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++) {
				Properties nextAccountData = (Properties) allDataRetrieved.elementAt(cnt);

				Book books = new Book(nextAccountData);

				if (books != null) {
					addBook(books);
					//System.out.println(books);
				}
			}
		}
		System.out.println(allDataRetrieved);

	}

	public void findBooksWithTitleLike(String title) {
		String query = "SELECT * FROM " + myTableName + " WHERE title LIKE '%" + title + "%'";

		Vector allDataRetrieved = getSelectQueryResult(query);
		if (allDataRetrieved != null) {
			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++) {
				Properties nextAccountData = (Properties) allDataRetrieved.elementAt(cnt);

				Book books = new Book(nextAccountData);

				if (books != null) {
					addBook(books);
					//System.out.println(books);
				}
			}
		}
		System.out.println(allDataRetrieved);

	}

	private void addBook(Book a)
	{
		//accounts.add(a);
		int index = findIndexToAdd(a);
		book.insertElementAt(a,index); // To build up a collection sorted on some key
	}

	//----------------------------------------------------------------------------------
	private int findIndexToAdd(Book a)
	{
		//users.add(u);
		int low=0;
		int high = book.size()-1;
		int middle;

		while (low <=high)
		{
			middle = (low+high)/2;

			Book midSession = book.elementAt(middle);

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

	//----------------------------------------------------------------------------------


	//----------------------------------------------------------------------------------



	/**
	 *
	 */
	//----------------------------------------------------------
	public Object getState(String key)
	{
		if (key.equals("Books"))
			return book;
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

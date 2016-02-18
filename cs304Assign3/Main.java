import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.*;

public class Main {
	private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	static int choice;
	static boolean quit;
	private static Connection con;
	
	public static void main(String[] args){
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@dbhost.ugrad.cs.ubc.ca:1522:ug", "ora_t2d0b", "a30783138");
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		try {
			BufferedReader br;
			br = new BufferedReader(
					new InputStreamReader(new FileInputStream("asg3.2015.sql.create.txt")));

			String line;
			Statement stmt = con.createStatement();
			
			// may need to execute these lines on first time runnning
			//stmt.executeUpdate("create table item (upc char(6))");
			//stmt.executeUpdate("create table book (upo char(6))");
			//stmt.executeUpdate("create table purchase (ura char(6))");
			//stmt.executeUpdate("create table itempurchase (t_id char(6))");
			 
			

			while ((line = br.readLine()) != null) {	    	

				if (line.length() > 5) {
					line = line.substring(0, line.length()-2);
					stmt.executeUpdate(line);
					System.out.println(line);
				}
			}
			
			


			br.close();
			br = new BufferedReader(
					new InputStreamReader(new FileInputStream("asg3.2015.sql.data.txt")));

			while ((line = br.readLine()) != null) {	    	

				System.out.println(line);
				if (line.length() > 5) {
					line = line.substring(0, line.length()-1);
					System.out.println(line);
					int rowCount = stmt.executeUpdate(line);
				}
			}
			br.close();	   }
		catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		quit = false;	
		while (!quit)
		{
			System.out.print("\n\nPlease choose one of the following: \n");
			System.out.print("1.  Enter a new Item\n");
			System.out.print("2.  Remove an Existing Item\n");
			System.out.print("3.  Print textbooks > 50 copies sold last week with less than 10 left\n");
			System.out.print("4.  Print top 3 selling items\n");
			System.out.print("5.  Quit\n>> ");

			try {
				choice = Integer.parseInt(in.readLine());
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println(" ");

			switch(choice)
			{
			case 1:  insertItem(); break;
			case 2:  removeItem(); break;
			case 3:  printTextbooks(); break;
			case 4:  printTopSellers(); break;
			case 5:  quit = true;
			}
		}

		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("\nGood Bye!\n\n");
		System.exit(0);
		}


private static void removeItem() {
	String upc = "";
	PreparedStatement  ps;
	PreparedStatement ps2;
	try {
		System.out.println("Is it a book? yes = 1, no = 0");
		try {
			choice = Integer.parseInt(in.readLine());
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		switch(choice){
		case 1:
			
			ps = con.prepareStatement("DELETE FROM BOOK WHERE upc = ?");
			ps2 = con.prepareStatement("DELETE FROM ITEM WHERE upc = ? AND stock = 0");
			System.out.println("To delete book, please enter its UPC");
			try {
				upc = in.readLine();
				ps.setString(1, upc);
				ps2.setString(1, upc);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ps.executeUpdate();

			System.out.println("BOOK " + upc + " deleted from book");
			
			ps2.executeUpdate();
			System.out.println("BOOK " + upc + " deleted from item");
			con.commit();
			break;
		case 0:
			
			ps2 = con.prepareStatement("DELETE FROM ITEM WHERE upc = ? AND stock=0");
			// for some reason it isn't checking the stock
			System.out.println("To delete non-book, please enter its UPC");
			try {
				upc = in.readLine();
				ps2.setString(1, upc);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ps2.executeUpdate();
			con.commit();
			System.out.println("NON-BOOK " + upc + " deleted");
			break;
		}
	} catch (SQLException e) {
		System.out.println("Item can't be deleted, stock is not 0");
		try 
	    {
		con.rollback();	
	    }
	    catch (SQLException ex2)
	    {
		System.out.println("Message: " + ex2.getMessage());
		System.exit(-1);
	    }
		e.printStackTrace();
	}
}

private static void printTextbooks() { 
	Statement stmt;
	try {
		stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery (
				
				"SELECT upc, sum(quantity) FROM (SELECT DISTINCT i.upc, x.quantity, b.TITLE from ITEM i, BOOK b, ITEMPURCHASE x, PURCHASE p WHERE p.t_id = x.t_id AND i.UPC = b.UPC AND b.UPC = x.UPC AND i.UPC = x.UPC AND i.stock <=9 INTERSECT select DISTINCT i.upc, x.quantity, b.TITLE from  ITEM i, BOOK b, ITEMPURCHASE x, PURCHASE P WHERE P.purchasedate = '15-OCT-28'"	 
				+ "OR P.purchasedate = '15-OCT-29'  OR P.purchasedate = '15-OCT-25' OR P.purchasedate = '15-OCT-26' OR P.purchasedate = '15-OCT-27' OR P.purchasedate = '15-OCT-31'OR P.purchasedate = '15-OCT-30') "
				 + "group by upc HAVING sum(quantity) > 50"

				);
		
		 while (rs.next()) {
			System.out.println("BOOK " + rs.getString("upc"));
		 }
		 stmt.close();
	} catch (SQLException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}

}

private static void printTopSellers() {  
	Statement stmt;
	try {
		stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery (
	
	" SELECT * FROM(SELECT upc, quantity*sellingprice AS sales FROM ( "
	+ "SELECT DISTINCT i.upc, x.quantity, i.sellingprice from ITEM i, ITEMPURCHASE x, PURCHASE p WHERE "
	+" p.t_id = x.t_id AND i.UPC = x.UPC INTERSECT "
	+" select DISTINCT i.upc, x.quantity, i.sellingprice from  ITEM i, ITEMPURCHASE x, PURCHASE p WHERE " 
	+" P.purchasedate = '15-OCT-28'	 	OR P.purchasedate = '15-OCT-29' "
	 +" OR P.purchasedate = '15-OCT-25'	OR P.purchasedate = '15-OCT-26' "
	+" OR P.purchasedate = '15-OCT-27'	 OR P.purchasedate = '15-OCT-31' "
	+" OR P.purchasedate = '15-OCT-30')	ORDER BY sales DESC)	WHERE rownum <= 3	ORDER BY rownum"
				);
		int i = 1;
		while (rs.next()) {
			
			System.out.println("ITEM " + "RANKED " + i + " " + rs.getString("upc"));
			i++;
		 }
	} catch (SQLException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
}

private static void insertItem() {
	String upc;
	int sellingprice;
	int stock;
	String taxable;
	PreparedStatement  ps;
	String title;
	String publisher;
	String flagtext;
	

	try
	{
		ps = con.prepareStatement("INSERT INTO ITEM VALUES (?,?,?,?)");
		
		System.out.print("\nItem UPC: ");
		upc= in.readLine();
		ps.setString(1, upc);
		
		System.out.print("\nItem Selling-Price: ");
		sellingprice = Integer.parseInt(in.readLine());
		ps.setFloat(2, sellingprice);
		
		System.out.print("\nItem Stock: ");
		stock = Integer.parseInt(in.readLine());
		ps.setInt(3, stock);

		System.out.print("\nTaxable? ");
		taxable = (in.readLine());
		ps.setString(4, taxable);
		
		ps.executeUpdate();
		System.out.println("Is it a book? yes = 1, no = 0");
		
		try {
			choice = Integer.parseInt(in.readLine());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		switch(choice)
		{
		case 1:
		ps = con.prepareStatement("INSERT INTO BOOK VALUES (?,?,?,?)");
		
		ps.setString(1, upc);
		
		System.out.println("Enter Book Title");
		title = in.readLine();
		ps.setString(2, title);
		
		System.out.println("Enter Book Publisher");
		publisher = in.readLine();
		ps.setString(3, publisher);
		
		System.out.println("Flag Text? y/n");
		flagtext = in.readLine();
		ps.setString(4, flagtext);
		
		ps.executeUpdate();
		con.commit();
		System.out.println("BOOK " + upc + " inserted");
		break;
		
		case 0:
		// commit work 
		con.commit();
			
		System.out.println("NON-BOOK " + upc + " inserted");
		break;
		}
		
		
		ps.close();

	}
	catch (SQLException ex)
	{
		System.out.println("Message: " + ex.getMessage());
		try 
		{
			// undo the insert
			con.rollback();	
		}
		catch (SQLException ex2)
		{
			System.out.println("Message: " + ex2.getMessage());
			System.exit(-1);
		}
	}
	catch (IOException ex){
		System.out.println(ex.getMessage());
	}
	
}



}



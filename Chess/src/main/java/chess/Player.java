package chess;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * -------ORIGINAL-------
 * This is the Player Class
 * It provides the functionality of keeping track of all the users
 * Objects of this class is updated and written in the Game's Data Files after every Game
 * 
 * -------UPDATED-------
 * Additional documentation
 * Fixed formatting, naming conventions, and switched to C style brackets (personal preference)
 */


public class Player implements Serializable 
{
	private String name;
	private int gamesPlayed;
	private int gamesWon;

	// Constructor
	public Player(String name) 
	{
		this.name = name.trim();
		gamesPlayed = 0;
		gamesWon = 0;
	}

	// Name Getter
	public String name() 
	{
		return name;
	}

	// Games Played Getter
	public int gamesPlayed() 
	{
		return gamesPlayed;
	}

	// Games Won Getter
	public int gamesWon() 
	{
		return gamesWon;
	}

	// Win/Loss Ratio
	public Integer winPercent() 
	{
		return (int) ((gamesWon * 100) / gamesPlayed);
	}

	// Increments the number of games played
	public void updateGamesPlayed() 
	{
		gamesPlayed++;
	}

	// Increments the number of games won
	public void updateGamesWon() 
	{
		gamesWon++;
	}

	// Function to fetch the list of the players
	public static ArrayList<Player> fetchPlayers() 
	{
		// Variables
		Player tempPlayer;
		ObjectInputStream input = null;
		ArrayList<Player> players = new ArrayList<Player>();

		// Try to read chessgamedata.dat
		try 
		{
			File inFile = new File(System.getProperty("user.dir") + File.separator + "chessgamedata.dat");
			input = new ObjectInputStream(new FileInputStream(inFile));

			// Try to read from input file
			try 
			{
				// Read the Player Objects from .dat file
				while (true) 
				{
					tempPlayer = (Player) input.readObject();
					players.add(tempPlayer);
				}
			}
			// Close if end of file
			catch (EOFException e) 
			{
				input.close();
			}
		}
		// Clear player list if not found
		catch (FileNotFoundException e) 
		{
			players.clear();
			return players;
		}
		// Print IO Exception
		catch (IOException e) 
		{

			e.printStackTrace();

			//Try to close stream
			try 
			{
				input.close();
			} 
			catch (IOException ex) 
			{
				JOptionPane.showMessageDialog(null, "Input stream was invalid!!!");
			}

			JOptionPane.showMessageDialog(null, "Unable to read the required Game files !!");
		}
		// Player Class not found
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Game Data File Corrupted !! Click Ok to Continue Builing New File");
		}
		// General Excpetion catch
		catch (Exception ex) 
		{
			ex.printStackTrace();
		}

		// Return current Player list
		return players;
	}

	// Function to update the statistics of a player
	public void updatePlayer() 
	{
		// Variables
		ObjectInputStream input = null;
		ObjectOutputStream output = null;
		Player tempPlayer;
		File inputFile = null;
		File outputFile = null;

		// Try to read chessgamedata.dat and write to tempfile.dat
		try 
		{
			inputFile = new File(System.getProperty("user.dir") + File.separator + "chessgamedata.dat");
			outputFile = new File(System.getProperty("user.dir") + File.separator + "tempfile.dat");
		}
		// Close program if no read/write
		catch (SecurityException e) 
		{
			JOptionPane.showMessageDialog(null, "Read-Write Permission Denied !! Program Cannot Start");
			System.exit(0);
		}

		// Does player exist in file?
		boolean playerNotExist;

		// Try writing new Player Object data
		try 
		{
			// If output file doesn't exist, create it
			if (outputFile.exists() == false)
				outputFile.createNewFile();

			// If input file doesn't exist, write this Player object to file
			if (inputFile.exists() == false) 
			{
				output = new ObjectOutputStream(new java.io.FileOutputStream(outputFile, true));
				output.writeObject(this);
			}
			// Else, we are adding a new Player to the file
			else 
			{
				// Variables
				input = new ObjectInputStream(new FileInputStream(inputFile));
				output = new ObjectOutputStream(new FileOutputStream(outputFile));
				playerNotExist = true;

				// Try to read and write data from file objects (defined above)
				try {
					// Read from input and write to output
					while (true) {
						tempPlayer = (Player) input.readObject();

						// If the new Player has the name of this Player Object, write this Player
						if (tempPlayer.name().equals(name())) 
						{
							output.writeObject(this);

							// After writing, mark the Player as existing
							playerNotExist = false;
						}
						// Else, write new Player to output
						else
							output.writeObject(tempPlayer);
					}
				}
				// Close if end of file
				catch (EOFException e) 
				{
					input.close();
				}

				// If this Player was not written to file (marked above), write this Player
				if (playerNotExist)
					output.writeObject(this);
			}
			// Delete outdated file
			inputFile.delete();
			// Close output stream
			output.close();

			// Create chessgamedata.dat with new information
			File newf = new File(System.getProperty("user.dir") + File.separator + "chessgamedata.dat");
			// Cannot rename output to chessgamedata.dat
			if (outputFile.renameTo(newf) == false)
				System.out.println("File Renaming Unsuccessful");
		}
		// Input/Output files cannot be found
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		// Print IO Exception
		catch (IOException e) 
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Unable to read/write the required Game files !! Press OK to continue");
		}
		// Player Class not found
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Game Data File Corrupted !! Click OK to Continue Builing New File");
		}
		// General Exception catch
		catch (Exception ex) 
		{
			ex.printStackTrace();
		}
	}
}

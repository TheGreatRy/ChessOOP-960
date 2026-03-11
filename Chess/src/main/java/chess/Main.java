package chess;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pieces.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Random;


/** 
 * @author ORIGINAL: Ashish Kedia and Adarsh Mohata | UPDATED: Ry Ellender
 */

/**
 * -------ORIGINAL-------
 * This is the Main Class of our project.
 * All GUI Elements are declared, initialized and used in this class itself.
 * It is inherited from the JFrame Class of Java's Swing Library.
 * 
 * -------UPDATED-------
 * Additional documentation
 * Fixed formatting, naming conventions, and switched to C style brackets (personal preference)
 * 
 * Game mode Chess960 is being added
 * Chess960 randomizes the ranked rows of both sets. The ranked row is the edge row with the non-pawn pieces
 * 
 * SETUP RULES: 
 * 	1. The bishops must be placed on opposite-color cell.
 * 	2. The king must be placed on a cell between the rooks. 
 * 	>> VALID: R--K--R | INVALID: R--R--K or K--R--R
 * 	3. White and Black's ranked rows are mirrored
 * 
 * The mode uses classic chess rules, and the original setup will still be available
 */

public class Main extends JFrame implements MouseListener 
{
	// Variable Declaration
	
	//#region Board
	public static Main mainBoard;
	private static final int Height = 800;
	private static final int Width = 1110;
	//#endregion
	
	//#region Player
	private Player whitePlayer = null, blackPlayer = null;
	private Player tempPlayer;
	private ArrayList<Player> itemWPlayer, itemBPlayer;
	private boolean selected = false, end = false;
	//#endregion

	//#region Pieces
	private static Rook wR01, wR02, bR01, bR02;
	private static Knight wN01, wN02, bN01, bN02;
	private static Bishop wB01, wB02, bB01, bB02;
	private static Pawn wP[], bP[];
	private static Queen wQ, bQ;
	private static King wK, bK;
	//#endregion

	//#region Cells
	private int playerTurn = 0;
	private Cell c, previous;
	private Cell boardState[][];
	private ArrayList<Cell> destinationList = new ArrayList<Cell>();
	//#endregion
	
	//#region Timer
	private Time timer;
	public static int timeRemaining = 60;
	//#endregion

	//#region GUI 
	private JPanel panBoard = new JPanel(new GridLayout(8, 8));
	private JPanel panWDetails = new JPanel(new GridLayout(3, 3));
	private JPanel panBDetails = new JPanel(new GridLayout(3, 3));
	private JPanel panWCombo = new JPanel();
	private JPanel panBCombo = new JPanel();
	private JPanel panControl, panWPlayer, panBPlayer, panTemp, panDisplayTime, panShowPlayer, panTime, panMode;
	
	private Container content;
	private ArrayList<String> listWNames = new ArrayList<String>();
	private ArrayList<String> listBNames = new ArrayList<String>();
	
	private String[] arrWNames = {}, arrBNames = {};
	private JComboBox<String> wCombo, bCombo;
	private String strWName = null, strBName = null, strWinner = null;
	static 	String strMove;
	
	private static JLabel labPlayerTurn;
	private JLabel labTime, labMove, labSetTimer, labSetMode;
	
	private JSplitPane split;
	private JScrollPane scrollW, scrollB;
	private JSlider timeSlider;
	private BufferedImage image;
	private Button bttnStart, bttnWSelectPlayer, bttnBSelectPlayer, bttnWCreatePlayer, bttnBCreatePlayer;
	
	private JRadioButton radClassic, radNineSixty;
	private ButtonGroup grpRadioButtons = new ButtonGroup();
	private boolean isClassic = true;
	//#endregion

	public static void main(String[] args) {

		// variable initialization
		wR01 = new Rook("wR01", "/chess/White_Rook.png", 0);
		wR02 = new Rook("wR02", "/chess/White_Rook.png", 0);
		bR01 = new Rook("bR01", "/chess/Black_Rook.png", 1);
		bR02 = new Rook("bR02", "/chess/Black_Rook.png", 1);
		wN01 = new Knight("wN01", "/chess/White_Knight.png", 0);
		wN02 = new Knight("wN02", "/chess/White_Knight.png", 0);
		bN01 = new Knight("bN01", "/chess/Black_Knight.png", 1);
		bN02 = new Knight("bN02", "/chess/Black_Knight.png", 1);
		wB01 = new Bishop("wB01", "/chess/White_Bishop.png", 0);
		wB02 = new Bishop("wB02", "/chess/White_Bishop.png", 0);
		bB01 = new Bishop("bB01", "/chess/Black_Bishop.png", 1);
		bB02 = new Bishop("bB02", "/chess/Black_Bishop.png", 1);
		wQ = new Queen("wQ", "/chess/White_Queen.png", 0);
		bQ = new Queen("bQ", "/chess/Black_Queen.png", 1);
		wK = new King("wK", "/chess/White_King.png", 0, 7, 3);
		bK = new King("bK", "/chess/Black_King.png", 1, 0, 3);
		wP = new Pawn[8];
		bP = new Pawn[8];
		for (int i = 0; i < 8; i++) {
			wP[i] = new Pawn("wP0" + (i + 1), "/chess/White_Pawn.png", 0);
			bP[i] = new Pawn("bP0" + (i + 1), "/chess/Black_Pawn.png", 1);
		}

		// Setting up the board
		mainBoard = new Main();
		mainBoard.setVisible(true);
		mainBoard.setResizable(false);

	}

	

	//Game Mode Choice
	private void RunSetup()
	{
		// Defining all the Cells
		Cell cell;
		pieces.Piece P;
		boardState = new Cell[8][8];

		if (isClassic)
		{
			//Standard Setup
			for (int i = 0; i < 8; i++)
			{
				for (int j = 0; j < 8; j++) {
					P = null;
					if (i == 0 && j == 0)
						P = bR01;
					else if (i == 0 && j == 7)
						P = bR02;
					else if (i == 7 && j == 0)
						P = wR01;
					else if (i == 7 && j == 7)
						P = wR02;
					else if (i == 0 && j == 1)
						P = bN01;
					else if (i == 0 && j == 6)
						P = bN02;
					else if (i == 7 && j == 1)
						P = wN01;
					else if (i == 7 && j == 6)
						P = wN02;
					else if (i == 0 && j == 2)
						P = bB01;
					else if (i == 0 && j == 5)
						P = bB02;
					else if (i == 7 && j == 2)
						P = wB01;
					else if (i == 7 && j == 5)
						P = wB02;
					else if (i == 0 && j == 3)
						P = bK;
					else if (i == 0 && j == 4)
						P = bQ;
					else if (i == 7 && j == 3)
						P = wK;
					else if (i == 7 && j == 4)
						P = wQ;
					else if (i == 1)
						P = bP[j];
					else if (i == 6)
						P = wP[j];
					cell = new Cell(i, j, P);
					cell.addMouseListener(this);
					panBoard.add(cell);
					boardState[i][j] = cell;
				}
			}
		}
		
		else
		{
			Random rand = new Random();
			// Chess960
			// Use White for setup (Rows 7 and 8)

			//Array of column values (ensures Bishops are on opposite colors)
			ArrayList<Integer> evens = new ArrayList<Integer>();
			evens.add(0);
			evens.add(2);
			evens.add(4);
			evens.add(6);

			ArrayList<Integer> odds = new ArrayList<Integer>();
			odds.add(1);
			odds.add(3);
			odds.add(5);
			odds.add(7);

			// Bishop Positions
			// First
			int bishop01Pos = rand.nextInt(4);
			
			P = wB01;
			cell = new Cell(7, evens.get(bishop01Pos), P);
			cell.addMouseListener(this);
			boardState[7][evens.get(bishop01Pos)] = cell;
			
			evens.remove(bishop01Pos);
			
			// Second
			int bishop02Pos = rand.nextInt(4);

			P = wB02;
			cell = new Cell(7, odds.get(bishop02Pos), P);
			cell.addMouseListener(this);
			
			boardState[7][odds.get(bishop02Pos)] = cell;

			odds.remove(bishop02Pos);

			//Update column values
			ArrayList<Integer> remainingCols = new ArrayList<Integer>();
			remainingCols.addAll(evens);
			remainingCols.addAll(odds);

			//Rook positions
			int rook01Col = 0;
			int rook02Col = 0;
			int kingPos = 0;

			while (Math.abs(rook01Col - rook02Col) < 2)
			{
				rook01Col = remainingCols.get(rand.nextInt(6));
				rook02Col = remainingCols.get(rand.nextInt(6));
				if (Math.abs(rook01Col - rook02Col) == 2)
				{
					kingPos = (rook01Col + rook02Col) / 2;
					if (boardState[7][kingPos] == null) break;
					else 
					{
						rook01Col = 0;
						rook02Col = 0;
					}
				}
				else if (Math.abs(rook01Col - rook02Col) > 2)
				{
					int lower = Math.min(rook01Col,rook02Col);
					int upper = Math.max(rook01Col,rook02Col);
					
					kingPos = (int) Math.floor((Math.random() * upper + 1) + lower+1);
				}
			}

			//Rooks and King
			P = wR01;
			cell = new Cell(7, rook01Col, P);
			cell.addMouseListener(this);
			
			boardState[7][rook01Col] = cell;
			
			P = wR02;
			cell = new Cell(7, rook02Col, P);
			cell.addMouseListener(this);
			
			boardState[7][rook02Col] = cell;
			
			P = wK;
			cell = new Cell(7, kingPos, P);
			cell.addMouseListener(this);
			
			boardState[7][kingPos] = cell;
			
			ArrayList<Integer> removeCols = new ArrayList<>();
			removeCols.add(rook01Col);
			removeCols.add(rook02Col);
			removeCols.add(kingPos);

			ArrayList<Integer> lastSet = new ArrayList<>();
			
			for (int item : remainingCols)
			{
				if (!removeCols.contains(item)) lastSet.add(item);
			}

			//3 positions left: Knights and Queen
			Collections.shuffle(lastSet);

			P = wN01;
			cell = new Cell(7, lastSet.get(0), P);
			cell.addMouseListener(this);
			
			boardState[7][lastSet.get(0)] = cell;

			P = wN02;
			cell = new Cell(7, lastSet.get(1), P);
			cell.addMouseListener(this);
			
			boardState[7][lastSet.get(1)] = cell;
			
			P = wQ;
			cell = new Cell(7, lastSet.get(2), P);
			cell.addMouseListener(this);
			
			boardState[7][lastSet.get(2)] = cell;

			//White ranked row is done, set up Black ranked row
			for (int i = 0; i < 8; i++)
			{
				int reverse = 7 - i;
				String piece = boardState[7][i].getPiece().getId().substring(1);
				switch (piece)
				{
					case "B01":
						P = bB01;
						cell = new Cell(0, reverse, P);
						cell.addMouseListener(this);
						
						boardState[0][reverse] = cell;
						break;
					case "B02":
						P = bB02;
						cell = new Cell(0, reverse, P);
						cell.addMouseListener(this);
						
						boardState[0][reverse] = cell;
						break;
					case "N01":
						P = bN01;
						cell = new Cell(0, reverse, P);
						cell.addMouseListener(this);
						
						boardState[0][reverse] = cell;
						break;
					case "N02":
						P = bN02;
						cell = new Cell(0, reverse, P);
						cell.addMouseListener(this);
						
						boardState[0][reverse] = cell;
						break;
					case "R01":
						P = bR01;
						cell = new Cell(0, reverse, P);
						cell.addMouseListener(this);
						
						boardState[0][reverse] = cell;
						break;
					case "R02":
						P = bR02;
						cell = new Cell(0, reverse, P);
						cell.addMouseListener(this);
						
						boardState[0][reverse] = cell;
						break;
					case "K":
						P = bK;
						cell = new Cell(0, reverse, P);
						cell.addMouseListener(this);
						
						boardState[0][reverse] = cell;
						break;
					case "Q":
						P = bQ;
						cell = new Cell(0, reverse, P);
						cell.addMouseListener(this);
						
						boardState[0][reverse] = cell;
						break;

				}
			}
			
			//Pawns
			for (int j = 0; j < 8; j++)
			{
				P = bP[j];
				cell = new Cell(1, j, P);
				cell.addMouseListener(this);
				
				boardState[1][j] = cell;
				
				P = wP[j];
				cell = new Cell(6, j, P);
				cell.addMouseListener(this);
				
				boardState[6][j] = cell;
			}

			//Blank spaces
			for (int i = 0; i < 8; i++)
			{
				for (int j = 0; j < 8; j++) {
					if (boardState[i][j] == null)
					{
						P = null;
						cell = new Cell(i, j, P);
						cell.addMouseListener(this);
						
						boardState[i][j] = cell;
					}
				}
			}
			
			//Add all the pieces
			for (int i = 0; i < 8; i++)
			{
				for (int j = 0; j < 8; j++) {
					panBoard.add(boardState[i][j]);
				}
			}
		}
	}
	
	// Constructor
	private Main() {

		SWITCH_RADIO getSwitch = new SWITCH_RADIO();
		timeRemaining = 60;
		timeSlider = new JSlider();
		strMove = "White";
		strWName = null;
		strBName = null;
		strWinner = null;
		panBoard = new JPanel(new GridLayout(8, 8));
		panWDetails = new JPanel(new GridLayout(3, 3));
		panBDetails = new JPanel(new GridLayout(3, 3));
		panBCombo = new JPanel();
		panWCombo = new JPanel();
		listWNames = new ArrayList<String>();
		listBNames = new ArrayList<String>();
		panBoard.setMinimumSize(new Dimension(800, 700));
		ImageIcon img = new ImageIcon(this.getClass().getResource("/chess/icon.png"));
		this.setIconImage(img.getImage());

		//#region Timer
		timeSlider.setMinimum(1);
		timeSlider.setMaximum(15);
		timeSlider.setValue(1);
		timeSlider.setMajorTickSpacing(2);
		timeSlider.setPaintLabels(true);
		timeSlider.setPaintTicks(true);
		timeSlider.addChangeListener(new TIME_CHANGE());
		//#endregion

		//#region Get Available Player
		itemWPlayer = Player.fetchPlayers();
		Iterator<Player> witr = itemWPlayer.iterator();
		while (witr.hasNext())
			listWNames.add(witr.next().name());

		itemBPlayer = Player.fetchPlayers();
		Iterator<Player> bitr = itemBPlayer.iterator();
		while (bitr.hasNext())
			listBNames.add(bitr.next().name());
		arrWNames = listWNames.toArray(arrWNames);
		arrBNames = listBNames.toArray(arrBNames);
		//#endregion

		//#region Board Setup
		
		panBoard.setBorder(BorderFactory.createLoweredBevelBorder());
		content = getContentPane();
		setSize(Width, Height);
		setTitle("Chess");
		content.setBackground(Color.black);
		panControl = new JPanel();
		content.setLayout(new BorderLayout());
		panControl.setLayout(new GridLayout(3, 3));
		panControl.setBorder(BorderFactory.createTitledBorder(null, "Statistics", TitledBorder.TOP,
				TitledBorder.CENTER, new Font("Lucida Calligraphy", Font.PLAIN, 20), Color.ORANGE));

		//#endregion

		//#region Player Box
		panWPlayer = new JPanel();
		panWPlayer.setBorder(BorderFactory.createTitledBorder(null, "White Player", TitledBorder.TOP,
				TitledBorder.CENTER, new Font("times new roman", Font.BOLD, 18), Color.RED));
		panWPlayer.setLayout(new BorderLayout());

		panBPlayer = new JPanel();
		panBPlayer.setBorder(BorderFactory.createTitledBorder(null, "Black Player", TitledBorder.TOP,
				TitledBorder.CENTER, new Font("times new roman", Font.BOLD, 18), Color.BLUE));
		panBPlayer.setLayout(new BorderLayout());
		//#endregion

		//#region Player Selection
		JPanel whitestats = new JPanel(new GridLayout(3, 3));
		JPanel blackstats = new JPanel(new GridLayout(3, 3));
		
		wCombo = new JComboBox<String>(arrWNames);
		bCombo = new JComboBox<String>(arrBNames);
		scrollW = new JScrollPane(wCombo);
		scrollB = new JScrollPane(bCombo);
		
		panWCombo.setLayout(new FlowLayout());
		panBCombo.setLayout(new FlowLayout());
		
		bttnWSelectPlayer = new Button("Select");
		bttnBSelectPlayer = new Button("Select");
		bttnWSelectPlayer.addActionListener(new SELECT_HANDLER(0));
		bttnBSelectPlayer.addActionListener(new SELECT_HANDLER(1));
		bttnWCreatePlayer = new Button("New Player");
		bttnBCreatePlayer = new Button("New Player");
		bttnWCreatePlayer.addActionListener(new HANDLER(0));
		bttnBCreatePlayer.addActionListener(new HANDLER(1));
		
		panWCombo.add(scrollW);
		panWCombo.add(bttnWSelectPlayer);
		panWCombo.add(bttnWCreatePlayer);
		panBCombo.add(scrollB);
		panBCombo.add(bttnBSelectPlayer);
		panBCombo.add(bttnBCreatePlayer);
		panWPlayer.add(panWCombo, BorderLayout.NORTH);
		panBPlayer.add(panBCombo, BorderLayout.NORTH);
		
		whitestats.add(new JLabel("Name   :"));
		whitestats.add(new JLabel("Played :"));
		whitestats.add(new JLabel("Won    :"));
		
		blackstats.add(new JLabel("Name   :"));
		blackstats.add(new JLabel("Played :"));
		blackstats.add(new JLabel("Won    :"));
		
		panWPlayer.add(whitestats, BorderLayout.WEST);
		panBPlayer.add(blackstats, BorderLayout.WEST);
		
		panControl.add(panWPlayer);
		panControl.add(panBPlayer);
		//#endregion

		//#region Display Timer and Game Mode
		panShowPlayer = new JPanel(new FlowLayout());
		panShowPlayer.add(timeSlider);
		labSetTimer = new JLabel("Set Timer(in mins):");
		labSetMode = new JLabel("Select Mode:");
		
		bttnStart = new Button("Start");
		bttnStart.setBackground(Color.black);
		bttnStart.setForeground(Color.white);
		bttnStart.addActionListener(new START());
		bttnStart.setPreferredSize(new Dimension(120, 40));
		
		panMode = new JPanel(new FlowLayout());
		radClassic = new JRadioButton();
		radClassic.setText("Classic Setup");
		radClassic.addActionListener(getSwitch);
		radClassic.setSelected(true);
		
		radNineSixty = new JRadioButton();
		radNineSixty.setText("Chess960");
		radNineSixty.addActionListener(getSwitch);

		grpRadioButtons.add(radClassic);	
		grpRadioButtons.add(radNineSixty);
		
		panMode.add(radClassic);
		panMode.add(radNineSixty);

		labSetTimer.setFont(new Font("Arial", Font.BOLD, 16));
		labSetMode.setFont(new Font("Arial", Font.BOLD, 16));
		
		labTime = new JLabel("Time Starts now", JLabel.CENTER);
		labTime.setFont(new Font("SERIF", Font.BOLD, 30));
		
		panDisplayTime = new JPanel(new FlowLayout());
		panTime = new JPanel(new GridLayout(5, 5));
		panTime.add(labSetTimer);
		panTime.add(panShowPlayer);
		panTime.add(labSetMode);
		panTime.add(panMode);
		panDisplayTime.add(bttnStart);
		panTime.add(panDisplayTime);
		//#endregion

		panControl.add(panTime);
		panBoard.setMinimumSize(new Dimension(800, 700));

		// The Left Layout When Game is inactive
		panTemp = new JPanel() 
		{
			@Override
			public void paintComponent(Graphics g) {
				try 
				{
					image = ImageIO.read(this.getClass().getResource("/chess/clash.jpg"));
				} 
				catch (IOException ex) 
				{
					System.out.println("Image not found!");
				}

				g.drawImage(image, 0, 0, null);
			}
		};

		panTemp.setMinimumSize(new Dimension(800, 700));
		panControl.setMinimumSize(new Dimension(285, 700));
		split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panTemp, panControl);

		content.add(split);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	// A function to change the playerTurn from White Player to Black Player or vice
	// verse
	// It is made public because it is to be accessed in the Time Class
	public void changePlayerTurn() {
		if (boardState[getKing(playerTurn).getx()][getKing(playerTurn).gety()].isCheck()) {
			playerTurn ^= 1;
			gameEnd();
		}
		if (destinationList.isEmpty() == false)
			cleanDestinations(destinationList);
		if (previous != null)
			previous.deselect();
		previous = null;
		playerTurn ^= 1;
		if (!end && timer != null) {
			timer.reset();
			timer.start();
			panShowPlayer.remove(labPlayerTurn);
			if (Main.strMove == "White")
				Main.strMove = "Black";
			else
				Main.strMove = "White";
			labPlayerTurn.setText(Main.strMove);
			panShowPlayer.add(labPlayerTurn);
		}
	}

	// A function to retrieve the Black King or White King
	private King getKing(int color) {
		if (color == 0)
			return wK;
		else
			return bK;
	}

	// A function to clean the highlights of possible destination cells
	private void cleanDestinations(ArrayList<Cell> destlist) // Function to clear the last move's destinations
	{
		ListIterator<Cell> it = destlist.listIterator();
		while (it.hasNext())
			it.next().removePossibleDestination();
	}

	// A function that indicates the possible moves by highlighting the Cells
	private void highlightDestinations(ArrayList<Cell> destlist) {
		ListIterator<Cell> it = destlist.listIterator();
		while (it.hasNext())
			it.next().setPossibleDestination();
	}

	// Function to check if the king will be in danger if the given move is made
	private boolean willKingBeInDanger(Cell fromcell, Cell tocell) {
		Cell newBoardState[][] = new Cell[8][8];
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++) {
				try {
					newBoardState[i][j] = new Cell(boardState[i][j]);
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
					System.out.println("There is a problem with cloning !!");
				}
			}

		if (newBoardState[tocell.x][tocell.y].getPiece() != null)
			newBoardState[tocell.x][tocell.y].removePiece();

		newBoardState[tocell.x][tocell.y].setPiece(newBoardState[fromcell.x][fromcell.y].getPiece());
		if (newBoardState[tocell.x][tocell.y].getPiece() instanceof King) {
			((King) (newBoardState[tocell.x][tocell.y].getPiece())).setx(tocell.x);
			((King) (newBoardState[tocell.x][tocell.y].getPiece())).sety(tocell.y);
		}
		newBoardState[fromcell.x][fromcell.y].removePiece();

		//Make sure piece is a King before casting it
		if (newBoardState[getKing(playerTurn).getx()][getKing(playerTurn).gety()].getPiece() instanceof King)
		{	
			if (((King) (newBoardState[getKing(playerTurn).getx()][getKing(playerTurn).gety()].getPiece()))
				.isInDanger(newBoardState) == true)
			{
				return true;
			}
			return false;
		}	
		else
			return false;
	}

	// A function to eliminate the possible moves that will put the King in danger
	private ArrayList<Cell> filterDestination(ArrayList<Cell> destlist, Cell fromcell) {
		ArrayList<Cell> newlist = new ArrayList<Cell>();
		Cell newBoardState[][] = new Cell[8][8];
		ListIterator<Cell> it = destlist.listIterator();
		int x, y;
		while (it.hasNext()) {
			for (int i = 0; i < 8; i++)
				for (int j = 0; j < 8; j++) {
					try {
						newBoardState[i][j] = new Cell(boardState[i][j]);
					} catch (CloneNotSupportedException e) {
						e.printStackTrace();
					}
				}

			Cell tempc = it.next();
			if (newBoardState[tempc.x][tempc.y].getPiece() != null)
				newBoardState[tempc.x][tempc.y].removePiece();
			newBoardState[tempc.x][tempc.y].setPiece(newBoardState[fromcell.x][fromcell.y].getPiece());
			x = getKing(playerTurn).getx();
			y = getKing(playerTurn).gety();
			if (newBoardState[fromcell.x][fromcell.y].getPiece() instanceof King) {
				((King) (newBoardState[tempc.x][tempc.y].getPiece())).setx(tempc.x);
				((King) (newBoardState[tempc.x][tempc.y].getPiece())).sety(tempc.y);
				x = tempc.x;
				y = tempc.y;
			}
			newBoardState[fromcell.x][fromcell.y].removePiece();
			if ((((King) (newBoardState[x][y].getPiece())).isInDanger(newBoardState) == false))
				newlist.add(tempc);
		}
		return newlist;
	}

	// A Function to filter the possible moves when the king of the current player
	// is under Check
	private ArrayList<Cell> inCheckFilter(ArrayList<Cell> destlist, Cell fromcell, int color) {
		ArrayList<Cell> newlist = new ArrayList<Cell>();
		Cell newBoardState[][] = new Cell[8][8];
		ListIterator<Cell> it = destlist.listIterator();
		int x, y;
		while (it.hasNext()) {
			for (int i = 0; i < 8; i++)
				for (int j = 0; j < 8; j++) {
					try {
						newBoardState[i][j] = new Cell(boardState[i][j]);
					} catch (CloneNotSupportedException e) {
						e.printStackTrace();
					}
				}
			Cell tempc = it.next();
			if (newBoardState[tempc.x][tempc.y].getPiece() != null)
				newBoardState[tempc.x][tempc.y].removePiece();
			newBoardState[tempc.x][tempc.y].setPiece(newBoardState[fromcell.x][fromcell.y].getPiece());
			x = getKing(color).getx();
			y = getKing(color).gety();
			if (newBoardState[tempc.x][tempc.y].getPiece() instanceof King) {
				((King) (newBoardState[tempc.x][tempc.y].getPiece())).setx(tempc.x);
				((King) (newBoardState[tempc.x][tempc.y].getPiece())).sety(tempc.y);
				x = tempc.x;
				y = tempc.y;
			}
			newBoardState[fromcell.x][fromcell.y].removePiece();
			if ((((King) (newBoardState[x][y].getPiece())).isInDanger(newBoardState) == false))
				newlist.add(tempc);
		}
		return newlist;
	}

	// A function to check if the King is check-mate. The Game Ends if this function
	// returns true.
	public boolean checkmate(int color) {
		ArrayList<Cell> dlist = new ArrayList<Cell>();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (boardState[i][j].getPiece() != null && boardState[i][j].getPiece().getColor() == color) {
					dlist.clear();
					dlist = boardState[i][j].getPiece().move(boardState, i, j);
					dlist = inCheckFilter(dlist, boardState[i][j], color);
					if (dlist.size() != 0)
						return false;
				}
			}
		}
		return true;
	}

	@SuppressWarnings("deprecation")
	private void gameEnd() {
		cleanDestinations(destinationList);
		panDisplayTime.disable();
		timer.countdownTimer.stop();
		if (previous != null)
			previous.removePiece();
		if (playerTurn == 0) {
			whitePlayer.updateGamesWon();
			whitePlayer.updatePlayer();
			strWinner = whitePlayer.name();
		} else {
			blackPlayer.updateGamesWon();
			blackPlayer.updatePlayer();
			strWinner = blackPlayer.name();
		}
		JOptionPane.showMessageDialog(panBoard, "Checkmate!!!\n" + strWinner + " wins");
		panWPlayer.remove(panWDetails);
		panBPlayer.remove(panBDetails);
		panDisplayTime.remove(labTime);

		panTime.add(labSetTimer);
		panTime.add(labSetMode);
		panTime.add(panMode);

		radClassic.enable();
		radNineSixty.enable();

		panDisplayTime.add(bttnStart);
		panShowPlayer.remove(labMove);
		panShowPlayer.remove(labPlayerTurn);
		panShowPlayer.revalidate();
		panShowPlayer.add(timeSlider);

		split.remove(panBoard);
		split.add(panTemp);
		bttnWCreatePlayer.enable();
		bttnBCreatePlayer.enable();
		bttnWSelectPlayer.enable();
		bttnBSelectPlayer.enable();
		end = true;
		mainBoard.disable();
		mainBoard.dispose();
		mainBoard = new Main();
		mainBoard.setVisible(true);
		mainBoard.setResizable(false);
	}

	//#region Mouse Events

	// These are the abstract function of the parent class. Only relevant method
	// here is the On-Click Fuction
	// which is called when the user clicks on a particular cell
	@Override
	public void mouseClicked(MouseEvent arg0) 
	{
		c = (Cell) arg0.getSource();
		if (previous == null) {
			if (c.getPiece() != null) {
				if (c.getPiece().getColor() != playerTurn)
					return;
				c.select();
				previous = c;
				destinationList.clear();
				destinationList = c.getPiece().move(boardState, c.x, c.y);
				if (c.getPiece() instanceof King)
					destinationList = filterDestination(destinationList, c);
				else {
					if (boardState[getKing(playerTurn).getx()][getKing(playerTurn).gety()].isCheck())
						destinationList = new ArrayList<Cell>(filterDestination(destinationList, c));
					else if (destinationList.isEmpty() == false && willKingBeInDanger(c, destinationList.get(0)))
						destinationList.clear();
				}
				highlightDestinations(destinationList);
			}
		} else {
			if (c.x == previous.x && c.y == previous.y) {
				c.deselect();
				cleanDestinations(destinationList);
				destinationList.clear();
				previous = null;
			} else if (c.getPiece() == null || previous.getPiece().getColor() != c.getPiece().getColor()) {
				if (c.isPossibleDestination()) {
					if (c.getPiece() != null)
						c.removePiece();
					c.setPiece(previous.getPiece());
					if (previous.isCheck())
						previous.removeCheck();
					previous.removePiece();
					if (getKing(playerTurn ^ 1).isInDanger(boardState)) {
						boardState[getKing(playerTurn ^ 1).getx()][getKing(playerTurn ^ 1).gety()].setCheck();
						if (checkmate(getKing(playerTurn ^ 1).getColor())) {
							previous.deselect();
							if (previous.getPiece() != null)
								previous.removePiece();
							gameEnd();
						}
					}
					if (getKing(playerTurn).isInDanger(boardState) == false)
						boardState[getKing(playerTurn).getx()][getKing(playerTurn).gety()].removeCheck();
					if (c.getPiece() instanceof King) {
						((King) c.getPiece()).setx(c.x);
						((King) c.getPiece()).sety(c.y);
					}
					changePlayerTurn();
					if (!end) {
						timer.reset();
						timer.start();
					}
				}
				if (previous != null) {
					previous.deselect();
					previous = null;
				}
				cleanDestinations(destinationList);
				destinationList.clear();
			} else if (previous.getPiece().getColor() == c.getPiece().getColor()) {
				previous.deselect();
				cleanDestinations(destinationList);
				destinationList.clear();
				c.select();
				previous = c;
				destinationList = c.getPiece().move(boardState, c.x, c.y);
				if (c.getPiece() instanceof King)
					destinationList = filterDestination(destinationList, c);
				else {
					if (boardState[getKing(playerTurn).getx()][getKing(playerTurn).gety()].isCheck())
						destinationList = new ArrayList<Cell>(filterDestination(destinationList, c));
					else if (destinationList.isEmpty() == false && willKingBeInDanger(c, destinationList.get(0)))
						destinationList.clear();
				}
				highlightDestinations(destinationList);
			}
		}
		if (c.getPiece() != null && c.getPiece() instanceof King) {
			((King) c.getPiece()).setx(c.x);
			((King) c.getPiece()).sety(c.y);
		}
	}

	// Other Irrelevant abstract function. Only the Click Event is captured.
	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

	}
	//#endregion

	// Listener Classes
	// Listener for Start Button
	class START implements ActionListener {

		@SuppressWarnings("deprecation")
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			if (whitePlayer == null || blackPlayer == null) {
				JOptionPane.showMessageDialog(panControl, "Fill in the details");
				return;
			}
			whitePlayer.updateGamesPlayed();
			whitePlayer.updatePlayer();
			blackPlayer.updateGamesPlayed();
			blackPlayer.updatePlayer();
			bttnWCreatePlayer.disable();
			bttnBCreatePlayer.disable();
			bttnWSelectPlayer.disable();
			bttnBSelectPlayer.disable();

			RunSetup();
			radClassic.disable();
			radNineSixty.disable();

			split.remove(panTemp);
			split.add(panBoard);
			panShowPlayer.remove(timeSlider);
			labMove = new JLabel("Move:");
			labMove.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
			labMove.setForeground(Color.red);

			panShowPlayer.add(labMove);
			labPlayerTurn = new JLabel(strMove);
			labPlayerTurn.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
			labPlayerTurn.setForeground(Color.blue);
			panShowPlayer.add(labPlayerTurn);

			panTime.remove(labSetTimer);
			panTime.remove(labSetMode);
			panTime.remove(panMode);
			panDisplayTime.remove(bttnStart);
			panDisplayTime.add(labTime);

			timer = new Time(labTime);
			timer.start();
		}
	}

	// Listener for countdown timer
	class TIME_CHANGE implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent arg0) {
			timeRemaining = timeSlider.getValue() * 60;
		}
	}

	// Listener for selecting a player
	class SELECT_HANDLER implements ActionListener {
		private int color;

		SELECT_HANDLER(int i) {
			color = i;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			tempPlayer = null;
			String n = (color == 0) ? strWName : strBName;
			JComboBox<String> jc = (color == 0) ? wCombo : bCombo;
			JComboBox<String> ojc = (color == 0) ? bCombo : wCombo;
			ArrayList<Player> pl = (color == 0) ? itemWPlayer : itemBPlayer;

			ArrayList<Player> opl = Player.fetchPlayers();
			if (opl.isEmpty())
				return;
			JPanel det = (color == 0) ? panWDetails : panBDetails;
			JPanel PL = (color == 0) ? panWPlayer : panBPlayer;
			if (selected == true)
				det.removeAll();
			n = (String) jc.getSelectedItem();
			Iterator<Player> it = pl.iterator();
			Iterator<Player> oit = opl.iterator();
			while (it.hasNext()) {
				Player p = it.next();
				if (p.name().equals(n)) {
					tempPlayer = p;
					break;
				}
			}
			while (oit.hasNext()) {
				Player p = oit.next();
				if (p.name().equals(n)) {
					opl.remove(p);
					break;
				}
			}

			if (tempPlayer == null)
				return;
			if (color == 0)
				whitePlayer = tempPlayer;
			else
				blackPlayer = tempPlayer;
			itemBPlayer = opl;
			ojc.removeAllItems();
			for (Player s : opl)
				ojc.addItem(s.name());
			det.add(new JLabel(" " + tempPlayer.name()));
			det.add(new JLabel(" " + tempPlayer.gamesPlayed()));
			det.add(new JLabel(" " + tempPlayer.gamesWon()));

			PL.revalidate();
			PL.repaint();
			PL.add(det);
			selected = true;
		}

	}

	// Listener for adding a player
	class HANDLER implements ActionListener {
		private int color;

		HANDLER(int i) {
			color = i;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			
			String n = (color == 0) ? strWName : strBName;
			JPanel j = (color == 0) ? panWPlayer : panBPlayer;
			ArrayList<Player> N = Player.fetchPlayers();
			Iterator<Player> it = N.iterator();
			JPanel det = (color == 0) ? panWDetails : panBDetails;
			n = JOptionPane.showInputDialog(j, "Enter your name");

			if (n != null) {

				while (it.hasNext()) {
					if (it.next().name().equals(n)) {
						JOptionPane.showMessageDialog(j, "Player exists");
						return;
					}
				}

				if (n.length() != 0) {
					Player tem = new Player(n);
					tem.updatePlayer();
					if (color == 0)
						whitePlayer = tem;
					else
						blackPlayer = tem;
				} else
					return;
			} else
				return;
			det.removeAll();
			det.add(new JLabel(" " + n));
			det.add(new JLabel(" 0"));
			det.add(new JLabel(" 0"));
			j.revalidate();
			j.repaint();
			j.add(det);
			selected = true;
		}
	}

	// Listener for selecting a Game Mode
	class SWITCH_RADIO implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (radClassic.isSelected()) isClassic = true;
			else isClassic = false;
		}
	}
}
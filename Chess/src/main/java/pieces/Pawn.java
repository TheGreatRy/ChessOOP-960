package pieces;

import java.util.ArrayList;

import chess.Cell;

/**
 * This is the Pawn Class inherited from the piece
 *
 */
public class Pawn extends Piece{
	
	//COnstructors
	public Pawn(String i,String p,int c)
	{
		setId(i);
		setPath(p);
		setColor(c);
	}
	
	//Move Function Overridden
	public ArrayList<Cell> move(Cell state[][],int x,int y)
	{
		//Pawn can move only one step except the first chance when it may move 2 steps
		//It can move in a diagonal fashion only for attacking a piece of opposite color
		//It cannot move backward or move forward to attact a piece
		
		possibleMoves.clear();
		if(getColor()==0)
		{
			if(x==0)
				return possibleMoves;
			if(state[x-1][y].getPiece()==null)
			{
				possibleMoves.add(state[x-1][y]);
				if(x==6)
				{
					if(state[4][y].getPiece()==null)
						possibleMoves.add(state[4][y]);
				}
			}
			if((y>0)&&(state[x-1][y-1].getPiece()!=null)&&(state[x-1][y-1].getPiece().getColor()!=this.getColor()))
				possibleMoves.add(state[x-1][y-1]);
			if((y<7)&&(state[x-1][y+1].getPiece()!=null)&&(state[x-1][y+1].getPiece().getColor()!=this.getColor()))
				possibleMoves.add(state[x-1][y+1]);
		}
		else
		{
			if(x==8)
				return possibleMoves;
			if(state[x+1][y].getPiece()==null)
			{
				possibleMoves.add(state[x+1][y]);
				if(x==1)
				{
					if(state[3][y].getPiece()==null)
						possibleMoves.add(state[3][y]);
				}
			}
			if((y>0)&&(state[x+1][y-1].getPiece()!=null)&&(state[x+1][y-1].getPiece().getColor()!=this.getColor()))
				possibleMoves.add(state[x+1][y-1]);
			if((y<7)&&(state[x+1][y+1].getPiece()!=null)&&(state[x+1][y+1].getPiece().getColor()!=this.getColor()))
				possibleMoves.add(state[x+1][y+1]);
		}
		return possibleMoves;
	}
}

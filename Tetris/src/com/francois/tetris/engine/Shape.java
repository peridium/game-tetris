package com.francois.tetris.engine;

import java.util.Random;

import com.francois.tetris.constant.Constant;

/**
 * Cette classe permet de modeliser les differentes formes du tetris
 * 
 * @author François
 *
 */
public class Shape implements Constant
{
	private static final int DONT_CHECK_CELL = -1000;

	private Grid grid;
	private Random random;
	private int[] elements;
	//prochaine forme à déclencher
	private int nextType;

	//le type de la forme
	private int type;
	//orientation de la forme, dans mon cas la forme commence avec une orientation est
	private int orientation;
	//L'etat de la forme
	private int state;

	private boolean isGameOver = false;
	private boolean isInited = false;


	/**
	 * Contructeur de la classe Shape
	 * @param grid
	 */
	public Shape(Grid grid) 
	{
		this.isGameOver = false;
		this.grid = grid;
		this.random = new Random();
		this.elements = new int[4];
	}

	/**
	 * 
	 * 
	 * @return boolean
	 */
	public boolean spawn()
	{
		this.isGameOver = false;

		for (int i = 0; i < this.elements.length; i++) 
			this.elements[i] = DONT_CHECK_CELL;

		this.type = this.random.nextInt(SHAPE_MAX_TYPES);
		this.elements[0] = START_CELL;
		this.orientation = EAST;
		this.state = STATE_USER;
		return alignFromOrientation(this.orientation);
	}

	/**
	 * mettre a jour notre forme selon le comportement de l'utilisateur.
	 * ACTION_LEFT = mouvement de la forme part l'utilisateur vers la gauche.
	 * ACTION_RIGHT = mouvement de la forme part l'utilisateur vers la droite.
	 * ACTION_DOWN = Mouvement de la forme par l'utilisateur vers le bas.
	 * ACTION_ROTATE = rotation de la forme part l'utilisateur.
	 * ACTION_FALL = Aucune action effectuer par
	 * @param currentAction
	 */
	public void updateShape(int currentAction) 
	{
		if(this.state == STATE_USER)
		{
			switch(currentAction)
			{
				case ACTION_LEFT:
					this.moveShape(C_LEFT);
					break;
				case ACTION_RIGHT:
					this.moveShape(C_RIGHT);
					break;
				case ACTION_DOWN:
					this.moveShape(C_DOWN);
					break;
				case ACTION_ROTATE:
					this.rotateShape(DEFAULT_ROTATION);
					break;
				case ACTION_FALL:
					this.state = STATE_FALLING;
					break;
			}
		}
	}

	public boolean IsFalling()
	{
		return this.state == STATE_FALLING;
	}

	//true when need to recheck playfield
	public boolean addGravity() 
	{
		if(isInited)
		{
			//gravity
			boolean falling = moveShape(C_DOWN);
			if(!falling)
			{
				this.state = STATE_LOCKED;
				isInited = false;
				return true;
			}
		}
		else
		{
			isInited = spawn();

			if(!isInited)//cela signifie qu'il n'y a plus de case pour acceulir la nouvelle forme, c'est game over
			{
				isGameOver = true;
			}
		}
		return false;
	}

	/**
	 * Gestion de deplacement de la forme
	 * 
	 * @param cellscrool
	 * @return boolean
	 */
	public boolean moveShape(int cellscrool)
	{
		int[] tmpElems = this.elements.clone();
		
		for(int i=0 ; i < this.elements.length ; i++)
			if(this.elements[i] != DONT_CHECK_CELL)
				tmpElems[i] += cellscrool;

		if(cellscrool == C_LEFT || cellscrool == C_RIGHT)
			if(this.grid.getRow(this.elements[0]) != this.grid.getRow(tmpElems[0]))
				return false;

		return tryToMove(tmpElems);
	}

	/**
	 * Gestion de la rotation de la forme
	 * 
	 * @param rotation
	 * @return boolean
	 */
	public boolean rotateShape(int rotation)
	{
		int orientation = this.orientation;
		orientation += rotation;
		
		if(orientation < NORTH)
			orientation = WEST;
		else if(orientation > WEST)
			orientation = NORTH;
		
		boolean hasRotated = alignFromOrientation(orientation);
		if(hasRotated)
			this.orientation = orientation;

		return hasRotated;
	}


	private boolean alignFromOrientation(int orientation)
	{
		int[] newElemPos = this.elements.clone();
		int typeOffset = (this.type*SHAPE_TABLE_TYPE_OFFSET);

		orientation *= SHAPE_TABLE_ELEMS_PER_ROW;
		
		newElemPos[1] = newElemPos[0] + SHAPE_TABLE[typeOffset+orientation];
		newElemPos[2] = newElemPos[0] + SHAPE_TABLE[typeOffset+orientation+1];
		newElemPos[3] = newElemPos[0] + SHAPE_TABLE[typeOffset+orientation+2];

		return tryToMove(newElemPos);
	}

	/**
	 * Tentative de deplacer la forme.
	 * 
	 * @param newElemPos
	 * @return boolean
	 */
	private boolean tryToMove(int[] newElemPos) 
	{
		boolean hasMoved = this.grid.tryToMoveCells(this.elements, newElemPos);
		if(hasMoved)
			this.elements = newElemPos;
		return hasMoved;
	}

	public boolean isGameOver() 
	{
		return isGameOver;
	}

	public void setGameOver(boolean isGameOver)
	{
		this.isGameOver = isGameOver;
	}

	public int getNextType() 
	{
		return nextType;
	}

	public boolean isInited()
	{
		return isInited;
	}

	public void setInited(boolean isInited)
	{
		this.isInited = isInited;
	}

	public int getState()
	{
		return state;
	}
}

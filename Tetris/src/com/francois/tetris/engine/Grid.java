package com.francois.tetris.engine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.francois.tetris.constant.Constant;

/**
 * Cette classe permet de définir le plateau du jeu mais également les règles du jeu
 * - Creation et gestion des cellules
 * - Gestion du déplacement des formes
 * - Gestion des dépassements des formes en dehors du plateau
 * 
 * @author François
 *
 */
public class Grid implements Constant
{	
	private boolean[] cells;//
	
	private int gridW;//Largeur de la grille
	private int gridH;//hauteur de la grille
	
	private int cellW;//Largeur d'une case
	private int cellH;//Hauteur d'une case

	private int marginLeft;
	private int marginTop;
	private int marginRight;
	private int marginBottom;

	/**
	 * Contructeur de la classe TetrisGrid
	 */
	public Grid()
	{
		this.cells = new boolean[GRID_COLS * GRID_ROWS];
	}

	/**
	 * Initialisation de notre tableau.
	 * <br> Case a vrai = case rempli.
	 * <br> Case a fausse = case vide.
	 */
	public void init()
	{
		for(int i = 0 ; i < cells.length ; i++)
			this.cells[i] = false;
	}

	/**
	 * Definie le taille du plateau de jeu et le positionnement de celle-ci sur l'ecran.
	 */
	public void setBackGroundDimentions(int w , int h)
	{
		this.marginLeft = MARGIN_LEFT;
		this.marginTop = MARGIN_TOP;
		
		w -= this.marginLeft + MARGIN_RIGHT;
		h -= this.marginTop + MARGIN_BOTTOM;
		
		this.gridW = w;
		this.gridH = h;
		
		this.cellW = w / GRID_COLS;
		this.cellH = h / GRID_ROWS;
		
		Log.e("w" , w+"");
		Log.e("h" , h+"");
		
		this.marginRight = this.marginLeft + (this.cellW*GRID_COLS);
		this.marginBottom = this.marginTop + (this.cellH*GRID_ROWS);
	}

	/**
	 * Retourne la ligne correspondant à l'index
	 * 
	 * @param index
	 * @return int
	 */
	public int getRow(int index)
	{
		if(index < 0)
			return -((Math.abs(index)/GRID_COLS) + 1);
		else
			return (index/GRID_COLS);
	}

	/**
	 * Verifie si la cellule est bien compris dans le tableau.
	 * 
	 * @param index
	 * @return boolean
	 */
	public boolean IsCellValid(int index)
	{
		return (index >= 0 && index < cells.length);
	}

	/**
	 * Verifie si la cellule est vide ou pleine.
	 * <br>Vrai -> Pleine.
	 * <br>Fausse -> Vide.
	 * 
	 * @param index
	 * @return boolean
	 */
	public boolean IsCellFree(int index)
	{
		if(IsCellValid(index))
			return !cells[index];
		else
			return false;
	}

	private boolean checkForRunOff(int[] from, int[] to) 
	{	
		int[] testArray = to.clone();
		for (int i = testArray.length-1 ; i >= 0 ; i--) 
		{
			testArray[i] -= testArray[0];
			testArray[i] += START_CELL;
		}

		for (int i = 0 ; i < to.length ; i++) 
		{
			if(getRow(to[i]) - getRow(to[0]) != getRow(testArray[i]) - getRow(testArray[0]))
				return false;
		}
		return true;
	}

	/**
	 * Methode qui va tenter de deplacer la forme dans le tableau
	 * 
	 * @param from
	 * @param to
	 * @return boolean
	 */
	public boolean tryToMoveCells(int[] from, int[] to) 
	{
		//test grid
		if(!checkForRunOff(from,to))
			return false;

		boolean validMove = false;
		for (int i = 0; i < to.length; i++) 
		{
			boolean cellAboveGrid = to[i] < 0; 
			if(!this.IsInArray(to[i], from))
				if(IsCellFree(to[i]) || cellAboveGrid)
					validMove = true;
				else
					return false;
		}

		//Si le mouvemenent est valide, on l'ecrit dans le tableau
		if(validMove)
		{
			for (int i = 0; i < from.length; i++) 
			{
				if(IsCellValid(from[i]))
					this.cells[from[i]] = false;
			}
			for (int i = 0; i < to.length; i++)
			{
				boolean cellAboveGrid = to[i] < 0;
				if(!cellAboveGrid)
					this.cells[to[i]] = true;
			}
		}

		return validMove;
	}
	
	/**
	 * Verfirie que la forme 
	 * @param value
	 * @param array
	 * @return
	 */
	private boolean IsInArray(int value, int[] array)
	{
		for (int i = 0; i < array.length; i++) 
		{
			if(value == array[i])
				return true;
		}
		return false;
	}

	public void paint(Canvas canvas, Paint paint)
	{
		//Couleur du fond d'ecran du plateau et on dessine le un rectangle dans notre canvas
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.FILL);
		canvas.drawRect(marginLeft , marginTop , marginRight , marginBottom , paint);

		//Couleur des elements de notre tabbleau.
		//Dans un premier j'effectue un calcul pour 
		int left,top,right,bottom;
		for(int i = 0 ; i < cells.length ; i++)
		{
			left = marginLeft + (i%GRID_COLS)*cellW;
			top = marginTop +(i/GRID_COLS)*cellH;
			right = left + cellW;
			bottom = top + cellH;
			
			//Carre interieur des formes pour des effets de cube
			if(this.cells[i])
			{
				paint.setStrokeWidth(8);
				
				paint.setColor(Color.GREEN);
				paint.setStyle(Paint.Style.FILL);
				canvas.drawRect(left , top , right , bottom , paint);
				
				paint.setColor(Color.BLACK);
				paint.setStyle(Paint.Style.STROKE);
				canvas.drawRect(left + 2 , top + 2 , right - 2 , bottom - 2 , paint);
			}
		}
	}

	public int updateGrid() 
	{
		int points = 0;
		for (int row =  GRID_ROWS-1; row >= 0; row--)
		{
			if(CheckRowForSame(row, false))
				break;
			if(CheckRowForSame(row, true))
			{
				points++;
				SetAllRowTo(row, false);
				MakeGridCollapse(row-1);
				row++;
			}
		}
		return points;
	}

	private void MakeGridCollapse(int row)
	{
		for (int r =  row; r >= 0; r--) 
		{
			if(CheckRowForSame(r, false))
				break;
			this.ShiftRowBy(r,C_DOWN);
			this.SetAllRowTo(r, false);
		}
	}

	private void ShiftRowBy(int row, int down) 
	{
		int index;
		for (int i = 0; i < GRID_COLS; i++) 
		{
			index = (row*GRID_COLS)+i;
			this.cells[index+ down] = cells[index];
		}

	}

	private void SetAllRowTo(int row, boolean b) 
	{
		for (int i = 0; i < GRID_COLS; i++) 
		{
			this.cells[(row*GRID_COLS)+i] = b;
		}
	}

	private boolean CheckRowForSame(int row, boolean b) 
	{
		for (int i = 0; i < GRID_COLS; i++) 
		{
			if(this.cells[(row*GRID_COLS)+i] != b )
				return false;
		}
		return true;
	}

	public int getGridW() 
	{
		return gridW;
	}

	public int getGridH()
	{
		return gridH;
	}

	public int getCellW() {
		return cellW;
	}

	public int getCellH() {
		return cellH;
	}
}

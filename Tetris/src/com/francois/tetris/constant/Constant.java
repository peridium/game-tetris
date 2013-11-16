package com.francois.tetris.constant;

/**
 * Cette interface me permet de stocker des variables importantes de mon application
 *  pour y avoir acces de n'importe qu'elle classe.
 *  On peut considere cette interface comme le fichier de configuration du jeu.
 * 
 * @author François
 *
 */
public interface Constant 
{
	//Specifie la taille du plateau de jeu.
	public static final int GRID_COLS = 10; //Le nombre de colonne.
	public static final int GRID_ROWS = 15; //Le nombre de ligne.

	//La marge de mon plateau de jeu(Les bandes noirs sur les extremités de la vue.
	public static final int MARGIN_TOP = 36;
	public static final int MARGIN_LEFT = 36;
	public static final int MARGIN_RIGHT = 36;
	public static final int MARGIN_BOTTOM = 36;

	//Cellule ou la nouvelle forme apparait, en cas general elle apparait au centre en haut de l'ecran.
	public static final int START_CELL = (GRID_COLS/2) - 1;

	//Mouvement possible de la forme
	public static final int C_LEFT = -1;
	public static final int C_UP = -GRID_COLS;
	public static final int C_RIGHT = 1;
	public static final int C_DOWN = GRID_COLS;

	//Valeur de rotation par defaut, dans mon cas j'ai choisi de faire une rotation de la forme vers la gauche
	public static final int DEFAULT_ROTATION = -1;

	//Les differentes actions disponibles pour les formes.
	public static final int ACTION_NONE = 0; //Aucune action sur la forme.
	public static final int ACTION_LEFT = 1; //Action de deplacement vers la gauche.
	public static final int ACTION_RIGHT = 2;// Action de deplacement vers la droite.
	public static final int ACTION_DOWN = 3;//Action de deplacement vers le bas.
	public static final int ACTION_ROTATE = 4; //Action de rotation de la forme.
	public static final int ACTION_FALL = 5; //Action de tomber de la forme.

	//Le status pour la forme courante
	public static final int STATE_USER    = 0; //Forme maniable par l'utilisateur
	public static final int STATE_LOCKED  = 1; //Forme placer donc bloquer
	public static final int STATE_FALLING = 2; //Forme qui ne recoit aucune interaction donc en train de tomber

	//Les differentes orientation possible
	public static final int NORTH = 0; 
	public static final int EAST  = 1;
	public static final int SOUTH = 2;
	public static final int WEST  = 3;

	//Mes differentes formes disponibles dans ce tetris
	public static final int SHAPE_MAX_TYPES	= 7; // Nombre de formes disponibles, nous sert pour la generation d'une nouvelle forme.

	//Cette table contient tous les formes possibles du tetris.
	public static final int SHAPE_TABLE_ELEMS_PER_ROW = 3;//Une forme dispose de 3 éléments dans le tableau ci-dessous.
	public static final int SHAPE_TABLE_ROWS_PER_TYPE = 4;//Une forme peut avoir 4 orientation ou etat possible dans la grille.
	
	//Offset permet d'aller cherche la forme correspondante dans le tableau ci-dessous.
	//Par exemple : SHAPE_L -> type = 12*2 = 24eme case du tableau puis ensuite l'orientation de celle-ci element1 = type+NORH , element2 = type+NORH+1 , element3 = type+NORH+2
	public static final int SHAPE_TABLE_TYPE_OFFSET	  = SHAPE_TABLE_ROWS_PER_TYPE * SHAPE_TABLE_ELEMS_PER_ROW;
	public static final int[] SHAPE_TABLE =
		{
		
		/**
		 * Table des differentes formes.
		 */
		
		
		
		/*
		 * SHAPE_LONG
		 */
		
		//NORTH
		C_UP, C_DOWN, C_UP*2, //I
		//EAST
		C_LEFT, C_RIGHT, C_RIGHT*2, //---
		//SOUTH
		C_UP, C_DOWN, C_DOWN*2, //I
		//WEST
		C_LEFT, C_RIGHT, C_LEFT*2,//---

		/*
		 * SHAPE_IL
		 */
		
		//NORTH
		C_UP, C_DOWN, C_UP+C_RIGHT,//inverse L
		//EAST
		C_LEFT, C_RIGHT, C_RIGHT+C_DOWN,//--|
		//SOUTH
		C_UP, C_DOWN, C_DOWN+C_LEFT,//inverse L
		//WEST
		C_LEFT, C_RIGHT, C_LEFT+C_UP,//|--

		/*
		 * SHAPE_L
		 */
		
		//NORTH
		C_UP, C_DOWN, C_UP+C_LEFT,//L
		//EAST
		C_LEFT, C_RIGHT, C_RIGHT+C_UP,//--|
		//SOUTH
		C_UP, C_DOWN, C_DOWN+C_RIGHT,//L
		//WEST
		C_LEFT, C_RIGHT, C_LEFT+C_DOWN,//|--

		/*
		 * SHAPE_SQUARE
		 */
		
		//NORTH (elem1,2,3, mincol, maxcol)
		C_RIGHT, C_DOWN, C_RIGHT+C_DOWN,//O
		//EAST (elem1,2,3)
		C_RIGHT, C_DOWN, C_RIGHT+C_DOWN,//O
		//SOUTH (elem1,2,3)
		C_RIGHT, C_DOWN, C_RIGHT+C_DOWN,//O
		//WEST (elem1,2,3)
		C_RIGHT, C_DOWN, C_RIGHT+C_DOWN,//O

		/*
		 * SHAPE_S
		 */
		
		//NORTH
		C_DOWN+C_LEFT, C_DOWN, C_RIGHT,//_|-
		//EAST
		C_UP, C_RIGHT, C_RIGHT+C_DOWN,//|-i
		//SOUTH
		C_DOWN+C_LEFT, C_DOWN, C_RIGHT,//_|-
		//WEST
		C_UP, C_RIGHT, C_RIGHT+C_DOWN,//|-i

		/*
		 * SHAPE_IS
		 */
		
		//NORTH
		C_LEFT, C_DOWN, C_DOWN+C_RIGHT,//-|_
		//EAST
		C_UP+C_RIGHT, C_RIGHT, C_DOWN,//i-|
		//SOUTH
		C_LEFT, C_DOWN, C_DOWN+C_RIGHT,//-|_
		//WEST
		C_UP+C_RIGHT, C_RIGHT, C_DOWN,//i-|

		/*
		 * SHAPE_T
		 */
		
		//NORTH
		C_UP, C_DOWN, C_RIGHT,//T
		//EAST
		C_LEFT, C_DOWN, C_RIGHT,//T
		//SOUTH
		C_LEFT, C_DOWN, C_UP,//T
		//WEST
		C_LEFT, C_UP, C_RIGHT,//T
		};
}

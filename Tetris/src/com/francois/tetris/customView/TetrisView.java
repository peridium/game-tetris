package com.francois.tetris.customView;

import com.francois.tetris.constant.Constant;
import com.francois.tetris.engine.Grid;
import com.francois.tetris.engine.Shape;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

/**
 * Classe maitresse de mon Tetris.
 * C'est la vue qui permet d'interagir avec l'utilisateur.
 * 
 * @author François
 *
 */
public class TetrisView extends View implements Constant
{
	//Image par seconde 48
	public static final int FRAME_RATE = 20;
	//Delaie avant la mise a jour de la canvas(de la toile de peinture).
	public final static int OUT_OF_PAUSE_DELAY = FRAME_RATE * 2;

	//Vitesse de la descente des formes
	public static final int SPPED_RATE = 500;

	private Grid grid;
	private Shape currentShape;
	private Context context;

	private AlertDialog alertDialog;
	private boolean isAlertDialogActive = false;

	//Le pinceau qui dessine mon tetris
	private Paint paint;

	private boolean hasFocus;
	private long nextUpdate;
	private long allowUptadeShage;

	//current game action fired by player
	private int currentAction;
	private int currentColShape = -1;
	private int currentRowShape = -1;

	//Gesture
	private GestureDetector gestureDetector;
	private OnTouchListener gestureListener;

	private static final int SWIPE_MIN_DISTANCE = 40;

	/**
	 * Contructeur de notre classe TetrisView
	 * 
	 * @param context
	 * @param attrs
	 */
	public TetrisView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);

		this.context = context;

		//Initialization de notre vue.
		//Mise en place de la couleur du fond d'ecran.
		this.setBackgroundColor(Color.BLACK);
		//On la rend accessible au toucher par l'utilisateur.
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
		this.requestFocus();

		//Instantation de notre grille.
		this.grid = new Grid();
		//Incorporation des formes dans notre grille.
		this.currentShape = new Shape(this.grid);

		//Instantiation de notre pinceau qui va nous servir a dessiner tout le jeu.
		this.paint = new Paint();

		this.initTetris();

		//Mise en place de la gesture sur la vue.
		this.gestureDetector = new GestureDetector(context, new MyGestureDetector());
		this.gestureListener = new OnTouchListener()
		{	
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				return gestureDetector.onTouchEvent(event);
			}
		};
		this.setOnTouchListener(gestureListener);
		this.setOnClickListener(null);
	}

	/**
	 * Initialisation du jeu
	 */
	private void initTetris()
	{
		this.currentShape.setInited(false);
		this.currentAction = ACTION_NONE;
		this.nextUpdate = 0;
		this.allowUptadeShage = 1;

		this.grid.init();
	}

	/**
	 * Permet de reinisialiser le jeu.
	 */
	private void restartTetris()
	{
		this.initTetris();
		this.isAlertDialogActive = false;
		this.currentShape.setGameOver(false);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) 
	{
		this.grid.setBackGroundDimentions(w, h);
		super.onSizeChanged(w, h, oldw, oldh);
	}

	/**
	 * Ma classe de gesture qui va me permettre de detecter les mouvements de doights de l'utilisateur.
	 * Gestion de deplacement des formes en fonction de cette position.
	 * 
	 * @author François
	 *
	 */
	private class MyGestureDetector extends SimpleOnGestureListener
	{		
		@Override
		public boolean onSingleTapUp(MotionEvent e)
		{
			//currentShape.rotateShape(DEFAULT_ROTATION);
			currentShape.updateShape(ACTION_ROTATE);
			return false;
		} 

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
		{
//			if(currentShape.getState() != STATE_LOCKED)
//			{
				Log.e("Colonne", (int)Math.abs(e2.getX()/grid.getCellW())+"");
				Log.e("Ligne", (int)Math.abs(e2.getY()/grid.getCellH())+"");
				
				if(e2.getX() > e1.getX() && e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE)//on glisse le doight de gauche a droite
				{
					if(currentColShape != ((int)Math.abs(e2.getX()/grid.getCellW())))
					{	
						currentColShape = ((int)Math.abs(e2.getX()/grid.getCellW()));
						currentShape.updateShape(ACTION_RIGHT);
					}
				}
				else if(e2.getX() < e1.getX() && e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE )//on glisse le doight de droite a gauche
				{
					if(currentColShape != ((int)Math.abs(e2.getX()/grid.getCellW())))
					{	
						currentColShape = ((int)Math.abs(e2.getX()/grid.getCellW()));
						currentShape.updateShape(ACTION_LEFT);
					}
				}

				if(e2.getY() > e1.getY() && (e2.getY() - e1.getY()) > SWIPE_MIN_DISTANCE)//on glisse le doight de haut en bas
				{
					if(currentRowShape != ((int)Math.abs(e2.getY()/grid.getCellH())))
					{
						currentRowShape = ((int)Math.abs(e2.getY()/grid.getCellH()));
						currentShape.updateShape(ACTION_DOWN);
					}
				}
//			}
			return false;
		}
	}

	/**
	 * Permet de mettre a jour notre plateau et verifie que l'utilisateur n'a pas perdu la partie.
	 * Si l'utilisateur perd la partie alors une popUp lui annonce la fin du jeu.
	 * Il se verra proposer entre quitter le jeu ou de relancer la partie.
	 */
	private void updateView() 
	{
		long currentTime = System.currentTimeMillis();

		if(this.hasFocus)
		{
			//Verifie si l'utilisateur n'a pas perdu
			if(currentShape.isGameOver())
			{
				AlertDialog.Builder dialog = new AlertDialog.Builder(context);
				dialog.setMessage("GAME OVER !");
				dialog.setCancelable(false);
				dialog.setPositiveButton("Restart",new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog,int id)
					{
						TetrisView.this.restartTetris();
					}
				})
				.setNegativeButton("Exit",new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog,int id)
					{
						dialog.cancel();
						((Activity)TetrisView.this.context).finish();
					}
				});

				if(!this.isAlertDialogActive)
				{
					this.alertDialog = dialog.create();
					this.alertDialog.show();
					this.isAlertDialogActive = true;
				}
			}
			//Si il n'a pas perdu on continu le jeu
			else if(currentTime > nextUpdate)
			{
				this.nextUpdate = currentTime + 1000 / FRAME_RATE;
				this.currentShape.updateShape(currentAction);
				this.currentAction = ACTION_NONE;

				if(currentTime - allowUptadeShage > SPPED_RATE || currentShape.IsFalling())
				{
					allowUptadeShage = currentTime;

					boolean shapeIsLocked = currentShape.addGravity();
					if(shapeIsLocked)
						this.grid.updateGrid();
				}
			}
		}
		else
		{
			this.nextUpdate = currentTime + (1000 / OUT_OF_PAUSE_DELAY);
		}
		return;
	}

	@Override
	protected void onDraw(Canvas canvas) 
	{
		this.updateView();
		super.onDraw(canvas);
		this.grid.paint(canvas, this.paint);
		this.invalidate();
	}

	public  void setGameFocus(boolean hasFocus)
	{
		this.hasFocus= hasFocus;
	}
}

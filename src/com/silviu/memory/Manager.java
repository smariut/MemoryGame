package com.silviu.memory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import com.silviu.memory.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class Manager extends Activity {
    private static int ROW_COUNT = -1;
	private static int COL_COUNT = -1;
	private Context context;
	private Drawable backImage;
	private int [] [] cards;
	private List<Drawable> images;
	private Card firstCard;
	private Card seconedCard;
	private ButtonListener buttonListener;
	private static Object lock = new Object();
	int turns;
	private TableLayout mainTable;
	private UpdateCardsHandler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        handler = new UpdateCardsHandler();
        loadImages();
        setContentView(R.layout.main);
       
       backImage =  getResources().getDrawable(R.drawable.star);
      
       buttonListener = new ButtonListener();
        
        mainTable = (TableLayout)findViewById(R.id.TabelJoc);
                
        context  = mainTable.getContext();
        
       	 Spinner s = (Spinner) findViewById(R.id.Spinner01);
	        ArrayAdapter adapter = ArrayAdapter.createFromResource(
	                this, R.array.type, android.R.layout.simple_spinner_item);
	        
	        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        
	        s.setAdapter(adapter);
	        	        
	        s.setOnItemSelectedListener(new OnItemSelectedListener(){
	        	
	    	  @Override
	    	  public void onItemSelected(
	    			  android.widget.AdapterView<?> arg0, 
	    			  View arg1, int pos, long arg3){
	    		  
	    		  ((Spinner) findViewById(R.id.Spinner01)).setSelection(0);
	    		  
	  			int x,y;
	  			
	  			switch (pos) {
				case 1:
					x=4;y=4;
					break;
				case 2:
					x=4;y=5;
					break;
				case 3:
					x=4;y=6;
					break;
				case 4:
					x=5;y=6;
					break;
				case 5:
					x=6;y=6;
					break;
				default:
					return;
				}
	  			newGame(x,y);
	  			
	  		}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
	  	});
    }
    
    private void newGame(int c, int r) {
    	ROW_COUNT = r;
    	COL_COUNT = c;
    	
    	cards = new int [COL_COUNT] [ROW_COUNT];
    	
    	
    	mainTable.removeView(findViewById(R.id.TableRow01));
    	mainTable.removeView(findViewById(R.id.TableRow02));
    	
    	TableRow tr = ((TableRow)findViewById(R.id.TableRow03));
    	tr.removeAllViews();
    	
    	mainTable = new TableLayout(context);
    	tr.addView(mainTable);
    	
    	 for (int y = 0; y < ROW_COUNT; y++) {
    		 mainTable.addView(createRow(y));
          }
    	 
    	 firstCard=null;
    	 loadCards();
    	 
    	 turns=0;
    	 ((TextView)findViewById(R.id.contor)).setText("Incercari: "+turns);
    	 
			
	}
    
    private void loadImages() {
    	images = new ArrayList<Drawable>();
    	
    	images.add(getResources().getDrawable(R.drawable.n1));
    	images.add(getResources().getDrawable(R.drawable.n2));
    	images.add(getResources().getDrawable(R.drawable.n3));
    	images.add(getResources().getDrawable(R.drawable.n4));
    	images.add(getResources().getDrawable(R.drawable.n5));
    	images.add(getResources().getDrawable(R.drawable.n6));
    	images.add(getResources().getDrawable(R.drawable.n7));
    	images.add(getResources().getDrawable(R.drawable.n8));
    	images.add(getResources().getDrawable(R.drawable.n9));
    	images.add(getResources().getDrawable(R.drawable.n10));
    	images.add(getResources().getDrawable(R.drawable.n11));
    	images.add(getResources().getDrawable(R.drawable.n12));
    	images.add(getResources().getDrawable(R.drawable.n13));
    	images.add(getResources().getDrawable(R.drawable.n14));
    	images.add(getResources().getDrawable(R.drawable.n15));
    	images.add(getResources().getDrawable(R.drawable.n16));
    	images.add(getResources().getDrawable(R.drawable.n17));
    	images.add(getResources().getDrawable(R.drawable.n18));

	}

	private void loadCards(){
		try{
	    	int size = ROW_COUNT*COL_COUNT;
	    	
	    	Log.i("loadCards()","size=" + size);
	    	
	    	ArrayList<Integer> list = new ArrayList<Integer>();
	    	
	    	for(int i=0;i<size;i++){
	    		list.add(new Integer(i));
	    	}
	    	
	    	
	    	Random r = new Random();
	    
	    	for(int i=size-1;i>=0;i--){
	    		int t=0;
	    		
	    		if(i>0){
	    			t = r.nextInt(i);
	    		}
	    		
	    		t=list.remove(t).intValue();
	    		cards[i%COL_COUNT][i/COL_COUNT]=t%(size/2);
	    		
	    		Log.i("loadCards()", "card["+(i%COL_COUNT)+
	    				"]["+(i/COL_COUNT)+"]=" + cards[i%COL_COUNT][i/COL_COUNT]);
	    	}
	    }
		catch (Exception e) {
			Log.e("loadCards()", e+"");
		}
		
    }
    
    private TableRow createRow(int y){
    	 TableRow row = new TableRow(context);
    	 row.setHorizontalGravity(Gravity.CENTER);
         
         for (int x = 0; x < COL_COUNT; x++) {
		         row.addView(createImageButton(x,y));
         }
         return row;
    }
    
    private View createImageButton(int x, int y){
    	Button button = new Button(context);
    	button.setBackgroundDrawable(backImage);
    	button.setId(100*x+y);
    	button.setOnClickListener(buttonListener);
    	return button;
    }
    
    class ButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			
			synchronized (lock) {
				if(firstCard!=null && seconedCard != null){
					return;
				}
				int id = v.getId();
				int x = id/100;
				int y = id%100;
				turnCard((Button)v,x,y);
			}
			
		}

		private void turnCard(Button button,int x, int y) {
			button.setBackgroundDrawable(images.get(cards[x][y]));
			
			if(firstCard==null){
				firstCard = new Card(button,x,y);
			}
			else{ 
				
				if(firstCard.x == x && firstCard.y == y){
					return; 
				}
					
				seconedCard = new Card(button,x,y);
				
				turns++;
				((TextView)findViewById(R.id.contor)).setText("Incercari: "+turns);
				
			
				TimerTask tt = new TimerTask() {
					
					@Override
					public void run() {
						try{
							synchronized (lock) {
							  handler.sendEmptyMessage(0);
							}
						}
						catch (Exception e) {
							Log.e("E1", e.getMessage());
						}
					}
				};
				
				  Timer t = new Timer(false);
			        t.schedule(tt, 500);
			}
			
				
		   }
		}
    
    class UpdateCardsHandler extends Handler{
    	
    	@Override
    	public void handleMessage(Message msg) {
    		synchronized (lock) {
    			checkCards();
    		}
    	}
    	public void checkCards(){
    	    	if(cards[seconedCard.x][seconedCard.y] == cards[firstCard.x][firstCard.y]){
    				firstCard.button.setVisibility(View.VISIBLE);
    				seconedCard.button.setVisibility(View.VISIBLE);
    			}
    			else {
    				seconedCard.button.setBackgroundDrawable(backImage);
    				firstCard.button.setBackgroundDrawable(backImage);
    			}
    	    	
    	    	firstCard=null;
    			seconedCard=null;
    	    }
    }
 
}
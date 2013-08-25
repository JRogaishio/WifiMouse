package com.codeferrets.wifimouse;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	ClientSocket client;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		
		//Display a pop-up requesting the target machine IP
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Enter Target Machines IP");

		// Set up the input
		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_PHONE);
		builder.setView(input);
		//Set the default text to the first 2 parts of the IP
		input.setText("192.168.");
		
		// Set up the buttons
		builder.setPositiveButton("Connect", new DialogInterface.OnClickListener() { 
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		    	//Enter the IP to the client
		    	connectClient(input.getText().toString());
		    }
		});

		builder.show();
				
		
		
		Button btnLeft = (Button) findViewById(R.id.btn_leftClick);
		Button btnRight = (Button) findViewById(R.id.btn_rightClick);
		
		btnLeft.setOnClickListener(btnLeft_onClick);
		btnRight.setOnClickListener(btnRight_onClick);
		
		
		
	}
	
	
	public OnClickListener btnLeft_onClick = new OnClickListener() {
	    public void onClick(final View v) {
	    	client.sendMessage(("2#0#0#"));
	    }
	};
	
	public OnClickListener btnRight_onClick = new OnClickListener() {
	    public void onClick(final View v) {
	    	client.sendMessage(("3#0#0#"));
	    }
	};
	
	private void connectClient(String ip) {
		//Create a new client
		client = new ClientSocket(ip, 8221);
		//Start the client connection in the background
		client.execute();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	
	public boolean onTouchEvent(MotionEvent event) {
	    int x = (int)event.getX();
	    int y = (int)event.getY();
	    switch (event.getAction()) {
	        case MotionEvent.ACTION_DOWN:
	        	client.sendMessage(("0#" + x + "#" + y + "#"));
	        	break;
	        case MotionEvent.ACTION_MOVE:
	        	client.sendMessage(("1#" + x + "#" + y + "#"));
	        	break;
	        case MotionEvent.ACTION_UP:
	        	//client.sendMessage(("2#" + x + "#" + y + "#"));
	        	break;
	    }
	    
	return false;
	}

}

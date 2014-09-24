package com.example.actionbar;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity implements OnClickListener  {
	ArrayList<String> todo_list = new ArrayList<String>();
	static ArrayList<String> archive_list = new ArrayList<String>();
	ArrayList<String> temp = new ArrayList<String>();
	ArrayList<String> temp2 = new ArrayList<String>();
	Button button;
	ListView listview;
	ArrayAdapter<String> adapter;
	int position;
	boolean backFromChild = false;
	//String aString;
	//public final static  String archiveItem= "com.example.actionBar.MESSAGE";
	//private static final String Filename = "file.sav";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewsById();
		OnClickListener listener = new OnClickListener(){
			@Override
			public void onClick(View v){ 
				EditText edit = (EditText) findViewById(R.id.edit_message);
				todo_list.add(edit.getText().toString());
				edit.setText("");
				adapter.notifyDataSetChanged();
				Intent intent = new Intent(MainActivity.this, Email.class);
				intent.putExtra("addEmail",edit.getText().toString());
				}
		};
		button.setOnClickListener(listener);
		//setListAdapter(adapter);
		registerForContextMenu(listview);
		}
	public void OnResume(){
	super.onResume();
	 
	if (backFromChild){
         backFromChild = false;
         archive_list = null;
         //if (temp != null)
         todo_list.addAll(temp);
         archive_list= temp2;
         adapter.notifyDataSetChanged();
         
	 }
	Log.d("Our list is",todo_list.toString());

	
	}
	
	@Override
	protected void onStart() {
	    super.onRestart();  // Always call the superclass method first
	    
	    
	    // Activity being restarted from stopped state    
	}
	private void findViewsById() {
        listview = (ListView) findViewById(R.id.list);
        button = (Button) findViewById(R.id.button_add);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice,todo_list);
		listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		listview.setAdapter(adapter);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
        
    	super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
        position = info.position;
        }
    @Override
    public boolean onContextItemSelected(MenuItem item){
    	//AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
    	position = info.position;
    	switch (item.getItemId()){
    	case R.id.item1:
    		archive_thang();
    		return true;
    	case R.id.item2:
    		delete_goal();
    		return true;
    	default:
    		return super.onContextItemSelected(item);
    	}
    	
    }
  

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main_action_bar, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()){

			case R.id.action_archive:
				bootArchive();
				return true;
		
			case R.id.action_email:
				bootEmail();
				return true;
			case R.id.action_stats:
				bootStats();
				return true;
			default:
				return super.onOptionsItemSelected(item);
				}
		
		}
	public void bootArchive(){
		Intent boot_intent = new Intent(this,Archive.class);
		boot_intent.putStringArrayListExtra("item", archive_list);
		startActivityForResult(boot_intent,1);
		}
	public void bootEmail(){
		Intent email_intent = new Intent(this,Email.class);
		startActivity(email_intent);
		}
	
	public void bootStats(){
		Intent stats_intent = new Intent(this,UsageStats.class);
		startActivity(stats_intent);;
		}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	public void delete_goal(){
		int postion_remove = position;
		todo_list.remove(postion_remove);
		adapter.notifyDataSetChanged();
		}
	
	public void archive_thang(){
		findViewsById();
		//Intent archive = new Intent(this,Archive.class);
		int itemPosition = position;
		String goal = todo_list.get(itemPosition).toString();
		archive_list.add(goal);
		todo_list.remove(position);
		}

	@Override
	protected void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		outState.putStringArrayList("todoSave", todo_list);
	
		}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState){
		super.onRestoreInstanceState(savedInstanceState);
		todo_list = savedInstanceState.getStringArrayList("todoSave");
	}
	
	public static ArrayList<String> giveList(){
		return archive_list;
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == 1) {
	        if (resultCode == RESULT_OK) {
	        	Intent intent = getIntent();
	        	temp = intent.getStringArrayListExtra("todos");
	        	temp2 =intent.getStringArrayListExtra("archives");
	            backFromChild = true;
	        }
	        if (resultCode == RESULT_CANCELED) {
	            // Write your code on no result return
	        }
	    }
	}
	
	

}

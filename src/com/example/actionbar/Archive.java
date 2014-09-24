package com.example.actionbar;
//http://stackoverflow.com/questions/20491764/returning-data-result-to-parent-activity-using-intents sept 23 2014
import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;

public class Archive extends Activity {
	ArrayList<String> archiveItems = new ArrayList<String>();
	ArrayList<String> dearchiveGoals = new ArrayList<String>();
	ListView listview;
	ArrayAdapter<String> adapter;
	int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_archive);
		// Show the Up button in the action bar.
		setupActionBar();
		findViewsById();
		registerForContextMenu(listview);

	}
	
	
	private void findViewsById() {
        listview = (ListView) findViewById(R.id.archive_list);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,archiveItems);
		Intent intent = getIntent();
		archiveItems.addAll(intent.getStringArrayListExtra("item"));
		listview.setAdapter(adapter);
    }
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.archive, menu);
		return true;
	}
	
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
        
    	super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_archive, menu);
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
    		unarchive();
    		return true;
    	case R.id.item2:
    		delete_goal();
    		return true;
    	default:
    		return super.onContextItemSelected(item);
    	}
    	
    }
  

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent boot_intent = new Intent(this,Archive.class);
			boot_intent.putStringArrayListExtra("todos", dearchiveGoals);
			boot_intent.putStringArrayListExtra("archives", archiveItems);
			setResult(RESULT_OK, boot_intent);
			finish();
			//onBackPressed();
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			//NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void delete_goal(){
		int postion_remove = position;
		archiveItems.remove(postion_remove);
		adapter.notifyDataSetChanged();
		}
	
	public void unarchive(){
		findViewsById();
		int itemPosition = position;
		String goal = archiveItems.get(itemPosition).toString();
		dearchiveGoals.add(goal);
		archiveItems.remove(position);
		}

}

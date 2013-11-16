package com.intravita.android.rss.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.intravita.android.rss.R;
import com.intravita.android.rss.http.RedditRestClient;
import com.intravita.android.rss.model.Post;
import com.intravita.android.rss.model.PostAdapter;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MainActivity extends SherlockActivity implements SearchView.OnQueryTextListener {
	private ListView listView;
	private PostAdapter adapter;
	
	private ProgressDialog pd;
	
	private SearchView searchView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listView = (ListView)findViewById(R.id.listView);
		searchView = new SearchView(getSupportActionBar().getThemedContext());
		pd = new ProgressDialog(this);
		pd.setCancelable(false);
		pd.setIndeterminate(true);
		doSearch("funny");
		searchView.setQuery("funny", false);
	}
	
	@Override
	public boolean onQueryTextSubmit(String query) {
		startPd();
		doSearch(query);
		return true;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		return false;
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		searchView.setQueryHint(getString(R.string.search_hint));
		searchView.setOnQueryTextListener(this);
		searchView.setIconified(false);
		
		menu.add("Search")
        .setActionView(searchView)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		return true;
	}
	
	
	private void doSearch(String query) {
		RedditRestClient.get(query, null, new JsonHttpResponseHandler() {
			@Override
            public void onSuccess(JSONObject content) {
				if (pd.isShowing()) {
					pd.dismiss();
				}
				
				JSONObject externaldata = content.optJSONObject("data");
				JSONArray array = externaldata.optJSONArray("children");
				
				ArrayList<Post> holder = new ArrayList<Post>();
				holder.clear();
				
				for (int i = 0; i < array.length(); i++) {
					JSONObject internalData = array.optJSONObject(i).optJSONObject("data");
					
					String thumbnail = internalData.optString("thumbnail");
					String title = internalData.optString("title");
					String author = internalData.optString("author");
					
					holder.add(new Post(thumbnail, title, author));
				}
				adapter = new PostAdapter(MainActivity.this, R.layout.listview_item_row, holder);
				listView.setAdapter(adapter);
				
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
		            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						shareContent(adapter.getItem(position).author, adapter.getItem(position).title);
					}
				});
            };
            
            @Override
            public void onFailure(Throwable t) {
            	noResults().show();
            }
		});
	}
	
	private AlertDialog noResults() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setIcon(R.drawable.ic_action_warning);
		builder.setTitle(R.string.no_subreddit);
		builder.setMessage(R.string.alert);
		
		builder.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
            	if (pd.isShowing()) {
					pd.dismiss();
				}
			}
		});
		return builder.create();
	}
	
	private void startPd() {
		pd.show();
	}
	
	private void shareContent(String author, String title) {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_TEXT, author + " - " + title);
		startActivity(Intent.createChooser(shareIntent, "Share Via"));
	}
}
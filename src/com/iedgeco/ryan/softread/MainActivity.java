package com.iedgeco.ryan.softread;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpResponse;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.iedgeco.ryan.softread.cache.AsyncImageLoader;
import com.iedgeco.ryan.softread.config.Config;
import com.iedgeco.ryan.softread.http.HttpRetriever;
import com.iedgeco.ryan.softread.models.SoftNews;
import com.iedgeco.ryan.softread.utils.Utils;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";
	
	private static final boolean DEBUG = true;
	
	private static String url = Config.SERVER_URL + Config.REQUEST_UPDATE;
	
	//views
	private ListView listViewNews;
	
	//controller
	private AsyncImageLoader imageLoader;
	private NewsAdapter newsAdapter;
	
	/**
	 * Adapter for the pictures
	 * */	
    private class ViewHolder{
    	public ImageView imageViewPic;
    	public TextView textViewNewsEN;
        public TextView textViewNewsZN;
        public TextView textViewTags;
        public TextView textViewLikeIt;
        public TextView textViewCreationDate;
    }
    
	class NewsAdapter extends ArrayAdapter<SoftNews>{

		private ArrayList<SoftNews> newsList;
		private LayoutInflater inflater;
		
		private boolean notEmpty(String str){
			if(str != null && !"".equals(str))
				return true;
			
			return false;
		}
		
		public NewsAdapter(Context context, int resource, ArrayList<SoftNews> newsList) {
			super(context, resource, newsList);
			this.newsList = newsList;
			this.inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
            if (convertView == null) {    //Prepare the view if doesn t exits
            	convertView = inflater.inflate(R.layout.news_item, null);
            	holder = new ViewHolder();
            	
            	holder.imageViewPic = (ImageView)convertView.findViewById(R.id.imageViewPic);
            	holder.textViewNewsEN = (TextView)convertView.findViewById(R.id.textViewNewsEN);
            	holder.textViewNewsZN = (TextView)convertView.findViewById(R.id.textViewNewsZN);
            	holder.textViewTags = (TextView)convertView.findViewById(R.id.textViewTags);
            	holder.textViewLikeIt = (TextView)convertView.findViewById(R.id.textViewLikeIt);
            	holder.textViewCreationDate = (TextView)convertView.findViewById(R.id.textViewCreationDate);
            	
            	convertView.setTag(holder);
            } else
                holder = (ViewHolder)convertView.getTag();
            
            //set data
            SoftNews news = newsList.get(position);
            if(news != null){
            	if(notEmpty(news.getPicture())){
            		holder.imageViewPic.setVisibility(View.VISIBLE);
            		imageLoader.displayBitmap(holder.imageViewPic, news.getPicture());
            	}
            	else
            		holder.imageViewPic.setVisibility(View.GONE);
            	if(notEmpty(news.getNews_en()))
            		holder.textViewNewsEN.setText(news.getNews_en());
            	if(notEmpty(news.getNews_zn()))
            		holder.textViewNewsZN.setText(news.getNews_zn());
            	if(news.getCreationDate() != null)
					try {
						holder.textViewCreationDate.setText(Utils.dateToString(news.getCreationDate(), Config.NEWS_ITEM_DATE_FORMAT));
					} catch (Exception e) { e.printStackTrace();	}
            	if(notEmpty(news.getTags()))
            		holder.textViewTags.setText(news.getTags());
            }
            return convertView;
		}
	}
	
    private void testUpdates() throws IllegalStateException, IOException {
    	InputStream in = null;
    	if(DEBUG){
    		//for test
    		in = Utils.readSampleJson(getResources());
    	}else{
    		// for real function
    		HttpRetriever httpRetriever = new HttpRetriever();
    		HttpResponse httpResponse = httpRetriever.requestGet(url, null);
    		in = httpResponse.getEntity().getContent();
    	}
		ArrayList<SoftNews> newsList = Utils.readNewsFromJsonStream(in);
		for(int i=0; newsList != null && i<newsList.size(); i++){
			//Log.i(TAG, "news " + i + ": " + newsList.get(i).toString());
			newsAdapter.add(newsList.get(i));
		}
		
		//refresh ui
		newsAdapter.notifyDataSetChanged();
	}
    
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_clear_cache:
			imageLoader.clearCache();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
    
    /** Activity life cycle */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_list);
        
        imageLoader = AsyncImageLoader.getInstance(this);
        
        //views
        listViewNews = (ListView) findViewById(R.id.listViewNews);
        
        newsAdapter = new NewsAdapter(this, R.layout.news_item, new ArrayList<SoftNews>());
        listViewNews.setAdapter(newsAdapter);
        
        try {
			testUpdates();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

}

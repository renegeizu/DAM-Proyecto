package com.izv.feedrss;

	import java.util.ArrayList;
	import java.util.List;
	import android.net.Uri;
	import android.os.AsyncTask;
	import android.os.Bundle;
	import android.app.Activity;
	import android.content.Intent;
	import android.view.Menu;
	import android.view.View;
	import android.view.View.OnClickListener;
	import android.widget.AdapterView;
	import android.widget.AdapterView.OnItemClickListener;
	import android.widget.Button;
	import android.widget.ListView;


public class Principal extends Activity {
	
	private Button bt;
    private List<ItemRss> lista;
    private ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_principal);
		bt=(Button) findViewById(R.id.btActualizar);
        bt.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		CargarXmlTask tarea = new CargarXmlTask();
        		tarea.execute("http://20minutos.feedsportal.com/c/32489/f/478284/index.rss");
        	}
        });
        lv=(ListView) findViewById(R.id.listView1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.principal, menu);
		return true;
	}
	
	private class CargarXmlTask extends AsyncTask<String,Integer,Boolean> {
		
        protected Boolean doInBackground(String... params) {
	       	 RssParser saxparser = new RssParser(params[0]);
	       	 lista = saxparser.parse();
	       	 return true;
        }
       
        protected void onPostExecute(Boolean result) {
        	Adaptador adaptador=new Adaptador(getApplicationContext(), (ArrayList<ItemRss>) lista);
        	lv.setAdapter(adaptador);
    		lv.setOnItemClickListener(new OnItemClickListener(){
    			@Override
    			public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
    				ItemRss elemento= lista.get(pos);
    				String url=elemento.getLink().trim();
					Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					startActivity(i);
    			}	
    		});
        }
	}
}



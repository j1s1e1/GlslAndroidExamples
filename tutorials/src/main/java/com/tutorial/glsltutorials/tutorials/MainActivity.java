package com.tutorial.glsltutorials.tutorials;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.Tutorials.Tutorials;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends ListActivity {

    private static final String ITEM_IMAGE = "item_image";
    private static final String ITEM_TITLE = "item_title";
    private static final String ITEM_SUBTITLE = "item_subtitle";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Shader.context = this;

        // Initialize data
        final List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        final SparseArray<Class<? extends Activity>> activityMapping = new SparseArray<Class<? extends Activity>>();

        int i = 0;
        {
            final Map<String, Object> item = new HashMap<String, Object>();
            item.put(ITEM_IMAGE, null);
            item.put(ITEM_TITLE, "Tutorials Title");
            item.put(ITEM_SUBTITLE, "Tutorials");
            data.add(item);
            activityMapping.put(i++, Tutorials.class);
        }

        final SimpleAdapter dataAdapter = new SimpleAdapter(this, data, R.layout.activity_main,
                new String[] {ITEM_IMAGE, ITEM_TITLE, ITEM_SUBTITLE},
                new int[] {R.id.Image, R.id.Title, R.id.SubTitle});
        setListAdapter(dataAdapter);

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                final Class<? extends Activity> activityToLaunch = activityMapping.get(position);
                if (activityToLaunch != null)
                {
                    final Intent launchIntent = new Intent(MainActivity.this, activityToLaunch);
                    startActivity(launchIntent);
                }
            }
        });
        // skip this screen
        final Intent launchIntent = new Intent(MainActivity.this, activityMapping.get(0));
        startActivity(launchIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

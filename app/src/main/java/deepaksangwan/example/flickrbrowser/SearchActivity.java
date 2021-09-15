package deepaksangwan.example.flickrbrowser;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.widget.SearchView;

import androidx.navigation.ui.AppBarConfiguration;

import deepaksangwan.example.flickrbrowser.databinding.ActivitySearchBinding;

public class SearchActivity extends BaseActivity {
    private static final String TAG = "SearchActivity";
    private SearchView mSearchview;
    private AppBarConfiguration appBarConfiguration;
    private ActivitySearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: created");
        super.onCreate(savedInstanceState);

        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

       activateToolbar(true);
        Log.d(TAG, "onCreate: ended");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: starts");
        getMenuInflater().inflate(R.menu.menu_search,menu);
        
        SearchManager searchManager = (SearchManager)getSystemService(SEARCH_SERVICE);
        mSearchview = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        mSearchview.setSearchableInfo(searchableInfo);
      //  Log.d(TAG, "onCreateOptionsMenu: started" + getComponentName().toString());
       // Log.d(TAG, "onCreateOptionsMenu: hint is" + mSearchview.getQueryHint());
        //Log.d(TAG, "onCreateOptionsMenu: searchable info is" + searchableInfo.toString());
        mSearchview.setIconified(false);
        mSearchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit: called");
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                sharedPreferences.edit().putString(FLICKR_QUERY,query).apply();
                finish();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mSearchview.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                finish();
                return false;
            }
        });
        Log.d(TAG, "onCreateOptionsMenu: returned"+ true);
        return true;
    }
}
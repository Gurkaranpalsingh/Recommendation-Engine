package com.example.movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class MovieCategories extends AppCompatActivity {


    ArrayAdapter<Data> dataListAdapter;
    ListView myListView;
    List<Data> dataList;
    String url;
    SearchView searchView;
    Spinner spinnerSorting;

    private ArrayList<SortingData> sortingList;
    private SortingAdapter sortingAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_categories);
        dataList = new ArrayList<Data>();

        Intent intent = getIntent();
        url = intent.getStringExtra("URL");
        myListView = findViewById(R.id.lv_list_view);
        new runBackgroundTask().execute(url);
        searchView = (SearchView) findViewById(R.id.searchView);
        initList();
        spinnerSorting = findViewById(R.id.spinner_sorting);
        sortingAdapter = new SortingAdapter(MovieCategories.this, sortingList);
        // set a adapter
        spinnerSorting.setAdapter(sortingAdapter);

        Toolbar toolbar = findViewById(R.id.my_toolbar_movie);
        setSupportActionBar(toolbar);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.sample_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.favourite) {
            Intent intent = new Intent(MovieCategories.this, LikedMovies.class);
            startActivity(intent);
        }  if (item.getItemId() == R.id.recommend) {
            Intent intent = new Intent(MovieCategories.this, Recommend.class);
            startActivity(intent);
        }        else {
            return super.onOptionsItemSelected(item);
        }
        return  true;
    }

    private void initList() {
        sortingList = new ArrayList<>();
        sortingList.add(new SortingData("Sort By Name"));
        sortingList.add(new SortingData("Sort By Rating"));
    }


    public void sortArrayListPrice() {
        Collections.sort(dataList, new Comparator<Data>() {
            @Override
            public int compare(Data data, Data data1) {
                return data1.getVote_average().compareTo(data.getVote_average());
            }
        });
        dataListAdapter.notifyDataSetChanged();
    }


    public void sortArrayListMovie() {
        Collections.sort(dataList, new Comparator<Data>() {
            @Override
            public int compare(Data data, Data data2) {
                return data.getTitle().compareTo(data2.getTitle());
            }
        });
        dataListAdapter.notifyDataSetChanged();
    }

    public class runBackgroundTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {
            String response = HttpRequest.excuteGet(urls[0]);
            return response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String data) {

            try {
                JSONObject jsonObj = new JSONObject(data);
                JSONArray results = jsonObj.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject result = results.getJSONObject(i);
                    String title = result.getString("title");
                    Double vote_average = result.getDouble("vote_average");
                    String release_date = result.getString("release_date");
                    String overview = result.getString("overview");
                    Data dataObject = new Data(title, release_date, vote_average, overview);
                    dataList.add(dataObject);

                    dataListAdapter = new ListAdapter(MovieCategories.this, R.layout.adapter_view_layout, dataList);
                    myListView.setAdapter(dataListAdapter);
                    // item on clicked listener.. goes to detail view
                    myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            Data data = (Data) adapterView.getItemAtPosition(position);
                            Intent intent = new Intent(MovieCategories.this, Detail.class);
                            intent.putExtra("title", data.getTitle());
                            intent.putExtra("overview", data.getOverview());
                            intent.putExtra("publishDate", data.getRelease_date());
                            intent.putExtra("link", data.getVote_average());
                            startActivity(intent);
                        }
                    });

                    // search view for searching
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            int textLength = newText.length();
                            ArrayList<Data> tempList = new ArrayList<Data>();
                            for (Data tempData : dataList) {
                                if (textLength <= tempData.getTitle().length()) {
                                    if (tempData.getTitle().toLowerCase().contains(newText.toLowerCase()) ||
                                            tempData.getOverview().toLowerCase().contains(newText.toLowerCase())
                                    ) {
                                        tempList.add(tempData);
                                    }
                                }
                            }

                            dataListAdapter = new ListAdapter(MovieCategories.this, R.layout.adapter_view_layout, tempList);
                            myListView.setAdapter(dataListAdapter);
                            return true;
                        }
                    });

                    //Sorting by dropdown
                    spinnerSorting.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            SortingData clickedItem = (SortingData) adapterView.getItemAtPosition(i);
                            String clickedName = clickedItem.getName();
                            if (clickedName.equalsIgnoreCase("sort by rating")) {
                                sortArrayListPrice();
                            }
                            if (clickedName.equalsIgnoreCase("sort by name")) {
                                sortArrayListMovie();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
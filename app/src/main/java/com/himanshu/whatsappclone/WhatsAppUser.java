package com.himanshu.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class WhatsAppUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_app_user);

        final ListView listView = findViewById(R.id.listView);
        final ArrayList<String> waUsers = new ArrayList<>();

        final ArrayAdapter adapter = new ArrayAdapter(WhatsAppUser.this, android.R.layout.simple_list_item_1 , waUsers);      // storing item of list in the adapter.

        final SwipeRefreshLayout mySwipeRefreshLayout = findViewById(R.id.swipeRefresh);

        // to get data from parse server
        try {

            ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
            parseQuery.whereNotEqualTo("username" , ParseUser.getCurrentUser().getUsername());
            parseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if(objects.size() > 0 && e == null)
                    {
                        for(ParseUser user : objects)
                        {
                            waUsers.add(user.getUsername());
                        }

                        listView.setAdapter(adapter);       // setting adapter for the listView.
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        // to get new users (if present) on swipe to refresh
        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                try {

                    ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
                    parseQuery.whereNotEqualTo("username" , ParseUser.getCurrentUser().getUsername());  // as we don't want current user.
                    parseQuery.whereNotContainedIn("username" , waUsers);       // as we also don't want those users which are already present in arrayList waUsers

                    parseQuery.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> objects, ParseException e) {
                            if(objects.size() > 0)  // new users are present
                            {
                                if(e == null)
                                {
                                    for(ParseUser user : objects)
                                    {
                                        waUsers.add(user.getUsername());
                                    }

                                    adapter.notifyDataSetChanged();     // to notify the listView that dataset is changed

                                    if(mySwipeRefreshLayout.isRefreshing())     // as after getting new user we need to stop the refreshing.
                                    {
                                        mySwipeRefreshLayout.setRefreshing(false);
                                    }
                                }
                            }
                            else    // when no new user is present
                            {
                                if(mySwipeRefreshLayout.isRefreshing())
                                {
                                    mySwipeRefreshLayout.setRefreshing(false);
                                }
                            }
                        }
                    });
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.my_menu , menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.logoutUserItem :

                FancyToast.makeText(WhatsAppUser.this , ParseUser.getCurrentUser().getUsername() + " is logged out." ,
                        FancyToast.LENGTH_SHORT , FancyToast.SUCCESS , true).show();

                ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null)
                        {
                            Intent intent = new Intent(WhatsAppUser.this , SignUp.class);
                            startActivity(intent);

                            finish();
                        }
                    }
                });

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
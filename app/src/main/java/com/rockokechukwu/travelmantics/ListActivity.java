package com.rockokechukwu.travelmantics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rockokechukwu.travelmantics.adapter.DealAdapter;
import com.rockokechukwu.travelmantics.model.TravelDeal;
import com.rockokechukwu.travelmantics.util.FirebaseUtil;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    ArrayList<TravelDeal> deals;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseRef;
    private ChildEventListener mChildListener;
    private DividerItemDecoration mDividerItemDecoration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("is Admin: ", ""+FirebaseUtil.isAdmin);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (FirebaseUtil.isAdmin == true){
            menu.findItem(R.id.insert_menu).setVisible(true);
        } else {
            menu.findItem(R.id.insert_menu).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.insert_menu:
                Intent intent = new Intent(this, DealActivity.class);
                startActivity(intent);
                return true;
            case R.id.logout_menu:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("Logout", "User logged out.");
                                FirebaseUtil.attachlistener();
                                // ...
                            }
                        });
                FirebaseUtil.detachListener();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseUtil.detachListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUtil.openFbReference("traveldeals", this);
        RecyclerView rvDeals = (RecyclerView) findViewById(R.id.rvDeals);
        final DealAdapter adapter = new DealAdapter();
        rvDeals.setAdapter(adapter);
        LinearLayoutManager dealsLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvDeals.setLayoutManager(dealsLayoutManager);

        mDividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvDeals.addItemDecoration(mDividerItemDecoration);
        rvDeals.setHasFixedSize(true);
        FirebaseUtil.attachlistener();
    }

    public void showMenu() {
        invalidateOptionsMenu();
    }
}

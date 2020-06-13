package it.runningexamples.fiscalcode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

public class SavedActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<CodiceFiscaleEntity> cfList;
    ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);

//        cfList = AppDatabase.getInstance(getApplicationContext()).codiceFiscaleDAO().getAll();

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
//        mAdapter = new RecyclerAdapter(cfList, getApplicationContext());
        mAdapter = new RecyclerAdapter(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        itemTouchHelper = new ItemTouchHelper(new SwipeCallback(mAdapter, mRecyclerView));

        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }


}

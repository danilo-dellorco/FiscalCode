package it.runningexamples.fiscalcode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseSequence;

import java.util.List;

public class SavedActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<CodiceFiscaleEntity> cfList;
    ItemTouchHelper itemTouchHelper;
    PreferenceManager prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtilities.applyActivityTheme(this);
        super.onCreate(savedInstanceState);
        if (AppDatabase.getInstance(getApplicationContext()).codiceFiscaleDAO().getDbSize() == 0){
            setContentView(R.layout.layout_empty_list);
        }
        else{
            prefs = new PreferenceManager(this);
            setContentView(R.layout.activity_saved);
            mRecyclerView = findViewById(R.id.recyclerView);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(this);
            mAdapter = new RecyclerAdapter(getApplicationContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);
            itemTouchHelper = new ItemTouchHelper(new SwipeCallback(mAdapter, mRecyclerView));
            itemTouchHelper.attachToRecyclerView(mRecyclerView);
            if (prefs.isFirstActivity("saved")){
                firstTutorial();
            }
        }
        Toolbar toolbarList = findViewById(R.id.toolbarList);
        setSupportActionBar(toolbarList);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void firstTutorial(){
        BubbleShowCaseBuilder builder1 = new BubbleShowCaseBuilder(SavedActivity.this);
        builder1.title("Clicca sulla carta per visualizzare il barcode");
        builder1.targetView(findViewById(R.id.help1));

        BubbleShowCaseBuilder builder2 = new BubbleShowCaseBuilder(SavedActivity.this);
        builder2.title("Tocca il codice per copiarlo rapidamente negli appunti");
        builder2.targetView(findViewById(R.id.help2));

        BubbleShowCaseBuilder builder3 = new BubbleShowCaseBuilder(SavedActivity.this);
        builder3.title("Scorri vestro sinistra per eliminare un codice salvato");
        builder3.targetView(findViewById(R.id.help3));
        builder3.image(getDrawable(R.drawable.swipe_left)); //Bubble main image

        BubbleShowCaseSequence sequence = new BubbleShowCaseSequence();
        sequence.addShowCase(builder1);
        sequence.addShowCase(builder2);
        sequence.addShowCase(builder3);
        sequence.show();

        prefs.setFirstActivity("saved",false);
    }
}

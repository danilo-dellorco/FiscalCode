package it.runningexamples.fiscalcode;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeCallback extends ItemTouchHelper.SimpleCallback {

    private ColorDrawable redBackground;
    private Drawable deleteIcon;
    private RecyclerAdapter recyclerAdapter;
    private RecyclerView rcv;
    int intrinsicWidth, intrinsicHeight;

    SwipeCallback(RecyclerAdapter adapter, RecyclerView rcv) {
        super(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.LEFT);
        recyclerAdapter = adapter;
        this.rcv = rcv;
        redBackground = new ColorDrawable(Color.RED);
        deleteIcon = ContextCompat.getDrawable(recyclerAdapter.getContext(), R.drawable.bin72);
        intrinsicWidth = deleteIcon.getIntrinsicWidth();
        intrinsicHeight = deleteIcon.getIntrinsicHeight();
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        recyclerAdapter.deleteItem(position, rcv);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View itemView = viewHolder.itemView;
        int itemHeight = itemView.getHeight();
        int backgroundCornerOffset = 10;        //backgroundCornerOffset per mettere il background dietro la card

        int deleteIconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
        int deleteIconMargin = (itemHeight - intrinsicHeight) / 8;
        int deleteIconLeft = itemView.getRight() - deleteIconMargin - intrinsicWidth;
        int deleteIconRight = itemView.getRight() - deleteIconMargin;
        int deleteIconBottom = deleteIconTop + intrinsicHeight;

        if (dX < 0) { // swipe verso sinistra
            deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
            redBackground.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else { // nessuno swipe
            redBackground.setBounds(0, 0, 0, 0);
        }
        redBackground.draw(c);
        deleteIcon.draw(c);
    }

}

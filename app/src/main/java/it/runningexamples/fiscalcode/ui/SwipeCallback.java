package it.runningexamples.fiscalcode.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import it.runningexamples.fiscalcode.R;

public class SwipeCallback extends ItemTouchHelper.SimpleCallback {

    private ColorDrawable redBackground;
    private Drawable deleteIcon;
    private RecyclerAdapter recyclerAdapter;
    private RecyclerView rcv;
    int intrinsicWidthDelete, intrinsicHeightDelete;

    SwipeCallback(RecyclerAdapter adapter, RecyclerView rcv) {
        super(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.LEFT);
        recyclerAdapter = adapter;
        this.rcv = rcv;
        redBackground = new ColorDrawable(Color.parseColor("#B80000")); //NON-NLS
        deleteIcon = ContextCompat.getDrawable(recyclerAdapter.getContext(), R.drawable.swipe_delete);
        intrinsicWidthDelete = deleteIcon.getIntrinsicWidth();
        intrinsicHeightDelete = deleteIcon.getIntrinsicHeight();
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.LEFT){
            recyclerAdapter.deleteItem(position, rcv);
        }else{
            recyclerAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View itemView = viewHolder.itemView;
        int itemHeight = itemView.getHeight();
        int backgroundCornerOffset = 10;        //backgroundCornerOffset per mettere il background dietro la card

        int deleteIconTop = itemView.getTop() + (itemHeight - intrinsicHeightDelete) / 2;
        int deleteIconMargin = (itemHeight - intrinsicHeightDelete) / 8;
        int deleteIconLeft = itemView.getRight() - deleteIconMargin - intrinsicWidthDelete;
        int deleteIconRight = itemView.getRight() - deleteIconMargin;
        int deleteIconBottom = deleteIconTop + intrinsicHeightDelete;

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

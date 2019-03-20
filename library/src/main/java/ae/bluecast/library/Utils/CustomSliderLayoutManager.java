package ae.bluecast.library.Utils;

import ae.bluecast.library.Interfaces.CharacterScrollListener;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class CustomSliderLayoutManager extends LinearLayoutManager {

    RecyclerView recyclerView;
    CharacterScrollListener selectorListener;
    LinearSnapHelper linearSnapHelper = new LinearSnapHelper();
    public static boolean isDragging = false;

    public CustomSliderLayoutManager(Context context, int orientation, boolean reverseLayout, CharacterScrollListener selectorListener) {
        super(context, orientation, reverseLayout);
        this.selectorListener = selectorListener;
    }


    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        recyclerView = view;
        if (linearSnapHelper != null)
            linearSnapHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        scaleDownView();
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {

        int scrolled = super.scrollHorizontallyBy(dx, recycler, state);
        scaleDownView();
        return scrolled;

    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        super.smoothScrollToPosition(recyclerView, state, position);
//        selectorListener.onItemScrolled(position);
    }

    @Override
    public boolean canScrollHorizontally() {
        return super.canScrollHorizontally();
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);

        switch (state) {
            case RecyclerView.SCROLL_STATE_DRAGGING:
                isDragging = true;
                break;
            case RecyclerView.SCROLL_STATE_IDLE:
                int recyclerViewCenterX = getRecyclerViewCenterX();
                int minDistance = recyclerView.getWidth();
                int position = -1;
                int tempDistance = 0;
                int childCount = recyclerView.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View child = recyclerView.getChildAt(i);
                    int decLEft = getDecoratedLeft(child);
                    int decRight = getDecoratedRight(child);

                    int childCenterX = getDecoratedLeft(child) + (getDecoratedRight(child) - getDecoratedLeft(child)) / 2;
                    int childDistanceFromCenter = Math.abs(childCenterX - recyclerViewCenterX);
//                    if (childDistanceFromCenter < minDistance) {
//                        minDistance = childDistanceFromCenter;
//                        position = recyclerView.getChildLayoutPosition(child);
//                    }
                    if (i == 0)
                        tempDistance = childDistanceFromCenter;

                    if (childDistanceFromCenter <= tempDistance) {
                        tempDistance = childDistanceFromCenter;
                        position = recyclerView.getChildLayoutPosition(child);
                    }
                    int positio1 = position;
                }

                if (isDragging) {
                    selectorListener.onItemScrolled(position);
                    isDragging = false;
                }
                break;
        }
    }

    @Override
    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
    }

    private int getRecyclerViewCenterX() {
        return (recyclerView.getRight() - recyclerView.getLeft()) / 2 + recyclerView.getLeft();
    }

    private void scaleDownView() {
        float mid = recyclerView.getWidth() / 2.0f;
        for (int i = 0; i < recyclerView.getChildCount(); i++) {

            // Calculating the distance of the child from the center
            View child = getChildAt(i);
            float childMid = (getDecoratedLeft(child) + getDecoratedRight(child)) / 2.0f;
            float distanceFromCenter = Math.abs(mid - childMid);

            // The scaling formula
            float scale = (float) (1 - Math.sqrt((distanceFromCenter / recyclerView.getWidth())) * 0.66f);

            // Set scale to view
            child.setScaleX(scale);
            child.setScaleY(scale);
        }
    }

}

package com.meetu.view;

import android.support.v7.widget.RecyclerView;
import android.widget.AbsListView;
import android.widget.ScrollView;

/**
 * Created by desmond on 10/4/15.
 */
public interface ScrollTabHolder {

	void adjustScroll(int scrollHeight, int headerHeight);

	void onScrollViewScroll(ScrollView view, int x, int y, int oldX, int oldY,
			int pagePosition);

	void onRecyclerViewScroll(RecyclerView view, int scrollY, int pagePosition);
}

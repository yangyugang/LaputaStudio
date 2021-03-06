/**
 * Created by YuGang Yang on December 24, 2014.
 * Copyright 2007-2015 Loopeer.com. All rights reserved.
 */
package com.laputa.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import com.laputa.widget.ResourceLoadingIndicator;

public abstract class BasePagedCollectionFragment<T> extends BaseCollectionFragment<T> implements
    AbsListView.OnScrollListener {

  private ResourceLoadingIndicator mLoadingIndicator;

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mLoadingIndicator = new ResourceLoadingIndicator(getActivity());
    mLoadingIndicator.setList(getListView());
    getListView().setOnScrollListener(this);
  }

  @Override public void onFinish() {
    super.onFinish();
    mLoadingIndicator.setVisible(getDataLoader().hasMore());
  }

  @Override public void onScrollStateChanged(AbsListView view, int scrollState) {
    // Intentionally left blank
  }

  protected boolean isUsable() {
    return getActivity() != null && !getActivity().isFinishing();
  }

  @Override public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
      int totalItemCount) {
    if (!isUsable()
        || hasWarned
        || !getDataLoader().canLoadMore()
        || totalItemCount == 0
        || view.getLastVisiblePosition() != totalItemCount - 1) {
      return;
    }

    hasWarned = true;

    if (getListView() != null && getListView().getLastVisiblePosition() >= getDataLoader().size()) {
      getDataLoader().loadMore();
    }
  }
}

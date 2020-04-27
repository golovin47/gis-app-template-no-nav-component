package com.gis.featurefeed.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class FragmentFeedBinding extends ViewDataBinding {
  @NonNull
  public final ProgressBar pbFeed;

  @NonNull
  public final RecyclerView rvFeed;

  @NonNull
  public final Toolbar tbFeed;

  protected FragmentFeedBinding(Object _bindingComponent, View _root, int _localFieldCount,
      ProgressBar pbFeed, RecyclerView rvFeed, Toolbar tbFeed) {
    super(_bindingComponent, _root, _localFieldCount);
    this.pbFeed = pbFeed;
    this.rvFeed = rvFeed;
    this.tbFeed = tbFeed;
  }

  @NonNull
  public static FragmentFeedBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.fragment_feed, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static FragmentFeedBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<FragmentFeedBinding>inflateInternal(inflater, com.gis.featurefeed.R.layout.fragment_feed, root, attachToRoot, component);
  }

  @NonNull
  public static FragmentFeedBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.fragment_feed, null, false, component)
   */
  @NonNull
  @Deprecated
  public static FragmentFeedBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<FragmentFeedBinding>inflateInternal(inflater, com.gis.featurefeed.R.layout.fragment_feed, null, false, component);
  }

  public static FragmentFeedBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.bind(view, component)
   */
  @Deprecated
  public static FragmentFeedBinding bind(@NonNull View view, @Nullable Object component) {
    return (FragmentFeedBinding)bind(component, view, com.gis.featurefeed.R.layout.fragment_feed);
  }
}

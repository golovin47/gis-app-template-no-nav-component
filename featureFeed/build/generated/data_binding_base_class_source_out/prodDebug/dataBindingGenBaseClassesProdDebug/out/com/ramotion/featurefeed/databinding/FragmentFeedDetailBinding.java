package com.gis.featurefeed.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class FragmentFeedDetailBinding extends ViewDataBinding {
  @NonNull
  public final ImageView ivImage;

  @NonNull
  public final ConstraintLayout mainView;

  @NonNull
  public final TextView tvDescription;

  @NonNull
  public final TextView tvTitle;

  protected FragmentFeedDetailBinding(Object _bindingComponent, View _root, int _localFieldCount,
      ImageView ivImage, ConstraintLayout mainView, TextView tvDescription, TextView tvTitle) {
    super(_bindingComponent, _root, _localFieldCount);
    this.ivImage = ivImage;
    this.mainView = mainView;
    this.tvDescription = tvDescription;
    this.tvTitle = tvTitle;
  }

  @NonNull
  public static FragmentFeedDetailBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.fragment_feed_detail, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static FragmentFeedDetailBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<FragmentFeedDetailBinding>inflateInternal(inflater, com.gis.featurefeed.R.layout.fragment_feed_detail, root, attachToRoot, component);
  }

  @NonNull
  public static FragmentFeedDetailBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.fragment_feed_detail, null, false, component)
   */
  @NonNull
  @Deprecated
  public static FragmentFeedDetailBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<FragmentFeedDetailBinding>inflateInternal(inflater, com.gis.featurefeed.R.layout.fragment_feed_detail, null, false, component);
  }

  public static FragmentFeedDetailBinding bind(@NonNull View view) {
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
  public static FragmentFeedDetailBinding bind(@NonNull View view, @Nullable Object component) {
    return (FragmentFeedDetailBinding)bind(component, view, com.gis.featurefeed.R.layout.fragment_feed_detail);
  }
}

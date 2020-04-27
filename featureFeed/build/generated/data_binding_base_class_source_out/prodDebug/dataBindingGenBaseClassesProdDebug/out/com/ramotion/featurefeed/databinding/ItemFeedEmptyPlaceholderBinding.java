package com.gis.featurefeed.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class ItemFeedEmptyPlaceholderBinding extends ViewDataBinding {
  @NonNull
  public final ImageView ivImage;

  @NonNull
  public final TextView tvTitle;

  protected ItemFeedEmptyPlaceholderBinding(Object _bindingComponent, View _root,
      int _localFieldCount, ImageView ivImage, TextView tvTitle) {
    super(_bindingComponent, _root, _localFieldCount);
    this.ivImage = ivImage;
    this.tvTitle = tvTitle;
  }

  @NonNull
  public static ItemFeedEmptyPlaceholderBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.item_feed_empty_placeholder, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ItemFeedEmptyPlaceholderBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ItemFeedEmptyPlaceholderBinding>inflateInternal(inflater, com.gis.featurefeed.R.layout.item_feed_empty_placeholder, root, attachToRoot, component);
  }

  @NonNull
  public static ItemFeedEmptyPlaceholderBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.item_feed_empty_placeholder, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ItemFeedEmptyPlaceholderBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ItemFeedEmptyPlaceholderBinding>inflateInternal(inflater, com.gis.featurefeed.R.layout.item_feed_empty_placeholder, null, false, component);
  }

  public static ItemFeedEmptyPlaceholderBinding bind(@NonNull View view) {
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
  public static ItemFeedEmptyPlaceholderBinding bind(@NonNull View view,
      @Nullable Object component) {
    return (ItemFeedEmptyPlaceholderBinding)bind(component, view, com.gis.featurefeed.R.layout.item_feed_empty_placeholder);
  }
}

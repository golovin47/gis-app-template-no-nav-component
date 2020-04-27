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
import com.google.android.material.card.MaterialCardView;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class ItemFeedBinding extends ViewDataBinding {
  @NonNull
  public final MaterialCardView feedItemCard;

  @NonNull
  public final ImageView ivImage;

  @NonNull
  public final ConstraintLayout mainView;

  @NonNull
  public final TextView tvDescription;

  @NonNull
  public final TextView tvTitle;

  protected ItemFeedBinding(Object _bindingComponent, View _root, int _localFieldCount,
      MaterialCardView feedItemCard, ImageView ivImage, ConstraintLayout mainView,
      TextView tvDescription, TextView tvTitle) {
    super(_bindingComponent, _root, _localFieldCount);
    this.feedItemCard = feedItemCard;
    this.ivImage = ivImage;
    this.mainView = mainView;
    this.tvDescription = tvDescription;
    this.tvTitle = tvTitle;
  }

  @NonNull
  public static ItemFeedBinding inflate(@NonNull LayoutInflater inflater, @Nullable ViewGroup root,
      boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.item_feed, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ItemFeedBinding inflate(@NonNull LayoutInflater inflater, @Nullable ViewGroup root,
      boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ItemFeedBinding>inflateInternal(inflater, com.gis.featurefeed.R.layout.item_feed, root, attachToRoot, component);
  }

  @NonNull
  public static ItemFeedBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.item_feed, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ItemFeedBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ItemFeedBinding>inflateInternal(inflater, com.gis.featurefeed.R.layout.item_feed, null, false, component);
  }

  public static ItemFeedBinding bind(@NonNull View view) {
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
  public static ItemFeedBinding bind(@NonNull View view, @Nullable Object component) {
    return (ItemFeedBinding)bind(component, view, com.gis.featurefeed.R.layout.item_feed);
  }
}

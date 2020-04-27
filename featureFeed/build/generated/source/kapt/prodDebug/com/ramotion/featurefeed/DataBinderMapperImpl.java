package com.gis.featurefeed;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import androidx.databinding.DataBinderMapper;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.gis.featurefeed.databinding.FragmentFeedBindingImpl;
import com.gis.featurefeed.databinding.FragmentFeedDetailBindingImpl;
import com.gis.featurefeed.databinding.ItemFeedBindingImpl;
import com.gis.featurefeed.databinding.ItemFeedEmptyPlaceholderBindingImpl;
import com.gis.featurefeed.databinding.ItemFeedLoadingBindingImpl;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBinderMapperImpl extends DataBinderMapper {
  private static final int LAYOUT_FRAGMENTFEED = 1;

  private static final int LAYOUT_FRAGMENTFEEDDETAIL = 2;

  private static final int LAYOUT_ITEMFEED = 3;

  private static final int LAYOUT_ITEMFEEDEMPTYPLACEHOLDER = 4;

  private static final int LAYOUT_ITEMFEEDLOADING = 5;

  private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP = new SparseIntArray(5);

  static {
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.gis.featurefeed.R.layout.fragment_feed, LAYOUT_FRAGMENTFEED);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.gis.featurefeed.R.layout.fragment_feed_detail, LAYOUT_FRAGMENTFEEDDETAIL);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.gis.featurefeed.R.layout.item_feed, LAYOUT_ITEMFEED);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.gis.featurefeed.R.layout.item_feed_empty_placeholder, LAYOUT_ITEMFEEDEMPTYPLACEHOLDER);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.gis.featurefeed.R.layout.item_feed_loading, LAYOUT_ITEMFEEDLOADING);
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View view, int layoutId) {
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = view.getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
        case  LAYOUT_FRAGMENTFEED: {
          if ("layout/fragment_feed_0".equals(tag)) {
            return new FragmentFeedBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_feed is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTFEEDDETAIL: {
          if ("layout/fragment_feed_detail_0".equals(tag)) {
            return new FragmentFeedDetailBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_feed_detail is invalid. Received: " + tag);
        }
        case  LAYOUT_ITEMFEED: {
          if ("layout/item_feed_0".equals(tag)) {
            return new ItemFeedBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for item_feed is invalid. Received: " + tag);
        }
        case  LAYOUT_ITEMFEEDEMPTYPLACEHOLDER: {
          if ("layout/item_feed_empty_placeholder_0".equals(tag)) {
            return new ItemFeedEmptyPlaceholderBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for item_feed_empty_placeholder is invalid. Received: " + tag);
        }
        case  LAYOUT_ITEMFEEDLOADING: {
          if ("layout/item_feed_loading_0".equals(tag)) {
            return new ItemFeedLoadingBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for item_feed_loading is invalid. Received: " + tag);
        }
      }
    }
    return null;
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View[] views, int layoutId) {
    if(views == null || views.length == 0) {
      return null;
    }
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = views[0].getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
      }
    }
    return null;
  }

  @Override
  public int getLayoutId(String tag) {
    if (tag == null) {
      return 0;
    }
    Integer tmpVal = InnerLayoutIdLookup.sKeys.get(tag);
    return tmpVal == null ? 0 : tmpVal;
  }

  @Override
  public String convertBrIdToString(int localId) {
    String tmpVal = InnerBrLookup.sKeys.get(localId);
    return tmpVal;
  }

  @Override
  public List<DataBinderMapper> collectDependencies() {
    ArrayList<DataBinderMapper> result = new ArrayList<DataBinderMapper>(3);
    result.add(new androidx.databinding.library.baseAdapters.DataBinderMapperImpl());
    result.add(new com.gis.repository.DataBinderMapperImpl());
    result.add(new com.gis.utils.DataBinderMapperImpl());
    return result;
  }

  private static class InnerBrLookup {
    static final SparseArray<String> sKeys = new SparseArray<String>(2);

    static {
      sKeys.put(0, "_all");
    }
  }

  private static class InnerLayoutIdLookup {
    static final HashMap<String, Integer> sKeys = new HashMap<String, Integer>(5);

    static {
      sKeys.put("layout/fragment_feed_0", com.gis.featurefeed.R.layout.fragment_feed);
      sKeys.put("layout/fragment_feed_detail_0", com.gis.featurefeed.R.layout.fragment_feed_detail);
      sKeys.put("layout/item_feed_0", com.gis.featurefeed.R.layout.item_feed);
      sKeys.put("layout/item_feed_empty_placeholder_0", com.gis.featurefeed.R.layout.item_feed_empty_placeholder);
      sKeys.put("layout/item_feed_loading_0", com.gis.featurefeed.R.layout.item_feed_loading);
    }
  }
}

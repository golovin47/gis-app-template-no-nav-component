package com.gis.featurefeed.presentation.ui.feeditemdetail;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0002\u0003\u0004B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u0082\u0001\u0002\u0005\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/gis/featurefeed/presentation/ui/feeditemdetail/FeedItemDetailIntent;", "", "()V", "GetFeedItem", "GoBack", "Lcom/gis/featurefeed/presentation/ui/feeditemdetail/FeedItemDetailIntent$GetFeedItem;", "Lcom/gis/featurefeed/presentation/ui/feeditemdetail/FeedItemDetailIntent$GoBack;", "featureFeed_prodDebug"})
public abstract class FeedItemDetailIntent {
    
    private FeedItemDetailIntent() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/gis/featurefeed/presentation/ui/feeditemdetail/FeedItemDetailIntent$GetFeedItem;", "Lcom/gis/featurefeed/presentation/ui/feeditemdetail/FeedItemDetailIntent;", "id", "", "(I)V", "getId", "()I", "featureFeed_prodDebug"})
    public static final class GetFeedItem extends com.gis.featurefeed.presentation.ui.feeditemdetail.FeedItemDetailIntent {
        private final int id = 0;
        
        public final int getId() {
            return 0;
        }
        
        public GetFeedItem(int id) {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/gis/featurefeed/presentation/ui/feeditemdetail/FeedItemDetailIntent$GoBack;", "Lcom/gis/featurefeed/presentation/ui/feeditemdetail/FeedItemDetailIntent;", "()V", "featureFeed_prodDebug"})
    public static final class GoBack extends com.gis.featurefeed.presentation.ui.feeditemdetail.FeedItemDetailIntent {
        public static final com.gis.featurefeed.presentation.ui.feeditemdetail.FeedItemDetailIntent.GoBack INSTANCE = null;
        
        private GoBack() {
            super();
        }
    }
}
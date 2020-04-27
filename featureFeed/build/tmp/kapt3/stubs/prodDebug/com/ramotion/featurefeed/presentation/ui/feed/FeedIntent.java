package com.gis.featurefeed.presentation.ui.feed;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0003\u0003\u0004\u0005B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u0082\u0001\u0003\u0006\u0007\b\u00a8\u0006\t"}, d2 = {"Lcom/gis/featurefeed/presentation/ui/feed/FeedIntent;", "", "()V", "LoadNextFeedPage", "ObserveFeed", "OpenDetail", "Lcom/gis/featurefeed/presentation/ui/feed/FeedIntent$ObserveFeed;", "Lcom/gis/featurefeed/presentation/ui/feed/FeedIntent$LoadNextFeedPage;", "Lcom/gis/featurefeed/presentation/ui/feed/FeedIntent$OpenDetail;", "featureFeed_prodDebug"})
public abstract class FeedIntent {
    
    private FeedIntent() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/gis/featurefeed/presentation/ui/feed/FeedIntent$ObserveFeed;", "Lcom/gis/featurefeed/presentation/ui/feed/FeedIntent;", "()V", "featureFeed_prodDebug"})
    public static final class ObserveFeed extends com.gis.featurefeed.presentation.ui.feed.FeedIntent {
        public static final com.gis.featurefeed.presentation.ui.feed.FeedIntent.ObserveFeed INSTANCE = null;
        
        private ObserveFeed() {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/gis/featurefeed/presentation/ui/feed/FeedIntent$LoadNextFeedPage;", "Lcom/gis/featurefeed/presentation/ui/feed/FeedIntent;", "nextPage", "", "(I)V", "getNextPage", "()I", "featureFeed_prodDebug"})
    public static final class LoadNextFeedPage extends com.gis.featurefeed.presentation.ui.feed.FeedIntent {
        private final int nextPage = 0;
        
        public final int getNextPage() {
            return 0;
        }
        
        public LoadNextFeedPage(int nextPage) {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u000b"}, d2 = {"Lcom/gis/featurefeed/presentation/ui/feed/FeedIntent$OpenDetail;", "Lcom/gis/featurefeed/presentation/ui/feed/FeedIntent;", "itemId", "", "view", "Landroid/view/View;", "(ILandroid/view/View;)V", "getItemId", "()I", "getView", "()Landroid/view/View;", "featureFeed_prodDebug"})
    public static final class OpenDetail extends com.gis.featurefeed.presentation.ui.feed.FeedIntent {
        private final int itemId = 0;
        @org.jetbrains.annotations.NotNull()
        private final android.view.View view = null;
        
        public final int getItemId() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.view.View getView() {
            return null;
        }
        
        public OpenDetail(int itemId, @org.jetbrains.annotations.NotNull()
        android.view.View view) {
            super();
        }
    }
}
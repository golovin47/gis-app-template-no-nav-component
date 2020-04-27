package com.gis.featurefeed.presentation.ui.feed;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0006\u0003\u0004\u0005\u0006\u0007\bB\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u0082\u0001\u0006\t\n\u000b\f\r\u000e\u00a8\u0006\u000f"}, d2 = {"Lcom/gis/featurefeed/presentation/ui/feed/FeedStateChange;", "", "()V", "Error", "FeedItemsReceived", "HideError", "NextPageLoaded", "StartLoading", "StartLoadingNextPage", "Lcom/gis/featurefeed/presentation/ui/feed/FeedStateChange$StartLoading;", "Lcom/gis/featurefeed/presentation/ui/feed/FeedStateChange$StartLoadingNextPage;", "Lcom/gis/featurefeed/presentation/ui/feed/FeedStateChange$NextPageLoaded;", "Lcom/gis/featurefeed/presentation/ui/feed/FeedStateChange$FeedItemsReceived;", "Lcom/gis/featurefeed/presentation/ui/feed/FeedStateChange$Error;", "Lcom/gis/featurefeed/presentation/ui/feed/FeedStateChange$HideError;", "featureFeed_prodDebug"})
public abstract class FeedStateChange {
    
    private FeedStateChange() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/gis/featurefeed/presentation/ui/feed/FeedStateChange$StartLoading;", "Lcom/gis/featurefeed/presentation/ui/feed/FeedStateChange;", "()V", "featureFeed_prodDebug"})
    public static final class StartLoading extends com.gis.featurefeed.presentation.ui.feed.FeedStateChange {
        public static final com.gis.featurefeed.presentation.ui.feed.FeedStateChange.StartLoading INSTANCE = null;
        
        private StartLoading() {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/gis/featurefeed/presentation/ui/feed/FeedStateChange$StartLoadingNextPage;", "Lcom/gis/featurefeed/presentation/ui/feed/FeedStateChange;", "()V", "featureFeed_prodDebug"})
    public static final class StartLoadingNextPage extends com.gis.featurefeed.presentation.ui.feed.FeedStateChange {
        public static final com.gis.featurefeed.presentation.ui.feed.FeedStateChange.StartLoadingNextPage INSTANCE = null;
        
        private StartLoadingNextPage() {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/gis/featurefeed/presentation/ui/feed/FeedStateChange$NextPageLoaded;", "Lcom/gis/featurefeed/presentation/ui/feed/FeedStateChange;", "()V", "featureFeed_prodDebug"})
    public static final class NextPageLoaded extends com.gis.featurefeed.presentation.ui.feed.FeedStateChange {
        public static final com.gis.featurefeed.presentation.ui.feed.FeedStateChange.NextPageLoaded INSTANCE = null;
        
        private NextPageLoaded() {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0013\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\u0002\u0010\u0005R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\b"}, d2 = {"Lcom/gis/featurefeed/presentation/ui/feed/FeedStateChange$FeedItemsReceived;", "Lcom/gis/featurefeed/presentation/ui/feed/FeedStateChange;", "feedItems", "", "Lcom/gis/featurefeed/presentation/ui/feed/FeedListItem;", "(Ljava/util/List;)V", "getFeedItems", "()Ljava/util/List;", "featureFeed_prodDebug"})
    public static final class FeedItemsReceived extends com.gis.featurefeed.presentation.ui.feed.FeedStateChange {
        @org.jetbrains.annotations.NotNull()
        private final java.util.List<com.gis.featurefeed.presentation.ui.feed.FeedListItem> feedItems = null;
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.gis.featurefeed.presentation.ui.feed.FeedListItem> getFeedItems() {
            return null;
        }
        
        public FeedItemsReceived(@org.jetbrains.annotations.NotNull()
        java.util.List<? extends com.gis.featurefeed.presentation.ui.feed.FeedListItem> feedItems) {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/gis/featurefeed/presentation/ui/feed/FeedStateChange$Error;", "Lcom/gis/featurefeed/presentation/ui/feed/FeedStateChange;", "error", "", "(Ljava/lang/Throwable;)V", "getError", "()Ljava/lang/Throwable;", "featureFeed_prodDebug"})
    public static final class Error extends com.gis.featurefeed.presentation.ui.feed.FeedStateChange {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.Throwable error = null;
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.Throwable getError() {
            return null;
        }
        
        public Error(@org.jetbrains.annotations.NotNull()
        java.lang.Throwable error) {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/gis/featurefeed/presentation/ui/feed/FeedStateChange$HideError;", "Lcom/gis/featurefeed/presentation/ui/feed/FeedStateChange;", "()V", "featureFeed_prodDebug"})
    public static final class HideError extends com.gis.featurefeed.presentation.ui.feed.FeedStateChange {
        public static final com.gis.featurefeed.presentation.ui.feed.FeedStateChange.HideError INSTANCE = null;
        
        private HideError() {
            super();
        }
    }
}
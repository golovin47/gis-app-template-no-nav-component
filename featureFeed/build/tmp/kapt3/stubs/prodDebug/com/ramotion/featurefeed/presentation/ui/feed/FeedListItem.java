package com.gis.featurefeed.presentation.ui.feed;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0003\u0003\u0004\u0005B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u0082\u0001\u0003\u0006\u0007\b\u00a8\u0006\t"}, d2 = {"Lcom/gis/featurefeed/presentation/ui/feed/FeedListItem;", "", "()V", "FeedDefaultItem", "FeedEmptyPlaceholderItem", "FeedLoadingItem", "Lcom/gis/featurefeed/presentation/ui/feed/FeedListItem$FeedDefaultItem;", "Lcom/gis/featurefeed/presentation/ui/feed/FeedListItem$FeedLoadingItem;", "Lcom/gis/featurefeed/presentation/ui/feed/FeedListItem$FeedEmptyPlaceholderItem;", "featureFeed_prodDebug"})
public abstract class FeedListItem {
    
    private FeedListItem() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0011"}, d2 = {"Lcom/gis/featurefeed/presentation/ui/feed/FeedListItem$FeedDefaultItem;", "Lcom/gis/featurefeed/presentation/ui/feed/FeedListItem;", "feedItem", "Lcom/gis/repository/domain/entity/FeedItem;", "(Lcom/gis/repository/domain/entity/FeedItem;)V", "getFeedItem", "()Lcom/gis/repository/domain/entity/FeedItem;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "featureFeed_prodDebug"})
    public static final class FeedDefaultItem extends com.gis.featurefeed.presentation.ui.feed.FeedListItem {
        @org.jetbrains.annotations.NotNull()
        private final com.gis.repository.domain.entity.FeedItem feedItem = null;
        
        @org.jetbrains.annotations.NotNull()
        public final com.gis.repository.domain.entity.FeedItem getFeedItem() {
            return null;
        }
        
        public FeedDefaultItem(@org.jetbrains.annotations.NotNull()
        com.gis.repository.domain.entity.FeedItem feedItem) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.gis.repository.domain.entity.FeedItem component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.gis.featurefeed.presentation.ui.feed.FeedListItem.FeedDefaultItem copy(@org.jetbrains.annotations.NotNull()
        com.gis.repository.domain.entity.FeedItem feedItem) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        @java.lang.Override()
        public java.lang.String toString() {
            return null;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object p0) {
            return false;
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/gis/featurefeed/presentation/ui/feed/FeedListItem$FeedLoadingItem;", "Lcom/gis/featurefeed/presentation/ui/feed/FeedListItem;", "()V", "featureFeed_prodDebug"})
    public static final class FeedLoadingItem extends com.gis.featurefeed.presentation.ui.feed.FeedListItem {
        public static final com.gis.featurefeed.presentation.ui.feed.FeedListItem.FeedLoadingItem INSTANCE = null;
        
        private FeedLoadingItem() {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/gis/featurefeed/presentation/ui/feed/FeedListItem$FeedEmptyPlaceholderItem;", "Lcom/gis/featurefeed/presentation/ui/feed/FeedListItem;", "()V", "featureFeed_prodDebug"})
    public static final class FeedEmptyPlaceholderItem extends com.gis.featurefeed.presentation.ui.feed.FeedListItem {
        public static final com.gis.featurefeed.presentation.ui.feed.FeedListItem.FeedEmptyPlaceholderItem INSTANCE = null;
        
        private FeedEmptyPlaceholderItem() {
            super();
        }
    }
}
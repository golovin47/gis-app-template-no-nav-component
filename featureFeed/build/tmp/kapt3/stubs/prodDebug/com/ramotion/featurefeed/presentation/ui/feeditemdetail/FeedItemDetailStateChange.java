package com.gis.featurefeed.presentation.ui.feeditemdetail;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0003\u0003\u0004\u0005B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u0082\u0001\u0003\u0006\u0007\b\u00a8\u0006\t"}, d2 = {"Lcom/gis/featurefeed/presentation/ui/feeditemdetail/FeedItemDetailStateChange;", "", "()V", "Error", "FeedItemReceived", "HideError", "Lcom/gis/featurefeed/presentation/ui/feeditemdetail/FeedItemDetailStateChange$FeedItemReceived;", "Lcom/gis/featurefeed/presentation/ui/feeditemdetail/FeedItemDetailStateChange$Error;", "Lcom/gis/featurefeed/presentation/ui/feeditemdetail/FeedItemDetailStateChange$HideError;", "featureFeed_prodDebug"})
public abstract class FeedItemDetailStateChange {
    
    private FeedItemDetailStateChange() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/gis/featurefeed/presentation/ui/feeditemdetail/FeedItemDetailStateChange$FeedItemReceived;", "Lcom/gis/featurefeed/presentation/ui/feeditemdetail/FeedItemDetailStateChange;", "feedItem", "Lcom/gis/repository/domain/entity/FeedItem;", "(Lcom/gis/repository/domain/entity/FeedItem;)V", "getFeedItem", "()Lcom/gis/repository/domain/entity/FeedItem;", "featureFeed_prodDebug"})
    public static final class FeedItemReceived extends com.gis.featurefeed.presentation.ui.feeditemdetail.FeedItemDetailStateChange {
        @org.jetbrains.annotations.NotNull()
        private final com.gis.repository.domain.entity.FeedItem feedItem = null;
        
        @org.jetbrains.annotations.NotNull()
        public final com.gis.repository.domain.entity.FeedItem getFeedItem() {
            return null;
        }
        
        public FeedItemReceived(@org.jetbrains.annotations.NotNull()
        com.gis.repository.domain.entity.FeedItem feedItem) {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/gis/featurefeed/presentation/ui/feeditemdetail/FeedItemDetailStateChange$Error;", "Lcom/gis/featurefeed/presentation/ui/feeditemdetail/FeedItemDetailStateChange;", "error", "", "(Ljava/lang/Throwable;)V", "getError", "()Ljava/lang/Throwable;", "featureFeed_prodDebug"})
    public static final class Error extends com.gis.featurefeed.presentation.ui.feeditemdetail.FeedItemDetailStateChange {
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
    
    @kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/gis/featurefeed/presentation/ui/feeditemdetail/FeedItemDetailStateChange$HideError;", "Lcom/gis/featurefeed/presentation/ui/feeditemdetail/FeedItemDetailStateChange;", "()V", "featureFeed_prodDebug"})
    public static final class HideError extends com.gis.featurefeed.presentation.ui.feeditemdetail.FeedItemDetailStateChange {
        public static final com.gis.featurefeed.presentation.ui.feeditemdetail.FeedItemDetailStateChange.HideError INSTANCE = null;
        
        private HideError() {
            super();
        }
    }
}
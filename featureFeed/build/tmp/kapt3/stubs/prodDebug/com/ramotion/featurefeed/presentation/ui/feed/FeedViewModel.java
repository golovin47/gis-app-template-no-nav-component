package com.gis.featurefeed.presentation.ui.feed;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0002\b\u0005\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001BO\u00128\u0010\u0003\u001a4\u0012\u0013\u0012\u00110\u0005\u00a2\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\b\u0012\u0013\u0012\u00110\t\u00a2\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\u0004\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u00a2\u0006\u0002\u0010\u0010J\u001c\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u00122\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012H\u0002J4\u0010\u0015\u001a&\u0012\f\u0012\n \u0018*\u0004\u0018\u00010\u00170\u0017 \u0018*\u0012\u0012\f\u0012\n \u0018*\u0004\u0018\u00010\u00170\u0017\u0018\u00010\u00160\u00162\u0006\u0010\u0019\u001a\u00020\u001aH\u0002J\b\u0010\u001b\u001a\u00020\u0002H\u0014J\b\u0010\u001c\u001a\u00020\u000bH\u0014J\u0018\u0010\u001d\u001a\u00020\u00022\u0006\u0010\u001e\u001a\u00020\u00022\u0006\u0010\u001f\u001a\u00020 H\u0014J\u001c\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00130\u00122\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012H\u0002J\u001a\u0010\"\u001a\b\u0012\u0004\u0012\u00020 0\u00162\n\u0010#\u001a\u0006\u0012\u0002\b\u00030\u0016H\u0014J\f\u0010$\u001a\u00020%*\u00020&H\u0002R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R@\u0010\u0003\u001a4\u0012\u0013\u0012\u00110\u0005\u00a2\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\b\u0012\u0013\u0012\u00110\t\u00a2\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\'"}, d2 = {"Lcom/gis/featurefeed/presentation/ui/feed/FeedViewModel;", "Lcom/gis/utils/presentation/BaseViewModel;", "Lcom/gis/featurefeed/presentation/ui/feed/FeedState;", "openDetail", "Lkotlin/Function2;", "", "Lkotlin/ParameterName;", "name", "itemId", "Landroid/view/View;", "view", "", "observeFeedUseCase", "Lcom/gis/repository/domain/interactors/ObserveFeedUseCase;", "loadNextFeedPageUseCase", "Lcom/gis/repository/domain/interactors/LoadNextFeedPageUseCase;", "(Lkotlin/jvm/functions/Function2;Lcom/gis/repository/domain/interactors/ObserveFeedUseCase;Lcom/gis/repository/domain/interactors/LoadNextFeedPageUseCase;)V", "finishNextPageLoading", "", "Lcom/gis/featurefeed/presentation/ui/feed/FeedListItem;", "prevList", "handleError", "Lio/reactivex/Observable;", "Lcom/gis/featurefeed/presentation/ui/feed/FeedStateChange;", "kotlin.jvm.PlatformType", "e", "", "initState", "onCleared", "reduceState", "previousState", "stateChange", "", "startNextPageLoading", "viewIntents", "intentStream", "toPresentation", "Lcom/gis/featurefeed/presentation/ui/feed/FeedListItem$FeedDefaultItem;", "Lcom/gis/repository/domain/entity/FeedItem;", "featureFeed_prodDebug"})
public final class FeedViewModel extends com.gis.utils.presentation.BaseViewModel<com.gis.featurefeed.presentation.ui.feed.FeedState> {
    private kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super android.view.View, kotlin.Unit> openDetail;
    private final com.gis.repository.domain.interactors.ObserveFeedUseCase observeFeedUseCase = null;
    private final com.gis.repository.domain.interactors.LoadNextFeedPageUseCase loadNextFeedPageUseCase = null;
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    protected com.gis.featurefeed.presentation.ui.feed.FeedState initState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    protected io.reactivex.Observable<java.lang.Object> viewIntents(@org.jetbrains.annotations.NotNull()
    io.reactivex.Observable<?> intentStream) {
        return null;
    }
    
    private final io.reactivex.Observable<com.gis.featurefeed.presentation.ui.feed.FeedStateChange> handleError(java.lang.Throwable e) {
        return null;
    }
    
    private final java.util.List<com.gis.featurefeed.presentation.ui.feed.FeedListItem> startNextPageLoading(java.util.List<? extends com.gis.featurefeed.presentation.ui.feed.FeedListItem> prevList) {
        return null;
    }
    
    private final java.util.List<com.gis.featurefeed.presentation.ui.feed.FeedListItem> finishNextPageLoading(java.util.List<? extends com.gis.featurefeed.presentation.ui.feed.FeedListItem> prevList) {
        return null;
    }
    
    private final com.gis.featurefeed.presentation.ui.feed.FeedListItem.FeedDefaultItem toPresentation(@org.jetbrains.annotations.NotNull()
    com.gis.repository.domain.entity.FeedItem $this$toPresentation) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    protected com.gis.featurefeed.presentation.ui.feed.FeedState reduceState(@org.jetbrains.annotations.NotNull()
    com.gis.featurefeed.presentation.ui.feed.FeedState previousState, @org.jetbrains.annotations.NotNull()
    java.lang.Object stateChange) {
        return null;
    }
    
    @java.lang.Override()
    protected void onCleared() {
    }
    
    public FeedViewModel(@org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super android.view.View, kotlin.Unit> openDetail, @org.jetbrains.annotations.NotNull()
    com.gis.repository.domain.interactors.ObserveFeedUseCase observeFeedUseCase, @org.jetbrains.annotations.NotNull()
    com.gis.repository.domain.interactors.LoadNextFeedPageUseCase loadNextFeedPageUseCase) {
        super();
    }
}
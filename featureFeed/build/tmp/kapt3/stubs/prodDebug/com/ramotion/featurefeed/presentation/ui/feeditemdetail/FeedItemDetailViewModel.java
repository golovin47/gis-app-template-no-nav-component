package com.gis.featurefeed.presentation.ui.feeditemdetail;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0002\b\u0005\n\u0002\u0010\u0000\n\u0002\b\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u001d\u0012\u000e\u0010\u0003\u001a\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u0004\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ4\u0010\t\u001a&\u0012\f\u0012\n \f*\u0004\u0018\u00010\u000b0\u000b \f*\u0012\u0012\f\u0012\n \f*\u0004\u0018\u00010\u000b0\u000b\u0018\u00010\n0\n2\u0006\u0010\r\u001a\u00020\u000eH\u0002J\b\u0010\u000f\u001a\u00020\u0002H\u0014J\b\u0010\u0010\u001a\u00020\u0005H\u0014J\u0018\u0010\u0011\u001a\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u0014H\u0014J\u001a\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00140\n2\n\u0010\u0016\u001a\u0006\u0012\u0002\b\u00030\nH\u0014R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0003\u001a\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/gis/featurefeed/presentation/ui/feeditemdetail/FeedItemDetailViewModel;", "Lcom/gis/utils/presentation/BaseViewModel;", "Lcom/gis/featurefeed/presentation/ui/feeditemdetail/FeedItemDetailState;", "goBack", "Lkotlin/Function0;", "", "getFeedItemUseCase", "Lcom/gis/repository/domain/interactors/GetFeedItemUseCase;", "(Lkotlin/jvm/functions/Function0;Lcom/gis/repository/domain/interactors/GetFeedItemUseCase;)V", "handleError", "Lio/reactivex/Observable;", "Lcom/gis/featurefeed/presentation/ui/feeditemdetail/FeedItemDetailStateChange;", "kotlin.jvm.PlatformType", "e", "", "initState", "onCleared", "reduceState", "previousState", "stateChange", "", "viewIntents", "intentStream", "featureFeed_prodDebug"})
public final class FeedItemDetailViewModel extends com.gis.utils.presentation.BaseViewModel<com.gis.featurefeed.presentation.ui.feeditemdetail.FeedItemDetailState> {
    private kotlin.jvm.functions.Function0<kotlin.Unit> goBack;
    private final com.gis.repository.domain.interactors.GetFeedItemUseCase getFeedItemUseCase = null;
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    protected com.gis.featurefeed.presentation.ui.feeditemdetail.FeedItemDetailState initState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    protected io.reactivex.Observable<java.lang.Object> viewIntents(@org.jetbrains.annotations.NotNull()
    io.reactivex.Observable<?> intentStream) {
        return null;
    }
    
    private final io.reactivex.Observable<com.gis.featurefeed.presentation.ui.feeditemdetail.FeedItemDetailStateChange> handleError(java.lang.Throwable e) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    protected com.gis.featurefeed.presentation.ui.feeditemdetail.FeedItemDetailState reduceState(@org.jetbrains.annotations.NotNull()
    com.gis.featurefeed.presentation.ui.feeditemdetail.FeedItemDetailState previousState, @org.jetbrains.annotations.NotNull()
    java.lang.Object stateChange) {
        return null;
    }
    
    @java.lang.Override()
    protected void onCleared() {
    }
    
    public FeedItemDetailViewModel(@org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> goBack, @org.jetbrains.annotations.NotNull()
    com.gis.repository.domain.interactors.GetFeedItemUseCase getFeedItemUseCase) {
        super();
    }
}
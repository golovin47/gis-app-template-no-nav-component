package com.gis.featurefeed.presentation.ui.feed;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J2\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00060\u000fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/gis/featurefeed/presentation/ui/feed/FeedItemVH;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "binding", "Lcom/gis/featurefeed/databinding/ItemFeedBinding;", "(Lcom/gis/featurefeed/databinding/ItemFeedBinding;)V", "bind", "", "item", "Lcom/gis/repository/domain/entity/FeedItem;", "imageLoader", "Lcom/gis/utils/domain/ImageLoader;", "intentsPublisher", "Lio/reactivex/subjects/PublishSubject;", "Lcom/gis/featurefeed/presentation/ui/feed/FeedIntent;", "startTransition", "Lkotlin/Function0;", "featureFeed_prodDebug"})
public final class FeedItemVH extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
    private final com.gis.featurefeed.databinding.ItemFeedBinding binding = null;
    
    public final void bind(@org.jetbrains.annotations.NotNull()
    com.gis.repository.domain.entity.FeedItem item, @org.jetbrains.annotations.NotNull()
    com.gis.utils.domain.ImageLoader imageLoader, @org.jetbrains.annotations.NotNull()
    io.reactivex.subjects.PublishSubject<com.gis.featurefeed.presentation.ui.feed.FeedIntent> intentsPublisher, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> startTransition) {
    }
    
    public FeedItemVH(@org.jetbrains.annotations.NotNull()
    com.gis.featurefeed.databinding.ItemFeedBinding binding) {
        super(null);
    }
}
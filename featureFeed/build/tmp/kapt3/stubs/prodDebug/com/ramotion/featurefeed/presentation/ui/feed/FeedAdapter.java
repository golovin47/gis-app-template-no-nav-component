package com.gis.featurefeed.presentation.ui.feed;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001B)\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n\u00a2\u0006\u0002\u0010\fJ\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000eH\u0016J\u0018\u0010\u0010\u001a\u00020\u000b2\u0006\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u000eH\u0016J\u0018\u0010\u0012\u001a\u00020\u00032\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u000eH\u0016R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2 = {"Lcom/gis/featurefeed/presentation/ui/feed/FeedAdapter;", "Landroidx/recyclerview/widget/ListAdapter;", "Lcom/gis/featurefeed/presentation/ui/feed/FeedListItem;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "intentsPublisher", "Lio/reactivex/subjects/PublishSubject;", "Lcom/gis/featurefeed/presentation/ui/feed/FeedIntent;", "imageLoader", "Lcom/gis/utils/domain/ImageLoader;", "startTransition", "Lkotlin/Function0;", "", "(Lio/reactivex/subjects/PublishSubject;Lcom/gis/utils/domain/ImageLoader;Lkotlin/jvm/functions/Function0;)V", "getItemViewType", "", "position", "onBindViewHolder", "holder", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "featureFeed_prodDebug"})
public final class FeedAdapter extends androidx.recyclerview.widget.ListAdapter<com.gis.featurefeed.presentation.ui.feed.FeedListItem, androidx.recyclerview.widget.RecyclerView.ViewHolder> {
    private final io.reactivex.subjects.PublishSubject<com.gis.featurefeed.presentation.ui.feed.FeedIntent> intentsPublisher = null;
    private final com.gis.utils.domain.ImageLoader imageLoader = null;
    private kotlin.jvm.functions.Function0<kotlin.Unit> startTransition;
    
    @java.lang.Override()
    public int getItemViewType(int position) {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public androidx.recyclerview.widget.RecyclerView.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    androidx.recyclerview.widget.RecyclerView.ViewHolder holder, int position) {
    }
    
    public FeedAdapter(@org.jetbrains.annotations.NotNull()
    io.reactivex.subjects.PublishSubject<com.gis.featurefeed.presentation.ui.feed.FeedIntent> intentsPublisher, @org.jetbrains.annotations.NotNull()
    com.gis.utils.domain.ImageLoader imageLoader, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> startTransition) {
        super(null);
    }
}
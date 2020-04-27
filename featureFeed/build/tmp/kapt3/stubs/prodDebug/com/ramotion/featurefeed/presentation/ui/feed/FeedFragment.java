package com.gis.featurefeed.presentation.ui.feed;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u00012\b\u0012\u0004\u0012\u00020\u00030\u0002B\u0005\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u001a\u001a\u00020\u001bH\u0016J\u001a\u0010\u001c\u001a\u00020\u001b2\u0006\u0010\u001d\u001a\u00020\u001e2\b\u0010\u001f\u001a\u0004\u0018\u00010 H\u0002J\b\u0010!\u001a\u00020\u001bH\u0016J\b\u0010\"\u001a\u00020\u001bH\u0002J\u0012\u0010#\u001a\u00020\u001b2\b\u0010$\u001a\u0004\u0018\u00010%H\u0016J&\u0010&\u001a\u0004\u0018\u00010\'2\u0006\u0010\u001d\u001a\u00020\u001e2\b\u0010\u001f\u001a\u0004\u0018\u00010 2\b\u0010$\u001a\u0004\u0018\u00010%H\u0016J\b\u0010(\u001a\u00020\u001bH\u0016J\u0010\u0010)\u001a\u00020\u001b2\u0006\u0010*\u001a\u00020\u0003H\u0016R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001b\u0010\t\u001a\u00020\n8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\r\u0010\u000e\u001a\u0004\b\u000b\u0010\fR\u001c\u0010\u000f\u001a\u0010\u0012\f\u0012\n \u0012*\u0004\u0018\u00010\u00110\u00110\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0015\u001a\u00020\u00168BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0019\u0010\u000e\u001a\u0004\b\u0017\u0010\u0018\u00a8\u0006+"}, d2 = {"Lcom/gis/featurefeed/presentation/ui/feed/FeedFragment;", "Landroidx/fragment/app/Fragment;", "Lcom/gis/utils/presentation/BaseView;", "Lcom/gis/featurefeed/presentation/ui/feed/FeedState;", "()V", "binding", "Lcom/gis/featurefeed/databinding/FragmentFeedBinding;", "feedAdapter", "Lcom/gis/featurefeed/presentation/ui/feed/FeedAdapter;", "imageLoader", "Lcom/gis/utils/domain/ImageLoader;", "getImageLoader", "()Lcom/gis/utils/domain/ImageLoader;", "imageLoader$delegate", "Lkotlin/Lazy;", "intentsPublisher", "Lio/reactivex/subjects/PublishSubject;", "Lcom/gis/featurefeed/presentation/ui/feed/FeedIntent;", "kotlin.jvm.PlatformType", "intentsSubscription", "Lio/reactivex/disposables/Disposable;", "viewModel", "Lcom/gis/featurefeed/presentation/ui/feed/FeedViewModel;", "getViewModel", "()Lcom/gis/featurefeed/presentation/ui/feed/FeedViewModel;", "viewModel$delegate", "handleStates", "", "initBinding", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "initIntents", "initRvFeed", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "Landroid/view/View;", "onDestroyView", "render", "state", "featureFeed_prodDebug"})
public final class FeedFragment extends androidx.fragment.app.Fragment implements com.gis.utils.presentation.BaseView<com.gis.featurefeed.presentation.ui.feed.FeedState> {
    private com.gis.featurefeed.databinding.FragmentFeedBinding binding;
    private final io.reactivex.subjects.PublishSubject<com.gis.featurefeed.presentation.ui.feed.FeedIntent> intentsPublisher = null;
    private io.reactivex.disposables.Disposable intentsSubscription;
    private final kotlin.Lazy viewModel$delegate = null;
    private final kotlin.Lazy imageLoader$delegate = null;
    private final com.gis.featurefeed.presentation.ui.feed.FeedAdapter feedAdapter = null;
    private java.util.HashMap _$_findViewCache;
    
    private final com.gis.featurefeed.presentation.ui.feed.FeedViewModel getViewModel() {
        return null;
    }
    
    private final com.gis.utils.domain.ImageLoader getImageLoader() {
        return null;
    }
    
    @java.lang.Override()
    public void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
    
    private final void initBinding(android.view.LayoutInflater inflater, android.view.ViewGroup container) {
    }
    
    private final void initRvFeed() {
    }
    
    @java.lang.Override()
    public void initIntents() {
    }
    
    @java.lang.Override()
    public void handleStates() {
    }
    
    @java.lang.Override()
    public void render(@org.jetbrains.annotations.NotNull()
    com.gis.featurefeed.presentation.ui.feed.FeedState state) {
    }
    
    public FeedFragment() {
        super();
    }
}
package com.gis.featurefeed.presentation.ui.feeditemdetail;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u0000 )2\u00020\u00012\b\u0012\u0004\u0012\u00020\u00030\u0002:\u0001)B\u0005\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0019\u001a\u00020\u001aH\u0016J\u001a\u0010\u001b\u001a\u00020\u001a2\u0006\u0010\u001c\u001a\u00020\u001d2\b\u0010\u001e\u001a\u0004\u0018\u00010\u001fH\u0002J\b\u0010 \u001a\u00020\u001aH\u0016J\u0012\u0010!\u001a\u00020\u001a2\b\u0010\"\u001a\u0004\u0018\u00010#H\u0016J&\u0010$\u001a\u0004\u0018\u00010%2\u0006\u0010\u001c\u001a\u00020\u001d2\b\u0010\u001e\u001a\u0004\u0018\u00010\u001f2\b\u0010\"\u001a\u0004\u0018\u00010#H\u0016J\b\u0010&\u001a\u00020\u001aH\u0016J\u0010\u0010\'\u001a\u00020\u001a2\u0006\u0010(\u001a\u00020\u0003H\u0016R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0007\u001a\u00020\b8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u000b\u0010\f\u001a\u0004\b\t\u0010\nR\u000e\u0010\r\u001a\u00020\u000eX\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u000f\u001a\u00020\u00108BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0013\u0010\f\u001a\u0004\b\u0011\u0010\u0012R\u001b\u0010\u0014\u001a\u00020\u00158BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0018\u0010\f\u001a\u0004\b\u0016\u0010\u0017\u00a8\u0006*"}, d2 = {"Lcom/gis/featurefeed/presentation/ui/feeditemdetail/FeedItemDetailFragment;", "Landroidx/fragment/app/Fragment;", "Lcom/gis/utils/presentation/BaseView;", "Lcom/gis/featurefeed/presentation/ui/feeditemdetail/FeedItemDetailState;", "()V", "binding", "Lcom/gis/featurefeed/databinding/FragmentFeedDetailBinding;", "imageLoader", "Lcom/gis/utils/domain/ImageLoader;", "getImageLoader", "()Lcom/gis/utils/domain/ImageLoader;", "imageLoader$delegate", "Lkotlin/Lazy;", "intentsSubscription", "Lio/reactivex/disposables/Disposable;", "itemId", "", "getItemId", "()I", "itemId$delegate", "viewModel", "Lcom/gis/featurefeed/presentation/ui/feeditemdetail/FeedItemDetailViewModel;", "getViewModel", "()Lcom/gis/featurefeed/presentation/ui/feeditemdetail/FeedItemDetailViewModel;", "viewModel$delegate", "handleStates", "", "initBinding", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "initIntents", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "Landroid/view/View;", "onDestroyView", "render", "state", "Companion", "featureFeed_prodDebug"})
public final class FeedItemDetailFragment extends androidx.fragment.app.Fragment implements com.gis.utils.presentation.BaseView<com.gis.featurefeed.presentation.ui.feeditemdetail.FeedItemDetailState> {
    private final kotlin.Lazy itemId$delegate = null;
    private com.gis.featurefeed.databinding.FragmentFeedDetailBinding binding;
    private io.reactivex.disposables.Disposable intentsSubscription;
    private final kotlin.Lazy viewModel$delegate = null;
    private final kotlin.Lazy imageLoader$delegate = null;
    public static final com.gis.featurefeed.presentation.ui.feeditemdetail.FeedItemDetailFragment.Companion Companion = null;
    private java.util.HashMap _$_findViewCache;
    
    private final int getItemId() {
        return 0;
    }
    
    private final com.gis.featurefeed.presentation.ui.feeditemdetail.FeedItemDetailViewModel getViewModel() {
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
    
    @java.lang.Override()
    public void initIntents() {
    }
    
    @java.lang.Override()
    public void handleStates() {
    }
    
    @java.lang.Override()
    public void render(@org.jetbrains.annotations.NotNull()
    com.gis.featurefeed.presentation.ui.feeditemdetail.FeedItemDetailState state) {
    }
    
    public FeedItemDetailFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/gis/featurefeed/presentation/ui/feeditemdetail/FeedItemDetailFragment$Companion;", "", "()V", "getInstance", "Lcom/gis/featurefeed/presentation/ui/feeditemdetail/FeedItemDetailFragment;", "itemId", "", "featureFeed_prodDebug"})
    public static final class Companion {
        
        @org.jetbrains.annotations.NotNull()
        public final com.gis.featurefeed.presentation.ui.feeditemdetail.FeedItemDetailFragment getInstance(int itemId) {
            return null;
        }
        
        private Companion() {
            super();
        }
    }
}
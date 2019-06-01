package com.feedapp.dagger;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.feedapp.annotations.ViewModelKey;
import com.feedapp.baseComponent.BaseViewModelFactory;
import com.feedapp.viewModels.FeedDetailsViewModel;
import com.feedapp.viewModels.FeedViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
abstract  class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(FeedViewModel.class)
    abstract ViewModel feedViewModel(FeedViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(FeedDetailsViewModel.class)
    abstract ViewModel feedDetailsViewModel(FeedDetailsViewModel viewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(BaseViewModelFactory viewModelFactory);
}

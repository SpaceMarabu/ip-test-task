package com.example.ip_test_task.di

import androidx.lifecycle.ViewModel
import com.example.ip_test_task.presentation.main.MainViewUDFModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @IntoMap
    @ViewModelKey(MainViewUDFModel::class)
    @Binds
    fun bindMainViewModel(viewModel: MainViewUDFModel): ViewModel

}

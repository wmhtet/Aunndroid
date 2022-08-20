package aunn.gg.rest.di

import androidx.lifecycle.ViewModel
import aunn.gg.rest.viewmodels.PostListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PostListModule {

    @Binds
    @IntoMap
    @ViewModelKey(PostListViewModel::class)
    abstract fun bindViewModel(viewmodel: PostListViewModel): ViewModel
}
package aunn.gg.rest.di

import aunn.gg.rest.MainActivity
import dagger.Subcomponent

@Subcomponent(modules = [PostListModule::class])
interface PostListComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): PostListComponent
    }

    fun inject(activity: MainActivity)
}
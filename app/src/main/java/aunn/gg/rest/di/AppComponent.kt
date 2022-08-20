package aunn.gg.rest.di

import android.content.Context
import aunn.gg.rest.repository.PostListRepository
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import javax.inject.Singleton

/**
 * Main component for the application.
 *
 */
@Singleton
@Component(
    modules = [
        AppModule::class,
        ViewModelBuilderModule::class,
        SubcomponentsModule::class
    ]
)

interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): AppComponent
    }

    fun postListComponent(): PostListComponent.Factory

    val postListRepository: PostListRepository
}

@Module(
    subcomponents = [
        PostListComponent::class
    ]
)
object SubcomponentsModule
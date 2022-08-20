package aunn.gg.rest.di

import android.content.Context
import androidx.room.Room
import aunn.gg.rest.database.PostListDatabase
import aunn.gg.rest.network.NetworkCall
import aunn.gg.rest.network.RestClient
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
object AppModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class PostListLocal

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class PostListRemote

    @Singleton
    @PostListLocal
    @Provides
    fun providePostListLocal(context: Context): PostListDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            PostListDatabase::class.java,
            "postlist"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @PostListRemote
    @Provides
    fun providePostListRemote(): RestClient {
        return NetworkCall()
    }
}
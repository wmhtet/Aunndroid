package aunn.gg.rest

import android.app.Application
import aunn.gg.rest.di.AppComponent
import aunn.gg.rest.di.DaggerAppComponent
import rest.BuildConfig
import timber.log.Timber
import timber.log.Timber.DebugTree

open class MyApplication : Application() {

    // Instance of the AppComponent that will be used by all the Activities in the project
    val appComponent: AppComponent by lazy {
        initializeComponent()
    }

    open fun initializeComponent(): AppComponent {
        // Creates an instance of AppComponent using its Factory constructor
        // We pass the applicationContext that will be used as Context in the graph
        return DaggerAppComponent.factory().create(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(
            object : DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String {
                    return "Timber:${super.createStackElementTag(element)}:${element.lineNumber}"
                }
            }
        )
    }
}
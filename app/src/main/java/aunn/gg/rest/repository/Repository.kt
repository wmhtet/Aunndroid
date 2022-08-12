package aunn.gg.rest.repository

import android.app.Application
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.*

abstract class Repository(application: Application) {
    private val TAG: String? = Repository::class.simpleName

    @OptIn(DelicateCoroutinesApi::class)
    var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(TAG, "Error : $throwable")
        GlobalScope.launch(Dispatchers.Main) {
            Toast.makeText(
                application, "$TAG Error : $throwable",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
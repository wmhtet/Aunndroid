package aunn.gg.rest.repository

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.*
import timber.log.Timber

abstract class Repository(context: Context) {
    private val TAG: String? = Repository::class.simpleName

    @OptIn(DelicateCoroutinesApi::class)
    var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e("Error : $throwable")
        GlobalScope.launch(Dispatchers.Main) {
            Toast.makeText(
                context, "$TAG Error : $throwable",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
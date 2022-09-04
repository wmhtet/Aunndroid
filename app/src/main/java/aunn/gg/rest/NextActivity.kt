package aunn.gg.rest

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import aunn.gg.rest.ui.theme.AunndroidTheme
import aunn.gg.rest.ui.composable.CommentListViewContent
import aunn.gg.rest.viewmodels.CommentListViewModel
import timber.log.Timber

class NextActivity : ComponentActivity() {
    private lateinit var viewModel: CommentListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = intent.getStringExtra("postID")!!
        Timber.d("onCreate : $id")
        if (id.isNotEmpty()) {
            viewModel = ViewModelProvider(
                this, CommentListViewModel.Factory(applicationContext, id)
            )[CommentListViewModel::class.java]
        } else {
            Timber.e("No post_id")
            finishActivity(0)
        }

        setContent {
            AunndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CommentListViewContent(viewModel)
                }
            }
        }
        viewModel.eventNetworkError.observe(this) { isNetworkError ->
            if (isNetworkError) onNetworkError()
        }
    }

    private fun onNetworkError() {
        if (!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(
                this,
                "${NextActivity::class.simpleName} Network Error",
                Toast.LENGTH_LONG
            ).show()
            viewModel.onNetworkErrorShown()
        }
    }
}
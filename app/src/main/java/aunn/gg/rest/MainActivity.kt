package aunn.gg.rest

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import aunn.gg.rest.ui.theme.AunndroidTheme
import aunn.gg.rest.ui.composable.PostListViewContent
import aunn.gg.rest.viewmodels.PostListViewModel
import javax.inject.Inject

class MainActivity : ComponentActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<PostListViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).appComponent.postListComponent().create().inject(this)
        setContent {
            AunndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PostListViewContent(viewModel, launchNextActivity)
                }
            }
        }
        viewModel.eventNetworkError.observe(this) { isNetworkError ->
            if (isNetworkError) onNetworkError()
        }
    }


    private val launchNextActivity: (String) -> Unit = { id ->
        val intent = Intent(this@MainActivity, NextActivity::class.java)
        intent.putExtra("postID", id)
        this.startActivity(intent)
    }

    private fun onNetworkError() {
        if (!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(
                this,
                "${MainActivity::class.simpleName} Network Error",
                Toast.LENGTH_LONG
            ).show()
            viewModel.onNetworkErrorShown()
        }
    }
}

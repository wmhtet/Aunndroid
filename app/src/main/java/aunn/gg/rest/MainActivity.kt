package aunn.gg.rest

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import aunn.gg.rest.domain.Post
import aunn.gg.rest.viewmodels.PostListViewModel
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.simpleName
    private lateinit var lv: ListView
    private lateinit var viewModelAdapter: ArrayAdapter<Post>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<PostListViewModel> { viewModelFactory }

    /*
    private val viewModel: PostListViewModel by lazy {
        requireNotNull(this) {}
        ViewModelProvider(
            this,
            PostListViewModel.Factory(application)
        )[PostListViewModel::class.java]
    }
*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).appComponent.postListComponent().create().inject(this)

        lv = ListView(this)
        setContentView(lv)
        lv.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this@MainActivity, NextActivity::class.java)
            intent.putExtra("postID", viewModelAdapter.getItem(position)?.id)
            this.startActivity(intent)
        }
        viewModelAdapter =
            ArrayAdapter<Post>(this, android.R.layout.simple_list_item_1, ArrayList<Post>())
        lv.adapter = viewModelAdapter
        viewModel.postList.observe(this) { postList ->
            postList?.apply {
                Timber.d("Update post list")
                viewModelAdapter.clear()
                viewModelAdapter.addAll(postList)
            }
        }
        viewModel.eventNetworkError.observe(this) { isNetworkError ->
            if (isNetworkError) onNetworkError()
        }
    }

    private fun onNetworkError() {
        if (!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(this, "$TAG Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }
}

package aunn.gg.rest

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import aunn.gg.rest.domain.Comment
import aunn.gg.rest.viewmodels.CommentListViewModel

class NextActivity : AppCompatActivity() {
    private lateinit var lv: ListView
    private lateinit var viewModelAdapter: ArrayAdapter<Comment>
    private lateinit var viewModel: CommentListViewModel
    private val TAG: String? = NextActivity::class.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lv = ListView(this)
        val id = intent.getStringExtra("postID")
        Log.e(TAG, "onCreate : $id")
        if (!id.isNullOrEmpty()) {
            viewModel = ViewModelProvider(
                this, CommentListViewModel.Factory( application, id)
            )[CommentListViewModel::class.java]
        }else{
            Log.e(TAG, "No post_id")
            finishActivity(0)
        }
        setContentView(lv)

        viewModelAdapter = ArrayAdapter<Comment>(
            this, android.R.layout.simple_list_item_1, ArrayList<Comment>()
        )
        lv.adapter = viewModelAdapter
        viewModel.commentList.observe(this) { commentList ->
            commentList?.apply {
                Log.d(TAG, "Update comment list")
                viewModelAdapter.clear()
                viewModelAdapter.addAll(commentList)
            }
        }
        viewModel.eventNetworkError.observe(this) { isNetworkError ->
            if (isNetworkError) onNetworkError()
        }
    }

    private fun onNetworkError() {
        if(!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(this, "$TAG Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }
}
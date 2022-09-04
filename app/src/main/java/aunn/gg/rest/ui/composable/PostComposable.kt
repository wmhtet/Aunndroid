package aunn.gg.rest.ui.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import aunn.gg.rest.domain.Post
import aunn.gg.rest.ui.theme.AunndroidTheme
import aunn.gg.rest.viewmodels.PostListViewModel
import rest.R
import timber.log.Timber


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostListViewContent(
    postListViewModel: PostListViewModel,
    launchNextActivity: (String) -> Unit
) {
    val postlist by postListViewModel.postList.observeAsState()
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        stickyHeader {
            Header()
        }
        postlist?.let { ps ->
            Timber.d("Update post list")
            this.itemsIndexed(ps) { _, item ->
                PostItemView(item, launchNextActivity)
            }
            item {
                Footer()
            }
        }
    }
}


@Composable
fun PostItemView(post: Post, launchNextActivity: (String) -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(corner = CornerSize(16.dp))
    ) {
        PostDataItemView(post.title, post.body, post.id, post.userId, launchNextActivity)
        //if (index < size) Divider(color = Color.LightGray, thickness = 1.dp)
    }
}

@Composable
fun PostDataItemView(
    title: String,
    body: String,
    id: String,
    userId: String,
    launchNextActivity: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = title,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = body,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            style = MaterialTheme.typography.bodyMedium
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = stringResource(id = R.string.postId, id))
            Text(text = stringResource(id = R.string.userId, userId))
            Button(
                onClick = { launchNextActivity(id) }) {
                Text(text = stringResource(id = R.string.comment))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PostItemPreview() {
    AunndroidTheme {
        PostDataItemView("Aunndroid", "Wjkjhjkh JKHJKHHHK KJKKJJJHJ KJKJKJKJ KKJJJJk ", "1", "3") {}
    }
}
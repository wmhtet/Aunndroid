package aunn.gg.rest.ui.composable

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import aunn.gg.rest.domain.Comment
import aunn.gg.rest.ui.theme.AunndroidTheme
import aunn.gg.rest.viewmodels.CommentListViewModel
import rest.R
import timber.log.Timber


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CommentListViewContent(postListViewModel: CommentListViewModel) {
    val commentList by postListViewModel.commentList.observeAsState()
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        stickyHeader {
            Header()
        }
        commentList?.let { cs ->
            Timber.d("Update comment list")
            this.itemsIndexed(cs) { _, comment ->
                CommentItemView(comment)
            }
            item {
                Footer()
            }
        }

    }
}


@Composable
fun CommentItemView(comment: Comment) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(corner = CornerSize(16.dp))
    ) {
        CommentDataItemView(comment.id, comment.postId, comment.body, comment.name, comment.email)
    }
}

@Composable
fun CommentDataItemView(id: String, postId: String, body: String, name: String, email: String) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.name_is, name),
                modifier = Modifier.fillMaxWidth(0.5f),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = stringResource(id = R.string.comment_id, id),
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Text(
            text = body,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            style = MaterialTheme.typography.bodyMedium
        )
        Button(onClick = {
            context.sendMail(
                to = email,
                subject = context.getString(R.string.regarding_subject, id, postId)
            )
        }) {
            Text(
                text = stringResource(id = R.string.send_email, email),
                textAlign = TextAlign.Center
            )
        }
    }
}

fun Context.sendMail(to: String, subject: String) {
    try {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "vnd.android.cursor.item/email"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Timber.e(e)
    } catch (t: Throwable) {
        Timber.e(t)
    }
}

@Preview(showBackground = true)
@Composable
fun CommentItemPreview() {
    AunndroidTheme {
        CommentDataItemView(
            "1",
            "3",
            "Wjkjhjkh JKHJKHHHK KJKKJJJHJ KJKJKJKJ KKJJJJk ",
            "Aunndroid",
            "test@aunndroid.com"
        )
    }
}
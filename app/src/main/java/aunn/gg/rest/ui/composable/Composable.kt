package aunn.gg.rest.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import rest.R

@Composable
fun Header() {
    Text(
        text = stringResource(id = R.string.app_name),
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(bottom = 12.dp),
        color = MaterialTheme.colorScheme.inversePrimary,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleLarge
    )
}

@Composable
fun Footer() {
    Text(
        text = stringResource(id = R.string.end_of_contents),
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary),
        color = MaterialTheme.colorScheme.inversePrimary,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodySmall
    )
}

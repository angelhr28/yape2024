package com.angelhr28.yapechallenge2024.ui.components

import android.content.res.Configuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.angelhr28.domain.util.BadRequestException
import com.angelhr28.domain.util.GenericException
import com.angelhr28.domain.util.NetworkException
import com.angelhr28.yapechallenge2024.R
import java.net.UnknownHostException

@Composable
fun ErrorComponent(error: Throwable, onDismiss: () -> Unit) {
    val defaultMessage = stringResource(R.string.generic_error_message)
    val message = when (error) {
        is GenericException, is BadRequestException ->
            error.message ?: defaultMessage
        is NetworkException, is UnknownHostException ->
            stringResource(R.string.network_error_message)
        else -> defaultMessage
    }

    AlertDialog(
        icon = {
            Icon(
                imageVector = Icons.Rounded.Error,
                contentDescription = stringResource(R.string.error_icon_description)
            )
        },
        title = { Text(text = stringResource(R.string.error)) },
        text = { Text(text = message, style = MaterialTheme.typography.bodyLarge) },
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(stringResource(R.string.accept))
            }
        }
    )
}

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
    BaseScreen {
        ErrorComponent(GenericException()) {}
    }
}

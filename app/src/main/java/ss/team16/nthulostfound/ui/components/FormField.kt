package ss.team16.nthulostfound.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FormField(
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    singleLine: Boolean = true,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = labelText
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = singleLine,
            modifier = modifier
                .fillMaxWidth()
        )
    }
}
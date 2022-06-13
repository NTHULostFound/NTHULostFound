package ss.team16.nthulostfound.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ss.team16.nthulostfound.ui.theme.NTHULostFoundTheme

@Composable
fun InfoBox(
    modifier: Modifier = Modifier,
    info: String = ""
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = "info",
            modifier = Modifier.padding(bottom = 2.dp)
        )
        Text(
            text = info,
            style = MaterialTheme.typography.body2,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun InfoBoxPreview() {
    NTHULostFoundTheme {
        InfoBox(
            info = "你知道嗎" + "\n" +
                "這是一段有點長的說明文字，星爆氣流斬可以打出十秒十六下，是刀劍神域這款遊戲中技能組二刀流中的其中一個技能，" +
                "只有伺服器中反應速度最快的人才能獲得這個技能",
            modifier = Modifier.padding(12.dp)
        )
    }
}
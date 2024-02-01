package jp.co.abs.paintkamada.ui.theme

import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import jp.co.abs.paintkamada.CustomDrawingPath

@Composable
fun DrawingTopAppBar(
    tracks: MutableState<List<CustomDrawingPath>?>, bitmap: MutableState<Bitmap?>,
    color: (Color) -> Unit
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .drawBehind {
                drawRect(
                    color = Color.LightGray,
                    size = size.copy(width = size.width, height = size.height)
                )
            }
    ) {
        Column {
            Row {
                Button(
                    onClick = { tracks.value = null },
                    modifier = Modifier
                        .weight(1f, fill = true),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(text = AnnotatedString("CLEAR"))
                }
                Button(
                    onClick = {
                        if (bitmap.value != null) {
                            saveDrawingProcess(bmp = bitmap.value!!, context = context)
                        } else {
                            Toast.makeText(context, "画像取得に失敗しました", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .weight(1f, fill = true),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(text = AnnotatedString("SAVE"))
                }
            }
            Row {
                Button(
                    onClick = { color(Color(0xFF000000)) },
                    colors = ButtonDefaults.buttonColors(Color(0xFF000000)),
                    modifier = Modifier
                        .weight(1f, fill = true)
                ) {}
                Button(
                    onClick = { color(Color(0xFFFF0000)) },
                    colors = ButtonDefaults.buttonColors(Color(0xFFFF0000)),
                    modifier = Modifier
                        .weight(1f, fill = true)
                ) {}
                Button(
                    onClick = { color(Color(0xFF0000FF)) },
                    colors = ButtonDefaults.buttonColors(Color(0xFF0000FF)),
                    modifier = Modifier
                        .weight(1f, fill = true)
                ) {}
                Button(
                    onClick = { color(Color(0xFFFFFB00)) },
                    colors = ButtonDefaults.buttonColors(Color(0xFFFFFB00)),
                    modifier = Modifier
                        .weight(1f, fill = true)
                ) {}
                Button(
                    onClick = { color(Color(0xFF00FF00)) },
                    colors = ButtonDefaults.buttonColors(Color(0xFF00FF00)),
                    modifier = Modifier
                        .weight(1f, fill = true)
                ) {}
                Button(
                    onClick = { color(Color(0xFFFF5722)) },
                    colors = ButtonDefaults.buttonColors(Color(0xFFFF5722)),
                    modifier = Modifier
                        .weight(1f, fill = true)
                ) {}
                Button(
                    onClick = { color(Color(0xFFA600FF)) },
                    colors = ButtonDefaults.buttonColors(Color(0xFFA600FF)),
                    modifier = Modifier
                        .weight(1f, fill = true)
                ) {}
                Button(
                    onClick = { color(Color(0xFFCF490A)) },
                    colors = ButtonDefaults.buttonColors(Color(0xFFCF490A)),
                    modifier = Modifier
                        .weight(1f, fill = true)
                ) {}
                Button(
                    onClick = { color(Color(0xFF00BCD4)) },
                    colors = ButtonDefaults.buttonColors(Color(0xFF00BCD4)),
                    modifier = Modifier
                        .weight(1f, fill = true)
                ) {}
                Button(
                    onClick = { color(Color(0xFF646464)) },
                    colors = ButtonDefaults.buttonColors(Color(0xFF646464)),
                    modifier = Modifier
                        .weight(1f, fill = true)
                ) {}
            }
        }
    }
}
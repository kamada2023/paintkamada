package jp.co.abs.paintkamada.ui.theme

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.AnnotatedString
import jp.co.abs.paintkamada.CustomDrawingPath

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawingScreen(modifier: Modifier = Modifier) {
    // 描画の履歴の記録のため
    var penSize by remember { mutableStateOf(4f) }
    var penType by remember { mutableStateOf(PenType.BRUSH) }
    val tracks = rememberSaveable { mutableStateOf<List<CustomDrawingPath>?>(null) }
    var penColor by remember { mutableStateOf(Color(0xFF000000)) }
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Text("ペイントアプリ")
                    }
                )
                /**カラーパレット*/
                DrawingTopAppBar(tracks = tracks, bitmap = bitmap) { color -> penColor = color }
            }
        },
        bottomBar = {
            Row(
                modifier = modifier
                    .drawBehind {
                        drawRect(
                            color = Color.LightGray,
                            size = size.copy(width = size.width, height = size.height)
                        )
                    }
            ) {
                Button(
                    onClick = {
                        penSize = 10.0f
                        penType = PenType.BRUSH
                    },
                    modifier = Modifier.weight(2f),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(text = AnnotatedString("太字"))
                }
                Button(
                    onClick = {
                        penSize = 4.0f
                        penType = PenType.BRUSH
                    },
                    modifier = Modifier.weight(2f),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(text = AnnotatedString("細字"))
                }
                TextButton(
                    onClick = { penType = PenType.ARC },
                    modifier = Modifier
                        .weight(1f)
                        .drawBehind {
                            drawArc(
                                color = Color.Blue,
                                startAngle = 0f,
                                sweepAngle = 360f,
                                useCenter = true,
                                topLeft = Offset(30f, 10f),
                                size = size.copy(90f, 90f)
                            )
                        }
                ) {}
                TextButton(
                    onClick = { penType = PenType.TRIANGLE },
                    modifier = Modifier
                        .weight(1f)
                        .drawBehind {
                            val path = Path()
                            path.moveTo(60f, 10f)
                            path.lineTo(10f, 100f)
                            path.lineTo(110f, 100f)
                            path.lineTo(60f, 10f)
                            drawPath(
                                path = path,
                                color = Color.Blue
                            )
                        }
                ) {}
                TextButton(
                    onClick = { penType = PenType.RECT },
                    modifier = Modifier
                        .weight(1f)
                        .drawBehind {
                            drawRect(
                                color = Color.Blue,
                                topLeft = Offset(10f, 10f),
                                size = size.copy(90f, 90f)
                            )
                        }
                ) {}
            }
        }
    ) {
        /**Canvasでの描画部分*/
        DrawingCanvas(
            tracks = tracks,
            penSize = penSize,
            penColor = penColor,
            penType = penType,
            modifier = Modifier.padding(it),
            density = density,
            layoutDirection = layoutDirection,
            bitmap = bitmap
        )
        Canvas(
            modifier = Modifier.padding(it),
            onDraw = {
                val bmp = bitmap.value
                if (bmp != null) drawImage(bmp.asImageBitmap())
            }
        )
    }
}

enum class PenType {
    BRUSH,
    ARC,
    TRIANGLE,
    RECT,
}


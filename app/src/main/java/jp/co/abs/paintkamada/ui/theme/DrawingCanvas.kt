package jp.co.abs.paintkamada.ui.theme

import android.graphics.Bitmap
import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import jp.co.abs.paintkamada.CustomDrawingPath
import jp.co.abs.paintkamada.DrawingPathRoute
import kotlin.math.abs

@Suppress("UNUSED_EXPRESSION")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrawingCanvas(
    tracks: MutableState<List<CustomDrawingPath>?>,
    penSize: Float,
    penColor: Color,
    penType: PenType,
    modifier: Modifier,
    density: Density,
    layoutDirection: LayoutDirection,
    bitmap: MutableState<Bitmap?>
) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInteropFilter { motionEvent: MotionEvent ->
                when (motionEvent.action) {
                    // 描き始めの処理
                    MotionEvent.ACTION_DOWN -> {
                        tracks.value = ArrayList<CustomDrawingPath>().apply {
                            tracks.value?.let { addAll(it) }
                            add(
                                if (penType == PenType.BRUSH) {
                                    CustomDrawingPath(
                                        path = DrawingPathRoute.MoveTo(
                                            motionEvent.x,
                                            motionEvent.y
                                        ),
                                        color = penColor,
                                        size = penSize,
                                        type = penType
                                    )
                                } else {
                                    CustomDrawingPath(
                                        path = DrawingPathRoute.TopLeft(
                                            motionEvent.x,
                                            motionEvent.y
                                        ),
                                        color = penColor,
                                        size = penSize,
                                        type = penType
                                    )
                                }
                            )
                        }
                    }
                    // 書いてる途中の処理
                    MotionEvent.ACTION_MOVE -> {
                        tracks.value = ArrayList<CustomDrawingPath>().apply {
                            tracks.value?.let { addAll(it) }
                            if (penType == PenType.BRUSH) {
                                add(
                                    CustomDrawingPath(
                                        path = DrawingPathRoute.LineTo(
                                            motionEvent.x,
                                            motionEvent.y
                                        ),
                                        color = penColor,
                                        size = penSize,
                                        type = penType
                                    )
                                )
                            }
                        }
                    }
                    // 指を離した位置
                    MotionEvent.ACTION_UP -> {
                        tracks.value = ArrayList<CustomDrawingPath>().apply {
                            tracks.value?.let { addAll(it) }
                            if (penType != PenType.BRUSH) {
                                add(
                                    CustomDrawingPath(
                                        path = DrawingPathRoute.SizeTo(
                                            motionEvent.x,
                                            motionEvent.y
                                        ),
                                        color = penColor,
                                        size = penSize,
                                        type = penType
                                    )
                                )
                            }
                        }
                    }

                    else -> false
                }
                true
            }
    ) {
        val imageBitmap = ImageBitmap(width = size.width.toInt(), height = size.height.toInt())

        CanvasDrawScope().draw(
            density,
            layoutDirection,
            androidx.compose.ui.graphics.Canvas(imageBitmap),
            size
        ) {
            clipRect {
                var currentPath = Path()
                var shapeOffset = Offset.Zero
                var top: Float
                var bottom: Float
                var shapeWidth: Float
                var shapeHeight: Float
                drawRect(
                    color = Color.White,
                    size = size.copy(width = size.width, height = size.height)
                )
                tracks.let {
                    tracks.value?.forEach { customDrawingPath ->
                        when (customDrawingPath.path) {
                            is DrawingPathRoute.MoveTo -> {
                                currentPath = Path().apply {
                                    moveTo(customDrawingPath.path.x, customDrawingPath.path.y)
                                }
                                customDrawingPath.color = penColor
                            }

                            is DrawingPathRoute.LineTo -> {
                                drawPath(
                                    path = currentPath.apply {
                                        lineTo(
                                            customDrawingPath.path.x,
                                            customDrawingPath.path.y
                                        )
                                    },
                                    color = customDrawingPath.color,
                                    style = Stroke(width = customDrawingPath.size),
                                    blendMode = BlendMode.SrcOver
                                )
                            }

                            is DrawingPathRoute.TopLeft -> {
                                shapeOffset =
                                    Offset(customDrawingPath.path.x, customDrawingPath.path.y)
                            }

                            is DrawingPathRoute.SizeTo -> {
                                when (customDrawingPath.type) {
                                    PenType.ARC -> {
                                        shapeWidth = shapeOffset.x
                                        shapeHeight = shapeOffset.y
                                        if (shapeOffset.x > customDrawingPath.path.x) shapeWidth =
                                            customDrawingPath.path.x
                                        if (shapeOffset.y > customDrawingPath.path.y) shapeHeight =
                                            customDrawingPath.path.y
                                        drawArc(
                                            color = customDrawingPath.color,
                                            topLeft = Offset(shapeWidth, shapeHeight),
                                            startAngle = 0f,
                                            sweepAngle = 360f,
                                            useCenter = false,
                                            size = Size(
                                                abs(customDrawingPath.path.x - shapeOffset.x),
                                                abs(customDrawingPath.path.y - shapeOffset.y)
                                            ),
                                            style = Stroke(width = 5f),
                                            blendMode = BlendMode.SrcOver
                                        )
                                    }

                                    PenType.TRIANGLE -> {
                                        drawPath(
                                            path = Path().apply {
                                                top = shapeOffset.y
                                                bottom = customDrawingPath.path.y
                                                if (top > customDrawingPath.path.y) {
                                                    top = customDrawingPath.path.y
                                                    bottom = shapeOffset.y
                                                }
                                                moveTo(
                                                    (customDrawingPath.path.x + shapeOffset.x) / 2f,
                                                    top
                                                )
                                                lineTo(shapeOffset.x, bottom)
                                                lineTo(customDrawingPath.path.x, bottom)
                                                lineTo(
                                                    (customDrawingPath.path.x + shapeOffset.x) / 2f,
                                                    top
                                                )
                                            },
                                            color = customDrawingPath.color,
                                            style = Stroke(
                                                width = 5f,
                                                cap = StrokeCap.Butt
                                            ),
                                            blendMode = BlendMode.SrcOver
                                        )
                                    }

                                    PenType.RECT -> {
                                        shapeWidth = shapeOffset.x
                                        shapeHeight = shapeOffset.y
                                        if (shapeOffset.x > customDrawingPath.path.x) shapeWidth =
                                            customDrawingPath.path.x
                                        if (shapeOffset.y > customDrawingPath.path.y) shapeHeight =
                                            customDrawingPath.path.y
                                        drawRect(
                                            color = customDrawingPath.color,
                                            topLeft = Offset(shapeWidth, shapeHeight),
                                            size = Size(
                                                abs(customDrawingPath.path.x - shapeOffset.x),
                                                abs(customDrawingPath.path.y - shapeOffset.y)
                                            ),
                                            style = Stroke(width = 5f),
                                            blendMode = BlendMode.SrcOver
                                        )
                                    }

                                    else -> {}
                                }
                            }
                        }
                    }
                }
            }
        }
        bitmap.value = imageBitmap.asAndroidBitmap()
    }
}
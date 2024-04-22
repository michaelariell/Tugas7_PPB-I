package com.example.waterbottleapp

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WaterBottle(
    modifier: Modifier = Modifier,
    totalWaterAmount: Int,
    unit: String,
    usedWaterAmount: Int,

    waterWavesColor: Color = Color(0xff325EFF),
    bottleColor: Color = Color.White,
    capStartColor: Color = Color(0xff47029B),
    capEndColor: Color = Color(0xFF0065B9)
) {
    val waterPercentage = animateFloatAsState(
        targetValue = (usedWaterAmount.toFloat() / totalWaterAmount.toFloat()),
        label = "Water Waves animation",
        animationSpec = tween(durationMillis = 1000)
    ).value

    val usedWaterAmountAnimation = animateIntAsState(
        targetValue = usedWaterAmount,
        label = "Used water amount animation",
        animationSpec = tween(durationMillis = 1000)
    ).value

    Box(
        modifier = modifier
            .width(200.dp)
            .height(600.dp)
    ) {

        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            val capWidth = size.width * 0.55f
            val capHeight = size.height * 0.13f

            //Draw the bottle body
            val bodyPath = Path().apply {
                moveTo(width * 0.3f, height * 0.1f)
                lineTo(width * 0.3f, height * 0.2f)
                quadraticBezierTo(
                    0f, height * 0.3f, // The pulling point
                    0f, height * 0.4f
                )
                lineTo(0f, height * 0.95f)
                quadraticBezierTo(
                    0f, height,
                    width * 0.05f, height
                )

                lineTo(width * 0.95f, height)
                quadraticBezierTo(
                    width, height,
                    width, height * 0.95f
                )
                lineTo(width, height * 0.4f)
                quadraticBezierTo(
                    width, height * 0.3f,
                    width * 0.7f, height * 0.2f
                )
                lineTo(width * 0.7f, height * 0.2f)
                lineTo(width * 0.7f, height * 0.1f)

                close()
            }
            clipPath(
                path = bodyPath
            ) {
                // Draw the color of the bottle
                drawRect(
                    color = bottleColor,
                    size = size,
                    topLeft = Offset(0f, 0f)
                )

                //Draw the water waves
                val waterWavesYPosition = (1 - waterPercentage) * size.height

                val wavesPath = Path().apply {
                    moveTo(
                        x = 0f,
                        y = waterWavesYPosition
                    )
                    lineTo(
                        x = size.width,
                        y = waterWavesYPosition
                    )
                    lineTo(
                        x = size.width,
                        y = size.height
                    )
                    lineTo(
                        x = 0f,
                        y = size.height
                    )
                    close()
                }
                drawPath(
                    path = wavesPath,
                    color = waterWavesColor,
                )
            }
            val capGradient = Brush.verticalGradient(
                colors = listOf(
                    capStartColor, // Start color
                    capEndColor  // End color
                )
            )
            //Draw the bottle cap
            drawRoundRect(
                brush = capGradient,
                size = Size(capWidth, capHeight),
                topLeft = Offset(size.width / 2 - capWidth / 2f, 0f),
                cornerRadius = CornerRadius(45f, 45f)
            )
            // Draw white lines on the cap
            val lineWidth = capWidth * 0.97f // Adjust this factor as needed
            val lineThickness = capHeight * 0.05f // Adjust thickness as needed

            drawRect(
                color = Color(0xFF243F55),
                size = Size(lineWidth, lineThickness),
                topLeft = Offset(size.width / 2 - lineWidth / 2f, capHeight * 0.30f)
            )

            drawRect(
                color = Color(0xFF243F55),
                size = Size(lineWidth, lineThickness),
                topLeft = Offset(size.width / 2 - lineWidth / 2f, capHeight * 0.45f)
            )

            drawRect(
                color = Color(0xFF243F55),
                size = Size(lineWidth, lineThickness),
                topLeft = Offset(size.width / 2 - lineWidth / 2f, capHeight * 0.60f)
            )

            drawRect(
                color = Color(0xFF243F55),
                size = Size(lineWidth, lineThickness),
                topLeft = Offset(size.width / 2 - lineWidth / 2f, capHeight * 0.750f)
            )

        }
        val text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = if (waterPercentage > 0.5f) bottleColor else waterWavesColor,
                    fontSize = 44.sp
                )
            ) {
                append(usedWaterAmountAnimation.toString())
            }
            withStyle(
                style = SpanStyle(
                    color = if (waterPercentage > 0.5f) bottleColor else waterWavesColor,
                    fontSize = 22.sp
                )
            ) {
                append(" ")
                append(unit)
            }
            append("\n")
            withStyle(
                style = SpanStyle(
                    color = Color.Black,
                    fontSize = 16.sp
                )
            ) {
                append("Water Level: ${String.format("%.0f%%", waterPercentage * 100)}")
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = text)
        }
    }
}


@Preview
@Composable
fun WaterBottlePreview() {
    WaterBottle(
        totalWaterAmount = 2200,
        unit = "ml",
        usedWaterAmount = 100,
        )
}
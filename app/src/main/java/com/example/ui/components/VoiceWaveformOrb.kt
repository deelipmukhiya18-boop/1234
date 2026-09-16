package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MayaPersona
import com.example.ui.theme.MayaCoral
import com.example.ui.theme.MayaCrimsonDark
import com.example.ui.theme.MayaRed
import com.example.ui.theme.MayaRedGlow
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun VoiceWaveformOrb(
    isListening: Boolean,
    isSpeaking: Boolean,
    isDuetActive: Boolean,
    isSleeping: Boolean,
    persona: MayaPersona,
    waveforms: List<Float>,
    onOrbClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "orb_pulse")

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = if (isListening || isSpeaking || isDuetActive) 1.15f else 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (isSpeaking || isListening) 700 else 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    val ringAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = if (isListening || isSpeaking || isDuetActive) 0.8f else 0.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ring_alpha"
    )

    val primaryColor = when {
        isSleeping -> Color(0xFF475569)
        persona == MayaPersona.VENOM -> Color(0xFF991B1B)
        persona == MayaPersona.FRIDAY -> Color(0xFF2563EB)
        else -> MayaRed
    }

    val glowColor = when {
        isSleeping -> Color(0xFF334155)
        persona == MayaPersona.VENOM -> Color(0xFFEF4444)
        persona == MayaPersona.FRIDAY -> Color(0xFF60A5FA)
        else -> MayaRedGlow
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(230.dp)
                .testTag("voice_orb_container"),
            contentAlignment = Alignment.Center
        ) {
            // Background concentric ripple rings
            Canvas(modifier = Modifier.size(220.dp)) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val baseRadius = size.minDimension / 2f

                drawCircle(
                    color = glowColor.copy(alpha = ringAlpha * 0.2f),
                    radius = baseRadius * pulseScale * 0.98f,
                    center = center,
                    style = Stroke(width = 2.dp.toPx())
                )
                drawCircle(
                    color = primaryColor.copy(alpha = ringAlpha * 0.4f),
                    radius = baseRadius * pulseScale * 0.82f,
                    center = center,
                    style = Stroke(width = 2.5.dp.toPx())
                )
                drawCircle(
                    color = glowColor.copy(alpha = ringAlpha * 0.65f),
                    radius = baseRadius * 0.65f,
                    center = center,
                    style = Stroke(width = 3.dp.toPx())
                )
            }

            // Central Glowing Sphere / Button
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .scale(pulseScale)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                glowColor,
                                primaryColor,
                                MayaCrimsonDark,
                                Color(0xFF0D0E14)
                            )
                        )
                    )
                    .border(2.dp, primaryColor.copy(alpha = 0.8f), CircleShape)
                    .clickable { onOrbClick() }
                    .testTag("voice_orb_button"),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = when {
                            isDuetActive -> Icons.Default.PhoneInTalk
                            isSleeping -> Icons.Default.MicOff
                            isListening -> Icons.Default.Mic
                            else -> Icons.Default.Mic
                        },
                        contentDescription = "Voice Indicator",
                        tint = TextPrimary,
                        modifier = Modifier.size(38.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = when {
                            isSleeping -> "SLEEP"
                            isDuetActive -> "DUET CALL"
                            isSpeaking -> "SPEAKING"
                            isListening -> "LISTENING"
                            else -> "READY"
                        },
                        color = TextPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Soundwave Audio Spectrum Bars
        Row(
            modifier = Modifier
                .height(36.dp)
                .padding(horizontal = 24.dp)
                .testTag("waveform_bar_row"),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            waveforms.forEachIndexed { index, heightFactor ->
                val barHeight = (heightFactor * 32.dp.value).coerceIn(4f, 34f).dp
                val barModifier = if (isSleeping) {
                    Modifier.background(TextSecondary.copy(alpha = 0.3f))
                } else {
                    Modifier.background(Brush.verticalGradient(listOf(MayaCoral, primaryColor)))
                }
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(barHeight)
                        .clip(CircleShape)
                        .then(barModifier)
                )
            }
        }
    }
}

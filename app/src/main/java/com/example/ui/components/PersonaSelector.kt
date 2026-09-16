package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MayaPersona
import com.example.ui.theme.MayaBlue
import com.example.ui.theme.MayaBorder
import com.example.ui.theme.MayaCardDark
import com.example.ui.theme.MayaRed
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun PersonaSelector(
    selectedPersona: MayaPersona,
    onSelectPersona: (MayaPersona) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ACTIVE PERSONA",
                color = TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(
                text = selectedPersona.tag,
                color = when (selectedPersona) {
                    MayaPersona.MAYA -> MayaRed
                    MayaPersona.FRIDAY -> MayaBlue
                    MayaPersona.VENOM -> Color(0xFFEF4444)
                },
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MayaPersona.entries.forEach { persona ->
                val isSelected = persona == selectedPersona
                val accentColor = when (persona) {
                    MayaPersona.MAYA -> MayaRed
                    MayaPersona.FRIDAY -> MayaBlue
                    MayaPersona.VENOM -> Color(0xFFEF4444)
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) accentColor.copy(alpha = 0.18f) else MayaCardDark)
                        .border(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) accentColor else MayaBorder,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { onSelectPersona(persona) }
                        .padding(vertical = 10.dp, horizontal = 8.dp)
                        .testTag("persona_tab_${persona.name.lowercase()}"),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = persona.title,
                            color = if (isSelected) TextPrimary else TextSecondary,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                        Text(
                            text = when (persona) {
                                MayaPersona.MAYA -> "Warm"
                                MayaPersona.FRIDAY -> "Crisp"
                                MayaPersona.VENOM -> "Blunt"
                            },
                            color = if (isSelected) accentColor else TextSecondary.copy(alpha = 0.7f),
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

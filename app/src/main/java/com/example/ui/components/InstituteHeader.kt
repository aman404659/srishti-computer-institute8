package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.language.AppLanguage
import com.example.ui.language.LocalAppLanguage
import com.example.ui.language.SrishtiStrings
import com.example.ui.theme.CyanContainer
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyMidnight
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WhitePure

@Composable
fun InstituteHeader(
    modifier: Modifier = Modifier,
    onLogoClick: () -> Unit = {}
) {
    val langState = LocalAppLanguage.current
    val currentLang = langState.currentLanguage

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding(),
        color = NavyDark,
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Institute Brand & Emblem
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    // Official Institute Logo
                    InstituteLogo(
                        size = 46.dp,
                        modifier = Modifier.testTag("institute_header_logo")
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = SrishtiStrings.INSTITUTE_NAME,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.3.sp
                            ),
                            color = WhitePure
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(NeonCyan)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = SrishtiStrings.INSTITUTE_TAGLINE,
                                style = MaterialTheme.typography.labelSmall,
                                color = OrangeAccent,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Bilingual Toggle Switch (EN / HI)
                AssistChip(
                    onClick = { langState.toggle() },
                    label = {
                        Text(
                            text = if (currentLang == AppLanguage.EN) "हिन्दी" else "ENG",
                            color = WhitePure,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Translate,
                            contentDescription = "Switch Language",
                            tint = NeonCyan,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = CyanContainer,
                        labelColor = WhitePure
                    ),
                    border = AssistChipDefaults.assistChipBorder(
                        borderColor = NeonCyan.copy(alpha = 0.5f),
                        borderWidth = 1.dp,
                        enabled = true
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.testTag("language_toggle_button")
                )
            }
        }
    }
}

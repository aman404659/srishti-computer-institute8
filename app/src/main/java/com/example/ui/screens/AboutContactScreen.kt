package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.InstituteLogo
import com.example.ui.language.SrishtiStrings
import com.example.ui.theme.CyanContainer
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyMidnight
import com.example.ui.theme.NavySurface
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.OrangeContainer
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WhitePure
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.InstituteViewModel

@Composable
fun AboutContactScreen(
    viewModel: InstituteViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    BackHandler {
        viewModel.navigateTo(AppScreen.HOME)
    }

    // Helper functions for opening Intents safely on Android mobile devices
    fun openDialer(phoneNumber: String) {
        try {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phoneNumber")
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Could not launch dialer: $phoneNumber", Toast.LENGTH_SHORT).show()
        }
    }

    fun openBrowserUri(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Could not open link", Toast.LENGTH_SHORT).show()
        }
    }

    fun copyToClipboard(label: String, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "$label copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NavyDark)
            .testTag("institute_info_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Institute Header Card (Official Logo, Name, Tagline)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("institute_header_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = NavyMidnight),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.linearGradient(
                        listOf(NeonCyan, OrangeAccent, NavyBorder)
                    )
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp, horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Official Institute Logo at the top
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(WhitePure)
                            .border(2.5.dp, OrangeAccent, CircleShape)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        InstituteLogo(
                            size = 88.dp,
                            isCircular = true,
                            showBorder = false,
                            showGlow = false
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Institute Name prominently displayed
                    Text(
                        text = SrishtiStrings.OFFICIAL_INSTITUTE_NAME,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = WhitePure,
                        textAlign = TextAlign.Center,
                        letterSpacing = 0.5.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Tagline: LEARN • PRACTICE • GROW
                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        color = OrangeContainer,
                        border = androidx.compose.foundation.BorderStroke(1.dp, OrangeAccent),
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = SrishtiStrings.INSTITUTE_TAGLINE,
                                fontWeight = FontWeight.ExtraBold,
                                color = OrangeAccent,
                                fontSize = 13.sp,
                                letterSpacing = 2.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        // 2. Quick Action Buttons (Call Now & WhatsApp Chat)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Call Now button (tel:8953994687)
                Button(
                    onClick = { openDialer(SrishtiStrings.MOBILE_NUMBER) },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("call_now_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CyanContainer,
                        contentColor = NeonCyan
                    ),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, NeonCyan)
                ) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Call Now",
                        tint = NeonCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Call Now",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                // WhatsApp Chat button (https://wa.me/918953994687)
                Button(
                    onClick = { openBrowserUri(SrishtiStrings.WHATSAPP_CHAT_URL) },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("whatsapp_chat_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SuccessGreen,
                        contentColor = WhitePure
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Chat,
                        contentDescription = "WhatsApp Chat",
                        tint = WhitePure,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "WhatsApp",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }

        // 3. Institute Website Button (🌐 Visit Our Website)
        item {
            Button(
                onClick = { openBrowserUri(SrishtiStrings.WEBSITE_LINK) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("visit_website_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NavySurface,
                    contentColor = WhitePure
                ),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, NeonCyan)
            ) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = null,
                    tint = NeonCyan,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "🌐 Visit Our Website",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = WhitePure
                )
            }
        }

        // 4. Community Channels Section (Join WhatsApp Group & Join Telegram Channel)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("community_channels_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = NavyMidnight),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.linearGradient(
                        listOf(NavyBorder, NeonCyan.copy(alpha = 0.4f))
                    )
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "OFFICIAL GROUPS & CHANNELS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = NeonCyan,
                        letterSpacing = 1.5.sp
                    )

                    // Button: Join WhatsApp Group
                    Button(
                        onClick = { openBrowserUri(SrishtiStrings.WHATSAPP_GROUP_URL) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("join_whatsapp_group_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NavySurface,
                            contentColor = WhitePure
                        ),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.2.dp, SuccessGreen)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(SuccessGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Group,
                                contentDescription = null,
                                tint = WhitePure,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Join WhatsApp Group",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = WhitePure
                        )
                    }

                    // Button: Join Telegram Channel
                    Button(
                        onClick = { openBrowserUri(SrishtiStrings.TELEGRAM_CHANNEL_URL) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("join_telegram_channel_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NavySurface,
                            contentColor = WhitePure
                        ),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFF0088CC))
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF0088CC)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = null,
                                tint = WhitePure,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Join Telegram Channel",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = WhitePure
                        )
                    }
                }
            }
        }

        // 5. Contact Details Directory Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("contact_numbers_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = NavyMidnight),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.linearGradient(
                        listOf(NavyBorder, OrangeAccent.copy(alpha = 0.3f))
                    )
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "DIRECT CONTACT NUMBERS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = OrangeAccent,
                        letterSpacing = 1.5.sp
                    )

                    // Mobile Number Row
                    ContactDetailRow(
                        icon = Icons.Default.Phone,
                        iconTint = NeonCyan,
                        label = "Mobile Number",
                        value = SrishtiStrings.MOBILE_NUMBER,
                        actionLabel = "Call",
                        onActionClick = { openDialer(SrishtiStrings.MOBILE_NUMBER) },
                        onCopyClick = { copyToClipboard("Mobile Number", SrishtiStrings.MOBILE_NUMBER) },
                        modifier = Modifier.testTag("mobile_number_row")
                    )

                    HorizontalDivider(color = NavyBorder.copy(alpha = 0.6f))

                    // WhatsApp Number Row
                    ContactDetailRow(
                        icon = Icons.AutoMirrored.Filled.Chat,
                        iconTint = SuccessGreen,
                        label = "WhatsApp Number",
                        value = SrishtiStrings.WHATSAPP_NUMBER,
                        actionLabel = "Chat",
                        onActionClick = { openBrowserUri(SrishtiStrings.WHATSAPP_CHAT_URL) },
                        onCopyClick = { copyToClipboard("WhatsApp Number", SrishtiStrings.WHATSAPP_NUMBER) },
                        modifier = Modifier.testTag("whatsapp_number_row")
                    )
                }
            }
        }

        // 6. Location Section (Displaying Full Institute Address & Google Maps Button)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("location_section_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = NavyMidnight),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.linearGradient(
                        listOf(OrangeAccent.copy(alpha = 0.5f), NavyBorder)
                    )
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(OrangeContainer)
                                .border(1.dp, OrangeAccent, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Location",
                                tint = OrangeAccent,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "INSTITUTE ADDRESS",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = OrangeAccent,
                                letterSpacing = 1.5.sp
                            )
                            Text(
                                text = "Campus Location",
                                fontSize = 12.sp,
                                color = TextMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Full Address Card
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = NavySurface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { copyToClipboard("Institute Address", SrishtiStrings.INSTITUTE_ADDRESS) }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Text(
                                text = SrishtiStrings.INSTITUTE_ADDRESS,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = WhitePure,
                                lineHeight = 22.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Tap to copy address",
                                    tint = TextMuted,
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = "Tap to copy address",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Google Maps Button (📍 View Institute on Google Maps)
                    Button(
                        onClick = { openBrowserUri(SrishtiStrings.GOOGLE_MAPS_LINK) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("view_on_google_maps_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OrangeAccent,
                            contentColor = WhitePure
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Map,
                            contentDescription = null,
                            tint = WhitePure,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "📍 View Institute on Google Maps",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp,
                            color = WhitePure
                        )
                    }
                }
            }
        }

        // Bottom space
        item {
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun ContactDetailRow(
    icon: ImageVector,
    iconTint: Color,
    label: String,
    value: String,
    actionLabel: String,
    onActionClick: () -> Unit,
    onCopyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(NavySurface)
                    .border(1.dp, NavyBorder, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }
            Column {
                Text(
                    text = label,
                    fontSize = 11.sp,
                    color = TextMuted
                )
                Text(
                    text = value,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = WhitePure
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            IconButton(
                onClick = onCopyClick,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy $label",
                    tint = TextSecondary,
                    modifier = Modifier.size(16.dp)
                )
            }

            Button(
                onClick = onActionClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CyanContainer,
                    contentColor = NeonCyan
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.6f))
            ) {
                Text(
                    text = actionLabel,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}

package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AppRegistration
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.language.AppLanguage
import com.example.ui.language.LocalAppLanguage
import com.example.ui.language.SrishtiStrings
import com.example.ui.theme.CyanContainer
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyMidnight
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextMuted
import com.example.ui.theme.WhitePure
import com.example.ui.viewmodel.AppScreen

data class NavItemData(
    val screen: AppScreen,
    val titleEn: String,
    val titleHi: String,
    val icon: ImageVector,
    val tag: String
)

val instituteNavItems = listOf(
    NavItemData(AppScreen.HOME, "Home", "होम", Icons.Default.Home, "nav_home"),
    NavItemData(AppScreen.COURSES, "Courses", "कोर्स", Icons.Default.MenuBook, "nav_courses"),
    NavItemData(AppScreen.ADMISSION, "Admission", "प्रवेश", Icons.Default.AppRegistration, "nav_admission"),
    NavItemData(AppScreen.STUDENT_PORTAL, "Student", "छात्र", Icons.Default.School, "nav_student"),
    NavItemData(AppScreen.ADMIN_PANEL, "Admin", "एडमिन", Icons.Default.AdminPanelSettings, "nav_admin"),
    NavItemData(AppScreen.ABOUT_CONTACT, "Contact", "संपर्क", Icons.Default.ContactPhone, "nav_contact")
)

@Composable
fun InstituteBottomBar(
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val langState = LocalAppLanguage.current
    val currentLang = langState.currentLanguage

    NavigationBar(
        modifier = modifier
            .navigationBarsPadding(),
        containerColor = NavyDark,
        tonalElevation = 8.dp
    ) {
        instituteNavItems.forEach { item ->
            val isSelected = currentScreen == item.screen
            val title = if (currentLang == AppLanguage.EN) item.titleEn else item.titleHi

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.screen) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = title,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = title,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = NeonCyan,
                    selectedTextColor = NeonCyan,
                    indicatorColor = CyanContainer,
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted
                ),
                modifier = Modifier.testTag(item.tag)
            )
        }
    }
}

@Composable
fun InstituteNavRail(
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val langState = LocalAppLanguage.current
    val currentLang = langState.currentLanguage

    NavigationRail(
        modifier = modifier,
        containerColor = NavyDark
    ) {
        instituteNavItems.forEach { item ->
            val isSelected = currentScreen == item.screen
            val title = if (currentLang == AppLanguage.EN) item.titleEn else item.titleHi

            NavigationRailItem(
                selected = isSelected,
                onClick = { onNavigate(item.screen) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = title,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = title,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = NeonCyan,
                    selectedTextColor = NeonCyan,
                    indicatorColor = CyanContainer,
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted
                ),
                modifier = Modifier.testTag(item.tag)
            )
        }
    }
}

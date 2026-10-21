package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.InstituteBottomBar
import com.example.ui.components.InstituteHeader
import com.example.ui.components.InstituteNavRail
import com.example.ui.language.LanguageState
import com.example.ui.language.LocalAppLanguage
import com.example.ui.screens.AboutContactScreen
import com.example.ui.screens.AdmissionScreen
import com.example.ui.screens.AdminPanelScreen
import com.example.ui.screens.CoursesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.StudentPortalScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NavyDark
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.InstituteViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val languageState = remember { LanguageState() }

            CompositionLocalProvider(LocalAppLanguage provides languageState) {
                MyApplicationTheme(darkTheme = true) {
                    val instituteViewModel: InstituteViewModel = viewModel()
                    MainAppRoot(viewModel = instituteViewModel)
                }
            }
        }
    }
}

@Composable
fun MainAppRoot(viewModel: InstituteViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()

    // Back handler: Return to HOME if on sub-screen
    BackHandler(enabled = currentScreen != AppScreen.HOME) {
        viewModel.navigateTo(AppScreen.HOME)
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(NavyDark)) {
        val isWideScreen = maxWidth > 600.dp

        if (isWideScreen) {
            // Tablet / Desktop layout with NavigationRail
            Row(modifier = Modifier.fillMaxSize()) {
                InstituteNavRail(
                    currentScreen = currentScreen,
                    onNavigate = { viewModel.navigateTo(it) }
                )

                Column(modifier = Modifier.weight(1f).fillMaxSize()) {
                    InstituteHeader(onLogoClick = { viewModel.navigateTo(AppScreen.HOME) })

                    Box(modifier = Modifier.weight(1f).fillMaxSize()) {
                        ScreenContent(
                            currentScreen = currentScreen,
                            viewModel = viewModel
                        )
                    }
                }
            }
        } else {
            // Phone layout with Top Header and Bottom Navigation Bar
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = NavyDark,
                topBar = {
                    InstituteHeader(onLogoClick = { viewModel.navigateTo(AppScreen.HOME) })
                },
                bottomBar = {
                    InstituteBottomBar(
                        currentScreen = currentScreen,
                        onNavigate = { viewModel.navigateTo(it) }
                    )
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    ScreenContent(
                        currentScreen = currentScreen,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun ScreenContent(
    currentScreen: AppScreen,
    viewModel: InstituteViewModel
) {
    when (currentScreen) {
        AppScreen.HOME -> HomeScreen(
            viewModel = viewModel,
            onNavigate = { viewModel.navigateTo(it) }
        )
        AppScreen.COURSES -> CoursesScreen(
            viewModel = viewModel,
            onNavigate = { viewModel.navigateTo(it) }
        )
        AppScreen.ADMISSION -> AdmissionScreen(
            viewModel = viewModel,
            onNavigate = { viewModel.navigateTo(it) }
        )
        AppScreen.STUDENT_PORTAL -> StudentPortalScreen(
            viewModel = viewModel,
            onNavigate = { viewModel.navigateTo(it) }
        )
        AppScreen.ADMIN_PANEL -> AdminPanelScreen(
            viewModel = viewModel
        )
        AppScreen.ABOUT_CONTACT -> AboutContactScreen(
            viewModel = viewModel
        )
    }
}

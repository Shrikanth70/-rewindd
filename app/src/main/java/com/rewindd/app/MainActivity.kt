package com.rewindd.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rewindd.app.core.designsystem.components.RewinddTopAppBar
import com.rewindd.app.core.designsystem.theme.Paper
import com.rewindd.app.core.designsystem.theme.RewinddTheme
import com.rewindd.app.feature.home.HomeViewModel
import com.rewindd.app.feature.library.LibraryViewModel
import com.rewindd.app.feature.rewind.RewindViewModel
import com.rewindd.app.navigation.BottomNavBar
import com.rewindd.app.navigation.RewinddNavGraph
import com.rewindd.app.navigation.TopLevelDestination
import com.rewindd.app.navigation.navigateToTopLevelDestination

import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private var sharedUrlForCapture by mutableStateOf<String?>(null)

    private val homeViewModel: HomeViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val app = application as RewinddApplication
                return HomeViewModel(
                    savedItemRepository = app.savedItemRepository
                ) as T
            }
        }
    }

    private val libraryViewModel: LibraryViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val app = application as RewinddApplication
                return LibraryViewModel(
                    savedItemRepository = app.savedItemRepository,
                    collectionRepository = app.collectionRepository,
                    tagRepository = app.tagRepository
                ) as T
            }
        }
    }

    private val rewindViewModel: RewindViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val app = application as RewinddApplication
                return RewindViewModel(savedItemRepository = app.savedItemRepository) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        // Drop the system splash on the first real frame. Do not hold it, and
        // do not play its icon exit — that is the second splash.
        splashScreen.setKeepOnScreenCondition { false }
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            splashScreenView.remove()
        }
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("rewindd_prefs", MODE_PRIVATE)

        // Ensure clean vault: purge legacy prototype dummy items if present from past installs
        lifecycleScope.launch {
            val repo = (application as RewinddApplication).savedItemRepository
            listOf(
                "item-substack-ai-agent",
                "item-arxiv-rag-eval",
                "item-instagram-habits-reel",
                "item-youtube-autonomous-agents"
            ).forEach { dummyId ->
                try {
                    repo.deleteById(dummyId)
                } catch (_: Exception) {}
            }
        }

        handleIncomingIntent(intent)

        setContent {
            var isDarkMode by remember {
                mutableStateOf(prefs.getBoolean("dark_mode_enabled", false))
            }

            RewinddTheme(darkTheme = isDarkMode) {
                var showSplash by remember { mutableStateOf(true) }

                if (showSplash) {
                    com.rewindd.app.core.designsystem.components.BotanicalSplashAnimation(
                        onAnimationComplete = { showSplash = false }
                    )
                } else {
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route
                    val app = application as RewinddApplication
                    val showBottomBar = com.rewindd.app.navigation.NavigationRules.shouldShowBottomBar(currentRoute)
                    val isTopLevelRoute = currentRoute in listOf("home", "library", "insights")

                    Scaffold(
                        topBar = {
                            if (isTopLevelRoute) {
                                RewinddTopAppBar(
                                    onSettingsClick = {
                                        navController.navigate("settings")
                                    }
                                )
                            }
                        },
                        bottomBar = {
                            if (showBottomBar) {
                                BottomNavBar(
                                    currentRoute = currentRoute,
                                    onNavigateToDestination = { destination ->
                                        navController.navigateToTopLevelDestination(destination.route)
                                    }
                                )
                            }
                        },
                        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.background
                    ) { innerPadding ->
                        RewinddNavGraph(
                            navController = navController,
                            homeViewModel = homeViewModel,
                            libraryViewModel = libraryViewModel,
                            rewindViewModel = rewindViewModel,
                            savedItemRepository = app.savedItemRepository,
                            collectionRepository = app.collectionRepository,
                            tagRepository = app.tagRepository,
                            incomingSharedUrl = sharedUrlForCapture,
                            onConsumeSharedUrl = { sharedUrlForCapture = null },
                            isDarkMode = isDarkMode,
                            onToggleDarkMode = { enabled ->
                                isDarkMode = enabled
                                prefs.edit().putBoolean("dark_mode_enabled", enabled).apply()
                            },
                            modifier = Modifier
                                .padding(innerPadding)
                                .consumeWindowInsets(innerPadding)
                        )
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIncomingIntent(intent)
    }

    private fun handleIncomingIntent(intent: Intent?) {
        if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
            val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
            if (!sharedText.isNullOrBlank()) {
                sharedUrlForCapture = sharedText
            }
        }
    }
}

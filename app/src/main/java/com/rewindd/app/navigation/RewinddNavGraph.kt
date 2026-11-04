package com.rewindd.app.navigation

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rewindd.app.RewinddApplication
import com.rewindd.app.data.repository.CollectionRepository
import com.rewindd.app.data.repository.SavedItemRepository
import com.rewindd.app.data.repository.TagRepository
import com.rewindd.app.domain.export.VaultDataExporter
import com.rewindd.app.domain.model.SavedItem
import com.rewindd.app.domain.usecase.ExportVaultUseCase
import com.rewindd.app.domain.usecase.PurgeDatabaseUseCase
import com.rewindd.app.feature.capture.QuickCaptureSheet
import com.rewindd.app.feature.capture.SaveConfirmationCard
import com.rewindd.app.feature.settings.ExportFormat
import com.rewindd.app.feature.collections.CollectionDetailScreen
import com.rewindd.app.feature.collections.CreateEditCollectionSheet
import com.rewindd.app.feature.collections.ManageCollectionsScreen
import com.rewindd.app.feature.home.HomeScreen
import com.rewindd.app.feature.home.HomeViewModel
import com.rewindd.app.feature.itemdetail.EditItemScreen
import com.rewindd.app.feature.itemdetail.ItemDetailScreen
import com.rewindd.app.feature.itemdetail.ItemDetailViewModel
import com.rewindd.app.feature.library.FiltersBottomSheet
import com.rewindd.app.feature.library.LibraryScreen
import com.rewindd.app.feature.library.LibraryViewModel
import com.rewindd.app.feature.library.SearchScreen
import com.rewindd.app.feature.onboarding.HowRewinddWorksScreen
import com.rewindd.app.feature.onboarding.OnboardingCompleteScreen
import com.rewindd.app.feature.onboarding.PrivacyExplanationScreen
import com.rewindd.app.feature.onboarding.WelcomeScreen
import com.rewindd.app.feature.rewind.RewindScreen
import com.rewindd.app.feature.rewind.RewindViewModel
import com.rewindd.app.feature.rewind.components.safeOpenUrl
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rewindd.app.core.designsystem.theme.Ink
import com.rewindd.app.core.designsystem.theme.Paper
import com.rewindd.app.core.designsystem.theme.Surface
import com.rewindd.app.core.designsystem.theme.hardShadow
import com.rewindd.app.feature.settings.AboutSupportScreen
import com.rewindd.app.feature.settings.DeleteDataDialog
import com.rewindd.app.feature.settings.ExportDataScreen
import com.rewindd.app.feature.settings.PauseTrackingBottomSheet
import com.rewindd.app.feature.settings.PrivacyCenterScreen
import com.rewindd.app.feature.settings.SettingsScreen
import com.rewindd.app.feature.tags.TagsManagementScreen

@Composable
fun RewinddNavGraph(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    libraryViewModel: LibraryViewModel,
    rewindViewModel: RewindViewModel,
    savedItemRepository: SavedItemRepository,
    collectionRepository: CollectionRepository,
    tagRepository: TagRepository,
    incomingSharedUrl: String? = null,
    onConsumeSharedUrl: () -> Unit = {},
    isDarkMode: Boolean = false,
    onToggleDarkMode: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val app = context.applicationContext as RewinddApplication
    val prefs = remember { context.getSharedPreferences("rewindd_prefs", Context.MODE_PRIVATE) }
    val hasCompletedOnboarding = remember { prefs.getBoolean("has_completed_onboarding", false) }
    val startDestination = if (hasCompletedOnboarding) "home" else "onboarding/welcome"

    var showFilterSheet by remember { mutableStateOf(false) }
    var showCreateCollectionSheet by remember { mutableStateOf(false) }
    var editingCollection by remember { mutableStateOf<com.rewindd.app.domain.model.Collection?>(null) }
    var showDeleteDataDialog by remember { mutableStateOf(false) }
    var showPauseTrackingSheet by remember { mutableStateOf(false) }

    val allCollections by libraryViewModel.allCollections.collectAsState()
    val isSaving by libraryViewModel.isSaving.collectAsState()
    val unreviewedCount by rewindViewModel.unreviewedCount.collectAsState()

    var showQuickCaptureSheet by remember { mutableStateOf(false) }
    var captureSheetInitialUrl by remember { mutableStateOf<String?>(null) }
    var captureSheetInitialNote by remember { mutableStateOf<String?>(null) }
    var captureSheetInitialCollectionId by remember { mutableStateOf<String?>(null) }
    var isCreatingCollectionFromCapture by remember { mutableStateOf(false) }
    var savedConfirmationItem by remember { mutableStateOf<SavedItem?>(null) }

    LaunchedEffect(incomingSharedUrl) {
        if (!incomingSharedUrl.isNullOrBlank()) {
            captureSheetInitialUrl = incomingSharedUrl
            showQuickCaptureSheet = true
            onConsumeSharedUrl()
        }
    }

    // Filter Modal Sheet (Screen 22)
    if (showFilterSheet) {
        FiltersBottomSheet(
            viewModel = libraryViewModel,
            onDismiss = { showFilterSheet = false }
        )
    }

    // Create / Edit Collection Sheet (Screen 25)
    if (showCreateCollectionSheet || editingCollection != null) {
        CreateEditCollectionSheet(
            existingCollection = editingCollection,
            onSave = { name, colorToken, isPinned, iconToken ->
                val current = editingCollection
                if (current != null) {
                    libraryViewModel.updateCollection(
                        current.copy(
                            name = name,
                            colorToken = colorToken,
                            isPinned = isPinned,
                            iconToken = iconToken
                        )
                    )
                } else {
                    libraryViewModel.createCollection(name, colorToken, isPinned, iconToken = iconToken) { newCol ->
                        if (isCreatingCollectionFromCapture) {
                            captureSheetInitialCollectionId = newCol.id
                            showQuickCaptureSheet = true
                            isCreatingCollectionFromCapture = false
                        }
                    }
                }
                editingCollection = null
                showCreateCollectionSheet = false
            },
            onDismiss = {
                if (isCreatingCollectionFromCapture) {
                    showQuickCaptureSheet = true
                    isCreatingCollectionFromCapture = false
                }
                editingCollection = null
                showCreateCollectionSheet = false
            }
        )
    }

    val purgeDatabaseUseCase = remember {
        PurgeDatabaseUseCase(
            savedItemRepository = savedItemRepository,
            collectionRepository = collectionRepository,
            tagRepository = tagRepository,
            usageStatsRepository = app.usageStatsRepository
        )
    }

    val exportVaultUseCase = remember {
        ExportVaultUseCase(
            savedItemRepository = savedItemRepository,
            collectionRepository = collectionRepository,
            tagRepository = tagRepository,
            usageStatsRepository = app.usageStatsRepository
        )
    }

    // Delete All Data Confirmation Dialog (Screen 42)
    if (showDeleteDataDialog) {
        DeleteDataDialog(
            onConfirmDelete = {
                coroutineScope.launch {
                    purgeDatabaseUseCase.purgeAllData()
                    prefs.edit().putBoolean("has_purged_data", true).apply()
                    showDeleteDataDialog = false
                    android.widget.Toast.makeText(context, "All local data has been permanently deleted.", android.widget.Toast.LENGTH_SHORT).show()
                }
            },
            onDismiss = { showDeleteDataDialog = false }
        )
    }

    // Pause Tracking Bottom Sheet (Screen 40)
    if (showPauseTrackingSheet) {
        PauseTrackingBottomSheet(
            onConfirmPause = { durationHours ->
                coroutineScope.launch {
                    val pauseUntil = when (durationHours) {
                        1 -> System.currentTimeMillis() + 3600000L
                        12 -> System.currentTimeMillis() + (12 * 3600000L)
                        else -> Long.MAX_VALUE
                    }
                    app.usageStatsRepository.setTrackingPaused(pauseUntil)
                    showPauseTrackingSheet = false
                    val msg = if (durationHours == 999) "Tracking paused indefinitely" else "Tracking paused for $durationHours hours"
                    android.widget.Toast.makeText(context, msg, android.widget.Toast.LENGTH_SHORT).show()
                }
            },
            onDismiss = { showPauseTrackingSheet = false }
        )
    }

    // Quick Capture Sheet (Screen 11)
    if (showQuickCaptureSheet) {
        QuickCaptureSheet(
            initialUrl = captureSheetInitialUrl,
            initialCollectionId = captureSheetInitialCollectionId,
            initialNote = captureSheetInitialNote,
            collections = allCollections,
            isSaving = isSaving,
            onSave = { url, collectionId, note ->
                libraryViewModel.saveUrl(
                    rawInput = url,
                    sourcePackage = null,
                    sourceLabel = null,
                    collectionId = collectionId,
                    note = note,
                    onSuccess = { item ->
                        showQuickCaptureSheet = false
                        captureSheetInitialUrl = null
                        captureSheetInitialNote = null
                        captureSheetInitialCollectionId = null
                        isCreatingCollectionFromCapture = false
                        savedConfirmationItem = item
                    }
                )
            },
            onCreateCollectionRequested = { currentUrl, currentNote ->
                captureSheetInitialUrl = currentUrl
                captureSheetInitialNote = currentNote
                isCreatingCollectionFromCapture = true
                showQuickCaptureSheet = false
                showCreateCollectionSheet = true
            },
            onDismiss = {
                showQuickCaptureSheet = false
                captureSheetInitialUrl = null
                captureSheetInitialNote = null
                captureSheetInitialCollectionId = null
                isCreatingCollectionFromCapture = false
            }
        )
    }

    // Save Confirmation Sheet (Screen 12)
    savedConfirmationItem?.let { item ->
        val collectionName = remember(item, allCollections) {
            item.collectionId?.let { id ->
                allCollections.firstOrNull { it.id == id }?.name
            }
        }
        SaveConfirmationCard(
            savedItem = item,
            collectionName = collectionName,
            unreviewedCount = unreviewedCount,
            onStartRewind = {
                savedConfirmationItem = null
                navController.navigate("rewind")
            },
            onNavigateToLibrary = {
                savedConfirmationItem = null
                navController.navigate("library")
            },
            onUndo = { targetItem ->
                libraryViewModel.deleteItem(targetItem.id)
                savedConfirmationItem = null
            },
            onDismiss = {
                savedConfirmationItem = null
            }
        )
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Onboarding Screens (Screens 01–06)
        composable("onboarding/welcome") {
            WelcomeScreen(
                onGetStartedClick = { navController.navigate("onboarding/how_it_works") }
            )
        }

        composable("onboarding/how_it_works") {
            HowRewinddWorksScreen(
                onBackClick = { navController.popBackStack() },
                onContinueClick = { navController.navigate("onboarding/privacy") }
            )
        }

        composable("onboarding/privacy") {
            PrivacyExplanationScreen(
                onBackClick = { navController.popBackStack() },
                onContinueClick = { navController.navigate("onboarding/complete") }
            )
        }

        composable("onboarding/complete") {
            OnboardingCompleteScreen(
                onEnterRewinddClick = {
                    prefs.edit().putBoolean("has_completed_onboarding", true).apply()
                    navController.navigate("home") {
                        popUpTo("onboarding/welcome") { inclusive = true }
                    }
                }
            )
        }

        // Settings Screen (Screen 37)
        composable("settings") {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                isDarkMode = isDarkMode,
                onToggleDarkMode = onToggleDarkMode,
                onReplayOnboarding = { navController.navigate("onboarding/welcome") },
                onExportData = { navController.navigate("settings/export") },
                onDeleteAllData = { showDeleteDataDialog = true },
                onOpenPrivacyCenter = { navController.navigate("settings/privacy") },
                onOpenAboutSupport = { navController.navigate("settings/about") }
            )
        }

        // Screen 41: Export Data
        composable("settings/export") {
            ExportDataScreen(
                onBack = { navController.popBackStack() },
                onPerformExport = { format ->
                    coroutineScope.launch {
                        val (exportContent, filename) = when (format) {
                            ExportFormat.JSON -> exportVaultUseCase.exportJson() to "rewindd-vault-export.json"
                            ExportFormat.MARKDOWN -> exportVaultUseCase.exportMarkdown() to "rewindd-vault-export.md"
                            ExportFormat.CSV, ExportFormat.CSV_SCREEN_TIME -> exportVaultUseCase.exportCsv() to "rewindd-vault-export.csv"
                        }

                        val sendIntent = android.content.Intent().apply {
                            action = android.content.Intent.ACTION_SEND
                            putExtra(android.content.Intent.EXTRA_TEXT, exportContent)
                            putExtra(android.content.Intent.EXTRA_TITLE, filename)
                            type = "text/plain"
                        }
                        val shareIntent = android.content.Intent.createChooser(sendIntent, "Export Rewindd Vault")
                        context.startActivity(shareIntent)
                    }
                }
            )
        }

        // Screen 43: Privacy Center
        composable("settings/privacy") {
            PrivacyCenterScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // Screen 44: About & Support
        composable("settings/about") {
            AboutSupportScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // Home tab (Screens 07, 08 & 09)
        composable("home") {
            val rawItemsWithDetails by libraryViewModel.rawItemsWithDetails.collectAsState()
            HomeScreen(
                homeViewModel = homeViewModel,
                feedbackEvents = libraryViewModel.feedbackEvents,
                collections = allCollections,
                savedItems = rawItemsWithDetails,
                onOpenCollection = { colId -> navController.navigate("library/collection/$colId") },
                onNavigateToItemDetail = { itemId -> navController.navigate("library/item/$itemId") },
                onStartRewind = { navController.navigateToTopLevelDestination("rewind") },
                onNavigateToLibrary = { navController.navigateToTopLevelDestination("library") },
                onNavigateToManageCollections = { navController.navigate("library/manage_collections") },
                onOpenSettings = { navController.navigate("settings") },
                onOpenQuickCapture = { url ->
                    captureSheetInitialUrl = url
                    showQuickCaptureSheet = true
                },
                onQuickSaveUrl = { url ->
                    captureSheetInitialUrl = url
                    showQuickCaptureSheet = true
                }
            )
        }

        // Rewind Review Deck (Screens 15–19)
        composable("rewind") {
            RewindScreen(
                viewModel = rewindViewModel,
                onNavigateToHome = { navController.navigateToTopLevelDestination("home") },
                onNavigateToLibrary = { navController.navigateToTopLevelDestination("library") }
            )
        }

        // Library Screen (Screen 20)
        composable("library") {
            LibraryScreen(
                viewModel = libraryViewModel,
                onNavigateToSearch = { navController.navigate("library/search") },
                onNavigateToItemDetail = { itemId -> navController.navigate("library/item/$itemId") },
                onNavigateToCreateCollection = { showCreateCollectionSheet = true },
                onOpenCollectionDetail = { colId -> navController.navigate("library/collection/$colId") },
                onNavigateToManageCollections = { navController.navigate("library/manage_collections") },
                onOpenOriginal = { url -> safeOpenUrl(context, url) {} },
                onOpenFilterSheet = { showFilterSheet = true },
                onNavigateToTags = { navController.navigate("library/tags") },
                onOpenQuickCapture = { url ->
                    captureSheetInitialUrl = url
                    showQuickCaptureSheet = true
                }
            )
        }

        // Tags Management Screen (Screen 27)
        composable("library/tags") {
            TagsManagementScreen(
                viewModel = libraryViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // Manage Collections Screen
        composable("library/manage_collections") {
            ManageCollectionsScreen(
                viewModel = libraryViewModel,
                onBack = { navController.popBackStack() },
                onOpenCollectionDetail = { colId -> navController.navigate("library/collection/$colId") },
                onCreateCollection = { showCreateCollectionSheet = true },
                onEditCollection = { col ->
                    editingCollection = col
                    showCreateCollectionSheet = true
                }
            )
        }

        // Search Screen (Screen 21)
        composable("library/search") {
            SearchScreen(
                viewModel = libraryViewModel,
                onBack = { navController.popBackStack() },
                onNavigateToItemDetail = { itemId -> navController.navigate("library/item/$itemId") },
                onOpenOriginal = { url -> safeOpenUrl(context, url) {} }
            )
        }

        // Item Detail Screen (Screen 23)
        composable(
            route = "library/item/{itemId}",
            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
            val detailViewModel = remember(itemId) {
                ItemDetailViewModel(
                    itemId = itemId,
                    savedItemRepository = savedItemRepository,
                    collectionRepository = collectionRepository,
                    tagRepository = tagRepository
                )
            }

            ItemDetailScreen(
                viewModel = detailViewModel,
                onBack = { navController.popBackStack() },
                onNavigateToEdit = { navController.navigate("library/item/$itemId/edit") },
                onOpenOriginal = { url -> safeOpenUrl(context, url) {} }
            )
        }

        // Edit Item Screen (Screen 24)
        composable(
            route = "library/item/{itemId}/edit",
            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
            val detailViewModel = remember(itemId) {
                ItemDetailViewModel(
                    itemId = itemId,
                    savedItemRepository = savedItemRepository,
                    collectionRepository = collectionRepository,
                    tagRepository = tagRepository
                )
            }

            EditItemScreen(
                viewModel = detailViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // Collection Detail Screen (Screen 26)
        composable(
            route = "library/collection/{collectionId}",
            arguments = listOf(navArgument("collectionId") { type = NavType.StringType })
        ) { backStackEntry ->
            val collectionId = backStackEntry.arguments?.getString("collectionId") ?: ""
            CollectionDetailScreen(
                collectionId = collectionId,
                viewModel = libraryViewModel,
                onBack = { navController.popBackStack() },
                onNavigateToItemDetail = { itemId -> navController.navigate("library/item/$itemId") },
                onEditCollection = { col ->
                    editingCollection = col
                    showCreateCollectionSheet = true
                }
            )
        }
    }
}

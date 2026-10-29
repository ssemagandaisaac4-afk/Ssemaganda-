package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PlatformMode
import com.example.model.ScreenTab
import com.example.ui.MainViewModel
import com.example.ui.components.AppLogoIcon
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.PackagesScreen
import com.example.ui.screens.ReferralScreen
import com.example.ui.screens.WalletScreen
import com.example.ui.theme.AccentRose
import com.example.ui.theme.AccentRoseDark
import com.example.ui.theme.BrandGreen
import com.example.ui.theme.BrandGreenDark
import com.example.ui.theme.BrandGreenLight
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                ArcheryApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun ArcheryApp(viewModel: MainViewModel) {
    val currentUser by viewModel.currentUser.collectAsState()
    val currentTab by viewModel.currentTab.collectAsState()
    val platformMode by viewModel.platformMode.collectAsState()
    val requests by viewModel.requests.collectAsState()
    val users by viewModel.users.collectAsState()
    val snackbarMsg by viewModel.snackbarMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMsg) {
        snackbarMsg?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackbar()
        }
    }

    // Handle Back Press
    BackHandler(enabled = currentUser != null && (platformMode == PlatformMode.ADMIN || currentTab != ScreenTab.DASHBOARD)) {
        if (platformMode == PlatformMode.ADMIN) {
            viewModel.setPlatformMode(PlatformMode.CLIENT)
        } else {
            viewModel.setTab(ScreenTab.DASHBOARD)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        containerColor = DarkBackground,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    containerColor = DarkSurfaceCard,
                    contentColor = TextPrimary,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .padding(16.dp)
                        .border(1.dp, BrandGreen.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                ) {
                    Text(text = data.visuals.message, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
            }
        },
        topBar = {
            if (currentUser != null) {
                MainAppTopBar(
                    platformMode = platformMode,
                    onSwitchMode = { viewModel.setPlatformMode(it) },
                    onLogout = { viewModel.logout() }
                )
            }
        },
        bottomBar = {
            if (currentUser != null && platformMode == PlatformMode.CLIENT) {
                MainBottomBar(
                    currentTab = currentTab,
                    onSelectTab = { viewModel.setTab(it) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val user = currentUser
            if (user == null) {
                AuthScreen(
                    onLogin = { phone, pin -> viewModel.login(phone, pin) },
                    onRegister = { phone, pin, refCode -> viewModel.register(phone, pin, refCode) }
                )
            } else {
                when (platformMode) {
                    PlatformMode.ADMIN -> {
                        AdminScreen(
                            requests = requests,
                            users = users,
                            onApproveRequest = { viewModel.adminApproveRequest(it) },
                            onRejectRequest = { viewModel.adminRejectRequest(it) },
                            onFixBalance = { phone, bal -> viewModel.adminFixBalance(phone, bal) },
                            onResetData = { viewModel.resetMockData() }
                        )
                    }
                    PlatformMode.CLIENT -> {
                        when (currentTab) {
                            ScreenTab.DASHBOARD -> DashboardScreen(
                                user = user,
                                onNavigateTab = { viewModel.setTab(it) },
                                onSimulateMidnightPayout = { viewModel.simulateMidnightPayout() }
                            )
                            ScreenTab.PACKAGES -> PackagesScreen(
                                user = user,
                                plans = viewModel.packagePlans,
                                onBuyPackage = { viewModel.buyPackage(it) },
                                onNavigateToDeposit = { viewModel.setTab(ScreenTab.WALLET) }
                            )
                            ScreenTab.WALLET -> WalletScreen(
                                user = user,
                                userRequests = requests.filter { it.phone == user.phone },
                                onSubmitDeposit = { amount, provider, phone ->
                                    viewModel.submitDeposit(amount, provider, phone)
                                },
                                onSubmitWithdrawal = { amount, provider, phone ->
                                    viewModel.submitWithdrawal(amount, provider, phone)
                                }
                            )
                            ScreenTab.REFERRALS -> ReferralScreen(
                                user = user,
                                onSimulateReferral = { viewModel.simulateReferral() },
                                onShowToast = { viewModel.showToast(it) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainAppTopBar(
    platformMode: PlatformMode,
    onSwitchMode: (PlatformMode) -> Unit,
    onLogout: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkSurface.copy(alpha = 0.95f))
            .border(1.dp, DarkBorder.copy(alpha = 0.5f))
            .statusBarsPadding()
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Brand Logo & Title
            Row(verticalAlignment = Alignment.CenterVertically) {
                AppLogoIcon(size = 38.dp)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Archery AI Hub",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(BrandGreenLight)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "AI Yield Active",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = BrandGreenLight
                        )
                    }
                }
            }

            // Mode Toggle (Client vs Admin) & Logout
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkBackground)
                        .border(1.dp, DarkBorder, RoundedCornerShape(12.dp))
                        .padding(3.dp)
                ) {
                    Row {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(9.dp))
                                .background(if (platformMode == PlatformMode.CLIENT) BrandGreenDark else Color.Transparent)
                                .clickable { onSwitchMode(PlatformMode.CLIENT) }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .testTag("top_mode_client"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Client",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (platformMode == PlatformMode.CLIENT) Color.White else TextSecondary
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(9.dp))
                                .background(if (platformMode == PlatformMode.ADMIN) AccentRoseDark else Color.Transparent)
                                .clickable { onSwitchMode(PlatformMode.ADMIN) }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .testTag("top_mode_admin"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Admin",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (platformMode == PlatformMode.ADMIN) Color.White else TextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                IconButton(
                    onClick = onLogout,
                    modifier = Modifier.testTag("top_logout_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Sign Out",
                        tint = TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MainBottomBar(
    currentTab: ScreenTab,
    onSelectTab: (ScreenTab) -> Unit
) {
    NavigationBar(
        containerColor = DarkSurface.copy(alpha = 0.95f),
        tonalElevation = 8.dp,
        modifier = Modifier
            .border(1.dp, DarkBorder.copy(alpha = 0.5f))
            .navigationBarsPadding()
    ) {
        val navItems = listOf(
            Triple(ScreenTab.DASHBOARD, "Dashboard", Icons.Default.Dashboard),
            Triple(ScreenTab.PACKAGES, "AI Packages", Icons.Default.Layers),
            Triple(ScreenTab.WALLET, "Wallet", Icons.Default.AccountBalanceWallet),
            Triple(ScreenTab.REFERRALS, "Referrals", Icons.Default.People)
        )

        navItems.forEach { (tab, label, icon) ->
            val isSelected = currentTab == tab
            NavigationBarItem(
                selected = isSelected,
                onClick = { onSelectTab(tab) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = BrandGreenLight,
                    indicatorColor = BrandGreenDark,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary
                ),
                modifier = Modifier.testTag("nav_item_${tab.name.lowercase()}")
            )
        }
    }
}

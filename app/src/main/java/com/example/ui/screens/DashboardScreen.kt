package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.ScreenTab
import com.example.model.User
import com.example.ui.components.GlassCard
import com.example.ui.components.StatCard
import com.example.ui.components.StatusPill
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.AccentRose
import com.example.ui.theme.BrandGreen
import com.example.ui.theme.BrandGreenDark
import com.example.ui.theme.BrandGreenLight
import com.example.ui.theme.CyberBlue
import com.example.ui.theme.CyberIndigo
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

@Composable
fun DashboardScreen(
    user: User,
    onNavigateTab: (ScreenTab) -> Unit,
    onSimulateMidnightPayout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalDailyIncome = user.packages.sumOf { it.dailyIncome }
    val totalInvested = user.packages.sumOf { it.cost }
    var showPayoutDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            // Welcome Member Portal Hero Card with Graphic Art
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = BrandGreen.copy(alpha = 0.4f),
                shape = RoundedCornerShape(26.dp)
            ) {
                Column {
                    // Hero Graphic Art
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.archery_dashboard_hero),
                            contentDescription = "Archery AI Financial Dashboard",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        // Gradient Overlay
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            DarkSurfaceCard.copy(alpha = 0.95f)
                                        )
                                    )
                                )
                        )
                        // Badge in Hero
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(14.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(BrandGreen.copy(alpha = 0.25f))
                                .border(1.dp, BrandGreenLight.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(BrandGreenLight)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "LIVE TRADING NODE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = BrandGreenLight
                                )
                            }
                        }
                    }

                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "Welcome back, ${user.phone}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Your AI Archery Trading robot is active and generating automated daily cash backs.",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            lineHeight = 17.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Simulate Midnight button
                            ElevatedButton(
                                onClick = {
                                    if (user.packages.isNotEmpty()) {
                                        showPayoutDialog = true
                                    }
                                    onSimulateMidnightPayout()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("simulate_midnight_button"),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.elevatedButtonColors(
                                    containerColor = AccentAmber,
                                    contentColor = Color.Black
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.HourglassBottom,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "12 AM Top-up",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            // Deposit Funds
                            ElevatedButton(
                                onClick = { onNavigateTab(ScreenTab.WALLET) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("dashboard_deposit_button"),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.elevatedButtonColors(
                                    containerColor = BrandGreenDark,
                                    contentColor = Color.White
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AddCircle,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Deposit Funds",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // 4 Stat Cards in 2x2 grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Account Balance",
                    value = "UGX " + String.format(Locale.US, "%,.0f", user.balance),
                    subtitle = "Includes Welcome Bonus",
                    icon = Icons.Default.AccountBalanceWallet,
                    iconTint = BrandGreenLight,
                    iconBg = BrandGreen.copy(alpha = 0.15f),
                    modifier = Modifier.weight(1f)
                )

                StatCard(
                    title = "Est. Daily Income",
                    value = "UGX " + String.format(Locale.US, "%,.0f", totalDailyIncome),
                    subtitle = "Paid @ 12:00 AM",
                    icon = Icons.Default.ShowChart,
                    iconTint = CyberPurple,
                    iconBg = CyberPurple.copy(alpha = 0.15f),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Active AI Items",
                    value = "${user.packages.size} Packages",
                    subtitle = "Invested: UGX " + String.format(Locale.US, "%,.0f", totalInvested),
                    icon = Icons.Default.Layers,
                    iconTint = CyberBlue,
                    iconBg = CyberBlue.copy(alpha = 0.15f),
                    modifier = Modifier.weight(1f)
                )

                StatCard(
                    title = "Referral Earnings",
                    value = "UGX " + String.format(Locale.US, "%,.0f", user.refEarned),
                    subtitle = "${user.refCount} Invited Friends",
                    icon = Icons.Default.People,
                    iconTint = AccentAmber,
                    iconBg = AccentAmber.copy(alpha = 0.15f),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Active Investment Items Section
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Your Active Investment Items",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Generates daily automated cash back to balance",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onNavigateTab(ScreenTab.PACKAGES) }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Buy Items",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = BrandGreenLight
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = BrandGreenLight,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    if (user.packages.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, DarkBorder, RoundedCornerShape(16.dp))
                                .padding(vertical = 24.dp, horizontal = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.Bolt,
                                    contentDescription = null,
                                    tint = TextMuted,
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "No active AI packages yet.",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = TextSecondary
                                )
                                Text(
                                    text = "Purchase from the AI Packages tab to start daily yield!",
                                    fontSize = 11.sp,
                                    color = TextMuted,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            user.packages.forEach { pkg ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(DarkSurface)
                                        .border(1.dp, BrandGreen.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
                                        .padding(14.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = pkg.name,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextPrimary
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = "+UGX ${String.format(Locale.US, "%,.0f", pkg.dailyIncome)} / day",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = BrandGreenLight
                                            )
                                        }
                                        Text(
                                            text = pkg.purchasedAt,
                                            fontSize = 11.sp,
                                            color = TextMuted
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Recent Account Activity Section
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Recent Account Activity",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    if (user.transactions.isEmpty()) {
                        Text(
                            text = "No transactions recorded yet.",
                            fontSize = 12.sp,
                            color = TextMuted,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            user.transactions.take(10).forEach { tx ->
                                val isPositive = tx.amount >= 0
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(DarkSurface.copy(alpha = 0.6f))
                                        .padding(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = tx.type,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = TextPrimary
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                StatusPill(text = tx.status, type = tx.status)
                                            }
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = tx.desc,
                                                fontSize = 11.sp,
                                                color = TextSecondary
                                            )
                                            Text(
                                                text = tx.date,
                                                fontSize = 10.sp,
                                                color = TextMuted
                                            )
                                        }

                                        Text(
                                            text = (if (isPositive) "+" else "") + "UGX " + String.format(Locale.US, "%,.0f", kotlin.math.abs(tx.amount)),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Black,
                                            color = if (isPositive) BrandGreenLight else AccentRose
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Payout Breakdown Modal
    if (showPayoutDialog && user.packages.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = { showPayoutDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = BrandGreenLight,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "12:00 AM Payout Credited!",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Your active trading packages have yielded automated profits credited directly to your balance:",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    user.packages.forEach { pkg ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(DarkBackground)
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = pkg.name, fontSize = 12.sp, color = TextPrimary)
                            Text(
                                text = "+UGX " + String.format(Locale.US, "%,.0f", pkg.dailyIncome),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = BrandGreenLight
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(DarkBorder)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Total Payout Credited:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(
                            text = "+UGX " + String.format(Locale.US, "%,.0f", totalDailyIncome),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = BrandGreenLight
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showPayoutDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandGreenDark)
                ) {
                    Text("Awesome!", color = Color.White)
                }
            },
            containerColor = DarkSurfaceCard
        )
    }
}

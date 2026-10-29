package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Stars
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PackagePlan
import com.example.model.User
import com.example.ui.components.GlassCard
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.AccentOrange
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
fun PackagesScreen(
    user: User,
    plans: List<PackagePlan>,
    onBuyPackage: (PackagePlan) -> Unit,
    onNavigateToDeposit: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedPlanForConfirmation by remember { mutableStateOf<PackagePlan?>(null) }
    var insufficientBalancePlan by remember { mutableStateOf<PackagePlan?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(100.dp))
                        .background(BrandGreen.copy(alpha = 0.12f))
                        .border(1.dp, BrandGreen.copy(alpha = 0.3f), RoundedCornerShape(100.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "ARCHERY AI HIGH-YIELD PACKAGES",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = BrandGreenLight,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Purchase AI Trading Bot Packages",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Invest from UGX 20,000 and start earning daily automatic profits credited every night at 12:00 AM.",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(12.dp))
                // Balance indicator
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkSurface)
                        .border(1.dp, DarkBorder, RoundedCornerShape(12.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Available Balance: UGX " + String.format(Locale.US, "%,.0f", user.balance),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandGreenLight
                    )
                }
            }
        }

        items(plans) { plan ->
            val iconVector = when (plan.tier) {
                1 -> Icons.Default.Bolt
                2 -> Icons.Default.ElectricBolt
                3 -> Icons.Default.Memory
                4 -> Icons.Default.Psychology
                else -> Icons.Default.Stars
            }

            val iconGradient = when (plan.tier) {
                1 -> Brush.linearGradient(listOf(BrandGreenDark, BrandGreenLight))
                2 -> Brush.linearGradient(listOf(CyberBlue, CyberIndigo))
                3 -> Brush.linearGradient(listOf(CyberPurple, CyberIndigo))
                4 -> Brush.linearGradient(listOf(Color(0xFFE11D48), Color(0xFFF43F5E)))
                else -> Brush.linearGradient(listOf(AccentAmber, AccentOrange))
            }

            val cardBorderColor = if (plan.isBestYield) AccentAmber.copy(alpha = 0.6f) else DarkBorder

            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = cardBorderColor,
                borderWidth = if (plan.isBestYield) 1.5.dp else 1.dp,
                shape = RoundedCornerShape(24.dp),
                backgroundColor = if (plan.isBestYield) DarkSurfaceCard.copy(alpha = 0.95f) else DarkSurfaceCard.copy(alpha = 0.85f)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(iconGradient),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = iconVector,
                                contentDescription = null,
                                tint = if (plan.isBestYield) Color.Black else Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        if (plan.badge != null) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(100.dp))
                                    .background(if (plan.isBestYield) AccentAmber.copy(alpha = 0.2f) else BrandGreen.copy(alpha = 0.15f))
                                    .border(
                                        1.dp,
                                        if (plan.isBestYield) AccentAmber.copy(alpha = 0.4f) else BrandGreen.copy(alpha = 0.3f),
                                        RoundedCornerShape(100.dp)
                                    )
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = plan.badge,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (plan.isBestYield) AccentAmber else BrandGreenLight
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = plan.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = plan.description,
                        fontSize = 12.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Details box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(DarkSurface)
                            .border(1.dp, DarkBorder, RoundedCornerShape(16.dp))
                            .padding(14.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Package Cost:", fontSize = 13.sp, color = TextSecondary)
                                Text(
                                    text = "UGX " + String.format(Locale.US, "%,.0f", plan.cost),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (plan.isBestYield) AccentAmber else TextPrimary
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Daily Profit:", fontSize = 13.sp, color = TextSecondary)
                                Text(
                                    text = "+UGX " + String.format(Locale.US, "%,.0f", plan.dailyIncome) + " / day",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = BrandGreenLight
                                )
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
                                Text(text = "Payout Time:", fontSize = 11.sp, color = TextMuted)
                                Text(text = "Daily @ 12:00 AM", fontSize = 11.sp, color = TextSecondary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Purchase Action Button
                    ElevatedButton(
                        onClick = {
                            if (user.balance < plan.cost) {
                                insufficientBalancePlan = plan
                            } else {
                                selectedPlanForConfirmation = plan
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("buy_package_tier_${plan.tier}"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = if (plan.isBestYield) AccentAmber else BrandGreenDark,
                            contentColor = if (plan.isBestYield) Color.Black else Color.White
                        )
                    ) {
                        Text(
                            text = if (plan.isBestYield) {
                                "Buy Ultra Package (UGX ${String.format(Locale.US, "%,.0f", plan.cost)})"
                            } else {
                                "Buy Package (UGX ${String.format(Locale.US, "%,.0f", plan.cost)})"
                            },
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Purchase Confirmation Dialog
    if (selectedPlanForConfirmation != null) {
        val plan = selectedPlanForConfirmation!!
        val remainingBalance = user.balance - plan.cost
        val projectedMonthly = plan.dailyIncome * 30

        AlertDialog(
            onDismissRequest = { selectedPlanForConfirmation = null },
            title = {
                Text(
                    text = "Confirm Purchase: ${plan.name}",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Are you sure you want to activate this AI trading robot package?",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(DarkBackground)
                            .padding(12.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Package Cost:", fontSize = 12.sp, color = TextSecondary)
                                Text(
                                    text = "-UGX " + String.format(Locale.US, "%,.0f", plan.cost),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentRose
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Daily Automated Return:", fontSize = 12.sp, color = TextSecondary)
                                Text(
                                    text = "+UGX " + String.format(Locale.US, "%,.0f", plan.dailyIncome) + "/day",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BrandGreenLight
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "30-Day Projected Yield:", fontSize = 12.sp, color = TextSecondary)
                                Text(
                                    text = "UGX " + String.format(Locale.US, "%,.0f", projectedMonthly),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = BrandGreenLight
                                )
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
                                Text(text = "Balance After Purchase:", fontSize = 12.sp, color = TextSecondary)
                                Text(
                                    text = "UGX " + String.format(Locale.US, "%,.0f", remainingBalance),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onBuyPackage(plan)
                        selectedPlanForConfirmation = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandGreenDark)
                ) {
                    Text("Confirm & Buy", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedPlanForConfirmation = null }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = DarkSurfaceCard
        )
    }

    // Insufficient Balance Dialog with direct Deposit Button
    if (insufficientBalancePlan != null) {
        val plan = insufficientBalancePlan!!
        val needed = plan.cost - user.balance

        AlertDialog(
            onDismissRequest = { insufficientBalancePlan = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = AccentAmber,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Insufficient Balance",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "You need UGX ${String.format(Locale.US, "%,.0f", plan.cost)} to purchase ${plan.name}.",
                        fontSize = 13.sp,
                        color = TextPrimary
                    )
                    Text(
                        text = "Your current balance is UGX ${String.format(Locale.US, "%,.0f", user.balance)}. You need an additional UGX ${String.format(Locale.US, "%,.0f", needed)}.",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        insufficientBalancePlan = null
                        onNavigateToDeposit()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandGreenDark)
                ) {
                    Icon(Icons.Default.AddCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Deposit Funds Now", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { insufficientBalancePlan = null }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = DarkSurfaceCard
        )
    }
}

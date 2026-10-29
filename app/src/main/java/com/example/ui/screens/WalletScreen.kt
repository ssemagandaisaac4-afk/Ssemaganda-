package com.example.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FinancialRequest
import com.example.model.User
import com.example.ui.components.GlassCard
import com.example.ui.components.StatusPill
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.AccentRose
import com.example.ui.theme.AccentRoseDark
import com.example.ui.theme.BrandGreen
import com.example.ui.theme.BrandGreenDark
import com.example.ui.theme.BrandGreenLight
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(
    user: User,
    userRequests: List<FinancialRequest> = emptyList(),
    onSubmitDeposit: (Double, String, String) -> Unit,
    onSubmitWithdrawal: (Double, String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var depositAmount by remember { mutableStateOf("20000") }
    var depositProvider by remember { mutableStateOf("MTN Mobile Money") }
    var depositPhone by remember { mutableStateOf(user.phone) }
    var depositDropdownExpanded by remember { mutableStateOf(false) }

    var withdrawAmount by remember { mutableStateOf("5000") }
    var withdrawProvider by remember { mutableStateOf("MTN Mobile Money") }
    var withdrawPhone by remember { mutableStateOf(user.phone) }
    var withdrawDropdownExpanded by remember { mutableStateOf(false) }

    val providers = listOf("MTN Mobile Money", "Airtel Money")

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Balance Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(DarkSurface)
                .border(1.dp, BrandGreen.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "CURRENT BALANCE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                    Text(
                        text = "UGX " + String.format(Locale.US, "%,.0f", user.balance),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = BrandGreenLight
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(BrandGreen.copy(alpha = 0.15f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(text = "Uganda Shillings (UGX)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BrandGreenLight)
                }
            }
        }

        // DEPOSIT CARD
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = BrandGreen.copy(alpha = 0.35f),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(BrandGreen.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowDownward,
                            contentDescription = null,
                            tint = BrandGreenLight,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = "Deposit Funds", fontSize = 18.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                        Text(
                            text = "Min deposit: UGX 20,000",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = BrandGreenLight
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Quick preset amounts
                Text(text = "SELECT AMOUNT", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("20000", "30000", "50000", "70000").forEach { preset ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (depositAmount == preset) BrandGreenDark else DarkSurface)
                                .border(1.dp, if (depositAmount == preset) BrandGreenLight else DarkBorder, RoundedCornerShape(10.dp))
                                .clickable { depositAmount = preset }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${preset.toInt() / 1000}k",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (depositAmount == preset) Color.White else TextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Custom amount field
                Text(text = "DEPOSIT AMOUNT (UGX)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = depositAmount,
                    onValueChange = { if (it.all { char -> char.isDigit() }) depositAmount = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("deposit_amount_input"),
                    placeholder = { Text("Minimum 20000", color = TextMuted) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrandGreen,
                        unfocusedBorderColor = DarkBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = DarkSurface,
                        unfocusedContainerColor = DarkSurface
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Provider dropdown
                Text(text = "MOBILE MONEY PROVIDER", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                Spacer(modifier = Modifier.height(4.dp))
                ExposedDropdownMenuBox(
                    expanded = depositDropdownExpanded,
                    onExpandedChange = { depositDropdownExpanded = !depositDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = depositProvider,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = depositDropdownExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrandGreen,
                            unfocusedBorderColor = DarkBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = DarkSurface,
                            unfocusedContainerColor = DarkSurface
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = depositDropdownExpanded,
                        onDismissRequest = { depositDropdownExpanded = false },
                        modifier = Modifier.background(DarkSurface)
                    ) {
                        providers.forEach { provider ->
                            DropdownMenuItem(
                                text = { Text(provider, color = TextPrimary) },
                                onClick = {
                                    depositProvider = provider
                                    depositDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Phone number
                Text(text = "PAYMENT PHONE NUMBER", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = depositPhone,
                    onValueChange = { depositPhone = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("deposit_phone_input"),
                    placeholder = { Text("0770000000", color = TextMuted) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrandGreen,
                        unfocusedBorderColor = DarkBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = DarkSurface,
                        unfocusedContainerColor = DarkSurface
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Instructions Box with USSD guide
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkSurface)
                        .padding(12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(text = "Mobile Money Instructions:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(text = "• MTN Uganda dial: *165# to approve transfer prompt.", fontSize = 11.sp, color = TextSecondary)
                        Text(text = "• Airtel Uganda dial: *185# to approve transfer prompt.", fontSize = 11.sp, color = TextSecondary)
                        Text(text = "• Submit request below to notify Admin for instant top-up.", fontSize = 11.sp, color = BrandGreenLight)
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                ElevatedButton(
                    onClick = {
                        val amt = depositAmount.toDoubleOrNull() ?: 0.0
                        onSubmitDeposit(amt, depositProvider, depositPhone)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("submit_deposit_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = BrandGreenDark,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Submit Deposit Request",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }

        // WITHDRAW CARD
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = AccentRose.copy(alpha = 0.35f),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(AccentRose.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowUpward,
                            contentDescription = null,
                            tint = AccentRose,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = "Withdraw Cash", fontSize = 18.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                        Text(
                            text = "Min withdrawal: UGX 5,000",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AccentRose
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Custom amount field
                Text(text = "WITHDRAWAL AMOUNT (UGX)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = withdrawAmount,
                    onValueChange = { if (it.all { char -> char.isDigit() }) withdrawAmount = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("withdraw_amount_input"),
                    placeholder = { Text("Minimum 5000", color = TextMuted) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentRose,
                        unfocusedBorderColor = DarkBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = DarkSurface,
                        unfocusedContainerColor = DarkSurface
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Provider dropdown
                Text(text = "TARGET MOBILE WALLET", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                Spacer(modifier = Modifier.height(4.dp))
                ExposedDropdownMenuBox(
                    expanded = withdrawDropdownExpanded,
                    onExpandedChange = { withdrawDropdownExpanded = !withdrawDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = withdrawProvider,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = withdrawDropdownExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AccentRose,
                            unfocusedBorderColor = DarkBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = DarkSurface,
                            unfocusedContainerColor = DarkSurface
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = withdrawDropdownExpanded,
                        onDismissRequest = { withdrawDropdownExpanded = false },
                        modifier = Modifier.background(DarkSurface)
                    ) {
                        providers.forEach { provider ->
                            DropdownMenuItem(
                                text = { Text(provider, color = TextPrimary) },
                                onClick = {
                                    withdrawProvider = provider
                                    withdrawDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Phone number
                Text(text = "RECEIVING MOBILE NUMBER", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = withdrawPhone,
                    onValueChange = { withdrawPhone = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("withdraw_phone_input"),
                    placeholder = { Text("0770000000", color = TextMuted) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentRose,
                        unfocusedBorderColor = DarkBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = DarkSurface,
                        unfocusedContainerColor = DarkSurface
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Notice Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkSurface)
                        .padding(12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(text = "Notice:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(text = "Withdrawals require Admin verification (ssemagandaisaac4@gmail.com) to prevent fraud.", fontSize = 11.sp, color = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                ElevatedButton(
                    onClick = {
                        val amt = withdrawAmount.toDoubleOrNull() ?: 0.0
                        onSubmitWithdrawal(amt, withdrawProvider, withdrawPhone)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("submit_withdrawal_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = AccentRoseDark,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Request Withdrawal",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }

        // USER FINANCIAL REQUESTS HISTORY
        if (userRequests.isNotEmpty()) {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            tint = BrandGreenLight,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Your Submitted Requests",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        userRequests.forEach { req ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(DarkSurface)
                                    .padding(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = req.type,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextPrimary
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            StatusPill(text = req.status, type = req.status)
                                        }
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(text = req.provider, fontSize = 11.sp, color = TextSecondary)
                                    }

                                    Text(
                                        text = "UGX " + String.format(Locale.US, "%,.0f", req.amount),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Black,
                                        color = if (req.type == "Deposit") BrandGreenLight else AccentRose
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

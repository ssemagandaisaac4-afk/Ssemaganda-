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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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

@Composable
fun AdminScreen(
    requests: List<FinancialRequest>,
    users: Map<String, User>,
    onApproveRequest: (String) -> Unit,
    onRejectRequest: (String) -> Unit,
    onFixBalance: (String, Double) -> Unit,
    onResetData: () -> Unit,
    modifier: Modifier = Modifier
) {
    var editingUserPhone by remember { mutableStateOf<String?>(null) }
    var newBalanceInput by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    var memberSearchQuery by remember { mutableStateOf("") }

    val pendingCount = requests.count { it.status == "Pending" }

    val filteredRequests = when (selectedFilter) {
        "Pending" -> requests.filter { it.status == "Pending" }
        "Approved" -> requests.filter { it.status == "Approved" }
        "Rejected" -> requests.filter { it.status == "Rejected" }
        else -> requests
    }

    val filteredUsers = users.values.filter {
        it.phone.contains(memberSearchQuery, ignoreCase = true)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            // Admin Executive Header Card
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = AccentRose.copy(alpha = 0.4f),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = AccentRose,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "SUPER ADMIN PANEL",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = AccentRose,
                                letterSpacing = 1.sp
                            )
                        }

                        OutlinedButton(
                            onClick = onResetData,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("admin_reset_data_button"),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Reset System", fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Archery AI Hub Executive Control",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Support Email: ssemagandaisaac4@gmail.com",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        // PENDING FINANCIAL REQUESTS SECTION
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
                        Text(
                            text = "Financial Requests",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(100.dp))
                                .background(AccentAmber.copy(alpha = 0.2f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "$pendingCount Pending",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AccentAmber
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Filter Chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("All", "Pending", "Approved", "Rejected").forEach { filter ->
                            val isSelected = selectedFilter == filter
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) BrandGreenDark else DarkSurface)
                                    .clickable { selectedFilter = filter }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = filter,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else TextSecondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    if (filteredRequests.isEmpty()) {
                        Text(
                            text = "No requests found for filter: $selectedFilter.",
                            fontSize = 12.sp,
                            color = TextMuted,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            filteredRequests.forEach { req ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(DarkSurface)
                                        .border(1.dp, DarkBorder, RoundedCornerShape(14.dp))
                                        .padding(14.dp)
                                ) {
                                    Column {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = req.phone,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = TextPrimary
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                StatusPill(text = req.type, type = req.type)
                                            }

                                            Text(
                                                text = "UGX " + String.format(Locale.US, "%,.0f", req.amount),
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Black,
                                                color = if (req.type == "Deposit") BrandGreenLight else AccentRose
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(text = req.provider, fontSize = 11.sp, color = TextSecondary)

                                        Spacer(modifier = Modifier.height(10.dp))

                                        if (req.status == "Pending") {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                ElevatedButton(
                                                    onClick = { onApproveRequest(req.id) },
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .testTag("admin_approve_${req.id}"),
                                                    shape = RoundedCornerShape(10.dp),
                                                    colors = ButtonDefaults.elevatedButtonColors(
                                                        containerColor = BrandGreenDark,
                                                        contentColor = Color.White
                                                    )
                                                ) {
                                                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(text = "Approve", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                                }

                                                ElevatedButton(
                                                    onClick = { onRejectRequest(req.id) },
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .testTag("admin_reject_${req.id}"),
                                                    shape = RoundedCornerShape(10.dp),
                                                    colors = ButtonDefaults.elevatedButtonColors(
                                                        containerColor = AccentRoseDark,
                                                        contentColor = Color.White
                                                    )
                                                ) {
                                                    Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(14.dp))
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(text = "Reject", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                                }
                                            }
                                        } else {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.End
                                            ) {
                                                StatusPill(text = req.status, type = req.status)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // MEMBER ACCOUNT DIRECTORY & BALANCE OVERRIDE
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Member Directory & Balance Override",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "View all accounts, override balance, or credit test deposits",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Member Search Field
                    OutlinedTextField(
                        value = memberSearchQuery,
                        onValueChange = { memberSearchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Search by phone number...", color = TextMuted, fontSize = 12.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(18.dp)) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
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

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        filteredUsers.forEach { member ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(DarkSurface)
                                    .border(1.dp, DarkBorder, RoundedCornerShape(14.dp))
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = member.phone,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextPrimary
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = "UGX " + String.format(Locale.US, "%,.0f", member.balance),
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Black,
                                                color = BrandGreenLight
                                            )
                                            Text(
                                                text = "${member.packages.size} Active Items",
                                                fontSize = 11.sp,
                                                color = TextMuted
                                            )
                                        }

                                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                            // Quick +50k
                                            OutlinedButton(
                                                onClick = {
                                                    onFixBalance(member.phone, member.balance + 50000.0)
                                                },
                                                shape = RoundedCornerShape(8.dp),
                                                modifier = Modifier.testTag("quick_add_50k_${member.phone}"),
                                                colors = ButtonDefaults.outlinedButtonColors(
                                                    contentColor = AccentAmber
                                                )
                                            ) {
                                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(12.dp))
                                                Text(text = "50k", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            }

                                            OutlinedButton(
                                                onClick = {
                                                    editingUserPhone = member.phone
                                                    newBalanceInput = member.balance.toInt().toString()
                                                },
                                                shape = RoundedCornerShape(8.dp),
                                                modifier = Modifier.testTag("fix_balance_${member.phone}"),
                                                colors = ButtonDefaults.outlinedButtonColors(
                                                    contentColor = BrandGreenLight
                                                )
                                            ) {
                                                Icon(Icons.Default.Build, contentDescription = null, modifier = Modifier.size(12.dp))
                                                Spacer(modifier = Modifier.width(3.dp))
                                                Text(text = "Edit", fontSize = 11.sp)
                                            }
                                        }
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

    // Balance Override Dialog
    if (editingUserPhone != null) {
        AlertDialog(
            onDismissRequest = { editingUserPhone = null },
            title = {
                Text(
                    text = "Adjust Balance for $editingUserPhone",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            },
            text = {
                Column {
                    Text(
                        text = "Enter new balance in UGX. This will update the user's available funds directly.",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = newBalanceInput,
                        onValueChange = { if (it.all { c -> c.isDigit() }) newBalanceInput = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_new_balance_input"),
                        label = { Text("New Balance (UGX)") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrandGreen,
                            unfocusedBorderColor = DarkBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = DarkSurface,
                            unfocusedContainerColor = DarkSurface
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amount = newBalanceInput.toDoubleOrNull()
                        if (amount != null && editingUserPhone != null) {
                            onFixBalance(editingUserPhone!!, amount)
                        }
                        editingUserPhone = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandGreenDark)
                ) {
                    Text("Save Balance", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { editingUserPhone = null }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = DarkSurface
        )
    }
}

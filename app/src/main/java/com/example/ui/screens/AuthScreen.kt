package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AppLogoIcon
import com.example.ui.components.GlassCard
import com.example.ui.components.GradientButton
import com.example.ui.theme.AccentAmber
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

@Composable
fun AuthScreen(
    onLogin: (String, String) -> Unit,
    onRegister: (String, String, String?) -> Unit,
    onUnlockAdmin: (String) -> Boolean = { false },
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isRegisterTab by remember { mutableStateOf(false) }
    var phoneInput by remember { mutableStateOf("") }
    var pinInput by remember { mutableStateOf("") }
    var refCodeInput by remember { mutableStateOf("") }
    var pinVisible by remember { mutableStateOf(false) }
    var showAdminDialog by remember { mutableStateOf(false) }
    var adminPinInput by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Glowing decorative ambient orbs
        Box(
            modifier = Modifier
                .size(240.dp)
                .align(Alignment.TopStart)
                .background(
                    Brush.radialGradient(
                        colors = listOf(BrandGreen.copy(alpha = 0.18f), Color.Transparent)
                    ),
                    shape = CircleShape
                )
        )
        Box(
            modifier = Modifier
                .size(240.dp)
                .align(Alignment.BottomEnd)
                .background(
                    Brush.radialGradient(
                        colors = listOf(CyberPurple.copy(alpha = 0.18f), Color.Transparent)
                    ),
                    shape = CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Header Logo & Branding
            AppLogoIcon(size = 72.dp)
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Archery AI Hub",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = TextPrimary
            )
            Text(
                text = "Smart Automated Yields & Yield Optimization",
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )

            // New User Bonus Banner
            Spacer(modifier = Modifier.height(14.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .background(BrandGreen.copy(alpha = 0.12f))
                    .border(1.dp, BrandGreen.copy(alpha = 0.35f), RoundedCornerShape(100.dp))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🎁", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "New User Bonus: ",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary
                    )
                    Text(
                        text = "UGX 2,000 Free",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = BrandGreenLight
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Main Glass Card for Auth
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = DarkBorder.copy(alpha = 0.8f),
                shape = RoundedCornerShape(28.dp)
            ) {
                Column(modifier = Modifier.padding(22.dp)) {
                    // Tab Switcher
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(DarkSurface)
                            .padding(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (!isRegisterTab) BrandGreenDark else Color.Transparent)
                                .clickable { isRegisterTab = false }
                                .padding(vertical = 10.dp)
                                .testTag("tab_login"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Sign In",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (!isRegisterTab) Color.White else TextSecondary
                            )
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isRegisterTab) BrandGreenDark else Color.Transparent)
                                .clickable { isRegisterTab = true }
                                .padding(vertical = 10.dp)
                                .testTag("tab_register"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Register",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isRegisterTab) Color.White else TextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Phone Number Input
                    Text(
                        text = "PHONE NUMBER",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = phoneInput,
                        onValueChange = { phoneInput = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_phone_input"),
                        placeholder = { Text("770000000", color = TextMuted, fontSize = 14.sp) },
                        leadingIcon = {
                            Text(
                                text = "+256",
                                color = BrandGreenLight,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(start = 12.dp, end = 4.dp)
                            )
                        },
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
                    Text(
                        text = "Enter active MTN or Airtel Uganda phone number",
                        fontSize = 11.sp,
                        color = TextMuted,
                        modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 5-Digit PIN Input
                    Text(
                        text = "5-DIGIT PASSWORD / PIN",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = pinInput,
                        onValueChange = { if (it.length <= 5 && it.all { char -> char.isDigit() }) pinInput = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_pin_input"),
                        placeholder = { Text("•••••", color = TextMuted, fontSize = 14.sp) },
                        visualTransformation = if (pinVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { pinVisible = !pinVisible }) {
                                Icon(
                                    imageVector = if (pinVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Toggle PIN visibility",
                                    tint = TextSecondary
                                )
                            }
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
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
                    Text(
                        text = "Must be exactly 5 numeric digits (e.g., 12345)",
                        fontSize = 11.sp,
                        color = TextMuted,
                        modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                    )

                    // Optional Referral Code
                    if (isRegisterTab) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "REFERRAL CODE (OPTIONAL)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = refCodeInput,
                            onValueChange = { refCodeInput = it.uppercase() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("auth_ref_code_input"),
                            placeholder = { Text("ARCH-XXXX", color = TextMuted, fontSize = 14.sp) },
                            singleLine = true,
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
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Submit Action Button
                    GradientButton(
                        onClick = {
                            if (isRegisterTab) {
                                onRegister(phoneInput, pinInput, refCodeInput.ifBlank { null })
                            } else {
                                onLogin(phoneInput, pinInput)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_submit_button"),
                        gradient = Brush.horizontalGradient(listOf(BrandGreenDark, CyberBlue))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = if (isRegisterTab) "Register & Claim UGX 2,000" else "Access Dashboard",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = if (isRegisterTab) Icons.Default.CardGiftcard else Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // Demo Helper Quick Fill
                    Spacer(modifier = Modifier.height(14.dp))
                    OutlinedButton(
                        onClick = {
                            phoneInput = "770000000"
                            pinInput = "12345"
                            isRegisterTab = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_demo_fill_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = TextSecondary
                        )
                    ) {
                        Text(
                            text = "⚡ Fill Demo Credentials (+256770000000 / 12345)",
                            fontSize = 11.sp
                        )
                    }
                }
            }

            // Support Email Footer
            Spacer(modifier = Modifier.height(28.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Need help? Contact support: ",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
                Text(
                    text = "ssemagandaisaac4@gmail.com",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandGreenLight,
                    modifier = Modifier.clickable {
                        try {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:ssemagandaisaac4@gmail.com")
                                putExtra(Intent.EXTRA_SUBJECT, "Archery AI Hub Support Request")
                            }
                            context.startActivity(intent)
                        } catch (_: Exception) {}
                    }
                )
            }

            // Discreet Executive Admin Department Entry
            Spacer(modifier = Modifier.height(18.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        adminPinInput = ""
                        showAdminDialog = true
                    }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
                    .testTag("admin_portal_entrance")
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Executive Admin Department",
                    fontSize = 11.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Admin Access Security Dialog
        if (showAdminDialog) {
            AlertDialog(
                onDismissRequest = { showAdminDialog = false },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = Color(0xFFF43F5E),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Admin Department Access",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Restricted to authorized executive administrators (ssemagandaisaac4@gmail.com).",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = adminPinInput,
                            onValueChange = { if (it.length <= 8) adminPinInput = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("admin_secret_pin_input"),
                            label = { Text("Master Admin Security PIN") },
                            placeholder = { Text("Enter 5-digit master PIN") },
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFF43F5E),
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
                            val success = onUnlockAdmin(adminPinInput)
                            if (success) {
                                showAdminDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE11D48)),
                        modifier = Modifier.testTag("admin_unlock_button")
                    ) {
                        Text("Verify & Unlock", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAdminDialog = false }) {
                        Text("Cancel", color = TextSecondary)
                    }
                },
                containerColor = DarkSurfaceCard
            )
        }
    }
}

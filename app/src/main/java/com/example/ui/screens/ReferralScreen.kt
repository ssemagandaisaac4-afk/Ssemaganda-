package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.User
import com.example.ui.components.GlassCard
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.BrandGreenDark
import com.example.ui.theme.BrandGreenLight
import com.example.ui.theme.CyberIndigo
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

@Composable
fun ReferralScreen(
    user: User,
    onSimulateReferral: () -> Unit,
    onShowToast: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val referralLink = "https://archeryai.hub/ref?user=" + user.phone.replace("+", "")

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = CyberPurple.copy(alpha = 0.4f),
            shape = RoundedCornerShape(26.dp)
        ) {
            Column {
                // Banner Graphic
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.archery_referral_reward),
                        contentDescription = "Referral Commission Chest",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color.Transparent,
                                        DarkSurfaceCard.copy(alpha = 0.95f)
                                    )
                                )
                            )
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(14.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(AccentAmber.copy(alpha = 0.25f))
                            .border(1.dp, AccentAmber.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = AccentAmber, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "10% INSTANT CASH BACK",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = AccentAmber
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Invite Friends & Earn Commission",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Share your unique Archery AI Hub link with friends. Get instant bonuses when they register and activate packages!",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Referral Link Input Box
                    OutlinedTextField(
                        value = referralLink,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("referral_link_field"),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberPurple,
                            unfocusedBorderColor = DarkBorder,
                            focusedTextColor = BrandGreenLight,
                            unfocusedTextColor = BrandGreenLight,
                            focusedContainerColor = DarkSurface,
                            unfocusedContainerColor = DarkSurface
                        ),
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Actions: Copy Link & Share Sheet
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ElevatedButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Archery AI Referral Link", referralLink)
                                clipboard.setPrimaryClip(clip)
                                onShowToast("Referral link copied to clipboard!")
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("copy_referral_link_button"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = BrandGreenDark,
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Copy Link", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        ElevatedButton(
                            onClick = {
                                val sendIntent: Intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "Join Archery AI Hub and claim your free UGX 2,000 welcome bonus! Start earning daily automated yields: $referralLink")
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, "Share Archery AI Hub")
                            context.startActivity(shareIntent)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("share_referral_link_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = CyberIndigo,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Share", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 3 Stats Boxes
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(DarkSurface)
                            .border(1.dp, DarkBorder, RoundedCornerShape(16.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Text(text = "Invited Friends", fontSize = 10.sp, color = TextSecondary, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "${user.refCount}", fontSize = 18.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(DarkSurface)
                            .border(1.dp, DarkBorder, RoundedCornerShape(16.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Text(text = "Commission", fontSize = 10.sp, color = TextSecondary, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "10% Cash", fontSize = 16.sp, fontWeight = FontWeight.Black, color = BrandGreenLight)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(DarkSurface)
                            .border(1.dp, DarkBorder, RoundedCornerShape(16.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Text(text = "Total Earned", fontSize = 10.sp, color = TextSecondary, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "UGX " + String.format(Locale.US, "%,.0f", user.refEarned),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = AccentAmber
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Simulation action for testing
                ElevatedButton(
                    onClick = onSimulateReferral,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("simulate_referral_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = AccentAmber,
                        contentColor = Color.Black
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "⚡ Simulate Friend Joining (+UGX 2,000 Commission)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    // Commission Rules Info Card
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Archery Affiliate Terms & Rules", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Row(verticalAlignment = Alignment.Top) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BrandGreenLight, modifier = Modifier.size(16.dp).padding(top = 2.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "You receive 10% instant cash commission on any deposit package your invitee activates.", fontSize = 11.sp, color = TextSecondary)
            }
            Row(verticalAlignment = Alignment.Top) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BrandGreenLight, modifier = Modifier.size(16.dp).padding(top = 2.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Commissions are credited directly to your account balance and are immediately withdrawable.", fontSize = 11.sp, color = TextSecondary)
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}
}

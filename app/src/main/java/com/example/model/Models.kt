package com.example.model

data class User(
    val phone: String,
    val pin: String,
    val balance: Double = 2000.0,
    val packages: List<PackageItem> = emptyList(),
    val refEarned: Double = 0.0,
    val refCount: Int = 0,
    val transactions: List<TransactionItem> = emptyList()
)

data class PackageItem(
    val id: String,
    val name: String,
    val cost: Double,
    val dailyIncome: Double,
    val purchasedAt: String
)

data class PackagePlan(
    val tier: Int,
    val name: String,
    val description: String,
    val cost: Double,
    val dailyIncome: Double,
    val badge: String? = null,
    val isBestYield: Boolean = false
)

data class TransactionItem(
    val id: String,
    val type: String, // Bonus, Purchase, Payout, Deposit, Withdrawal, Admin Adjust, Refund
    val desc: String,
    val amount: Double,
    val status: String, // Completed, Pending, Rejected
    val date: String
)

data class FinancialRequest(
    val id: String,
    val phone: String,
    val type: String, // Deposit, Withdrawal
    val amount: Double,
    val provider: String,
    val status: String, // Pending, Approved, Rejected
    val date: String = ""
)

enum class ScreenTab {
    DASHBOARD,
    PACKAGES,
    WALLET,
    REFERRALS
}

enum class PlatformMode {
    CLIENT,
    ADMIN
}

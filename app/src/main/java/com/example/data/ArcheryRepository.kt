package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.FinancialRequest
import com.example.model.PackageItem
import com.example.model.PackagePlan
import com.example.model.TransactionItem
import com.example.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class ArcheryRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("archery_ai_hub_prefs", Context.MODE_PRIVATE)

    val packagePlans: List<PackagePlan> = listOf(
        PackagePlan(
            tier = 1,
            name = "Archery AI Tier 1",
            description = "Entry level trading robot algorithm with steady daily yield.",
            cost = 20000.0,
            dailyIncome = 1730.0,
            badge = "Starter AI"
        ),
        PackagePlan(
            tier = 2,
            name = "Archery AI Tier 2",
            description = "Enhanced yield precision targeting for higher efficiency.",
            cost = 30000.0,
            dailyIncome = 2680.0
        ),
        PackagePlan(
            tier = 3,
            name = "Archery AI Tier 3",
            description = "High speed arbitrage node tracking multi-market liquidity.",
            cost = 40000.0,
            dailyIncome = 3260.0
        ),
        PackagePlan(
            tier = 4,
            name = "Archery AI Tier 4",
            description = "Pro neural model algorithm with deep predictive pattern learning.",
            cost = 50000.0,
            dailyIncome = 3713.0
        ),
        PackagePlan(
            tier = 5,
            name = "Archery AI Ultra Tier 5",
            description = "Maximum daily yield engine with priority algorithmic order routing.",
            cost = 70000.0,
            dailyIncome = 5000.0,
            badge = "🔥 Best Yield",
            isBestYield = true
        )
    )

    private val _users = MutableStateFlow<Map<String, User>>(emptyMap())
    val users: StateFlow<Map<String, User>> = _users.asStateFlow()

    private val _requests = MutableStateFlow<List<FinancialRequest>>(emptyList())
    val requests: StateFlow<List<FinancialRequest>> = _requests.asStateFlow()

    private val _currentUserPhone = MutableStateFlow<String?>(null)
    val currentUserPhone: StateFlow<String?> = _currentUserPhone.asStateFlow()

    init {
        loadFromPrefs()
    }

    private fun currentTimeString(): String {
        val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun currentDateString(): String {
        val sdf = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
        return sdf.format(Date())
    }

    fun normalizePhone(raw: String): String {
        val digits = raw.filter { it.isDigit() }
        return when {
            digits.startsWith("256") -> "+$digits"
            digits.startsWith("0") -> "+256${digits.substring(1)}"
            else -> "+256$digits"
        }
    }

    private fun getDefaultUsers(): Map<String, User> {
        val defaultPhone = "+256770000000"
        return mapOf(
            defaultPhone to User(
                phone = defaultPhone,
                pin = "12345",
                balance = 2000.0,
                packages = emptyList(),
                refEarned = 0.0,
                refCount = 0,
                transactions = listOf(
                    TransactionItem(
                        id = UUID.randomUUID().toString(),
                        type = "Bonus",
                        desc = "Welcome Bonus Credited",
                        amount = 2000.0,
                        status = "Completed",
                        date = currentTimeString()
                    )
                )
            )
        )
    }

    private fun getDefaultRequests(): List<FinancialRequest> {
        return listOf(
            FinancialRequest(
                id = "REQ-101",
                phone = "+256770000000",
                type = "Deposit",
                amount = 20000.0,
                provider = "MTN Mobile Money",
                status = "Pending",
                date = currentTimeString()
            )
        )
    }

    private fun loadFromPrefs() {
        val usersJsonStr = prefs.getString("users_json", null)
        val requestsJsonStr = prefs.getString("requests_json", null)
        val currentPhone = prefs.getString("current_phone", "+256770000000")

        if (usersJsonStr != null) {
            try {
                val map = mutableMapOf<String, User>()
                val rootObj = JSONObject(usersJsonStr)
                val keys = rootObj.keys()
                while (keys.hasNext()) {
                    val key = keys.next()
                    val userObj = rootObj.getJSONObject(key)
                    val phone = userObj.getString("phone")
                    val pin = userObj.getString("pin")
                    val balance = userObj.getDouble("balance")
                    val refEarned = userObj.optDouble("refEarned", 0.0)
                    val refCount = userObj.optInt("refCount", 0)

                    val packagesList = mutableListOf<PackageItem>()
                    val pkgsArr = userObj.optJSONArray("packages") ?: JSONArray()
                    for (i in 0 until pkgsArr.length()) {
                        val pObj = pkgsArr.getJSONObject(i)
                        packagesList.add(
                            PackageItem(
                                id = pObj.getString("id"),
                                name = pObj.getString("name"),
                                cost = pObj.getDouble("cost"),
                                dailyIncome = pObj.getDouble("dailyIncome"),
                                purchasedAt = pObj.getString("purchasedAt")
                            )
                        )
                    }

                    val txList = mutableListOf<TransactionItem>()
                    val txArr = userObj.optJSONArray("transactions") ?: JSONArray()
                    for (i in 0 until txArr.length()) {
                        val tObj = txArr.getJSONObject(i)
                        txList.add(
                            TransactionItem(
                                id = tObj.optString("id", UUID.randomUUID().toString()),
                                type = tObj.getString("type"),
                                desc = tObj.getString("desc"),
                                amount = tObj.getDouble("amount"),
                                status = tObj.getString("status"),
                                date = tObj.getString("date")
                            )
                        )
                    }

                    map[phone] = User(
                        phone = phone,
                        pin = pin,
                        balance = balance,
                        packages = packagesList,
                        refEarned = refEarned,
                        refCount = refCount,
                        transactions = txList
                    )
                }
                _users.value = map
            } catch (e: Exception) {
                _users.value = getDefaultUsers()
            }
        } else {
            _users.value = getDefaultUsers()
        }

        if (requestsJsonStr != null) {
            try {
                val reqList = mutableListOf<FinancialRequest>()
                val reqArr = JSONArray(requestsJsonStr)
                for (i in 0 until reqArr.length()) {
                    val rObj = reqArr.getJSONObject(i)
                    reqList.add(
                        FinancialRequest(
                            id = rObj.getString("id"),
                            phone = rObj.getString("phone"),
                            type = rObj.getString("type"),
                            amount = rObj.getDouble("amount"),
                            provider = rObj.getString("provider"),
                            status = rObj.getString("status"),
                            date = rObj.optString("date", currentTimeString())
                        )
                    )
                }
                _requests.value = reqList
            } catch (e: Exception) {
                _requests.value = getDefaultRequests()
            }
        } else {
            _requests.value = getDefaultRequests()
        }

        _currentUserPhone.value = currentPhone
        saveToPrefs()
    }

    private fun saveToPrefs() {
        try {
            val rootObj = JSONObject()
            for ((key, user) in _users.value) {
                val userObj = JSONObject()
                userObj.put("phone", user.phone)
                userObj.put("pin", user.pin)
                userObj.put("balance", user.balance)
                userObj.put("refEarned", user.refEarned)
                userObj.put("refCount", user.refCount)

                val pkgsArr = JSONArray()
                for (pkg in user.packages) {
                    val pObj = JSONObject()
                    pObj.put("id", pkg.id)
                    pObj.put("name", pkg.name)
                    pObj.put("cost", pkg.cost)
                    pObj.put("dailyIncome", pkg.dailyIncome)
                    pObj.put("purchasedAt", pkg.purchasedAt)
                    pkgsArr.put(pObj)
                }
                userObj.put("packages", pkgsArr)

                val txArr = JSONArray()
                for (tx in user.transactions) {
                    val tObj = JSONObject()
                    tObj.put("id", tx.id)
                    tObj.put("type", tx.type)
                    tObj.put("desc", tx.desc)
                    tObj.put("amount", tx.amount)
                    tObj.put("status", tx.status)
                    tObj.put("date", tx.date)
                    txArr.put(tObj)
                }
                userObj.put("transactions", txArr)

                rootObj.put(key, userObj)
            }

            val reqArr = JSONArray()
            for (req in _requests.value) {
                val rObj = JSONObject()
                rObj.put("id", req.id)
                rObj.put("phone", req.phone)
                rObj.put("type", req.type)
                rObj.put("amount", req.amount)
                rObj.put("provider", req.provider)
                rObj.put("status", req.status)
                rObj.put("date", req.date)
                reqArr.put(rObj)
            }

            prefs.edit()
                .putString("users_json", rootObj.toString())
                .putString("requests_json", reqArr.toString())
                .putString("current_phone", _currentUserPhone.value)
                .apply()
        } catch (_: Exception) {}
    }

    fun register(phoneInput: String, pin: String, refCode: String?): Result<User> {
        val phone = normalizePhone(phoneInput)
        if (pin.length != 5 || !pin.all { it.isDigit() }) {
            return Result.failure(IllegalArgumentException("PIN must be exactly 5 numeric digits!"))
        }
        val currentUsers = _users.value.toMutableMap()
        if (currentUsers.containsKey(phone)) {
            return Result.failure(IllegalStateException("Phone number already registered. Please sign in."))
        }

        val newUser = User(
            phone = phone,
            pin = pin,
            balance = 2000.0,
            packages = emptyList(),
            refEarned = 0.0,
            refCount = 0,
            transactions = listOf(
                TransactionItem(
                    id = UUID.randomUUID().toString(),
                    type = "Bonus",
                    desc = "Welcome Registration Bonus",
                    amount = 2000.0,
                    status = "Completed",
                    date = currentTimeString()
                )
            )
        )
        currentUsers[phone] = newUser
        _users.value = currentUsers
        _currentUserPhone.value = phone
        saveToPrefs()
        return Result.success(newUser)
    }

    fun login(phoneInput: String, pin: String): Result<User> {
        val phone = normalizePhone(phoneInput)
        val user = _users.value[phone] ?: return Result.failure(IllegalArgumentException("User not found with this phone number."))
        if (user.pin != pin) {
            return Result.failure(IllegalArgumentException("Invalid 5-digit PIN. Please try again."))
        }
        _currentUserPhone.value = phone
        saveToPrefs()
        return Result.success(user)
    }

    fun logout() {
        _currentUserPhone.value = null
        saveToPrefs()
    }

    fun buyPackage(phone: String, plan: PackagePlan): Result<String> {
        val user = _users.value[phone] ?: return Result.failure(IllegalStateException("User not found"))
        if (user.balance < plan.cost) {
            return Result.failure(IllegalStateException("Insufficient balance! You need UGX ${String.format(Locale.US, "%,.0f", plan.cost)} to purchase ${plan.name}."))
        }

        val updatedUser = user.copy(
            balance = user.balance - plan.cost,
            packages = user.packages + PackageItem(
                id = "PKG-${System.currentTimeMillis()}",
                name = plan.name,
                cost = plan.cost,
                dailyIncome = plan.dailyIncome,
                purchasedAt = currentDateString()
            ),
            transactions = listOf(
                TransactionItem(
                    id = UUID.randomUUID().toString(),
                    type = "Purchase",
                    desc = "Purchased ${plan.name}",
                    amount = -plan.cost,
                    status = "Completed",
                    date = currentTimeString()
                )
            ) + user.transactions
        )

        val updatedMap = _users.value.toMutableMap()
        updatedMap[phone] = updatedUser
        _users.value = updatedMap
        saveToPrefs()
        return Result.success("Success! Purchased ${plan.name}. Daily yield increased by +UGX ${String.format(Locale.US, "%,.0f", plan.dailyIncome)}.")
    }

    fun simulateMidnightPayout(phone: String): Result<Double> {
        val user = _users.value[phone] ?: return Result.failure(IllegalStateException("User not found"))
        if (user.packages.isEmpty()) {
            return Result.failure(IllegalStateException("No active packages. Purchase an AI package to start receiving daily top-ups!"))
        }

        val totalPayout = user.packages.sumOf { it.dailyIncome }
        val updatedUser = user.copy(
            balance = user.balance + totalPayout,
            transactions = listOf(
                TransactionItem(
                    id = UUID.randomUUID().toString(),
                    type = "Payout",
                    desc = "12:00 AM Automatic Daily Yield Top-up",
                    amount = totalPayout,
                    status = "Completed",
                    date = currentTimeString()
                )
            ) + user.transactions
        )

        val updatedMap = _users.value.toMutableMap()
        updatedMap[phone] = updatedUser
        _users.value = updatedMap
        saveToPrefs()
        return Result.success(totalPayout)
    }

    fun submitDeposit(phone: String, amount: Double, provider: String, paymentPhone: String): Result<String> {
        if (amount < 20000.0) {
            return Result.failure(IllegalArgumentException("Minimum deposit is UGX 20,000."))
        }
        val user = _users.value[phone] ?: return Result.failure(IllegalStateException("User not found"))
        val reqId = "REQ-${(1000..9999).random()}"
        val newReq = FinancialRequest(
            id = reqId,
            phone = phone,
            type = "Deposit",
            amount = amount,
            provider = "$provider ($paymentPhone)",
            status = "Pending",
            date = currentTimeString()
        )
        _requests.value = listOf(newReq) + _requests.value
        saveToPrefs()
        return Result.success("Deposit request of UGX ${String.format(Locale.US, "%,.0f", amount)} submitted! Awaiting Admin approval.")
    }

    fun submitWithdrawal(phone: String, amount: Double, provider: String, receivingPhone: String): Result<String> {
        if (amount < 5000.0) {
            return Result.failure(IllegalArgumentException("Minimum withdrawal is UGX 5,000."))
        }
        val user = _users.value[phone] ?: return Result.failure(IllegalStateException("User not found"))
        if (user.balance < amount) {
            return Result.failure(IllegalStateException("Insufficient account balance to withdraw this amount."))
        }

        // Deduct balance upfront and record pending withdrawal
        val updatedUser = user.copy(
            balance = user.balance - amount,
            transactions = listOf(
                TransactionItem(
                    id = UUID.randomUUID().toString(),
                    type = "Withdrawal",
                    desc = "Withdrawal request to $receivingPhone",
                    amount = -amount,
                    status = "Pending",
                    date = currentTimeString()
                )
            ) + user.transactions
        )

        val updatedMap = _users.value.toMutableMap()
        updatedMap[phone] = updatedUser
        _users.value = updatedMap

        val reqId = "REQ-${(1000..9999).random()}"
        val newReq = FinancialRequest(
            id = reqId,
            phone = phone,
            type = "Withdrawal",
            amount = amount,
            provider = "$provider ($receivingPhone)",
            status = "Pending",
            date = currentTimeString()
        )
        _requests.value = listOf(newReq) + _requests.value
        saveToPrefs()
        return Result.success("Withdrawal request for UGX ${String.format(Locale.US, "%,.0f", amount)} submitted for Admin verification!")
    }

    fun adminApproveRequest(reqId: String): Result<String> {
        val req = _requests.value.find { it.id == reqId } ?: return Result.failure(IllegalStateException("Request not found"))
        val user = _users.value[req.phone] ?: return Result.failure(IllegalStateException("User not found"))

        val updatedRequests = _requests.value.map {
            if (it.id == reqId) it.copy(status = "Approved") else it
        }
        _requests.value = updatedRequests

        val updatedUsers = _users.value.toMutableMap()
        if (req.type == "Deposit") {
            val updatedUser = user.copy(
                balance = user.balance + req.amount,
                transactions = listOf(
                    TransactionItem(
                        id = UUID.randomUUID().toString(),
                        type = "Deposit",
                        desc = "Deposit Approved by Admin",
                        amount = req.amount,
                        status = "Completed",
                        date = currentTimeString()
                    )
                ) + user.transactions
            )
            updatedUsers[req.phone] = updatedUser
        } else if (req.type == "Withdrawal") {
            val updatedTransactions = user.transactions.map {
                if (it.type == "Withdrawal" && it.status == "Pending" && kotlin.math.abs(it.amount) == req.amount) {
                    it.copy(status = "Completed")
                } else it
            }
            updatedUsers[req.phone] = user.copy(transactions = updatedTransactions)
        }
        _users.value = updatedUsers
        saveToPrefs()
        return Result.success("Approved ${req.type} of UGX ${String.format(Locale.US, "%,.0f", req.amount)} for ${req.phone}")
    }

    fun adminRejectRequest(reqId: String): Result<String> {
        val req = _requests.value.find { it.id == reqId } ?: return Result.failure(IllegalStateException("Request not found"))
        val user = _users.value[req.phone] ?: return Result.failure(IllegalStateException("User not found"))

        val updatedRequests = _requests.value.map {
            if (it.id == reqId) it.copy(status = "Rejected") else it
        }
        _requests.value = updatedRequests

        val updatedUsers = _users.value.toMutableMap()
        if (req.type == "Withdrawal") {
            // Refund the deducted amount
            val updatedUser = user.copy(
                balance = user.balance + req.amount,
                transactions = listOf(
                    TransactionItem(
                        id = UUID.randomUUID().toString(),
                        type = "Refund",
                        desc = "Withdrawal Rejected - Funds Refunded",
                        amount = req.amount,
                        status = "Completed",
                        date = currentTimeString()
                    )
                ) + user.transactions.map {
                    if (it.type == "Withdrawal" && it.status == "Pending" && kotlin.math.abs(it.amount) == req.amount) {
                        it.copy(status = "Rejected")
                    } else it
                }
            )
            updatedUsers[req.phone] = updatedUser
        }
        _users.value = updatedUsers
        saveToPrefs()
        return Result.success("Rejected ${req.type} request for ${req.phone}")
    }

    fun adminFixBalance(phone: String, newBalance: Double): Result<String> {
        val user = _users.value[phone] ?: return Result.failure(IllegalStateException("User not found"))
        val updatedUser = user.copy(
            balance = newBalance,
            transactions = listOf(
                TransactionItem(
                    id = UUID.randomUUID().toString(),
                    type = "Admin Adjust",
                    desc = "Balance corrected by Admin Support",
                    amount = newBalance,
                    status = "Completed",
                    date = currentTimeString()
                )
            ) + user.transactions
        )
        val updatedMap = _users.value.toMutableMap()
        updatedMap[phone] = updatedUser
        _users.value = updatedMap
        saveToPrefs()
        return Result.success("Updated balance for $phone to UGX ${String.format(Locale.US, "%,.0f", newBalance)}")
    }

    fun simulateReferral(phone: String): Result<Double> {
        val user = _users.value[phone] ?: return Result.failure(IllegalStateException("User not found"))
        val bonus = 2000.0 // 10% on 20,000 package
        val updatedUser = user.copy(
            balance = user.balance + bonus,
            refEarned = user.refEarned + bonus,
            refCount = user.refCount + 1,
            transactions = listOf(
                TransactionItem(
                    id = UUID.randomUUID().toString(),
                    type = "Bonus",
                    desc = "Referral Commission (New Member Joined)",
                    amount = bonus,
                    status = "Completed",
                    date = currentTimeString()
                )
            ) + user.transactions
        )
        val updatedMap = _users.value.toMutableMap()
        updatedMap[phone] = updatedUser
        _users.value = updatedMap
        saveToPrefs()
        return Result.success(bonus)
    }

    fun resetToDefault() {
        _users.value = getDefaultUsers()
        _requests.value = getDefaultRequests()
        _currentUserPhone.value = "+256770000000"
        saveToPrefs()
    }
}

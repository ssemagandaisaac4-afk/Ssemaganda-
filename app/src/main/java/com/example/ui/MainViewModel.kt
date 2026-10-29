package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ArcheryRepository
import com.example.model.FinancialRequest
import com.example.model.PackagePlan
import com.example.model.PlatformMode
import com.example.model.ScreenTab
import com.example.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ArcheryRepository(application.applicationContext)

    val packagePlans: List<PackagePlan> = repository.packagePlans
    val users: StateFlow<Map<String, User>> = repository.users
    val requests: StateFlow<List<FinancialRequest>> = repository.requests
    val currentUserPhone: StateFlow<String?> = repository.currentUserPhone

    val currentUser: StateFlow<User?> = combine(repository.users, repository.currentUserPhone) { usersMap, phone ->
        phone?.let { usersMap[it] }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _currentTab = MutableStateFlow(ScreenTab.DASHBOARD)
    val currentTab: StateFlow<ScreenTab> = _currentTab.asStateFlow()

    private val _platformMode = MutableStateFlow(PlatformMode.CLIENT)
    val platformMode: StateFlow<PlatformMode> = _platformMode.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    fun showToast(msg: String) {
        _snackbarMessage.value = msg
    }

    fun setTab(tab: ScreenTab) {
        _currentTab.value = tab
    }

    fun setPlatformMode(mode: PlatformMode) {
        _platformMode.value = mode
    }

    fun login(phone: String, pin: String) {
        viewModelScope.launch {
            val result = repository.login(phone, pin)
            result.fold(
                onSuccess = { user ->
                    _snackbarMessage.value = "Welcome back, ${user.phone}!"
                },
                onFailure = { err ->
                    _snackbarMessage.value = err.message ?: "Login failed"
                }
            )
        }
    }

    fun register(phone: String, pin: String, refCode: String?) {
        viewModelScope.launch {
            val result = repository.register(phone, pin, refCode)
            result.fold(
                onSuccess = {
                    _snackbarMessage.value = "Account created! UGX 2,000 Welcome Bonus Credited 🎁"
                },
                onFailure = { err ->
                    _snackbarMessage.value = err.message ?: "Registration failed"
                }
            )
        }
    }

    fun logout() {
        repository.logout()
        _platformMode.value = PlatformMode.CLIENT
        _currentTab.value = ScreenTab.DASHBOARD
        _snackbarMessage.value = "Signed out of Archery AI Hub"
    }

    fun buyPackage(plan: PackagePlan) {
        val phone = currentUserPhone.value ?: return
        val result = repository.buyPackage(phone, plan)
        result.fold(
            onSuccess = { msg ->
                _snackbarMessage.value = msg
            },
            onFailure = { err ->
                _snackbarMessage.value = err.message ?: "Failed to buy package"
            }
        )
    }

    fun simulateMidnightPayout() {
        val phone = currentUserPhone.value ?: return
        val result = repository.simulateMidnightPayout(phone)
        result.fold(
            onSuccess = { amount ->
                _snackbarMessage.value = "🌙 12:00 AM Payout Simulated! +UGX ${String.format(Locale.US, "%,.0f", amount)} added to your balance."
            },
            onFailure = { err ->
                _snackbarMessage.value = err.message ?: "Simulation failed"
            }
        )
    }

    fun submitDeposit(amount: Double, provider: String, phone: String) {
        val userPhone = currentUserPhone.value ?: return
        val result = repository.submitDeposit(userPhone, amount, provider, phone)
        result.fold(
            onSuccess = { msg ->
                _snackbarMessage.value = msg
            },
            onFailure = { err ->
                _snackbarMessage.value = err.message ?: "Deposit failed"
            }
        )
    }

    fun submitWithdrawal(amount: Double, provider: String, receivingPhone: String) {
        val userPhone = currentUserPhone.value ?: return
        val result = repository.submitWithdrawal(userPhone, amount, provider, receivingPhone)
        result.fold(
            onSuccess = { msg ->
                _snackbarMessage.value = msg
            },
            onFailure = { err ->
                _snackbarMessage.value = err.message ?: "Withdrawal failed"
            }
        )
    }

    fun adminApproveRequest(reqId: String) {
        val result = repository.adminApproveRequest(reqId)
        result.fold(
            onSuccess = { msg -> _snackbarMessage.value = msg },
            onFailure = { err -> _snackbarMessage.value = err.message ?: "Approval failed" }
        )
    }

    fun adminRejectRequest(reqId: String) {
        val result = repository.adminRejectRequest(reqId)
        result.fold(
            onSuccess = { msg -> _snackbarMessage.value = msg },
            onFailure = { err -> _snackbarMessage.value = err.message ?: "Rejection failed" }
        )
    }

    fun adminFixBalance(phone: String, newBalance: Double) {
        val result = repository.adminFixBalance(phone, newBalance)
        result.fold(
            onSuccess = { msg -> _snackbarMessage.value = msg },
            onFailure = { err -> _snackbarMessage.value = err.message ?: "Balance update failed" }
        )
    }

    fun simulateReferral() {
        val phone = currentUserPhone.value ?: return
        val result = repository.simulateReferral(phone)
        result.fold(
            onSuccess = { bonus ->
                _snackbarMessage.value = "🎉 Friend registered via your link! Commission of +UGX ${String.format(Locale.US, "%,.0f", bonus)} added!"
            },
            onFailure = { err ->
                _snackbarMessage.value = err.message ?: "Referral simulation failed"
            }
        )
    }

    fun resetMockData() {
        repository.resetToDefault()
        _snackbarMessage.value = "System restored to default test data!"
    }
}

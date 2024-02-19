package com.example.githubusers.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import com.example.githubusers.data.db.entity.GithubUser
import com.example.githubusers.data.network.repository.GithubUserRepository
import com.example.githubusers.data.network.repository.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A Github User ViewModel class
 *
 * @property userRepository
 *
 * @param pager
 */
@HiltViewModel
class GithubUserViewModel @Inject constructor(
    private val userRepository: GithubUserRepository,
    pager: Pager<Int, GithubUser>
) : ViewModel() {

    /**
     * Single user details state flow
     */
    private val _userFlow = MutableStateFlow<GithubUser?>(null)
    val userFLow get() = _userFlow.asStateFlow()

    /**
     * Users list state flow
     */
    private val _usersFlow = MutableStateFlow<List<GithubUser>>(emptyList())
    val usersFlow get() = _usersFlow.asStateFlow()

    /**
     * Searching state flow
     */
    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    /**
     * Pagination enabled flag state flow
     */
    private val _isPagination = MutableStateFlow(false)
    val isPagination = _isPagination.asStateFlow()

    /**
     * Search text state flow
     */
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    /**
     * Searchable state flow filter the users list by string containing to not nullable GithubUser login value
     */
    val searchFlow = searchText
        .combine(_usersFlow) { text, users ->
            if (text.isBlank()) {
                userList
            }
            users.filter { user ->
                user.login.uppercase().contains(text.trim().uppercase())
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = _usersFlow.value
        )

    /**
     * Paging users list flow
     */
    val userPagingFlow = pager.flow.cachedIn(viewModelScope)

    /**
     * Progress indicator state flow
     */
    private val _showProgress = MutableStateFlow(true)
    val showProgress = _showProgress.asStateFlow()

    /**
     * Errors state flow
     */
    private val _showError = MutableStateFlow(false)
    val showError get() = _showError.asStateFlow()

    private var userList = emptyList<GithubUser>()


    /**
     * A function getting users list from API
     *
     */
    fun getUsers() {
        viewModelScope.launch {
            when (val result = userRepository.getGithubUsers()) {
                is Result.Success -> {
                    val mappedUserList = result.data
                    _usersFlow.value = mappedUserList
                    userList = mappedUserList
                    _showProgress.value = false
                    _showError.value = false
                }

                else -> {
                    val mappedUserListDB = userRepository.getGithubUsersFromDb()
                    _usersFlow.value = mappedUserListDB
                    userList = mappedUserListDB
                    _showProgress.value = false
                    _showError.value = true
                }
            }
        }
    }

    /**
     * A function getting user details from API
     *
     * @param id
     */
    fun getUserById(id: Int) {
        viewModelScope.launch {
            when (val result = userRepository.getGithubUser(id)) {
                is Result.Success -> {
                    val user = result.data
                    _userFlow.value = user
                    _showError.value = false
                }

                else -> {
                    val user = userRepository.getGithubUserFromDb(id)
                    _userFlow.value = user
                    _showError.value = true
                }
            }
        }
    }


    /**
     * A function setting current searchable text
     *
     * @param text
     */
    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    /**
     * A function setting searchable state (when data is providing or not)
     *
     */
    fun onToggleSearch() {
        _isSearching.value = !_isSearching.value
        if (!_isSearching.value) {
            onSearchTextChange("")
        }
    }

    /**
     * A function reset searchText value when searchable closed
     *
     */
    fun clearSearchBar() {
        _searchText.value = ""
    }

    /**
     * A function setting current pagination mechanism state
     *
     */
    fun onPaginationChange() {
        _isPagination.value = !_isPagination.value
    }


}
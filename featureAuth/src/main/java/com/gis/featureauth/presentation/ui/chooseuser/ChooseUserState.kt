package com.gis.featureauth.presentation.ui.chooseuser

import androidx.fragment.app.FragmentActivity
import com.gis.repository.domain.entity.User

data class ChooseUserState(
  val loading: Boolean = false,
  val users: List<ChooseUserListItem> = emptyList(),
  val error: Throwable? = null
)


sealed class ChooseUserIntent {
  object GetUsers : ChooseUserIntent()
  class AuthWithUser(val user: User) : ChooseUserIntent()
}


sealed class ChooseUserStateChange {
  object Idle : ChooseUserStateChange()
  object StartLoading : ChooseUserStateChange()
  class UsersReceived(val users: List<User>) : ChooseUserStateChange()
  class Error(val error: Throwable) : ChooseUserStateChange()
  object HideError : ChooseUserStateChange()
}

sealed class ChooseUserListItem {
  class UserItem(val user: User) : ChooseUserListItem()
}
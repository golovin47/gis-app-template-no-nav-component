package com.gis.featureauth.presentation.ui.chooseuser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.*
import com.gis.repository.domain.entity.User
import com.jakewharton.rxbinding2.view.RxView
import com.gis.featureauth.R
import com.gis.featureauth.databinding.ItemChooseUserBinding
import com.gis.featureauth.presentation.ui.chooseuser.ChooseUserListItem.*
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import com.gis.featureauth.presentation.ui.chooseuser.ChooseUserIntent.*

class ChooseUserAdapter(private val intentsPublisher: PublishSubject<ChooseUserIntent>) :
  ListAdapter<ChooseUserListItem, ViewHolder>(object : DiffUtil.ItemCallback<ChooseUserListItem>() {

    override fun areItemsTheSame(oldItem: ChooseUserListItem, newItem: ChooseUserListItem): Boolean =
      when {
        oldItem is UserItem && newItem is UserItem && oldItem.user.id == newItem.user.id -> true
        else -> false
      }

    override fun areContentsTheSame(oldItem: ChooseUserListItem, newItem: ChooseUserListItem): Boolean =
      when {
        oldItem is UserItem && newItem is UserItem && oldItem.user == newItem.user -> true
        else -> false
      }
  }) {

  private val USER_ITEM = 0x999

  override fun getItemViewType(position: Int): Int {
    val item = getItem(position)
    return if (item is UserItem) USER_ITEM else -1
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
    UserItemVH(
      DataBindingUtil.inflate(
        LayoutInflater.from(parent.context),
        R.layout.item_choose_user,
        parent,
        false
      ),
      intentsPublisher
    )

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = getItem(position)
    if (holder is UserItemVH && item is UserItem) {
      holder.bind(item.user)
    }
  }
}


class UserItemVH(val binding: ItemChooseUserBinding, private val intentsPublisher: PublishSubject<ChooseUserIntent>) :
  ViewHolder(binding.root) {

  fun bind(user: User) {
    binding.tvName.text = when {
      user.username.isNotBlank() -> user.username
      user.email.isNotBlank() -> user.email
      else -> user.phone
    }

    RxView.clicks(binding.tvName)
      .throttleFirst(1000, TimeUnit.MILLISECONDS)
      .map { AuthWithUser(user) }
      .subscribe(intentsPublisher)
  }
}
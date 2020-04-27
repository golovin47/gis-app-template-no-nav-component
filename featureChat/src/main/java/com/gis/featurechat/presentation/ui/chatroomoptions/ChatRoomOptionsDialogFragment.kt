package com.gis.featurechat.presentation.ui.chatroomoptions

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.gis.utils.presentation.BaseView
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import com.gis.featurechat.R
import com.gis.featurechat.databinding.DialogFragmentChatRoomOptionsBinding
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.gis.featurechat.presentation.ui.chatroomoptions.ChatRoomOptionsIntent.*
import java.util.concurrent.TimeUnit

class ChatRoomOptionsDialogFragment : DialogFragment(), BaseView<ChatRoomOptionsState> {

  private var binding: DialogFragmentChatRoomOptionsBinding? = null
  private val viewModel: ChatRoomOptionsViewModel by viewModel()
  private lateinit var intentsSubscription: Disposable

  companion object {
    fun getInstance(chatId: Int) =
      ChatRoomOptionsDialogFragment().apply { arguments = Bundle().apply { putInt("chatId", chatId) } }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    handleStates()
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    initBinding()
    initIntents()
    return initDialog()
  }

  override fun onDestroyView() {
    binding = null
    intentsSubscription.dispose()
    super.onDestroyView()
  }

  private fun initBinding() {
    binding = DataBindingUtil.inflate(
      LayoutInflater.from(context),
      R.layout.dialog_fragment_chat_room_options,
      null,
      false
    )
  }

  private fun initDialog() =
    AlertDialog.Builder(context!!)
      .setView(binding!!.root)
      .create()

  override fun initIntents() {
    intentsSubscription = Observable.merge(
      listOf(
        RxView.clicks(binding!!.tvDeleteChatRoom)
          .throttleFirst(1000, TimeUnit.MILLISECONDS)
          .map { ShowDeleteConfirmationDialog(arguments!!.getInt("chatId")) }
      )
    )
      .subscribe(viewModel.viewIntentsConsumer())
  }

  override fun handleStates() {
    viewModel.stateReceived().observe(this, Observer { state -> render(state) })
  }

  override fun render(state: ChatRoomOptionsState) {
    binding!!.loading = state.loading

    if (state.closeDialog)
      dismiss()

    state.error?.run {
      Snackbar.make(binding!!.root, message!!, Snackbar.LENGTH_SHORT).show()
    }
  }
}
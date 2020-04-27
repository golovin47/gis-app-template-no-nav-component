package com.gis.featureauth.presentation.ui.chooseuser

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gis.utils.presentation.BaseView
import com.google.android.material.snackbar.Snackbar
import com.gis.featureauth.R
import com.gis.featureauth.databinding.DialogFragmentChooseUserBinding
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.gis.featureauth.presentation.ui.chooseuser.ChooseUserIntent.*
import io.reactivex.Observable

class ChooseUserDialogFragment : DialogFragment(), BaseView<ChooseUserState> {

  private var binding: DialogFragmentChooseUserBinding? = null
  private val intentsPublisher = PublishSubject.create<ChooseUserIntent>()
  private lateinit var intentsSubscription: Disposable
  private val viewModel: ChooseUserViewModel by viewModel()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    handleStates()
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    initBinding()
    initRvChooseUser()
    initIntents()
    isCancelable = false
    return initDialog()
  }

  private fun initBinding() {
    binding = DataBindingUtil.inflate(
      LayoutInflater.from(context),
      R.layout.dialog_fragment_choose_user,
      null,
      false
    )
  }

  private fun initRvChooseUser() {
    binding!!.rvUsers.adapter = ChooseUserAdapter(intentsPublisher)
    binding!!.rvUsers.layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
    binding!!.rvUsers.addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
  }

  private fun initDialog(): AlertDialog =
    AlertDialog.Builder(context!!)
      .setView(binding!!.root)
      .create()

  override fun onDestroyView() {
    binding = null
    intentsSubscription.dispose()
    super.onDestroyView()
  }

  override fun initIntents() {
    intentsSubscription = Observable.merge(
      listOf(
        Observable.just(GetUsers),

        intentsPublisher
      )
    )
      .subscribe(viewModel.viewIntentsConsumer())
  }

  override fun handleStates() {
    viewModel.stateReceived().observe(this, Observer { state -> render(state) })
  }

  override fun render(state: ChooseUserState) {
    (binding!!.rvUsers.adapter as ChooseUserAdapter).submitList(state.users)

    state.error?.run {
      Snackbar.make(binding!!.root, message!!, Snackbar.LENGTH_SHORT).show()
    }
  }
}
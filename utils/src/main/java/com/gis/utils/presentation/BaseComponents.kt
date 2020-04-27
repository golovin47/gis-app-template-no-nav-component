package com.gis.utils.presentation

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.gis.utils.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

interface BaseView<State> {

  fun initIntents()

  fun handleStates()

  fun render(state: State)
}


abstract class BaseViewModel<State> : ViewModel() {

  private val states = MutableLiveData<State>()
  private val viewIntentsConsumer: PublishRelay<Any> = PublishRelay.create()
  private var intentsDisposable: Disposable? = null

  protected abstract fun initState(): State

  private fun handleIntents() {
    intentsDisposable = Observable.merge(vmIntents(), viewIntents(viewIntentsConsumer))
      .scan(initState()) { previousState, stateChanges ->
        reduceState(previousState, stateChanges)
      }
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe { state -> states.value = state }
  }

  protected open fun vmIntents(): Observable<Any> = Observable.never()

  protected abstract fun viewIntents(intentStream: Observable<*>): Observable<Any>

  protected abstract fun reduceState(previousState: State, stateChange: Any): State

  fun viewIntentsConsumer() = viewIntentsConsumer.also {
    if (intentsDisposable == null)
      handleIntents()
  }

  fun stateReceived(): LiveData<State> = states

  override fun onCleared() {
    intentsDisposable?.dispose()
  }
}


abstract class BaseMviFragment<State, Binding : ViewDataBinding, ViewModel : BaseViewModel<State>> :
  Fragment(),
  BaseView<State> {

  protected abstract val layoutId: Int
  protected var binding: Binding? = null
  protected lateinit var intentsSubscription: Disposable
  protected abstract val viewModel: ViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    handleStates()
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    initBinding(inflater, container)
    initIntents()
    return binding!!.root
  }

  override fun onDestroyView() {
    binding = null
    intentsSubscription.dispose()
    super.onDestroyView()
  }

  private fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
    binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
  }

  override fun initIntents() {
    intentsSubscription = userIntents().subscribe(viewModel.viewIntentsConsumer())
  }

  protected abstract fun userIntents(): Observable<Any>

  override fun handleStates() {
    viewModel.stateReceived().observe(this, Observer { state -> render(state) })
  }

  override fun render(state: State) {

  }
}


abstract class BaseMviBottomDialogFragment<State, Binding : ViewDataBinding, ViewModel : BaseViewModel<State>>
  : BottomSheetDialogFragment(), BaseView<State> {
  protected abstract val layoutId: Int
  protected var binding: Binding? = null
  protected var dialog: BottomSheetDialog? = null
  protected lateinit var intentsSubscription: Disposable
  protected abstract val viewModel: ViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    handleStates()
    super.onCreate(savedInstanceState)
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    initBinding()
    initIntents()
    dialog = initDialog()
    return dialog!!
  }

  override fun onDestroyView() {
    binding = null
    intentsSubscription.dispose()
    super.onDestroyView()
  }

  private fun initBinding() {
    binding = DataBindingUtil.inflate(LayoutInflater.from(requireContext()), layoutId, null, false)
  }

  open fun initDialog(): BottomSheetDialog =
    BottomSheetDialog(requireContext()).apply {
      setContentView(binding!!.root)
    }

  override fun initIntents() {
    intentsSubscription = userIntents().subscribe(viewModel.viewIntentsConsumer())
  }

  protected abstract fun userIntents(): Observable<Any>

  override fun handleStates() {
    viewModel.stateReceived().observe(this, Observer { state -> render(state) })
  }

  override fun render(state: State) {

  }
}


interface BackPressHandler {

  fun onBackPress(): Boolean
}
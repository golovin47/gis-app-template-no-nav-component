package com.gis.featureuserprofile.presentation.ui.changeuserprofile.changeuserbirthdate

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface.BUTTON_NEGATIVE
import android.content.DialogInterface.BUTTON_POSITIVE
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.gis.utils.presentation.BaseView
import com.gis.featureuserprofile.R
import com.gis.featureuserprofile.presentation.ui.changeuserprofile.changeuserbirthdate.ChangeUserBirthdateIntent.Cancel
import com.gis.featureuserprofile.presentation.ui.changeuserprofile.changeuserbirthdate.ChangeUserBirthdateIntent.ChangeBirthdate
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import java.util.concurrent.TimeUnit


const val YEAR = "year"
const val MONTH = "month"
const val DAY = "day"

class ChangeUserBirthDateDialogFragment : DialogFragment(),
  BaseView<ChangeUserBirthdateState> {

  private val intentsPublisher = PublishSubject.create<ChangeUserBirthdateIntent>()
  private lateinit var intentsSubscription: Disposable
  private val viewModel: ChangeUserBirthdateViewModel by viewModel()
  private var currentState: ChangeUserBirthdateState? = null

  companion object {
    fun getInstance(year: Int, month: Int, day: Int) =
      ChangeUserBirthDateDialogFragment().apply {
        arguments = Bundle().apply {
          putInt(YEAR, year)
          putInt(MONTH, month)
          putInt(DAY, day)
        }
      }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    handleStates()
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    initIntents()
    return initDialog()
  }

  override fun onDestroyView() {
    intentsSubscription.dispose()
    super.onDestroyView()
  }

  private fun initDialog(): DatePickerDialog {
    val defYear = arguments!!.getInt(YEAR)
    val defMonth = arguments!!.getInt(MONTH)
    val defDay = arguments!!.getInt(DAY)
    val dialog = object : DatePickerDialog(context!!, null, defYear, defMonth, defDay) {
      override fun dismiss() {
        if (currentState!!.closeDialog) {
          super.dismiss()
        }
      }
    }
      .apply {
        datePicker.maxDate = System.currentTimeMillis()

        setButton(BUTTON_POSITIVE, getString(R.string.ok)) { _, _ ->
          val year = datePicker.year
          val month = datePicker.month
          val day = datePicker.dayOfMonth
          intentsPublisher.onNext(
            ChangeBirthdate(
              currentState!!.user.copy(birthDate = GregorianCalendar(year, month, day).time)
            )
          )
        }

        setButton(BUTTON_NEGATIVE, getString(R.string.cancel)) { _, _ ->
          intentsPublisher.onNext(Cancel)
        }
      }
    isCancelable = false
    return dialog
  }

  override fun initIntents() {
    intentsSubscription = Observable.merge(
      listOf(
        intentsPublisher
          .throttleFirst(1000, TimeUnit.MILLISECONDS)
      )
    )
      .subscribe(viewModel.viewIntentsConsumer())
  }

  override fun handleStates() {
    viewModel.stateReceived().observe(this, Observer { state -> render(state) })
  }

  override fun render(state: ChangeUserBirthdateState) {
    currentState = state

    (dialog as DatePickerDialog).getButton(BUTTON_POSITIVE).isEnabled = !state.loading
    (dialog as DatePickerDialog).getButton(BUTTON_NEGATIVE).isEnabled = !state.loading

    if (state.closeDialog) {
      dismiss()
    }
  }
}
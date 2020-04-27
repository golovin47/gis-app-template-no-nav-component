package com.gis.utils.presentation.ui

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.gis.utils.R
import io.reactivex.subjects.PublishSubject
import com.gis.utils.presentation.ui.SmsCodeInputEvents.*

class SmsCodeView : ConstraintLayout {

  private val etCodeInput: UnselectableEditText by lazy(LazyThreadSafetyMode.NONE) { findViewById<UnselectableEditText>(R.id.etCodeInput) }
  private val tvFirstDigit: TextView by lazy(LazyThreadSafetyMode.NONE) { findViewById<TextView>(R.id.tvFirstDigit) }
  private val tvSecondDigit: TextView by lazy(LazyThreadSafetyMode.NONE) { findViewById<TextView>(R.id.tvSecondDigit) }
  private val tvThirdDigit: TextView by lazy(LazyThreadSafetyMode.NONE) { findViewById<TextView>(R.id.tvThirdDigit) }
  private val tvFourthDigit: TextView by lazy(LazyThreadSafetyMode.NONE) { findViewById<TextView>(R.id.tvFourthDigit) }
  private val tvFifthDigit: TextView by lazy(LazyThreadSafetyMode.NONE) { findViewById<TextView>(R.id.tvFifthDigit) }
  private val tvSixthDigit: TextView by lazy(LazyThreadSafetyMode.NONE) { findViewById<TextView>(R.id.tvSixthDigit) }

  private val inputEventsPublisher = PublishSubject.create<SmsCodeInputEvents>()

  constructor(context: Context) : super(context) {
    init(context)
  }

  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
    init(context)
  }

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    init(context)
  }

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
    context,
    attrs,
    defStyleAttr,
    defStyleRes
  ) {
    init(context)
  }

  private fun init(context: Context) {
    val inflater = LayoutInflater.from(context)
    inflater.inflate(R.layout.view_sms_code, this, true)

    etCodeInput.addTextChangedListener(object : TextWatcher {
      override fun afterTextChanged(s: Editable?) {}

      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        s?.run {
          applyInputToFields(this)
        }
      }
    })
  }

  private fun applyInputToFields(s: CharSequence) {
    for (indexedChar in s.withIndex()) {
      when (indexedChar.index) {
        0 -> tvFirstDigit.text = indexedChar.value.toString()
        1 -> tvSecondDigit.text = indexedChar.value.toString()
        2 -> tvThirdDigit.text = indexedChar.value.toString()
        3 -> tvFourthDigit.text = indexedChar.value.toString()
        4 -> tvFifthDigit.text = indexedChar.value.toString()
        5 -> tvSixthDigit.text = indexedChar.value.toString()
      }
    }

    applyBlankFields(s)

    if (s.length == 6)
      inputEventsPublisher.onNext(SmsCodeInputFinished(s.toString()))
    else
      inputEventsPublisher.onNext(SmsCodeInputChanged(s.toString()))
  }

  private fun applyBlankFields(s: CharSequence) {
    when (s.length) {
      0 -> {
        tvFirstDigit.text = ""
        tvFirstDigit.isSelected = false
        tvSecondDigit.text = ""
        tvSecondDigit.isSelected = false
        tvThirdDigit.text = ""
        tvThirdDigit.isSelected = false
        tvFourthDigit.text = ""
        tvFourthDigit.isSelected = false
        tvFifthDigit.text = ""
        tvFifthDigit.isSelected = false
        tvSixthDigit.text = ""
        tvSixthDigit.isSelected = false
      }

      1 -> {
        tvFirstDigit.isSelected = true
        tvSecondDigit.text = ""
        tvSecondDigit.isSelected = false
        tvThirdDigit.text = ""
        tvThirdDigit.isSelected = false
        tvFourthDigit.text = ""
        tvFourthDigit.isSelected = false
        tvFifthDigit.text = ""
        tvFifthDigit.isSelected = false
        tvSixthDigit.text = ""
        tvSixthDigit.isSelected = false
      }

      2 -> {
        tvFirstDigit.isSelected = true
        tvSecondDigit.isSelected = true
        tvThirdDigit.text = ""
        tvThirdDigit.isSelected = false
        tvFourthDigit.text = ""
        tvFourthDigit.isSelected = false
        tvFifthDigit.text = ""
        tvFifthDigit.isSelected = false
        tvSixthDigit.text = ""
        tvSixthDigit.isSelected = false
      }

      3 -> {
        tvFirstDigit.isSelected = true
        tvSecondDigit.isSelected = true
        tvThirdDigit.isSelected = true
        tvFourthDigit.text = ""
        tvFourthDigit.isSelected = false
        tvFifthDigit.text = ""
        tvFifthDigit.isSelected = false
        tvSixthDigit.text = ""
        tvSixthDigit.isSelected = false
      }

      4 -> {
        tvFirstDigit.isSelected = true
        tvSecondDigit.isSelected = true
        tvThirdDigit.isSelected = true
        tvFourthDigit.isSelected = true
        tvFifthDigit.text = ""
        tvFifthDigit.isSelected = false
        tvSixthDigit.text = ""
        tvSixthDigit.isSelected = false
      }

      5 -> {
        tvFirstDigit.isSelected = true
        tvSecondDigit.isSelected = true
        tvThirdDigit.isSelected = true
        tvFourthDigit.isSelected = true
        tvFifthDigit.isSelected = true
        tvSixthDigit.text = ""
        tvSixthDigit.isSelected = false
      }

      6 -> {
        tvFirstDigit.isSelected = true
        tvSecondDigit.isSelected = true
        tvThirdDigit.isSelected = true
        tvFourthDigit.isSelected = true
        tvFifthDigit.isSelected = true
        tvSixthDigit.isSelected = true
      }
    }
  }

  fun observeSmsCode() = inputEventsPublisher

  fun disableInput() {
    etCodeInput.isEnabled = false
  }

  fun enableInput() {
    etCodeInput.isEnabled = true
  }

  fun showError() {
    tvFirstDigit.isActivated = true
    tvSecondDigit.isActivated = true
    tvThirdDigit.isActivated = true
    tvFourthDigit.isActivated = true
    tvFifthDigit.isActivated = true
    tvSixthDigit.isActivated = true
  }

  fun hideError() {
    tvFirstDigit.isActivated = false
    tvSecondDigit.isActivated = false
    tvThirdDigit.isActivated = false
    tvFourthDigit.isActivated = false
    tvFifthDigit.isActivated = false
    tvSixthDigit.isActivated = false
  }
}


class UnselectableEditText : EditText {
  constructor(context: Context) : super(context)

  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
    context,
    attrs,
    defStyleAttr,
    defStyleRes
  )

  override fun onSelectionChanged(selStart: Int, selEnd: Int) {
    setSelection(this.length())
  }
}

sealed class SmsCodeInputEvents {
  class SmsCodeInputChanged(val code: String) : SmsCodeInputEvents()
  class SmsCodeInputFinished(val code: String) : SmsCodeInputEvents()
}
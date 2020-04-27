package com.gis.featurechoosecountry.presentation.ui.choosecountry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.gis.utils.presentation.BaseView
import com.gis.utils.domain.ImageLoader
import com.gis.utils.hideKeyboard
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.gis.featurechoosecountry.R
import com.gis.featurechoosecountry.databinding.FragmentChooseCountryBinding
import com.gis.featurechoosecountry.presentation.ui.choosecountry.ChooseCountryIntent.*
import com.gis.utils.presentation.BaseMviFragment
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class ChooseCountryFragment :
  BaseMviFragment<ChooseCountryState, FragmentChooseCountryBinding, ChooseCountryViewModel>() {

  override val layoutId: Int = R.layout.fragment_choose_country
  private val intentsPublisher = PublishSubject.create<ChooseCountryIntent>()
  override val viewModel: ChooseCountryViewModel by viewModel()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    super.onCreateView(inflater, container, savedInstanceState)
    initRvCountries()
    return binding!!.root
  }

  private fun initRvCountries() {
    with(binding!!.rvCountries) {
      val imageLoader: ImageLoader by inject()
      adapter = ChooseCountryAdapter(intentsPublisher, imageLoader)
      layoutManager = LinearLayoutManager(context, VERTICAL, false)
      addItemDecoration(DividerItemDecoration(context, VERTICAL))
    }
  }

  override fun userIntents(): Observable<Any> =
    Observable.merge(listOf(

      intentsPublisher,

      RxView.clicks(binding!!.ivClearSearch)
        .throttleFirst(500, TimeUnit.MILLISECONDS)
        .doOnNext { binding!!.etSearchCountry.text.clear() }
        .map { ClearSearch },

      RxTextView.textChanges(binding!!.etSearchCountry)
        .map { text -> SearchCountry(text.toString()) },

      RxToolbar.navigationClicks(binding!!.tbChooseCountry)
        .throttleFirst(500, TimeUnit.MILLISECONDS)
        .doOnNext { binding!!.etSearchCountry.hideKeyboard() }
        .map { GoBack }
    ))

  override fun render(state: ChooseCountryState) {
    binding!!.ivClearSearch.visibility =
      if (binding!!.etSearchCountry.text.isNotEmpty()) VISIBLE else INVISIBLE

    (binding!!.rvCountries.adapter as ChooseCountryAdapter).submitList(
      if (state.searchCountries.isNotEmpty())
        state.searchCountries
      else
        state.countries
    )

    state.error?.run {
      Snackbar.make(binding!!.root, message!!, Snackbar.LENGTH_SHORT).show()
    }
  }
}
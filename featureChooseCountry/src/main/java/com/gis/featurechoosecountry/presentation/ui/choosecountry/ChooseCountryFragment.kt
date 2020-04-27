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
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class ChooseCountryFragment : Fragment(), BaseView<ChooseCountryState> {

  private lateinit var intentsSubscription: Disposable
  private val intentsPublisher = PublishSubject.create<ChooseCountryIntent>()
  private var binding: FragmentChooseCountryBinding? = null
  private val viewModel: ChooseCountryViewModel by viewModel()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    handleStates()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    initBinding(inflater, container)
    initRvCountries()
    initIntents()
    return binding!!.root
  }

  private fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_choose_country, container, false)
  }

  private fun initRvCountries() {
    with(binding!!.rvCountries) {
      val imageLoader: ImageLoader by inject()
      adapter = ChooseCountryAdapter(intentsPublisher, imageLoader)
      layoutManager = LinearLayoutManager(context, VERTICAL, false)
      addItemDecoration(DividerItemDecoration(context, VERTICAL))
    }
  }

  override fun onDestroyView() {
    binding = null
    intentsSubscription.dispose()
    super.onDestroyView()
  }

  override fun initIntents() {
    intentsSubscription = Observable.merge(listOf(

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
      .subscribe(viewModel.viewIntentsConsumer())
  }

  override fun handleStates() {
    viewModel.stateReceived().observe(this, Observer { state -> render(state) })
  }

  override fun render(state: ChooseCountryState) {
    binding!!.ivClearSearch.visibility = if (binding!!.etSearchCountry.text.isNotEmpty()) VISIBLE else INVISIBLE

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
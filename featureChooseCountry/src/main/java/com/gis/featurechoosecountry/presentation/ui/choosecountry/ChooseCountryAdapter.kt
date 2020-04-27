package com.gis.featurechoosecountry.presentation.ui.choosecountry

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.gis.repository.domain.entity.Country
import com.gis.utils.databinding.EmptySearchPlaceholderBinding
import com.gis.utils.domain.ImageLoader
import com.jakewharton.rxbinding2.view.RxView
import com.gis.featurechoosecountry.R
import com.gis.featurechoosecountry.databinding.ItemCountryBinding
import com.gis.featurechoosecountry.presentation.ui.choosecountry.CountryListItem.CountryItem
import com.gis.featurechoosecountry.presentation.ui.choosecountry.CountryListItem.EmptySearchItem
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import com.gis.featurechoosecountry.presentation.ui.choosecountry.ChooseCountryIntent.*

const val COUNTRY_ITEM = 0x999
const val COUNTRY_EMPTY_SEARCH = 0x998

class ChooseCountryAdapter(private val intentsPublisher: PublishSubject<ChooseCountryIntent>,
                           private val imageLoader: ImageLoader) :
  ListAdapter<CountryListItem, ViewHolder>(object : DiffUtil.ItemCallback<CountryListItem>() {

    override fun areItemsTheSame(oldItem: CountryListItem, newItem: CountryListItem): Boolean =
      when {
        oldItem is CountryItem && newItem is CountryItem && oldItem.country.name == newItem.country.name -> true
        oldItem is EmptySearchItem && newItem is EmptySearchItem -> true
        else -> false
      }

    override fun areContentsTheSame(oldItem: CountryListItem, newItem: CountryListItem): Boolean =
      when {
        oldItem is CountryItem && newItem is CountryItem && oldItem.country == newItem.country -> true
        oldItem is EmptySearchItem && newItem is EmptySearchItem -> true
        else -> false
      }
  }) {

  override fun getItemViewType(position: Int): Int {
    val item = getItem(position)
    return when (item) {
      is CountryItem -> COUNTRY_ITEM
      else -> COUNTRY_EMPTY_SEARCH
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
    when (viewType) {
      COUNTRY_ITEM -> CountryViewHolder(
        DataBindingUtil.inflate(
          LayoutInflater.from(parent.context),
          R.layout.item_country,
          parent,
          false
        )
      )

      else -> EmptySearchViewHolder(
        DataBindingUtil.inflate(
          LayoutInflater.from(parent.context),
          R.layout.empty_search_placeholder,
          parent,
          false
        )
      )
    }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = getItem(position)
    if (item is CountryItem && holder is CountryViewHolder) {
      holder.bind(item.country, intentsPublisher, imageLoader)
    }
  }
}


class CountryViewHolder(private val binding: ItemCountryBinding) : ViewHolder(binding.root) {

  fun bind(country: Country, intentsPublisher: PublishSubject<ChooseCountryIntent>, imageLoader: ImageLoader) {
    imageLoader.loadImg(binding.ivFlag, country.flagResId, false)
    binding.tvName.text = country.name
    binding.tvCode.text = country.code

    RxView.clicks(binding.itemRoot)
      .throttleFirst(1000, TimeUnit.MILLISECONDS)
      .map { ChooseCountry(country.name) }
      .subscribe(intentsPublisher)
  }
}

class EmptySearchViewHolder(binding: EmptySearchPlaceholderBinding) : ViewHolder(binding.root)
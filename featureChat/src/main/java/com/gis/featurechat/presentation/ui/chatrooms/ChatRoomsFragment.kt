package com.gis.featurechat.presentation.ui.chatrooms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gis.featurechat.R
import com.gis.featurechat.databinding.FragmentChatRoomsBinding
import com.gis.featurechat.presentation.ui.chatrooms.ChatRoomsIntent.*
import com.gis.featurechat.presentation.ui.chatrooms.ChatRoomsListItem.ChatRoomsEmptySearchItem
import com.gis.featurechat.presentation.ui.chatrooms.ChatRoomsListItem.ChatRoomsLoadingItem
import com.gis.utils.domain.ImageLoader
import com.gis.utils.hideKeyboard
import com.gis.utils.presentation.BaseMviFragment
import com.gis.utils.showKeyboard
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class ChatRoomsFragment :
  BaseMviFragment<ChatRoomsState, FragmentChatRoomsBinding, ChatRoomsViewModel>() {

  override val layoutId: Int = R.layout.fragment_chat_rooms
  private val intentsPublisher = PublishSubject.create<ChatRoomsIntent>()
  override val viewModel: ChatRoomsViewModel by viewModel()
  private val imageLoader: ImageLoader by inject()
  private var currentState: ChatRoomsState? = null


  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    super.onCreateView(inflater, container, savedInstanceState)
    initToolbar()
    initRecyclerView()
    initSwipeRefresh()
    initClearSearchBtn()
    return binding!!.root
  }

  private fun initToolbar() {
    binding!!.tbChatRooms.inflateMenu(R.menu.menu_chat_rooms)
    binding!!.tbChatRooms.setOnMenuItemClickListener { item ->
      intentsPublisher.onNext(ShowSearchField)
      return@setOnMenuItemClickListener true
    }
    binding!!.tbChatRooms.setNavigationOnClickListener {
      binding!!.etSearch.hideKeyboard()
      intentsPublisher.onNext(CloseSearchField)
    }
  }

  private fun initRecyclerView() {
    with(binding!!.rvChatRooms) {
      adapter = ChatRoomsAdapter(intentsPublisher, imageLoader)
      layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
      addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
      addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
          loadNextPageIfNeed(this@with, dy)
          if (dy > 0)
            binding?.fabCreateChatRoom?.hide()
          else if (dy < 0)
            binding?.fabCreateChatRoom?.show()
        }
      })
    }
  }

  private fun loadNextPageIfNeed(rv: RecyclerView, dy: Int) {
    val llm = rv.layoutManager as LinearLayoutManager
    val totalItemCount = llm.itemCount
    val lastVisibleItem = llm.findLastVisibleItemPosition()
    if (!currentState?.chatRooms?.contains(ChatRoomsLoadingItem)!! &&
      !currentState?.chatRooms?.contains(ChatRoomsEmptySearchItem)!! &&
      currentState?.searchResult?.isEmpty()!! &&
      totalItemCount <= lastVisibleItem + 4 &&
      dy > 0
    ) {
      val nextPage = (currentState!!.chatRooms.size / 20) + 1
      intentsPublisher.onNext(LoadNextChatRoomsPage(nextPage, 20))
    }
  }

  private fun initSwipeRefresh() {
    binding!!.srlChatRooms.setOnRefreshListener {
      intentsPublisher.onNext(RefreshChatRooms)
    }
  }

  private fun initClearSearchBtn() {
    binding!!.ivClearSearch.setOnClickListener {
      binding!!.etSearch.setText("")
    }
  }

  override fun userIntents(): Observable<Any> =
    Observable.merge(listOf(

      Observable.just(StartObserveChatRooms),

      intentsPublisher,

      RxTextView.textChanges(binding!!.etSearch)
        .doOnNext { text ->
          binding!!.ivClearSearch.visibility =
            if (text.toString().isBlank()) View.GONE else View.VISIBLE
        }
        .debounce(300, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
        .map { SearchChatRooms(it.toString().trim()) },

      RxView.clicks(binding!!.fabCreateChatRoom)
        .throttleFirst(1000, TimeUnit.MILLISECONDS)
        .map { GoToCreateNewRoom }
    ))

  override fun render(state: ChatRoomsState) {
    currentState = state

    binding!!.srlChatRooms.isRefreshing = state.loading

    (binding!!.rvChatRooms.adapter as ChatRoomsAdapter).apply {
      submitList(
        if (state.searchResult.isEmpty()) {
          state.chatRooms
        } else {
          state.searchResult
        }
      )

      binding!!.srlChatRooms.isEnabled = state.searchResult.isEmpty()
    }

    binding!!.showSearchField = state.showSearchField

    with(binding!!.tbChatRooms) {
      if (state.showSearchField) {
        binding!!.etSearch.visibility = View.VISIBLE
        binding!!.etSearch.requestFocus()
        binding!!.etSearch.showKeyboard()
        menu.findItem(R.id.menu_search).isVisible = false
        setNavigationIcon(R.drawable.ic_arrow_back)
      } else {
        binding!!.etSearch.setText("")
        binding!!.etSearch.visibility = View.GONE
        menu.findItem(R.id.menu_search).isVisible = true
        navigationIcon = null
      }
    }

    state.error?.run {
      Snackbar.make(binding!!.root, message!!, Snackbar.LENGTH_SHORT).show()
    }
  }
}
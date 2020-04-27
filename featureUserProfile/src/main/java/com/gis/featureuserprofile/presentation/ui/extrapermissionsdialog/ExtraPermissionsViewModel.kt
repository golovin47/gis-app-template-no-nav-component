package com.gis.featureuserprofile.presentation.ui.extrapermissionsdialog

import com.gis.utils.presentation.BaseViewModel
import io.reactivex.Observable
import com.gis.featureuserprofile.presentation.ui.extrapermissionsdialog.ExtraPermissionsIntent.*
import com.gis.featureuserprofile.presentation.ui.extrapermissionsdialog.ExtraPermissionsStateChange.*

class ExtraPermissionsViewModel : BaseViewModel<ExtraPermissionsState>() {

  override fun initState(): ExtraPermissionsState =
    ExtraPermissionsState()

  override fun viewIntents(intentStream: Observable<*>): Observable<Any> =
    Observable.merge(listOf(

      intentStream.ofType(OpenSettings::class.java)
        .map { GoToSettings },

      intentStream.ofType(Cancel::class.java)
        .map { CloseDialog }
    ))

  override fun reduceState(previousState: ExtraPermissionsState, stateChange: Any): ExtraPermissionsState =
    when (stateChange) {
      is GoToSettings -> previousState.copy(closeDialog = true, goToSettings = true)

      is CloseDialog -> previousState.copy(closeDialog = true)

      else -> previousState
    }
}
package com.gis.featureuserprofile.presentation.ui.extrapermissionsdialog


data class ExtraPermissionsState(
  val closeDialog: Boolean = false,
  val goToSettings: Boolean = false
)


sealed class ExtraPermissionsIntent {
  object OpenSettings : ExtraPermissionsIntent()
  object Cancel : ExtraPermissionsIntent()
}


sealed class ExtraPermissionsStateChange {
  object GoToSettings : ExtraPermissionsStateChange()
  object CloseDialog : ExtraPermissionsStateChange()
}
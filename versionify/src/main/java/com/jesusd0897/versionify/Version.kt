package com.jesusd0897.versionify

import android.os.Parcelable
import androidx.annotation.StringRes
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

private const val APP_KEY_TAG = "app_key"
private const val APP_KEY = "citas"

private const val VERSION_TAG = "version"
private const val DOWNLOAD_TAG= "descarga"
private const val DESCRIPTION_TAG="funcionalidades"

@Parcelize
data class AppKey(@Expose @SerializedName(APP_KEY_TAG) var appKey: String = APP_KEY) : Parcelable

@Parcelize
data class Version(
    @Expose @SerializedName(VERSION_TAG) var versionName: String,
    @Expose @SerializedName(DOWNLOAD_TAG) var url: String,
    @Expose @SerializedName(DESCRIPTION_TAG) var description: String?
) : Parcelable

enum class VersionStatus(@StringRes val message: Int) {
    UPDATED(R.string.version_updated_msg),
    MINIMUM_CHANGE(R.string.version_minimum_change_msg),
    MEDIUM_CHANGE(R.string.version_medium_change_msg),
    BIG_CHANGE(R.string.version_big_change_msg),
}
/*
 * Copyright (c) 2020 jesusd0897.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jesusd0897.versionify

import android.os.Parcelable
import androidx.annotation.StringRes
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

private const val APP_KEY_TAG = "app_key"
private const val APP_KEY = "citas"

private const val VERSION_TAG = "version"
private const val DOWNLOAD_TAG = "descarga"
private const val DESCRIPTION_TAG = "funcionalidades"

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
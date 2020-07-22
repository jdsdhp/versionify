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

import android.Manifest
import android.app.Activity
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.google.android.material.dialog.MaterialAlertDialogBuilder

const val VERSION_REQUEST_CODE_W_EXT_STORAGE = 4444

/**
 * Create a dialog builder for version control.
 * @param activity App context activity.
 * @param title Dialog title.
 * @param msg Dialog message.
 * @param positiveText  Actionable button text. By default os "Download".
 * @param icon Dialog icon.
 */
private fun versionWarningDialogBuilder(
    activity: Activity,
    title: String,
    msg: String,
    positiveText: String,
    @DrawableRes icon: Int? = null
) = MaterialAlertDialogBuilder(activity)
    .setTitle(title)
    .setMessage(msg)
    .setPositiveButton(positiveText) { _, _ ->
        requestDialogPermissions(
            activity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            VERSION_REQUEST_CODE_W_EXT_STORAGE
        )
    }.apply {
        icon?.let { this.setIcon(it) }
    }

private fun versionWarningDialogIfNeeded(
    activity: Activity,
    newVersion: Version,
    status: VersionStatus,
    title: String,
    positiveText: String,
    negativeText: String,
    @DrawableRes icon: Int? = null,
    showAtMinimum: Boolean = false
) {
    if (status != VersionStatus.UPDATED && (status != VersionStatus.MINIMUM_CHANGE || showAtMinimum)) {
        val message =
            activity.getString(status.message) +
                    ": v." + "${newVersion.versionName}\n•••••••\n" +
                    "${newVersion.description}"
        val builder = versionWarningDialogBuilder(
            activity, title, message, positiveText, icon
        )
        if (status == VersionStatus.BIG_CHANGE) builder.setCancelable(false)
        else builder.setNegativeButton(negativeText, null)
        builder.show()
    }
}

/**
 * @param activity Context activity.
 * @param version Web service version.
 * @param title Dialog title.
 * @param positiveText  Actionable button text. By default os "Download".
 * @param negativeText Cancelable button text. By default os "Cancel".
 * @param icon Dialog icon.
 * @param showAtMinimum Indicates if it should be shown when it is a minimum change. By default is false.
 */
fun checkVersion(
    activity: Activity,
    version: Version,
    title: String? = null,
    positiveText: String? = null,
    negativeText: String? = null,
    @DrawableRes icon: Int? = null,
    showAtMinimum: Boolean = false
) {
    storeApkVersion(activity, version)
    val buildVersionName =
        activity.packageManager.getPackageInfo(activity.packageName, 0).versionName
    val status = versionStatus(activity, buildVersionName)
    versionWarningDialogIfNeeded(
        activity,
        version,
        status,
        title ?: activity.getString(R.string.version_warning_title),
        positiveText ?: activity.getString(R.string.version_download),
        negativeText ?: activity.getString(R.string.version_cancel),
        icon,
        showAtMinimum
    )
}

/**
 * @param activity Context activity.
 * @param version Web service version.
 * @param title Dialog title.
 * @param positiveText  Actionable button text. By default os "Download".
 * @param negativeText Cancelable button text. By default os "Cancel".
 * @param icon Dialog icon.
 * @param showAtMinimum Indicates if it should be shown when it is a minimum change. By default is false.
 */
fun checkVersion(
    activity: Activity,
    version: Version,
    @StringRes title: Int? = null,
    @StringRes positiveText: Int? = null,
    @StringRes negativeText: Int? = null,
    @DrawableRes icon: Int? = null,
    showAtMinimum: Boolean = false
) {
    storeApkVersion(activity, version)
    val buildVersionName =
        activity.packageManager.getPackageInfo(activity.packageName, 0).versionName
    val status = versionStatus(activity, buildVersionName)
    versionWarningDialogIfNeeded(
        activity,
        version,
        status,
        activity.getString(title ?: R.string.version_warning_title),
        activity.getString(positiveText ?: R.string.version_download),
        activity.getString(negativeText ?: R.string.version_cancel),
        icon,
        showAtMinimum
    )
}
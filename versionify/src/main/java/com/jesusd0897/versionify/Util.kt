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

import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.core.app.ActivityCompat
import com.google.gson.GsonBuilder
import com.jesusd0897.preferenza.consultPreference
import com.jesusd0897.preferenza.storePreference

private const val GENERAL_KEY_VERSION = "general_version"
private const val VERSION_KEY = "version_key"

/**
 * Request permission through Dialog.
 * @param activity           Activity who request.
 * @param manifestPermission Permission from manifest.
 * @param idPermission       Permission ID.
 */
internal fun requestDialogPermissions(
    activity: Activity,
    manifestPermission: String,
    idPermission: Int
) =
    ActivityCompat.requestPermissions(activity, arrayOf(manifestPermission), idPermission)

/**
 * Request permission through Dialog.
 * @param activity            Activity who request.
 * @param manifestPermissions Permissions from manifest.
 * @param idPermission        Permission ID.
 */
internal fun requestDialogPermissions(
    activity: Activity,
    manifestPermissions: Array<String>,
    idPermission: Int
) =
    ActivityCompat.requestPermissions(activity, manifestPermissions, idPermission)

/**
 * Provide a Version object wish contain full info of current supported
 * version by the web service.
 * @param context App context.
 */
fun apkVersion(context: Context): Version? {
    val versionString = consultPreference(context, GENERAL_KEY_VERSION, VERSION_KEY, "")
    if (versionString.isEmpty()) return null
    return try {
        val gson = GsonBuilder().serializeNulls().setPrettyPrinting().create()
        gson.fromJson(versionString, Version::class.java)
    } catch (ex: Exception) {
        ex.printStackTrace()
        null
    }
}

/**
 * Store a Version object wish contain full info of current supported
 * version by the web service.
 * @param context App context.
 * @param version App version from webservice to store into preferences.
 */
internal fun storeApkVersion(context: Context, version: Version) {
    val gson = GsonBuilder().serializeNulls().setPrettyPrinting().create()
    storePreference(context, GENERAL_KEY_VERSION, hashMapOf(VERSION_KEY to gson.toJson(version)))
}

/**
 * Provide stored version status.
 * @param context App context.
 * @param buildVersion App version name.
 */
fun isVersionOutdated(context: Context, buildVersion: String) =
    versionStatus(context, buildVersion) != VersionStatus.UPDATED

/**
 * Provide stored version status.
 * @param context App context.
 * @param buildVersion App version name.
 */
fun versionStatus(context: Context, buildVersion: String): VersionStatus {
    val fullVersion = apkVersion(context)
    if (fullVersion == null) return VersionStatus.BIG_CHANGE
    else {
        val cloudApkVersion = fullVersion.versionName.trim()
        if (buildVersion >= cloudApkVersion) return VersionStatus.UPDATED
        val installedDigits = buildVersion.split(".")
        val cloudDigits = cloudApkVersion.split(".")
        return when {
            installedDigits[0] < cloudDigits[0] -> VersionStatus.BIG_CHANGE
            installedDigits[1] < cloudDigits[1] -> VersionStatus.MEDIUM_CHANGE
            else -> VersionStatus.MINIMUM_CHANGE
        }
    }
}

/**
 * Ask the Download Manager to download a file from an URL.
 * @param context App context.
 * @param downloadURL Url to download apk file.
 * @param title Download manager notification title.
 * @param description Download manager notification description.
 */
fun downloadFile(context: Context, downloadURL: String, title: String, description: String?) {
    val request = DownloadManager.Request(Uri.parse(downloadURL))
        .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        .setTitle(title)
        .setDescription(description)
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        .setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            title + "_" + System.currentTimeMillis() + ".apk"
        )
    request.allowScanningByMediaScanner()
    val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    dm.enqueue(request)
}

/**
 * Send Download Intent to Navigator.
 * @param context App context.
 * @param downloadURL Url to download apk file.
 */
fun downloadFileThroughNavigatorIntent(context: Context, downloadURL: String) =
    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(downloadURL)))
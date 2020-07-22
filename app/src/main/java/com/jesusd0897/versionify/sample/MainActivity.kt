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

package com.jesusd0897.versionify.sample

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jesusd0897.versionify.VERSION_REQUEST_CODE_W_EXT_STORAGE
import com.jesusd0897.versionify.Version
import com.jesusd0897.versionify.checkVersion
import com.jesusd0897.versionify.downloadFile

class MainActivity : AppCompatActivity() {

    private var version: Version? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Version from the web service
        version = Version(
            "1.2.3",
            //"https://github.com/skydoves/Pokedex/releases/download/1.0.1/app-debug.apk",
            "https://picsum.photos/200/300",
            """Include new features like:
                |- Add profile photo.
                |- New fancy fonts.
            """.trimMargin()
        )

        checkVersion(
            activity = this,
            version = version!!,
            title = "New Version",
            positiveText = "Download",
            negativeText = "Cancel",
            icon = R.drawable.ic_system_update,
            showAtMinimum = true   //By default is false.
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        var allGrant = true
        for (result in grantResults) if (result != PackageManager.PERMISSION_GRANTED) {
            allGrant = false
            break
        }
        if (allGrant) when (requestCode) {
            VERSION_REQUEST_CODE_W_EXT_STORAGE -> {
                version?.let { ver ->
                    downloadFile(
                        context = this,
                        downloadURL = ver.url,
                        title = getString(R.string.app_name),
                        description = getString(com.jesusd0897.versionify.R.string.version_downloading)
                                + " " + version!!.versionName
                    )
                }
                return
            }
        } else {
            Toast.makeText(this, "Permissions Required!!", Toast.LENGTH_LONG).show()
            finish()
        }
    }

}
💎⏫ Versionify
=======

Utility to simplify versioning in the app. Notifies current update status and provides download functionality via URL.

[![JitPack](https://jitpack.io/v/jdsdhp/versionify.svg)](https://jitpack.io/#jdsdhp/versionify) 
[![API](https://img.shields.io/badge/API-18%2B-red.svg?style=flat)](https://android-arsenal.com/api?level=18) 
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://github.com/jdsdhp/versionify/blob/master/LICENSE) 
[![Twitter](https://img.shields.io/badge/Twitter-@jdsdhp-9C27B0.svg)](https://twitter.com/jdsdhp)

## Including in your project

#### Gradle

```gradle
allprojects  {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
dependencies {
    implementation 'com.github.jdsdhp:versionify:$version'
}
```

## Usage

### Maniefiest
Add the following permissions to the manifest file in your app.
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

### Kotlin
- Declare version property in your activity.
```kotlin
private var version: Version? = null
```
- Receive and handle you Version object from web service. For simplify this sample app, version object it's already created.
- Check the webservice version with current running app version and show dialog if is needed.
```kotlin
// Version sample from the web service.
version = Version(
    "1.2.3",
    "https://picsum.photos/200/300", //An image just for sample.
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
```
- Handle needed permission to write into storage.
```kotlin
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
                downloadVersion(
                    context = this,
                    downloadURL = ver.url,
                    title = getString(R.string.app_name),
                    description = getString(com.jesusd0897.versionify.R.string.version_downloading)
                        + " " + version!!.versionName,
                    downloadWay = DownloadWay.DOWNLOAD_MANAGER
                )
            }
                return
        }
    } else {
        Toast.makeText(this, "Permissions     Required!!", Toast.LENGTH_LONG).show()
        finish()
    }
}
```

## Sample project

It's very important to check out the sample app. Most techniques that you would want to implement are already implemented in the examples.

View the sample app's source code [here](https://github.com/jdsdhp/versionify/tree/master/app)

## TODO
* Add on Download complete Broadcast Receiver.

License
=======

    Copyright (c) 2020 jesusd0897.
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
        http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

package dev.teamhub.firebase

import cocoapods.FirebaseCore.*

actual open class FirebaseException(message: String) : Exception(message)
actual open class FirebaseNetworkException(message: String) : FirebaseException(message)
actual open class FirebaseTooManyRequestsException(message: String) : FirebaseException(message)
actual open class FirebaseApiNotAvailableException(message: String) : FirebaseException(message)

actual val Firebase.app: FirebaseApp
    get() = FirebaseApp(FIRApp.defaultApp()!!)

actual fun Firebase.app(name: String): FirebaseApp =
    FirebaseApp(FIRApp.appNamed(name)!!)

actual fun Firebase.initialize(context: Any?): FirebaseApp? =
    FIRApp.configure().let { app }

actual fun Firebase.initialize(context: Any?, options: FirebaseOptions, name: String): FirebaseApp =
    FIRApp.configureWithName(name, options.toIos()).let { app(name) }

actual fun Firebase.initialize(context: Any?, options: FirebaseOptions) =
    FIRApp.configureWithOptions(options.toIos()).let { app }

actual class FirebaseApp internal constructor(val ios: FIRApp) {
    actual val name: String
        get() = ios.name
    actual val options: FirebaseOptions
        get() = ios.options.run { FirebaseOptions(bundleID, APIKey!!, databaseURL!!, trackingID, storageBucket, projectID) }
}

actual fun Firebase.apps(context: Any?) = FIRApp.allApps()!!
    .values
    .map { FirebaseApp(it as FIRApp) }

private fun FirebaseOptions.toIos() = FIROptions().apply {
        bundleID = this@toIos.applicationId
        APIKey = this@toIos.apiKey
        databaseURL = this@toIos.databaseUrl
        trackingID = this@toIos.gaTrackingId
        storageBucket = this@toIos.storageBucket
        projectID = this@toIos.projectId
    }
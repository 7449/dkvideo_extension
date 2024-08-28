plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.kotlin)
}
android {
    compileSdk = libs.versions.compileSdk.get().toInt()
    namespace = "xyz.doikki.videoplayer.pipextension"
    defaultConfig { minSdk = libs.versions.minSdk.get().toInt() }
    buildFeatures.viewBinding = true
    buildTypes { release { isMinifyEnabled = false } }
    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.jvmTarget.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.jvmTarget.get())
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
}
dependencies {
    implementation(libs.bundles.video)
}
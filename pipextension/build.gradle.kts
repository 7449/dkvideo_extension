plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.kotlin)
}
android {
    compileSdk = libs.versions.compileSdk.get().toInt()
    namespace = "xyz.doikki.videoplayer.pipextension"
    defaultConfig { minSdk = 19 }
    buildFeatures.viewBinding = true
    buildTypes { release { isMinifyEnabled = false } }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
}
dependencies {
    implementation(libs.appcompat)
    implementation(libs.recyclerview)
    implementation(libs.player.java)
    implementation(libs.player.ui)
    implementation(libs.player.cache)
    implementation(libs.player.core)
}
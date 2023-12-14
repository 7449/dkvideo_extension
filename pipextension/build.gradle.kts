plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
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
    implementation(libs.material)
    implementation(libs.startup)
    implementation(libs.player.java)
    implementation(libs.player.ui)
    implementation(libs.player.cache)
    implementation(libs.player.core)
}
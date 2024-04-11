plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.android.kotlin)
}
android {
    compileSdk = libs.versions.compileSdk.get().toInt()
    namespace = "xyz.doikki.videoplayer.pipextension.sample"
    defaultConfig {
        targetSdk = libs.versions.targetSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()
    }
    buildFeatures.viewBinding = true
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
    debugImplementation(libs.leak.canary)
    implementation(project(":pipextension"))
}

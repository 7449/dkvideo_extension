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
        sourceCompatibility = JavaVersion.toVersion(libs.versions.jvmTarget.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.jvmTarget.get())
    }
}
dependencies {
    implementation(libs.androidx.appcompat)
    implementation(project(":pipextension"))
}

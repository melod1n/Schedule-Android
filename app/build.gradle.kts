plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-android-extensions")
}

android {
    compileSdk = 31
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId = "com.melod1n.schedule"
        minSdk = 23
        targetSdk = 31
        versionCode = 1
        versionName = "0.1"
    }

    buildTypes {
        getByName("release").apply {
            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true

        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")

    implementation("de.hdodenhof:circleimageview:3.1.0")

    implementation("androidx.core:core-ktx:1.6.0")

    implementation("androidx.preference:preference-ktx:1.1.1")

    implementation("androidx.appcompat:appcompat:1.3.1")

    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    implementation("com.google.android.material:material:1.4.0")

    implementation("com.squareup.picasso:picasso:2.71828")

    implementation("org.greenrobot:eventbus:3.2.0")

    implementation("net.yslibrary.keyboardvisibilityevent:keyboardvisibilityevent:2.0.1")

    implementation("com.jakewharton:butterknife:10.2.3")

    kapt("com.jakewharton:butterknife-compiler:10.2.3")

    implementation("org.apache.commons:commons-lang3:3.12.0")

    implementation("org.jetbrains:annotations:22.0.0")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.5.30")

    implementation("androidx.room:room-runtime:2.3.0")

    kapt("androidx.room:room-compiler:2.3.0")

    implementation("com.github.yogacp:android-viewbinding:1.0.2")

    implementation("com.google.dagger:hilt-android:2.38.1")

    kapt("com.google.dagger:hilt-android-compiler:2.38.1")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2-native-mt")

    implementation("androidx.fragment:fragment-ktx:1.3.6")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
}

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp") version "1.6.10-1.0.4"
}

android {
    namespace = "com.example.printermobile"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.printermobile"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures{
        viewBinding = true
    }
}

dependencies {
    val lifecycle_version = "2.6.2"
    val activity_version = "1.6.1"
    val fragment_version = "1.5.5"
    val retrofit_version = "2.9.0"
    val room_version = "2.5.0"
    val hilt_version = "2.44"
    val navigation_fragment = "2.7.4"

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Navigation Fragment
    implementation("androidx.navigation:navigation-fragment-ktx:${navigation_fragment}")
    implementation("androidx.navigation:navigation-ui-ktx:${navigation_fragment}")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    // Live Data
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")

    // Activity
    implementation("androidx.activity:activity-ktx:$activity_version")
    implementation("androidx.fragment:fragment-ktx:$fragment_version")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit_version")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")

    // Hilt
    implementation("com.google.dagger:hilt-android:$hilt_version")
    kapt("com.google.dagger:hilt-android-compiler:$hilt_version")

    // Print libraries
    implementation("com.github.DantSu:ESCPOS-ThermalPrinter-Android:3.3.0")
    implementation("com.github.anastaciocintra:escpos-coffee:4.1.0")

    // Picasso
    implementation("com.squareup.picasso:picasso:2.8")

    // Room
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    implementation("androidx.room:room-rxjava2:$room_version")
    implementation("androidx.room:room-rxjava3:$room_version")
    implementation("androidx.room:room-guava:$room_version")
    testImplementation("androidx.room:room-testing:$room_version")
    implementation("androidx.room:room-paging:$room_version")

    // Splash
    implementation("androidx.core:core-splashscreen:1.0.0")

    // Swipe decorator
    implementation("it.xabaras.android:recyclerview-swipedecorator:1.4")

    // Expansion panel
    implementation("com.robertlevonyan.view:MaterialExpansionPanel:2.1.6")

    // printer 1
    implementation("org.jsoup:jsoup:1.13.1")
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.20")

    // Showcase View
    implementation ("com.github.erkutaras:ShowcaseView:1.5.0")

    implementation ("com.github.mazenrashed:Printooth:1.3.1")
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.0.0")
}
kapt {
    correctErrorTypes = true
}
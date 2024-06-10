plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.dicoding.com.storyapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dicoding.com.storyapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "BASE_URL", "\"https://story-api.dicoding.dev/v1/\"")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs += listOf("-Xopt-in=kotlin.RequiresOptIn")
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true

    }
}


dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.activity)
    implementation(libs.play.services.maps)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

//    retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
//    okhttp
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
//    datastore
    implementation("androidx.datastore:datastore-preferences:1.1.1")
//    circleimageview
    implementation("de.hdodenhof:circleimageview:3.1.0")
//    glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
//    location
    implementation("com.google.android.gms:play-services-location:21.2.0")
//    paging3
    implementation("androidx.paging:paging-runtime-ktx:3.1.0")
//    room
    implementation("androidx.room:room-paging:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
//    InstantTaskExecutorRule
    testImplementation("androidx.arch.core:core-testing:2.2.0")
//    TestDispatcher
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
//    mockito
    testImplementation("org.mockito:mockito-core:4.4.0")
    testImplementation("org.mockito:mockito-inline:4.4.0")
//    IdlingResource
    implementation("androidx.test.espresso:espresso-idling-resource:3.5.1")
//    IntentsTestRule
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.1")

}
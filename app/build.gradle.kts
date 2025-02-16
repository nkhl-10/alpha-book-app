plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlinx-serialization")
}

android {
    namespace = "com.app.jetpackcomposedemo"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.app.jetpackcomposedemo"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.activity:activity-compose:1.10.0")
    implementation(platform("androidx.compose:compose-bom:2025.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation ("androidx.compose.ui:ui:1.7.8")
    implementation ("androidx.compose.material:material:1.7.8")

    /*navigation*/
    implementation ("androidx.navigation:navigation-compose:2.8.7")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2025.02.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")


    //K-tor
    val ktorVersion = "1.6.8"
//    implementation("io.ktor:ktor-client-android:$ktorVersion")
//    implementation("io.ktor:ktor-client-core:$ktorVersion")
//    implementation("io.ktor:ktor-client-serialization-jvm:$ktorVersion")
//    implementation("io.ktor:ktor-client-logging:$ktorVersion")

    //Coroutines
    val coroutinesVersion = "1.7.3"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    // Coroutines - Deferred adapter
    implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")

    //di
    val koinVersion = "3.5.3"
    implementation("io.insert-koin:koin-android:$koinVersion")

    implementation ("io.ktor:ktor-client-android:$ktorVersion")
    implementation ("io.ktor:ktor-client-json-jvm:$ktorVersion")
    implementation ("io.ktor:ktor-client-serialization-jvm:$ktorVersion")

    implementation("com.google.code.gson:gson:2.10.1")

    implementation("androidx.compose.material:material-icons-extended:1.7.8")

}
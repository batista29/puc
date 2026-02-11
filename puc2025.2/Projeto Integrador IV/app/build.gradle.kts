plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services") // Plugin do Firebase

    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")//Maps
    id("org.jetbrains.kotlin.android") // Plugin do Firebase
}

android {
    namespace = "com.example.petcare"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.petcare"
        minSdk = 26
        targetSdk = 36
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

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // --- 🔹 Compose ---
    implementation(platform("androidx.compose:compose-bom:2024.09.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.activity:activity-compose:1.10.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.4")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation ("com.google.firebase:firebase-firestore-ktx:24.4.0") //

    // --- 🔹 AndroidX Essentials ---
    implementation("androidx.core:core-ktx:1.16.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation(libs.androidx.lifecycle.runtime.ktx)
    // REMOVA: implementation ('com.google.firebase:firebase-storage-ktx') <-- REMOVIDO!


    // --- 🔹 Firebase (com BOM para manter versões compatíveis) ---
    // A plataforma define as versões.
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))

    // AGORA, BASTA LISTAR AS BIBLIOTECAS SEM A VERSÃO OU PREFIXO COMPLETO
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")

    // Adicione o Storage (corretamente) e o Kotlin Coroutines para .await()
    implementation("com.google.firebase:firebase-storage-ktx") // 🌟 CORRIGIDO!
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3") // Para .await()

    // Outras dependências que não usam o BOM:
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("androidx.compose.material:material-icons-extended:1.6.0")

    // --- 🔹 Imagens (Coil) ---
    implementation("io.coil-kt:coil-compose:2.6.0")
    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.activity:activity-ktx:1.8.2")
    implementation("androidx.activity:activity:1.11.0")
    implementation(libs.androidx.animation.core.android)
    implementation(libs.androidx.foundation.android)


    // --- 🔹 Testes / Debug ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.09.00"))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // --- 🔹 Debug ---
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // --- GOOGLE MAPS
    // Maps SDK for Android
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.maps.android:maps-compose:4.4.1")
}
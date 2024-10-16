plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.app.karapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.app.karapp"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation (libs.constraintlayout)
    implementation (libs.vectordrawable)
    implementation (libs.navigation.fragment)
    implementation (libs.androidx.navigation.ui)
    implementation (libs.androidx.lifecycle.extensions)


//bootstrap
    implementation (libs.androidbootstrap)
//android networking
    implementation(libs.android.networking)
    implementation (libs.android.spinkit)
//circular image
    implementation (libs.circleimageview)

//image view
    implementation (libs.dexter)
//dexter permissions
    implementation (libs.ucrop)
//Glide
    implementation (libs.glide)
    annotationProcessor (libs.compiler)
//Butterknife
   /* implementation (libs.butterknife.v1023)  // Update to a newer version
    annotationProcessor (libs.butterknife.compiler.v1023)
*/
// Choose one Material Design version based on your needs
    implementation (libs.material.v100)  // Assuming you want Material Design v100

//font
    implementation (libs.calligraphy3)
    implementation (libs.viewpump)
//Rating bar
    implementation (libs.library)

    implementation("com.github.bumptech.glide:okhttp3-integration:4.0.0") {
        exclude("glide-parent")
    }

    implementation (libs.multidex)
}
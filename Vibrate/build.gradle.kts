import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.maven)
    alias(libs.plugins.dokka)
}

kotlin {
    jvmToolchain(17)

    androidTarget { publishLibraryVariants("release") }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }

        iosMain.dependencies {

        }
        androidMain.dependencies {
            implementation(libs.androidx.startup.runtime)
        }
    }

    //https://kotlinlang.org/docs/native-objc-interop.html#export-of-kdoc-comments-to-generated-objective-c-headers
    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        compilations["main"].compileTaskProvider.configure {
            compilerOptions {
                freeCompilerArgs.add("-Xexport-kdoc")
            }
        }
    }

}

android {
    namespace = "com.wonddak"
    compileSdk = 36

    defaultConfig {
        minSdk = 21
    }
}

dokka {
    moduleName.set("KMP-Vibrate")

    dokkaPublications.html {
        outputDirectory.set(project.mkdir("build/dokka"))
    }

    dokkaSourceSets {
        this.commonMain {
            displayName.set("Common")
        }
        named("androidMain") {
            displayName.set("Android")
        }
        named("iosMain") {
            displayName.set("iOS")
        }
    }
}

mavenPublishing {
    configure(
        KotlinMultiplatform(
            javadocJar = JavadocJar.Dokka("dokkaGeneratePublicationHtml"),
            sourcesJar = true
        )
    )
}
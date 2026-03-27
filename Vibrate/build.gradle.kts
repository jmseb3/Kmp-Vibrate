import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.maven)
    alias(libs.plugins.dokka)
}

kotlin {
    androidLibrary {
        namespace = "com.wonddak"
        compileSdk = 36
        minSdk = 23
        androidResources.enable = true
        compilerOptions { jvmTarget.set(JvmTarget.JVM_17) }

    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    js {
        browser()
    }

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

        jsMain {

        }

        wasmJsMain {

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
        named("jsMain") {
            displayName.set("js")
        }
        named("wasmJsMain") {
            displayName.set("wasm")
        }
    }
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
    configure(
        KotlinMultiplatform(
            javadocJar = JavadocJar.Dokka("dokkaGeneratePublicationHtml"),
            sourcesJar = true
        )
    )
}

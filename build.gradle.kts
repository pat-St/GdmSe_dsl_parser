plugins {
    kotlin("jvm") version "1.3.71"
    kotlin("plugin.serialization") version "1.3.70"
    application
}

application {
    mainClassName = "org.dsl.Main"
}

repositories {
    jcenter()
    mavenCentral()
    maven(  "https://dl.bintray.com/lukasjapan/de.cvguy.kotlin")
}

sourceSets {
    main {
        java {
            srcDir("src/main/kotlin")
        }
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("stdlib-js"))
    implementation(kotlin("reflect"))
    implementation("de.cvguy.kotlin:koreander:0.1.0")
}

group = "org.dsl"
version = "0.0.1-SNAPSHOT"
description = "intern_dsl"

java {
    sourceCompatibility = org.gradle.api.JavaVersion.VERSION_1_8
    targetCompatibility = org.gradle.api.JavaVersion.VERSION_1_8
}


tasks{
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileJava {
        options.encoding = "UTF-8"
    }
}
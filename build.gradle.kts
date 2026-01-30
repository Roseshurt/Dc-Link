plugins {
    alias(libs.plugins.fabric.loom)
}

base {
    archivesName = properties["archives_base_name"] as String
    version = libs.versions.mod.version.get()
    group = properties["maven_group"] as String
}

repositories {
    // Fabric & Minecraft dependencies
    maven { url = uri("https://maven.fabricmc.net/") }
    maven { url = uri("https://maven.terraformersmc.com/releases/") }
    mavenCentral()
    maven { url = uri("https://repo.spongepowered.org/maven") }

    // Meteor repositories
    maven { url = uri("https://maven.meteordev.org/releases") }
    maven { url = uri("https://maven.meteordev.org/snapshots") }
}

dependencies {
    // Fabric
    minecraft("com.mojang:minecraft:1.21.1")
    mappings(variantOf("net.fabricmc:yarn:1.21.8-rc1+build.2") { it.classifier = "v2" })
    modImplementation("net.fabricmc:fabric-loader:0.14.23")

    // Meteor
    modImplementation("meteordevelopment:meteor-client:1.10.8")
}


tasks {
    processResources {
        val propertyMap = mapOf(
            "version" to project.version,
            "mc_version" to libs.versions.minecraft.get()
        )

        inputs.properties(propertyMap)

        filteringCharset = "UTF-8"

        filesMatching("fabric.mod.json") {
            expand(propertyMap)
        }
    }

    jar {
        inputs.property("archivesName", project.base.archivesName.get())

        from("LICENSE") {
            rename { "${it}_${inputs.properties["archivesName"]}" }
        }
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release = 21
        options.compilerArgs.add("-Xlint:deprecation")
        options.compilerArgs.add("-Xlint:unchecked")
    }
}

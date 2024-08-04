tasks.register("listConfigurations") {
    doLast {
        println("Configurations in project '${project.name}':")
        project.configurations.forEach { config ->
            println(" - ${config.name}")
        }
    }
}

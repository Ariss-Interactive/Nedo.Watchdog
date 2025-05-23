package ariss

import java.io.BufferedWriter
import java.io.File
import java.io.OutputStreamWriter
import java.nio.file.Paths

lateinit var server: Process
lateinit var serverInput: BufferedWriter

fun javaHome(config: ServerSettings): String {
    return if (config.java.home == "default") System.getenv("JAVA_HOME") else config.java.home
}

fun javaGenLaunchCommand(config: ServerSettings): List<String> {
    var args: Array<String> = emptyArray()
    args += "${javaHome(config)}${File.separator}bin${File.separator}java"

    for (arg in config.java.args) {
        args += arg
    }

    args += "-jar"
    args += config.startJar

    for (arg in config.serverArgs) {
        args += arg
    }

    return args.toList()
}

fun javaRun(config: ServerSettings) {
    server = ProcessBuilder()
        .command(javaGenLaunchCommand(config))
        .directory(Paths.get("").toAbsolutePath().toFile())
        .redirectError(ProcessBuilder.Redirect.INHERIT)
        .redirectOutput(ProcessBuilder.Redirect.INHERIT)
        .redirectInput(ProcessBuilder.Redirect.INHERIT)
        .start()

    serverInput = BufferedWriter(OutputStreamWriter(server.outputStream))
}
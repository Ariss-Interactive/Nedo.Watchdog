package ariss

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import net.mamoe.yamlkt.Comment
import net.mamoe.yamlkt.Yaml
import java.io.File
import kotlin.system.exitProcess

const val configPath: String = "watchdog.yml"
lateinit var config: WatchdogConfig

@Serializable
data class WatchdogConfig(
    val servers: Map<String, ServerSettings>
)

@Serializable
data class ServerSettings(
    @Comment(
        """
        Human-readable name, used in
        notifications.
    """
    )
    val name: String,

    @Comment(
        """
        Specifies the jar to launch.
    """
    )
    val startJar: String,
    @Comment(
        """
        The URL/path/whatever to check for updates on.  
    """
    )
    val updateUrl: String,

    @Comment(
        """
        Where the server will be ran.
        In actuality, this allows using
        one jar for multiple servers.
    """
    )
    val workingDir: String,

    @Comment(
        """
        Server arguments. Passed after the jar.
    """
    )
    val serverArgs: List<String> = emptyList(),
    val java: JavaOpts,
) {
    @Serializable
    data class JavaOpts(
        @Comment(
            """
        The path to a java environment.
        Leave as 'default' to use the
        version in the system's PATH.
        """
        )
        val home: String = "default",

        @Comment(
            """
        Specify your java arguments here.
        """
        )
        val args: List<String> = listOf(
            "-Xms128m", "-Xmx2048m"
        )
    )
}

fun configCreate() {
    val file = File(configPath)

    if (file.createNewFile())
        file.writeText(
            Yaml.encodeToString<WatchdogConfig>(
                WatchdogConfig(
                    mapOf(
                        Pair("my-server", ServerSettings(
                            name = "My server",
                            startJar = "server.jar",
                            updateUrl = "https://example.com",
                            workingDir = "./",
                            java = ServerSettings.JavaOpts()
                        ))
                    )
                )
            )
        )

    print("Created watchdog.yml\nEdit the file and restart")
    exitProcess(0)
}

fun configInit() {
    val file = File(configPath)
    config = Yaml.decodeFromString<WatchdogConfig>(file.readText())
}
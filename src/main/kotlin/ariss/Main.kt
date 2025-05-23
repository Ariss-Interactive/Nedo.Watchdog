package ariss

import io.github.oshai.kotlinlogging.KotlinLogging
import java.nio.file.Files
import kotlin.io.path.Path

private val logger = KotlinLogging.logger {}

fun main() {
    if (!Files.exists(Path(configPath)))
        configCreate()

    configInit()

    logger.info { "Nedo.Watchdog from Ariss Interactive" }
    logger.info { "Managing ${config.servers.size} server today..." }
    for ((id, server) in config.servers) {
        logger.info { "Launching $id..." }
        javaRun(server)
    }
}
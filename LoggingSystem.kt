import java.io.File

// ─────────────────────────────────────────────
//  Logger Interface
// ─────────────────────────────────────────────
interface Logger {
    fun log(message: String)
}


// ─────────────────────────────────────────────
//  Implementation 1: ConsoleLogger
// ─────────────────────────────────────────────

/** Logs messages directly to the console */
class ConsoleLogger : Logger {
    override fun log(message: String) {
        println("[CONSOLE] $message")
    }
}


// ─────────────────────────────────────────────
//  Implementation 2: FileLogger
// ─────────────────────────────────────────────

/** Logs messages by appending them to a file on disk */
class FileLogger(private val fileName: String) : Logger {
    override fun log(message: String) {
        File(fileName).appendText("[FILE] $message\n")
        println("[FILE -> $fileName] $message")   // echo so we can see it in the demo
    }
}


// ─────────────────────────────────────────────
//  Application — Kotlin CLASS DELEGATION
// ─────────────────────────────────────────────

/**
 * [Application] implements [Logger] BY DELEGATION.
 *
 * The `by logger` keyword tells Kotlin to forward every call
 * to [Logger]'s interface methods directly to the [logger]
 * instance supplied at construction — no boilerplate override needed.
 *
 * Internally [Application] can also add its own behaviour
 * (e.g. [start] / [stop]) while still acting as a [Logger].
 */
class Application(private val appName: String, logger: Logger) : Logger by logger {

    fun start() {
        log("[$appName] Application is starting...")
    }

    fun processRequest(request: String) {
        log("[$appName] Processing request: $request")
    }

    fun stop() {
        log("[$appName] Application is shutting down.")
    }
}


// ─────────────────────────────────────────────
//  Entry Point
// ─────────────────────────────────────────────
fun main() {

    // ── Demo 1: Application delegates to ConsoleLogger ──
    println("=== Console Logging ===")
    val consoleApp = Application("MyApp", ConsoleLogger())
    consoleApp.start()
    consoleApp.processRequest("GET /home")
    consoleApp.stop()

    // ── Demo 2: Application delegates to FileLogger ──
    println("\n=== File Logging ===")
    val fileApp = Application("MyApp", FileLogger("app.log"))
    fileApp.start()
    fileApp.processRequest("POST /login")
    fileApp.stop()

    println("\nCheck app.log to see the file output.")
}

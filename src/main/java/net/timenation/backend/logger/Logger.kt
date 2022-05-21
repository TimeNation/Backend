package net.timenation.backend.logger

class Logger {

    companion object {
        @JvmStatic
        fun log(logMessage: String?, logType: LogType) {
            when (logType) {
                LogType.INFO -> println("${ConsoleColor.BLACK_BRIGHT}[ ${ConsoleColor.RED_BRIGHT}BACKEND ${ConsoleColor.BLACK_BRIGHT}]  ${ConsoleColor.CYAN_BRIGHT}$logMessage")
                LogType.SUCCESSFULLY -> println("${ConsoleColor.BLACK_BRIGHT}[ ${ConsoleColor.RED_BRIGHT}BACKEND ${ConsoleColor.BLACK_BRIGHT}]  ${ConsoleColor.GREEN_BRIGHT}$logMessage")
                LogType.WARNING -> println("${ConsoleColor.BLACK_BRIGHT}[ ${ConsoleColor.RED_BRIGHT}BACKEND ${ConsoleColor.BLACK_BRIGHT}]  ${ConsoleColor.YELLOW_BRIGHT}$logMessage")
                LogType.ERROR -> println("${ConsoleColor.BLACK_BRIGHT}[ ${ConsoleColor.RED_BRIGHT}BACKEND ${ConsoleColor.BLACK_BRIGHT}]  ${ConsoleColor.RED_BRIGHT}$logMessage")
            }
        }
    }
}

enum class LogType {
    INFO,
    SUCCESSFULLY,
    WARNING,
    ERROR
}

class ConsoleColor {
    companion object {
        const val BLACK_BRIGHT = "\u001b[0;90m"
        const val RED_BRIGHT = "\u001b[0;91m"
        const val GREEN_BRIGHT = "\u001b[0;92m"
        const val YELLOW_BRIGHT = "\u001b[0;93m"
        const val CYAN_BRIGHT = "\u001b[0;96m"
    }
}
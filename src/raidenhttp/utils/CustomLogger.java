package raidenhttp.utils;

// Imports
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

public class CustomLogger {
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String logFilePath = "raidenapp.log";
    private final PrintWriter fileWriter;

    // Options
    private final boolean isDebug = true;

    public enum LogLevel {
        NOTICE,
        INFO,
        DEBUG,
        WARN,
        ERROR,
        CRITICAL
    }

    public CustomLogger() {
        AnsiConsole.systemInstall();
        try {
            fileWriter = new PrintWriter(new FileWriter(logFilePath, true));
        } catch (IOException e) {
            throw new RuntimeException("Failed to open log file: " + e.getMessage());
        }
    }

    private void print(LogLevel level, String message) {
        String timestamp = dtf.format(LocalDateTime.now());
        String formattedMessage = "[" + timestamp + "] ";

        switch (level) {
            case NOTICE:
                System.out.println(formattedMessage + Ansi.ansi().fg(Ansi.Color.BLUE).a("[NOTICE] ").fg(Ansi.Color.WHITE).a(message).reset());
                formattedMessage += "[NOTICE] " + message;
                break;
            case INFO:
                System.out.println(formattedMessage + Ansi.ansi().fg(Ansi.Color.BLUE).a("[INFO] ").fg(Ansi.Color.WHITE).a(message).reset());
                formattedMessage += "[INFO] " + message;
                break;
            case DEBUG:
                if(this.isDebug) {
                    System.out.println(formattedMessage + Ansi.ansi().fg(Ansi.Color.MAGENTA).a("[DEBUG] ").fg(Ansi.Color.WHITE).a(message).reset());
                    formattedMessage += "[DEBUG] " + message;
                }
                break;
            case WARN:
                System.out.println(formattedMessage + Ansi.ansi().fg(Ansi.Color.YELLOW).a("[WARN] ").fg(Ansi.Color.WHITE).a(message).reset());
                formattedMessage += "[WARN] " + message;
                break;
            case ERROR:
                System.out.println(formattedMessage + Ansi.ansi().fg(Ansi.Color.RED).a("[ERROR] ").fg(Ansi.Color.WHITE).a(message).reset());
                formattedMessage += "[ERROR] " + message;
                break;
            case CRITICAL:
                System.out.println(formattedMessage + Ansi.ansi().fg(Ansi.Color.RED).a("[CRITICAL] ").fg(Ansi.Color.WHITE).a(message).reset());
                formattedMessage += "[CRITICAL] " + message;
                break;
            default:
                throw new RuntimeException("Reached unknown log level: " + level);
        }

        if (fileWriter != null) {
            fileWriter.println(formattedMessage);
            fileWriter.flush();
        }

        if(level == LogLevel.CRITICAL) {
            System.exit(1);
        }
    }

    public void info(String message, Object... args) {
        print(LogLevel.INFO, String.format(message, args));
    }

    public void debug(String message, Object... args) {
        if(this.isDebug) {
            print(LogLevel.DEBUG, String.format(message, args));
        }
    }

    public void warn(String message, Object... args) {
        print(LogLevel.WARN, String.format(message, args));
    }

    public void error(String message, Object... args) {
        print(LogLevel.ERROR, String.format(message, args));
    }

    public void critical(String message, Object... args) {
        print(LogLevel.CRITICAL, String.format(message, args));
    }

    public void stop() {
        AnsiConsole.systemUninstall();
        if (fileWriter != null) {
            fileWriter.close();
        }
    }
}
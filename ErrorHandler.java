import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

public class ErrorHandler {
/**
 * 
 * @param catalogPath
 * @param lineContent
 * @param exception
 */
    public static void logCatalogLineError(Path catalogPath, String lineContent, Exception exception) {
        writeLogEntry(catalogPath, "INVALID LINE", lineContent, exception);
    }
/**
 * 
 * @param catalogPath
 * @param userInput
 * @param exception
 */
    public static void logInputError(Path catalogPath, String userInput, Exception exception) {
        writeLogEntry(catalogPath, "INVALID INPUT", userInput, exception);
    }
/**
 * 
 * @param catalogPath
 * @param typeOfError
 * @param textRelated
 * @param exception
 */
    private static void writeLogEntry(Path catalogPath,
                                      String typeOfError,
                                      String textRelated,
                                      Exception exception) {
        try {
            Path parentDir = catalogPath.getParent();
            if (parentDir == null) {
                parentDir = Path.of(".");
            }

            Path logPath = parentDir.resolve("errors.log");

            String logMessage =
                    "[" + LocalDateTime.now().withNano(0) + "] "
                            + typeOfError + ": "
                            + "\"" + textRelated + "\""
                            + System.lineSeparator()
                            + exception.getClass().getSimpleName()
                            + ": "
                            + exception.getMessage()
                            + System.lineSeparator();

            Files.write(logPath,
                    logMessage.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);

        } catch (IOException ioEx) {
            System.out.println("Unable to write to errors.log");
        }
    }
}
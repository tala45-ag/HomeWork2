import java.io.*;
import java.nio.file.*;
import java.util.*;

public class LibraryBookTracker {

    private static int validRecords = 0;
    private static int searchResults = 0;
    private static int booksAdded = 0;
    private static int errorsCount = 0;

    public static void main(String[] args) {

        List<Book> catalog = new ArrayList<>();
        Path catalogPath = Paths.get("catalog.txt"); 

        try {
            
            if (args.length < 2) {
                InsufficientArgumentsException e =
                        new InsufficientArgumentsException("Insufficient arguments");
                ErrorHandler.logInputError(catalogPath, "NO ARGUMENTS", e);
                throw e;
            }

            String fileName = args[0];
            String operation = args[1];

            
            if (!fileName.endsWith(".txt")) {
                InvalidFileNameException e =
                        new InvalidFileNameException("File must end with .txt");
                catalogPath = Paths.get(fileName);
                ErrorHandler.logInputError(catalogPath, fileName, e);
                throw e;
            }

            catalogPath = Paths.get(fileName);

            if (catalogPath.getParent() != null)
                Files.createDirectories(catalogPath.getParent());

            if (!Files.exists(catalogPath))
                Files.createFile(catalogPath);

            
            List<String> lines = Files.readAllLines(catalogPath);

            for (String line : lines) {
                try {
                    Book book = Book.parse(line);
                    catalog.add(book);
                    validRecords++;
                } catch (BookCatalogException e) {
                    ErrorHandler.logCatalogLineError(catalogPath, line, e);
                    errorsCount++;
                }
            }

     
            if (operation.matches("\\d{13}")) {

                List<Book> found = new ArrayList<>();
                for (Book b : catalog) {
                    if (b.getIsbn().equals(operation))
                        found.add(b);
                }

                if (found.size() > 1)
                    throw new DuplicateISBNException("Duplicate ISBN found");

                printHeader();
                for (Book b : found) {
                    System.out.println(b);
                    searchResults++;
                }

            } else if (operation.contains(":")) { 

                try {
                    Book newBook = Book.parse(operation);
                    catalog.add(newBook);
                    sortBooksByTitle(catalog);
                    booksAdded++;

                    List<String> updatedLines = new ArrayList<>();
                    for (Book b : catalog) {
                        updatedLines.add(
                                b.getTitle() + ":" +
                                b.getAuthor() + ":" +
                                b.getIsbn() + ":" +
                                b.getCopies());
                    }

                    Files.write(catalogPath, updatedLines);

                    printHeader();
                    System.out.println(newBook);

                } catch (BookCatalogException e) {
                    ErrorHandler.logInputError(catalogPath, operation, e);
                    errorsCount++;
                }

            } else { 
                printHeader();
                String keyword = operation.toLowerCase();

                for (Book b : catalog) {
                    if (b.getTitle().toLowerCase().contains(keyword)) {
                        System.out.println(b);
                        searchResults++;
                    }
                }
            }

        } catch (Exception e) { 
            System.out.println("Error: " + e.getMessage());
            ErrorHandler.logInputError(catalogPath, "PROGRAM ERROR", e);
            errorsCount++;
        } finally {
            System.out.println();
            System.out.println("Valid records processed: " + validRecords);
            System.out.println("Search results: " + searchResults);
            System.out.println("Books added: " + booksAdded);
            System.out.println("Errors encountered: " + errorsCount);
            System.out.println("Thank you for using the Library Book Tracker.");
        }
    }

    private static void printHeader() {
        System.out.printf("%-30s %-20s %-15s %5s%n",
                "Title", "Author", "ISBN", "Copies");
        System.out.println("---------------------------------------------------------------------");
    }
/**
 * 
 * @param books
 */
    public static void sortBooksByTitle(List<Book> books) {
        Collections.sort(books, Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER));
    }
}
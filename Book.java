public class Book  {

    private String title;
    private String author;
    private String isbn;
    private int copies;
/**
 * 
 * @param title
 * @param author
 * @param isbn
 * @param copies
 */
    public Book(String title, String author, String isbn, int copies) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.copies = copies;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public int getCopies() { return copies; }

    @Override
    public String toString() {
        return String.format("%-30s %-20s %-15s %5d",
                title, author, isbn, copies);
    }
/**
 * 
 * @param line
 * @return
 * @throws MalformedBookEntryException
 * @throws InvalidISBNException
 */
    public static Book parse(String line)
            throws MalformedBookEntryException, InvalidISBNException {

        String[] parts = line.split(":");

        if (parts.length != 4)
            throw new MalformedBookEntryException("Book entry must have 4 fields");

        String title = parts[0].trim();
        String author = parts[1].trim();
        String isbn = parts[2].trim();
        String copiesStr = parts[3].trim();

        if (title.isEmpty())
            throw new MalformedBookEntryException("Title is empty");

        if (author.isEmpty())
            throw new MalformedBookEntryException("Author is empty");

        if (!isbn.matches("\\d{13}"))
            throw new InvalidISBNException("ISBN must be exactly 13 digits");

        int copies;
        try {
            copies = Integer.parseInt(copiesStr);
        } catch (NumberFormatException e) {
            throw new MalformedBookEntryException("Copies is not a valid integer");
        }

        if (copies <= 0)
            throw new MalformedBookEntryException("Copies must be positive");

        return new Book(title, author, isbn, copies);
    }
}
import java.io.Serializable;

public class Book implements Serializable {
    private String bookId;
    private String title;
    private String author;
    private String category;
    private int quantity;
    private int availableCopies;

    public Book(String bookId, String title,
                String author, String category,
                int quantity) {

        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.quantity = quantity;
        this.availableCopies = quantity;
    }

    public String getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setQuantity(int quantity) {
        int issued = this.quantity - this.availableCopies;
        this.quantity = quantity;
        this.availableCopies = quantity - issued;
    }

    public void issueBook() {
        if (availableCopies > 0) {
            availableCopies--;
        }
    }

    public void returnBook() {
        if (availableCopies < quantity) {
            availableCopies++;
        }
    }
}

import java.util.ArrayList;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.Serializable;

class LibraryData implements Serializable {

    ArrayList<Book> books;
    ArrayList<Student> students;
    ArrayList<Transaction> transactions;

    int transactionCounter;

    public LibraryData(ArrayList<Book> books,
                       ArrayList<Student> students,
                       ArrayList<Transaction> transactions,
                       int transactionCounter) {

        this.books = books;
        this.students = students;
        this.transactions = transactions;
        this.transactionCounter = transactionCounter;
    }
}
public class LibraryManager {

    private ArrayList<Book> books = new ArrayList<>();
    private ArrayList<Student> students = new ArrayList<>();
    private ArrayList<Transaction> transactions = new ArrayList<>();

    private int transactionCounter = 1;

    // ---------------- BOOK MANAGEMENT ----------------

    public void addBook(Book book) {
        books.add(book);
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public Book findBook(String bookId) {
        for (Book book : books) {
            if (book.getBookId().equalsIgnoreCase(bookId)) {
                return book;
            }
        }
        return null;
    }

    public boolean bookExists(String bookId) {
        return findBook(bookId) != null;
    }

    public boolean deleteBook(String bookId) {
        Book book = findBook(bookId);

        if (book == null || book.getAvailableCopies() != book.getQuantity()) {
            return false;
        }

        books.remove(book);
        return true;
    }

    // ---------------- STUDENT MANAGEMENT ----------------

    public void addStudent(Student student) {
        students.add(student);
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public Student findStudent(String studentId) {
        for (Student student : students) {
            if (student.getStudentId().equalsIgnoreCase(studentId)) {
                return student;
            }
        }
        return null;
    }

    public boolean studentExists(String studentId) {
        return findStudent(studentId) != null;
    }

    public boolean deleteStudent(String studentId) {
        Student student = findStudent(studentId);

        if (student == null || hasActiveIssues(studentId)) {
            return false;
        }

        students.remove(student);
        return true;
    }

    // ---------------- TRANSACTION MANAGEMENT ----------------

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public boolean issueBook(String bookId, String studentId) {

        Book book = findBook(bookId);
        Student student = findStudent(studentId);

        if (book == null || student == null) {
            return false;
        }

        if (book.getAvailableCopies() <= 0) {
            return false;
        }

        // Prevent the same student from issuing the same book twice
        for (Transaction transaction : transactions) {
            if (transaction.getBookId().equalsIgnoreCase(bookId)
                    && transaction.getStudentId().equalsIgnoreCase(studentId)
                    && !transaction.isReturned()) {
                return false;
            }
        }

        String transactionId = "TXN" + transactionCounter;
        transactionCounter++;

        Transaction transaction =
                new Transaction(transactionId, bookId, studentId);

        book.issueBook();
        transactions.add(transaction);

        return true;
    }

    public boolean returnBook(String bookId, String studentId) {

        Book book = findBook(bookId);

        if (book == null || findStudent(studentId) == null) {
            return false;
        }

        for (Transaction transaction : transactions) {

            if (transaction.getBookId().equalsIgnoreCase(bookId)
                    && transaction.getStudentId().equalsIgnoreCase(studentId)
                    && !transaction.isReturned()) {

                book.returnBook();
                transaction.markReturned();

                return true;
            }
        }

        return false;
    }

    // Check whether a student has any books that are not returned
    public boolean hasActiveIssues(String studentId) {

        for (Transaction transaction : transactions) {

            if (transaction.getStudentId().equalsIgnoreCase(studentId)
                    && !transaction.isReturned()) {
                return true;
            }
        }

        return false;
    }

    // Count the total number of currently issued books
    public int getIssuedCount() {

        int count = 0;

        for (Transaction transaction : transactions) {
            if (!transaction.isReturned()) {
                count++;
            }
        }

        return count;
    }
    // ---------------- FILE HANDLING ----------------

public void saveData() {

    try {
        LibraryData data = new LibraryData(
            books,
            students,
            transactions,
            transactionCounter
        );

        ObjectOutputStream output =
            new ObjectOutputStream(
                new FileOutputStream("library_data.dat")
            );

        output.writeObject(data);
        output.close();

        System.out.println("Library data saved successfully!");

    } catch (IOException e) {
        System.out.println("Error saving library data: " + e.getMessage());
    }
}


public void loadData() {

    try {
        ObjectInputStream input =
            new ObjectInputStream(
                new FileInputStream("library_data.dat")
            );

        LibraryData data = (LibraryData) input.readObject();

        books = data.books;
        students = data.students;
        transactions = data.transactions;
        transactionCounter = data.transactionCounter;

        input.close();

        System.out.println("Library data loaded successfully!");

    } catch (IOException | ClassNotFoundException e) {
        System.out.println("No previous library data found. Starting fresh.");
    }
}
}


import java.time.LocalDate;
import java.io.Serializable;

public class Transaction implements Serializable {
    private String transactionId;
    private String bookId;
    private String studentId;

    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    private boolean returned;

    public Transaction(String transactionId, String bookId, String studentId) {

        this.transactionId = transactionId;
        this.bookId = bookId;
        this.studentId = studentId;

        issueDate = LocalDate.now();
        dueDate = issueDate.plusDays(14);

        returned = false;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getBookId() {
        return bookId;
    }

    public String getStudentId() {
        return studentId;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public boolean isReturned() {
        return returned;
    }

    public void markReturned() {
        returned = true;
        returnDate = LocalDate.now();
    }
}

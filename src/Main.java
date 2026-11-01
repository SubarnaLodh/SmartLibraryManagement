import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.regex.Pattern;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Main extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentPanel;

    private LibraryManager library = new LibraryManager();

    private final Color SIDEBAR = new Color(35, 52, 73);
    private final Color BACKGROUND = new Color(245, 247, 250);
    private final Color TEXT = new Color(40, 55, 75);
    private final Color BLUE = new Color(49, 105, 170);

    private JTable bookTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField searchField;

    // Student management variables
    private JTable studentTable;
    private DefaultTableModel studentTableModel;
    private TableRowSorter<DefaultTableModel> studentSorter;
    private JTextField studentSearchField;
    private JTable transactionTable;
    private DefaultTableModel transactionTableModel;
    public Main() {
        library.loadData();
        setTitle("Smart Library Management System");
        setSize(1000, 650);
        setMinimumSize(new Dimension(800, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setBackground(SIDEBAR);
        sidebar.setPreferredSize(new Dimension(200, 650));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JLabel logo = new JLabel("SMART LIBRARY");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Arial", Font.BOLD, 17));
        logo.setBorder(BorderFactory.createEmptyBorder(30, 15, 30, 10));
        sidebar.add(logo);

        String[] menus = {
            "Dashboard",
            "Manage Books",
            "Manage Students",
            "Issue Book",
            "Return Book",
            "Transaction History"
        };

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(BACKGROUND);

        for (String menu : menus) {

            JButton button = new JButton(menu);
            button.setMaximumSize(new Dimension(200, 45));
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setForeground(Color.WHITE);
            button.setBackground(SIDEBAR);
            button.setFont(new Font("Arial", Font.PLAIN, 14));
            button.setFocusPainted(false);
            button.setBorderPainted(false);

            button.addActionListener(e -> {
                cardLayout.show(contentPanel, menu);

                if (menu.equals("Manage Books")) {
                    refreshBookTable();
                }
                if (menu.equals("Manage Students")) {
                    refreshStudentTable();
               }
               if (menu.equals("Transaction History")) {
                    refreshTransactionTable();
             }
            });

            sidebar.add(button);

            contentPanel.add(createPage(menu), menu);
        }
    
// Logout Button
JButton logoutButton = new JButton("Logout");

logoutButton.setMaximumSize(new Dimension(200, 45));
logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
logoutButton.setForeground(Color.WHITE);
logoutButton.setBackground(new Color(180, 50, 50));
logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
logoutButton.setFocusPainted(false);
logoutButton.setBorderPainted(false);

logoutButton.addActionListener(e -> {

    int choice = JOptionPane.showConfirmDialog(
        this,
        "Are you sure you want to logout?",
        "Logout Confirmation",
        JOptionPane.YES_NO_OPTION
    );

    if (choice == JOptionPane.YES_OPTION) {

        dispose();

        new Login();
    }
});

sidebar.add(logoutButton);

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        cardLayout.show(contentPanel, "Dashboard");

        setVisible(true);
    }

    // Create application pages
    private JPanel createPage(String pageName) {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND);
        panel.setBorder(
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        );

        JLabel heading = new JLabel(pageName);
        heading.setFont(new Font("Arial", Font.BOLD, 25));
        heading.setForeground(TEXT);

        panel.add(heading, BorderLayout.NORTH);

        if (pageName.equals("Dashboard")) {
            panel.add(createDashboard(), BorderLayout.CENTER);
        }
        else if (pageName.equals("Manage Books")) {
            panel.add(createBooksPage(), BorderLayout.CENTER);
        }
        else if (pageName.equals("Manage Students")) {
            panel.add(createStudentsPage(), BorderLayout.CENTER);
        }
        else if (pageName.equals("Issue Book")) {
            panel.add(createIssuePage(), BorderLayout.CENTER);
        }
        else if (pageName.equals("Return Book")) {
            panel.add(createReturnPage(), BorderLayout.CENTER);
        }
        else if (pageName.equals("Transaction History")) {
            panel.add(createTransactionHistoryPage(), BorderLayout.CENTER);
        }
        else {
            JLabel message = new JLabel(
                pageName + " module will be developed next.",
                SwingConstants.CENTER
            );

            message.setFont(new Font("Arial", Font.PLAIN, 17));
            panel.add(message, BorderLayout.CENTER);
        }

        return panel;
    }

    // Dashboard
    private JPanel createDashboard() {

        JPanel dashboard = new JPanel(new GridLayout(2, 2, 15, 15));
        dashboard.setBackground(BACKGROUND);
        dashboard.setBorder(
            BorderFactory.createEmptyBorder(30, 0, 0, 0)
        );

        dashboard.add(createCard("Total Books", "0"));
        dashboard.add(createCard("Available Books", "0"));
        dashboard.add(createCard("Issued Books", "0"));
        dashboard.add(createCard("Total Students", "0"));

        return dashboard;
    }

    private JPanel createCard(String title, String value) {

        JPanel card = new JPanel(new GridLayout(2, 1));
        card.setBackground(Color.WHITE);
        card.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    new Color(225, 230, 237)
                ),
                BorderFactory.createEmptyBorder(15, 18, 15, 18)
            )
        );

        JLabel name = new JLabel(title);
        name.setForeground(Color.GRAY);
        name.setFont(new Font("Arial", Font.PLAIN, 15));

        JLabel number = new JLabel(value);
        number.setForeground(TEXT);
        number.setFont(new Font("Arial", Font.BOLD, 28));

        card.add(name);
        card.add(number);

        return card;
    }

    // Book management page
    private JPanel createBooksPage() {

        JPanel panel = new JPanel(new BorderLayout(10, 15));
        panel.setBackground(BACKGROUND);
        panel.setBorder(
            BorderFactory.createEmptyBorder(25, 0, 0, 0)
        );

        // Search section
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(BACKGROUND);

        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JButton resetButton = new JButton("Reset");

        topPanel.add(new JLabel("Search Book:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(resetButton);

        panel.add(topPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {
            "Book ID", "Title", "Author",
            "Category", "Quantity", "Available"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        bookTable = new JTable(tableModel);
        bookTable.setRowHeight(30);
        bookTable.setFont(new Font("Arial", Font.PLAIN, 13));
        bookTable.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 13)
        );

        sorter = new TableRowSorter<>(tableModel);
        bookTable.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(bookTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Action buttons
        JPanel buttonPanel = new JPanel(
            new FlowLayout(FlowLayout.LEFT, 10, 10)
        );
        buttonPanel.setBackground(BACKGROUND);

        JButton addButton = new JButton("Add Book");
        JButton editButton = new JButton("Edit Book");
        JButton deleteButton = new JButton("Delete Book");

        addButton.setBackground(BLUE);
        addButton.setForeground(Color.WHITE);

        editButton.setBackground(new Color(50, 140, 100));
        editButton.setForeground(Color.WHITE);

        deleteButton.setBackground(new Color(190, 65, 65));
        deleteButton.setForeground(Color.WHITE);

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Button actions
        addButton.addActionListener(e -> addBook());

        editButton.addActionListener(e -> editBook());

        deleteButton.addActionListener(e -> deleteBook());

        searchButton.addActionListener(e -> searchBooks());

        resetButton.addActionListener(e -> {
            searchField.setText("");
            sorter.setRowFilter(null);
        });

        return panel;
    }

    // Add book
    private void addBook() {

        JTextField id = new JTextField();
        JTextField title = new JTextField();
        JTextField author = new JTextField();
        JTextField category = new JTextField();
        JTextField quantity = new JTextField();

        Object[] fields = {
            "Book ID:", id,
            "Title:", title,
            "Author:", author,
            "Category:", category,
            "Quantity:", quantity
        };

        int result = JOptionPane.showConfirmDialog(
            this, fields, "Add New Book",
            JOptionPane.OK_CANCEL_OPTION
        );

        if (result != JOptionPane.OK_OPTION) return;

        try {

            String bookId = id.getText().trim();
            String bookTitle = title.getText().trim();
            String bookAuthor = author.getText().trim();
            String bookCategory = category.getText().trim();
            int qty = Integer.parseInt(quantity.getText().trim());

            if (bookId.isEmpty() || bookTitle.isEmpty()
                || bookAuthor.isEmpty() || bookCategory.isEmpty()) {

                JOptionPane.showMessageDialog(
                    this, "Please fill in all fields."
                );
                return;
            }

            if (qty <= 0) {
                JOptionPane.showMessageDialog(
                    this, "Quantity must be greater than zero."
                );
                return;
            }

            if (library.bookExists(bookId)) {
                JOptionPane.showMessageDialog(
                    this, "Book ID already exists."
                );
                return;
            }

            Book book = new Book(
                bookId, bookTitle, bookAuthor,
                bookCategory, qty
            );

            library.addBook(book);
            library.saveData();
            refreshBookTable();

            JOptionPane.showMessageDialog(
                this, "Book added successfully!"
            );

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                this, "Enter a valid number for quantity."
            );
        }
    }

    // Edit selected book
    private void editBook() {

        int selected = bookTable.getSelectedRow();

        if (selected == -1) {
            JOptionPane.showMessageDialog(
                this, "Please select a book first."
            );
            return;
        }

        int modelRow = bookTable.convertRowIndexToModel(selected);

        String id = tableModel.getValueAt(modelRow, 0).toString();
        Book book = library.findBook(id);

        JTextField title = new JTextField(book.getTitle());
        JTextField author = new JTextField(book.getAuthor());
        JTextField category = new JTextField(book.getCategory());
        JTextField quantity = new JTextField(
            String.valueOf(book.getQuantity())
        );

        Object[] fields = {
            "Title:", title,
            "Author:", author,
            "Category:", category,
            "Quantity:", quantity
        };

        int result = JOptionPane.showConfirmDialog(
            this, fields, "Edit Book",
            JOptionPane.OK_CANCEL_OPTION
        );

        if (result != JOptionPane.OK_OPTION) return;

        try {

            int qty = Integer.parseInt(quantity.getText().trim());

            if (title.getText().trim().isEmpty()
                || author.getText().trim().isEmpty()
                || category.getText().trim().isEmpty()
                || qty <= 0) {

                JOptionPane.showMessageDialog(
                    this, "Enter valid book details."
                );
                return;
            }

            int issued = book.getQuantity() -
                         book.getAvailableCopies();

            if (qty < issued) {
                JOptionPane.showMessageDialog(
                    this, "Quantity cannot be less than issued copies."
                );
                return;
            }

            book.setTitle(title.getText().trim());
            book.setAuthor(author.getText().trim());
            book.setCategory(category.getText().trim());
            book.setQuantity(qty);

            library.saveData();
            refreshBookTable();

            JOptionPane.showMessageDialog(
                this, "Book updated successfully!"
            );

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                this, "Enter a valid quantity."
            );
        }
    }

    // Delete selected book
    private void deleteBook() {

        int selected = bookTable.getSelectedRow();

        if (selected == -1) {
            JOptionPane.showMessageDialog(
                this, "Please select a book first."
            );
            return;
        }

        int modelRow = bookTable.convertRowIndexToModel(selected);

        String id = tableModel.getValueAt(modelRow, 0).toString();

        Book book = library.findBook(id);

        if (book.getAvailableCopies() != book.getQuantity()) {
            JOptionPane.showMessageDialog(
                this, "Cannot delete a book that is currently issued."
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this book?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            library.deleteBook(id);
            library.saveData();
            refreshBookTable();

            JOptionPane.showMessageDialog(
                this, "Book deleted successfully!"
            );
        }
    }

    // Search books
    private void searchBooks() {

        String search = searchField.getText().trim();

        if (search.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(
                RowFilter.regexFilter(
                    "(?i)" + Pattern.quote(search)
                )
            );
        }
    }

    // Refresh table
    private void refreshBookTable() {

        tableModel.setRowCount(0);

        for (Book book : library.getBooks()) {

            tableModel.addRow(new Object[] {
                book.getBookId(),
                book.getTitle(),
                book.getAuthor(),
                book.getCategory(),
                book.getQuantity(),
                book.getAvailableCopies()
            });
        }

        if (sorter != null) {
            sorter.setRowFilter(null);
        }

        if (searchField != null) {
            searchField.setText("");
        }
    }

    
    // ---------------- STUDENT MANAGEMENT PAGE ----------------

    private JPanel createStudentsPage() {

        JPanel panel = new JPanel(new BorderLayout(10, 15));
        panel.setBackground(BACKGROUND);

        panel.setBorder(
            BorderFactory.createEmptyBorder(25, 0, 0, 0)
        );

        // Search section
        JPanel topPanel = new JPanel(
            new FlowLayout(FlowLayout.LEFT)
        );

        topPanel.setBackground(BACKGROUND);

        studentSearchField = new JTextField(20);

        JButton searchButton = new JButton("Search");
        JButton resetButton = new JButton("Reset");

        topPanel.add(new JLabel("Search Student:"));
        topPanel.add(studentSearchField);
        topPanel.add(searchButton);
        topPanel.add(resetButton);

        panel.add(topPanel, BorderLayout.NORTH);

        // Student table
        String[] columns = {
            "Student ID", "Name", "Course",
            "Email", "Phone"
        };

        studentTableModel = new DefaultTableModel(
            columns, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        studentTable = new JTable(studentTableModel);

        studentTable.setRowHeight(30);

        studentTable.setFont(
            new Font("Arial", Font.PLAIN, 13)
        );

        studentTable.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 13)
        );

        studentSorter = new TableRowSorter<>(
            studentTableModel
        );

        studentTable.setRowSorter(studentSorter);

        JScrollPane scrollPane = new JScrollPane(studentTable);

        panel.add(scrollPane, BorderLayout.CENTER);

        // Action buttons
        JPanel buttonPanel = new JPanel(
            new FlowLayout(FlowLayout.LEFT, 10, 10)
        );

        buttonPanel.setBackground(BACKGROUND);

        JButton addButton = new JButton("Add Student");
        JButton editButton = new JButton("Edit Student");
        JButton deleteButton = new JButton("Delete Student");

        addButton.setBackground(BLUE);
        addButton.setForeground(Color.WHITE);

        editButton.setBackground(
            new Color(50, 140, 100)
        );
        editButton.setForeground(Color.WHITE);

        deleteButton.setBackground(
            new Color(190, 65, 65)
        );
        deleteButton.setForeground(Color.WHITE);

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Button actions
        addButton.addActionListener(e -> addStudent());

        editButton.addActionListener(e -> editStudent());

        deleteButton.addActionListener(e -> deleteStudent());

        searchButton.addActionListener(e -> searchStudents());

        resetButton.addActionListener(e -> {
            studentSearchField.setText("");
            studentSorter.setRowFilter(null);
        });

        return panel;
    }

    // ---------------- ADD STUDENT ----------------

    private void addStudent() {

        JTextField id = new JTextField();
        JTextField name = new JTextField();
        JTextField course = new JTextField();
        JTextField email = new JTextField();
        JTextField phone = new JTextField();

        Object[] fields = {
            "Student ID:", id,
            "Student Name:", name,
            "Course:", course,
            "Email:", email,
            "Phone:", phone
        };

        int result = JOptionPane.showConfirmDialog(
            this,
            fields,
            "Register New Student",
            JOptionPane.OK_CANCEL_OPTION
        );

        if (result != JOptionPane.OK_OPTION) return;

        String studentId = id.getText().trim();
        String studentName = name.getText().trim();
        String studentCourse = course.getText().trim();
        String studentEmail = email.getText().trim();
        String studentPhone = phone.getText().trim();

        if (studentId.isEmpty() || studentName.isEmpty()
            || studentCourse.isEmpty()
            || studentEmail.isEmpty()
            || studentPhone.isEmpty()) {

            JOptionPane.showMessageDialog(
                this, "Please fill in all fields."
            );
            return;
        }

        if (!studentEmail.matches(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        )) {
            JOptionPane.showMessageDialog(
                this, "Please enter a valid email address."
            );
            return;
        }

        if (!studentPhone.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(
                this, "Phone number must contain 10 digits."
            );
            return;
        }

        if (library.studentExists(studentId)) {
            JOptionPane.showMessageDialog(
                this, "Student ID already exists."
            );
            return;
        }

        Student student = new Student(
            studentId,
            studentName,
            studentCourse,
            studentEmail,
            studentPhone
        );

        library.addStudent(student);
        library.saveData();
        refreshStudentTable();

        JOptionPane.showMessageDialog(
            this, "Student registered successfully!"
        );
    }

    // ---------------- EDIT STUDENT ----------------

    private void editStudent() {

        int selected = studentTable.getSelectedRow();

        if (selected == -1) {
            JOptionPane.showMessageDialog(
                this, "Please select a student first."
            );
            return;
        }

        int modelRow =
            studentTable.convertRowIndexToModel(selected);

        String id = studentTableModel.getValueAt(
            modelRow, 0
        ).toString();

        Student student = library.findStudent(id);

        JTextField name = new JTextField(student.getName());
        JTextField course = new JTextField(student.getCourse());
        JTextField email = new JTextField(student.getEmail());
        JTextField phone = new JTextField(student.getPhone());

        Object[] fields = {
            "Student Name:", name,
            "Course:", course,
            "Email:", email,
            "Phone:", phone
        };

        int result = JOptionPane.showConfirmDialog(
            this,
            fields,
            "Edit Student",
            JOptionPane.OK_CANCEL_OPTION
        );

        if (result != JOptionPane.OK_OPTION) return;

        String newName = name.getText().trim();
        String newCourse = course.getText().trim();
        String newEmail = email.getText().trim();
        String newPhone = phone.getText().trim();

        if (newName.isEmpty() || newCourse.isEmpty()
            || newEmail.isEmpty() || newPhone.isEmpty()) {

            JOptionPane.showMessageDialog(
                this, "Please fill in all fields."
            );
            return;
        }

        if (!newEmail.matches(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        ) || !newPhone.matches("\\d{10}")) {

            JOptionPane.showMessageDialog(
                this, "Enter a valid email and 10-digit phone number."
            );
            return;
        }

        student.setName(newName);
        student.setCourse(newCourse);
        student.setEmail(newEmail);
        student.setPhone(newPhone);

        library.saveData();
        refreshStudentTable();

        JOptionPane.showMessageDialog(
            this, "Student updated successfully!"
        );
    }

    // ---------------- DELETE STUDENT ----------------

    private void deleteStudent() {

        int selected = studentTable.getSelectedRow();

        if (selected == -1) {
            JOptionPane.showMessageDialog(
                this, "Please select a student first."
            );
            return;
        }

        int modelRow =
            studentTable.convertRowIndexToModel(selected);

        String id = studentTableModel.getValueAt(
            modelRow, 0
        ).toString();

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this student?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            library.deleteStudent(id);
            library.saveData();
            refreshStudentTable();

            JOptionPane.showMessageDialog(
                this, "Student deleted successfully!"
            );
        }
    }

    // ---------------- SEARCH STUDENTS ----------------

    private void searchStudents() {

        String search = studentSearchField.getText().trim();

        if (search.isEmpty()) {
            studentSorter.setRowFilter(null);
        } else {
            studentSorter.setRowFilter(
                RowFilter.regexFilter(
                    "(?i)" + Pattern.quote(search)
                )
            );
        }
    }

    // ---------------- REFRESH STUDENT TABLE ----------------

    private void refreshStudentTable() {

        studentTableModel.setRowCount(0);

        for (Student student : library.getStudents()) {

            studentTableModel.addRow(new Object[] {
                student.getStudentId(),
                student.getName(),
                student.getCourse(),
                student.getEmail(),
                student.getPhone()
            });
        }

        studentSorter.setRowFilter(null);
        studentSearchField.setText("");
    }

    
    // ========== ISSUE BOOK PAGE ==========

    private JPanel createIssuePage() {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND);

        JPanel form = new JPanel(new GridLayout(3, 2, 15, 20));
        form.setBackground(Color.WHITE);
        form.setBorder(
            BorderFactory.createEmptyBorder(30, 35, 30, 35)
        );

        JTextField bookId = new JTextField(15);
        JTextField studentId = new JTextField(15);

        JButton issueButton = new JButton("Issue Book");

        issueButton.setBackground(BLUE);
        issueButton.setForeground(Color.WHITE);

        form.add(new JLabel("Book ID:"));
        form.add(bookId);

        form.add(new JLabel("Student ID:"));
        form.add(studentId);

        form.add(new JLabel(""));
        form.add(issueButton);

        issueButton.addActionListener(e -> {

            String bId = bookId.getText().trim();
            String sId = studentId.getText().trim();

            if (bId.isEmpty() || sId.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this, "Please enter both IDs."
                );
                return;
            }

            if (!library.bookExists(bId)) {
                JOptionPane.showMessageDialog(
                    this, "Book ID not found."
                );
                return;
            }

            if (!library.studentExists(sId)) {
                JOptionPane.showMessageDialog(
                    this, "Student ID not found."
                );
                return;
            }

            if (library.issueBook(bId, sId)) {

                library.saveData();

                JOptionPane.showMessageDialog(
                    this, "Book issued successfully!\n"
                    + "Due date: 14 days from today."
                );

                bookId.setText("");
                studentId.setText("");
                refreshBookTable();

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Book is unavailable or already issued "
                    + "to this student."
                );
            }
        });

        panel.add(form);

        return panel;
    }

    // ========== RETURN BOOK PAGE ==========

    private JPanel createReturnPage() {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND);

        JPanel form = new JPanel(new GridLayout(3, 2, 15, 20));
        form.setBackground(Color.WHITE);

        form.setBorder(
            BorderFactory.createEmptyBorder(30, 35, 30, 35)
        );

        JTextField bookId = new JTextField(15);
        JTextField studentId = new JTextField(15);

        JButton returnButton = new JButton("Return Book");

        returnButton.setBackground(
            new Color(50, 140, 100)
        );

        returnButton.setForeground(Color.WHITE);

        form.add(new JLabel("Book ID:"));
        form.add(bookId);

        form.add(new JLabel("Student ID:"));
        form.add(studentId);

        form.add(new JLabel(""));
        form.add(returnButton);

        returnButton.addActionListener(e -> {

            String bId = bookId.getText().trim();
            String sId = studentId.getText().trim();

            if (bId.isEmpty() || sId.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this, "Please enter both IDs."
                );
                return;
            }

            boolean returned = library.returnBook(bId, sId);

            if (returned) {

                library.saveData();
                JOptionPane.showMessageDialog(
                    this, "Book returned successfully!"
                );

                bookId.setText("");
                studentId.setText("");
                refreshBookTable();

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "No active issue record found for these IDs."
                );
            }
        });

        panel.add(form);

        return panel;
    }

private JPanel createTransactionHistoryPage() {

    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(BACKGROUND);

    String[] columns = {
        "Transaction ID",
        "Book ID",
        "Book Title",
        "Student ID",
        "Student Name",
        "Issue Date",
        "Due Date",
        "Return Date",
        "Status",
        "Fine (₹)"
    };

    transactionTableModel = new DefaultTableModel(columns, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    transactionTable = new JTable(transactionTableModel);

    transactionTable.setRowHeight(30);
    transactionTable.setFont(new Font("Arial", Font.PLAIN, 12));
    transactionTable.getTableHeader().setFont(
        new Font("Arial", Font.BOLD, 12)
    );

    JScrollPane scrollPane = new JScrollPane(transactionTable);

    panel.add(scrollPane, BorderLayout.CENTER);

    return panel;
}
private void refreshTransactionTable() {

    if (transactionTableModel == null) {
        return;
    }

    transactionTableModel.setRowCount(0);

    for (Transaction transaction : library.getTransactions()) {

        Book book = library.findBook(transaction.getBookId());
        Student student = library.findStudent(transaction.getStudentId());

        String bookTitle = (book != null) ? book.getTitle() : "-";
        String studentName = (student != null) ? student.getName() : "-";

        String returnDate = "-";
        LocalDate effectiveDate = LocalDate.now();

        if (transaction.isReturned()) {
            returnDate = transaction.getReturnDate().toString();
            effectiveDate = transaction.getReturnDate();
        }

        long daysLate = ChronoUnit.DAYS.between(
            transaction.getDueDate(),
            effectiveDate
        );

        long fine = 0;

        if (daysLate > 0) {
            fine = daysLate * 5;
        }

        String status;

        if (transaction.isReturned()) {
            status = "Returned";
        }
        else if (daysLate > 0) {
            status = "Overdue";
        }
        else {
            status = "Issued";
        }

        transactionTableModel.addRow(new Object[] {
            transaction.getTransactionId(),
            transaction.getBookId(),
            bookTitle,
            transaction.getStudentId(),
            studentName,
            transaction.getIssueDate(),
            transaction.getDueDate(),
            returnDate,
            status,
            "₹" + fine
        });
    }
}

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new Main();
        });
    }
}

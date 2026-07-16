package org.library.system.ui;

import org.library.system.model.Book;
import org.library.system.model.EBook;
import org.library.system.model.PhysicalBook;
import org.library.system.service.BookService;
import org.library.system.util.ServiceRegistry;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * GUI panel for catalog management: view, search, add, and remove books.
 */
public class BookPanel extends JPanel {

    private final DefaultTableModel tableModel;
    private final JTable bookTable;
    private final JTextField searchField;

    public BookPanel() {
        setLayout(new BorderLayout(8, 8));

        // --- Top: search bar ---
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JButton refreshButton = new JButton("Show All");
        searchPanel.add(new JLabel("Search title/author:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(refreshButton);
        add(searchPanel, BorderLayout.NORTH);

        // --- Center: book table ---
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Title", "Author", "Type", "Borrow Days"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        bookTable = new JTable(tableModel);
        add(new JScrollPane(bookTable), BorderLayout.CENTER);

        // --- Bottom: action buttons ---
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addPhysicalBtn = new JButton("Add Physical Book");
        JButton addEbookBtn = new JButton("Add E-Book");
        JButton removeBtn = new JButton("Remove Selected");
        actionPanel.add(addPhysicalBtn);
        actionPanel.add(addEbookBtn);
        actionPanel.add(removeBtn);
        add(actionPanel, BorderLayout.SOUTH);

        // --- Wiring ---
        searchButton.addActionListener(e -> doSearch());
        refreshButton.addActionListener(e -> loadAllBooks());
        addPhysicalBtn.addActionListener(e -> addPhysicalBook());
        addEbookBtn.addActionListener(e -> addEBook());
        removeBtn.addActionListener(e -> removeSelectedBook());

        loadAllBooks();
    }

    private BookService bookService() {
        return ServiceRegistry.getBookService();
    }

    private void loadAllBooks() {
        searchField.setText("");
        populateTable(bookService().searchBooks(""));
    }

    private void doSearch() {
        String query = searchField.getText().trim();
        populateTable(bookService().searchBooks(query));
    }

    private void populateTable(List<Book> books) {
        tableModel.setRowCount(0);
        for (Book b : books) {
            String type = (b instanceof PhysicalBook) ? "Physical" : "EBook";
            tableModel.addRow(new Object[]{
                    b.getBookId(), b.getTitle(), b.getAuthor(), type, b.getBorrowDurationDays()
            });
        }
    }

    private void addPhysicalBook() {
        JTextField idField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField publisherField = new JTextField();
        JTextField shelfField = new JTextField();
        JTextField copiesField = new JTextField();

        Object[] form = {
                "Book ID:", idField,
                "Title:", titleField,
                "Author:", authorField,
                "Publisher:", publisherField,
                "Shelf Location:", shelfField,
                "Total Copies:", copiesField
        };

        int result = JOptionPane.showConfirmDialog(this, form, "Add Physical Book",
                JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) return;

        if (idField.getText().isBlank() || titleField.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Book ID and Title are required.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int copies;
        try {
            copies = Integer.parseInt(copiesField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Total Copies must be a number.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Book book = new PhysicalBook(
                idField.getText().trim(), titleField.getText().trim(),
                authorField.getText().trim(), publisherField.getText().trim(),
                shelfField.getText().trim(), copies
        );

        boolean added = bookService().addBook(book);
        if (!added) {
            JOptionPane.showMessageDialog(this, "A book with that ID already exists.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        loadAllBooks();
    }

    private void addEBook() {
        JTextField idField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField publisherField = new JTextField();
        JTextField formatField = new JTextField();
        JTextField sizeField = new JTextField();
        JTextField urlField = new JTextField();

        Object[] form = {
                "Book ID:", idField,
                "Title:", titleField,
                "Author:", authorField,
                "Publisher:", publisherField,
                "File Format:", formatField,
                "File Size (MB):", sizeField,
                "Download URL:", urlField
        };

        int result = JOptionPane.showConfirmDialog(this, form, "Add E-Book",
                JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) return;

        if (idField.getText().isBlank() || titleField.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Book ID and Title are required.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double sizeMb;
        try {
            sizeMb = Double.parseDouble(sizeField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "File Size must be a number.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Book book = new EBook(
                idField.getText().trim(), titleField.getText().trim(),
                authorField.getText().trim(), publisherField.getText().trim(),
                formatField.getText().trim(), sizeMb, urlField.getText().trim()
        );

        boolean added = bookService().addBook(book);
        if (!added) {
            JOptionPane.showMessageDialog(this, "A book with that ID already exists.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        loadAllBooks();
    }

    private void removeSelectedBook() {
        int row = bookTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a book to remove first.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String bookId = (String) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Remove book \"" + tableModel.getValueAt(row, 1) + "\"?",
                "Confirm Removal", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            bookService().removeBook(bookId);
            loadAllBooks();
        }
    }
}
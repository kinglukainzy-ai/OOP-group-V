package org.library.system.ui;

import org.library.system.model.BorrowTransaction;
import org.library.system.service.BorrowTransactionService;
import org.library.system.util.ServiceRegistry;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * GUI panel for processing book check-outs and check-ins,
 * and viewing a member's active borrow transactions.
 */
public class BorrowTransactionPanel extends JPanel {

    private final DefaultTableModel tableModel;
    private final JTable transactionTable;
    private final JTextField memberIdField;
    private final JTextField bookIdField;
    private final JTextField lookupMemberField;

    public BorrowTransactionPanel() {
        setLayout(new BorderLayout(8, 8));

        // --- Top: check-out form ---
        JPanel checkoutPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        memberIdField = new JTextField(10);
        bookIdField = new JTextField(10);
        JButton checkoutBtn = new JButton("Check Out");
        checkoutPanel.add(new JLabel("Member ID:"));
        checkoutPanel.add(memberIdField);
        checkoutPanel.add(new JLabel("Book ID:"));
        checkoutPanel.add(bookIdField);
        checkoutPanel.add(checkoutBtn);
        add(checkoutPanel, BorderLayout.NORTH);

        // --- Center: active transactions table ---
        tableModel = new DefaultTableModel(
                new Object[]{"Transaction ID", "Book", "Member", "Borrow Date", "Due Date", "Overdue?"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        transactionTable = new JTable(tableModel);
        add(new JScrollPane(transactionTable), BorderLayout.CENTER);

        // --- Bottom: lookup + return ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lookupMemberField = new JTextField(10);
        JButton lookupBtn = new JButton("View Member's Active Loans");
        JButton returnBtn = new JButton("Return Selected");
        bottomPanel.add(new JLabel("Member ID:"));
        bottomPanel.add(lookupMemberField);
        bottomPanel.add(lookupBtn);
        bottomPanel.add(returnBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        // --- Wiring ---
        checkoutBtn.addActionListener(e -> checkOutBook());
        lookupBtn.addActionListener(e -> loadActiveTransactions());
        returnBtn.addActionListener(e -> returnSelectedBook());
    }

    private BorrowTransactionService transactionService() {
        return ServiceRegistry.getBorrowTransactionService();
    }

    private void checkOutBook() {
        String memberId = memberIdField.getText().trim();
        String bookId = bookIdField.getText().trim();

        if (memberId.isBlank() || bookId.isBlank()) {
            JOptionPane.showMessageDialog(this, "Member ID and Book ID are required.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            BorrowTransaction txn = transactionService().borrowBook(memberId, bookId);
            JOptionPane.showMessageDialog(this,
                    "Checked out successfully. Due: " + txn.getDueDate(),
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            memberIdField.setText("");
            bookIdField.setText("");
            lookupMemberField.setText(memberId);
            loadActiveTransactions();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Cannot Check Out", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadActiveTransactions() {
        String memberId = lookupMemberField.getText().trim();
        if (memberId.isBlank()) {
            JOptionPane.showMessageDialog(this, "Enter a Member ID to look up.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<BorrowTransaction> active = transactionService().getActiveTransactions(memberId);
        tableModel.setRowCount(0);
        for (BorrowTransaction t : active) {
            tableModel.addRow(new Object[]{
                    t.getTransactionId(),
                    t.getBook().getTitle(),
                    t.getMember().getName(),
                    t.getBorrowDate(),
                    t.getDueDate(),
                    t.isOverdue() ? "Yes" : "No"
            });
        }
    }

    private void returnSelectedBook() {
        int row = transactionTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a transaction to return first.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String transactionId = (String) tableModel.getValueAt(row, 0);
        transactionService().returnBook(transactionId);
        JOptionPane.showMessageDialog(this, "Book returned.",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        loadActiveTransactions();
    }
}
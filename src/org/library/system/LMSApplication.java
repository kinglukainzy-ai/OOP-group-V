package org.library.system;

import org.library.system.service.BookService;
import org.library.system.service.MemberService;
import org.library.system.service.BorrowTransactionService;
import org.library.system.service.BookServiceImpl;
import org.library.system.service.MemberServiceImpl;
import org.library.system.service.BorrowTransactionServiceImpl;
import org.library.system.ui.MainFrame;
import org.library.system.util.LibraryData;
import org.library.system.util.ServiceRegistry;

import javax.swing.*;

/**
 * Application entry point. Wires up the data layer, service layer,
 * and registers services in ServiceRegistry before launching the GUI.
 */
public class LMSApplication {

    public static void main(String[] args) {
        // 1. Initialize data store
        LibraryData data = new LibraryData();
        data.loadDataFromFiles();

        // Save on normal JVM exit (window close, System.exit(), Ctrl+C).
        Runtime.getRuntime().addShutdownHook(new Thread(data::saveDataToFiles));

        // 2. Initialize logic implementations
        BookService bookService = new BookServiceImpl(data);
        MemberService memberService = new MemberServiceImpl(data);
        BorrowTransactionService borrowTransactionService = new BorrowTransactionServiceImpl(data);

        // 3. Register services
        ServiceRegistry.initialize(bookService, memberService, borrowTransactionService);

        // 4. Start GUI
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
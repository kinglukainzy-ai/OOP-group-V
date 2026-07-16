package org.library.system.util;

import org.library.system.service.BookService;
import org.library.system.service.BorrowTransactionService;
import org.library.system.service.MemberService;

/**
 * Global registry to access service instances from the GUI.
 * Initialized once at application startup (see LMSApplication.main()),
 * then used by ui/ panels to reach the business logic layer without
 * hardcoding new service instances everywhere.
 */
public class ServiceRegistry {

    private static BookService bookService;
    private static MemberService memberService;
    private static BorrowTransactionService borrowTransactionService;

    // Prevent instantiation - this is a static-only utility class
    private ServiceRegistry() {}

    /**
     * Registers the concrete service implementations.
     * Must be called once, before any UI code calls the getters below.
     */
    public static void initialize(BookService bs, MemberService ms, BorrowTransactionService bts) {
        bookService = bs;
        memberService = ms;
        borrowTransactionService = bts;
    }

    public static BookService getBookService() {
        checkInitialized(bookService, "BookService");
        return bookService;
    }

    public static MemberService getMemberService() {
        checkInitialized(memberService, "MemberService");
        return memberService;
    }

    public static BorrowTransactionService getBorrowTransactionService() {
        checkInitialized(borrowTransactionService, "BorrowTransactionService");
        return borrowTransactionService;
    }

    private static void checkInitialized(Object service, String name) {
        if (service == null) {
            throw new IllegalStateException(
                    name + " requested before ServiceRegistry.initialize() was called.");
        }
    }
}
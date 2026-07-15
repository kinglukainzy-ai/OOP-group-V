# Handover & Integration Guide: Data Utility Layer
## Team Guidelines for using `LibraryData` and `FileIOHelper`

This document details how the Architecture, Backend, and Frontend teams must interact with the utility layer (`org.library.system.util`) to ensure data persistence, avoid compilation errors, and prevent runtime issues.

---

## 1. Overview of the Data Utility Layer

The utility package handles loading and saving application data to local CSV files. It consists of two primary classes:
1. **`FileIOHelper.java`**: Handles raw text reading and writing, parsing columns, and recreating objects and relationships.
2. **`LibraryData.java`**: Holds the runtime in-memory instance of `Library` and coordinates save/load operations.

---

## 2. Guidelines for the Architecture Team (`org.library.system.model`)

### CSV Column Order
The file loader relies on fixed index columns inside `data/books.csv`, `data/members.csv`, and `data/transactions.csv`. If you add, delete, or re-order fields in the model classes, you **must** update the index parser in `FileIOHelper.java`.

### File Serialization Structure
* **`books.csv`**: Saves generic books using a `type` flag (`PHYSICAL` or `EBOOK`):
  * **PhysicalBook**: `PHYSICAL,bookId,title,author,publisher,shelfLocation,totalCopies,availableCopies`
  * **EBook**: `EBOOK,bookId,title,author,publisher,fileFormat,fileSizeMb,downloadUrl`
* **`members.csv`**: Saves member objects:
  * `memberId,name,email,isSuspended`
* **`transactions.csv`**: Saves transactions using references to the `bookId` and `memberId`:
  * `transactionId,bookId,memberId,borrowDate,dueDate,returnDate`

---

## 3. Guidelines for the Backend Team (`org.library.system.service`)

### Avoid Multiple Library Instances
Do **not** instantiate new `Library` objects (e.g., `new Library()`) inside your services (`BookServiceImpl`, `MemberServiceImpl`, `BorrowTransactionServiceImpl`). The application must share a single `Library` instance so that database changes made in one service are visible to others.

### Dependency Injection Pattern
Inject the `LibraryData` instance into your services at startup. 

**Example Implementation Pattern:**
```java
package org.library.system.service;

import org.library.system.model.Library;
import org.library.system.util.LibraryData;

public class BookServiceImpl implements BookService {
    private final Library library;

    // The single database context is passed in here
    public BookServiceImpl(LibraryData libraryData) {
        this.library = libraryData.getLibrary();
    }

    @Override
    public boolean addBook(Book book) {
        // Query or modify the shared library lists directly
        library.addBook(book);
        return true;
    }
}
```

---

## 4. Guidelines for the Frontend Team (`org.library.system.ui`)

### Load on Application Startup
Before loading and displaying the GUI frame, you must initialize `LibraryData` and trigger file loading so the panels are populated with existing records.

```java
// Inside main() method:
LibraryData data = new LibraryData();
data.loadDataFromFiles(); // Loads books, members, and transactions from CSV
```

### Save on Application Exit (Crucial!)
Because the system runs in memory, changes are only written to files when explicitly requested. You **must** call `saveDataToFiles()` when the user closes the desktop application (e.g., in a Window Listener callback for your main Swing JFrame or JavaFX stage).

```java
// Triggered on window closing event
data.saveDataToFiles(); // Writes current state back to CSV files
```

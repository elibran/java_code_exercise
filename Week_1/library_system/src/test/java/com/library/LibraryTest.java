package com.library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.library.exception.BookNotAvailableException;
import com.library.exception.BookNotFoundException;
import com.library.exception.MemberNotFoundException;
import com.library.model.Book;
import com.library.model.Member;
import com.library.service.Library;

import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;

public class LibraryTest {
    private Library library;
    private Book testBook1;
    private Book testBook2;
    private Member testMember1;
    private Member testMember2;
    
    @BeforeEach
    void setUp() {
        library = new Library();
        testBook1 = new Book("B001", "Test Book 1", "Test Author 1", "123456789");
        testBook2 = new Book("B002", "Test Book 2", "Test Author 2", "987654321");
        testMember1 = new Member("M001", "John Doe", "john@test.com");
        testMember2 = new Member("M002", "Jane Smith", "jane@test.com");
        
        library.addBook(testBook1);
        library.addBook(testBook2);
        library.addMember(testMember1);
        library.addMember(testMember2);
    }
    
    @Test
    @DisplayName("Test Book Creation and Getters")
    void testBookCreation() {
        assertEquals("B001", testBook1.getBookId());
        assertEquals("Test Book 1", testBook1.getTitle());
        assertEquals("Test Author 1", testBook1.getAuthor());
        assertEquals("123456789", testBook1.getIsbn());
        assertTrue(testBook1.isAvailable());
    }
    
    @Test
    @DisplayName("Test Book Equals and HashCode")
    void testBookEquality() {
        Book book1 = new Book("B001", "Different Title", "Different Author", "Different ISBN");
        Book book2 = new Book("B002", "Test Book 1", "Test Author 1", "123456789");
        
        assertEquals(testBook1, book1); // Same bookId
        assertNotEquals(testBook1, book2); // Different bookId
        assertEquals(testBook1.hashCode(), book1.hashCode());
    }
    
    @Test
    @DisplayName("Test Member Creation and Book Management")
    void testMemberBookManagement() {
        assertEquals("M001", testMember1.getMemberId());
        assertEquals("John Doe", testMember1.getName());
        assertEquals("john@test.com", testMember1.getEmail());
        assertTrue(testMember1.getBorrowedBooks().isEmpty());
        
        testMember1.addBorrowedBook(testBook1);
        assertEquals(1, testMember1.getBorrowedBooks().size());
        assertTrue(testMember1.getBorrowedBooks().contains(testBook1));
        
        testMember1.removeBorrowedBook(testBook1);
        assertTrue(testMember1.getBorrowedBooks().isEmpty());
    }
    
    @Test
    @DisplayName("Test Successful Book Borrowing")
    void testSuccessfulBorrowBook() {
        assertDoesNotThrow(() -> {
            library.borrowBook("M001", "B001");
        });
        
        assertFalse(testBook1.isAvailable());
        assertEquals(1, testMember1.getBorrowedBooks().size());
        assertTrue(testMember1.getBorrowedBooks().contains(testBook1));
    }
    
    @Test
    @DisplayName("Test Book Not Found Exception")
    void testBookNotFoundException() {
        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> {
            library.borrowBook("M001", "B999");
        });
        
        assertTrue(exception.getMessage().contains("B999"));
        assertTrue(exception.getMessage().contains("not found"));
    }
    
    @Test
    @DisplayName("Test Member Not Found Exception")
    void testMemberNotFoundException() {
        MemberNotFoundException exception = assertThrows(MemberNotFoundException.class, () -> {
            library.borrowBook("M999", "B001");
        });
        
        assertTrue(exception.getMessage().contains("M999"));
        assertTrue(exception.getMessage().contains("not found"));
    }
    
    @Test
    @DisplayName("Test Book Not Available Exception")
    void testBookNotAvailableException() {
        // First borrowing should succeed
        assertDoesNotThrow(() -> {
            library.borrowBook("M001", "B001");
        });
        
        // Second borrowing should throw exception
        BookNotAvailableException exception = assertThrows(BookNotAvailableException.class, () -> {
            library.borrowBook("M002", "B001");
        });
        
        assertTrue(exception.getMessage().contains("not available"));
    }
    
    @Test
    @DisplayName("Test Book Return")
    void testReturnBook() {
        // Borrow book first
        assertDoesNotThrow(() -> {
            library.borrowBook("M001", "B001");
        });
        
        assertFalse(testBook1.isAvailable());
        
        // Return book
        assertDoesNotThrow(() -> {
            library.returnBook("M001", "B001");
        });
        
        assertTrue(testBook1.isAvailable());
        assertTrue(testMember1.getBorrowedBooks().isEmpty());
    }
    
    @Test
    @DisplayName("Test Search Books by Author")
    void testSearchBooksByAuthor() {
        // Add another book by same author
        Book book3 = new Book("B003", "Test Book 3", "Test Author 1", "111111111");
        library.addBook(book3);
        
        List<Book> books = library.searchBooksByAuthor("Test Author 1");
        assertEquals(2, books.size());
        assertTrue(books.contains(testBook1));
        assertTrue(books.contains(book3));
        
        // Test case insensitive search
        List<Book> booksLowerCase = library.searchBooksByAuthor("test author 1");
        assertEquals(2, booksLowerCase.size());
        
        // Test no results
        List<Book> noBooks = library.searchBooksByAuthor("Non-existent Author");
        assertTrue(noBooks.isEmpty());
    }
    
    @Test
    @DisplayName("Test Get Members Borrowing Books")
    void testGetMembersBorrowingBooks() {
        // Initially no members borrowing books
        List<Member> members = library.getMembersBorrowingBooks();
        assertTrue(members.isEmpty());
        
        // Borrow some books
        assertDoesNotThrow(() -> {
            library.borrowBook("M001", "B001");
            library.borrowBook("M002", "B002");
        });
        
        members = library.getMembersBorrowingBooks();
        assertEquals(2, members.size());
        assertTrue(members.contains(testMember1));
        assertTrue(members.contains(testMember2));
    }
    
    @Test
    @DisplayName("Test Multiple Books Borrowing by Same Member")
    void testMultipleBorrowingBySameMember() {
        assertDoesNotThrow(() -> {
            library.borrowBook("M001", "B001");
            library.borrowBook("M001", "B002");
        });
        
        assertEquals(2, testMember1.getBorrowedBooks().size());
        assertFalse(testBook1.isAvailable());
        assertFalse(testBook2.isAvailable());
    }
    
    @Test
    @DisplayName("Test File Save and Load Operations")
    void testFileOperations() {
        String testFilename = "test_library_data.txt";
        
        // Borrow some books to create test data
        assertDoesNotThrow(() -> {
            library.borrowBook("M001", "B001");
            library.borrowBook("M002", "B002");
        });
        
        // Save data
        library.saveLibraryData(testFilename);
        
        // Create new library and load data
        Library newLibrary = new Library();
        newLibrary.loadLibraryData(testFilename);
        
        // Verify data was loaded correctly
        assertEquals(2, newLibrary.getBooks().size());
        assertEquals(2, newLibrary.getMembers().size());
        
        // Verify book availability status
        Book loadedBook1 = newLibrary.getBooks().get("B001");
        Book loadedBook2 = newLibrary.getBooks().get("B002");
        assertNotNull(loadedBook1);
        assertNotNull(loadedBook2);
        assertFalse(loadedBook1.isAvailable()); // Should be borrowed
        assertFalse(loadedBook2.isAvailable()); // Should be borrowed
        
        // Cleanup test file
        File testFile = new File(testFilename);
        if (testFile.exists()) {
            testFile.delete();
        }
    }
    
    @Test
    @DisplayName("Test Book toString Method")
    void testBookToString() {
        String expected = "B001: Test Book 1 by Test Author 1 (ISBN: 123456789) - Available: true";
        assertEquals(expected, testBook1.toString());
        
        testBook1.setAvailable(false);
        String expectedUnavailable = "B001: Test Book 1 by Test Author 1 (ISBN: 123456789) - Available: false";
        assertEquals(expectedUnavailable, testBook1.toString());
    }
    
    @Test
    @DisplayName("Test Member toString Method")
    void testMemberToString() {
        String expected = "John Doe (M001): john@test.com - 0 books borrowed";
        assertEquals(expected, testMember1.toString());
        
        testMember1.addBorrowedBook(testBook1);
        String expectedWithBook = "John Doe (M001): john@test.com - 1 books borrowed";
        assertEquals(expectedWithBook, testMember1.toString());
    }
    
    @Test
    @DisplayName("Test Edge Cases - Empty Author Search")
    void testEmptyAuthorSearch() {
        List<Book> books = library.searchBooksByAuthor("");
        assertTrue(books.isEmpty());
        
        List<Book> booksNull = library.searchBooksByAuthor(null);
        assertTrue(booksNull.isEmpty());
    }
    
    @Test
    @DisplayName("Test Book Setters")
    void testBookSetters() {
        testBook1.setTitle("Updated Title");
        testBook1.setAuthor("Updated Author");
        testBook1.setIsbn("Updated ISBN");
        testBook1.setAvailable(false);
        
        assertEquals("Updated Title", testBook1.getTitle());
        assertEquals("Updated Author", testBook1.getAuthor());
        assertEquals("Updated ISBN", testBook1.getIsbn());
        assertFalse(testBook1.isAvailable());
    }
    
    @Test
    @DisplayName("Test Member Setters")
    void testMemberSetters() {
        testMember1.setName("Updated Name");
        testMember1.setEmail("updated@email.com");
        testMember1.setMemberId("M999");
        
        assertEquals("Updated Name", testMember1.getName());
        assertEquals("updated@email.com", testMember1.getEmail());
        assertEquals("M999", testMember1.getMemberId());
    }
    
    @Test
    @DisplayName("Test Return Book - Member Not Found")
    void testReturnBookMemberNotFound() {
        MemberNotFoundException exception = assertThrows(MemberNotFoundException.class, () -> {
            library.returnBook("M999", "B001");
        });
        
        assertTrue(exception.getMessage().contains("M999"));
        assertTrue(exception.getMessage().contains("not found"));
    }
    
    @Test
    @DisplayName("Test Return Book - Book Not Found")
    void testReturnBookBookNotFound() {
        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> {
            library.returnBook("M001", "B999");
        });
        
        assertTrue(exception.getMessage().contains("B999"));
        assertTrue(exception.getMessage().contains("not found"));
    }
    
    @Test
    @DisplayName("Test Complex Borrowing and Returning Scenario")
    void testComplexBorrowingScenario() {
        // Member 1 borrows both books
        assertDoesNotThrow(() -> {
            library.borrowBook("M001", "B001");
            library.borrowBook("M001", "B002");
        });
        
        assertEquals(2, testMember1.getBorrowedBooks().size());
        assertFalse(testBook1.isAvailable());
        assertFalse(testBook2.isAvailable());
        
        // Member 1 returns one book
        assertDoesNotThrow(() -> {
            library.returnBook("M001", "B001");
        });
        
        assertEquals(1, testMember1.getBorrowedBooks().size());
        assertTrue(testBook1.isAvailable());
        assertFalse(testBook2.isAvailable());
        
        // Member 2 can now borrow the returned book
        assertDoesNotThrow(() -> {
            library.borrowBook("M002", "B001");
        });
        
        assertEquals(1, testMember2.getBorrowedBooks().size());
        assertFalse(testBook1.isAvailable());
        
        // Check members with borrowed books
        List<Member> membersWithBooks = library.getMembersBorrowingBooks();
        assertEquals(2, membersWithBooks.size());
    }
    
    @Test
    @DisplayName("Test File Operations with Non-existent File")
    void testLoadNonExistentFile() {
        // This should not throw an exception, just print an error message
        assertDoesNotThrow(() -> {
            library.loadLibraryData("non_existent_file.txt");
        });
    }
    
    @Test
    @DisplayName("Test Custom Exception Messages")
    void testCustomExceptionMessages() {
        BookNotFoundException bookNotFound = new BookNotFoundException("Custom book message");
        assertEquals("Custom book message", bookNotFound.getMessage());
        
        BookNotAvailableException bookNotAvailable = new BookNotAvailableException("Custom availability message");
        assertEquals("Custom availability message", bookNotAvailable.getMessage());
        
        MemberNotFoundException memberNotFound = new MemberNotFoundException("Custom member message");
        assertEquals("Custom member message", memberNotFound.getMessage());
    }
    
    @Test
    @DisplayName("Test Library State After Operations")
    void testLibraryState() {
        // Initial state
        assertEquals(2, library.getBooks().size());
        assertEquals(2, library.getMembers().size());
        
        // Add more books and members
        Book newBook = new Book("B003", "New Book", "New Author", "999999999");
        Member newMember = new Member("M003", "New Member", "new@email.com");
        
        library.addBook(newBook);
        library.addMember(newMember);
        
        assertEquals(3, library.getBooks().size());
        assertEquals(3, library.getMembers().size());
        
        // Test that all books are initially available
        for (Book book : library.getBooks().values()) {
            assertTrue(book.isAvailable());
        }
        
        // Test that all members have no borrowed books initially
        for (Member member : library.getMembers().values()) {
            assertTrue(member.getBorrowedBooks().isEmpty());
        }
    }
}
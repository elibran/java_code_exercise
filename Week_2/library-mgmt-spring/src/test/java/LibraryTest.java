//package com.library;
//
//import com.library.model.Book;
//import com.library.model.Member;
//import com.library.service.LibraryService;
//import com.library.exception.*;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.DisplayName;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.util.List;
//
//@SpringBootTest
//@ActiveProfiles("test")
//@Transactional
//class LibraryServiceTest {
//
//    @Autowired
//    private LibraryService libraryService;
//
//    @Test
//    @DisplayName("Test Add and Retrieve Book")
//    void testAddAndRetrieveBook() {
//        // Given
//        Book book = new Book("TEST001", "Test Book", "Test Author", "123456789");
//
//        // When
//        Book savedBook = libraryService.addBook(book);
//
//        // Then
//        assertNotNull(savedBook);
//        assertEquals("TEST001", savedBook.getBookId());
//        assertEquals("Test Book", savedBook.getTitle());
//        assertTrue(savedBook.isAvailable());
//    }
//
//    @Test
//    @DisplayName("Test Add and Retrieve Member")
//    void testAddAndRetrieveMember() {
//        // Given
//        Member member = new Member("TEST001", "Test Member", "test@email.com");
//
//        // When
//        Member savedMember = libraryService.addMember(member);
//
//        // Then
//        assertNotNull(savedMember);
//        assertEquals("TEST001", savedMember.getMemberId());
//        assertEquals("Test Member", savedMember.getName());
//        assertEquals("test@email.com", savedMember.getEmail());
//    }
//
//    @Test
//    @DisplayName("Test Successful Book Borrowing")
//    void testSuccessfulBorrowBook() {
//        // Given
//        Book book = libraryService.addBook(new Book("TEST002", "Test Book 2", "Test Author", "987654321"));
//        Member member = libraryService.addMember(new Member("TEST002", "Test Member 2", "test2@email.com"));
//
//        // When & Then
//        assertDoesNotThrow(() -> {
//            libraryService.borrowBook("TEST002", "TEST002");
//        });
//
//        // Verify book is no longer available
//        Book borrowedBook = libraryService.getBookById("TEST002").orElse(null);
//        assertNotNull(borrowedBook);
//        assertFalse(borrowedBook.isAvailable());
//
//        // Verify member has the book
//        Member updatedMember = libraryService.getMemberById("TEST002").orElse(null);
//        assertNotNull(updatedMember);
//        assertEquals(1, updatedMember.getBorrowedBooks().size());
//    }
//
//    @Test
//    @DisplayName("Test Book Not Found Exception")
//    void testBookNotFoundException() {
//        // Given
//        Member member = libraryService.addMember(new Member("TEST003", "Test Member 3", "test3@email.com"));
//
//        // When & Then
//        assertThrows(BookNotFoundException.class, () -> {
//            libraryService.borrowBook("TEST003", "NONEXISTENT");
//        });
//    }
//
//    @Test
//    @DisplayName("Test Search Books by Author")
//    void testSearchBooksByAuthor() {
//        // Given
//        libraryService.addBook(new Book("SEARCH001", "Book 1", "Search Author", "111111111"));
//        libraryService.addBook(new Book("SEARCH002", "Book 2", "Search Author", "222222222"));
//        libraryService.addBook(new Book("SEARCH003", "Book 3", "Different Author", "333333333"));
//
//        // When
//        List<Book> books = libraryService.searchBooksByAuthor("Search Author");
//
//        // Then
//        assertEquals(2, books.size());
//        assertTrue(books.stream().allMatch(book -> "Search Author".equals(book.getAuthor())));
//    }
//}
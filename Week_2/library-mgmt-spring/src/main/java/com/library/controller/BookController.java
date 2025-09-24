//package com.library.controller;
//
//import com.library.model.Book;
//import com.library.service.LibraryService;
//import com.library.exception.BookNotFoundException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import jakarta.validation.Valid;
//
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/books")
//@CrossOrigin(origins = "*")
//public class BookController {
//
//    private final LibraryService libraryService;
//
//    @Autowired
//    public BookController(LibraryService libraryService) {
//        this.libraryService = libraryService;
//    }
//
//    @GetMapping
//    public ResponseEntity<List<Book>> getAllBooks() {
//        List<Book> books = libraryService.getAllBooks();
//        return ResponseEntity.ok(books);
//    }
//
//    @GetMapping("/{bookId}")
//    public ResponseEntity<Book> getBookById(@PathVariable String bookId) {
//        Optional<Book> book = libraryService.getBookById(bookId);
//        return book.map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @GetMapping("/available")
//    public ResponseEntity<List<Book>> getAvailableBooks() {
//        List<Book> books = libraryService.getAvailableBooks();
//        return ResponseEntity.ok(books);
//    }
//
//    @GetMapping("/search")
//    public ResponseEntity<List<Book>> searchBooksByAuthor(@RequestParam String author) {
//        List<Book> books = libraryService.searchBooksByAuthor(author);
//        return ResponseEntity.ok(books);
//    }
//
//    @PostMapping
//    public ResponseEntity<Book> addBook(@Valid @RequestBody Book book) {
//        Book savedBook = libraryService.addBook(book);
//        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
//    }
//
//    @PutMapping("/{bookId}")
//    public ResponseEntity<Book> updateBook(@PathVariable String bookId, @Valid @RequestBody Book book) {
//        book.setBookId(bookId);
//        Book updatedBook = libraryService.addBook(book); // save() works for update too
//        return ResponseEntity.ok(updatedBook);
//    }
//
//    @DeleteMapping("/{bookId}")
//    public ResponseEntity<Void> deleteBook(@PathVariable String bookId) {
//        try {
//            libraryService.deleteBook(bookId);
//            return ResponseEntity.noContent().build();
//        } catch (BookNotFoundException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//}
package com.library.service;

import com.library.exception.BookNotFoundException;
import com.library.model.Book;
import com.library.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }
        public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(String bookId) {
        return bookRepository.findById(bookId);
    }

    public List<Book> getAvailableBooks() {
        return bookRepository.findByIsAvailableTrue();
    }


    public List<Book> searchBooksByAuthor(String author) {
        return bookRepository.findByAuthorIgnoreCase(author);
    }
    public void deleteBook(String bookId) throws BookNotFoundException {
        if (!bookRepository.existsById(bookId)) {
            throw new BookNotFoundException("Book with ID " + bookId + " not found");
        }
        bookRepository.deleteById(bookId);
    }




}

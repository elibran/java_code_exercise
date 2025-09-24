package com.library.service;

import com.library.model.Book;
import com.library.model.Member;
import com.library.repository.BookRepository;
import com.library.repository.LibraryRepository;
import com.library.repository.MemberRepository;
import com.library.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LibraryService {
    private final LibraryRepository libraryRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;

    public LibraryService(LibraryRepository libraryRepository, MemberRepository memberRepository, BookRepository bookRepository) {
        this.libraryRepository = libraryRepository;
        this.memberRepository = memberRepository;
        this.bookRepository = bookRepository;
    }

    // Borrowing operations
    public void borrowBook(String memberId, String bookId)
            throws MemberNotFoundException, BookNotFoundException, BookNotAvailableException {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member with ID " + memberId + " not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book with ID " + bookId + " not found"));

        if (!book.isAvailable()) {
            throw new BookNotAvailableException("Book \"" + book.getTitle() + "\" is not available for borrowing");
        }

        book.setAvailable(false);
        member.addBorrowedBook(book);

        bookRepository.save(book);
        memberRepository.save(member);
    }

    public void returnBook(String memberId, String bookId)
            throws MemberNotFoundException, BookNotFoundException {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member with ID " + memberId + " not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book with ID " + bookId + " not found"));

        book.setAvailable(true);
        member.removeBorrowedBook(book);

        bookRepository.save(book);
        memberRepository.save(member);
    }
}
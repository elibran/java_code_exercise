//package com.library.service;
//
//import com.library.model.Book;
//import com.library.model.Member;
//import com.library.repository.BookRepository;
//import com.library.repository.MemberRepository;
//import com.library.exception.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@Transactional
//public class LibraryService {
//
//    private final BookRepository bookRepository;
//    private final MemberRepository memberRepository;
//
//    @Autowired
//    public LibraryService(BookRepository bookRepository, MemberRepository memberRepository) {
//        this.bookRepository = bookRepository;
//        this.memberRepository = memberRepository;
//    }
//
//    // Book operations
//    public Book addBook(Book book) {
//        return bookRepository.save(book);
//    }
//
//    public List<Book> getAllBooks() {
//        return bookRepository.findAll();
//    }
//
//    public Optional<Book> getBookById(String bookId) {
//        return bookRepository.findById(bookId);
//    }
//
//    public List<Book> getAvailableBooks() {
//        return bookRepository.findByIsAvailableTrue();
//    }
//
//    public List<Book> searchBooksByAuthor(String author) {
//        return bookRepository.findByAuthorIgnoreCase(author);
//    }
//
//    public void deleteBook(String bookId) throws BookNotFoundException {
//        if (!bookRepository.existsById(bookId)) {
//            throw new BookNotFoundException("Book with ID " + bookId + " not found");
//        }
//        bookRepository.deleteById(bookId);
//    }
//
//    // Member operations
//    public Member addMember(Member member) {
//        return memberRepository.save(member);
//    }
//
//    public List<Member> getAllMembers() {
//        return memberRepository.findAll();
//    }
//
//    public Optional<Member> getMemberById(String memberId) {
//        return memberRepository.findById(memberId);
//    }
//
//    public List<Member> getMembersWithBorrowedBooks() {
//        return memberRepository.findMembersWithBorrowedBooks();
//    }
//
//    public void deleteMember(String memberId) throws MemberNotFoundException {
//        if (!memberRepository.existsById(memberId)) {
//            throw new MemberNotFoundException("Member with ID " + memberId + " not found");
//        }
//        memberRepository.deleteById(memberId);
//    }
//
//    // Borrowing operations
//    public void borrowBook(String memberId, String bookId)
//            throws MemberNotFoundException, BookNotFoundException, BookNotAvailableException {
//
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new MemberNotFoundException("Member with ID " + memberId + " not found"));
//
//        Book book = bookRepository.findById(bookId)
//                .orElseThrow(() -> new BookNotFoundException("Book with ID " + bookId + " not found"));
//
//        if (!book.isAvailable()) {
//            throw new BookNotAvailableException("Book \"" + book.getTitle() + "\" is not available for borrowing");
//        }
//
//        book.setAvailable(false);
//        member.addBorrowedBook(book);
//
//        bookRepository.save(book);
//        memberRepository.save(member);
//    }
//
//    public void returnBook(String memberId, String bookId)
//            throws MemberNotFoundException, BookNotFoundException {
//
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new MemberNotFoundException("Member with ID " + memberId + " not found"));
//
//        Book book = bookRepository.findById(bookId)
//                .orElseThrow(() -> new BookNotFoundException("Book with ID " + bookId + " not found"));
//
//        book.setAvailable(true);
//        member.removeBorrowedBook(book);
//
//        bookRepository.save(book);
//        memberRepository.save(member);
//    }
//}
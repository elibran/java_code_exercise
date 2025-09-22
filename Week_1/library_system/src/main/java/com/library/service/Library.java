package com.library.service;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import com.library.exception.BookNotAvailableException;
import com.library.exception.BookNotFoundException;
import com.library.exception.MemberNotFoundException;
import com.library.model.Book;
import com.library.model.Member;

public class Library {
    private Map<String, Book> books;
    private Map<String, Member> members;
    
    public Library() {
        this.books = new HashMap<>();
        this.members = new HashMap<>();
    }
    
    public void addBook(Book book) {
        books.put(book.getBookId(), book);
        System.out.println("Book added successfully: " + book.getTitle());
    }
    
    public void addMember(Member member) {
        members.put(member.getMemberId(), member);
        System.out.println("Member added successfully: " + member.getName());
    }
    
    public void borrowBook(String memberId, String bookId) 
            throws MemberNotFoundException, BookNotFoundException, BookNotAvailableException {
        Member member = members.get(memberId);
        if (member == null) {
            throw new MemberNotFoundException("Member with ID " + memberId + " not found");
        }
        
        Book book = books.get(bookId);
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + bookId + " not found");
        }
        
        if (!book.isAvailable()) {
            throw new BookNotAvailableException("Book \"" + book.getTitle() + "\" is not available for borrowing");
        }
        
        book.setAvailable(false);
        member.addBorrowedBook(book);
        System.out.println("Book \"" + book.getTitle() + "\" successfully borrowed by " + member.getName());
    }
    
    public void returnBook(String memberId, String bookId) 
            throws MemberNotFoundException, BookNotFoundException {
        Member member = members.get(memberId);
        if (member == null) {
            throw new MemberNotFoundException("Member with ID " + memberId + " not found");
        }
        
        Book book = books.get(bookId);
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + bookId + " not found");
        }
        
        book.setAvailable(true);
        member.removeBorrowedBook(book);
        System.out.println("Book \"" + book.getTitle() + "\" successfully returned by " + member.getName());
    }
    
    public List<Book> searchBooksByAuthor(String author) {
        return books.values().stream()
                .filter(book -> book.getAuthor().equalsIgnoreCase(author))
                .collect(Collectors.toList());
    }
    
    public List<Member> getMembersBorrowingBooks() {
        return members.values().stream()
                .filter(member -> !member.getBorrowedBooks().isEmpty())
                .collect(Collectors.toList());
    }
    
    public void saveLibraryData(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("BOOKS:");
            for (Book book : books.values()) {
                writer.println(book.getBookId() + "," + book.getTitle() + "," + 
                             book.getAuthor() + "," + book.getIsbn() + "," + book.isAvailable());
            }
            
            writer.println("MEMBERS:");
            for (Member member : members.values()) {
                writer.println(member.getMemberId() + "," + member.getName() + "," + member.getEmail());
                for (Book book : member.getBorrowedBooks()) {
                    writer.println("BORROWED:" + book.getBookId());
                }
            }
            System.out.println("Library data saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }
    
    public void loadLibraryData(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            String section = "";
            Member currentMember = null;
            
            while ((line = reader.readLine()) != null) {
                if (line.equals("BOOKS:")) {
                    section = "BOOKS";
                    continue;
                } else if (line.equals("MEMBERS:")) {
                    section = "MEMBERS";
                    continue;
                }
                
                if (section.equals("BOOKS") && !line.isEmpty()) {
                    String[] parts = line.split(",");
                    Book book = new Book(parts[0], parts[1], parts[2], parts[3]);
                    book.setAvailable(Boolean.parseBoolean(parts[4]));
                    books.put(book.getBookId(), book);
                } else if (section.equals("MEMBERS")) {
                    if (line.startsWith("BORROWED:")) {
                        String bookId = line.substring(9);
                        Book book = books.get(bookId);
                        if (currentMember != null && book != null) {
                            currentMember.addBorrowedBook(book);
                        }
                    } else if (!line.isEmpty()) {
                        String[] parts = line.split(",");
                        currentMember = new Member(parts[0], parts[1], parts[2]);
                        members.put(currentMember.getMemberId(), currentMember);
                    }
                }
            }
            System.out.println("Library data loaded from " + filename);
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }
    
    public Map<String, Book> getBooks() { return books; }
    public Map<String, Member> getMembers() { return members; }
}


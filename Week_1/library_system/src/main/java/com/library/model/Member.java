package com.library.model;

import java.util.*;

public class Member {
    private String memberId;
    private String name;
    private String email;
    private List<Book> borrowedBooks;
    
    public Member(String memberId, String name, String email) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.borrowedBooks = new ArrayList<>();
    }
    
    // Getters and Setters
    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public List<Book> getBorrowedBooks() { return borrowedBooks; }
    public void setBorrowedBooks(List<Book> borrowedBooks) { this.borrowedBooks = borrowedBooks; }
    
    public void addBorrowedBook(Book book) {
        borrowedBooks.add(book);
    }
    
    public void removeBorrowedBook(Book book) {
        borrowedBooks.remove(book);
    }
    
    @Override
    public String toString() {
        return String.format("%s (%s): %s - %d books borrowed", 
                name, memberId, email, borrowedBooks.size());
    }
}

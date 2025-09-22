package com.library.main;

import java.util.*;

import com.library.exception.BookNotAvailableException;
import com.library.exception.BookNotFoundException;
import com.library.exception.MemberNotFoundException;
import com.library.model.Book;
import com.library.model.Member;
import com.library.service.Library;

public class LibraryManagementSystem {
    private static Library library = new Library();
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        initializeSampleData();
        
        while (true) {
            displayMenu();
            int choice = getChoice();
            
            switch (choice) {
                case 1: addBook(); break;
                case 2: addMember(); break;
                case 3: borrowBook(); break;
                case 4: returnBook(); break;
                case 5: searchBooksByAuthor(); break;
                case 6: displayMembersWithBorrowedBooks(); break;
                case 7: saveData(); break;
                case 8: loadData(); break;
                case 9: 
                    System.out.println("Thank you for using Library Management System!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            System.out.println();
        }
    }
    
    private static void initializeSampleData() {
        // Sample books
        library.addBook(new Book("B001", "The Java Complete Reference", "Herbert Schildt", "978-1260440232"));
        library.addBook(new Book("B002", "Effective Java", "Joshua Bloch", "978-0134685991"));
        library.addBook(new Book("B003", "Head First Java", "Kathy Sierra", "978-0596009205"));
        library.addBook(new Book("B004", "Java Concurrency in Practice", "Brian Goetz", "978-0321349606"));
        
        // Sample members
        library.addMember(new Member("M001", "Alice Johnson", "alice@email.com"));
        library.addMember(new Member("M002", "Bob Smith", "bob@email.com"));
        library.addMember(new Member("M003", "Carol Davis", "carol@email.com"));
        
        System.out.println("Sample data initialized.\n");
    }
    
    private static void displayMenu() {
        System.out.println("=== Library Management System ===");
        System.out.println("1. Add a new book");
        System.out.println("2. Add a new member");
        System.out.println("3. Borrow a book");
        System.out.println("4. Return a book");
        System.out.println("5. Search books by author");
        System.out.println("6. Display members with borrowed books");
        System.out.println("7. Save data to file");
        System.out.println("8. Load data from file");
        System.out.println("9. Exit");
        System.out.print("Enter your choice: ");
    }
    
    private static int getChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private static void addBook() {
        System.out.print("Enter book ID: ");
        String bookId = scanner.nextLine();
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        System.out.print("Enter author: ");
        String author = scanner.nextLine();
        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine();
        
        library.addBook(new Book(bookId, title, author, isbn));
    }
    
    private static void addMember() {
        System.out.print("Enter member ID: ");
        String memberId = scanner.nextLine();
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        
        library.addMember(new Member(memberId, name, email));
    }
    
    private static void borrowBook() {
        System.out.print("Enter member ID: ");
        String memberId = scanner.nextLine();
        System.out.print("Enter book ID: ");
        String bookId = scanner.nextLine();
        
        try {
            library.borrowBook(memberId, bookId);
        } catch (MemberNotFoundException | BookNotFoundException | BookNotAvailableException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void returnBook() {
        System.out.print("Enter member ID: ");
        String memberId = scanner.nextLine();
        System.out.print("Enter book ID: ");
        String bookId = scanner.nextLine();
        
        try {
            library.returnBook(memberId, bookId);
        } catch (MemberNotFoundException | BookNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void searchBooksByAuthor() {
        System.out.print("Enter author name: ");
        String author = scanner.nextLine();
        
        List<Book> books = library.searchBooksByAuthor(author);
        if (books.isEmpty()) {
            System.out.println("No books found by author: " + author);
        } else {
            System.out.println("Books by " + author + ":");
            for (Book book : books) {
                System.out.println("- " + book);
            }
        }
    }
    
    private static void displayMembersWithBorrowedBooks() {
        List<Member> members = library.getMembersBorrowingBooks();
        if (members.isEmpty()) {
            System.out.println("No members have borrowed books currently.");
        } else {
            System.out.println("Members with borrowed books:");
            for (Member member : members) {
                System.out.println("- " + member.getName() + " (" + member.getMemberId() + "): " 
                                 + member.getBorrowedBooks().size() + " books borrowed");
                for (Book book : member.getBorrowedBooks()) {
                    System.out.println("  * " + book.getBookId() + ": " + book.getTitle());
                }
            }
        }
    }
    
    private static void saveData() {
        System.out.print("Enter filename: ");
        String filename = scanner.nextLine();
        library.saveLibraryData(filename);
    }
    
    private static void loadData() {
        System.out.print("Enter filename: ");
        String filename = scanner.nextLine();
        library.loadLibraryData(filename);
    }
}
package com.library.controller;

import com.library.service.LibraryService;
import com.library.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/api/library")
@CrossOrigin(origins = "*")
public class LibraryController {

    private final LibraryService libraryService;

    @Autowired
    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @PostMapping("/borrow")
    public ResponseEntity<Map<String, String>> borrowBook(
            @RequestParam String memberId,
            @RequestParam String bookId) {

        try {
            libraryService.borrowBook(memberId, bookId);
            return ResponseEntity.ok(Map.of(
                    "message", "Book borrowed successfully",
                    "memberId", memberId,
                    "bookId", bookId
            ));
        } catch (MemberNotFoundException | BookNotFoundException | BookNotAvailableException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/return")
    public ResponseEntity<Map<String, String>> returnBook(
            @RequestParam String memberId,
            @RequestParam String bookId) {

        try {
            libraryService.returnBook(memberId, bookId);
            return ResponseEntity.ok(Map.of(
                    "message", "Book returned successfully",
                    "memberId", memberId,
                    "bookId", bookId
            ));
        } catch (MemberNotFoundException | BookNotFoundException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
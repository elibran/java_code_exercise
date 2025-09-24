package com.library.controller;

import com.library.model.Book;
import com.library.service.LibraryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/library")
public class LibraryController {
    private LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping("/ping")
    public String ping() {
        return "Welcome to the Banking API! in some changed format";
    }

    @PostMapping("addBook")
    public ResponseEntity<String> addBook(@Valid @RequestBody Book book){
        String message = libraryService.addBook(book);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }
}

package com.library.service;

import com.library.model.Book;
import com.library.repository.LibraryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
//@Transactional
public class LibraryService {

    private LibraryRepository libraryRepository;

    public LibraryService(LibraryRepository libraryRepository) {
        this.libraryRepository = libraryRepository;
    }

    public String addBook(Book book) {
        Book savedBook =  libraryRepository.save(book);
        if (null != savedBook){
            return "The book is created with id = " + savedBook.getId();
        } else{
            return " There seems to be some issue. Please contact ";
        }
    }
}

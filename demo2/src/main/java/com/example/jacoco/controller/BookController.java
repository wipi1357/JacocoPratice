package com.example.jacoco.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jacoco.model.Book;
import com.example.jacoco.service.BookService;

@RestController
@RequestMapping("/book")
public class BookController {

	@Autowired
	private BookService bookService;

	@PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Book> create(@RequestBody Book book) {
		Book newBook = bookService.create(book.getTitle(), book.getContent());
		return new ResponseEntity<>(newBook, HttpStatus.CREATED);
	}

	@PostMapping(value = "/getByTitle", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Book>> getByTitle(String title) {
		List<Book> books = bookService.getByTitle(title);
		if (books.isEmpty())
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		return new ResponseEntity<>(books, HttpStatus.OK);
	}

	@PostMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Book> update(@RequestBody Book book) {
		boolean isSuccess = bookService.update(book);
		if (isSuccess) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping(value = "/deleteById", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Book> deleteById(Long bookId) {
		boolean isSuccess = bookService.delete(bookId);
		if (isSuccess) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}

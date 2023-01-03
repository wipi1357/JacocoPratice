package com.example.jacoco.service;

import java.util.List;

import com.example.jacoco.model.Book;

public interface BookService {

	Book create(String title, String content);

	List<Book> getByTitle(String title);

	boolean update(Book book);

	boolean delete(Long id);
}

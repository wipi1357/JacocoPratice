package com.example.jacoco.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.jacoco.dao.BookDao;
import com.example.jacoco.model.Book;
import com.example.jacoco.service.BookService;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	BookDao bookDao;

	@Override
	public Book create(String title, String content) {
		Book book = new Book();
		book.setTitle(title);
		book.setContent(content);
		Long id = bookDao.create(title, content);
		book.setId(id);
		return book;
	}

	@Override
	public List<Book> getByTitle(String title) {
		return bookDao.getByTitle(title);
	}

	@Override
	public boolean update(Book book) {
		return bookDao.update(book);
	}

	@Override
	public boolean delete(Long id) {
		return bookDao.delete(id);
	}

}
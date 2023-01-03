package com.example.jacoco.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.example.jacoco.model.Book;

@Repository
public class BookDao {

	@Autowired
	private JdbcTemplate demoTL;

	public Long create(String title, String content) {
		String sql = "INSERT INTO book (title, content) VALUES (?, ?)";
		GeneratedKeyHolder holder = new GeneratedKeyHolder();
		demoTL.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				statement.setString(1, title);
				statement.setString(2, content);
				return statement;
			}
		}, holder);
		long id = holder.getKey().longValue();
		return id;
	}

	public List<Book> getByTitle(String title) {
		String sql = "SELECT * FROM book WHERE title = ?";
		List<Book> books = demoTL.query(sql, BeanPropertyRowMapper.newInstance(Book.class), title);
		return books;
	}

	public boolean update(Book book) {
		String sql = "UPDATE book SET title = ?, content = ? WHERE id = ?";
		int count = demoTL.update(sql, book.getTitle(), book.getContent(), book.getId());
		return (count > 0);
	}

	public boolean delete(Long id) {
		String sql = "DELETE FROM book WHERE id = ?";
		int count = demoTL.update(sql, id);
		return (count > 0);
	}
}

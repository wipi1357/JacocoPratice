package com.example.jacoco.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.jacoco.model.Book;
import com.example.jacoco.service.BookService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
//@Sql({ "/drop_schema.sql", "/create_schema.sql" }) 可切成多份引用
@Sql(scripts = "classpath:test/data.sql") // sql 檔案放置的地方
// @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)  清除緩存 會重設資料庫資料
@TestMethodOrder(OrderAnnotation.class)
class BookControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	BookService bookService;
	@Autowired
	ObjectMapper objectMapper;

	@Test
	@Order(1)
	void testCreate() throws Exception {
		// [Arrange] 預期回傳的值
		Book book = new Book();
		book.setTitle("Alibaba");
		book.setContent("Alibaba win the treasure");

		// [Act] 模擬網路呼叫[POST] /book/create
		String content = objectMapper.writeValueAsString(book);
		String returnString = mockMvc
				.perform(MockMvcRequestBuilders.post("/book/create").contentType(MediaType.APPLICATION_JSON)
						.content(content).accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
		Book newBook = objectMapper.readValue(returnString, new TypeReference<Book>() {
		});

		// [Assert] 判定回傳的body是否跟預期的一樣
		assertEquals(book.getTitle(), newBook.getTitle());
		assertNotNull(newBook.getId());
	}

	@Test
	@Order(2)
	void testGetByTitle() throws Exception {
		// [Arrange] 預期回傳的值
		List<Book> expectedList = new ArrayList<Book>();
		Book book = new Book();
		book.setId(2L);
		book.setTitle("Alibaba");
		book.setContent("Alibaba win the treasure");
		expectedList.add(book);

		// [Act] 模擬網路呼叫[POST] /book/getByTitle
		String returnString = mockMvc
				.perform(MockMvcRequestBuilders.post("/book/getByTitle").param("title", book.getTitle())
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
		List<Book> actualList = objectMapper.readValue(returnString, new TypeReference<List<Book>>() {
		});

		// [Assert] 判定回傳的body是否跟預期的一樣
		assertEquals(expectedList, actualList);
	}

	@Test
	@Order(3)
	void testUpdate() throws Exception {
		// [Arrange] 預期回傳的值
		List<Book> expectedList = new ArrayList<Book>();
		Book book = new Book();
		book.setId(2L);
		book.setTitle("Alibaba_v2");
		book.setContent("Alibaba lost the treasure");
		expectedList.add(book);

		// [Act] 模擬網路呼叫[POST] /book/update
		String content = objectMapper.writeValueAsString(book);
		mockMvc.perform(MockMvcRequestBuilders.post("/book/update").contentType(MediaType.APPLICATION_JSON)
				.content(content).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

		// [Act] 模擬網路呼叫[POST] /book/update
		String returnString = mockMvc
				.perform(MockMvcRequestBuilders.post("/book/getByTitle").param("title", book.getTitle())
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
		Iterable<Book> actualList = objectMapper.readValue(returnString, new TypeReference<Iterable<Book>>() {
		});

		// [Assert] 判定回傳的body是否跟預期的一樣
		assertEquals(expectedList, actualList);
	}

	@Test
	@Order(4)
	void testDeleteById() throws Exception {
		// [Arrange] 預期回傳的值
		Long bookId = 2L;
		String bookTitle = "Alibaba_v2";

		// [Act] 模擬網路呼叫[POST] /book/getUserById /book/deleteById
		String returnString = mockMvc
				.perform(MockMvcRequestBuilders.post("/book/getByTitle").param("title", bookTitle)
						.accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
		List<Book> actualList = objectMapper.readValue(returnString, new TypeReference<List<Book>>() {
		});
		assertFalse(actualList.isEmpty());
		mockMvc.perform(MockMvcRequestBuilders.post("/book/deleteById").param("bookId", bookId.toString())
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

		mockMvc.perform(MockMvcRequestBuilders.post("/book/getByTitle").accept(MediaType.APPLICATION_JSON)
				.param("title", bookTitle)).andExpect(status().isNoContent());
		// [Assert] 判定回傳的body是否跟預期的一樣
	}

}

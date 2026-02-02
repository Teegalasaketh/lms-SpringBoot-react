// LibraryStudentDAO.java

package com.lms.dao;

import java.util.List;

import com.lms.model.*;

public interface LibraryStudentDAO {
	public List<Book> findAllBooks(int regId);
	public boolean reserveBook(String isbn, int regId);
	public List<BookIssued> findReservedBooks(int regId);
	public List<BookIssued> findIssuedBooks(int regId);
	public List<BookIssued> findRenewaledBooks(int regId);
	public List<BookIssued> findReturnedBooks(int regId);
	boolean returnBook(int issueId);
}

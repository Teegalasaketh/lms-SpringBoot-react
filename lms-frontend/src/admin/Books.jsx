// admin/Books.jsx
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import api from "../api/axios";
import ConfirmPopup from "../components/ConfirmPopup";

function Books() {
  const [books, setBooks] = useState([]);

  const [popup, setPopup] = useState({
    show: false,
    title: "",
    message: "",
    onOk: null
  });

  useEffect(() => {
    loadBooks();
  }, []);

  const loadBooks = () => {
    api.get("/admin/books")
      .then(res => setBooks(res.data))
      .catch(() =>
        setPopup({
          show: true,
          title: "Error",
          message: "Failed to load books",
          onOk: closePopup
        })
      );
  };

  const closePopup = () => {
    setPopup({ show: false, title: "", message: "", onOk: null });
  };

  const confirmDelete = (isbn) => {
    setPopup({
      show: true,
      title: "Confirm Delete",
      message: "Are you sure you want to delete this book?",
      onOk: () => deleteBook(isbn)
    });
  };

  const deleteBook = (isbn) => {
    api.delete(`/admin/books/${isbn}`)
      .then(() => {
        closePopup();
        loadBooks();
      })
      .catch(() =>
        setPopup({
          show: true,
          title: "Error",
          message: "Delete failed",
          onOk: closePopup
        })
      );
  };

  return (
    <>
      {/* PAGE TITLE */}
      <h3 className="heading-no-underline text-center d-block mx-auto mb-4">
        All Books
      </h3>

      {/* GLASS CONTAINER */}
      <div className="glass p-3 mt-3">

        {/* HEADER */}
        <div className="d-flex justify-content-between align-items-center mb-3">
          <h3 className="sub-heading">Books List</h3>

          <Link to="/admin/add-book" className="btn-glass btn-accent btn-sm">
            ➕ Add Book
          </Link>
        </div>

        {/* TABLE */}
        <table className="table glass-table table-fixed text-center mb-0">
  <thead>
    <tr>
      <th>ISBN</th>
      <th>Book Name</th>
      <th>Author</th>
      <th>Price</th>
      <th>Publisher</th>
      <th>Year</th>
      <th>Qty</th>
      <th>Actions</th>
    </tr>
  </thead>

  <tbody className="table-scroll-body">
    {books.length > 0 ? (
      books.map(book => (
        <tr key={book.isbn}>
          <td>{book.isbn}</td>
          <td>{book.bookName}</td>
          <td>{book.author}</td>
          <td>₹ {book.price}</td>
          <td>{book.publisher}</td>
          <td>{book.publishingYear}</td>
          <td>
            <span
              className={`status-badge ${
                book.qtyAvailable > 0 ? "success" : "danger"
              }`}
            >
              {book.qtyAvailable}
            </span>
          </td>
          <td>
  <div className="action-stack">
    <Link
      to={`/admin/update-book/${book.isbn}`}
      className="btn-glass btn-sm"
    >
      ✏ Update
    </Link>

    <button
      className="btn-glass btn-sm text-danger"
      onClick={() => confirmDelete(book.isbn)}
    >
      🗑 Delete
    </button>
  </div>
</td>

        </tr>
      ))
    ) : (
      <tr>
        <td colSpan="8" className="text-muted">
          No books found
        </td>
      </tr>
    )}
  </tbody>
</table>

      </div>

      {/* POPUP */}
      <ConfirmPopup
        show={popup.show}
        title={popup.title}
        message={popup.message}
        onOk={popup.onOk}
        onCancel={closePopup}
      />
    </>
  );
}

export default Books;

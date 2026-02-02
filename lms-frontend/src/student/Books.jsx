// student/Books.jsx
import { useEffect, useState } from "react";
import api from "../api/axios";
import ConfirmPopup from "../components/ConfirmPopup";

function Books() {
  const [books, setBooks] = useState([]);
  const [popup, setPopup] = useState({ show: false, title: "", message: "", onOk: null });

  useEffect(() => {
    api.get("/student/books")
      .then(res => setBooks(res.data))
      .catch(() =>
        setPopup({
          show: true,
          title: "Error",
          message: "Failed to load books",
          onOk: closePopup
        })
      );
  }, []);

  const closePopup = () =>
    setPopup({ show: false, title: "", message: "", onOk: null });

  const reserveBook = (isbn) => {
    api.post(`/student/reserve/${isbn}`)
      .then(() =>
        setPopup({
          show: true,
          title: "Success",
          message: "Book reserved successfully",
          onOk: closePopup
        })
      )
      .catch(() =>
        setPopup({
          show: true,
          title: "Error",
          message: "Failed to reserve book",
          onOk: closePopup
        })
      );
  };

  return (
    <>
      <h3 className="heading-no-underline text-center d-block mx-auto mb-4">All Books</h3>

      <div className="glass p-3">
          <table className="table table-hover text-center mb-0 glass-table table-fixed">
            <thead>
              <tr>
                <th>ISBN</th>
                <th>Book</th>
                <th>Author</th>
                <th>Publisher</th>
                <th>Year</th>
                <th>Qty</th>
                <th>Action</th>
              </tr>
            </thead>

            <tbody className="table-scroll-body">
              {books.length ? books.map(b => (
                <tr key={b.isbn}>
                  <td>{b.isbn}</td>
                  <td>{b.bookName}</td>
                  <td>{b.author}</td>
                  <td>{b.publisher}</td>
                  <td>{b.publishingYear}</td>
                  <td>
                    <span className={`status-badge ${b.qtyAvailable > 0 ? "success" : "danger"}`}>
                      {b.qtyAvailable}
                    </span>
                  </td>
                  <td>
                    <div className="action-stack">
                    <button
                      className="btn-glass btn-sm"
                      disabled={b.qtyAvailable <= 0}
                      onClick={() => reserveBook(b.isbn)}
                    >
                      Reserve
                    </button>
                    </div>
                  </td>
                </tr>
              )) : (
                <tr>
                  <td colSpan="7" className="text-muted">No books available</td>
                </tr>
              )}
            </tbody>
          </table>
      </div>

      <ConfirmPopup {...popup} onCancel={closePopup} />
    </>
  );
}

export default Books;

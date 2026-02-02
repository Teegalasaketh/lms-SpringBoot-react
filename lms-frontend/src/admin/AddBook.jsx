// admin/AddBook.jsx
import { useState } from "react";
import api from "../api/axios";
import ConfirmPopup from "../components/ConfirmPopup";

function AddBook() {
  const [book, setBook] = useState({
    isbn: "",
    bookName: "",
    author: "",
    price: "",
    publisher: "",
    publishingYear: "",
    qtyAvailable: ""
  });

  const [popup, setPopup] = useState({
    show: false,
    title: "",
    message: "",
    type: "" // "confirm" | "alert"
  });

  const handleChange = (e) =>
    setBook({ ...book, [e.target.name]: e.target.value });

  // 👉 ONLY place where API call happens
  const addBook = () => {
    api.post("/admin/books", book)
      .then(() => {
        setPopup({
          show: true,
          title: "Success",
          message: "Book added successfully!",
          type: "alert"
        });

        setBook({
          isbn: "",
          bookName: "",
          author: "",
          price: "",
          publisher: "",
          publishingYear: "",
          qtyAvailable: ""
        });
      })
      .catch(() =>
        setPopup({
          show: true,
          title: "Error",
          message: "Error adding book. Please try again.",
          type: "alert"
        })
      );
  };

  return (
    <>
      {/* PAGE TITLE */}
      <h3 className="heading-no-underline text-center d-block mx-auto mb-4">
        Add Book
      </h3>

      {/* FORM */}
      <div className="glass p-4 col-md-6 mx-auto mb-4">
        <form>

          {Object.keys(book).map((k) => (
            <input
              key={k}
              className="form-control mb-3"
              name={k}
              value={book[k]}
              placeholder={k.replace(/([A-Z])/g, " $1")}
              onChange={handleChange}
              required
            />
          ))}

          <div className="text-center">
            <button
              type="button"
              className="btn-glass btn-accent px-4"
              onClick={() =>
                setPopup({
                  show: true,
                  title: "Confirm",
                  message: "Do you want to add this book?",
                  type: "confirm"
                })
              }
            >
              Add Book
            </button>
          </div>

        </form>
      </div>

      {/* POPUP */}
      <ConfirmPopup
        show={popup.show}
        title={popup.title}
        message={popup.message}
        onOk={() => {
          if (popup.type === "confirm") {
            addBook();
          }
          setPopup({ ...popup, show: false });
        }}
        onCancel={
          popup.type === "confirm"
            ? () => setPopup({ ...popup, show: false })
            : null
        }
      />
    </>
  );
}

export default AddBook;

// admin/ReturnedBooks.jsx
import { useEffect, useState } from "react";
import api from "../api/axios";

function ReturnedBooks() {
  const [list, setList] = useState([]);

  useEffect(() => {
    loadReturnedBooks();
  }, []);

  const loadReturnedBooks = () => {
    api.get("/admin/issues/returned")
      .then(res => setList(res.data))
      .catch(() => alert("Failed to load returned books"));
  };

  return (
    <>
      <h3 className="heading-no-underline text-center d-block mx-auto mb-4">
  Returned Books
</h3>

<div className="glass p-3">

  <table className="table glass-table table-fixed">
    <thead>
      <tr>
        <th>Issue ID</th>
        <th>ISBN</th>
        <th>Book Name</th>
        <th>Reg ID</th>
        <th>Name</th>
        <th>Reserve Date</th>
        <th>Status</th>
      </tr>
    </thead>

    <tbody className="table-scroll-body">
      {list.map(b => (
        <tr key={b.issueId}>
          <td>{b.issueId}</td>
          <td>{b.isbn}</td>
          <td>{b.bookName}</td>
          <td>{b.regId}</td>
          <td>{b.name}</td>
          <td>{b.reserveDate}</td>
          <td>
            <span className="status-badge neutral">
              {b.status}
            </span>
          </td>
        </tr>
      ))}
    </tbody>
  </table>

</div>

    </>
  );
}

export default ReturnedBooks;

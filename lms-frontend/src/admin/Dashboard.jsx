// admin/Dashboard.jsx
import { Link } from "react-router-dom";

function Dashboard() {
  return (
    <>
      {/* PAGE HEADING */}
      <h3 className="heading-no-underline text-center d-block mx-auto mb-4">
        Admin Dashboard
      </h3>

      {/* GLASS CONTAINER */}
      <div className="glass p-4">

        <div className="d-flex flex-wrap justify-content-center gap-3">

          <Link className="dashboard-btn" to="/admin/add-book">
            ➕ Add Book
          </Link>

          <Link className="dashboard-btn" to="/admin/books">
            📘 All Books
          </Link>

          <Link className="dashboard-btn" to="/admin/reserved">
            📚 Reserved Books
          </Link>

          <Link className="dashboard-btn" to="/admin/issued">
            📤 Issued Books
          </Link>

          <Link className="dashboard-btn" to="/admin/renewaled">
            🔄 Renewaled Books
          </Link>

          <Link className="dashboard-btn" to="/admin/returned">
            📥 Returned Books
          </Link>

        </div>

      </div>
    </>
  );
}

export default Dashboard;

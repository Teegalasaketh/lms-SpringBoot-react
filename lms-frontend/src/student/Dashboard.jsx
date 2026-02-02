// student/Dashboard.jsx
import { Link } from "react-router-dom";

function Dashboard() {
  return (
    <>
      <h3 className="heading-no-underline text-center d-block mx-auto mb-4">
        Student Dashboard
      </h3>

      <div className="glass p-4">
        <div className="d-flex flex-wrap justify-content-center gap-3">

          <Link className="dashboard-btn" to="/student/books">
            📚 All Books
          </Link>

          <Link className="dashboard-btn" to="/student/reserved">
            🔒 Reserved
          </Link>

          <Link className="dashboard-btn" to="/student/issued">
            📖 Issued
          </Link>

          <Link className="dashboard-btn" to="/student/renewaled">
            🔄 Renewed
          </Link>

          <Link className="dashboard-btn" to="/student/returned">
            ✔ Returned
          </Link>

        </div>
      </div>
    </>
  );
}

export default Dashboard;

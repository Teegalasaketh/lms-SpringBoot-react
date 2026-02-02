// App.jsx
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";

// ===== AUTH =====
import Login from "./auth/Login";
import Signup from "./auth/Signup";

// ===== PROTECTION & LAYOUTS =====
import ProtectedRoute from "./components/ProtectedRoute";
import AdminLayout from "./components/AdminLayout";
import StudentLayout from "./components/StudentLayout";

// ===== STUDENT PAGES =====
import StudentDashboard from "./student/Dashboard";
import StudentBooks from "./student/Books";
import StudentReserved from "./student/ReservedBooks";
import StudentIssued from "./student/IssuedBooks";
import StudentReturned from "./student/ReturnedBooks";
import StudentRenewaled from "./student/RenewaledBooks";

// ===== ADMIN PAGES =====
import AdminDashboard from "./admin/Dashboard";
import AdminBooks from "./admin/Books";
import AddBook from "./admin/AddBook";
import AdminReserved from "./admin/ReservedBooks";
import AdminIssued from "./admin/IssuedBooks";
import AdminReturned from "./admin/ReturnedBooks";
import AdminRenewaled from "./admin/RenewaledBooks";

function App() {
  return (
    <div className="app-layout">
    <BrowserRouter>
      <Routes>

        {/* ================= PUBLIC ROUTES ================= */}
        <Route path="/" element={<Navigate to="/login" replace />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />

        {/* ================= STUDENT ROUTES ================= */}
        <Route
          path="/student/dashboard"
          element={
            <ProtectedRoute role="STUDENT">
              <StudentLayout>
                <StudentDashboard />
              </StudentLayout>
            </ProtectedRoute>
          }
        />

        <Route
          path="/student/books"
          element={
            <ProtectedRoute role="STUDENT">
              <StudentLayout>
                <StudentBooks />
              </StudentLayout>
            </ProtectedRoute>
          }
        />

        <Route
          path="/student/reserved"
          element={
            <ProtectedRoute role="STUDENT">
              <StudentLayout>
                <StudentReserved />
              </StudentLayout>
            </ProtectedRoute>
          }
        />

        <Route
          path="/student/issued"
          element={
            <ProtectedRoute role="STUDENT">
              <StudentLayout>
                <StudentIssued />
              </StudentLayout>
            </ProtectedRoute>
          }
        />

        <Route
          path="/student/returned"
          element={
            <ProtectedRoute role="STUDENT">
              <StudentLayout>
                <StudentReturned />
              </StudentLayout>
            </ProtectedRoute>
          }
        />
        <Route
  path="/student/renewaled"
  element={
    <ProtectedRoute role="STUDENT">
      <StudentLayout>
        <StudentRenewaled />
      </StudentLayout>
    </ProtectedRoute>
  }
/>

        {/* ================= ADMIN ROUTES ================= */}
        <Route
          path="/admin/dashboard"
          element={
            <ProtectedRoute role="ADMIN">
              <AdminLayout>
                <AdminDashboard />
              </AdminLayout>
            </ProtectedRoute>
          }
        />

        <Route
          path="/admin/books"
          element={
            <ProtectedRoute role="ADMIN">
              <AdminLayout>
                <AdminBooks />
              </AdminLayout>
            </ProtectedRoute>
          }
        />

        <Route
          path="/admin/add-book"
          element={
            <ProtectedRoute role="ADMIN">
              <AdminLayout>
                <AddBook />
              </AdminLayout>
            </ProtectedRoute>
          }
        />

        <Route
          path="/admin/reserved"
          element={
            <ProtectedRoute role="ADMIN">
              <AdminLayout>
                <AdminReserved />
              </AdminLayout>
            </ProtectedRoute>
          }
        />

        <Route
          path="/admin/issued"
          element={
            <ProtectedRoute role="ADMIN">
              <AdminLayout>
                <AdminIssued />
              </AdminLayout>
            </ProtectedRoute>
          }
        />

        <Route
          path="/admin/returned"
          element={
            <ProtectedRoute role="ADMIN">
              <AdminLayout>
                <AdminReturned />
              </AdminLayout>
            </ProtectedRoute>
          }
        />
        <Route
            path="/admin/renewaled"
            element={
              <ProtectedRoute role="ADMIN">
                <AdminLayout>
                  <AdminRenewaled />
                </AdminLayout>
              </ProtectedRoute>
            }
          />
        {/* ================= FALLBACK ================= */}
        <Route path="*" element={<Navigate to="/login" replace />} />

      </Routes>
    </BrowserRouter>
    </div>
  );
}

export default App;


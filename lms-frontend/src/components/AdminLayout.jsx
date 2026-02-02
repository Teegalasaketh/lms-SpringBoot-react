// components/AdminLayout.jsx
import Navbar from "./Navbar";
import Footer from "./Footer";
function AdminLayout({ children }) {
  return (
    <>
      <Navbar role="ADMIN" />
      <main className="app-content container mt-4">
        {children}
      </main>
      <Footer />
    </>
  );
}

export default AdminLayout;

// components/StudentLayout.jsx
import Navbar from "./Navbar";
import Footer from "./Footer";

function StudentLayout({ children }) {
  return (
    <>
      <Navbar role="STUDENT" />
      <main className="app-content container mt-4">
        {children}
      </main>
      <Footer />
    </>
  );
}

export default StudentLayout;

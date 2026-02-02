// components/Footer.jsx
function Footer() {
  return (
    <footer className="app-footer glass">
      <div className="footer-content">

        {/* LEFT */}
        <div className="footer-left">
          <img src="/logo.png" alt="Library Logo" className="footer-logo" />
          <span className="footer-title">Library Management System</span>
        </div>

        {/* RIGHT */}
        <div className="footer-right">
          <span>© {new Date().getFullYear()} All rights reserved</span>
        </div>

      </div>
    </footer>
  );
}

export default Footer;

// components/AuthNavbar.jsx
function AuthNavbar() {
  return (
    <nav className="navbar glass px-4 py-3 position-relative">
      {/* CENTER BRAND */}
      <div className="nav-brand position-absolute start-50 translate-middle-x">
        <img src="/logo.png" alt="Library Logo" className="nav-logo" />
        <span className="auth-nav-brand">Library Management</span>
      </div>

      {/* THEME TOGGLE */}
      <div className="ms-auto">
        <button
          className="btn-glass"
          onClick={() => document.body.classList.toggle("dark-mode")}
          aria-label="Toggle theme"
        >
          🌙
        </button>
      </div>
    </nav>
  );
}

export default AuthNavbar;

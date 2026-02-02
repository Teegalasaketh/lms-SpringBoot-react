// components/Navbar.jsx
import { useEffect, useState } from "react";
import api from "../api/axios";

function Navbar({ role }) {
  const [name, setName] = useState("");
  const [count, setCount] = useState(0);
  const [list, setList] = useState([]);
  const [open, setOpen] = useState(false);

  const base = role === "ADMIN" ? "/admin" : "/student";

  useEffect(() => {
    api.get("/auth/me").then(res => {
      if (res.data.authenticated) {
        setName(res.data.name);
        loadCount();
      }
    });
  }, []);

  const loadCount = () => {
    api.get(`${base}/notifications/count`)
      .then(res => setCount(res.data))
      .catch(() => {});
  };

  const loadNotifications = () => {
    api.get(`${base}/notifications`)
      .then(res => setList(res.data))
      .catch(() => {});
  };

  const toggleNotifications = () => {
    setOpen(!open);

    if (!open) {
      loadNotifications();
      api.post(`${base}/notifications/read`)
        .then(() => setCount(0))
        .catch(() => {});
    }
  };

  const logout = () => {
    api.post(`${base}/logout`).finally(() => {
      window.location.href = "/login";
    });
  };

  return (
    <nav className="navbar glass px-4 py-2">
      <img src="/logo.png" alt="Library Logo" className="nav-logo" />
      <span className="auth-nav-brand">
        {role === "ADMIN" ? "Library Admin" : "Library Portal"}
      </span>

      <div className="ms-auto d-flex align-items-center gap-3">

        {/* 🔔 NOTIFICATIONS */}
        <div className="notif-wrapper">
          <button className="btn-glass notif-btn" onClick={toggleNotifications}>
            🔔
            {count > 0 && <span className="notif-dot">{count}</span>}
          </button>

          {open && (
            <div className="notif-dropdown glass">
              <h6 className="heading-no-underline text-center d-block mx-auto mb-4 "style={{ fontSize: "1rem" }}>Notifications</h6>

              {list.length ? (
                list.map((n, i) => (
                  <div key={i} className="notif-item">
                    {n.message}
                  </div>
                ))
              ) : (
                <p className="text-muted text-center" style={{ fontSize: "0.8rem" }}>
                  No new notifications
                </p>
              )}
            </div>
          )}
        </div>

        {/* PROFILE */}
        <button className="profile-circle btn-glass notif-btn ">
          {name?.charAt(0)}
        </button>

        {/* LOGOUT */}
        <button className="btn-glass notif-btn">
  <span
    style={{
      display: "flex",
      alignItems: "center",
      justifyContent: "center",
      fontSize: "20px",
      lineHeight: "1"
    }}
  >
    ⦸
  </span>
</button>


        {/* THEME */}
        <button
          className="btn-glass"
          onClick={() => document.body.classList.toggle("dark-mode")}
        >
          🌙
        </button>
      </div>
    </nav>
  );
}

export default Navbar;

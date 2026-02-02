// components/Notifications.jsx
import { useEffect, useState } from "react";
import api from "../api/axios";

function Notifications({ role }) {

  const [notifications, setNotifications] = useState([]);

  useEffect(() => {
    loadNotifications();
  }, [role]);

  const loadNotifications = () => {
    const url =
      role === "ADMIN"
        ? "/admin/notifications"
        : "/student/notifications";

    api.get(url).then(res => setNotifications(res.data));
  };

  const markAllRead = () => {
    const url =
      role === "ADMIN"
        ? "/admin/notifications/read"
        : "/student/notifications/read";

    api.post(url).then(() => setNotifications([]));
  };

  return (
    <div className="nav-item dropdown mr-3">
      <span
        className="nav-link dropdown-toggle text-white"
        role="button"
        data-toggle="dropdown"
      >
        🔔
        <span className="badge badge-danger ml-1">
          {notifications.length}
        </span>
      </span>

      <div className="dropdown-menu dropdown-menu-right notif-dropdown">

        <div className="notif-header">
          Notifications
        </div>

        {notifications.length > 0 ? (
          <>
            {notifications.map((n, i) => (
              <div className="notif-item" key={i}>
                <div className="notif-icon">📩</div>
                <div>
                  <div className="notif-message">{n.message}</div>
                  <div className="notif-time">{n.createdAt}</div>
                </div>
              </div>
            ))}

            <div className="text-center border-top py-2">
              <button
                className="btn btn-link text-primary"
                onClick={markAllRead}
              >
                Mark all as read
              </button>
            </div>
          </>
        ) : (
          <div className="p-3 text-center text-muted">
            No new notifications
          </div>
        )}

      </div>
    </div>
  );
}

export default Notifications;

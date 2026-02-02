// components/ProtectedRoute.jsx
import { useEffect, useState } from "react";
import { Navigate } from "react-router-dom";
import api from "../api/axios";

function ProtectedRoute({ role, children }) {

  const [loading, setLoading] = useState(true);
  const [allowed, setAllowed] = useState(false);

  useEffect(() => {
    api.get("/auth/me")
      .then(res => {
        if (
          res.data.authenticated &&
          (!role || res.data.role === role)
        ) {
          setAllowed(true);
        } else {
          setAllowed(false);
        }
      })
      .catch(() => setAllowed(false))
      .finally(() => setLoading(false));
  }, [role]);

  if (loading) {
    return (
      <div className="text-center mt-5">
        <h5>Checking authentication...</h5>
      </div>
    );
  }

  return allowed ? children : <Navigate to="/login" replace />;
}

export default ProtectedRoute;

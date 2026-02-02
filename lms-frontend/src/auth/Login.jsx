// auth/Login.jsx
import { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../api/axios";
import AuthNavbar from "../components/AuthNavbar";
function Login() {
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [pass, setPass] = useState("");
  const [role, setRole] = useState("");
  const [msg, setMsg] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    // Check if user is already logged in
    const checkAuth = async () => {
      try {
        const token = localStorage.getItem("token");
        if (token) {
          const response = await api.get("/auth/me");
          if (response.data.authenticated) {
            redirectBasedOnRole(response.data.role);
          }
        }
      } catch (error) {
        console.error("Auth check failed:", error);
        localStorage.removeItem("token");
      }
    };
    
    checkAuth();
  }, [navigate]);

  const redirectBasedOnRole = (userRole) => {
    if (userRole === "ADMIN") {
      navigate("/admin/dashboard", { replace: true });
    } else {
      navigate("/student/dashboard", { replace: true });
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMsg("");
    setIsLoading(true);

    if (!email || !pass || !role) {
      setMsg("Please fill in all fields");
      setIsLoading(false);
      return;
    }

    try {
      console.log('Attempting login with:', { email, role });
      
      // 1️⃣ Login (creates session and gets token)
      const response = await api.post("/auth/login", {
  email,
  pass,
  role
});

      console.log('Login response:', response);

      // Store the token if present in the response
      if (response.data.token) {
        console.log('Token stored in localStorage');
      } else {
        console.warn('No token received in login response');
      }

      // 2️⃣ Verify session and get user data
      console.log('Verifying session...');
      const me = await api.get("/auth/me");
      console.log('Session verification response:', me);
      
      if (me.data.authenticated) {
        console.log('Authentication successful, redirecting to:', me.data.role);
        redirectBasedOnRole(me.data.role);
      } else {
        console.warn('Authentication failed - not authenticated in me response');
        setMsg("Authentication failed. Please try again.");
      }
    } catch (error) {
      console.error("Login error:", error);
      if (error.response) {
        // The request was made and the server responded with a status code
        // that falls out of the range of 2xx
        if (error.response.status === 401) {
          setMsg("Invalid email, password, or role");
        } else if (error.response.status === 500) {
          setMsg("Server error. Please try again later.");
        } else {
          setMsg(error.response.data?.message || "An error occurred during login");
        }
      } else if (error.request) {
        // The request was made but no response was received
        setMsg("Network error. Please check your connection and try again.");
      } else {
        // Something happened in setting up the request
        setMsg("An unexpected error occurred. Please try again.");
      }
    } finally {
      setIsLoading(false);
    }
  };

  return (
  <>
    {/* NAVBAR */}
    <AuthNavbar />


    <div className="d-flex justify-content-center align-items-center vh-100">
      <div className="glass p-4" style={{ width: 420 }}>

        <h3 className="heading-no-underline text-center d-block mx-auto mb-4">
  Login
</h3>

        {msg && (
          <div className="text-center text-danger mb-3">
            <b>{msg}</b>
          </div>
        )}

        <form onSubmit={handleSubmit}>

          <div className="mb-3">
            <label>Email</label>
            <input
              type="email"
              className="form-control"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>

          <div className="mb-3">
            <label>Password</label>
            <input
              type="password"
              className="form-control"
              value={pass}
              onChange={(e) => setPass(e.target.value)}
              required
            />
          </div>

          <div className="mb-4">
            <label>Role</label>
            <select
              className="form-control"
              value={role}
              onChange={(e) => setRole(e.target.value)}
              required
            >
              <option value="" disabled>Select role</option>
              <option value="ADMIN">Admin</option>
              <option value="STUDENT">Student</option>
            </select>
          </div>

          <button className="btn-glass btn-accent w-100 mb-3">
            {isLoading ? "Signing in..." : "Login"}
          </button>

        </form>

        <div className="text-center">
          <small>
            Don’t have an account? <Link to="/signup">Sign up</Link>
          </small>
        </div>

      </div>
    </div>
  </>
);
}

export default Login;

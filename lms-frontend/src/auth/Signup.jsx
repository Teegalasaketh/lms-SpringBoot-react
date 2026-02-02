// auth/Signup.jsx
import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import api from "../../../lms-frontend/src/api/axios";
import AuthNavbar from "../components/AuthNavbar";

function Signup() {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    name: "",
    email: "",
    pass: "",
    roleName: ""
  });

  const [msg, setMsg] = useState("");

  const handleChange = (e) => {
    setForm({
      ...form,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMsg("");

    try {
      const res = await api.post("/auth/signup", form);

      if (!res.data.success) {
        setMsg(res.data.message);
        return;
      }

      setMsg(res.data.message);

      // redirect to login after short delay
      setTimeout(() => {
        navigate("/login");
      }, 1500);

    } catch (err) {
      setMsg("Server error. Please try again.");
    }
  };

  return (
  <>
    <AuthNavbar />



    <div className="d-flex justify-content-center align-items-center vh-100">
      <div className="glass p-4" style={{ width: 420 }}>

        <h3 className="heading-no-underline text-center d-block mx-auto mb-4">
  Register
</h3>

        {msg && (
          <div className="text-center text-success mb-3">
            <b>{msg}</b>
          </div>
        )}

        <form onSubmit={handleSubmit}>

          <div className="mb-3">
            <label>Name</label>
            <input
              type="text"
              className="form-control"
              name="name"
              value={form.name}
              onChange={handleChange}
              required
            />
          </div>

          <div className="mb-3">
            <label>Email</label>
            <input
              type="email"
              className="form-control"
              name="email"
              value={form.email}
              onChange={handleChange}
              required
            />
          </div>

          <div className="mb-3">
            <label>Password</label>
            <input
              type="password"
              className="form-control"
              name="pass"
              value={form.pass}
              onChange={handleChange}
              required
            />
          </div>

          <div className="mb-4">
            <label>Role</label>
            <select
              className="form-control"
              name="roleName"
              value={form.roleName}
              onChange={handleChange}
              required
            >
              <option value="" disabled>Select role</option>
              <option value="ADMIN">Admin</option>
              <option value="STUDENT">Student</option>
            </select>
          </div>

          <button className="btn-glass btn-accent w-100 mb-3">
            Register
          </button>

        </form>

        <div className="text-center">
          <small>
            Already registered? <Link to="/login">Sign in</Link>
          </small>
        </div>

      </div>
    </div>
  </>
);
}

export default Signup;

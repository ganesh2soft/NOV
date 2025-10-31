import { Fragment, useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { toast } from "react-toastify";
import { Link, useNavigate } from "react-router-dom";
import {
  loginRequest,
  loginSuccess,
  loginFailed,
  clearError,
} from "../slices/authSlice";
import axios from "axios";

export default function Login() {
  const [userName, setUserName] = useState("");
  const [password, setPassword] = useState("");

  const dispatch = useDispatch();
  const navigate = useNavigate();

  // Access your auth state
  const { loading, error, isAuthenticated } = useSelector(
    (state) => state.auth
  );

  // Handle login
  const submitHandler = async (e) => {
    e.preventDefault();
    dispatch(loginRequest());

    try {
      const { data } = await axios.post(
        `${process.env.REACT_APP_API_URL}/auth/signin`,
        { userName, password },
        { withCredentials: true }
      );

      dispatch(loginSuccess({ user: data }));
    } catch (err) {
      dispatch(
        loginFailed(err.response?.data?.message || "Invalid credentials")
      );
    }
  };

  // Redirect if logged in, show error toast if any
  useEffect(() => {
    if (isAuthenticated) {
      navigate("/");
    }

    if (error) {
      toast.error(error, {
        position: "bottom-center",
        onOpen: () => dispatch(clearError()), // clears error when toast shows
      });
    }
  }, [isAuthenticated, error, dispatch, navigate]);

  return (
    <Fragment>
      <div className="row wrapper">
        <div className="col-10 col-lg-5">
          <form onSubmit={submitHandler} className="shadow-lg">
            <h1 className="mb-3">Login</h1>

            <div className="form-group">
              <label htmlFor="email_field">Email</label>
              <input
                type="email"
                id="email_field"
                className="form-control"
                value={userName}
                onChange={(e) => setUserName(e.target.value)}
                required
              />
            </div>

            <div className="form-group">
              <label htmlFor="password_field">Password</label>
              <input
                type="password"
                id="password_field"
                className="form-control"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
            </div>

            <button
              type="submit"
              className="btn btn-block py-2"
              disabled={loading}
            >
              {loading ? "Signing in..." : "Login"}
            </button>

            <Link to="/register" className="float-right mt-3">
              New User?
            </Link>
          </form>
        </div>
      </div>
    </Fragment>
  );
}

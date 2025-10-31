import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { register } from "../actions/userActions";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

export default function Register() {
  const [userData, setUserData] = useState({
    userName: "",
    email: "",
    password: "",
  });
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { loading, isAuthenticated, error } = useSelector((state) => state.authState);
  const onChangeHandler = (e) => {
    setUserData({ ...userData, [e.target.name]: e.target.value });
  };
  const submitHandler = (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append("userName", userData.userName);
    formData.append("email", userData.email);
    formData.append("password", userData.password);
    dispatch(register(formData));
  };

  useEffect(() => {
    if (isAuthenticated) {
      // ✅ Redirect to login after successful registration
      toast.success("Registration successful! Please login.");
      setTimeout(() => navigate("/login"), 500);
    }
  }, [isAuthenticated, navigate]);
  return (
    <div className="container container-fluid">
      <div className="row wrapper">
        <div className="col-10 col-lg-5">
          <form onSubmit={submitHandler}>
            <h1 className="mb-3">Register</h1>

            <div className="form-group">
              <label htmlFor="name_field">Name</label>
              <input type="text" name="userName" onChange={onChangeHandler} id="name_field" className="form-control" />
            </div>

            <div className="form-group">
              <label htmlFor="email_field">Email</label>
              <input type="email" name="email" onChange={onChangeHandler} id="email_field" className="form-control" />
            </div>

            <div className="form-group">
              <label htmlFor="password_field">Password</label>
              <input type="password" name="password" onChange={onChangeHandler} id="password_field" className="form-control" />
            </div>

            <button id="register_button" disabled={loading} type="submit" className="btn btn-block py-3">
              REGISTER
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}

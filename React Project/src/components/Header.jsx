import { Link, useNavigate } from "react-router-dom";
import CategoryNavbar from "./CategoryNavbar";
import Search from "./Search";
import { Dropdown, Image } from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";

export default function Header({ cartItems }) {
  const { isAuthenticated, user } = useSelector((state) => state.auth) || {};
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const logoutHandler = () => {
    //dispatch(logout);
  };

  return (
    <div>
      <nav className="navbar row">
        {/* Logo */}
        <div className="col-12 col-md-3">
          <div className="navbar-brand">
            <Link to="/">
              <img
                src="/images/logo.png"
                height="50px"
                width="150px"
                alt="LOGO"
              />
            </Link>
          </div>
        </div>

        {/* Search bar */}
        <div className="col-12 col-md-6 mt-2 mt-md-0">
          <Search />
        </div>

        {/* Login / Logout + Cart */}
        <div className="col-12 col-md-3 mt-4 mt-md-0 d-flex justify-content-center align-items-center gap-3">
          {/* Conditionally show buttons based on authentication */}
          {isAuthenticated ? (
            <Dropdown className="d-inline">
              <Dropdown.Toggle
                variant="default text-white pr-5"
                id="dropdown-basic"
              >
                <span className="me-2 text-white">
                  HELLO ! {user?.username || "Guest"}{" "}
                </span>
                <figure className="avatar avatar-nav">
                  <Image width="50px" src={user ?? "./images/profilepic.jpg"} />
                </figure>
              </Dropdown.Toggle>
              <Dropdown.Menu>
                <Dropdown.Item onClick={logoutHandler} className="text-danger">
                  Logout
                </Dropdown.Item>
              </Dropdown.Menu>
            </Dropdown>
          ) : (
            <Link to="/login" className="btn btn-success me-3">
              Login
            </Link>
          )}

          <Link
            to={"/cart"}
            className="cart-link text-decoration-none text-light position-relative ms-3"
          >
            <div className="d-flex align-items-center">
              <i className="fa-solid fa-cart-shopping cart-icon"></i>
              <span className="cart-count">{cartItems.length}</span>
            </div>
            <span className="cart-text ms-2">Cart</span>
          </Link>
        </div>
      </nav>

      <CategoryNavbar />
    </div>
  );
}

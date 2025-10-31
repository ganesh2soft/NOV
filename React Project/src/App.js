import "./App.css";
import Home from "./pages/Home";
import Footer from "./components/Footer";
import Header from "./components/Header";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import ProductDetails from "./pages/ProductDetail";
import { useEffect, useState } from "react";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import Cart from "./pages/Cart";
import Checkout from "./pages/Checkout";
import OrderSuccess from "./pages/OrderSuccess";
//import LoginPage from "./pages/Login";
import ProtectedRoute from "./routes/ProtectedRoute";
import Login from "./user/Login";
import Register from "./components/Register";
import store from "./store/store";
import { loadUser } from "./actions/userActions";
function App() {
  const [cartItems, setCartItems] = useState([]);
  useEffect(() => {
    store.dispatch(loadUser);
  });
  return (
    <div className="App">
      <Router>
        <div>
          <ToastContainer />
          <Header cartItems={cartItems} />
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/search" element={<Home />} />
            <Route path="/product/:id" element={<ProductDetails cartItems={cartItems} setCartItems={setCartItems} />} />
            <Route path="/cart" element={<Cart cartItems={cartItems} setCartItems={setCartItems} />} />

            <Route
              path="/checkout"
              element={
                // <ProtectedRoute>
                <Checkout cartItems={cartItems} setCartItems={setCartItems} userEmail="deeps@gmail.com" />
                // </ProtectedRoute>
              }
            />

            <Route
              path="/ordersuccess"
              element={
                <ProtectedRoute>
                  <OrderSuccess />
                </ProtectedRoute>
              }
            />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
          </Routes>
        </div>
      </Router>
      <Footer />
    </div>
  );
}

export default App;

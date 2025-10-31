import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

export default function Checkout({ cartItems, setCartItems, userEmail }) {
  const [address, setAddress] = useState("");
  const [paymentMethod, setPaymentMethod] = useState("COD");
  const navigate = useNavigate();

  const handlePlaceOrder = async () => {
    if (!address) {
      alert("Please enter address");
      return;
    }
    if (!cartItems || cartItems.length === 0) {
      alert("Cart is empty");
      return;
    }
    const token = localStorage.getItem("token");
    if (!token) {
      alert("You must be logged in to place an order");
      return;
    }
    const payload = {
      email: userEmail || "guest@example.com",
      address,
      paymentMethod: "CARD",
      pgName: "Stripe",
      pgPaymentId: "1234567",
      pgStatus: "Successful",
      pgResponseMessage: "Payment Success",
      cartItems: cartItems.map((ci) => ({ productId: ci.product.id, qty: ci.qty })),
    };

    try {
      const res = await axios.post(`${process.env.REACT_APP_API_URL}/public/order/users/payments/${paymentMethod}`, payload, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      // success
      setCartItems([]); // clear local cart
      navigate("/order-success", { state: { order: res.data } });
    } catch (err) {
      console.error(err);
      alert("Order failed: " + (err.response?.data?.message || err.message));
    }
  };

  return (
    <div className="container mt-4">
      <h2>Checkout</h2>

      <div className="mb-3">
        <label>Delivery Address</label>
        <textarea className="form-control" value={address} onChange={(e) => setAddress(e.target.value)} rows={4}></textarea>
      </div>

      <div className="mb-3">
        <label>Payment Method</label>
        <select className="form-control" value={paymentMethod} onChange={(e) => setPaymentMethod(e.target.value)}>
          <option value="COD">Cash on Delivery</option>
          <option value="CARD">Card</option>
        </select>
      </div>

      <button className="btn btn-primary" onClick={handlePlaceOrder}>
        Place Order
      </button>
    </div>
  );
}

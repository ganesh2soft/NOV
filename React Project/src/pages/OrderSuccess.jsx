import React from "react";
import { Link } from "react-router-dom";

const OrderSuccess = () => {
  return (
    <div className="container mx-auto text-center my-20">
      <h1 className="text-3xl font-bold text-green-600 mb-4">🎉 Order Placed Successfully!</h1>
      <p className="text-lg mb-6">Thanks for shopping with us.</p>

      <Link to="/products">
        <button className="bg-blue-600 text-white px-6 py-2 rounded">Continue Shopping</button>
      </Link>
    </div>
  );
};

export default OrderSuccess;

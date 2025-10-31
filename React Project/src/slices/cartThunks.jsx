import { createAsyncThunk } from "@reduxjs/toolkit";

// Fetch all cart items
export const fetchCart = createAsyncThunk(
  "cart/fetchCart",
  async (_, { rejectWithValue }) => {
    try {
      const res = await fetch(`${process.env.REACT_APP_API_URL}/users/cart`, {
        credentials: "include", // sends cookies
      });
      if (!res.ok) throw new Error("Failed to fetch cart");
      const data = await res.json();
      return data; // array of cart items
    } catch (err) {
      return rejectWithValue(err.message);
    }
  }
);

// Add a product to cart
export const addToCart = createAsyncThunk(
  "cart/addToCart",
  async ({ productId, qty }, { rejectWithValue }) => {
    try {
      const res = await fetch(`${process.env.REACT_APP_API_URL}/cart/add`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include", // important for cookie auth
        body: JSON.stringify({ productId, qty }),
      });
      if (!res.ok) throw new Error("Failed to add item to cart");
      const data = await res.json();
      return data; // updated cart items
    } catch (err) {
      return rejectWithValue(err.message);
    }
  }
);

// Remove an item from cart
export const removeCartItem = createAsyncThunk(
  "cart/removeCartItem",
  async (productId, { rejectWithValue }) => {
    try {
      const res = await fetch(`${process.env.REACT_APP_API_URL}/cart/remove`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify({ productId }),
      });
      if (!res.ok) throw new Error("Failed to remove item from cart");
      const data = await res.json();
      return data; // updated cart items
    } catch (err) {
      return rejectWithValue(err.message);
    }
  }
);

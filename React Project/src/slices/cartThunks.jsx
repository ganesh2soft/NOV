import { createAsyncThunk } from "@reduxjs/toolkit";

// Fetch all cart items
export const fetchCart = createAsyncThunk(
  "cart/fetchCart",
  async (_, { rejectWithValue }) => {
    try {
      const res = await fetch(`${process.env.REACT_APP_API_URL}/users/cart`, {
        credentials: "include", // send cookies for auth
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
  async ({ productId, quantity }, { rejectWithValue }) => {
    try {
      const res = await fetch(
        `${process.env.REACT_APP_API_URL}/products/${productId}/quantity/${quantity}`,
        {
          method: "POST",
          credentials: "include", // send cookies
        }
      );
      if (!res.ok) throw new Error("Failed to add item to cart");
      const data = await res.json();
      return data; // updated cart
    } catch (err) {
      return rejectWithValue(err.message);
    }
  }
);

// Remove an item from cart
export const removeCartItem = createAsyncThunk(
  "cart/removeCartItem",
  async ({ productId }, { rejectWithValue }) => {
    try {
      const res = await fetch(
        `${process.env.REACT_APP_API_URL}/cart/remove/${productId}`,
        {
          method: "POST",
          credentials: "include",
        }
      );
      if (!res.ok) throw new Error("Failed to remove item from cart");
      const data = await res.json();
      return data; // updated cart
    } catch (err) {
      return rejectWithValue(err.message);
    }
  }
);

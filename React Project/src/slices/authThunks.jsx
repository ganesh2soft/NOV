import { createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";

// Login user
export const loginUser = createAsyncThunk(
  "auth/loginUser",
  async ({ userName, password }, { rejectWithValue }) => {
    try {
      const { data } = await axios.post(
        `${process.env.REACT_APP_API_URL}/auth/signin`,
        { userName, password },
        { withCredentials: true }
      );
      return data; // the user object
    } catch (err) {
      return rejectWithValue(
        err.response?.data?.message || "Invalid Credentials"
      );
    }
  }
);

// Load user (auto-login)
export const loadUser = createAsyncThunk(
  "auth/loadUser",
  async (_, { rejectWithValue }) => {
    try {
      const { data } = await axios.get(
        `${process.env.REACT_APP_API_URL}/auth/user`,
        {
          withCredentials: true,
        }
      );
      return data;
    } catch (err) {
      return rejectWithValue(
        err.response?.data?.message || "Failed to load user"
      );
    }
  }
);

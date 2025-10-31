import { createSlice } from "@reduxjs/toolkit";

const authSlice = createSlice({
  name: "auth",
  initialState: {
    loading: false,
    isAuthenticated: false,
    user: null,
    error: null,
  },
  reducers: {
    loginRequest(state, action) {
      return {
        ...state,
        loading: true,
        isAuthenticated: false,
        error: null,
      };
    },
    loginSuccess(state, action) {
      return {
        loading: false,
        isAuthenticated: true,
        user: action.payload.user,
        error: null,
      };
    },
    loginFailed(state, action) {
      return {
        ...state,
        loading: false,
        error: action.payload.error,
      };
    },
    clearError(state, action) {
      return {
        ...state,
        error: null,
      };
    },
    registerRequest(state, action) {
      return {
        ...state,
        loading: true,
      };
    },
    registerSuccess(state, action) {
      return {
        loading: false,
        isAuthenticated: false, // still user need to login
        user: action.payload.user,
      };
    },
    registerFailed(state, action) {
      return {
        ...state,
        loading: false,
        error: action.payload.error,
      };
    },
    loadUserRequest(state, action) {
      return {
        ...state,
        loading: true,
        isAuthenticated: false,
      };
    },
    loadUserSuccess(state, action) {
      return {
        loading: false,
        isAuthenticated: true,
        user: action.payload.user,
      };
    },
    loadUserFailed(state, action) {
      return {
        ...state,
        loading: false,
        error: action.payload.error,
      };
    },
  },
});
const { actions, reducer } = authSlice;
export const {
  loginRequest,
  loginSuccess,
  loginFailed,
  clearError,
  registerRequest,
  registerSuccess,
  registerFailed,
  loadUserRequest,
  loadUserSuccess,
  loadUserFailed,
} = actions;
export default reducer;

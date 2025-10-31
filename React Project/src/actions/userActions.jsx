import axios from "axios";
import {
  clearError,
  loadUserFailed,
  loadUserRequest,
  loadUserSuccess,
  loginFailed,
  loginRequest,
  loginSuccess,
  registerFailed,
  registerRequest,
  registerSuccess,
} from "../slices/authSlice";

export const login = (userName, password) => async (dispatch) => {
  try {
    console.log(userName, password);
    dispatch(loginRequest());
    const { data } = await axios
      .post(
        process.env.REACT_APP_API_URL + `/auth/signin`,
        { userName, password },
        { withCredentials: true }
      )
      .then((response) => {
        dispatch(loginSuccess({ user: response.data }));
      });
  } catch (error) {
    //dispatch(loginFailed(error.response.data.message));
    dispatch(loginFailed({ error: "Oyie !Invalid Credentials " }));
  }
};

export const clearAuthError = (dispatch) => {
  dispatch(clearError());
};

export const register = (userData) => async (dispatch) => {
  try {
    dispatch(registerRequest());
    console.log(process.env.REACT_APP_API_URL);
    const { data } = await axios.post(
      process.env.REACT_APP_API_URL + `/auth/signup`,
      userData,
      {
        withCredentials: true,
        headers: { "Content-Type": "application/json" },
      }
    );

    dispatch(registerSuccess(data));
  } catch (error) {
    //dispatch(loginFailed(error.response.data.message));
    dispatch(
      registerFailed(error.response?.data?.message || "Registration failed")
    );
  }
};
/*
This loaduser function cant be called in any particular component, as this
is common through out the application 
*/
export const loadUser = async (dispatch) => {
  try {
    dispatch(loadUserRequest());
    const { data } = await axios.get(
      process.env.REACT_APP_API_URL + `/auth/user`,
      {
        withCredentials: true,
        headers: { "Content-Type": "application/json" },
      }
    );

    dispatch(loadUserSuccess(data));
  } catch (error) {
    //dispatch(loginFailed(error.response.data.message));
    dispatch(
      loadUserFailed(error.response?.data?.message || "Load user Failed")
    );
  }
};

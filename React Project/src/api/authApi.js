import axios from "axios";

const API_URL = process.env.REACT_APP_API_URL;

export const loginUser = async (email, password) => {
  return axios.post(
    `${API_URL}/auth/signin`,
    { email, password },
    { withCredentials: true } // Required for cookies
  );
};

export const logoutUser = () => {
  localStorage.removeItem("token");
  return axios.post(`${API_URL}/auth/signout`, {}, { withCredentials: true });
};

import productsReducer from "../slices/productsSlice";
import authReducer from "../slices/authSlice";

const { combineReducers, configureStore } = require("@reduxjs/toolkit");

const reducer = combineReducers({
  productsState: productsReducer,
  authState: authReducer,
});

//To create store
const store = configureStore({
  reducer // It will change our state
});

export default store;

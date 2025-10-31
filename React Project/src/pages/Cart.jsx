import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Link, useNavigate } from "react-router-dom";
import { fetchCart, addToCart, removeCartItem } from "../slices/cartThunks";

export default function Cart() {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const { cartItems, loading, error } = useSelector((state) => state.cart);
  const { isAuthenticated } = useSelector((state) => state.auth);

  useEffect(() => {
    if (isAuthenticated) {
      dispatch(fetchCart());
    }
  }, [dispatch, isAuthenticated]);

  const increaseQty = (item) => {
    if (item.product.quantity === item.qty) return;
    dispatch(addToCart({ productId: item.product.id, qty: item.qty + 1 }));
  };

  const decreaseQty = (item) => {
    if (item.qty > 1) {
      dispatch(addToCart({ productId: item.product.id, qty: item.qty - 1 }));
    }
  };

  const removeItem = (item) => {
    dispatch(removeCartItem(item.product.id));
  };

  if (!isAuthenticated) {
    return <h2 className="mt-5">Please login to view your cart!</h2>;
  }

  return loading ? (
    <h2 className="mt-5">Loading Cart...</h2>
  ) : cartItems.length > 0 ? (
    <div className="container container-fluid">
      <h2 className="mt-5">
        Your Cart: <b>{cartItems.length} items</b>
      </h2>

      <div className="row d-flex justify-content-between">
        <div className="col-12 col-lg-8">
          <hr />
          {cartItems.map((item) => {
            const imageUrl =
              item.product.image && item.product.image.length > 0
                ? `${process.env.REACT_APP_API_URL}/public/products/image/${item.product.image}`
                : "/images/noimage.jpg";

            return (
              <div className="cart-item" key={item.product.id}>
                <div className="row">
                  <div className="col-4 col-lg-3">
                    <img
                      src={imageUrl}
                      alt={item.product.productName}
                      height="90"
                      width="115"
                    />
                  </div>

                  <div className="col-5 col-lg-3">
                    <Link to={"/product/" + item.product.id}>
                      {item.product.productName}
                    </Link>
                  </div>

                  <div className="col-4 col-lg-2 mt-4 mt-lg-0">
                    <p id="card_item_price">₹{item.product.specialPrice}</p>
                  </div>

                  <div className="col-4 col-lg-3 mt-4 mt-lg-0">
                    <div className="stockCounter d-inline">
                      <span
                        className="btn btn-danger minus"
                        onClick={() => decreaseQty(item)}
                      >
                        -
                      </span>
                      <input
                        type="number"
                        className="form-control count d-inline"
                        value={item.qty}
                        readOnly
                      />
                      <span
                        className="btn btn-primary plus"
                        onClick={() => increaseQty(item)}
                      >
                        +
                      </span>
                    </div>
                  </div>

                  <div className="col-4 col-lg-1 mt-4 mt-lg-0">
                    <i
                      id="delete_cart_item"
                      onClick={() => removeItem(item)}
                      className="fa fa-trash btn btn-danger"
                    ></i>
                  </div>
                </div>
              </div>
            );
          })}
          <hr />
        </div>

        <div className="col-12 col-lg-3 my-4">
          <div id="order_summary">
            <h4>Order Summary</h4>
            <hr />
            <p>
              <b>Subtotal:</b>{" "}
              <span className="order-summary-values">
                {cartItems.reduce((acc, item) => acc + item.qty, 0)} Items
              </span>
            </p>
            <p>
              <b>To Pay:</b>{" "}
              <span className="order-summary-values">
                ₹
                {cartItems.reduce(
                  (acc, item) => acc + item.product.specialPrice * item.qty,
                  0
                )}
              </span>
            </p>
            <hr />
            <button
              id="checkout_btn"
              onClick={() => navigate("/ordersuccess")}
              className="btn btn-primary btn-block"
            >
              Proceed to Buy{" "}
              {cartItems.reduce((acc, item) => acc + item.qty, 0)} Items
            </button>
          </div>
        </div>
      </div>
    </div>
  ) : (
    <h2 className="mt-5">Your Cart is Empty!</h2>
  );
}

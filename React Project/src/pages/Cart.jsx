import { Fragment } from "react";
import { Link, useNavigate } from "react-router-dom";

export default function Cart({ cartItems, setCartItems }) {
  const navigate = useNavigate();
  function increaseQty(item) {
    if (item.product.quantity === item.qty) {
      return;
    }
    const updatedItems = cartItems.map((i) => {
      if (i.product.id === item.product.id) {
        i.qty++;
      }
      return i;
    });
    setCartItems(updatedItems);
  }
  function decreaseQty(item) {
    if (item.qty > 1) {
      const updatedItems = cartItems.map((i) => {
        if (i.product.id === item.product.id) {
          i.qty--;
        }
        return i;
      });
      setCartItems(updatedItems);
    }
  }

  function removeItemFromCart(item) {
    const updatedItems = cartItems.filter((i) => {
      if (i.product.id !== item.product.id) {
        return true;
      }
      return false;
    });
    setCartItems(updatedItems);
  }
  return cartItems.length > 0 ? (
    <Fragment>
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
                  : "/images/noimage.jpg"; // fallback image

              return (
                <Fragment key={item.product.id}>
                  <div className="cart-item">
                    <div className="row">
                      <div className="col-4 col-lg-3">
                        <img src={imageUrl} alt="/images/noimage.jpg" height="90" width="115" />
                      </div>

                      <div className="col-5 col-lg-3">
                        {/* <a href="#">{item.product.productName}</a> */}
                        <Link to={"/product/" + item.product.id}>{item.product.productName}</Link>
                      </div>

                      <div className="col-4 col-lg-2 mt-4 mt-lg-0">
                        <p id="card_item_price">₹{item.product.specialPrice}</p>
                      </div>

                      <div className="col-4 col-lg-3 mt-4 mt-lg-0">
                        <div className="stockCounter d-inline">
                          <span className="btn btn-danger minus" onClick={() => decreaseQty(item)}>
                            -
                          </span>
                          <input type="number" className="form-control count d-inline" value={item.qty} readOnly />

                          <span className="btn btn-primary plus" onClick={() => increaseQty(item)}>
                            +
                          </span>
                        </div>
                      </div>

                      <div className="col-4 col-lg-1 mt-4 mt-lg-0">
                        <i id="delete_cart_item" onClick={() => removeItemFromCart(item)} className="fa fa-trash btn btn-danger"></i>
                      </div>
                    </div>
                  </div>
                </Fragment>
              );
            })}
            <hr />
          </div>

          <div className="col-12 col-lg-3 my-4">
            <div id="order_summary">
              <h4>Order Summary</h4>
              <hr />
              <p>
                <b> Subtotal: </b>
                <span className="order-summary-values">{cartItems.reduce((acc, item) => acc + item.qty, 0)} (Items)</span>
              </p>
              <p>
                <b>To Pay:</b>
                <span className="order-summary-values">
                  {cartItems.reduce((acc, item) => acc + item.product.specialPrice * item.qty, 0)}
                </span>
              </p>

              <hr />
              <button id="checkout_btn" onClick={() => navigate("/checkout")} className="btn btn-primary btn-block">
                Proceed to Buy {cartItems.reduce((acc, item) => acc + item.qty, 0)} Items
              </button>
            </div>
          </div>
        </div>
      </div>
    </Fragment>
  ) : (
    <h2 className="mt-5">Your Cart is Empty !</h2>
  );
}

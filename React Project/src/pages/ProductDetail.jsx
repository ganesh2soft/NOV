import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { toast } from "react-toastify";

export default function ProductDetails({ cartItems, setCartItems }) {
  const [product, setProduct] = useState(null);
  const [qty, setQty] = useState(1);

  const { id } = useParams();
  useEffect(() => {
    fetch(process.env.REACT_APP_API_URL + "/public/products/" + id)
      .then((res) => res.json())
      .then((data) => {
        console.log(data);
        setProduct(data);
      })
      .catch((err) => console.error(err));
  }, [id]);
  if (!product) {
    return <h2 className="text-center mt-5">Loading...</h2>;
  }
  const imageUrl =
    product.image && product.image.length > 0
      ? `${process.env.REACT_APP_API_URL}/public/products/image/${product.image}`
      : "/images/noimage.jpg"; // fallback image

  function increaseQty() {
    if (product.quantity === qty) {
      return;
    }
    setQty((state) => state + 1);
  }
  function decreaseQty() {
    if (qty > 1) {
      setQty((state) => state - 1);
    }
  }
  function addToCart() {
    const itemsExists = cartItems.find((item) => item.product.id === product.id);
    if (!itemsExists) {
      const newItem = { product, qty };
      setCartItems((state) => [...state, newItem]);
      toast.success("Cart Item Added Successfully !!!", {
        position: "top-center",
        theme: "colored",
        autoClose: 3000,
      });
    }
  }
  return (
    <div>
      <div className="container container-fluid">
        <div className="row f-flex justify-content-around">
          <div className="col-12 col-lg-5 img-fluid" id="product_image">
            <img src={imageUrl} alt="/images/noimage.jpg" height="500" width="500" />
          </div>

          <div className="col-12 col-lg-5 mt-5">
            <h3>{product.productName}</h3>
            <p id="product_id">Product # {product.id}</p>

            <hr />
            <p className="card-text mb-1">
              <span id="product_price" className="card-text text-danger font-weight-bold">
                ₹{product.specialPrice}
              </span>

              <small className="text-muted">
                <del>₹{product.price}</del>
              </small>
              <span className="badge bg-success ms-2">{product.discount}% OFF</span>
            </p>

            <div className="stockCounter d-inline">
              <span className="btn btn-danger minus" onClick={decreaseQty}>
                -
              </span>

              {/* <input type="number" className="form-control count d-inline" value="1" readOnly /> */}
              <input type="number" className="form-control count d-inline" value={qty} readOnly />
              <span className="btn btn-primary plus" onClick={increaseQty}>
                +
              </span>
            </div>
            <button
              onClick={addToCart}
              disabled={product.quantity === 0}
              type="button"
              id="cart_btn"
              className="btn btn-primary d-inline ml-4"
            >
              Add to Cart
            </button>

            <hr />

            <p>
              Status:
              <span id="stock_status" className={product.quantity > 0 ? "text-success" : "text-danger"}>
                {product.quantity > 0 ? "In Stock" : "Out of Stock"}
              </span>
            </p>

            <hr />

            <h4 className="mt-2">Description:</h4>
            <p>
              Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum id enim non justo hendrerit maximus ac nec sem.
              Pellentesque feugiat ante at neque tristique, eget pulvinar ex ornare. Nulla facilisi. Integer cursus congue enim ut commodo.
              Donec aliquam mattis velit, sit amet bibendum justo egestas interdum. Duis volutpat dolor est, sit amet sagittis libero
              interdum eleifend.
            </p>
            <hr />
            <p id="product_seller mb-3">
              Sold by: <strong>Amazon</strong>
            </p>

            <div className="rating w-50"></div>
          </div>
        </div>
      </div>
    </div>
  );
}

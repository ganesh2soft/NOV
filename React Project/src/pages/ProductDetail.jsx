import { useDispatch, useSelector } from "react-redux";
import { useParams, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { addToCart, fetchCart } from "../slices/cartThunks";

export default function ProductDetails() {
  const { id } = useParams();
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const { isAuthenticated } = useSelector((state) => state.auth);
  const { loading } = useSelector((state) => state.cart);

  const [product, setProduct] = useState(null);
  const [quantity, setQuantity] = useState(1);

  // Fetch product details from API
  useEffect(() => {
    const getProduct = async () => {
      try {
        const res = await fetch(
          `${process.env.REACT_APP_API_URL}/public/products/${id}`
        );
        const data = await res.json();
        setProduct(data);
      } catch (err) {
        console.error("Error fetching product:", err);
      }
    };
    getProduct();
  }, [id]);

  // Quantity handlers
  const increaseQty = () => setQuantity((prev) => prev + 1);
  const decreaseQty = () => setQuantity((prev) => (prev > 1 ? prev - 1 : 1));

  // Handle add to cart
  const handleAddToCart = async () => {
    if (!isAuthenticated) {
      navigate("/login");
      return;
    }

    try {
      // Wait for addToCart thunk to finish
      await dispatch(addToCart({ productId: product.id, quantity })).unwrap();
      // Refresh cart
      dispatch(fetchCart());
      // Forward to success page
      navigate("/ordersuccess");
    } catch (err) {
      console.error("Failed to add to cart:", err);
      alert("Could not add product to cart. Please try again.");
    }
  };

  if (!product) return <h2>Loading product...</h2>;

  return (
    <div className="container mt-5">
      <div className="row">
        {/* Product Image */}
        <div className="col-md-6">
          <img
            src={
              product.image
                ? `${process.env.REACT_APP_API_URL}/public/products/image/${product.image}`
                : "/images/noimage.jpg"
            }
            alt={product.productName}
            className="img-fluid"
          />
        </div>

        {/* Product Info */}
        <div className="col-md-6">
          <h3>{product.productName}</h3>
          <p>{product.description}</p>
          <h4>₹{product.specialPrice}</h4>

          {/* Quantity selector */}
          <div className="d-flex align-items-center my-3">
            <button className="btn btn-secondary" onClick={decreaseQty}>
              -
            </button>
            <span className="mx-2">{quantity}</span>
            <button className="btn btn-secondary" onClick={increaseQty}>
              +
            </button>
          </div>

          {/* Add to Cart button */}
          <button
            disabled={loading}
            onClick={handleAddToCart}
            className="btn btn-primary mt-3"
          >
            {loading ? "Adding..." : "Add to Cart"}
          </button>
        </div>
      </div>
    </div>
  );
}

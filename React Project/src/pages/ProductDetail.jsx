import { useDispatch, useSelector } from "react-redux";
import { useParams, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { addToCart, fetchCart } from "../slices/cartThunks";

export default function ProductDetails() {
  const { id } = useParams();
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const { isAuthenticated } = useSelector((state) => state.auth);
  const { cartItems, loading } = useSelector((state) => state.cart);

  const [product, setProduct] = useState(null);

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

  // Handle add to cart
  const handleAddToCart = async () => {
    if (!isAuthenticated) {
      navigate("/login");
      return;
    }

    // Default quantity is 1
    await dispatch(addToCart({ productId: product.id, qty: 1 }));
    dispatch(fetchCart()); // refresh cart after adding
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

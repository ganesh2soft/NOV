import React from "react";
import { Link } from "react-router-dom";
function ProductCard({product}) {
  const imageUrl = `${process.env.REACT_APP_API_URL}/public/products/image/${product.image}`;
  return (
    <div>
      <div className="col-sm-12 col-md-6 col-lg-3 my-3">
        <div className="card p-3 rounded">
          <img className="card-img-top mx-auto" src={imageUrl} alt="image-unavailable" />
          <div className="card-body d-flex flex-column">
            <h5 className="card-title">
              <Link to={"/product/" + product.id}>{product.productName}</Link>
            </h5>
            <p className="card-text mb-1">
              <span className="card-text text-danger font-weight-bold"> ₹{product.specialPrice}</span>

              <small className="text-muted">
                <del>₹{product.price}</del>
              </small>
              <span className="badge bg-success ms-2">{product.discount}% OFF</span>
            </p>
            <Link to={"/product/" + product.id} id="view_btn" className="btn btn-block">
              View Details
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
}

export default ProductCard;

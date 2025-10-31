import { Fragment } from "react/jsx-runtime";
import ProductCard from "../components/ProductCard";
import { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";

export default function Home() {
  const [products, setProducts] = useState([]);
  const [searchParams] = useSearchParams();

  useEffect(() => {
    console.log("Fetching all ", process.env.REACT_APP_API_URL);
    fetch(process.env.REACT_APP_API_URL + "/public/products")
      .then((res) => res.json())
      .then((data) => setProducts(data))
      .catch((err) => console.error(err));
  }, []);

  useEffect(() => {
    const queryString = searchParams.toString(); // "keyword=abc"
    if (!queryString) return; // skip if no search

    fetch(
      `${process.env.REACT_APP_API_URL}/public/products/search?${queryString}`
    )
      .then((res) => res.json())
      .then((data) => {
        if (Array.isArray(data)) setProducts(data);
        else setProducts([]);
      })
      .catch((err) => console.error(err));
  }, [searchParams]);

  return (
    <Fragment>
      <h1 id="products_heading">Latest Products</h1>

      <section id="products" className="container mt-5">
        <div className="row">
          {products.map((pr) => (
            <ProductCard key={pr.id} product={pr} />
          ))}
          {/* {Array.isArray(products) && products.length > 0 ? (
            products.map((pr) => <ProductCard key={pr.id} product={pr} />)
          ) : (
            <p>No products found.</p>
          )} */}
        </div>
      </section>
    </Fragment>
  );
}

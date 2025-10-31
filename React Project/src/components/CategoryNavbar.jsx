import React from "react";
import { Container, Nav, Navbar } from "react-bootstrap";

function CategoryNavbar() {
  return (
    <Navbar bg="primary" expand="lg" className="shadow-sm category-navbar mb-5 category-navbar-custom">
      <Container>
        <Navbar.Toggle aria-controls="category-navbar" />
        <Navbar.Collapse id="category-navbar">
          <Nav className="me-auto">
            <Nav.Link href="/electronics">Electronics</Nav.Link>
            <Nav.Link href="/toys">Toys</Nav.Link>
            <Nav.Link href="/clothes">Clothes</Nav.Link>
            <Nav.Link href="/kitchen">Kitchen & Dining</Nav.Link>
            <Nav.Link href="/health-beauty">Health & Beauty</Nav.Link>
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
  /* return (
    <Navbar bg="primary" expand="lg" className="shadow-sm category-navbar mb-5 category-navbar-custom">
      <Container>
        <Navbar.Toggle aria-controls="category-navbar" />
        <Navbar.Collapse id="category-navbar">
          <Nav className="me-auto">
            <Nav.Link as={Link} to="/electronics">
              Electronics
            </Nav.Link>
            <Nav.Link as={Link} to="/toys">
              Toys
            </Nav.Link>
            <Nav.Link as={Link} to="/clothes">
              Clothes
            </Nav.Link>
            <Nav.Link as={Link} to="/kitchen">
              Kitchen & Dining
            </Nav.Link>
            <Nav.Link as={Link} to="/beauty-health">
              Beauty & Health
            </Nav.Link>
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );*/
}

export default CategoryNavbar;

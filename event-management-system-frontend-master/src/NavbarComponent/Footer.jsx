import { Link } from "react-router-dom";
import { FaSquareFacebook,FaTwitter,FaSquareInstagram,FaLinkedin
   ,FaVoicemail} from "react-icons/fa6";
const Footer = () => {
  return (
    <div>
      <div class="container my-5">
        <footer class="text-center text-lg-start text-color">
          <div class="container-fluid p-4 pb-0">
            <section class="">
              <div class="row">
                <div class="col-lg-4 col-md-4 me-3 mb-4 mb-md-0">
                  <h5 class="text-uppercase text-color-second fw-bold">
                    Event Management System
                  </h5>

                  <p>
                    Welcome to our event management system website, where every
                    detail meets perfection. Let's turn your vision into an
                    unforgettable experience!
                  </p>
                </div>

                <div class="col-lg-2 col-md-4 ms-3 me-2 mb-4 mb-md-0">
                  <h5 class="text-uppercase text-color-second fw-bold">Quick Links</h5>

                  <ul class="list-unstyled mb-0">
                    <li><a href="" class="text-decoration-none text-dark  ">Products</a></li>
                    <li><a href="" class="text-decoration-none   text-dark  ">Partners</a></li>
                    <li><a href="" class="text-decoration-none   text-dark  ">Support</a></li>
                    <li><a href="" class="text-decoration-none   text-dark  ">Customers</a></li>
                    <li><a href="" class="text-decoration-none   text-dark  ">Trails</a></li>
                  </ul>
                </div>

                <div class="col-lg-2 col-md-4 ms-3 mb-4 me-4  mb-md-0">
                  <h5 class="text-uppercase text-color-second fw-bold">Contact us</h5>

                  <ul class="list-unstyled mb-0">
                  <li><a href="" class="text-decoration-none  text-dark  ">info@gmail.com</a></li>
                  <li><a href="" class="text-decoration-none text-dark ">+91 9876543210</a></li>
                  <li><a href="" class="text-decoration-none text-dark fw-semibold">Address</a></li>
                  <span >#04-01, Paya Lebar Quarter<br/>Singapore, 408533</span>
                  </ul>
                </div>

                <div class="col-lg-3 col-md-4 ms-2  mb-4 mb-md-0">
                  <h5 class="text-uppercase text-color-second fw-bold">Follow Us</h5>

                  
                  <div class="mt-5">
                  <span><a href="" class="text-decoration-none me-2" ><FaSquareFacebook style={{height:'35px',width:'35px'}} /></a></span>
                  <span><a href="" class="text-decoration-none me-2" ><FaTwitter style={{height:'35px',width:'35px'}} /></a></span>
                  <span><a href="" class="text-decoration-none me-2" ><FaSquareInstagram style={{height:'35px',width:'35px'}} /></a></span>
                  <span><a href="" class="text-decoration-none" ><FaLinkedin style={{height:'35px',width:'35px'}} /></a></span>
                  </div>
                </div>

                 
              </div>
            </section>

            <hr class="mb-4" />

            <section class="">
              <p class="d-flex justify-content-center align-items-center">
                <span class="me-3 text-color">Login from here</span>
                <Link to="/user/login" class="active">
                  <button
                    type="button"
                    class="btn btn-outline-light btn-rounded bg-color text-color"
                  >
                    Log in
                  </button>
                </Link>
              </p>
            </section>

            <hr class="mb-3" />
          </div>

          <div class="text-center">
            © 2024 Copyright:
            <a class="text-color-3" href="#">
              eventmanagementsystem.com
            </a>
          </div>
        </footer>
      </div>
    </div>
  );
};

export default Footer;

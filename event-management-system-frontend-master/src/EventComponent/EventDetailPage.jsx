import { useParams, useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import axios from "axios";
import { ToastContainer, toast } from "react-toastify";

const EventDetailPage = () => {
  const { eventId } = useParams();

  const customer = JSON.parse(sessionStorage.getItem("active-customer"));
  const customer_jwtToken = sessionStorage.getItem("customer-jwtToken");

  const navigate = useNavigate();

  const [event, setEvent] = useState({});

  useEffect(() => {
    const getEvent = async () => {
      const fetchEventResponse = await retrieveEvent();
      if (fetchEventResponse) {
        setEvent(fetchEventResponse.events[0]);
      }
    };
    getEvent();
  }, []);

  const retrieveEvent = async () => {
    const response = await axios.get(
      "http://localhost:8080/api/event/fetch?eventId=" + eventId
    );
    console.log(response.data);
    return response.data;
  };

  const formatDateFromEpoch = (epochTime) => {
    const date = new Date(Number(epochTime));
    const formattedDate = date.toLocaleString(); // Adjust the format as needed

    return formattedDate;
  };

  const bookEventPage = (e) => {
    e.preventDefault();
    if (customer === null) {
      alert("Please login as customer to book an event!!!");
    } else {
      navigate("/event/booking/page", { state: event });
    }
  };

  return (
    <div className="mb-3">
      <div className="col ml-5 mt-3 ms-5 me-5">
        {/* Company and Employer Details Card */}
        <div className="card rounded-card h-100 shadow-lg ">
          <div className="row g-0">
            {/* Left side - Company Details Card */}
            <div className="col-md-6">
              <div className="card-body">
                <h4 className="card-title text-color-second">Event Details</h4>
                <div className="row g-0">
                  {/* Left side - Company Logo */}
                  <div className="col-md-4 d-flex align-items-center justify-content-center">
                    <img
                      src={"http://localhost:8080/api/event/" + event.image}
                      className="card-img-top rounded img-fluid"
                      alt="event image"
                      style={{
                        maxHeight: "100px",
                        width: "auto",
                      }}
                    />
                  </div>
                  {/* Right side - Job Details */}
                  <div className="col-md-8">
                    <div className="card-body text-color">
                      <h3 className="card-title d-flex justify-content-between text-color-second">
                        <div>
                          <b>{event.name}</b>
                        </div>
                      </h3>
                      <b className="card-text">{event.description}</b>
                      <br />
                      <b className="card-text">{event.location}</b>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            {/* Right side - Employer Details Card */}
            <div className="col-md-6">
              <div className="card-body">
                <h4 className="card-title text-color-second">Venue Details</h4>
                {/* Include the necessary details for the employer */}
                {/* Display First Name and Last Name in a row */}
                <div className="row mt-4">
                  <div className="col-md-6">
                    <p className="mb-2">
                      <b>Venue Name:</b> {event.venueName}
                    </p>
                  </div>
                  <div className="col-md-6">
                    <p className="mb-2">
                      <b>Venue Type:</b> {event.venueType}
                    </p>
                  </div>
                </div>
                <div className="row mt-2">
                  <div className="col-md-6">
                    <p className="mb-2">
                      <b>Location:</b> {event.location}
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* New Row - Job Details Card */}
        <div className="row mt-3">
          <div className="col">
            <div className="card rounded-card h-100 shadow-lg">
              <div className="card-body">
                <h3 className="card-title text-color-second">Ticket Details</h3>
                {/* Row 1 - Job Title */}
                <div className="row mt-4 ms-4 me-4">
                  <div className="col-md-4">
                    <p className="mb-2">
                      <b className="mb-2">Total Tickets:</b> {event.noOfTickets}
                    </p>
                  </div>
                  <div className="col-md-4">
                    <p className="mb-2">
                      <b>Available Tickets:</b> {event.availableTickets}
                    </p>
                  </div>
                  <div className="col-md-4">
                    <p className="mb-2">
                      <b>Ticket Price:</b> &#8377; {event.ticketPrice}
                    </p>
                  </div>
                </div>

                {/* Row 2 - Job Title */}
                <div className="row mt-4 ms-4 me-4">
                  <div className="col-md-4">
                    <p className="mb-2">
                      <b className="mb-2">Event Start Time:</b>{" "}
                      {formatDateFromEpoch(event.startDate)}
                    </p>
                  </div>
                  <div className="col-md-4">
                    <p className="mb-2">
                      <b className="mb-2">Event End Time:</b>{" "}
                      {formatDateFromEpoch(event.endDate)}
                    </p>
                  </div>
                </div>

                <div className="d-flex justify-content-center mt-4">
                  <button
                    type="button"
                    className="btn bg-color custom-bg-text mb-3"
                    onClick={(e) => bookEventPage(e)}
                  >
                    Book Event
                  </button>
                  <ToastContainer />
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default EventDetailPage;

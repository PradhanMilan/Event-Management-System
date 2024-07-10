import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import Carousel from "./Carousel";
import Footer from "../NavbarComponent/Footer";
import { useNavigate } from "react-router-dom";
import EventCard from "../EventComponent/EventCard";

const HomePage = () => {
  const navigate = useNavigate();
  const [categories, setCategories] = useState([]);

  const [eventName, setEventName] = useState("");
  const [eventCategoryId, setEventCategoryId] = useState("");
  const [tempEventName, setTempEventName] = useState("");
  const [tempEventCategoryId, setTempEventCategoryId] = useState("");
  const [allEvents, setAllEvents] = useState([]);

  const retrieveAllCategories = async () => {
    const response = await axios.get(
      "http://localhost:8080/api/event/category/fetch/all"
    );
    return response.data;
  };

  useEffect(() => {
    
    console.log(eventName);

    const getAllEvents = async () => {
      const allEvents = await retrieveAllEvents();
      if (allEvents) {
        setAllEvents(allEvents.events);
      }
    };

    const getSearchedEvents = async () => {
      const allEvents = await searchEvents();
      if (allEvents) {
        setAllEvents(allEvents.events);
      }
    };

    const getAllCategories = async () => {
      const resCategory = await retrieveAllCategories();
      if (resCategory) {
        setCategories(resCategory.categories);
      }
    };

    if (eventCategoryId !== "" || eventName !== "") {
      getSearchedEvents();
    } else {
      getAllEvents();
    }

    getAllCategories();
  }, [eventCategoryId, eventName]);

  const retrieveAllEvents = async () => {
    const response = await axios.get(
      "http://localhost:8080/api/event/fetch/all/active"
    );
    return response.data;
  };

  const searchEvents = async () => {
    if (eventName !== "") {
      const response = await axios.get(
        "http://localhost:8080/api/event/fetch/name-wise?eventName=" + eventName
      );

      return response.data;
    } else if (eventCategoryId !== "" || eventCategoryId !== "0") {
      const response = await axios.get(
        "http://localhost:8080/api/event/fetch/category-wise?categoryId=" +
          eventCategoryId
      );
      return response.data;
    }
  };

  const searchEventByName = (e) => {
    e.preventDefault();
    setEventName(tempEventName);

    setTempEventName("");
    setEventCategoryId("");
  };

  const searchEventByCategory = (e) => {
    e.preventDefault();
    setEventCategoryId(tempEventCategoryId);

    setTempEventCategoryId("");
    setEventName("");
  };

  return (
    <div className="container-fluid mb-2">
      <Carousel />
      <h5 className="text-color-second text-center mt-3">
        Search Events here..!!
      </h5>

      <div className="d-flex aligns-items-center justify-content-center">
        <div className="row">
          <div className="col-auto">
            <div className="mt-3">
              <form class="row g-3">
                <div class="col-auto">
                  <input
                    type="text"
                    className="form-control"
                    id="city"
                    name="eventName"
                    onChange={(e) => setTempEventName(e.target.value)}
                    value={tempEventName}
                    placeholder="Search Event here..."
                  />
                </div>

                <div class="col-auto">
                  <button
                    type="submit"
                    class="btn bg-color text-color mb-3"
                    onClick={searchEventByName}
                  >
                    Search
                  </button>
                </div>
              </form>
            </div>
          </div>
          <div className="col">
            <div className="mt-3">
              <form class="row g-3">
                <div class="col-auto">
                  <select
                    name="tempEventCategoryId"
                    onChange={(e) => setTempEventCategoryId(e.target.value)}
                    className="form-control"
                    required
                  >
                    <option value="">Select Event Category</option>

                    {categories.map((category) => {
                      return (
                        <option value={category.id}> {category.name} </option>
                      );
                    })}
                  </select>
                </div>

                <div class="col-auto">
                  <button
                    type="submit"
                    class="btn bg-color text-color mb-3"
                    onClick={searchEventByCategory}
                  >
                    Search
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>

      <div className="col-md-12 mt-3 mb-5">
        <div className="row row-cols-1 row-cols-md-2 g-4">
          {allEvents.map((event) => {
            return <EventCard item={event} key={event.id} />;
          })}
        </div>
      </div>
      <hr />
      <Footer />
    </div>
  );
};

export default HomePage;

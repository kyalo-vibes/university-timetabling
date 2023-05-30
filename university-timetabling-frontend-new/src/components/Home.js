import React, { useState, useEffect } from "react";
import axios from "axios";
import { Button, Form, Container } from "react-bootstrap";
import Layout from "../Layout/DashboardLayout";
import { useContext } from "react";
import AuthContext from "../context/AuthProvider";
import { useNavigate } from "react-router-dom";
import useAuth from "../hooks/useAuth";
import "../styles/styles.css";

const Home = () => {
  const [semester, setSemester] = useState(1);
  const [timetables, setTimetables] = useState([]);
  const [originalData, setOriginalData] = useState([]);
  const [loading, setLoading] = useState(false); // New state variable for loading
  const [isCompleted, setIsCompleted] = useState(false); // New state variable for completion
  const { setAuth } = useContext(AuthContext);
  const { auth } = useAuth();
  const navigate = useNavigate();

  // Function to generate timetable
  const generateTimetable = (event) => {
    event.preventDefault();
    setLoading(true); 
    axios
    .post(
      `http://localhost:8080/api/schedule/generate?semester=${semester}`,
      {},
      {
        headers: {
          Authorization: `Bearer ${auth.accessToken}`,
        },
      }
    )
    .then((response) => {
      let intervalId = setInterval(() => {
        axios
          .get(
            `http://localhost:8080/api/schedule/status?semester=${semester}`,
            {
              headers: {
                Authorization: `Bearer ${auth.accessToken}`,
              },
            }
          )
          .then((response) => {
            if (response.data === 'COMPLETED') {
              clearInterval(intervalId);
              setLoading(false); 
              setIsCompleted(true); 
              // Use setTimeout to create a small delay before the alert
              window.setTimeout(() => alert("Timetables generated"), 50);
              fetchTimetables(); 
            }
          })
          .catch((error) => console.error(`Error: ${error}`));
      }, 5000);
    })
    .catch((error) => {
      console.error(`Error: ${error}`);
      setLoading(false);
    });
};

  const handleFilter = (filters) => {
    // filter your data here based on the filters
    let filteredData = [...timetables];

    // filter by ID
    if (filters.ID) {
      filteredData = filteredData.filter(
        (item) => item.id === Number(filters.ID)
      );
    }

    // filter by Course Codes
    if (filters["Course Codes"]) {
      filteredData = filteredData.filter((item) =>
        item.courseCodes.some((courseCode) =>
          courseCode.includes(filters["Course Codes"])
        )
      );
    }

    // filter by Time Slots
    if (filters["Time Slots"]) {
      filteredData = filteredData.filter((item) =>
        item.timeSlots.some((timeSlot) =>
          timeSlot.includes(filters["Time Slots"])
        )
      );
    }

    // filter by Instructor Names
    if (filters["Instructor Names"]) {
      filteredData = filteredData.filter((item) =>
        item.instructorNames.some((instructorName) =>
          instructorName.includes(filters["Instructor Names"])
        )
      );
    }

    // filter by Room Names
    if (filters["Room Names"]) {
      filteredData = filteredData.filter((item) =>
        item.roomNames.some((roomName) =>
          roomName.includes(filters["Room Names"])
        )
      );
    }

    // update the data state with the filtered data
    setTimetables(filteredData);
  };

  const handleReset = () => {
    setTimetables(originalData);
  };

  // Function to fetch all timetables
  const fetchTimetables = () => {
    axios
      .get("http://localhost:8080/api/schedule/timetables", {
        headers: {
          Authorization: `Bearer ${auth.accessToken}`,
        },
      })
      .then((response) => {
        setTimetables(response.data);
        setOriginalData(response.data);
      })
      .catch((error) => console.error(`Error: ${error}`));
  };

  // Fetch all timetables on initial render
  useEffect(() => {
    if (!loading && isCompleted) {
      const timer = setTimeout(() => setIsCompleted(false), 2000);
      return () => clearTimeout(timer);
    }
  }, [loading, isCompleted]);

  useEffect(() => {
    fetchTimetables();
  }, []);

  console.log(timetables);

  // handle logout
  async function logout() {
    setAuth({});
    localStorage.removeItem("user"); // Clear 'user' in localStorage
    navigate("/login");
  }

  return (
    <Layout>
      <main>
        {loading || isCompleted ? (
          <div className="loading-overlay">
            <div className={`spinner ${isCompleted ? "success" : "spinning"}`}>
              {isCompleted && <div className="success-message">✔︎</div>}
            </div>
            <p className="text">
              {isCompleted
                ? "Successfully generated timetables!"
                : "Generating timetables..."}
            </p>
          </div>
        ) : null}

        <section className="flex justify-between">
          <h1>University Timetabling System</h1>
          <button onClick={logout} className="btn btn-warning">
            Logout
          </button>
        </section>

        <form className="flex justify-start items-center py-4">
          <div>
            <label>Semester</label>
            <select className="select select-accent w-full max-w-xs">
              <option disabled selected>
                Select the semester
              </option>
              <option>1</option>
              <option>2</option>
            </select>
          </div>
          <button
            className="btn btn-accent"
            onClick={(event) => generateTimetable(event)}
          >
            Generate Timetable
          </button>
        </form>

        <h2>Timetables</h2>
        <div className="flex flex-col items-start">
          <Filter
            columns={[
              "ID",
              "Course Codes",
              "Time Slots",
              "Instructor Names",
              "Room Names",
            ]}
            onFilter={handleFilter}
          />
          <button className="btn btn-accent" onClick={handleReset}>
            Reset Filters
          </button>
        </div>
        <Table data={timetables} />
      </main>
    </Layout>
  );
};

const Table = ({ data }) => {
  return (
    <table className="table-auto">
      <thead>
        <tr>
          <th className="px-4 py-2">ID</th>
          <th className="px-4 py-2">Course Codes</th>
          <th className="px-4 py-2">Time Slots</th>
          <th className="px-4 py-2">Instructor Names</th>
          <th className="px-4 py-2">Room Names</th>
        </tr>
      </thead>
      <tbody>
        {data.map((item) =>
          item.courseCodes.map((courseCode, index) => (
            <tr key={`${item.id}-${index}`}>
              <td className="border px-4 py-2">{item.id}</td>
              <td className="border px-4 py-2">{courseCode}</td>
              <td className="border px-4 py-2">{item.timeSlots[index]}</td>
              <td className="border px-4 py-2">
                {item.instructorNames[index]}
              </td>
              <td className="border px-4 py-2">{item.roomNames[index]}</td>
            </tr>
          ))
        )}
      </tbody>
    </table>
  );
};

const Filter = ({ columns, onFilter }) => {
  const [filters, setFilters] = useState({});

  const handleFilterChange = (event) => {
    const { name, value } = event.target;
    setFilters((prevFilters) => ({
      ...prevFilters,
      [name]: value,
    }));
  };

  const handleFilterSubmit = (event) => {
    event.preventDefault();
    onFilter(filters);
  };

  return (
    <form onSubmit={handleFilterSubmit} className="flex items-center py-6">
      {columns.map((column) => (
        <div key={column}>
          <label htmlFor={column}>{column}</label>
          <input
            className="input w-1/2 input-bordered input-primary"
            type="text"
            id={column}
            name={column}
            onChange={handleFilterChange}
          />
        </div>
      ))}
      <button className="btn" type="submit">
        Filter
      </button>
    </form>
  );
};

export default Home;

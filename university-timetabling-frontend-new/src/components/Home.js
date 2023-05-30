import React, { useState, useEffect } from "react";
import axios from "axios";
import { Button, Form, Container } from "react-bootstrap";
import Layout from "../Layout/DashboardLayout";
import { useContext } from "react";
import AuthContext from "../context/AuthProvider";
import { useNavigate } from "react-router-dom";
import useAuth from "../hooks/useAuth";

const Home = () => {
  const [semester, setSemester] = useState(1);
  const [timetables, setTimetables] = useState([]);
  const { setAuth } = useContext(AuthContext);
  const { auth } = useAuth();
  const navigate = useNavigate();

  // Function to generate timetable
  const generateTimetable = () => {
    axios
      .post(
        "http://localhost:8080/api/schedule/generate",
        {
          semester: semester,
        },
        {
          headers: {
            Authorization: `Bearer ${auth.accessToken}`,
          },
        }
      )
      .then((response) => {
        alert("Timetables generated");
        fetchTimetables();
      })
      .catch((error) => console.error(`Error: ${error}`));
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
      })
      .catch((error) => console.error(`Error: ${error}`));
  };

  // Fetch all timetables on initial render
  useEffect(() => {
    fetchTimetables();
  }, []);

  console.log(timetables);

  // handle logout
  async function logout() {
    setAuth({});
    localStorage.removeItem("user");
    navigate("/login");
  }

  return (
    <Layout>
      <main>
        <h1>University Timetabling System</h1>

        <button onClick={logout} className="btn btn-warning">
          Logout
        </button>

        <form>
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
          <button className="btn btn-accent" onClick={generateTimetable}>
            Generate Timetable
          </button>
        </form>

        <h2>Timetables</h2>
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
    <form onSubmit={handleFilterSubmit}>
      {columns.map((column) => (
        <div key={column}>
          <label htmlFor={column}>{column}</label>
          <input
            type="text"
            id={column}
            name={column}
            onChange={handleFilterChange}
          />
        </div>
      ))}
      <button type="submit">Filter</button>
    </form>
  );
};

export default Home;

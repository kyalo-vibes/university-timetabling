import React, { useState, useEffect } from "react";
import axios from "axios";
import { Button, Form, Container, Modal } from "react-bootstrap";
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
  const [timeslots, setTimeslots] = useState([]);
  const [resetLoading, setResetLoading] = useState(false);
  const [showResetModal, setShowResetModal] = useState(false); // State variable for showing the reset confirmation modal

  

  const fetchTimeslots = () => {
    axios
      .get("http://localhost:8080/api/timeslots", {
        headers: {
          Authorization: `Bearer ${auth.accessToken}`,
        },
      })
      .then((response) => {
        setTimeslots(response.data.map(timeslot => {
          const startHour = timeslot.startTime.slice(0, 5);
          const endHour = timeslot.endTime.slice(0, 5);
          return `${timeslot.day}: ${startHour} - ${endHour}`;
        }));
      })
      .catch((error) => console.error(`Error: ${error}`));
};

  
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

  const handleHardReset = () => {
    setResetLoading(true);

    axios
      .post("http://localhost:8080/reset", null, {
        headers: {
          Authorization: `Bearer ${auth.accessToken}`,
        },
      })
      .then((response) => {
        alert("Reset successful");
        fetchTimetables();
        setResetLoading(false);
      })
      .catch((error) => {
        console.error(`Error: ${error}`);
        setResetLoading(false);
      });
  };


  const cancelReset = () => {
    setShowResetModal(false);
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

  useEffect(() => {
    fetchTimeslots();
  }, []);

  console.log(timeslots);

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
  <div className="mr-4">
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
    className="btn btn-accent mt-5"
    onClick={(event) => generateTimetable(event)}
  >
    Generate Timetable
  </button>
  <button
  className="btn btn-danger mt-5 ml-5"
  onClick={handleHardReset}
  disabled={resetLoading} // Use resetLoading state instead of loading state
>
  Reset Schedules
</button>


</form>

        <h2>Timetables</h2>
        <div className="flex flex-row items-center">
  <Filter
    columns={[
      "ID",
      "Course Codes",
      "Time Slots",
      "Instructor Names",
      "Room Names",
    ]}
    onFilter={handleFilter}
    className="mr-4" // Add margin-right to create a gap
  />
  <button className="btn btn-accent ml-4" onClick={handleReset}>
    Reset Filters
  </button>
</div>


        <Table data={timetables} timeslots={timeslots} />
      </main>
    </Layout>
  );
};

// Utility function to format timetable data
const formatTimetableData = (scheduleData, uniqueTimeslots) => {
  const daysOfWeek = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday"];

  // Initialize timetable
  let timetable = { 
    days: daysOfWeek,
    timeslots: uniqueTimeslots,
    schedule: {}
  };

  // Initialize schedule with empty arrays
  daysOfWeek.forEach(day => {
    timetable.schedule[day] = Array(uniqueTimeslots.length).fill(null);
  });

  // Populate schedule with data
  scheduleData.timeSlots.forEach((timeSlot, index) => {
    const [day, timeslot] = timeSlot.split(': ');
    const timeslotStripped = timeslot.trim(); // remove leading and trailing spaces
    const timetableIndex = uniqueTimeslots.indexOf(timeslotStripped);

    if (timetableIndex !== -1) {
      timetable.schedule[day][timetableIndex] = {
        courseCode: scheduleData.courseCodes[index],
        instructorName: scheduleData.instructorNames[index],
        roomName: scheduleData.roomNames[index]
      };
    }
  });

  return timetable;
};


const Table = ({ data, timeslots }) => {
  // Extract unique timeslots from the timeslots prop
  const uniqueTimeslots = [...new Set(timeslots.map(timeSlot => timeSlot.split(': ')[1]))];

  return (
    <>
      {data.map((item) => {
        const timetable = formatTimetableData(item, uniqueTimeslots);

        return (
          <div key={item.id} className="timetable">
            <h3 className="timetable-header">Timetable ID: {item.id}</h3>
            <p className="message">{item.message}</p>
            <table className="table-auto">
              <thead>
                <tr>
                  <th className="px-4 py-2 border">Time Slot</th>
                  {timetable.days.map(day => (
                    <th key={day} className="px-4 py-2 border">{day}</th>
                  ))}
                </tr>
              </thead>
              <tbody>
                {timetable.timeslots.map((timeslot, index) => (
                  <tr key={timeslot}>
                    <td className="border px-4 py-2 text-center bold-text">{timeslot}</td>
                    {timetable.days.map(day => (
                      <td key={day} className={`border px-4 py-2 ${timetable.schedule[day][index] ? '' : 'shaded'} text-center`}>
                        {timetable.schedule[day][index] ? 
                          <>
                            <div>{timetable.schedule[day][index].courseCode}</div>
                            <div>{timetable.schedule[day][index].instructorName}</div>
                            <div>{timetable.schedule[day][index].roomName}</div>
                          </> 
                          : ''}
                      </td>
                    ))}
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        );
      })}
    </>
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

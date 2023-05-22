import React, { useState, useEffect } from "react";
import "./instructor.css";
import axios from "axios";
import { MdDelete } from "react-icons/md";
import Header from "../../components/Header";

const Instructor = () => {
  const [instructors, setInstructors] = useState([]);
  const [departments, setDepartments] = useState([]);
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [selectedDeptName, setSelectedDeptName] = useState("");
  const [allPreferences, setAllPreferences] = useState([]);

  // pagination for the table
  const [currentPage, setCurrentPage] = useState(1);
  const [recordsPerPage] = useState(10);

  const indexOfLastRecord = currentPage * recordsPerPage;
  const indexOfFirstRecord = indexOfLastRecord - recordsPerPage;

  const currentRecords = instructors.slice(
    indexOfFirstRecord,
    indexOfLastRecord
  );

  const isFirstPage = currentPage === 1;

  function handleNextPage() {
    setCurrentPage((prev) => prev + 1);
  }

  function handlePreviousPage() {
    if (!isFirstPage) {
      setCurrentPage((prev) => prev - 1);
    }
  }

  // Fetch all instructors
  const fetchInstructors = () => {
    axios
      .get("http://localhost:8080/api/instructors")
      .then((response) => {
        setInstructors(response.data);
      })
      .catch((error) => console.error(`Error: ${error}`));
  };

  const fetchAllPreferences = () => {
    axios
      .get("http://localhost:8080/api/instructors/preferences")
      .then((response) => {
        const preferencesData = response.data;
        setAllPreferences(preferencesData);
      })
      .catch((error) => console.error(`Error: ${error}`));
  };

  // Fetch all departments
  const fetchDepartments = () => {
    axios
      .get("http://localhost:8080/api/departments")
      .then((response) => {
        setDepartments(response.data);
      })
      .catch((error) => console.error(`Error: ${error}`));
  };

  // Add a new instructor
  const addInstructor = () => {
    const newInstructor = {
      firstName: firstName,
      lastName: lastName,
      deptName: selectedDeptName,
    };

    axios
      .post("http://localhost:8080/api/instructors", newInstructor)
      .then((response) => {
        alert("Instructor added");
        fetchInstructors();
        fetchAllPreferences();
      })
      .catch((error) => console.error(`Error: ${error}`));
  };

  useEffect(() => {
    fetchInstructors();
    fetchDepartments();
    fetchAllPreferences();
  }, []);

  const handleDelete = (id) => {
    axios
      .delete(`http://localhost:8080/api/instructors/${id}`)
      .then((response) => {
        // Handle successful delete
        fetchInstructors();
      })
      .catch((error) => console.error(`Error: ${error}`));
  };

  return (
    <div className="instructor-page">
      <Header />
      <main>
        <h1 className="text-3xl font-bold">Instructors</h1>

        <div className="main-content--instructors">
          <form
            className="w-2/3 max-w-sm mx-auto"
            onSubmit={(e) => {
              e.preventDefault();
              addInstructor();
            }}
          >
            <div className="md:flex flex-col md:items-center mb-6">
              <div className="md:w-1/2">
                <label className="block text-gray-500 font-bold md:text-center mb-1 md:mb-0 pr-4">
                  First Name
                </label>
                <input
                  required="true"
                  className="bg-gray-200 appearance-none border-2 border-gray-200 rounded w-full py-2 px-4 text-gray-700 leading-tight focus:outline-none focus:bg-white focus:border-purple-500"
                  type="text"
                  value={firstName}
                  onChange={(e) => setFirstName(e.target.value)}
                />
              </div>
              <div className="md:w-1/2">
                <label className="block text-gray-500 font-bold md:text-center mb-1 md:mb-0 pr-4">
                  Last Name
                </label>
                <input
                  required="true"
                  className="bg-gray-200 appearance-none border-2 border-gray-200 rounded w-full py-2 px-4 text-gray-700 leading-tight focus:outline-none focus:bg-white focus:border-purple-500"
                  type="text"
                  value={lastName}
                  onChange={(e) => setLastName(e.target.value)}
                />
              </div>
              <div className="md:w-1/2">
                <label className="block text-gray-500 font-bold md:text-center mb-1 md:mb-0 pr-4">
                  Department
                </label>
                <select
                  required="true"
                  className="bg-gray-200 appearance-none border-2 border-gray-200 rounded w-full py-2 px-4 text-gray-700 leading-tight focus:outline-none focus:bg-white focus:border-purple-500"
                  value={selectedDeptName}
                  onChange={(e) => setSelectedDeptName(e.target.value)}
                >
                  {departments.map((department) => (
                    <option key={department.id} value={department.deptName}>
                      {department.deptName}
                    </option>
                  ))}
                </select>
              </div>
              <div className="md:flex md: items-center mt-6">
                <button
                  className="shadow bg-purple-500 hover:bg-purple-400 focus:shadow-outline focus:outline-none text-white font-bold py-2 px-4 rounded"
                  type="submit"
                >
                  Add Instructor
                </button>
              </div>
            </div>
          </form>

          <section className="instructor-cards">
            <div className="stat">
              <div className="stat-title">Total Instructors</div>
              <div className="stat-value">{instructors.length}</div>
            </div>
            <div className="stat">
              <div className="stat-title">Total Departments</div>
              <div className="stat-value">{departments.length}</div>
            </div>
          </section>

          <section className="">
            <div className="w-4/5 max-w-2xl mx-auto">
              <h2 className="text-lg font-semibold mb-4">
                Existing Instructors
              </h2>
              <table className="w-full text-left border-collapse">
                <thead>
                  <tr>
                    <th className="py-2 px-3 font-bold uppercase text-xs text-gray-700 border-b border-gray-200">
                      First Name
                    </th>
                    <th className="py-2 px-3 font-bold uppercase text-xs text-gray-700 border-b border-gray-200">
                      Last Name
                    </th>
                    <th className="py-2 px-3 font-bold uppercase text-xs text-gray-700 border-b border-gray-200">
                      Department Name
                    </th>

                    <th className="py-2 px-3 font-bold uppercase text-xs text-gray-700 border-b border-gray-200">
                      Preferences
                    </th>

                    <th className="py-2 px-3 font-bold uppercase text-xs text-gray-700 border-b border-gray-200">
                      Actions
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {currentRecords.map((instructor, index) => (
                    <tr key={index}>
                      <td className="py-2 px-3 border-b border-gray-200">
                        {instructor.firstName}
                      </td>
                      <td className="py-2 px-3 border-b border-gray-200">
                        {instructor.lastName}
                      </td>
                      <td className="py-2 px-3 border-b border-gray-200">
                        {instructor.deptName}
                      </td>
                      <td className="py-2 px-3 border-b border-gray-200">
                        {/* displaying the instructor preference data */}
                        {allPreferences
                          ? allPreferences.map((preference) => (
                              <div key={preference.id}>
                                {preference.day} {preference.startTime}
                              </div>
                            ))
                          : "No preference yet"}
                      </td>
                      <td className="py-2 px-3 border-b border-gray-200">
                        {/* Provide Edit/Delete functionality here */}
                        <div className="flex items-center ">
                          <MdDelete
                            style={{ cursor: "pointer" }}
                            fontSize={20}
                            className="text-rose-500"
                            onClick={() => handleDelete(instructor.id)}
                          />

                          <button
                            className="text-purple-600 hover:text-purple-400 font-semibold ml-6"
                            onClick={() => handleDelete(instructor.id)}
                          >
                            Edit
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>

              <div className="flex justify-between mt-4">
                <button
                  className="shadow bg-purple-500 hover:bg-purple-400 focus:shadow-outline focus:outline-none text-white font-bold py-2 px-4 rounded"
                  disabled={isFirstPage}
                  onClick={handlePreviousPage}
                >
                  Previous
                </button>
                <button
                  className="shadow bg-purple-500 hover:bg-purple-400 focus:shadow-outline focus:outline-none text-white font-bold py-2 px-4 rounded"
                  onClick={handleNextPage}
                >
                  Next
                </button>
              </div>
            </div>
          </section>

          <section className="preferences">
            <ul className="list-disc list-inside">
              {allPreferences.map((instructor) => (
                <li key={instructor.instructorName}>
                  <span>Instructor Name </span>
                  {instructor.instructorName}
                  <ul>
                    {instructor.preferences.map((preference) => (
                      <li key={preference.id} className="text-gray-700">
                        <span>
                          Preffered Time Slot:
                          {preference.day} {`${preference.startTime}`}
                        </span>
                      </li>
                    ))}
                  </ul>
                </li>
              ))}
            </ul>
          </section>
          <section className="timeslots"></section>
        </div>
      </main>
    </div>
  );
};

export default Instructor;

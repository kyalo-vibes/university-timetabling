import React, { useState, useEffect } from "react";
import axios from "axios";
import DashboardLayout from "../../Layout/DashboardLayout";
import EditModal from "./EditModal";
import AuthContext from "../../context/AuthProvider";
import { useNavigate } from "react-router-dom";
import useAuth from "../../hooks/useAuth";

const Course = () => {
  const [courses, setCourses] = useState([]);
  const [departments, setDepartments] = useState([]);
  const [programmes, setProgrammes] = useState([]);
  const [instructors, setInstructors] = useState([]);
  const [courseCode, setCourseCode] = useState("");
  const [courseName, setCourseName] = useState("");
  const [year, setYear] = useState("");
  const [semester, setSemester] = useState("");
  const [selectedProgrammeName, setSelectedProgrammeName] = useState("");
  const [selectedDeptName, setSelectedDeptName] = useState("");
  const [selectedInstructorName, setSelectedInstructorName] = useState("");
  const [editingCourseId, setEditingCourseId] = useState(null);
  const [showModal, setShowModal] = useState(false);

  const { auth } = useAuth();
  const navigate = useNavigate();

  // pagination for the table
  const [currentPage, setCurrentPage] = useState(1);
  const [recordsPerPage] = useState(10);

  const indexOfLastRecord = currentPage * recordsPerPage;
  const indexOfFirstRecord = indexOfLastRecord - recordsPerPage;

  const currentCourses = courses.slice(indexOfFirstRecord, indexOfLastRecord);

  const isFirstPage = currentPage === 1;

  function handleNextPage() {
    setCurrentPage((prev) => prev + 1);
  }

  function handlePreviousPage() {
    if (!isFirstPage) {
      setCurrentPage((prev) => prev - 1);
    }
  }

  function handleEdit(id) {
    console.log(id);
    setEditingCourseId(id);
    setShowModal(true);
  }

  const toggleModal = () => {
    setShowModal(!showModal);
  };

  // Fetch all courses
  const fetchCourses = () => {
    axios
      .get("http://localhost:8080/api/courses", {
        headers: {
          Authorization: `Bearer ${auth.accessToken}`,
        },
      })
      .then((response) => {
        console.log(response.data);
        setCourses(response.data);
      })
      .catch((error) => console.error(`Error: ${error}`));
  };

  // Fetch all departments
  const fetchDepartments = () => {
    axios
      .get("http://localhost:8080/api/departments", {
        headers: {
          Authorization: `Bearer ${auth.accessToken}`,
        },
      })
      .then((response) => {
        console.log(response.data);

        setDepartments(response.data);
        if (response.data.length > 0) {
          setSelectedDeptName(response.data[0].deptName);
        }
      })
      .catch((error) => console.error(`Error: ${error}`));
  };

  // Fetch all programmes
  const fetchProgrammes = () => {
    axios
      .get("http://localhost:8080/api/programs", {
        headers: {
          Authorization: `Bearer ${auth.accessToken}`,
        },
      })
      .then((response) => {
        console.log(response.data);

        setProgrammes(response.data);
        if (response.data.length > 0) {
          setSelectedProgrammeName(response.data[0].programmeName);
        }
      })
      .catch((error) => console.error(`Error: ${error}`));
  };

  // Fetch all instructors
  const fetchInstructors = () => {
    axios
      .get("http://localhost:8080/api/instructors", {
        headers: {
          Authorization: `Bearer ${auth.accessToken}`,
        },
      })
      .then((response) => {
        setInstructors(response.data);
        if (response.data.length > 0) {
          setSelectedInstructorName(response.data[0].firstName);
        }
      })
      .catch((error) => console.error(`Error: ${error}`));
  };

  // Add a new course
  const addCourse = (e) => {
    e.preventDefault(); // Prevent default form submission behavior

    const newCourse = {
      courseCode: courseCode,
      courseName: courseName,
      year: parseInt(year, 10),
      semester: parseInt(semester, 10),
      programmeName: selectedProgrammeName,
      deptName: selectedDeptName,
      instructorName: selectedInstructorName,
    };

    axios
      .post("http://localhost:8080/api/courses", newCourse, {
        headers: {
          Authorization: `Bearer ${auth.accessToken}`,
        },
      })
      .then((response) => {
        alert("Course added");
        fetchCourses();
      })
      .catch((error) => console.error(`Error: ${error}`));
  };

  const handleDelete = (id) => {
    axios
      .delete(`http://localhost:8080/api/courses/${id}`, {
        headers: {
          Authorization: `Bearer ${auth.accessToken}`,
        },
      })
      .then((response) => {
        // Handle successful delete
        console.log(response.status);
      })
      .catch((error) => console.error(`Error: ${error}`));
    // redisplay table
    fetchCourses();
  };

  useEffect(() => {
    fetchCourses();
    fetchDepartments();
    fetchProgrammes();
    fetchInstructors();
  }, []);

  return (
    <DashboardLayout>
      <main>
        <h1 className="font-bold text-3xl">Courses</h1>

        <div className="main-content">
          <div className="courses-cards">
            <div className="stat">
              <div className="stat-title">Total Departments</div>
              <div className="stat-value">{departments.length}</div>
            </div>
            <div className="stat"></div>
          </div>
        </div>

        <section id="courses-table" className="w-4/5 mx-auto">
          <div className="flex items-center justify-between py-4">
            <h2 className="text-lg font-semibold mb-4">Existing Courses</h2>
            <div className="add-course ">
              <label htmlFor="my-modal-4" className="btn">
                add course
              </label>

              {/* Put this part before </body> tag */}
              <input type="checkbox" id="my-modal-4" className="modal-toggle" />
              <label htmlFor="my-modal-4" className="modal cursor-pointer">
                <label className="modal-box relative" htmlFor="">
                  <form>
                    <div className="flex justify-between items-center mt-4">
                      <label htmlFor="course_code" className="label ml-8">
                        Course Code
                      </label>
                      <input
                        className="input input-bordered w-full max-w-[60%]"
                        type="text"
                        id="course_code"
                        value={courseCode}
                        onChange={(e) => setCourseCode(e.target.value)}
                      />
                    </div>

                    <div className="flex justify-between items-center mt-4">
                      <label htmlFor="course_name" className="label ml-8">
                        Course Name
                      </label>
                      <input
                        className="input input-bordered w-full max-w-[60%]"
                        type="text"
                        id="course_name"
                        value={courseName}
                        onChange={(e) => setCourseName(e.target.value)}
                      />
                    </div>

                    <div className="flex justify-between items-center mt-4">
                      <label htmlFor="year" className="label ml-8">
                        Year
                      </label>
                      <input
                        className="input input-bordered w-full max-w-[60%]"
                        type="number"
                        id="year"
                        value={year}
                        onChange={(e) => setYear(e.target.value)}
                      />
                    </div>

                    <div className="flex justify-between items-center mt-4">
                      <label htmlFor="semester" className="label ml-8">
                        Semester
                      </label>
                      <input
                        className="input input-bordered w-full max-w-[60%]"
                        type="number"
                        id="semester"
                        value={semester}
                        onChange={(e) => setSemester(e.target.value)}
                      />
                    </div>

                    <div className="flex justify-between items-center mt-4">
                      <label htmlFor="programme" className="label ml-8">
                        Programme
                      </label>
                      <select
                        className="select select-info w-full max-w-[60%]"
                        as="select"
                        id="programme"
                        value={selectedProgrammeName}
                        onChange={(e) =>
                          setSelectedProgrammeName(e.target.value)
                        }
                      >
                        {programmes.map((programme) => (
                          <option
                            key={programme.id}
                            value={programme.programmeName}
                          >
                            {programme.programmeName}
                          </option>
                        ))}
                      </select>
                    </div>

                    <div className="flex justify-between items-center mt-4">
                      <label htmlFor="department" className="label ml-8">
                        Department
                      </label>
                      <select
                        className="select select-info w-full max-w-[60%]"
                        as="select"
                        id="department"
                        value={selectedDeptName}
                        onChange={(e) => setSelectedDeptName(e.target.value)}
                      >
                        {departments.map((department) => (
                          <option
                            key={department.id}
                            value={department.deptName}
                          >
                            {department.deptName}
                          </option>
                        ))}
                      </select>
                    </div>

                    <div className="flex justify-between items-center mt-4">
                      <label htmlFor="instructor" className="label ml-8">
                        Instructor
                      </label>
                      <select
                        className="select select-info w-full max-w-[60%]"
                        as="select"
                        id="instructor"
                        value={selectedInstructorName}
                        onChange={(e) =>
                          setSelectedInstructorName(e.target.value)
                        }
                      >
                        {instructors.map((instructor) => (
                          <option
                            key={instructor.id}
                            value={instructor.firstName}
                          >
                            {instructor.firstName}
                          </option>
                        ))}
                      </select>
                    </div>

                    <div className="flex justify-end my-4">
                      <button className="btn btn-primary" onClick={addCourse}>
                        Add Course
                      </button>
                    </div>
                  </form>
                </label>
              </label>
            </div>
          </div>

          <CourseTable
            courses={currentCourses}
            handleDelete={handleDelete}
            handleEdit={handleEdit}
          />

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
        </section>
      </main>
    </DashboardLayout>
  );
};

const CourseTable = ({ courses, handleDelete }) => {
  const [departments, setDepartments] = useState([]);
  const [programmes, setProgrammes] = useState([]);
  const [instructors, setInstructors] = useState([]);
  const [courseCode, setCourseCode] = useState("");
  const [courseName, setCourseName] = useState("");
  const [year, setYear] = useState("");
  const [semester, setSemester] = useState("");
  const [selectedProgrammeName, setSelectedProgrammeName] = useState("");
  const [selectedDeptName, setSelectedDeptName] = useState("");
  const [selectedInstructorName, setSelectedInstructorName] = useState("");

  const { auth } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    // Fetch departments
    axios
      .get("http://localhost:8080/api/departments", {
        headers: {
          Authorization: `Bearer ${auth.accessToken}`,
        },
      })
      .then((response) => {
        setDepartments(response.data);
      })
      .catch((error) => console.error(`Error: ${error}`));

    // Fetch programmes
    axios
      .get("http://localhost:8080/api/programs", {
        headers: {
          Authorization: `Bearer ${auth.accessToken}`,
        },
      })
      .then((response) => {
        setProgrammes(response.data);
      })
      .catch((error) => console.error(`Error: ${error}`));

    // Fetch instructors
    axios
      .get("http://localhost:8080/api/instructors", {
        headers: {
          Authorization: `Bearer ${auth.accessToken}`,
        },
      })
      .then((response) => {
        setInstructors(response.data);
      })
      .catch((error) => console.error(`Error: ${error}`));
  }, []);

  const handleUpdate = (id) => {
    console.log(id);
    const updatedCourse = {
      courseCode: courseCode,
      courseName: courseName,
      year: parseInt(year, 10),
      semester: parseInt(semester, 10),
      programmeName: selectedProgrammeName,
      deptName: selectedDeptName,
      instructorName: selectedInstructorName,
    };

    axios
      .put(`http://localhost:8080/api/courses/${id}`, updatedCourse, {
        headers: {
          Authorization: `Bearer ${auth.accessToken}`,
        },
      })
      .then((response) => {
        // fetchCourses();
        console.log(response.data);
      })
      .catch((error) => console.error(`Error: ${error}`));

    // toggleModal();
    // fetchCourses();
  };

  return (
    <div className="relative overflow-x-auto shadow-md sm:rounded-lg">
      <table className="w-full text-sm text-left text-gray-500">
        <thead className="text-xs text-gray-700 uppercase bg-gray-50  ">
          <tr>
            <th scope="col" className="px-2 py-3">
              Course Name
            </th>
            <th scope="col" className="px-2 py-3">
              Year
            </th>
            <th scope="col" className="px-4 py-3">
              Semester
            </th>
            <th scope="col" className="px-6 py-3">
              Programme Name
            </th>
            <th scope="col" className="px-8 py-3">
              Department Name
            </th>
            <th scope="col" className="px-6 py-3">
              Instructor Name
            </th>
            <th scope="col" className="px-2 py-3">
              Action
            </th>
          </tr>
        </thead>
        <tbody>
          {courses.map((course, index) => (
            <tr
              key={course.index}
              className={`${
                course.available ? "bg-white" : "bg-gray-50"
              } border-b `}
            >
              <th
                scope="row"
                className="px-6 py-4 font-medium text-gray-900 whitespace "
              >
                {course.courseName} ({course.courseCode})
              </th>
              <td className="px-6 py-4">{course.year}</td>
              <td className="px-6 py-4">{course.semester}</td>
              <td className="px-6 py-4">{course.programmeName}</td>
              <td className="px-6 py-4">{course.deptName}</td>
              <td className="px-6 py-4">{course.instructorName}</td>
              <td className="px-6 py-4">
                {/* Provide Edit/Delete functionality here */}
                <div className="flex items-center ">
                  {/* The button to open modal */}
                  <label htmlFor="my-modal" className="btn btn-warning">
                    Delete
                  </label>

                  {/* Put this part before </body> tag */}
                  <input
                    type="checkbox"
                    id="my-modal"
                    className="modal-toggle"
                  />
                  <div className="modal">
                    <div className="modal-box">
                      <h3 className="font-bold text-lg">
                        Confirm delete of the instructor?
                      </h3>
                      <p className="py-4">
                        Do you want to delete the instructor, you can cancel.
                      </p>
                      <div className="modal-action">
                        <label htmlFor="my-modal" className="btn btn-success">
                          Cancel
                        </label>
                        <label
                          htmlFor="my-modal"
                          className="btn btn-secondary"
                          onClick={() => handleDelete(course.id)}
                        >
                          Delete
                        </label>
                      </div>
                    </div>
                  </div>
                  <div>
                    <label
                      htmlFor="my-modal-2"
                      className="text-purple-600 hover:text-purple-400 font-semibold ml-6"
                    >
                      Edit
                    </label>
                    {/* Put this part before </body> tag */}
                    <input
                      type="checkbox"
                      id="my-modal-2"
                      className="modal-toggle"
                    />
                    <div className="modal">
                      <div className="modal-box">
                        <h3 className="font-bold text-lg">
                          Update the instructor
                        </h3>
                        <form>
                          <div className="flex justify-between items-center mt-4">
                            <label htmlFor="course_code" className="label ml-8">
                              Course Code
                            </label>
                            <input
                              className="input input-bordered w-full max-w-[60%]"
                              type="text"
                              id="course_code"
                              value={courseCode}
                              onChange={(e) => setCourseCode(e.target.value)}
                            />
                          </div>

                          <div className="flex justify-between items-center mt-4">
                            <label htmlFor="course_name" className="label ml-8">
                              Course Name
                            </label>
                            <input
                              className="input input-bordered w-full max-w-[60%]"
                              type="text"
                              id="course_name"
                              value={courseName}
                              onChange={(e) => setCourseName(e.target.value)}
                            />
                          </div>

                          <div className="flex justify-between items-center mt-4">
                            <label htmlFor="year" className="label ml-8">
                              Year
                            </label>
                            <input
                              className="input input-bordered w-full max-w-[60%]"
                              type="number"
                              min={1}
                              max={6}
                              id="year"
                              value={year}
                              onChange={(e) => setYear(e.target.value)}
                            />
                          </div>

                          <div className="flex justify-between items-center mt-4">
                            <label htmlFor="semester" className="label ml-8">
                              Semester
                            </label>
                            <input
                              className="input input-bordered w-full max-w-[60%]"
                              type="number"
                              min={1}
                              max={2}
                              id="semester"
                              value={semester}
                              onChange={(e) => setSemester(e.target.value)}
                            />
                          </div>

                          <div className="flex justify-between items-center mt-4">
                            <label htmlFor="programme" className="label ml-8">
                              Programme
                            </label>
                            <select
                              className="select select-info w-full max-w-[60%]"
                              as="select"
                              id="programme"
                              value={selectedProgrammeName}
                              onChange={(e) =>
                                setSelectedProgrammeName(e.target.value)
                              }
                            >
                              {programmes.map((programme) => (
                                <option
                                  key={programme.id}
                                  value={programme.programmeName}
                                >
                                  {programme.programmeName}
                                </option>
                              ))}
                            </select>
                          </div>

                          <div className="flex justify-between items-center mt-4">
                            <label htmlFor="department" className="label ml-8">
                              Department
                            </label>
                            <select
                              className="select select-info w-full max-w-[60%]"
                              as="select"
                              id="department"
                              value={selectedDeptName}
                              onChange={(e) =>
                                setSelectedDeptName(e.target.value)
                              }
                            >
                              {departments.map((department) => (
                                <option
                                  key={department.id}
                                  value={department.deptName}
                                >
                                  {department.deptName}
                                </option>
                              ))}
                            </select>
                          </div>

                          <div className="flex justify-between items-center mt-4">
                            <label htmlFor="instructor" className="label ml-8">
                              Instructor
                            </label>
                            <select
                              className="select select-info w-full max-w-[60%]"
                              as="select"
                              id="instructor"
                              value={selectedInstructorName}
                              onChange={(e) =>
                                setSelectedInstructorName(e.target.value)
                              }
                            >
                              {instructors.map((instructor) => (
                                <option
                                  key={instructor.id}
                                  value={instructor.firstName}
                                >
                                  {instructor.firstName}
                                </option>
                              ))}
                            </select>
                          </div>
                          <div className="modal-action">
                            <label
                              htmlFor="my-modal-2"
                              className="btn btn-success"
                            >
                              Cancel
                            </label>
                            <label
                              htmlFor="my-modal-2"
                              className="btn btn-primary"
                              onClick={() => handleUpdate(course.id)}
                            >
                              Update
                            </label>
                          </div>
                        </form>
                      </div>
                    </div>
                    <section></section>
                  </div>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default Course;

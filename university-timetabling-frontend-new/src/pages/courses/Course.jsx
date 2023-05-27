import React, { useState, useEffect } from "react";
import axios from "axios";
import { Button, Form, Container } from "react-bootstrap";
import Layout from "../Layout";
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

  // Fetch all courses
  const fetchCourses = () => {
    axios
      .get("http://localhost:8080/api/courses")
      .then((response) => {
        console.log(response.data);
        setCourses(response.data);
      })
      .catch((error) => console.error(`Error: ${error}`));
  };

  // Fetch all departments
  const fetchDepartments = () => {
    axios
      .get("http://localhost:8080/api/departments")
      .then((response) => {
        console.log(response.data);

        setDepartments(response.data);
      })
      .catch((error) => console.error(`Error: ${error}`));
  };

  // Fetch all programmes
  const fetchProgrammes = () => {
    axios
      .get("http://localhost:8080/api/programs")
      .then((response) => {
        console.log(response.data);

        setProgrammes(response.data);
      })
      .catch((error) => console.error(`Error: ${error}`));
  };

  // Fetch all instructors
  const fetchInstructors = () => {
    axios
      .get("http://localhost:8080/api/instructors")
      .then((response) => {
        setInstructors(response.data);
      })
      .catch((error) => console.error(`Error: ${error}`));
  };

  // Add a new course
  const addCourse = () => {
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
      .post("http://localhost:8080/api/courses", newCourse)
      .then((response) => {
        alert("Course added");
        fetchCourses();
      })
      .catch((error) => console.error(`Error: ${error}`));
  };

  useEffect(() => {
    fetchCourses();
    fetchDepartments();
    fetchProgrammes();
    fetchInstructors();
  }, []);

  return (
    <Layout>
      <main>
        <h1 className="font-bold text-3xl">Courses</h1>

        <section id="courses-table" className="w-4/5">
          <div className="flex items-center justify-between">
            <h2>Existing Courses</h2>
            <div className="add-instructor ">
              <label htmlFor="my-modal-4" className="btn">
                add instructor
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

          <CourseTable courses={courses} />
        </section>
      </main>
    </Layout>
  );
};

const CourseTable = ({ courses }) => {
  return (
    <div className="relative overflow-x-auto shadow-md sm:rounded-lg">
      <table className="w-full text-sm text-left text-gray-500">
        <thead className="text-xs text-gray-700 uppercase bg-gray-50  ">
          <tr>
            <th scope="col" className="px-6 py-3">
              Course Name
            </th>
            <th scope="col" className="px-6 py-3">
              Year
            </th>
            <th scope="col" className="px-6 py-3">
              Semester
            </th>
            <th scope="col" className="px-6 py-3">
              Programme Name
            </th>
            <th scope="col" className="px-6 py-3">
              Department Name
            </th>
            <th scope="col" className="px-6 py-3">
              Instructor Name
            </th>
            <th scope="col" className="px-6 py-3">
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
                className="px-6 py-4 font-medium text-gray-900 whitespace-nowrap "
              >
                {course.courseName} ({course.courseCode})
              </th>
              <td className="px-6 py-4">{course.year}</td>
              <td className="px-6 py-4">{course.semester}</td>
              <td className="px-6 py-4">{course.programmeName}</td>
              <td className="px-6 py-4">{course.deptName}</td>
              <td className="px-6 py-4">{course.instructorName}</td>
              <td className="px-6 py-4">
                <a
                  href="#"
                  className="font-medium text-blue-600 dark:text-blue-500 hover:underline"
                >
                  Edit
                </a>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default Course;

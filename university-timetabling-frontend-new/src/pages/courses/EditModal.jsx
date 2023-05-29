import React, { useState, useEffect } from "react";
import axios from "axios";

export default function EditModal({ courseId, toggleModal, fetchCourses }) {
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

  useEffect(() => {
    // Fetch departments
    axios
      .get("http://localhost:8080/api/departments")
      .then((response) => {
        setDepartments(response.data);
      })
      .catch((error) => console.error(`Error: ${error}`));

    // Fetch programmes
    axios
      .get("http://localhost:8080/api/programs")
      .then((response) => {
        setProgrammes(response.data);
      })
      .catch((error) => console.error(`Error: ${error}`));

    // Fetch instructors
    axios
      .get("http://localhost:8080/api/instructors")
      .then((response) => {
        setInstructors(response.data);
      })
      .catch((error) => console.error(`Error: ${error}`));
  }, []);

  const handleUpdate = () => {
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
      .put(`http://localhost:8080/api/courses/${courseId}`, updatedCourse)
      .then((response) => {
        toggleModal();
        fetchCourses();
      })
      .catch((error) => console.error(`Error: ${error}`));

    toggleModal();
    fetchCourses();
  };

  return (
    <section>
      {/* The button to open modal */}
      <label htmlFor="my-modal-3" className="btn">
        open modal
      </label>

      {/* Put this part before </body> tag */}
      <input type="checkbox" id="my-modal-3" className="modal-toggle" />
      <div className="modal">
        <div className="modal-box relative">
          <label
            htmlFor="my-modal-3"
            className="btn btn-sm btn-circle absolute right-2 top-2"
          >
            ✕
          </label>
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
                onChange={(e) => setSelectedProgrammeName(e.target.value)}
              >
                {programmes.map((programme) => (
                  <option key={programme.id} value={programme.programmeName}>
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
                  <option key={department.id} value={department.deptName}>
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
                onChange={(e) => setSelectedInstructorName(e.target.value)}
              >
                {instructors.map((instructor) => (
                  <option key={instructor.id} value={instructor.firstName}>
                    {instructor.firstName}
                  </option>
                ))}
              </select>
            </div>

            <div className="flex justify-end my-4">
              <button className="btn btn-primary" onClick={handleUpdate}>
                Update Course
              </button>
            </div>
          </form>
        </div>
      </div>
    </section>
  );
}

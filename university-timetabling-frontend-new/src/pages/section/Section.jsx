import React, { useState, useEffect } from "react";
import axios from "axios";
import { Button, Form, Container } from "react-bootstrap";
import Layout from "../Layout";

const Section = () => {
  const [sections, setSections] = useState([]);
  const [courses, setCourses] = useState([]);
  const [numberOfClasses, setNumberOfClasses] = useState("");
  const [selectedCourseName, setSelectedCourseName] = useState("");

  // Fetch all sections
  const fetchSections = () => {
    axios
      .get("http://localhost:8080/api/sections")
      .then((response) => {
        setSections(response.data);
      })
      .catch((error) => console.error(`Error: ${error}`));
  };

  // Fetch all courses
  const fetchCourses = () => {
    axios
      .get("http://localhost:8080/api/courses")
      .then((response) => {
        setCourses(response.data);
      })
      .catch((error) => console.error(`Error: ${error}`));
  };

  // Add a new section
  const addSection = () => {
    const newSection = {
      numberOfClasses: numberOfClasses,
      courseName: selectedCourseName,
    };

    axios
      .post("http://localhost:8080/api/sections", newSection)
      .then((response) => {
        alert("Section added");
        fetchSections();
      })
      .catch((error) => console.error(`Error: ${error}`));
  };

  useEffect(() => {
    fetchSections();
    fetchCourses();
  }, []);

  return (
    <Layout>
      <main>
        <h1 className="text-3xl font-bold">Sections</h1>

        <section className="w-4/5 mx-auto">
          <div className="flex items-center justify-between">
            <h2>Existing Sections</h2>
            <div className="add-section">
              <label htmlFor="my-modal-4" className="btn">
                add sections
              </label>

              {/* Put this part before </body> tag */}
              <input type="checkbox" id="my-modal-4" className="modal-toggle" />
              <label htmlFor="my-modal-4" className="modal cursor-pointer">
                <label className="modal-box relative" htmlFor="">
                  <form
                    onSubmit={(e) => {
                      e.preventDefault();
                      addSection();
                    }}
                  >
                    <div className="flex justify-between items-center mt-4">
                      <label className="label">Number of Classes</label>
                      <input
                        className="input input-bordered w-full max-w-[50%]"
                        type="number"
                        min={1}
                        value={numberOfClasses}
                        onChange={(e) => setNumberOfClasses(e.target.value)}
                      />
                    </div>
                    <div className="flex justify-between items-center mt-4">
                      <label className="label">Course</label>
                      <select
                        className="input input-bordered w-full max-w-[50%]"
                        as="select"
                        value={selectedCourseName}
                        onChange={(e) => setSelectedCourseName(e.target.value)}
                      >
                        {courses.map((course) => (
                          <option key={course.id} value={course.courseName}>
                            {course.courseName}
                          </option>
                        ))}
                      </select>
                    </div>
                    <button className="btn btn-primary" type="submit">
                      Add Section
                    </button>
                  </form>
                </label>
              </label>
            </div>
          </div>
          <SectionTable sections={sections} />
        </section>
      </main>
    </Layout>
  );
};

const SectionTable = ({ sections }) => {
  return (
    <div className="relative overflow-x-auto shadow-md sm:rounded-lg">
      <table className="w-full text-sm text-left text-gray-500">
        <thead className="text-xs text-gray-700 uppercase bg-gray-50  ">
          <tr>
            <th scope="col" className="px-6 py-3">
              Section ID
            </th>
            <th scope="col" className="px-6 py-3">
              Number of Classes
            </th>
            <th scope="col" className="px-6 py-3">
              Course
            </th>
          </tr>
        </thead>
        <tbody>
          {sections.map((section, index) => (
            <tr key={section.index}>
              <td className="px-6 py-4">{section.numberOfClasses}</td>
              <td className="px-6 py-4">{section.courseName}</td>
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

export default Section;

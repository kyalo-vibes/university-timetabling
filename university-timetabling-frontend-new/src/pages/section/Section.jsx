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
          {sections.map((section, index) => (
            <div key={index}>
              <h3>Section ID: {section.id}</h3>
              <p>Number of Classes: {section.numberOfClasses}</p>
              <p>Course: {section.courseName}</p>
              {/* Provide Edit/Delete functionality here */}
            </div>
          ))}
        </section>
      </main>
    </Layout>
  );
};

export default Section;

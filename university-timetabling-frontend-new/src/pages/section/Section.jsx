import React, { useState, useEffect } from "react";
import axios from "axios";
import Layout from "../../Layout/DashboardLayout";
import AuthContext from "../../context/AuthProvider";
import { useNavigate } from "react-router-dom";
import useAuth from "../../hooks/useAuth";

const Section = () => {
  const [sections, setSections] = useState([]);
  const [courses, setCourses] = useState([]);
  const [numberOfClasses, setNumberOfClasses] = useState("");
  const [selectedCourseName, setSelectedCourseName] = useState("");

  const { auth } = useAuth();
  const navigate = useNavigate();

  // Fetch all sections
  const fetchSections = () => {
    axios
      .get("http://localhost:8080/api/sections", {
        headers: {
          Authorization: `Bearer ${auth.accessToken}`,
        },
      })
      .then((response) => {
        setSections(response.data);
      })
      .catch((error) => console.error(`Error: ${error}`));
  };

  // Fetch all courses
  const fetchCourses = () => {
    axios
      .get("http://localhost:8080/api/courses/", {
        headers: {
          Authorization: `Bearer ${auth.accessToken}`,
        },
      })
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
      .post("http://localhost:8080/api/sections", newSection, {
        headers: {
          Authorization: `Bearer ${auth.accessToken}`,
        },
      })
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
  const [courses, setCourses] = useState([]);
  const [numberOfClasses, setNumberOfClasses] = useState("");
  const [selectedCourseName, setSelectedCourseName] = useState("");

  const { auth } = useAuth();
  const navigate = useNavigate();

  // Fetch all courses
  const fetchCourses = () => {
    axios
      .get("http://localhost:8080/api/courses/", {
        headers: {
          Authorization: `Bearer ${auth.accessToken}`,
        },
      })
      .then((response) => {
        setCourses(response.data);
      })
      .catch((error) => console.error(`Error: ${error}`));
  };
  useEffect(() => {
    fetchCourses();
  }, []);

  const handleSectionUpdate = (id) => {
    console.log(id);
    const updatedCourse = {
      id: id,
      numberOfClasses: numberOfClasses,
      courseName: selectedCourseName,
    };

    console.log(sections.id);

    axios
      .put(`http://localhost:8080/api/sections/${id}`, updatedCourse, {
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
            <th scope="col" className="px-6 py-3">
<<<<<<< Updated upstream
              Number of Classes
||||||| Stash base
              Section ID
=======
            Number of Classes
>>>>>>> Stashed changes
            </th>
            <th scope="col" className="px-6 py-3">
<<<<<<< Updated upstream
              Course Name
||||||| Stash base
              Number of Classes
=======
            Course
>>>>>>> Stashed changes
            </th>
            <th scope="col" className="px-6 py-3">
            Action  
            </th>
          </tr>
        </thead>
        <tbody>
          {sections.map((section, index) => (
            <tr key={section.index}>
              <td className="px-6 py-4">{section.numberOfClasses}</td>
              <td className="px-6 py-4">{section.courseName}</td>
              <td className="px-6 py-4">
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
                      <h3 className="font-bold text-lg">Update the section</h3>
                      <form>
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
                            onChange={(e) =>
                              setSelectedCourseName(e.target.value)
                            }
                          >
                            {sections.map((item) => (
                              <option key={item.id} value={item.courseName}>
                                {item.courseName}
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
                            onClick={() =>
                              handleSectionUpdate(section.section_id)
                            }
                            htmlFor="my-modal-2"
                            className="btn btn-primary"
                          >
                            Update
                          </label>
                        </div>
                      </form>
                    </div>
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

export default Section;

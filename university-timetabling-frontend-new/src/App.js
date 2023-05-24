import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Home from "./components/Home";
import Instructor from "./pages/instructors/Instructor";
import Room from "./pages/rooms/Room";
import TimeSlot from "./components/TimeSlot";
import Programme from "./components/Programme";
import Course from "./pages/courses/Course";
import Faculty from "./components/Faculty";
import Department from "./components/Department";
import Section from "./components/Section";
import InstructorPreference from "./pages/instructors/InstructorPreference";
import InstructorTimetable from "./pages/instructors/InstructorTimetable";

function App() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/instructor" element={<Instructor />} />
      <Route path="/room" element={<Room />} />
      <Route path="/timeslot" element={<TimeSlot />} />
      <Route path="/programme" element={<Programme />} />
      <Route path="/course" element={<Course />} />
      <Route path="/faculty" element={<Faculty />} />
      <Route path="/department" element={<Department />} />
      <Route path="/section" element={<Section />} />
      <Route path="/instructor-preference" element={<InstructorPreference />} />
      <Route path="/instructor-timetable" element={<InstructorTimetable />} />
    </Routes>
  );
}

export default App;

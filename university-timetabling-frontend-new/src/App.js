import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Home from './components/Home';
import Instructor from './components/Instructor';
import Room from './components/Room';
import TimeSlot from './components/TimeSlot';
import Programme from './components/Programme';
import Course from './components/Course';
import Faculty from './components/Faculty';
import Department from './components/Department';
import Section from './components/Section';
import InstructorPreference from './components/InstructorPreference';
import InstructorTimetable from './components/InstructorTimetable';

function App() {
  return (
    <Router>
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
    </Router>
  );
}

export default App;

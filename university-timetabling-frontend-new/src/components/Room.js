import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Button, Form, Container } from 'react-bootstrap';

const Room = () => {
  const [rooms, setRooms] = useState([]);
  const [departments, setDepartments] = useState([]);
  const [roomName, setRoomName] = useState('');
  const [roomCapacity, setRoomCapacity] = useState('');
  const [roomType, setRoomType] = useState('');
  const [isAvailable, setIsAvailable] = useState(true);
  const [selectedDeptName, setSelectedDeptName] = useState('');

  const fetchRooms = () => {
    axios.get('http://localhost:8080/api/rooms')
    .then(response => {
      setRooms(response.data);
    })
    .catch(error => console.error(`Error: ${error}`));
  }

  const fetchDepartments = () => {
    axios.get('http://localhost:8080/api/departments')
    .then(response => {
      setDepartments(response.data);
    })
    .catch(error => console.error(`Error: ${error}`));
  }

  const addRoom = () => {
    const newRoom = {
      roomName: roomName,
      roomCapacity: roomCapacity,
      roomType: roomType,
      isAvailable: isAvailable,
      deptName: selectedDeptName
    };

    axios.post('http://localhost:8080/api/rooms', newRoom)
    .then(response => {
      alert('Room added');
      fetchRooms();
    })
    .catch(error => console.error(`Error: ${error}`));
  }

  useEffect(() => {
    fetchRooms();
    fetchDepartments();
  }, []);

  return (
    <Container>
      <h1>Rooms</h1>

      <Form onSubmit={(e) => {e.preventDefault(); addRoom();}}>
        <Form.Group>
          <Form.Label>Room Name</Form.Label>
          <Form.Control type="text" value={roomName} onChange={(e) => setRoomName(e.target.value)} />
        </Form.Group>
        <Form.Group>
          <Form.Label>Room Capacity</Form.Label>
          <Form.Control type="number" value={roomCapacity} onChange={(e) => setRoomCapacity(e.target.value)} />
        </Form.Group>
        <Form.Group>
          <Form.Label>Room Type</Form.Label>
          <Form.Control type="text" value={roomType} onChange={(e) => setRoomType(e.target.value)} />
        </Form.Group>
        <Form.Group>
          <Form.Check type="checkbox" label="Available" checked={isAvailable} onChange={(e) => setIsAvailable(e.target.checked)} />
        </Form.Group>
        <Form.Group>
          <Form.Label>Department</Form.Label>
          <Form.Control as="select" value={selectedDeptName} onChange={(e) => setSelectedDeptName(e.target.value)}>
            {departments.map(department => <option key={department.id} value={department.deptName}>{department.deptName}</option>)}
          </Form.Control>
        </Form.Group>
        <Button type="submit">Add Room</Button>
      </Form>

      <h2>Existing Rooms</h2>
      {
    rooms.map((room, index) => {
        console.log(room);
        return (
            <div key={index}>
                <h3>{room.roomName}</h3>
                <p>Capacity: {room.roomCapacity}</p>
                <p>Type: {room.roomType}</p>
                <p>Available: {room.isAvailable ? 'Yes' : 'No'}</p>
                <p>Department Name: {room.deptName}</p>
                {/* Provide Edit/Delete functionality here */}
            </div>
        );
    })
}

    </Container>
  );
}

export default Room;

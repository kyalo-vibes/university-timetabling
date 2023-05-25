import React, { useState, useEffect } from "react";
import axios from "axios";
import { Button, Form, Container } from "react-bootstrap";
import Layout from "../Layout";

const Room = () => {
  const [rooms, setRooms] = useState([]);
  const [departments, setDepartments] = useState([]);
  const [roomName, setRoomName] = useState("");
  const [roomCapacity, setRoomCapacity] = useState("");
  const [roomType, setRoomType] = useState("");
  const [isAvailable, setIsAvailable] = useState(true);
  const [selectedDeptName, setSelectedDeptName] = useState("");

  const fetchRooms = () => {
    axios
      .get("http://localhost:8080/api/rooms")
      .then((response) => {
        setRooms(response.data);
      })
      .catch((error) => console.error(`Error: ${error}`));
  };

  const fetchDepartments = () => {
    axios
      .get("http://localhost:8080/api/departments")
      .then((response) => {
        setDepartments(response.data);
      })
      .catch((error) => console.error(`Error: ${error}`));
  };

  const addRoom = () => {
    const newRoom = {
      roomName: roomName,
      roomCapacity: roomCapacity,
      roomType: roomType,
      isAvailable: isAvailable,
      deptName: selectedDeptName,
    };

    axios
      .post("http://localhost:8080/api/rooms", newRoom)
      .then((response) => {
        alert("Room added");
        fetchRooms();
      })
      .catch((error) => console.error(`Error: ${error}`));
  };

  useEffect(() => {
    fetchRooms();
    fetchDepartments();
  }, []);

  function handleAddRoom() {}

  return (
    <Layout>
      <main>
        <h1 className="text-3xl font-bold">Rooms</h1>

        <div className="view-rooms">
          <div className="flex items-center justify-between">
            <h2>Existing Rooms</h2>

            <div className="add-room">
              {/* The button to open modal */}
              <label htmlFor="my-modal-5" className="btn">
                Add Room
              </label>

              {/* Put this part before </body> tag */}
              <input type="checkbox" id="my-modal-5" className="modal-toggle" />
              <div className="modal">
                <div className="modal-box w-11/12 max-w-5xl">
                  <form
                    onSubmit={(e) => {
                      e.preventDefault();
                      addRoom();
                    }}
                  >
                    <div>
                      <label className="label">Room Name</label>
                      <input
                        className="input input-bordered w-full max-w-[18%]"
                        type="text"
                        value={roomName}
                        onChange={(e) => setRoomName(e.target.value)}
                      />
                    </div>
                    <div>
                      <label className="label">Room Capacity</label>
                      <input
                        className="input input-bordered w-full max-w-[18%]"
                        type="number"
                        value={roomCapacity}
                        onChange={(e) => setRoomCapacity(e.target.value)}
                      />
                    </div>
                    <div>
                      <label className="label">Room Type</label>
                      <select
                        className="select select-info w-full max-w-xs"
                        as="select"
                        value={roomType}
                        onChange={(e) => setRoomType(e.target.value)}
                      >
                        <option value="LT">LT</option>
                        <option value="SLT">SLT</option>
                      </select>
                    </div>
                    <div className="w-[10%]">
                      <label className="cursor-pointer label">
                        <span className="label-text">Available</span>
                        <input
                          type="checkbox"
                          checked={isAvailable}
                          onChange={(e) => setIsAvailable(e.target.checked)}
                          className="toggle toggle-accent"
                        />
                      </label>
                    </div>
                    <div>
                      <label>Department</label>
                      <select
                        className="select select-info w-full max-w-xs"
                        as="select"
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

                    <div className="modal-action">
                      <button
                        htmlFor="my-modal-5"
                        type="submit"
                        className="btn btn-primary "
                      >
                        Add Room
                      </button>
                      <label htmlFor="my-modal-5" className="btn">
                        See Rooms
                      </label>
                    </div>
                  </form>
                </div>
              </div>
            </div>
          </div>
          <RoomTable rooms={rooms} />
        </div>
      </main>
    </Layout>
  );
};

const RoomTable = ({ rooms }) => {
  return (
    <div className="relative overflow-x-auto shadow-md sm:rounded-lg">
      <table className="w-full text-sm text-left text-gray-500">
        <thead className="text-xs text-gray-700 uppercase bg-gray-50  ">
          <tr>
            <th scope="col" className="px-6 py-3">
              Room Name
            </th>
            <th scope="col" className="px-6 py-3">
              Capacity
            </th>
            <th scope="col" className="px-6 py-3">
              Type
            </th>
            <th scope="col" className="px-6 py-3">
              Department Name
            </th>
            <th scope="col" className="px-6 py-3">
              Available
            </th>
            <th scope="col" className="px-6 py-3">
              Action
            </th>
          </tr>
        </thead>
        <tbody>
          {rooms.map((room, index) => (
            <tr
              key={room.index}
              className={`${
                room.available ? "bg-white" : "bg-gray-50"
              } border-b `}
            >
              <th
                scope="row"
                className="px-6 py-4 font-medium text-gray-900 whitespace-nowrap "
              >
                {room.roomName}
              </th>
              <td className="px-6 py-4">{room.roomCapacity}</td>
              <td className="px-6 py-4">{room.roomType}</td>
              <td className="px-6 py-4">{room.deptName}</td>
              <td className="px-6 py-4">{room.available ? "Yes" : "No"}</td>
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

export default Room;

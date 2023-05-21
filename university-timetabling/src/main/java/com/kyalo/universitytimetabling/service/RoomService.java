package com.kyalo.universitytimetabling.service;

import com.kyalo.universitytimetabling.domain.Department;
import com.kyalo.universitytimetabling.domain.Room;
import com.kyalo.universitytimetabling.domain.RoomDTO;
import com.kyalo.universitytimetabling.repository.DepartmentRepository;
import com.kyalo.universitytimetabling.repository.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository, DepartmentRepository departmentRepository) {
        this.roomRepository = roomRepository;
        this.departmentRepository = departmentRepository;
    }

    public List<RoomDTO> getAllRooms() {
        return roomRepository.findAll().stream().map(room -> new RoomDTO(
                room.getId(),
                room.getRoomName(),
                room.getCapacity(),
                room.getRoomType(),
                room.isAvailable(),
                room.getDepartment().getName()
        )).collect(Collectors.toList());
    }
    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }

    public Room createRoom(RoomDTO roomDto) {
        System.out.println(roomDto.toString());

        Room room = new Room();
        room.setRoomName(roomDto.getRoomName());
        room.setCapacity(roomDto.getRoomCapacity());
        room.setRoomType(roomDto.getRoomType());
        room.setAvailable(roomDto.isAvailable());

        Department department = departmentRepository.findByName(roomDto.getDeptName())
                .orElseThrow(() -> new IllegalArgumentException("Invalid department name:" + roomDto.getDeptName()));
        room.setDepartment(department);

        return roomRepository.save(room);
    }

    public RoomDTO updateRoom(Long id, RoomDTO roomDto) {
        Optional<Room> roomOptional = roomRepository.findById(id);
        if (roomOptional.isPresent()) {
            Room room = roomOptional.get();
            room.setRoomName(roomDto.getRoomName());
            room.setCapacity(roomDto.getRoomCapacity());
            room.setRoomType(roomDto.getRoomType());
            room.setAvailable(roomDto.isAvailable());

            Department department = departmentRepository.findByName(roomDto.getDeptName())
                    .orElseThrow(() -> new EntityNotFoundException("Department not found with name: " + roomDto.getDeptName()));

            room.setDepartment(department);

            Room updatedRoom = roomRepository.save(room);

            return new RoomDTO(
                    updatedRoom.getId(),
                    updatedRoom.getRoomName(),
                    updatedRoom.getCapacity(),
                    updatedRoom.getRoomType(),
                    updatedRoom.isAvailable(),
                    updatedRoom.getDepartment().getName()
            );
        } else {
            throw new EntityNotFoundException("Room not found with id: " + id);
        }
    }


    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }
}

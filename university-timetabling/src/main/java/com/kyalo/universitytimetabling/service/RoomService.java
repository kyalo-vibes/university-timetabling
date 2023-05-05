package com.kyalo.universitytimetabling.service;

import com.kyalo.universitytimetabling.domain.Room;
import com.kyalo.universitytimetabling.repository.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }

    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    public Room updateRoom(Room room) {
        Optional<Room> roomOptional = roomRepository.findById(room.getId());
        if (roomOptional.isPresent()) {
            return roomRepository.save(room);
        } else {
            throw new EntityNotFoundException("Room not found with id: " + room.getId());
        }
    }


    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }
}

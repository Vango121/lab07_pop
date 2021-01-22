package org.openjfx;

import org.openjfx.models.Room;

import java.util.List;

public interface UiInterface {
    void removeRoom(int id);
    List<Room> findRoom(String size,int count);
    void cancelReservation();
}

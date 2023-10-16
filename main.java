import java.util.*;

import com.Class.Room;
import com.Class.Hotel;
import com.Service.RoomManagementService;
import com.Service.DataSeedingService;

public class main {
    public static void main(String args[]) {
        // Set up data in memory
        Hotel hotel = DataSeedingService.createSeedData();
        RoomManagementService roomManagementService = new RoomManagementService(hotel);

        Scanner sc = new Scanner(System.in);
        promptCommands();
        while (sc.hasNext()) {
            try {
                int command = sc.nextInt();
                switch (command) {
                    case 1 -> {
                        Room requestedRoom = roomManagementService.requestForAvailableRoom();
                        System.out.println("Allocated room: " + requestedRoom.getRoomNumber());
                    }
                    case 2 -> {
                        System.out.println("Enter the room number that you want to check out: ");
                        String roomNumber = sc.next();
                        roomManagementService.checkOutRoom(roomNumber);
                        System.out.println("Successfully checked out room: " + roomNumber);
                    }
                    case 3 -> {
                        System.out.println("Enter the room number that you want to mark as AVAILABLE: ");
                        String roomNumber = sc.next();
                        roomManagementService.markRoomAsAvailable(roomNumber);
                        System.out.println("Room: " + roomNumber + " is now available.");
                    }
                    case 4 -> {
                        System.out.println("Enter the room number that you want to mark for repair: ");
                        String roomNumber = sc.next();
                        roomManagementService.markRoomForRepair(roomNumber);
                        System.out.println("Room: " + roomNumber + " is now under repair.");
                    }
                    case 5 -> {
                        System.out.println("Enter the room number that you want to mark as repaired: ");
                        String roomNumber = sc.next();
                        roomManagementService.markRoomAsRepaired(roomNumber);
                        System.out.println("Room: " + roomNumber + " is now vacant.");
                    }
                    case 6 -> roomManagementService.listAllAvailableRooms();
                    default -> {
                        System.out.println("No such command");
                        promptCommands();
                    }
                }
            }
            catch (Exception exception) {
                System.out.println(exception);
            }
            finally {
                promptCommands();
            }
        }
    }

    public static void promptCommands() {
        System.out.println();
        System.out.println("Enter a command: ");
        System.out.println("1. To request the nearest vacant room");
        System.out.println("2. Check out a room");
        System.out.println("3. Mark a room as cleaned ");
        System.out.println("4. Mark a room for repair ");
        System.out.println("5. Mark a room as repaired ");
        System.out.println("6. List all available rooms ");
        System.out.println();
    }
}

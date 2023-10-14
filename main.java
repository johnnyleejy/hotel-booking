import java.util.*;

import com.Class.Room;
import com.Class.Hotel;
import com.Enum.RoomStatusEnum;
import com.Service.RoomManagementService;

public class main {
    public static void main(String args[]) {
        // Set up data in memory
        Hotel hotel = createSeedData();
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
                        System.out.println("Room: " + roomNumber + " is under repair.");
                    }
                    case 5 -> roomManagementService.listAllAvailableRooms();
                    default -> System.out.println("No such command");
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
        System.out.println("5. List all available rooms ");
    }

    public static Hotel createSeedData() {
        // Initialise seed data
        Comparator<Room> distanceComparator = Comparator.comparing(Room::getDistance);
        PriorityQueue<Room> availableRooms = new PriorityQueue<>(distanceComparator);
        HashMap<String, Room> rooms = new HashMap<>();
        char[] suffixes = { 'A', 'B', 'C', 'D', 'E' };
        char[] reversedSuffixes = { 'E', 'D', 'C', 'B', 'A' };
        int distance = 1;
        // 4 storeys
        for (int i = 1; i < 5; i++) {
            // 5 rooms per storey
            for (int j = 1; j < 6; j++) {
                char suffix;
                if (i % 2 != 0) {
                    suffix = suffixes[j - 1];
                }
                else {
                    suffix = reversedSuffixes[j - 1];
                }
                Room newRoom = new Room(distance, i, suffix, RoomStatusEnum.AVAILABLE);
                availableRooms.offer(newRoom);
                rooms.put(newRoom.getRoomNumber(), newRoom);
                distance++;
            }
        }
        return new Hotel("Botique Hotel", rooms, availableRooms);
    }
}

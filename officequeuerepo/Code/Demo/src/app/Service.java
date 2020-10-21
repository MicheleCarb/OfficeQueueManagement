package app;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Service {
    LinkedList<RequestType> requestTypes = new LinkedList<>();
    List<Counter> counters = new LinkedList<>();
    DAO officeDAO;

    public Service() throws SQLException, ClassNotFoundException {
        this.officeDAO = new DAO();
    }

    public void printAllStats(Character DayWeekMonth) throws SQLException {
        //Preparing for formatted output
        String typeDate;
        switch (DayWeekMonth) {
            case 'w':
                typeDate = "Week";
            case 'm':
                typeDate = "Month";
            default:
                typeDate = "Date";
        }
        ResultSet rs = officeDAO.getAllStats(DayWeekMonth);
        System.out.printf("%20s %20s %20s\n", "Request Type", "Customers", typeDate);
        if (!rs.next()) System.out.printf("%40s\n", "No data to show");
        else {
            do {
                System.out.printf("%20s %20d %20s\n", rs.getString(1), rs.getInt(2), rs.getString(3));
            } while (rs.next());
        }
    }

    public void printAllStatsByCounter(Character DayWeekMonth) throws SQLException {
        //Preparing for formatted output
        String typeDate;
        switch (DayWeekMonth) {
            case 'w':
                typeDate = "Week";
            case 'm':
                typeDate = "Month";
            default:
                typeDate = "Date";
        }
        ResultSet rs = officeDAO.getAllStatsByCounter(DayWeekMonth);
        System.out.printf("%20s %20s %20s %20s\n", "Request Type", "Counter", "Customers", typeDate);
        if (!rs.next()) System.out.printf("%50s\n", "No data to show");
        else {
            do {
                System.out.printf("%20s %20d %20s %20s\n",
                        rs.getString(1), rs.getInt(2),
                        rs.getInt(3), rs.getString(4));
            } while (rs.next());
        }
    }

    void createDefinition(String tagName, Float averageTime) {
        requestTypes.add(new RequestType(tagName, averageTime));
    }

    void createCounter() {
        System.out.println("Available request types:");
        requestTypes.forEach(requestType -> System.out.println(requestType.getId() + " - " + requestType.getTagName() + "\n"));

        System.out.println("Enter which request types the new counter can serve, through a comma-separated list of ids: ");
        Scanner scanner = new Scanner(System.in);
        counters.add(new Counter(
                Arrays.asList(scanner.nextLine().split("[,]"))
                        .stream().map(numberAsString -> Integer.parseUnsignedInt(numberAsString))
                        .collect(Collectors.toList()))
        );
    }

    Ticket getTicket() {
        //receive from the menu the id of the selected type of request

        System.out.println("Available request types:");
        requestTypes.forEach(requestType -> System.out.println(requestType.getId() + " - " + requestType.getTagName() + "\n"));


        float Tr;

        System.out.println("Enter which request type you want:");
        Scanner scanner = new Scanner(System.in);
        Integer idRequestType = scanner.nextInt();

        float tr = requestTypes.stream()
                .filter(requestType -> requestType.getId().equals(idRequestType))
                .collect(Collectors.toList()).get(0)
                .getAverageTime();

        Integer nr = requestTypes.stream()
                .filter(requestType -> requestType.getId().equals(idRequestType))
                .collect(Collectors.toList()).get(0)
                .count();

        float sum = 0;
        for (Counter c : counters) {
            sum += (((float) 1 / c.count()) * (c.canServeRequestType(idRequestType) ? 1 : 0));
        }

        Tr = tr * ((nr / sum) + (float) 1 / 2);

        Ticket t = new Ticket(idRequestType, Tr);

        requestTypes.stream()
                .filter(requestType -> requestType.getId().equals(idRequestType))
                .collect(Collectors.toList()).get(0)
                .addTicket(t);

        return t;
    }

    //Show the waiting tickets in the digital display board
    void watchTicketsInQueues() {
        System.out.println("Request Type\t" + "People in queue");
        for (RequestType reqType : requestTypes) {
            System.out.print("- " + reqType.getTagName() + "\t" + reqType.count() + "\n");
        }
    }

    void callNextCustomer(Integer counterId) throws SQLException {
        Counter counter = counters.stream()
                .filter(cnt -> cnt.getId().equals(counterId))
                .findFirst()
                .get();

        RequestType queue = requestTypes.stream()
                .filter(requestType -> {
                    //System.out.println(counter.canServeRequestType(requestType.getId()));
                    return counter.canServeRequestType(requestType.getId());
                })
                .sorted(((requestType1, requestType2) -> {
                    if (!requestType1.count().equals(requestType2.count()))
                        return requestType2.count().compareTo(requestType1.count());
                    else
                        return requestType2.getAverageTime().compareTo(requestType1.getAverageTime());
                }))
                .findFirst()
                .get();

        Integer ticketId;
        try {
            ticketId = queue.removeTicket();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        printTurn(queue.getId(), ticketId, queue.count(), queue.getTagName());
        officeDAO.insertRequest(counterId, queue.getTagName());
    }

    void printTurn(Integer id, Integer ticketId, Integer peopleWaiting, String tagName) {
        System.out.printf("%50s\n", "Counters");
        System.out.printf("%10s - %10s - %10s - %10s\n", "Counter", "Queue Type", "Ticket #", "People in queue");
        System.out.printf("%10d %10s %10d %10d\n", id, tagName, ticketId, peopleWaiting);
    }

    //Reset all queues every morning
    void resetQueues() {
        for (RequestType rt : requestTypes) {
            rt.reset();
        }
        System.out.println("All queues are empty");
    }
}

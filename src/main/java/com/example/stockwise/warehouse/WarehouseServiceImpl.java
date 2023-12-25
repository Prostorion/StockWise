package com.example.stockwise.warehouse;

import com.example.stockwise.graph.Edge;
import com.example.stockwise.graph.Vertex;
import com.example.stockwise.graph.VertexRepository;
import com.example.stockwise.items.history.HistoryRepository;
import com.example.stockwise.rack.Rack;
import com.example.stockwise.rack.RackRepository;
import com.example.stockwise.task.order.OrderRepository;
import com.example.stockwise.task.supply.SupplyRepository;
import com.example.stockwise.user.User;
import com.example.stockwise.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final UserService userService;
    private final WarehouseRepository warehouseRepository;
    private final RackRepository rackRepository;
    private final HistoryRepository historyRepository;
    private final VertexRepository vertexRepository;
    private final OrderRepository orderRepository;
    private final SupplyRepository supplyRepository;


    @Override
    public void addWarehouse(Warehouse warehouse) throws Exception {
        User user = userService.getUser();
        validateWarehouse(user, warehouse);
        warehouseRepository.save(warehouse);
        warehouse = warehouseRepository.findByName(warehouse.getName()).orElseThrow(() -> new Exception("warehouse is not found SERIOUS EXCEPTION"));
        user.getWarehouses().add(warehouse);
        userService.updateUser(user);
    }

    @Override
    public Set<Warehouse> getUserWarehouses(User user) {
        return warehouseRepository.findAllByUserSetContainsOrderByName(user);
    }

    @Override
    @Transactional
    public void deleteWarehouseById(Long id) throws Exception {
        Warehouse warehouse = warehouseRepository.findById(id).orElseThrow(() -> new Exception("warehouse not found"));
        warehouse.getUserSet().forEach(user -> user.getWarehouses().remove(warehouse));
        historyRepository.deleteAllByWarehouseId(id);
        warehouseRepository.deleteById(id);
    }

    @Override
    public Warehouse getWarehouseById(Long id) throws Exception {
        return warehouseRepository.findById(id).orElseThrow(() -> new Exception("warehouse not found"));
    }

    @Override
    public void updateWarehouse(Long id, Warehouse newWarehouse) throws Exception {
        User user = userService.getUser();
        Warehouse oldWarehouse = getWarehouseById(id);
        if (!oldWarehouse.getName().equals(newWarehouse.getName())) {
            nameValidation(user, newWarehouse.getName());
        }
        addressValidation(newWarehouse.getAddress());
        oldWarehouse.setName(newWarehouse.getName());
        oldWarehouse.setAddress(newWarehouse.getAddress());
        warehouseRepository.save(oldWarehouse);
    }

    @Override
    @Transactional
    public void generateState(Long id, Warehouse preWarehouse) throws Exception {
        validateWarehouseDimensions(preWarehouse);
        Warehouse warehouse = setDimensionsToNewWarehouse(id, preWarehouse);
        vertexRepository.deleteAllByWarehouseId(warehouse.getId());
        warehouse.getVertices().removeAll(warehouse.getVertices());
        rackRepository.deleteByWarehouseId(id);
        orderRepository.deleteAllByWarehouseId(id);
        supplyRepository.deleteAllByWarehouseId(id);
        warehouse.getRacks().removeAll(warehouse.getRacks());
        warehouse = warehouseRepository.save(warehouse);
        boolean hasLastRight = createRacks(warehouse);
        makeEdges(warehouse, hasLastRight);
        warehouse.setHasState(true);
        warehouseRepository.save(warehouse);

    }

    private static boolean createRacks(Warehouse warehouse) {
        Long currentRackNumber = 1L;
        currentRackNumber = generateLeft(warehouse, currentRackNumber);
        currentRackNumber = generateTop(warehouse, currentRackNumber);
        currentRackNumber = generateRight(warehouse, currentRackNumber);
        return generateCenter(warehouse, currentRackNumber);

    }

    private void makeEdges(Warehouse warehouse, boolean hasLastRight) {
        addVerticalEdges(warehouse, hasLastRight);
        addHorizontalEdges(warehouse);

    }

    private static void addHorizontalEdges(Warehouse warehouse) {
        Set<Vertex> vertexSet = warehouse.getVertices();
        addTopLine(warehouse, vertexSet);
        addDownLine(warehouse, vertexSet);
    }

    private static void addDownLine(Warehouse warehouse, Set<Vertex> vertexSet) {
        Double firstY = warehouse.getHeight() - warehouse.getRackHeight() / 2.0;
        List<Vertex> currentVertexSet = new ArrayList<>(vertexSet.stream().filter(v -> v.getY().equals(firstY)).toList());
        currentVertexSet.sort(Comparator.comparing(Vertex::getX).thenComparing(Vertex::getY));
        currentVertexSet.get(0).setStart("true");
        addEdges(currentVertexSet);
    }

    private static void addTopLine(Warehouse warehouse, Set<Vertex> vertexSet) {
        Double firstY = warehouse.getRackHeight() * 3 / 2.0;
        List<Vertex> currentVertexSet = new ArrayList<>(vertexSet.stream().filter(v -> v.getY().equals(firstY)).toList());
        currentVertexSet.sort(Comparator.comparing(Vertex::getX).thenComparing(Vertex::getY));
        addEdges(currentVertexSet);
    }

    private static void addVerticalEdges(Warehouse warehouse, boolean hasLastRight) {
        Set<Vertex> vertexSet = warehouse.getVertices();
        Set<Double> xSet = vertexSet.stream().map(Vertex::getX).collect(Collectors.toSet());
        xSet.forEach(x -> {
            List<Vertex> currentVertexSet = new ArrayList<>(vertexSet.stream().filter(v -> v.getX().equals(x)).toList());
            currentVertexSet.sort(Comparator.comparing(Vertex::getY));
            addEdges(currentVertexSet);
        });
        if (warehouse.getWidth() % (warehouse.getRackHeight() * 3) != 0 && hasLastRight) {
            List<Double> lastTwoX = new ArrayList<>(xSet.stream().toList());
            Collections.sort(lastTwoX, Comparator.reverseOrder());
            if (lastTwoX.size() >= 2) {
                double x1 = lastTwoX.get(0);
                double x2 = lastTwoX.get(1);
                double x3 = lastTwoX.get(2);
                List<Vertex> secondVertexSet;
                if (vertexSet.stream().filter(v -> v.getX().equals(x2)).count() == 1) {
                    secondVertexSet = vertexSet.stream().filter(v -> v.getX().equals(x3)).sorted((v1, v2) -> v1.getY() > v2.getY() ? 1 : (v1.getY().equals(v2.getY()) ? 0 : -1)).toList();
                } else {
                    secondVertexSet = vertexSet.stream().filter(v -> v.getX().equals(x2)).sorted((v1, v2) -> v1.getY() > v2.getY() ? 1 : (v1.getY().equals(v2.getY()) ? 0 : -1)).toList();
                }
                List<Vertex> firstVertexSet = vertexSet.stream().filter(v -> v.getX().equals(x1)).sorted((v1, v2) -> v1.getY() > v2.getY() ? 1 : (v1.getY().equals(v2.getY()) ? 0 : -1)).toList();
                List<Vertex> longerList = firstVertexSet.size() > secondVertexSet.size() ? firstVertexSet : secondVertexSet;
                List<Vertex> shortestList = firstVertexSet.size() <= secondVertexSet.size() ? firstVertexSet : secondVertexSet;

                addEdges(new ArrayList<>(List.of(longerList.get(0), shortestList.get(0))));
                for (int i = 1; i < shortestList.size(); i++) {
                    addEdges(new ArrayList<>(List.of(longerList.get(i), shortestList.get(i))));
                    addEdges(new ArrayList<>(List.of(longerList.get(i), shortestList.get(i - 1))));
                }
                addEdges(new ArrayList<>(List.of(longerList.get(longerList.size() - 1), shortestList.get(shortestList.size() - 1))));
            }
        }

    }

    private static void addEdges(List<Vertex> currentVertexSet) {
        currentVertexSet.sort(Comparator.comparing(Vertex::getY).thenComparing(Vertex::getX));
        if (currentVertexSet.size() >= 2) {
            Vertex currentV = currentVertexSet.get(0);
            for (int i = 1; i < currentVertexSet.size(); i++) {
                Edge edge = Edge.builder()
                        .firstVertex(currentV)
                        .secondVertex(currentVertexSet.get(i))
                        .build();
                currentV.getEdgesStart().add(edge);
                currentVertexSet.get(i).getEdgesEnd().add(edge);
                currentV = currentVertexSet.get(i);
            }
        }
    }

    private static boolean generateCenter(Warehouse warehouse, Long currentRackNumber) {
        Long currentX = warehouse.getRackHeight() * 2;
        Long currentY = warehouse.getRackHeight() * 2;
        Long maxY = warehouse.getHeight() - warehouse.getRackHeight();
        boolean hasLastRight = false;
        while (currentX <= warehouse.getWidth() - 3 * warehouse.getRackHeight()) {
            hasLastRight = false;
            while (currentY <= maxY - warehouse.getRackWidth()) {
                createRack(currentRackNumber, "left", currentX, currentY, warehouse);
                currentRackNumber++;
                currentX += warehouse.getRackHeight();
                if (currentX <= warehouse.getWidth() - 3 * warehouse.getRackHeight()) {
                    createRack(currentRackNumber, "right", currentX, currentY, warehouse);
                    currentRackNumber++;
                    hasLastRight = true;
                }

                currentY += warehouse.getRackWidth();
                currentX -= warehouse.getRackHeight();
            }
            createVerticesInTopAnDownLines(warehouse, currentX - warehouse.getRackHeight() / 2.0);
            if (currentX <= warehouse.getWidth() - 4 * warehouse.getRackHeight()) {
                createVerticesInTopAnDownLines(warehouse, currentX + warehouse.getRackHeight() * 5 / 2.0);
            }
            currentX += 3 * warehouse.getRackHeight();
            currentY = warehouse.getRackHeight() * 2;
        }
        return hasLastRight;
    }

    private static void createVerticesInTopAnDownLines(Warehouse warehouse, Double currentX) {
        warehouse.getVertices().add(Vertex.builder()
                .warehouse(warehouse)
                .x(currentX)
                .y(warehouse.getRackHeight() * 3 / 2.0)
                .edgesStart(new HashSet<>())
                .edgesEnd(new HashSet<>())
                .build());
        warehouse.getVertices().add(Vertex.builder()
                .warehouse(warehouse)
                .x(currentX)
                .y(warehouse.getHeight() - warehouse.getRackHeight() / 2.0)
                .edgesStart(new HashSet<>())
                .edgesEnd(new HashSet<>())
                .build());
    }

    private static Long generateLeft(Warehouse warehouse, Long currentRackNumber) {
        currentRackNumber = (warehouse.getHeight() - warehouse.getRackHeight()) / warehouse.getRackWidth();
        if ((warehouse.getHeight() - warehouse.getRackHeight()) % warehouse.getRackWidth() == 0) currentRackNumber--;
        long result = currentRackNumber;
        Long currentX = 0L;
        Long currentY = warehouse.getRackHeight();
        while (currentY + warehouse.getRackWidth() <= warehouse.getHeight()) {
            Rack rack = createRack(currentRackNumber, "right", currentX, currentY, warehouse);
            currentRackNumber--;
            currentY += warehouse.getRackWidth();
        }
        createVerticesInTopAnDownLines(warehouse, warehouse.getRackHeight() * 3 / 2.0);
        return result + 1;
    }


    private static Long generateRight(Warehouse warehouse, Long currentRackNumber) {
        Long currentX = warehouse.getWidth() - warehouse.getRackHeight();
        Long currentY = warehouse.getRackHeight();
        while (currentY + warehouse.getRackWidth() <= warehouse.getHeight()) {
            Rack rack = createRack(currentRackNumber, "left", currentX, currentY, warehouse);
            currentRackNumber++;
            currentY += warehouse.getRackWidth();
        }
        createVerticesInTopAnDownLines(warehouse, warehouse.getWidth() - warehouse.getRackHeight() * 3 / 2.0);
        return currentRackNumber;
    }

    private static Long generateTop(Warehouse warehouse, Long currentRackNumber) {
        Long currentX = warehouse.getRackHeight();
        Long currentY = 0L;
        while (currentX + warehouse.getRackWidth() <= warehouse.getWidth() - warehouse.getRackHeight()) {
            Rack rack = createRack(currentRackNumber, "down", currentX, currentY, warehouse);
            currentRackNumber++;
            currentX += warehouse.getRackWidth();
        }
        return currentRackNumber;
    }

    private static Rack createRack(Long number, String direction, Long x, Long y, Warehouse warehouse) {
        Rack rack = null;
        if ("down".equals(direction)) {
            rack = new Rack(null,
                    number, x, y, direction,
                    Vertex.builder()
                            .x(x + warehouse.getRackWidth() / 2.0)
                            .y(y + warehouse.getRackHeight() * 3 / 2.0)
                            .edgesEnd(new HashSet<>())
                            .edgesStart(new HashSet<>())
                            .warehouse(warehouse)
                            .build(), new HashSet<>(),
                    warehouse);
        } else if ("up".equals(direction)) {
            rack = new Rack(null,
                    number, x, y, direction,
                    Vertex.builder()
                            .x(x + warehouse.getRackWidth() / 2.0)
                            .y(y - warehouse.getRackHeight() / 2.0)
                            .edgesEnd(new HashSet<>())
                            .edgesStart(new HashSet<>())
                            .warehouse(warehouse)
                            .build(), new HashSet<>(),
                    warehouse);
        } else if ("right".equals(direction)) {
            rack = new Rack(null,
                    number, x, y, direction,
                    Vertex.builder()
                            .x(x + warehouse.getRackHeight() * 3 / 2.0)
                            .y(y + warehouse.getRackWidth() / 2.0)
                            .edgesEnd(new HashSet<>())
                            .edgesStart(new HashSet<>())
                            .warehouse(warehouse)
                            .build()
                    , new HashSet<>(),
                    warehouse);
        } else {
            rack = new Rack(null,
                    number, x, y, direction,
                    Vertex.builder()
                            .x(x - warehouse.getRackHeight() / 2.0)
                            .y(y + warehouse.getRackWidth() / 2.0)
                            .edgesEnd(new HashSet<>())
                            .edgesStart(new HashSet<>())
                            .warehouse(warehouse)
                            .build()
                    , new HashSet<>(),
                    warehouse);
        }
        rack.getVertex().setRack(rack);
        warehouse.getRacks().add(rack);
        warehouse.getVertices().add(rack.getVertex());
        return rack;
    }

    private Warehouse setDimensionsToNewWarehouse(Long id, Warehouse warehouse) throws Exception {
        Warehouse oldWarehouse = warehouseRepository.findById(id).orElseThrow(() -> new Exception("Warehouse not found"));
        oldWarehouse.setHeight(warehouse.getHeight());
        oldWarehouse.setWidth(warehouse.getWidth());
        oldWarehouse.setRackHeight(warehouse.getRackHeight());
        oldWarehouse.setRackWidth(warehouse.getRackWidth());
        return oldWarehouse;
    }

    private void validateWarehouseDimensions(Warehouse warehouse) throws Exception {
        if (warehouse.getHeight() < 0 || warehouse.getWidth() < 0 || warehouse.getRackHeight() < 0 || warehouse.getRackWidth() < 0) {
            throw new Exception("Dimension is < 0");
        }
        if (warehouse.getRackHeight() > warehouse.getHeight() * 0.5 || warehouse.getRackWidth() > warehouse.getWidth() * 0.5) {
            throw new Exception("Rack width or height cannot exceed 50% of warehouse dimension.");
        }
        if (warehouse.getRackHeight() > warehouse.getRackWidth()) {
            throw new Exception("Rack width must be >= rack height");
        }
    }


    private void validateWarehouse(User user, Warehouse warehouse) throws Exception {
        nameValidation(user, warehouse.getName());
        addressValidation(warehouse.getAddress());
    }

    private void nameValidation(User user, String name) throws Exception {

        if (user.getWarehouses().stream().anyMatch(w -> w.getName().equals(name))) {
            throw new Exception(name + " is already exist");
        }

        String regex = "^[a-zA-Z0-9 .,-]{4,30}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);

        if (!matcher.matches()) {
            throw new Exception("Name is invalid (4-30 characters, letters, numbers ',.-' only)");
        }
    }

    private void addressValidation(String address) throws Exception {
        String regex = "^[a-zA-Z0-9 .,-]{5,30}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(address);

        if (!matcher.matches()) {
            throw new Exception("Address is invalid (5-30 characters, letters, numbers ',.-' only)");
        }
    }

    private static Vertex createEdge(Vertex lastVertex, Rack rack) {
        if (lastVertex == null) {
            lastVertex = rack.getVertex();
        } else {
            Edge edge = new Edge(null, lastVertex, rack.getVertex());
            rack.getVertex().getEdgesEnd().add(edge);
            lastVertex.getEdgesStart().add(edge);
            lastVertex = rack.getVertex();
        }
        return lastVertex;
    }
}

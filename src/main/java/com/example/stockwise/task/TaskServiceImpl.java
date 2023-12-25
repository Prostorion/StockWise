package com.example.stockwise.task;

import com.example.stockwise.graph.Edge;
import com.example.stockwise.graph.EdgeRepository;
import com.example.stockwise.graph.Vertex;
import com.example.stockwise.graph.VertexRepository;
import com.example.stockwise.items.item.ItemRepository;
import com.example.stockwise.items.itemPending.PendingItem;
import com.example.stockwise.items.itemToExport.ExportItem;
import com.example.stockwise.rack.Rack;
import com.example.stockwise.rack.RackRepository;
import com.example.stockwise.task.order.Order;
import com.example.stockwise.task.order.OrderRepository;
import com.example.stockwise.task.supply.Supply;
import com.example.stockwise.task.supply.SupplyRepository;
import com.example.stockwise.warehouse.Warehouse;
import com.example.stockwise.warehouse.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.AStarAdmissibleHeuristic;
import org.jgrapht.alg.shortestpath.AStarShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final WarehouseService warehouseService;
    private final EdgeRepository edgeRepository;
    private final VertexRepository vertexRepository;
    private final OrderRepository orderRepository;
    private final SupplyRepository supplyRepository;
    private final RackRepository rackRepository;
    private final ItemRepository itemRepository;

    @Override
    public List<List<Edge>> getPath(Long taskId, Long id, boolean isOrder) throws Exception {
        Warehouse warehouse = warehouseService.getWarehouseById(id);
        Graph<String, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        warehouse.getVertices().forEach(v -> graph.addVertex(v.getId().toString()));
        List<Edge> edgeSet = edgeRepository.findAllByFirstVertexWarehouseIdOrSecondVertexWarehouseIdOrderById(id, id);
        for (Edge e : edgeSet) {
            double distance = Math.sqrt(Math.pow(Math.abs(e.getFirstVertex().getX() - e.getSecondVertex().getX()), 2) +
                    Math.pow(Math.abs(e.getFirstVertex().getY() - e.getSecondVertex().getY()), 2));
            addWeightedEdge(graph, e.getFirstVertex().getId(), e.getSecondVertex().getId(), distance);
        }
        List<Vertex> list = warehouse.getVertices().stream().toList();
        AStarShortestPath<String, DefaultWeightedEdge> aStar = new AStarShortestPath<>(graph, new AStarAdmissibleHeuristic<>() {
            @Override
            public double getCostEstimate(String sourceVertex, String targetVertex) {
                Vertex first = list.stream().filter(v -> v.getId().toString().equals(sourceVertex)).findAny().orElseThrow();
                Vertex second = list.stream().filter(v -> v.getId().toString().equals(targetVertex)).findAny().orElseThrow();

                return Math.abs(first.getX() - second.getX()) +
                        Math.abs(first.getY() - second.getY());
            }
        });
        List<Optional<Rack>> racks;

        if (isOrder) {
            Order order = orderRepository.findById(taskId).orElseThrow();
            racks = order.getItems().stream().map(PendingItem::getRackNumber).map((Long number) -> rackRepository.findRackByNumberAndWarehouseId(number, id)).toList();

        } else {
            Supply supply = supplyRepository.findById(taskId).orElseThrow();
            racks = supply.getItems().stream()
                    .map(ExportItem::getItem_id)
                    .map(itemRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(i -> rackRepository.findRackByNumberAndWarehouseId(i.getRack().getNumber(), id)).toList();
        }
        List<String> vertices = new ArrayList<>();
        vertices.add(warehouse.getVertices().stream().filter(v -> "true".equals(v.getStart())).findAny().orElseThrow().getId().toString());


        for (Optional<Rack> rack : racks) {
            rack.ifPresent(r -> vertices.add(r.getVertex().getId().toString()));
        }

        var pathList = SimpleGA.optimize(aStar, vertices);
        List<List<Edge>> result = new ArrayList<>();
        for (var path : pathList) {
            List<String> vertexList = path.getVertexList();
            List<Edge> currentResult = new ArrayList<>();
            for (int i = 0; i < vertexList.size() - 1; i++) {
                currentResult.add(Edge.builder()
                        .firstVertex(vertexRepository.findById(Long.parseLong(vertexList.get(i))).orElseThrow())
                        .secondVertex(vertexRepository.findById(Long.parseLong(vertexList.get(i + 1))).orElseThrow())
                        .build());


            }
            result.add(currentResult);
        }
        result.removeIf(Objects::isNull);
        return result;
    }

    private static void addWeightedEdge(Graph<String, DefaultWeightedEdge> graph, Long source, Long target, double weight) {
        if (!graph.containsEdge(source.toString(), target.toString())) {
            DefaultWeightedEdge edge = graph.addEdge(source.toString(), target.toString());
            graph.setEdgeWeight(edge, weight);
        }

    }
}



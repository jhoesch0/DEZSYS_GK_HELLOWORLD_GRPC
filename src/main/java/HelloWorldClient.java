import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class HelloWorldClient {

    public static void main(String[] args) {

        String firstname = args.length > 0 ? args[0] : "Jonas";
        String lastname = args.length > 1 ? args[1] : "Hösch";

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        HelloWorldServiceGrpc.HelloWorldServiceBlockingStub stub = HelloWorldServiceGrpc.newBlockingStub(channel);

        Hello.HelloResponse helloResponse = stub.hello(Hello.HelloRequest.newBuilder()
                .setFirstname(firstname)
                .setLastname(lastname)
                .build());
        System.out.println(helloResponse.getText());



        System.out.println("\nSending WarehouseRecord...\n");

        // Build product list
        Hello.ProductData p1 = Hello.ProductData.newBuilder()
                .setProductId(101)
                .setProductName("Laptop Pro 15")
                .setPrice(1499.99)
                .setQuantity(3)
                .build();

        Hello.ProductData p2 = Hello.ProductData.newBuilder()
                .setProductId(102)
                .setProductName("Mouse Wireless")
                .setPrice(29.99)
                .setQuantity(10)
                .build();

        // Build warehouse record
        Hello.WarehouseRecord warehouse = Hello.WarehouseRecord.newBuilder()
                .setWarehouseID("W-001")
                .setWarehouseName("Main Warehouse Europe")
                .setTimestamp("2025-12-02T10:15:00Z")
                .addProductDataList(p1)
                .addProductDataList(p2)
                .build();

        // Call RPC
        Hello.HelloResponse warehouseResponse = stub.sendWarehouse(warehouse);
        System.out.println("Server Response: " + warehouseResponse.getText());

    }
}

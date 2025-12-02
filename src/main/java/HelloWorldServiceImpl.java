import io.grpc.stub.StreamObserver;

public class HelloWorldServiceImpl extends HelloWorldServiceGrpc.HelloWorldServiceImplBase {

    @Override
    public void hello(Hello.HelloRequest request, StreamObserver<Hello.HelloResponse> responseObserver) {

        System.out.println("Handling hello endpoint: " + request.toString());

        String text = "Hello World, " + request.getFirstname() + " " + request.getLastname();
        Hello.HelloResponse response = Hello.HelloResponse.newBuilder().setText(text).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }

    @Override
    public void sendWarehouse(Hello.WarehouseRecord request,
                              StreamObserver<Hello.HelloResponse> responseObserver) {

        System.out.println("Handling sendWarehouse endpoint: ");
        System.out.println("Warehouse ID: " + request.getWarehouseID());
        System.out.println("Warehouse Name: " + request.getWarehouseName());
        System.out.println("Timestamp: " + request.getTimestamp());
        System.out.println("Products:");

        for (Hello.ProductData p : request.getProductDataListList()) {
            System.out.println("- " + p.getProductName() +
                    " (ID " + p.getProductId() +
                    ", Price " + p.getPrice() +
                    ", Qty " + p.getQuantity() + ")");
        }

        String summary = "Warehouse '" + request.getWarehouseName()
                + "' received with " + request.getProductDataListCount()
                + " products.";

        Hello.HelloResponse response = Hello.HelloResponse.newBuilder()
                .setText(summary)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
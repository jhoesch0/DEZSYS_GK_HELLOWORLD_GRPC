# Middleware Engineering "DEZSYS_GK_HELLOWORLD_GRPC"

## Aufgabenstellung

The task involves creating a simple HelloWorld application using the gRPC framework, including defining a Proto file and implementing a gRPC server and client. Additionally, the service should be extended to transfer a simple DataWarehouse record.

## Implementierung

Start HelloWorldServer (Java)  
`gradle clean build`  
`gradle runServer`

Start HelloWorldClient (Java)  
`gradle runClient`

-------------------------------- Python 

Add grpcio packages  
`pip3 install grpcio grpcio-tools`  

Compile .proto file  
`python3 -m grpc_tools.protoc -I src/main/proto  
  --python_out=src/main/resources  
  --grpc_python_out=src/main/resources  
  src/main/proto/hello.proto`  

Start HelloWorldClient (Python)  
`python3 src/main/resources/helloWorldClient.py`  

- What is gRPC and why does it work accross languages and platforms?
  
  gRPC (Google Remote Procedure Call) is an open-source, RPC framework developed by Google. It allows services running on different machines and written in different languages to communicate with each other.
  
  It provides code generation for multiple language and it also uses Protocol Buffers as the interface definition language.

- Describe the RPC life cycle starting with the RPC client?
  
  Client calls stub method. -> Stub serializes data (protobuf) and sends it. -> Server deserializes and executes the method. -> Server serializes response and sends it back. -> Client receives and deserializes the result.

- Describe the workflow of Protocol Buffers?

        Define data in a `.proto` file. Then Compile it with `protoc` to generate code. Use         generated code to create and send messages. Data is serialized/deserialized in         binary format.

- What are the benefits of using protocol buffers?

        Extremely fast serialization, Backward compatibility, Strong type safety, Automatic         code generation, Multilingual support

- When is the use of protocol not recommended?

        When human-readable data is required (JSON is better)   

        Very small projects where schema compilation adds complexity

- List 3 different data types that can be used with protocol buffers?
  
  string, int32 / int64, double  

### Changes

In the Proto File I added a methode that takes a WarehouseRecord and returns a HelloResponse.

```protobuf
service HelloWorldService {
  rpc hello(HelloRequest) returns (HelloResponse) {}
  rpc sendWarehouse(WarehouseRecord) returns (HelloResponse) {}
} 
```

`ProductData` defines a Product and `WarehouseRecord` defines a Warehouse Record from the exercise MidEng 7.1.

```protobuf
message ProductData {
  int32 productId = 1;
  string productName = 2;
  string productCategory = 3;
  int32 quantity = 4;
  double price = 5;
}
message WarehouseRecord {
  string warehouseID = 1;
  string warehouseName = 2;
  string timestamp = 3;
  repeated ProductData productDataList = 4;
}
```

In the Java client, the code was modified to create `ProductData` objects, assemble them into a `WarehouseRecord`, and send it to the gRPC server using the `sendWarehouse` method, then print the server’s response.

```java
 System.out.println("\nSending WarehouseRecord...\n");
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

        Hello.WarehouseRecord warehouse = Hello.WarehouseRecord.newBuilder()
                .setWarehouseID("W-001")
                .setWarehouseName("Main Warehouse Europe")
                .setTimestamp("2025-12-02T10:15:00Z")
                .addProductDataList(p1)
                .addProductDataList(p2)
                .build();

        Hello.HelloResponse warehouseResponse = stub.sendWarehouse(warehouse);
        System.out.println("Server Response: " + warehouseResponse.getText());

    }
```

This method handles the `sendWarehouse` RPC call by printing the warehouse and product details from the request, then creating and sending a summary response back to the client.

```java
    @Override
    public void sendWarehouse(Hello.WarehouseRecord request,StreamObserver<Hello.HelloResponse> responseObserver) {

        System.out.println("Handling sendWarehouse endpoint: ");
        System.out.println("Warehouse ID: " + request.getWarehouseID());
        System.out.println("Warehouse Name: " + request.getWarehouseName());
        System.out.println("Timestamp: " + request.getTimestamp());
        System.out.println("Products:");

       for (Hello.ProductData p : request.getProductDataListList()) {
    System.out.println("- " + p.getProductName() +
            " (ID " + p.getProductId() +
            ", Category " + p.getProductCategory() +
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
```

### EK

This Python script connects to a gRPC server, sends a `HelloRequest` with a first and last name, and prints the server response. Then it constructs a `WarehouseRecord` with product data and sends it to the server, printing the server's confirmation.

```python
import grpc
import sys

import hello_pb2
import hello_pb2_grpc


def main():
    firstname = sys.argv[1] if len(sys.argv) > 1 else "Jonas.py"
    lastname = sys.argv[2] if len(sys.argv) > 2 else "Hösch"

    with grpc.insecure_channel("localhost:50051") as channel:
        stub = hello_pb2_grpc.HelloWorldServiceStub(channel)

        hello_request = hello_pb2.HelloRequest(firstname=firstname, lastname=lastname)
        hello_response = stub.hello(hello_request)
        print(hello_response.text)

        print("\nSending WarehouseRecord...\n")
        p1 = hello_pb2.ProductData(
            productId=101,
            productName="Laptop Pro 15",
            price=1499.99,
            quantity=3
        )

        p2 = hello_pb2.ProductData(
            productId=102,
            productName="Mouse Wireless",
            price=29.99,
            quantity=10
        )

        warehouse = hello_pb2.WarehouseRecord(
            warehouseID="W-001",
            warehouseName="Main Warehouse Europe",
            timestamp="2025-12-02T10:15:00Z",
            productDataList=[p1, p2]
        )

        warehouse_response = stub.sendWarehouse(warehouse)
        print("Server Response: " + warehouse_response.text)


if __name__ == "__main__":
    main()

```

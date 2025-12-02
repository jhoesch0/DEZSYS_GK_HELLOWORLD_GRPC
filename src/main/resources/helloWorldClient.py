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

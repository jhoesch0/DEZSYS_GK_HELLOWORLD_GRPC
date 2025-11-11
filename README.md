# Middleware Engineering "DEZSYS_GK_HELLOWORLD_GRPC"

## Aufgabenstellung

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

- 

- When is the use of protocol not recommended?

- List 3 different data types that can be used with protocol buffers?

## Quellen

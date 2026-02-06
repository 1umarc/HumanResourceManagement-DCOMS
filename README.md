# HumanResourceManagement-DCOMS

## How to Build & Run


### Linux (Terminal)
Install Maven
`sudo apt install maven`

`./run_nodes.sh`

The Script has a temporary fix to fully kill the running java processes (only on start up of the script so the processes will still be running but you could kill it after you finish running the project with `pkill java`)

### Windows (Terminal)
Install Maven (Online)

Build The Project with Maven
`mvn clean package`

Starts up the Registry Node (Max 1)
`mvn exec:java -Dexec.mainClass=src.main.java.nodes.RegistryNode -Dexec.args="5000"`

Wait for Registry Node to start up Before The Rest

Starts up Server Nodes (New Terminal For Every New Node - Change the server port `####` ex. 6000 for each)
`mvn exec:java -Dexec.mainClass=src.main.java.nodes.ServerNode -Dexec.args="5000 ####"`

Starts up Client Nodes (New Terminal For Every New Node)
`mvn exec:java -Dexec.mainClass=src.main.java.nodes.ClientNode -Dexec.args="5000"`

Kill Processes by `Ctrl+C` or Closing the Terminal

## Directory Structure

.
├── README.md
├── pom.xml (Maven Structure)
├── run_nodes.sh (Run Nodes in one Command)
├── Client (View + View RMI Implementations (Doesnt really have RMI Implementations))
│   └── Luven.java
├── DataAbstractions (Model Logic)
│   ├── Item.java
│   ├── LeaveApplication.java (Concrete Item)
│   ├── User.java (Concrete Item)
│   ├── ItemCollection.java (A collection/Array of Items (Concrete Items like User and Leave Application) With Utility Function)
│   ├── ItemCollectionFactory.java (Creates a Type of ItemCollection Iff have Permission)
│   └── base (Not visible to System User - Only Used By Item, Children of Item, ItemCollection, and ItemCollectionFactory)
│       ├── DataReader.java (Interface)
│       ├── DataWriter.java (Interface)
│       ├── FileHandler.java (Implements Reader and Writer)
│       ├── ItemFactory.java (Creates Concrete Items)
├── Database (Model + Model RMI Implementation)
│   └── Jonathan.java
├── RMI_Communication (Shared Interfaces or Classes - mostly for RMI)
│   ├── ClientToServer.java
│   ├── DatabaseToServer.java
│   ├── RMI.java (Uniforms the RMI Serialization, Deserialization and also RMI calls)
│   ├── ServerToClient.java
│   └── ServerToDatabase.java
├── Server (Controller + Controller RMI Implementation)
│   ├── Chunkit.java
│   └── Meekail.java
├── nodes (Main Executables - Hosts Remote Interfaces/Implementations onto RMI or Calls Main View Function)
│   ├── ClientNode.java
│   ├── DatabaseNode.java
│   ├── RegistryNode.java
│   └── ServerNode.java
└── resources (Data)
    └── User.txt

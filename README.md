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
`mvn exec:java -Dexec.mainClass=nodes.RegistryNode -Dexec.args="5000"`

Wait for Registry Node to start up Before The Rest

Starts up Server Nodes (New Terminal For Every New Node - Change the server port `####` ex. 6000 for each)
`mvn exec:java -Dexec.mainClass=nodes.ServerNode -Dexec.args="5000 ####"`

Starts up Client Nodes (New Terminal For Every New Node)
`mvn exec:java -Dexec.mainClass=nodes.ClientNode -Dexec.args="5000"`

Kill Processes by `Ctrl+C` or Closing the Terminal

## Directory Structure

.
├── README.md
├── pom.xml (Maven Structure)
├── run_nodes.sh (Run Nodes in one Command)
├── Client (View + View RMI Implementations (Doesnt really have RMI Implementations))
│   └── Luven.java
├── Database (Model + Model RMI Implementation)
│   ├── FileHandler.java (Implementation of DatabaseInterface)
│   └── resources (Data)
│       ├── User.txt
│       └── LeaveApplication.txt
├── Server (Controller + Controller RMI Implementation)
│   ├── DataLogic (Database Logic)
│   │   ├── ItemCollectionFactory.java (Creates a Type of ItemCollection)
│   │   ├── ItemCollection.java (A collection/Array of Items With Utility Function)
│   │   └── Item.java (A Data Transfer Object That Can Hold Any Data ex. Leave Application and User)
│   ├── Chunkit.java
│   └── Meekail.java
├── Shared_Interfaces (Shared Interfaces or Classes - mostly for RMI)
│   ├── DatabaseToServer
│   │   ├── DatabaseInterface.java (Remote Interface Only Used in ItemCollection)
│   ├── ServerToClient
│   │   ├── AuthInterface.java (Authentication Logic)
│   │   ├── LeaveInterface.java (Leave Application Logic)
│   │   └── ProfileInterface.java (Profile Logic)
│   └── RMIBind.java (Uniforms the RMI Serialization, Deserialization and also RMI calls)
├── nodes (Main Executables - Hosts Remote Interfaces/Implementations onto RMI or Calls Main View Function)
    ├── ClientNode.java
    ├── DatabaseNode.java
    ├── RegistryNode.java
    └── ServerNode.java

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

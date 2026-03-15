/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;

import java.io.BufferedReader;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.FileReader;
import Shared_Interfaces.DatabaseToServer.DatabaseInterface;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author JONATHAN
 */
public class FileHandler extends UnicastRemoteObject implements DatabaseInterface {
    private String FileName;
    private FileWriter writer;
    private BufferedReader reader;
    private List<List<String>> data = new ArrayList<>();
    private List<String> fields = new ArrayList<>();

    public FileHandler() throws RemoteException {
        super();
    }
    
    private String parseFilePath(String Type) {
        String currentWorkingDirectory = System.getProperty("user.dir");
        
        return currentWorkingDirectory + "/src/main/java/Database/resources/" + Type + ".txt";
    }
    
    private String parseData(List<String> rowData) { 
        String parsedRowData = "";

        for (String currentData : rowData) {
            parsedRowData += currentData + ";";
        }
        
        return parsedRowData;
    }
    
    private void openFileWriter(String FilePath) {
        try {
            this.writer = new FileWriter(FilePath);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    
    private void closeFileWriter() {
        try {
            this.writer.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    
    private void openFileReader(String FilePath) {
        try {
            FileReader fileReader = new FileReader(FilePath);
            
            this.reader = new BufferedReader(fileReader);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    
    private void closeFileReader() {
        try {
            this.reader.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    
    private List<List<String>> sortData(List<List<String>> Data) {
        List<List<String>> sortedData = new ArrayList<>(Data);
        sortedData.sort( (a, b) -> { return a.get(0).compareTo(b.get(0)); } ); // Sorts ID Alphabetically
        return sortedData;
    }

    @Override
    public synchronized final Boolean setFileName(String FileName) {
        try {
            String FilePath = this.parseFilePath(FileName);
            File file = new File(FilePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            this.FileName = FileName;
            
        } catch (IOException e) {
            System.out.println("File Not Found!");
            return false;
        }

        this.getFieldAndData();
        return true;
    }
    
    @Override
    public synchronized String getFileName() {
        return this.FileName;
    }

    @Override
    public synchronized Boolean writeData(List<List<String>> Items) {
        Items = this.sortData(Items);
        
        try {
            String FilePath = this.parseFilePath(this.FileName);
            this.openFileWriter(FilePath);
            
            String writeBuffer = this.parseData(this.fields) + "\n";
            for (List<String> rowData : Items) {
               writeBuffer += this.parseData(rowData) + "\n";
            }
            
            this.writer.write(writeBuffer);
            
            this.closeFileWriter();
            
            return true;
            
        } catch (IOException e) {
            System.out.println(e);
            return false;
        }
    }

    private void getFieldAndData() {
        List<List<String>> Data = new ArrayList<>();
        
        String FilePath = this.parseFilePath(this.FileName);
        
        this.openFileReader(FilePath);
        
        try {
            String unparsedData;
            if ( ( unparsedData = this.reader.readLine()) != null) {

                this.fields = Arrays.asList(unparsedData.split(";"));
            } else {return;}

            while ( ( unparsedData = this.reader.readLine()) != null ) {
                List<String> rowData = Arrays.asList(unparsedData.split(";"));
                
                List<String> MutableRowData = new ArrayList<>(rowData);
                
                if (MutableRowData.isEmpty()) {
                    continue;
                }

                Data.add(MutableRowData);
            }

            this.data = Data;
        } catch (IOException e) {
            System.out.println(e);
        }
        
        this.closeFileReader();
    }

    @Override
    public synchronized List<List<String>> getData() {
        return this.data;
    }

    @Override
    public synchronized List<String> getFieldName() {
        return this.fields;
    }

}

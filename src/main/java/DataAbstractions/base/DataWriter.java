/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package src.main.java.DataAbstractions.base;

import java.util.List;

/**
 *
 * @author JONATHAN
 */
public interface DataWriter {
    public Boolean setFileName(String filePath);
    public String getFileName();
    public Boolean writeData(List<List<String>> Data);
    public Boolean appendData(List<String> Data);
    public Boolean updateData(List<String> Data);
    public Boolean deleteData(String ID);
    public Boolean updateCompositeData(List<String> Data, List<String> Keys);
    public Boolean deleteCompositeData(List<String> Keys);
}

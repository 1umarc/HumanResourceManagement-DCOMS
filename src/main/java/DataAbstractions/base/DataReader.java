/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataAbstractions.base;


import java.util.List;

/**
 *
 * @author JONATHAN
 */
public interface DataReader {
    public Boolean setFileName(String filePath);
    public String getFileName();
    public List<List<String>> getData();
    public List<String> getRow(String Key);
    public List<String> getCompositeRow(List<String> Keys);
    public List<String> getColumn(String Field);
    public List<String> getFieldName();
    public List<List<String>> FitlerData(List<String> Field, List<String> Value);
    public List<List<String>> FitlerData(String Field, List<String> Value);
    public List<List<String>> FitlerData(String Field, String Value);
}

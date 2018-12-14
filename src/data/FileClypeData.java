package data;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.*;
import java.nio.file.Files;

import javafx.scene.image.Image;

import javax.imageio.ImageIO;

/**
 * Child of ClypeData
 *
 * @see ClypeData
 * @author Henry Cadoff
 */
public class FileClypeData extends ClypeData {

    private String fileName;
    private String fileContents;
    private byte[] fc;

    /**
     * Calls the ClypeData super constructor
     * FileContents is set to "null"
     *
     * @param userName  Name of the User
     * @param fileName  Name of the file being sent
     * @param type      The type code number
     */
    public FileClypeData(String userName, String fileName, int type){
        super(userName, type);
        this.fileName = fileName;
        fileContents = null;
    }

    /**
     * Calls the ClypeData default constructor
     * Default constructor
     * Sets fileName and fileContents to null;
     */
    public FileClypeData(){
        super();
        fileName = null;
        fileContents = null;
    }

    /**
     * Lets user change the fileName
     *
     * @param fileName  Name of the file being sent
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     *
     * @return  fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Currently empty
     */
    public void readFileContents(){
        try {
            fileContents = "";
            FileReader read = new FileReader(fileName);
            BufferedReader buff = new BufferedReader(read);
            String line;
            while ((line = buff.readLine()) != null) {
                fileContents += line;
            }
            buff.close();
            read.close();
        }
        catch (FileNotFoundException nf){
            System.out.println("File Not Found");
        }
        catch(IOException e){
            System.out.println("There was a problem reading the file");
        }
    }

    public void readFileContents(String Key){
        try {
            fileContents = "";
            FileReader read = new FileReader(fileName);
            BufferedReader buff = new BufferedReader(read);
            String line;
            while ((line = buff.readLine()) != null) {
                fileContents += encrypt(line, Key);
            }
            buff.close();
            read.close();
        }
        catch (FileNotFoundException nf){
            System.out.println("File Not Found");
        }
        catch(IOException e){
            System.out.println("There was a problem reading the file");
        }
    }

    /**
     * Currently empty
     */
    public void writeFileContents(){
        try {
            File f = new File(fileName);
            fc = Files.readAllBytes(f.toPath());

        }
        catch (IOException e){
            System.err.println(e);
        }
    }

    public void writeFileContents(String Key){
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(decrypt(fileContents, Key));
            writer.close();
        }
        catch (FileNotFoundException nf){
            System.out.println("File Not Found");
        }
        catch (IOException e){
            System.out.println("There was a problem writing to the file");
        }
    }

    /**
     *@return hashCode
     */
    @Override
    public int hashCode() {
        int result = 31;
        result = 17 * result + this.getType();
        result = 17 * result + fileName.hashCode() + fileContents.hashCode() + getUserName().hashCode();
        return result;
    }

    /**
     * Calls: getUserName(), getFileName(), getType()
     *
     * @return String containing: userName, fileName, type
     */
    @Override
    public String toString() {
        return "User Name: " + getUserName() + "\nFile Name: " + getFileName() + "\nFile Contents: " + fileContents
                + "\nType: " + getType();
    }

    /**
     * Checks if userName, fileName, fileCOntents and type are equal
     *
     * @param obj   a FileClypeData object to compare to
     * @return if equal
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if ( !(obj instanceof FileClypeData)) return false;
        FileClypeData other = (FileClypeData) obj;
        return this.getUserName().equals(other.getUserName()) && this.fileName.equals(other.fileName)
                && this.fileContents.equals(other.fileContents) && this.getType() == other.getType();
    }

    /**
     * Unimplemented
     *
     * @return null
     */
    @Override
    public String getData(String Key) {
        return decrypt(fileContents, Key);
    }

    @Override
    public Object getData() {
        return fc;
    }
}

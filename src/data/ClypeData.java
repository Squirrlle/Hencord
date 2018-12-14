package data;

import java.util.Date;
import java.io.Serializable;

/**
 *
 * @author Jacob Burritt
 *
 */

public abstract class ClypeData implements Serializable {
    private static final int DEFAULT_TYPE = 1;
    private static final long serialVersionUID = 1L;
    private String userName;
    private int type;
    private Date date;

    public static final int LOGOUT = 0;
    public static final int LIST_USERS = 1;
    public static final int MESSAGE = 2;
    public static final int FILE = 3;


    /**
     * Constructor for ClypeData object
     * @param userName
     * @param type
     */
    public ClypeData(String userName, int type) {
        this.userName = userName;
        this.type = type;
        this.date = new Date();
    }

    /**
     * Constructor for ClypeData object
     * @param type
     */
    public ClypeData(int type) {
        this("Anon", type);
    }

    /**
     * Constructor for ClypeData object
     */
    public ClypeData() {
        this(DEFAULT_TYPE);
    }

    /**
     * Getter function for the private variable "type".
     * @return
     */
    public int getType() {
        return type;
    }

    /**
     * Getter function for the private variable "userName".
     * @return
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Getter function for the private variable "date".
     * @return
     */
    public Date getDate() {
        return date;
    }

    /**
     * Abstract method to be implemented in child classes.
     * @return
     */
    public abstract Object getData();

    public abstract String getData(String key);

    protected String encrypt(String inputStringToEncrypt, String key) {
        String result = "";
        inputStringToEncrypt = inputStringToEncrypt.toUpperCase();
        for (int i = 0, j = 0; i < inputStringToEncrypt.length(); i++) {
            char c = inputStringToEncrypt.charAt(i);
            if (c < 'A' || c > 'Z') {
                result += (char)(c);
                continue;
            }
            result += (char)((c + key.charAt(j) - 2 * 'A') % 26 + 'A');
            j = ++j % key.length();
        }
        return result;
    }

    protected String decrypt(String inputStringToDecrypt, String key) {
        String result = "";
        inputStringToDecrypt = inputStringToDecrypt.toUpperCase();
        for (int i = 0, j = 0; i < inputStringToDecrypt.length(); i++) {
            char c = inputStringToDecrypt.charAt(i);
            if (c < 'A' || c > 'Z') {
                result += (char)(c);
                continue;
            }
            result += (char)((c - key.charAt(j) + 26) % 26 + 'A');
            j = ++j % key.length();
        }
        return result;
    }

    public long getSerialVersionUID() {
        return serialVersionUID;
    }

}

package data;

/**
 * Subclass of ClypeData used to handle Message Data
 * @author Jacob Burritt
 *
 */

public class MessageClypeData extends ClypeData {
    private String message;

    /**
     * Constructor used for MessageClypeData object that also encrypts the message
     * @param userName
     * @param message
     * @param key
     * @param type
     */

    public MessageClypeData(String userName, String message, String key, int type) {
        super(userName,type);
        this.message = super.encrypt(message, key);
    }

    /**
     * Constructor used for MessageClypeData object
     * @param userName
     * @param message
     * @param type
     */

    public MessageClypeData(String userName, String message, int type){
        super(userName,type);
        if(message.contains(":)")){
            this.message = message.replace(":)", "😊");
        }
        else {
            this.message = message;
        }
    }

    /**
     * Default Constructor used for MessageClypeData object
     */
    public MessageClypeData(){
        super();
        this.message = "NULL";
    }

    /**
     * Getter function used to access the private variable, "message"
     */
    public String getData(){
        return message;
    }

    public String getData(String key) {
        return super.decrypt(message, key);
    }

    @Override
    public int hashCode() {
        final int prime = 59;
        int result = 1;
        result = prime * result + ((message == null) ? 0 : message.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if( other == null ) return false;
        if( !(other instanceof MessageClypeData) ) return false;
        MessageClypeData f = ( MessageClypeData )other;
        return this.message.equals( f.message ) && super.equals( f );
    }

    @Override
    public String toString() {
        return(getUserName() + ": " + getData() + '\n');
    }

}

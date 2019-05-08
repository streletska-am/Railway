package util;

import java.util.ResourceBundle;

public class Message {
    private static Message INSTANCE;
    private static ResourceBundle bundle;
    private static final String BUNDLE_NAME = "message";

    public static final String SERVLET_EXCEPTION = "message.servlet";
    public static final String IO_EXCEPTION = "message.io";
    public static final String EXCEPTION = "message.exception";
    public static final String PAGE_IS_NULL = "message.nullpage";


    private Message(){
        bundle = ResourceBundle.getBundle(BUNDLE_NAME);
    }

    public static Message getInstance(){
        if(INSTANCE == null){
            synchronized (Message.class){
                if (INSTANCE == null){
                    INSTANCE = new Message();
                }
            }
        }

        return INSTANCE;
    }

    public String getMessage(String parameter){
        return bundle.getString(parameter);
    }
}

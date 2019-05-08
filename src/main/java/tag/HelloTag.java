package tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.ResourceBundle;

public class HelloTag extends TagSupport {
    private static final String BUNDLE_MESSAGE = "navbar";
    private ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_MESSAGE);
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int doStartTag() throws JspException {
        try {
            pageContext.getOut().write(bundle.getString("navbar.hello") + " " + name);
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
    }
}

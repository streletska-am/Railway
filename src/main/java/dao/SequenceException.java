package dao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SequenceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String message;

    public SequenceException(String message) {
        this.message = message;
    }
}

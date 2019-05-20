package dao;

public interface SequenceDao {

    Long getNextSequenceId() throws SequenceException;
}

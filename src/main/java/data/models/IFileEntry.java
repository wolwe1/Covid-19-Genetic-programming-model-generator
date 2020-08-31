package data.models;

public interface IFileEntry {
    IFileEntry MakeCopy(String[] data) throws Exception;
}

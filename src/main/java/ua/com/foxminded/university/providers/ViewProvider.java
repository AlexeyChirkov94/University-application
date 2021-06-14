package ua.com.foxminded.university.providers;

public interface ViewProvider {

    void printMessage(String message);

    String read();

    int readInt();

    long readLong();

}

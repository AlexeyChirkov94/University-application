package ua.com.foxminded.university.providers;

import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class ViewProviderImpl implements ViewProvider{

    @Override
    public void printMessage(String message) {
        System.out.println(message);
    }

    @Override
    public String read() {
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    @Override
    public int readInt() {
        Scanner sc = new Scanner(System.in);
        return sc.nextInt();
    }

    @Override
    public long readLong() {
        Scanner sc = new Scanner(System.in);
        return sc.nextLong();
    }

}

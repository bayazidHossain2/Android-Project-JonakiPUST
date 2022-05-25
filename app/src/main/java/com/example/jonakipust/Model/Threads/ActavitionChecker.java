package com.example.jonakipust.Model.Threads;

import com.example.jonakipust.Database.FirebaseHelper;

public class ActavitionChecker extends Thread{
    public static boolean stop = false;
    public static ActavitionChecker checker;

    public static ActavitionChecker getInstance(){
        if (checker == null){
            checker = new ActavitionChecker();
        }else{
            stop = true;
            try {
                sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stop = false;
        }
        return checker;
    }

    @Override
    public void run() {
        while (!stop) {
            //FirebaseHelper.isConnected();
            try {
                sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

package com.example.myapplication.SignUp;

import com.example.myapplication.SignUp.User;

import java.util.ArrayList;

public class UserDetails {

    public static User details(ArrayList <User> users ,String email)
    {
        for(int i = 0; i < users.size(); i++)
        {
           if(users.get(i).getM_email().equals(email))
               return users.get(i);
        }
             return null;
    }

    public static ArrayList<User> lookForCollection(ArrayList<User> users , String lookfor) {
        ArrayList<User> users1 = new ArrayList<>();
        switch (lookfor) {
            case "man looking for women": {
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getM_genderLookFor().equals("woman looking for men")) {
                        users1.add(users.get(i));
                    }
                }
                break;
            }
            case "woman looking for men": {
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getM_genderLookFor().equals("man looking for women")) {
                        users1.add(users.get(i));
                    }
                }
                break;
            }
            case "woman looking for women": {
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getM_genderLookFor().equals("woman looking for women")) {
                        users1.add(users.get(i));
                    }
                }
                break;
            }
            case "man looking for men": {

                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getM_genderLookFor().equals("man looking for men")) {
                        users1.add(users.get(i));
                    }
                }
                break;
            }
        }
        return users1;

    }
}

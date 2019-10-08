package a33y.jo.gazinotlar.Listeners;

import java.util.List;

import a33y.jo.gazinotlar.Models.User;

public interface UserListener{
    public void OnUsersAdded();
    public void OnSearchFinished(List<User> users);
    public void OnUserSent();
    public void OnUserFound(User user);

}

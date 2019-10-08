package a33y.jo.gazinotlar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import a33y.jo.gazinotlar.Adapters.UserListAdapter;
import a33y.jo.gazinotlar.Helpers.DataHelper;
import a33y.jo.gazinotlar.Helpers.Helper;
import a33y.jo.gazinotlar.Listeners.ChatListeners;
import a33y.jo.gazinotlar.Listeners.UserListener;
import a33y.jo.gazinotlar.Models.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConvFrag extends Fragment implements ChatListeners,UserListener{

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    LinearLayout loading;
    UserListAdapter adapter;

    public ConvFrag() {
        DataHelper.addUserListener(this);
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_conv,container,false);
        recyclerView = v.findViewById(R.id.recycler);
        layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        loading =v.findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);

        return v;

    }


    @Override
    public void OnMessageSent(boolean isSuccessful) {

    }

    @Override
    public void OnMessagesReceived(boolean isSuccessful) {
        Helper.fillChatUsers();
        loading.setVisibility(View.GONE);
        adapter = new UserListAdapter(getContext(), User.getCurrentUser()!=null?User.getCurrentUser().getChatUsers():null,1);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void OnUsersAdded() {
        DataHelper.readMessages(this);
    }

    @Override
    public void OnSearchFinished(List<User> users) {

    }

    @Override
    public void OnUserSent() {

    }

    @Override
    public void OnUserFound(User user) {

    }
}

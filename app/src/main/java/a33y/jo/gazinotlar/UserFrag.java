package a33y.jo.gazinotlar;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.List;

import a33y.jo.gazinotlar.Adapters.UserListAdapter;
import a33y.jo.gazinotlar.Helpers.DataHelper;
import a33y.jo.gazinotlar.Listeners.UserListener;
import a33y.jo.gazinotlar.Models.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFrag extends Fragment implements UserListener{

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    LinearLayout loading;
    UserListAdapter adapter;
    EditText search;
    public UserFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_users, container, false);
        recyclerView = v.findViewById(R.id.recycler);
        DataHelper.addUserListener(this);
        DataHelper.readUsers();

        adapter = new UserListAdapter(getContext(), User.getUsers(),0);
        layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        loading =v.findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);
        search = v.findViewById(R.id.search);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               DataHelper.searchUsers(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
      if(!search.getText().equals(""))
          DataHelper.searchUsers(search.getText().toString().toLowerCase());

        return v;
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getActivity().getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(getContext());
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void OnUsersAdded() {
        loading.setVisibility(View.GONE);
        adapter = new UserListAdapter(getContext(), User.getUsers(),0);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void OnSearchFinished(List<User> users) {
        adapter = new UserListAdapter(getContext(), users,0);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void OnUserSent() {

    }

    @Override
    public void OnUserFound(User user) {

    }


}

package com.example.ecoleenligne.ui.Recommandation;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecoleenligne.MainActivity;
import com.example.ecoleenligne.Model.Notification;
import com.example.ecoleenligne.Model.Parent;
import com.example.ecoleenligne.R;
import com.example.ecoleenligne.ui.ListeEnfant.ListeEnfantViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class RecommandationFragment extends Fragment {

    private RecommandationViewModel RecommandationViewModel;
    private static final String TAG = "RecommandationFragment";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    String[] myArray;
    private ListeEnfantViewModel listeEnfantViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MainActivity activity = (MainActivity) getActivity();
        Boolean b=activity.getTypeUser();
        RecommandationViewModel =
                ViewModelProviders.of(this).get(RecommandationViewModel.class);
        View root = inflater.inflate(R.layout.fragment_recommandation, container, false);
        mAuth=FirebaseAuth.getInstance();

        if(b)
        {

            readDataParentNotification(new MyCallback() {
                @Override
                public void onCallback(Notification notification) {
                    if(notification!=null) {
                        myArray = new String[notification.getNotifs().size()];
                        notification.getNotifs().toArray(myArray);
                        Log.d(TAG, "onCallback: " + notification.getNotifs().size());
                        for (String phrase : myArray) {
                            Log.d(TAG, "myArray" + phrase);
                        }
                        RecyclerView recyclerView = root.findViewById(R.id.recyclerViewRecommandation);
                        RecyclerViewRecommendationAdapter adapter = new RecyclerViewRecommendationAdapter(getContext(), myArray);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }


                }
            });

        }


        Log.d(TAG, "onCreateView: I'm in");
        return root;
    }


    public interface MyCallback {
        void onCallback(Notification notification);

    }

    public void readDataParentNotification(MyCallback myCallback) {
        String id=mAuth.getCurrentUser().getUid();
        db=FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Notification").document("user "+id);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Notification notif = documentSnapshot.toObject(Notification.class);
                myCallback.onCallback(notif);
            }
        });
    }

    /*
    @Override
    public void onResume() {
        super.onResume();

        if(getView() == null){
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    // handle back button's click listener
                    System.exit(0);
                    return true;
                }
                return false;
            }
        });
    }

     */


}
